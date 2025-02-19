import { Routes } from '@angular/router';
import {AuthGuard} from "../core/guards/auth.guard";
import {BookHomeComponent} from "./book-home/book-home.component";
import {BookFormComponent} from "./book-form/book-form.component";
import {BookModelComponent} from "./book-model/book-model.component";

export const booksRoutes: Routes = [
    { path: '', component: BookHomeComponent, canActivate: [AuthGuard], data: { title: 'TITLE.BOOKS.MY_BOOKS' } },
    { path: 'add', component: BookFormComponent, canActivate: [AuthGuard], data: { title: 'TITLE.BOOKS.ADD_BOOK' } },
    { path: 'library', component: BookModelComponent, canActivate: [AuthGuard], data: { title: 'TITLE.BOOKS.LIBRARY' } }
];
