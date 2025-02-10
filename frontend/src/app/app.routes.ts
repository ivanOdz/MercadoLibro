import { Routes } from '@angular/router';
import { AuthComponent } from "./features/auth/auth.component";
import {PublicationsComponent} from "./features/publications/publications.component";
import {ProfileComponent} from "./features/profile/profile.component";
import {ExchangesComponent} from "./features/exchanges/exchanges.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {HistoryComponent} from "./features/exchanges/history.component";
import {RequestsComponent} from "./features/exchanges/requests.component";
import {RegisterComponent} from "./features/register/register.component";
import {VerifyComponent} from "./features/verify/verify.component";
import {ChangePasswordComponent} from "./features/change-password/change-password.component";
import {ForgotPasswordComponent} from "./features/forgot-password/forgot-password.component";
import {BookFormComponent} from "./features/book-form/book-form.component";
import { NotFoundComponent } from './features/errors/not-found/not-found.component';
import { BookHomeComponent } from './features/book-home/book-home.component';
import {MyPublicationsComponent} from "./features/my-publications/my-publications.component";
import {FavoritePublicationsComponent} from "./features/favorite-publications/favorite-publications.component";

export const routes: Routes = [
  {path: '', redirectTo: 'publications', pathMatch: 'full'},
  {
    path: 'publications',
    component: PublicationsComponent,
    title: 'Home',
  },
  {
    path: 'publications/mine',
    component: MyPublicationsComponent,
    title: 'My Publications',
    canActivate: [AuthGuard],
  },
  {
    path: 'publications/favorites',
    component: FavoritePublicationsComponent,
    title: 'Favorites',
    canActivate: [AuthGuard],
  },
  {
    path: 'profile',
    component: ProfileComponent,
    title: 'Profile',
    canActivate: [AuthGuard],
  },
  {
    path: 'auth/login',
    component: AuthComponent,
    title: 'Login'
  },
  {
    path: 'auth/register',
    component: RegisterComponent,
    title: 'Register'
  },
  {
    path: 'auth/verify',
    component: VerifyComponent,
    title: 'Verify'
  },
  {
    path: 'auth/request-change-password',
    component: ForgotPasswordComponent,
    title: 'Request Password Change'
  },
  {
    path: 'auth/change-password',
    component: ChangePasswordComponent,
    title: 'Change Password'
  },
  {
    path: 'exchanges',
    component: ExchangesComponent,
    title: 'Exchanges',
    canActivate: [AuthGuard],
  },
  {
    path: 'exchanges/requests',
    component: RequestsComponent,
    title: 'Exchange requests',
    canActivate: [AuthGuard],
  },
  {
    path: 'exchanges/history',
    component: HistoryComponent,
    title: 'Exchange history',
    canActivate: [AuthGuard],
  },
  {
    path: 'books/add',
    component: BookFormComponent,
    title: 'Add a new book',
    /*canActivate: [AuthGuard],*/
  },
  {
    path: 'my-books',
    component: BookHomeComponent,
    title: 'Books',
    /*canActivate: [AuthGuard],*/
  },
  //Error Pages
  {
    path: '**',
    component: NotFoundComponent,
    title: 'Error',
  },
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
