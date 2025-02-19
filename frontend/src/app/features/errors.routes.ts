import { Routes } from '@angular/router';
import {NotFoundComponent} from "./errors/not-found/not-found.component";

export const errorRoutes: Routes = [
    { path: '**', component: NotFoundComponent, data: {title: 'TITLE.NOT_FOUND'}, pathMatch: 'full' },
];
