import { Routes } from '@angular/router';
import {ExchangesComponent} from "./exchanges/exchanges.component";
import {AuthGuard} from "../core/guards/auth.guard";
import {RequestsComponent} from "./exchanges/requests.component";
import {HistoryComponent} from "./exchanges/history.component";

export const exchangesRoutes: Routes = [
    { path: '', component: ExchangesComponent, canActivate: [AuthGuard], data: { title: 'TITLE.EXCHANGES.MAIN' } },
    { path: 'requests', component: RequestsComponent, canActivate: [AuthGuard], data: { title: 'TITLE.EXCHANGES.REQUESTS' } },
    { path: 'history', component: HistoryComponent, canActivate: [AuthGuard], data: { title: 'TITLE.EXCHANGES.HISTORY' } }
];
