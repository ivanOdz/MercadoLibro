import { Routes } from '@angular/router';
import {AuthGuard} from "../core/guards/auth.guard";
import {NotFoundComponent} from "./errors/not-found/not-found.component";

export const errorRoutes: Routes = [
    { path: '404', component: NotFoundComponent, canActivate: [AuthGuard], title: 'Page Not Found' },
];
