import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import {ActivatedRoute, Router} from "@angular/router";

@Injectable({ providedIn: 'root' })
export class AuthService {
  private isAuthenticated = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.isAuthenticated.asObservable();

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {}


  login(username: string, password: string, rememberMe: boolean = false) {
    const authHeader = `Basic ${btoa(`${username}:${password}`)}`;
    const headers = new HttpHeaders({Authorization: authHeader, 'Accept': 'application/vnd.book_models.v1+json' });

    this.http.head('http://localhost:8080/api/book_models', {headers, observe: 'response'}).subscribe({
      next: (headResponse) => {
            const accessToken = headResponse.headers.get('X-Access-Token');
            const refreshToken = headResponse.headers.get('X-Refresh-Token');

            if (accessToken && refreshToken) {
              if (rememberMe) {
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);
              } else {
                sessionStorage.setItem('accessToken', accessToken);
                sessionStorage.setItem('refreshToken', refreshToken);
              }

              this.isAuthenticated.next(true);

              const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
              this.router.navigateByUrl(returnUrl);
            }
          },
      error: (headError) => {
        // TODO: Manejar error en login.
        this.isAuthenticated.next(false);
      },
    });
  }

  logout() {
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');

    this.isAuthenticated.next(false);
  }
}