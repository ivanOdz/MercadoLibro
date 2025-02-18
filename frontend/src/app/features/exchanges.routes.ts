import { Routes } from '@angular/router';
import {ExchangesComponent} from "./exchanges/exchanges.component";
import {AuthGuard} from "../core/guards/auth.guard";
import {RequestsComponent} from "./exchanges/requests.component";
import {HistoryComponent} from "./exchanges/history.component";

export const exchangesRoutes: Routes = [
    { path: '', component: ExchangesComponent, canActivate: [AuthGuard], title: 'Exchanges' },
    { path: 'requests', component: RequestsComponent, canActivate: [AuthGuard], title: 'Exchange requests' },
    { path: 'history', component: HistoryComponent, canActivate: [AuthGuard], title: 'Exchange history' }
];