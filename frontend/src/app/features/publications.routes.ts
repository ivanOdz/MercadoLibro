import { Routes } from '@angular/router';
import {AuthGuard} from "../core/guards/auth.guard";
import {PublicationsComponent} from "./publications/publications.component";
import {MyPublicationsComponent} from "./my-publications/my-publications.component";
import {FavoritePublicationsComponent} from "./favorite-publications/favorite-publications.component";
import {PublicationComponent} from "./publication-detail/publication.component";

export const publicationsRoutes: Routes = [
    { path: '', component: PublicationsComponent, data: { title: 'TITLE.PUBLICATIONS.MAIN' } },
    { path: 'mine', component: MyPublicationsComponent, canActivate: [AuthGuard], data: { title: 'TITLE.PUBLICATIONS.MY_PUBLICATIONS' } },
    { path: 'favorites', component: FavoritePublicationsComponent, canActivate: [AuthGuard], data: { title: 'TITLE.PUBLICATIONS.FAVORITES' } },
    { path: ':id', component: PublicationComponent, data: { title: 'TITLE.PUBLICATIONS.DETAIL' } }
];

