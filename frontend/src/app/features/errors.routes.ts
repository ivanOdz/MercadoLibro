import { Routes } from '@angular/router';
import {AuthGuard} from "../core/guards/auth.guard";
import {NotFoundComponent} from "./errors/not-found/not-found.component";
import {UnauthorizedComponent} from "./errors/unauthorized/unauthorized.component";
import {ForbiddenComponent} from "./errors/forbidden/forbidden.component";

export const errorRoutes: Routes = [
    { path: '401', component: UnauthorizedComponent, title: 'Unauthorized' },
    { path: '403', component: ForbiddenComponent, title: 'Forbidden' },
];
