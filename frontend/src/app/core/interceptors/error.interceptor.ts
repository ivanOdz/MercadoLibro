import { HttpInterceptorFn } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    const router = inject(Router);

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            let errorMessage = "Error";
            let redirectUrl: string | null = null;

            if (error.error && error.error.message) {
                errorMessage = error.error.message;
            } else {
                switch (error.status) {
                    case 401:
                        if (!req.url.includes('/book_models')) {
                            redirectUrl = "/error/401";
                        }
                        break;
                    case 403:
                        redirectUrl = "/error/403";
                        break;
                    case 404:
                        redirectUrl = "/error/404"; //como no existe va a mandar a la pagina de error 404
                        break;
                }
            }

            if (redirectUrl) {
                router.navigate([redirectUrl]).then();
            }

            return throwError(() => new Error(errorMessage));
        })
    );
};
