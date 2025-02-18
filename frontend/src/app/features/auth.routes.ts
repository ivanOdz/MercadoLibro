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
    { path: 'login', component: AuthComponent, title: 'Login' },
    { path: 'register', component: RegisterComponent, title: 'Register' },
    { path: 'register/success', component: RegisterComponentSuccess , title: 'Success' },
    { path: 'verify', component: VerifyComponent, title: 'Verify' },
    { path: 'request-change-password', component: ForgotPasswordComponent, title: 'Request Password Change' },
    { path: 'change-password', component: ChangePasswordComponent, title: 'Change Password' },
    { path: 'profile', component: ProfileComponent, title: 'Profile', canActivate: [AuthGuard] },
];
