import { Component } from '@angular/core';
import {UserInfo} from "./models/business/UserInfo";
import {MenuItem} from "./models/ui/MenuItem";
import {Subscription} from "rxjs";
import {NavigationService} from "./services/utilities/navigation.service";
import {StorageService} from "./services/utilities/storage.service";
import {InitDataService} from "./services/webservices/init-data.service";
import {MessageManagerService} from "./services/utilities/message-manager.service";
import {InitDataResponse} from "./models/response/InitDataResponse";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title = 'BuanderieBackOffice';

  isOnline = true
  isUserConnected = false
  userProfil = new UserInfo()
  menuList: MenuItem[] = []

  initDataSubscription?: Subscription

  constructor(private navigation: NavigationService, private storage: StorageService, private initDataService: InitDataService,
              public messageManager: MessageManagerService) {
    this.isUserConnected = this.storage.isUserConnected()
    this.userProfil = this.storage.getUserProfil()
    this.isOnline = navigator.onLine


    if (storage.isUserConnected()) {
      const authortyList = this.storage.getSessionRoleList()
      this.menuList.push(new MenuItem(0, "Dashboard", "dashboard", "/dashboard"))

      if (authortyList.includes("SuperAdmin") ||  authortyList.includes("Admin")) {
        this.menuList.push(new MenuItem(4, "Utilisateurs", "admin_panel_settings", "/user-list"))
      }

      this.initDataSubscription = initDataService.getInitData().subscribe(data => {
          console.log(data)
          this.storage.saveData(data.result as InitDataResponse)
          this.userProfil = (data.result as InitDataResponse).userInfo
        },
        error => {

          console.log(error)
          if (error.status == 401) {
            this.messageManager.logOutExpired()
          } else {
            this.messageManager.showErrorSB("Problème d'initalisation des dnonnés", "Ok")
          }
        })
    }

  }

  ngOnInit() {

  }

  ngOnDestroy(): void {
    this.initDataSubscription?.unsubscribe()
  }



  itemClick(item: MenuItem) {
    this.navigation.toSpecificPath(item.routerPath)
  }

  logOut() {
    this.isUserConnected = false
    this.storage.logOut()
    location.reload()
  }
}
