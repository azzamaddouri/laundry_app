import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {NavigationService} from "../../../services/utilities/navigation.service";
import {ActivatedRoute} from "@angular/router";
import {AuthenticationService} from "../../../services/webservices/authentication.service";
import {ResetPasswordDto} from "../../../models/dto/ResetPasswordDto";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit, OnDestroy {

  pwdFC = new FormControl('', [Validators.required, Validators.minLength(6)]);
  confirmPwdFC = new FormControl('', [Validators.required, Validators.minLength(6) ]);

  hidePwd = true;

  resetToken = ""

  password = ""
  confirmPassword = ""

  resetPasswordSubscription? :Subscription

  constructor(private messageManager: MessageManagerService, private navigation: NavigationService,private activatedRoute: ActivatedRoute,
              private authenticationService:AuthenticationService) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.resetToken = params['token'];

      console.log(this.resetToken);
    });
  }

  ngOnDestroy() {
    this.resetPasswordSubscription?.unsubscribe()
  }

  getPwdErrorMessage() {
    if (this.pwdFC.hasError('required')) {
      return 'La mot de passe est obligatoire';
    }else {
      return 'le mot de passe doit comporter au moins 6 charactères';
    }
  }

  getConfirmPwdErrorMessage() {

    if (this.confirmPwdFC.hasError('required')) {
      return 'La confirmation de mot de passe est obligatoire';
    }
    else  {
      return 'le mot de passe doit comporter au moins 6 charactères';
    }

  }
  submitForm(){
    console.log("submitForm")
    if (this.pwdFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifier votre mot de passe")
    } else if (this.confirmPwdFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifier la confirmation de votre mot de passe")
    } else  if (this.password != this.confirmPassword) {
      this.messageManager.showInfoSB("votre mot de passe n'est pas conforme")
      this.password = ""
      this.confirmPassword = ""
    } else {
      this.resetPassword()
    }

  }

  resetPassword(){

    this.resetPasswordSubscription = this.authenticationService.resetPassword(new ResetPasswordDto(this.resetToken,this.password)).subscribe(
      data => {
        console.log(data)

        if (data.code==200){
          this.messageManager.showSuccessSB("Mot de passe modifié avec succès")
          this.navigation.toLogin()
        } else if(data.code==5001){
          this.password = ""
          this.confirmPassword = ""
          this.messageManager.showErrorSB("Le lien n'est plus disponible essayer de nouveau")
        } else if(data.code==5002){
          this.password = ""
          this.confirmPassword = ""
          this.messageManager.showErrorSB("Le lien est expiré")
        } else if(data.code==5099){
          this.password = ""
          this.confirmPassword = ""
          this.messageManager.showErrorSB("Un problème est survenue")
        } else {
          this.password = ""
          this.confirmPassword = ""
          this.messageManager.showErrorSB("Un problème inconnu est survenue")
        }


      },
      error => {

        console.log(error)
        this.messageManager.showErrorSB("Problème serveur")
      })
  }

}
