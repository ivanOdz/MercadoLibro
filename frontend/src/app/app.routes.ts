import { Routes } from '@angular/router';
import { PublicationsComponent } from './publications/publications.component';
import { ProfileComponent } from './profile/profile.component';
import { ExchangesComponent } from './exchanges/exchanges.component';

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
];

export default routeConfig;
