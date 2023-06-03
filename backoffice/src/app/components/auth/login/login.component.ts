import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {NavigationService} from "../../../services/utilities/navigation.service";
import {StorageService} from "../../../services/utilities/storage.service";
import {AuthenticationService} from "../../../services/webservices/authentication.service";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {LoginDto} from "../../../models/dto/LoginDto";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  @HostListener('window:keydown.enter') enterEvent(){
    this.login();
  }

  userNameFC = new FormControl('', [Validators.required]);
  pwdFC = new FormControl('', [Validators.required]);

  isLoading = false;
  hidePwd = true;

  email = ""
  password = ""

  loginSubscription? : Subscription

  constructor(private navigation : NavigationService, private authenticationService : AuthenticationService, private messageManager: MessageManagerService,
              private storage : StorageService) {

    if (this.storage.isUserConnected()) {
      console.log("connected!!")
      this.navigation.toDashborad()

    }

  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void{
    this.loginSubscription?.unsubscribe()
  }

  getUserNameErrorMessage() {
    return "Merci de saisir votre nom d'utilisateur";
  }

  getPwdErrorMessage() {
    return "Merci de saisir votre mot de passe";
  }

  login(){
    this.isLoading = true
    this.loginSubscription = this.authenticationService.login(new LoginDto(this.email,this.password)).subscribe(
      data => {
        this.isLoading = false
        console.log(data)
        this.storage.saveToken(data.result)
        location.reload()

      },
      error => {
        console.log(error)
        this.isLoading = false

        console.log("error.code", error.code);

        if(error.error == null){
          this.messageManager.showErrorSB("Problème serveur inconnue")
        } else {
          switch (error.error.code) {

            case 301:{
              this.messageManager.showErrorSB("Merci de vérifier votre login et mot de passe")
              break
            }
            case 5001:{
              this.messageManager.showErrorSB("Votre mot de passe n'est pas encore initialisé")
              break
            }
            case 5002:{
              this.messageManager.showErrorSB("Votre compte est désactivé")
              break
            }
            case 5003:{
              this.messageManager.showErrorSB("Votre compte est archivé")
              break
            }
            case 5004:{
              this.messageManager.showErrorSB("Compte n’exite pas")
              break
            }
            default:{
              this.messageManager.showErrorSB("Problème inconnue")
              break
            }
          }
        }
      })
  }





  register(){
    this.navigation.toRegiter()
  }

  forgetPwd(){
    this.navigation.toForgotPassword()
  }

}
function keyEvent(event: Event | undefined, KeyboardEvent: { new(type: string, eventInitDict?: KeyboardEventInit | undefined): KeyboardEvent; prototype: KeyboardEvent; readonly DOM_KEY_LOCATION_LEFT: number; readonly DOM_KEY_LOCATION_NUMPAD: number; readonly DOM_KEY_LOCATION_RIGHT: number; readonly DOM_KEY_LOCATION_STANDARD: number; }) {
  throw new Error('Function not implemented.');
}
