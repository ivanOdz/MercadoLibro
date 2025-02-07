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

  // Usamos un BehaviorSubject para mantener al usuario logueado
  private _loggedUser = new BehaviorSubject<User | null>(null);
  loggedUser$ = this._loggedUser.asObservable();
  private rememberMe = false;


  constructor(
      private http: HttpClient,
      private route: ActivatedRoute,
      private router: Router,
      private userService: UserService
  ) {
    this.loadRememberMe();
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

  // El getter de loggedUser ahora devuelve el observable del BehaviorSubject
  getloggedUser() {
    return this._loggedUser.asObservable();
  }

  setLoggedUser(user: User | null) {
    this._loggedUser.next(user);
  }

  storeTokens(accessToken: string, refreshToken: string) {
    if (this.rememberMe) {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    } else {
      sessionStorage.setItem('accessToken', accessToken);
      sessionStorage.setItem('refreshToken', refreshToken);
    }
  }

  fetchAndSetUser(tokenUri: string) {
    this.userService.getUser(tokenUri).subscribe({
      next: (user) => {
        this.setLoggedUser(user);
        this.isAuthenticated.next(true);
      },
      error: () => this.logout()
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
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');

    this._loggedUser.next(null);  // Limpiamos el usuario logueado
    this.isAuthenticated.next(false);  // Marcamos como no autenticado
  }

  register(email: string, username: string, password: string): Observable<string | null> {
    return this.userService.registerUser(email, username, password);
  }
}
