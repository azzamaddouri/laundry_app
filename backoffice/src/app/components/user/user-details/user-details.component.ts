import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {SendSetPassordEmailDto} from "../../../models/dto/SendSetPassordEmailDto";
import {User} from "../../../models/business/User";
import {EnableUserDto} from "../../../models/dto/EnableUserDto";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {ArchiveUserDto} from "../../../models/dto/ArchiveUserDto";
import {Subscription} from "rxjs";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {UserService} from "../../../services/webservices/user.service";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit, OnDestroy {

  isLoading = false
  selectedUser : User


  archiveSubscription? : Subscription
  sendPwdEmailSubscription? : Subscription
  enableSubscription? : Subscription
  constructor(public dialogRef: MatDialogRef<UserDetailsComponent>, @Inject(MAT_DIALOG_DATA) public data: User,
              private userService: UserService, private messageManager : MessageManagerService) {
    this.selectedUser = data
    console.log("selectedData---------------------------->")
    console.log(this.selectedUser)
  }

  ngOnInit(): void {
  }

  ngOnDestroy() {

    this.archiveSubscription?.unsubscribe()
    this.sendPwdEmailSubscription?.unsubscribe()
    this.enableSubscription?.unsubscribe()
  }


  archiveUser(event: MatSlideToggleChange){
    this.isLoading = true
    this.archiveSubscription = this.userService.archive(new ArchiveUserDto(this.selectedUser.id,event.checked)).subscribe(
      data => {
        console.log(data)
        this.isLoading = false
        switch (data.code) {
          case 200:{
            let user = data.result as User
            this.selectedUser = user
            if (user.archived){
              this.messageManager.showSuccessSB("Utilisateur archivé")
            } else {
              this.messageManager.showSuccessSB("Utilisateur désarchivé")
            }
            this.dialogRef.close({data: {refresh: true}});
            break
          }
          case 5001:{
            this.messageManager.showErrorSB("Opération non autorisé")
            this.dialogRef.close();
            break
          }
          default: {
            this.messageManager.showErrorSB("Erreur inconnue")
            this.dialogRef.close();
            break;
          }
        }
      },
      error => {
        console.log(error)
        this.isLoading = false
        if (error.status == 401){
          this.messageManager.logOutExpired()
        } else {
          this.messageManager.showErrorSB("Problème d'archivage de l'utilisateur");
        }
      })
  }

  enableUser(event: MatSlideToggleChange){
    this.isLoading = true
    this.enableSubscription = this.userService.enable(new EnableUserDto(this.selectedUser.id,event.checked)).subscribe(
      data => {
        console.log(data)
        this.isLoading = false
        switch (data.code) {
          case 200:{
            let user = data.result as User
            this.selectedUser = user
            if (user.enabled){
              this.messageManager.showSuccessSB("Utilisateur activé")
            } else {
              this.messageManager.showSuccessSB("Utilisateur désactivé")
            }
            this.dialogRef.close({data: {refresh: true}});
            break
          }
          case 5001:{
            this.messageManager.showErrorSB("Opération non autorisé")
            this.dialogRef.close();
            break
          }
          default: {
            this.messageManager.showErrorSB("Erreur inconnue")
            this.dialogRef.close();
            break;
          }
        }
      },
      error => {
        console.log(error)
        this.isLoading = false
        if (error.status == 401){
          this.messageManager.logOutExpired()
        } else {
          this.messageManager.showErrorSB("Problème d'activation de l'utilisateur", );
        }
      })
  }

  sendPasswordEmail(){
    this.isLoading = true
    this.sendPwdEmailSubscription = this.userService.sendSetPasswordEmail(new SendSetPassordEmailDto(this.selectedUser.id)).subscribe(
      data => {
        console.log(data)
        this.isLoading = false
        switch (data.code) {
          case 200:{
            this.messageManager.showSuccessSB("Email Envoyé")
            this.dialogRef.close({data: {refresh: true}});
            break
          }
          case 5001:{
            this.messageManager.showErrorSB("Mot de passe déjà initialisé")
            this.dialogRef.close();
            break
          }
          case 5002:{
            this.messageManager.showErrorSB("Problème d'envoi d'email")
            this.dialogRef.close();
            break
          }
          default: {
            this.messageManager.showErrorSB("Erreur inconnue")
            this.dialogRef.close();
            break;
          }
        }
      },
      error => {
        console.log(error)
        this.isLoading = false
        if (error.status == 401){
          this.messageManager.logOutExpired()
        } else {
          this.messageManager.showErrorSB("Problème d'envoi d'email");
        }
      })
  }
}
