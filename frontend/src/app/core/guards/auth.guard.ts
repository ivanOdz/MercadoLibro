import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import {filter, Observable} from 'rxjs';
import { map, take, tap, switchMap } from 'rxjs/operators';
import { AuthService } from "../services/auth.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.authService.ready$.pipe(
            filter(ready => ready),
            take(1),
            switchMap(() =>
                this.authService.isAuthenticated$.pipe(
                    take(1),
                    tap(isAuthenticated => {
                        if (!isAuthenticated) {
                            console.log("Usuario no autenticado, redirigiendo...");
                            this.router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
                        }
                    })
                )
            )
        );
    }
}
