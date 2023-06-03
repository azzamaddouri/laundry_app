import {Role} from "./Role";
import {UserInfo} from "./UserInfo";

export class User {
  id = 0
  pseudo = ""
  email = ""
  roleList : Role [] = []
  enabled = false
  archived = false
  deletable = false
  userInfo = new UserInfo()
}
