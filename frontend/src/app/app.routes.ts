import { Routes } from '@angular/router';

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
  //Error Pages
  {
    path: '**',
    loadChildren: () => import('./features/errors.routes').then(m => m.errorRoutes)
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
