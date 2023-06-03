import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Role} from "../../../models/business/Role";
import {AddUserDto} from "../../../models/dto/AddUserDto";
import {MatDialogRef} from "@angular/material/dialog";
import {StorageService} from "../../../services/utilities/storage.service";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {UserService} from "../../../services/webservices/user.service";
import {Subscription} from "rxjs";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit, OnDestroy {

  roleList: Role[] = this.storage.getRoleList()
  selectedRoleMap = new Map();

  @ViewChild('roleInput') roleInput!: ElementRef<HTMLInputElement>;

  isLoading = false

  hidePwd = true;

  firstNameFC = new FormControl('', [Validators.required]);
  lastNameFC = new FormControl('', [Validators.required]);
  pseudoFC = new FormControl('', [Validators.required]);
  emailFC = new FormControl('', [Validators.required, Validators.email]);
  roleFC = new FormControl('', [Validators.required]);

  nom = ""
  prenom = ""
  username = ""
  email = ""

  addUserSubscription?: Subscription

  constructor(public dialogRef: MatDialogRef<UserAddComponent>, private messageManager: MessageManagerService,
              private storage: StorageService, private userService: UserService) {
  }


  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.addUserSubscription?.unsubscribe()
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




  submitForm() {
    console.log("submitForm")
    if (this.firstNameFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifier le nom")
    } else if (this.lastNameFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifer le prénom")
    } else if (this.pseudoFC.invalid) {
      this.messageManager.showInfoSB("Merci de vérifer le pseudo")
    } else if (this.emailFC.invalid) {
      this.messageManager.showInfoSB("Merci de saisir un email")
    } else if (this.selectedRoleMap.size == 0) {
      this.messageManager.showInfoSB("Merci de choisir au moins un type d'utlisateur")
    } else {
      this.addUser()
    }

  }

  colseDialog() {
    this.dialogRef.close();
  }


  addUser() {
    this.isLoading = true
    this.addUserSubscription = this.userService.addUser(new AddUserDto(this.prenom, this.nom, this.username, this.email, Array.from(this.selectedRoleMap.keys()))).subscribe(
      data => {
        console.log(data)
        this.isLoading = false
        switch (data.code) {
          case 200:{
            this.messageManager.showSuccessSB("Utilisateur ajouté")
            this.dialogRef.close({data: {refresh: true}});
            break
          }
          case 5001:{
            this.messageManager.showErrorSB("Adresse email déja utilisée")
            this.dialogRef.close();
            break
          }
          case 5002:{
            this.messageManager.showErrorSB("Problème d'envoi d'email")
            this.dialogRef.close();
            break
          }
          case 5003:{
            this.messageManager.showErrorSB("Vous ne pouvez pas ajouté un Super Admin")
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
          this.messageManager.showErrorSB("Problème d'ajout d'un utilisateur");
        }
      })
  }

  roleSelctionChange(role: Role, value: boolean) {
    console.log("role::::" + role.id + "::" + role.libelle + "=>" + value)
    if (value) {
      this.selectedRoleMap.set(role.id, role)
    } else {
      this.selectedRoleMap.delete(role.id)
    }
    console.log("role:" + this.selectedRoleMap.size)

  }
}
