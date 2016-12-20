import { Routes, RouterModule } from '@angular/router';

import {AuthGuard} from "./login/auth.guard";
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";

const appRoutes: Routes = [
    { path: '', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    //{ path: 'register', component: RegisterComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
