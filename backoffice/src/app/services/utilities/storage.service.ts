import { Injectable } from '@angular/core';
import {Role} from "../../models/business/Role";
import {UserInfo} from "../../models/business/UserInfo";
import {InitDataResponse} from "../../models/response/InitDataResponse";
import jwtDecode, {JwtPayload} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  prefix: string = 'tn.UpTech.Sav3Stars.'
  AuthToken: string = this.prefix + 'AuthToken'
  Role: string = this.prefix + 'Role'
  UserInfo: string = this.prefix + 'UserInfo'
  MarqueList: string = this.prefix + 'MarqueList'
  CategorieList: string = this.prefix + 'CategorieList'
  ModeleList: string = this.prefix + 'ModeleList'
  StatusClaimList: string = this.prefix + 'StatusClaimList'
  RaisonClaimList: string = this.prefix + 'RaisonClaimList'
  EtatProduitList: string = this.prefix + 'EtatProduitList'
  GouverneratList: string = this.prefix + 'GouverneratList'
  StatuDevisList: string = this.prefix + 'StatuDevisList'
  TypeDemandeurList: string = this.prefix + 'TypeDemandeurList'
  TypeInterventionList: string = this.prefix + 'TypeInterventionList'

  PanneList: string = this.prefix + 'PanneList'
  TypePanneList: string = this.prefix + 'TypePanneList'
  PiecesRechangeList: string = this.prefix + 'PiecesRechangeList'
  TechnicienList: string = this.prefix + 'TechnicienList'
  MoeList: string = this.prefix + 'MoeList'

  //TechnicienList

  constructor() { }
  logOut() {
    window.localStorage.removeItem(this.AuthToken);
    window.localStorage.clear();
  }
  public saveToken(token: string) {
    window.localStorage.removeItem(this.AuthToken);
    window.localStorage.setItem(this.AuthToken, "Bearer " + token);
  }
  public getToken(): string {
    return localStorage.getItem(this.AuthToken) || "";
  }
  public getSessionRoleList(): String[] {
    let token = localStorage.getItem(this.AuthToken) ?? "";
    if (token == "") {
      return []
    }
    token = token.replace("Bearer ", "")
    console.log(jwtDecode<JwtPayload>(token))
    let authorotyStr = jwtDecode<JwtPayload>(token)["iss"] as string;
    authorotyStr = authorotyStr.replace(new RegExp(" ", 'g'), "")
    authorotyStr = authorotyStr.replace("[", "")
    authorotyStr = authorotyStr.replace("]", "")
    return authorotyStr.split(",")
  }
  public isUserConnected(): boolean {
    return this.getToken() != "";
  }
  public saveData(value: InitDataResponse) {
    this.saveUserProfil(value.userInfo)
    this.saveRoles(value.roleList)
  }
  public saveUserProfil(value: UserInfo) {
    window.localStorage.removeItem(this.UserInfo);
    window.localStorage.setItem(this.UserInfo, JSON.stringify(value));
  }
  public getUserProfil(): UserInfo {
    return JSON.parse(localStorage.getItem(this.UserInfo) || "{}");
  }
  public saveRoles(value: Role[]) {
    window.localStorage.removeItem(this.Role);
    window.localStorage.setItem(this.Role, JSON.stringify(value));
  }
  public getRoleList(): Role[] {
    var result: Role[] = JSON.parse(localStorage.getItem(this.Role) || "");
    if (!this.getSessionRoleList().includes("SuperAdmin")) {
      let index = result.findIndex(role => role.libelle == "SuperAdmin")
      result.splice(index, 1)
    }
    return result;
  }

}
