import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take, tap } from 'rxjs/operators';
import {AuthService} from "../services/auth.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.authService.isAuthenticated$.pipe(
            take(1), // Solo toma el primer valor y se completa
            map(isAuthenticated => !!isAuthenticated), // Asegura que el valor sea booleano
            tap(isAuthenticated => {
                if (!isAuthenticated) {
                    console.log("Estado de la autenticacion: ", isAuthenticated);
                    this.router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
                }
            })
        );
    }
}
