import { Routes } from '@angular/router';
import { PublicationsComponent } from './features/publications/publications.component';
import { ProfileComponent } from './features/profile/profile.component';
import { ExchangesComponent } from './features/exchanges/exchanges.component';
import {HistoryComponent} from "./features/exchanges/history.component";
import {RequestsComponent} from "./features/exchanges/requests.component";

const routeConfig: Routes = [
  {
    path: '',
    component: PublicationsComponent,
    title: 'Home'
  },
  {
    path: 'profile',
    component: ProfileComponent,
    title: 'Profile'
  },
  {
    path: 'exchanges',
    component: ExchangesComponent,
    title: 'Exchanges'
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

export default routeConfig;
