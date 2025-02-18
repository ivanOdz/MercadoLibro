import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AuthService } from "../services/auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);

    const accessToken = sessionStorage.getItem('accessToken') || localStorage.getItem('accessToken');

    if (accessToken) {
        req = req.clone({
            setHeaders: { Authorization: `Bearer ${accessToken}` }
        });
    }

    const handleResponse = (response: HttpResponse<any>) => {
        const newAccessToken = response.headers.get('X-Access-Token');
        const newRefreshToken = response.headers.get('X-Refresh-Token');
        const tokenUri = response.headers.get('X-User-URI');

        if (newAccessToken && newRefreshToken) {
            authService.storeTokens(newAccessToken, newRefreshToken, tokenUri);
        }

        if (tokenUri) {
            authService.fetchAndSetUser(tokenUri);
        }
    };

    return next(req).pipe(
        tap(response => {
            if (response instanceof HttpResponse) {
                handleResponse(response);
            }
        }),
        catchError((error) => {
            if (error.status === 401 && !req.url.includes('/favorite')) {
                const refreshToken = sessionStorage.getItem('refreshToken') || localStorage.getItem('refreshToken');

                if (refreshToken) {
                    const refreshedReq = req.clone({
                        setHeaders: { Authorization: `Bearer ${refreshToken}` }
                    });

                    return next(refreshedReq).pipe(
                        tap(refreshResponse => {
                            if (refreshResponse instanceof HttpResponse) {
                                handleResponse(refreshResponse);
                            }
                        }),
                        catchError((refreshError) => {
                            if (refreshError.status === 401) {
                                authService.logout();
                                window.location.reload();
                            }
                            return throwError(() => refreshError);
                        })
                    );
                } else {
                    authService.logout();
                }
            }

            return throwError(() => error);
        })
    );
};
