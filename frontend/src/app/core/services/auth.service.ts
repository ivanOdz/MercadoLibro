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

  constructor(
      private http: HttpClient,
      private route: ActivatedRoute,
      private router: Router,
      private us: UserService
  ) {}

  // El getter de loggedUser ahora devuelve el observable del BehaviorSubject
  get loggedUser() {
    return this._loggedUser.asObservable();
  }

  login(username: string, password: string, rememberMe: boolean = false) {
    const authHeader = `Basic ${btoa(`${username}:${password}`)}`;
    const headers = new HttpHeaders({
      Authorization: authHeader,
      'Accept': 'application/vnd.book_models.v1+json',
    });

    this.http.head('http://localhost:8080/api/book_models', { headers, observe: 'response' }).subscribe({
      next: (headResponse) => {
        const accessToken = headResponse.headers.get('X-Access-Token');
        const refreshToken = headResponse.headers.get('X-Refresh-Token');

        if (accessToken && refreshToken) {
          // Guardamos el token según la preferencia de "remember me"
          if (rememberMe) {
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
          } else {
            sessionStorage.setItem('accessToken', accessToken);
            sessionStorage.setItem('refreshToken', refreshToken);
          }

          const tokenUri = headResponse.headers.get('X-User-Uri');
          if (tokenUri) {
            // Obtenemos el usuario usando el tokenUri y lo asignamos a _loggedUser
            this.us.getUser(tokenUri).subscribe({
              next: (user) => {
                this._loggedUser.next(user);  // Actualiza el usuario logueado
              },
              error: () => {
                // Manejo de error en caso de que falle la obtención del usuario
                this._loggedUser.next(null);
              },
            });
          }

          this.isAuthenticated.next(true);  // Marcamos como autenticado
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigateByUrl(returnUrl);
        }
      },
      error: () => {
        this.isAuthenticated.next(false);  // Marcamos como no autenticado en caso de error
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
}
