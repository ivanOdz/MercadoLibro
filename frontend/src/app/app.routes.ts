import { Routes } from '@angular/router';
import { AuthComponent } from "./features/auth/auth.component";
import {PublicationsComponent} from "./features/publications/publications.component";
import {ProfileComponent} from "./features/profile/profile.component";
import {ExchangesComponent} from "./features/exchanges/exchanges.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {HistoryComponent} from "./features/exchanges/history.component";
import {RequestsComponent} from "./features/exchanges/requests.component";

export const routes: Routes = [
    {
    path: 'publications',
    component: PublicationsComponent,
    title: 'Home',
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
    path: 'exchanges',
    component: ExchangesComponent,
    title: 'Exchanges',
    canActivate: [AuthGuard],
  },
  {
    path: 'exchanges/requests',
    component: RequestsComponent,
    title: 'Exchange requests'
  },
  {
    path: 'exchanges/history',
    component: HistoryComponent,
    title: 'Exchange history'
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
