import { Routes } from '@angular/router';
import { NotFoundComponent } from "./features/errors/not-found/not-found.component";

export const routes: Routes = [
  {path: '', redirectTo: 'publications', pathMatch: 'full'},
  {
    path: 'publications',
    title: 'Home',
    loadChildren: () => import('./features/publications.routes').then(m => m.publicationsRoutes)
  },
  {
    path: 'auth',
    title: 'Auth',
    loadChildren: () => import('./features/auth.routes').then(m => m.authRoutes)
  },
  {
    path: 'exchanges',
    title: 'Exchanges',
    loadChildren: () => import('./features/exchanges.routes').then(m => m.exchangesRoutes)
  },
  {
    path: 'books',
    title: 'Books',
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
    title: 'Page Not Found'
  }
];

// Para proteger el acceso a las rutas que se requiere tener autenticacion
/*const routes: Routes = [

  {
    path: '',
    canActivate: [AuthGuard], // Protege todas las rutas hijas
    children: [
      { path: 'perfil', component: PerfilComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'configuracion', component: ConfiguracionComponent }
    ]
  },
  { path: 'login', component: LoginComponent }
];
*/
