import {Component, OnDestroy, OnInit} from '@angular/core';
import {ForgotPasswordDto} from "../../../models/dto/ForgotPasswordDto";
import {FormControl, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {NavigationService} from "../../../services/utilities/navigation.service";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {AuthenticationService} from "../../../services/webservices/authentication.service";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {

  emailFC = new FormControl('', [Validators.required, Validators.email]);

  forgotPasswordSubscription? : Subscription

  constructor(private navigation: NavigationService ,private messageManager : MessageManagerService, private authenticationService: AuthenticationService) { }
  email = ""
  ngOnInit(): void {
  }

  ngOnDestroy() {
    this.forgotPasswordSubscription?.unsubscribe()
  }

  getErrorMessage() {
    if (this.emailFC.hasError('required')) {
      return 'You must enter a value';
    }

    return this.emailFC.hasError('email') ? 'Not a valid email' : '';
  }

  submitForm(){
    if (this.emailFC.invalid) {
      this.messageManager.showInfoSB("Merci de saisir un email")
    } else {
      this.sendPassword()
    }
  }

  sendPassword(){
    this.forgotPasswordSubscription = this.authenticationService.forgotPassword(new ForgotPasswordDto(this.email)).subscribe(
      data => {
        console.log(data)
        switch (data.code) {
          case 200:{
            this.messageManager.showSuccessSB("Merci de vérifier votre boite Email")
            break
          }
          case 5001:{
            this.messageManager.showErrorSB("Problème de compte")
            break
          }
          case 5002:{
            this.messageManager.showErrorSB("Problème d'envoi d'email")
            break
          }
          default: {
            this.messageManager.showErrorSB("Erreur inconnue")
            break;
          }
        }
        this.navigation.toLogin()
      },
      error => {
        console.log(error)
        this.messageManager.showErrorSB("Un problème est survenue")
      })
  }

}
