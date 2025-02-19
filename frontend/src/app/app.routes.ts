import { Routes } from '@angular/router';
import { NotFoundComponent } from "./features/errors/not-found/not-found.component";

export const routes: Routes = [
  {path: '', redirectTo: 'publications', pathMatch: 'full'},
  {
    path: 'publications',
    data: { title: 'TITLE.HOME' },
    loadChildren: () => import('./features/publications.routes').then(m => m.publicationsRoutes)
  },
  {
    path: 'auth',
    data: { title: 'TITLE.AUTH' },
    loadChildren: () => import('./features/auth.routes').then(m => m.authRoutes)
  },
  {
    path: 'exchanges',
    data: { title: 'TITLE.EXCHANGES' },
    loadChildren: () => import('./features/exchanges.routes').then(m => m.exchangesRoutes)
  },
  {
    path: 'books',
    data: { title: 'TITLE.BOOKS' },
    loadChildren: () => import('./features/books.routes').then(m => m.booksRoutes)
  },
  {
    path: 'error',
    loadChildren: () => import('./features/errors.routes').then(m => m.errorRoutes)
  },
  //Error Pages
  {
    path: '**',
    component: NotFoundComponent,
    title: 'TITLE.NOT_FOUND'
  }
];

