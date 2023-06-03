import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "../components/auth/login/login.component";
import {ResetPasswordComponent} from "../components/auth/reset-password/reset-password.component";
import {RegisterComponent} from "../components/auth/register/register.component";
import {ForgotPasswordComponent} from "../components/auth/forgot-password/forgot-password.component";
import {UserListComponent} from "../components/user/user-list/user-list.component";
import {UserManagmentGuard} from "../guard/user-managment.guard";
import {AuthGuard} from "../guard/auth.guard";
import {DashboardComponent} from "../components/dashboard/dashboard.component";
import {SetPasswordComponent} from "../components/auth/set-password/set-password.component";
import { WelcomeComponent } from '../components/welcome/welcome.component';


const routes: Routes = [
{
    path : 'welcome',
    component : WelcomeComponent
  },
  {
    path : 'login',
    component : LoginComponent
  },
  {
    path : 'reset-password',
    component : ResetPasswordComponent
  },
  {
    path : 'register',
    component : RegisterComponent
  },
  {
    path : 'forgot-password',
    component : ForgotPasswordComponent
  },
  {
    path : 'set-password',
    component : SetPasswordComponent
  },
  {
    path : 'user-list',
    component : UserListComponent,
    canActivate: [AuthGuard,UserManagmentGuard]
  },
  {
    path : 'dashboard',
    component : DashboardComponent,
    canActivate: [AuthGuard]
  },

  {
    path: '',
    redirectTo: 'welcome',
    pathMatch: 'full',
  },
  { path: '**',
    redirectTo: 'dashboard'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
