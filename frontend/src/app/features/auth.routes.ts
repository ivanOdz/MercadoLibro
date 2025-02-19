import { Routes } from '@angular/router';
import {AuthComponent} from "./auth/auth.component";
import {RegisterComponent} from "./register/register.component";
import {RegisterComponentSuccess} from "./register/register-success.component";
import {VerifyComponent} from "./verify/verify.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {ChangePasswordComponent} from "./change-password/change-password.component";
import {ProfileComponent} from "./profile/profile.component";
import {AuthGuard} from "../core/guards/auth.guard";

export const authRoutes: Routes = [
    { path: 'login', component: AuthComponent, data: { title: 'TITLE.AUTH.LOGIN' } },
    { path: 'register', component: RegisterComponent, data: { title: 'TITLE.AUTH.REGISTER' } },
    { path: 'register/success', component: RegisterComponentSuccess, data: { title: 'TITLE.AUTH.SUCCESS' } },
    { path: 'verify', component: VerifyComponent, data: { title: 'TITLE.AUTH.VERIFY' } },
    { path: 'request-change-password', component: ForgotPasswordComponent, data: { title: 'TITLE.AUTH.REQUEST_PASSWORD_CHANGE' } },
    { path: 'change-password', component: ChangePasswordComponent, data: { title: 'TITLE.AUTH.CHANGE_PASSWORD' } },
    { path: 'profile', component: ProfileComponent, data: { title: 'TITLE.PROFILE' }, canActivate: [AuthGuard] },
];

