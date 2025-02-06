import {HttpInterceptorFn, HttpResponse} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import {catchError, tap, throwError} from 'rxjs';
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

    const handleTokenStorage = (response: HttpResponse<any>) => {
        const newAccessToken = response.headers.get('X-Access-Token');
        const newRefreshToken = response.headers.get('X-Refresh-Token');

        if (newAccessToken && newRefreshToken) {
            authService.storeTokens(newAccessToken, newRefreshToken);
        }
    };

    return next(req).pipe(
        tap(response => {
            if (response instanceof HttpResponse) {
                handleTokenStorage(response);
            }
        }),
        catchError((error) => {
            if (error.status === 401) {
                const refreshToken = sessionStorage.getItem('refreshToken') || localStorage.getItem('refreshToken');

                if (refreshToken) {
                    const refreshedReq = req.clone({
                        setHeaders: { Authorization: `Bearer ${refreshToken}` }
                    });

                    return next(refreshedReq).pipe(
                        tap(refreshResponse => {
                            if (refreshResponse instanceof HttpResponse) {
                                handleTokenStorage(refreshResponse);
                            }
                        }),
                        catchError((refreshError) => {
                            if (refreshError.status === 401) {
                                authService.logout();
                                router.navigate(['/auth/login']);
                            }
                            return throwError(() => refreshError);
                        })
                    );
                } else {
                    authService.logout();
                    router.navigate(['/auth/login']);
                }
            }
            return throwError(() => error);
        })
    );
};
