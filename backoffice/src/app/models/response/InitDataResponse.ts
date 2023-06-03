
import { Role } from "../business/Role";
import { UserInfo } from "../business/UserInfo";

export class InitDataResponse {
  userInfo = new UserInfo()
  roleList: Role[] = []

}
