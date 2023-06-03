import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import {StorageService} from "../services/utilities/storage.service";
import {NavigationService} from "../services/utilities/navigation.service";

@Injectable({
  providedIn: 'root'
})
export class UserManagmentGuard implements CanActivate {
  constructor(private storage: StorageService, private navigation: NavigationService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const authortyList = this.storage.getSessionRoleList()
    if (authortyList.includes("SuperAdmin") || authortyList.includes("Admin")) {
      return true;
    } else {
      this.navigation.toDashborad()
      return false;
    }
  }

}
