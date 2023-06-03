import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NoInternetComponent } from './components/no-internet/no-internet.component';
import { UserAddComponent } from './components/user/user-add/user-add.component';
import { UserEditComponent } from './components/user/user-edit/user-edit.component';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { UserDetailsComponent } from './components/user/user-details/user-details.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ForgotPasswordComponent } from './components/auth/forgot-password/forgot-password.component';
import { SetPasswordComponent } from './components/auth/set-password/set-password.component';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password.component';
import {AppRoutingModule} from "./app-routing/app-routing.module";
import {RouterModule} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from "@angular/material/card";
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatDividerModule } from "@angular/material/divider";
import { MatListModule } from "@angular/material/list";
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from "@angular/material/table";
import {MatPaginatorModule } from "@angular/material/paginator";
import {MatGridListModule } from "@angular/material/grid-list";
import { MatMenuModule } from "@angular/material/menu";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDialogModule } from '@angular/material/dialog';
import { MatSortModule } from "@angular/material/sort";
import { MatRippleModule } from "@angular/material/core";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { MatToolbarModule } from "@angular/material/toolbar";
import { WelcomeComponent } from './components/welcome/welcome.component';
@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    NoInternetComponent,
    UserAddComponent,
    UserEditComponent,
    UserListComponent,
    UserDetailsComponent,
    LoginComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    SetPasswordComponent,
    ResetPasswordComponent,
    WelcomeComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule,
    AppRoutingModule,
    HttpClientModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    FlexLayoutModule,
    MatProgressBarModule,
    MatSidenavModule,
    MatDividerModule,
    MatListModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatGridListModule,
    MatCheckboxModule,
    MatDialogModule,
    MatSortModule,
    MatRippleModule,
    MatSlideToggleModule,
    MatMenuModule,
    MatToolbarModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
