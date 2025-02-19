import { Routes } from '@angular/router';
import {AuthGuard} from "../core/guards/auth.guard";
import {UnauthorizedComponent} from "./errors/unauthorized/unauthorized.component";
import {ForbiddenComponent} from "./errors/forbidden/forbidden.component";
import {BadRequestComponent} from "./errors/bad-request/bad-request.component";

export const errorRoutes: Routes = [
    { path: '400', component: BadRequestComponent, title: 'Bad Request' },
    { path: '401', component: UnauthorizedComponent, title: 'Unauthorized' },
    { path: '403', component: ForbiddenComponent, title: 'Forbidden' },
];
