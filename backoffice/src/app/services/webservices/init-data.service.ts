import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {StorageService} from "../utilities/storage.service";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class InitDataService {

  constructor(private httpClient: HttpClient, private storage: StorageService) { }

  getInitData() {
    let headers = new HttpHeaders({
      'Authorization': this.storage.getToken()
    })
    return this.httpClient.get<any>(environment.baseUrl + 'init/initData', { headers: headers })
  }
  /*   getInitData(){
      let headers = new HttpHeaders({
        'Authorization': this.storage.getToken()
      })
      return this.httpClient.get<any>(environment.baseUrl+'init/initData',{headers: headers})
    } */
}
