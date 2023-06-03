import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ForgotPasswordDto} from "../../models/dto/ForgotPasswordDto";
import {environment} from "../../../environments/environment";
import {SetPasswordDto} from "../../models/dto/SetPasswordDto";
import {RegisterDto} from "../../models/dto/RegisterDto";
import {LoginDto} from "../../models/dto/LoginDto";
import {WSResponse} from "../../models/response/WSResponse";
import {ResetPasswordDto} from "../../models/dto/ResetPasswordDto";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  login(loginDto:LoginDto) {
    const credentials = {"username": loginDto.email, "password": loginDto.password};
    const headers = new HttpHeaders();
    return this.http.post<WSResponse>(environment.baseUrl + 'auth/signIn', credentials, {headers: headers});
  }

  register(registerRequest: RegisterDto) {
    const headers = new HttpHeaders();
    return this.http.post<WSResponse>(environment.baseUrl + 'auth/register', registerRequest, {headers: headers});
  }

  forgotPassword(forgotPasswordRequest: ForgotPasswordDto) {
    const headers = new HttpHeaders();
    return this.http.post<WSResponse>(environment.baseUrl + 'auth/forgotPassword', forgotPasswordRequest, {headers: headers});
  }

  resetPassword(resetPasswordDto: ResetPasswordDto) {
    const headers = new HttpHeaders();
    return this.http.post<WSResponse>(environment.baseUrl + 'auth/resetPassword', resetPasswordDto, {headers: headers});
  }

  setPassword(setPasswordDto: SetPasswordDto) {
    const headers = new HttpHeaders();
    return this.http.post<WSResponse>(environment.baseUrl + 'auth/setPassword', setPasswordDto, {headers: headers});
  }
}
