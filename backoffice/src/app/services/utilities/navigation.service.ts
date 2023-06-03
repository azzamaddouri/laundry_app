import { Injectable } from '@angular/core';
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(private router:Router) { }

  toSpecificPath(path:String){
    this.router.navigateByUrl(path.toString())
  }

  toLogin(){
    this.router.navigateByUrl("/login")
  }

  toRegiter(){
    this.router.navigateByUrl("/register")
  }

  toForgotPassword(){
    this.router.navigateByUrl("/forgot-password")
  }

  toDashborad(){
    this.router.navigateByUrl(`/dashboard`)
  }


}
