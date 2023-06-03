import {Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Role} from "../../../models/business/Role";
import {EditUserDto} from "../../../models/dto/EditUserDto";
import {FormControl, Validators} from "@angular/forms";
import {User} from "../../../models/business/User";
import {Subscription} from "rxjs";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {StorageService} from "../../../services/utilities/storage.service";
import {UserService} from "../../../services/webservices/user.service";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit, OnDestroy {
  roleList: Role[] = this.storage.getRoleList()
  selectedRoleMap = new Map();

  @ViewChild('roleInput') roleInput!: ElementRef<HTMLInputElement>;

  isLoading = false


  firstNameFC = new FormControl('', [Validators.required]);
  lastNameFC = new FormControl('', [Validators.required]);
  pseudoFC = new FormControl('', [Validators.required]);
  emailFC = new FormControl('', [Validators.required, Validators.email]);
  roleFC = new FormControl('', [Validators.required]);

  selectedUser:User
  lastName = ""
  firstName = ""
  username = ""
  email = ""

  editUserSubscription? : Subscription

  constructor(public dialogRef: MatDialogRef<UserEditComponent>, @Inject(MAT_DIALOG_DATA) public data: User, private messageManager: MessageManagerService,
              private storage: StorageService, private userService: UserService) {

    this.selectedUser = data
    this.lastName = data.userInfo.lastName
    this.firstName = data.userInfo.firstName
    this.username = data.userInfo.userName
    this.email = data.userInfo.email
    this.selectedRoleMap.clear()
    data.roleList.forEach(item=>{
      this.selectedRoleMap.set(item.id,item)
    })

    this.emailFC.disable()
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.editUserSubscription?.unsubscribe()
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
    }  else {
      this.editUser()
    }

  }

  colseDialog() {
    this.dialogRef.close();
  }


  editUser() {
    this.isLoading = true

    this.editUserSubscription = this.userService.editUser(new EditUserDto(this.selectedUser.id,this.firstName, this.lastName, this.username, this.email, Array.from(this.selectedRoleMap.keys()))).subscribe(
      data => {
        console.log(data)
        this.isLoading = false
        switch (data.code) {
          case 200:{
            this.messageManager.showSuccessSB("Utilisateur modifié")
            this.dialogRef.close({data: {refresh: true}});
            break
          }
          case 5001:{
            this.messageManager.showErrorSB("Opération non autorisé")
            this.dialogRef.close();
            break
          } case 5003:{
            this.messageManager.showErrorSB("Vous ne pouvez pas modifié un Super Admin")
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
          this.messageManager.showErrorSB("Problème de modification de l'utilisateur");
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
