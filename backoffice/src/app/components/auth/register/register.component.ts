import {Component, OnDestroy, OnInit} from '@angular/core';
import {RegisterDto} from "../../../models/dto/RegisterDto";
import {AuthenticationService} from "../../../services/webservices/authentication.service";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {NavigationService} from "../../../services/utilities/navigation.service";
import {Subscription} from "rxjs";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {

  isLoading = false

  hidePwd = true;

  firstNameFC = new FormControl('', [Validators.required]);
  lastNameFC = new FormControl('', [Validators.required]);
  pseudoFC = new FormControl('', [Validators.required]);
  emailFC = new FormControl('', [Validators.required, Validators.email]);
  roleFC = new FormControl('', [Validators.required]);
  pwdFC = new FormControl('', [Validators.required, Validators.minLength(6)]);
  confirmPwdFC = new FormControl('', [Validators.required, Validators.minLength(6) ]);

  nom = ""
  prenom = ""
  username = ""
  email = ""
  password = ""
  confirmPwd = ""

  registerSubscription? : Subscription

  constructor(private navigation:NavigationService,private messageManager: MessageManagerService, private autthenticationService: AuthenticationService) { }

  ngOnInit(): void {
  }

  ngOnDestroy() {
    this.registerSubscription?.unsubscribe()
  }

  getFirstNameErrorMessage() {
    if (this.firstNameFC.hasError('required')) {
      return 'Le nom est obligatoire';
    } else {
      return 'Le nom est obligatoire';
    }
  }

  getLastNameErrorMessage() {
    if (this.lastNameFC.hasError('required')) {
      return 'Le prénom est obligatoire';
    } else {
      return 'Le prénom est obligatoire';
    }
  }

  getPseudoErrorMessage() {
    if (this.pseudoFC.hasError('required')) {
      return 'Le pseudo est obligatoire';
    } else {
      return 'Le pseudo est obligatoire';
    }
  }

  getEmailErrorMessage() {
    if (this.emailFC.hasError('required')) {
      return 'L\'adresse email est obligatoire';
    }

    return this.emailFC.hasError('email') ? 'L\'adresse email n\'est pas valide' : '';
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
    if(this.firstNameFC.invalid){
      this.messageManager.showInfoSB("Merci de vérifier le nom")
    }else if (this.lastNameFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifer le prénom")
    } else if (this.pseudoFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifer le pseudo")
    } else if (this.emailFC.invalid) {
      this.messageManager.showInfoSB("Merci de saisir un email")
    } else if (this.pwdFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifier votre mot de passe")
    } else if (this.confirmPwdFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifier la confirmation de votre mot de passe")
    } else if (this.password != this.confirmPwd) {
      this.messageManager.showInfoSB("votre mot de passe n'est pas conforme")
      this.password = ""
      this.confirmPwd = ""
    } else {
      this.addUser()
    }
  }

  addUser(){
    this.isLoading = true
    const registerRequest = new RegisterDto(this.prenom,this.nom,this.username,this.email,this.password);
    this.registerSubscription = this.autthenticationService.register(registerRequest).subscribe(
      data => {
        console.log(data)
        this.isLoading = false
        if(data.code==301){
          this.email = ""
          this.messageManager.showErrorSB("L'adresse email est déja utilisée")
        } else {
          this.messageManager.showSuccessSB("Utilisateur ajouté")
          this.navigation.toLogin()
        }


      },
      error => {
        this.isLoading = false
        console.log(error)
        this.messageManager.showErrorSB("Un problème est survenue")
      })
  }

  annuler(){
    this.navigation.toLogin()
  }

}
