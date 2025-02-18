import { Routes } from '@angular/router';
import {AuthGuard} from "../core/guards/auth.guard";
import {BookHomeComponent} from "./book-home/book-home.component";
import {BookFormComponent} from "./book-form/book-form.component";
import {BookModelComponent} from "./book-model/book-model.component";

export const booksRoutes: Routes = [
    { path: '', component: BookHomeComponent, canActivate: [AuthGuard], title: 'My books' },
    { path: 'add', component: BookFormComponent, canActivate: [AuthGuard], title: 'Add book' },
    { path: 'library', component: BookModelComponent, canActivate: [AuthGuard], title: 'Our library' }
];
