import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, throwError, map } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../models/user.model';
import { UserService } from './user.service';
import {environment} from "../../../environments/environment";
import {take} from "rxjs/operators";
import {TranslateService} from "@ngx-translate/core";

@Injectable({ providedIn: 'root' })
export class AuthService {
  private isAuthenticated = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.isAuthenticated.asObservable();

  private _loggedUser = new BehaviorSubject<User | null>(null);
  loggedUser$ = this._loggedUser.asObservable();
  private rememberMe = false;

  private readySubject = new BehaviorSubject<boolean>(false);
  ready$ = this.readySubject.asObservable();

  constructor(
      private http: HttpClient,
      private route: ActivatedRoute,
      private router: Router,
      private userService: UserService,
      private translate: TranslateService
  ) {
    this.loadRememberMe();
    this.restoreUser();
  }

  private loadRememberMe() {
    const hasLocalTokens = localStorage.getItem('accessToken') && localStorage.getItem('refreshToken');
    const hasSessionTokens = sessionStorage.getItem('accessToken') && sessionStorage.getItem('refreshToken');

    if (hasLocalTokens) {
      this.rememberMe = true;
    } else if (hasSessionTokens) {
      this.rememberMe = false;
    }
  }

  private restoreUser() {
    const userUrn = localStorage.getItem('userUrn') || sessionStorage.getItem('userUrn');
    if (userUrn) {
      this.fetchAndSetUser(userUrn);
    } else {
      this.isAuthenticated.next(false);
      this.readySubject.next(true);
    }
  }




  // El getter de loggedUser ahora devuelve el observable del BehaviorSubject
  getloggedUser() {
    return this._loggedUser.asObservable();
  }

  setLoggedUser(user: User | null) {
    this._loggedUser.next(user);
  }

  storeTokens(accessToken: string, refreshToken: string, userUrn: string | null) {
    const storage = this.rememberMe ? localStorage : sessionStorage;
    storage.setItem('accessToken', accessToken);
    storage.setItem('refreshToken', refreshToken);
    if(userUrn !== null) {
      storage.setItem('userUrn', userUrn);
    }
  }

  fetchAndSetUser(tokenUri: string | null) {
    if (!tokenUri) {
      this.isAuthenticated.next(false);
      this.readySubject.next(true);
      return;
    }

    this.userService.getUser(`${environment.production ? environment.productionUrl : environment.developmentUrl}${tokenUri}`)
        .subscribe({
          next: (user) => {
            this.setLoggedUser(user);
            this.isAuthenticated.next(true);
            this.readySubject.next(true);
          },
          error: () => {
            this.isAuthenticated.next(false);
            this.readySubject.next(true);
          }
        });
  }




  login(username: string, password: string, rememberMe: boolean = false): Observable<void> {
    this.rememberMe = rememberMe;
    const authHeader = `Basic ${btoa(`${username}:${password}`)}`; // Codificar credenciales en Base64
    const headers = new HttpHeaders({
      Authorization: authHeader,
      'Accept': 'application/vnd.book_models.v1+json',
    });

    return this.http.head(`${environment.production ? environment.productionUrl : environment.developmentUrl}/book_models`, { headers, observe: 'response' }).pipe(
        tap((headResponse) => {
          const accessToken = headResponse.headers.get('accessToken');
          const refreshToken = headResponse.headers.get('refreshToken');
          const userUrn = headResponse.headers.get('userUrn');

          if (accessToken && refreshToken && userUrn) {
            this.storeTokens(accessToken, refreshToken, userUrn);

            this.fetchAndSetUser(userUrn);

            this.loggedUser$.pipe(take(1)).subscribe(user => {
              if (user) {
                this.isAuthenticated.next(true);

                const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
                this.router.navigateByUrl(returnUrl);
              }
            });
          } else {
            this.isAuthenticated.next(false);
          }
        }),
        map(() => void 0),
        catchError((error) => {
          this.isAuthenticated.next(false);
          return throwError(() => error);
        })
    );
  }

  logout() {
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    sessionStorage.removeItem('loggedUser');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('loggedUser');
    sessionStorage.removeItem('userUrn');
    localStorage.removeItem('userUrn');


    this._loggedUser.next(null);
    this.isAuthenticated.next(false);
    this.readySubject.next(true);
  }

  register(email: string, username: string, password: string): Observable<string | null> {
    return this.userService.registerUser(email, username, password).pipe(
        catchError((error: any) => {
          let errorMessage = "AUTH.ERROR_REGISTRATION";
          if (error.status === 409) {
            errorMessage = "PROFILE.USERNAME_ALREADY_EXISTS";
          }
          return throwError(() => ({
            status: error.status,
            message: errorMessage,
            originalError: error,
          }));
        })
    );
  }
}
