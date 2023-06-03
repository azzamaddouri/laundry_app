import { Injectable } from '@angular/core';
import {StorageService} from "./storage.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class MessageManagerService {

  constructor(private storage: StorageService, private snackBar: MatSnackBar) {
  }

  logOutExpired() {
    this.storage.logOut()
    setTimeout(() => {
      location.reload()
    }, 5000)
    const snackBarRef = this.showErrorSB("Votre session est expiré", "OK", 5000)
    snackBarRef.onAction().subscribe(() => {
      location.reload()
    });
  }

  showInfoSB(msg: string, action: string = "", duration: number = 5000): MatSnackBarRef<any> {
    return this.snackBar.open(msg, action, {duration: duration, panelClass: ['info-snackbar']})
  }

  showSuccessSB(msg: string, action: string = "", duration: number = 5000) {
    return this.snackBar.open(msg, action, {duration: duration, panelClass: ['success-snackbar']})
  }

  showErrorSB(msg: string, action: string = "", duration: number = 5000) {
    return this.snackBar.open(msg, action, {duration: duration, panelClass: ['error-snackbar']})
  }
}
