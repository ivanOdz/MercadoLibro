import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../models/user.model';
import { UserService } from './user.service';

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
      private userService: UserService
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
    if(!tokenUri) {
      this.readySubject.next(true);
      return;
    }

    this.userService.getUser('/' + tokenUri).subscribe({
      next: (user) => {
        this.setLoggedUser(user);
        this.isAuthenticated.next(true);
        this.readySubject.next(true);
      },
      error: () => {
        this.logout();
        this.readySubject.next(true);
        }
    });
  }


  login(username: string, password: string, rememberMe: boolean = false) {
    this.rememberMe = rememberMe;

    const authHeader = `Basic ${btoa(`${username}:${password}`)}`;
    const headers = new HttpHeaders({
      Authorization: authHeader,
      'Accept': 'application/vnd.book_models.v1+json',
    });

    this.http.head('http://localhost:8080/api/book_models', { headers, observe: 'response' }).subscribe({
      next: (headResponse) => {
        this.isAuthenticated.next(true);
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
        this.router.navigateByUrl(returnUrl);
      },
      error: () => {
        this.isAuthenticated.next(false);
      },
    });
  }

  logout() {
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    sessionStorage.removeItem('loggedUser');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('loggedUser');

    this._loggedUser.next(null);
    this.isAuthenticated.next(false);
    this.readySubject.next(true);
    this.router.navigateByUrl('/publications');
  }

  register(email: string, username: string, password: string): Observable<string | null> {
    return this.userService.registerUser(email, username, password);
  }
}
