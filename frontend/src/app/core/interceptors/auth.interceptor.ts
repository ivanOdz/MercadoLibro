import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import {AuthService} from "../services/auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const accessToken = sessionStorage.getItem('accessToken') || localStorage.getItem('accessToken');
    if (accessToken) {
        req = req.clone({
            setHeaders: { Authorization: `Bearer ${accessToken}` }
        });
    }

    return next(req).pipe(
        catchError((error) => {
            if (error.status === 401) {
                const refreshToken = sessionStorage.getItem('refreshToken') || localStorage.getItem('refreshToken');

                if (refreshToken) {
                    const refreshedReq = req.clone({
                        setHeaders: { Authorization: `Bearer ${refreshToken}` }
                    });

                    return next(refreshedReq).pipe(
                        catchError((refreshError) => {
                            if (refreshError.status === 401) {
                                authService.logout();
                                router.navigate(['/login']);
                            }
                            return throwError(() => refreshError);
                        })
                    );
                } else {
                    authService.logout();
                    router.navigate(['/login']);
                }
            }
            return throwError(() => error);
        })
    );
};
