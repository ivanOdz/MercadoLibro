import { Routes } from '@angular/router';
import { PublicationsComponent } from './features/publications/publications.component';
import { ProfileComponent } from './features/profile/profile.component';
import { ExchangesComponent } from './features/exchanges/exchanges.component';

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
