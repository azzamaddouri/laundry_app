import { Injectable } from '@angular/core';
import {EnableUserDto} from "../../models/dto/EnableUserDto";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {SendSetPassordEmailDto} from "../../models/dto/SendSetPassordEmailDto";
import {environment} from "../../../environments/environment";
import {ArchiveUserDto} from "../../models/dto/ArchiveUserDto";
import {EditUserDto} from "../../models/dto/EditUserDto";
import {AddUserDto} from "../../models/dto/AddUserDto";
import {StorageService} from "../utilities/storage.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient :HttpClient,private storage :StorageService) { }

  userList(){
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })
    return this.httpClient.get<any>(environment.baseUrl+'user/findAll',{headers: headers})
  }

  addUser(addUserDto: AddUserDto){
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })

    return this.httpClient.post<any>(environment.baseUrl+'user/add',addUserDto,{headers: headers} )
  }

  editUser(editUserDto: EditUserDto){
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })

    return this.httpClient.put<any>(environment.baseUrl+'user/edit',editUserDto,{headers: headers} )
  }

  archive(archiveUserDto: ArchiveUserDto){
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })

    return this.httpClient.put<any>(environment.baseUrl+'user/archive',archiveUserDto,{headers: headers} )
  }

  enable(enableUserDto: EnableUserDto){
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })
    return this.httpClient.put<any>(environment.baseUrl+'user/enable',enableUserDto,{headers: headers} )
  }

  sendSetPasswordEmail(sendSetPassordEmailDto: SendSetPassordEmailDto){
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })

    return this.httpClient.post<any>(environment.baseUrl+'user/sendSetPasswordEmail',sendSetPassordEmailDto,{headers: headers} )
  }

}
