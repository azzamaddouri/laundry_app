import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import {NavigationService} from "../services/utilities/navigation.service";
import {StorageService} from "../services/utilities/storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private navigation :NavigationService, private storage: StorageService) {
    if(!this.storage.isUserConnected()){
      console.log("isNotConnected!!")
      this.navigation.toLogin()
    }
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if(this.storage.isUserConnected()){
      return true;
    }else {
      this.navigation.toLogin()
      return false;
    }
  }

}
