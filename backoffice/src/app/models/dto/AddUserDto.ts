export class AddUserDto {
  firstName = ""
  lastName = ""
  pseudo = ""
  email = ""
  roleIdList : Number [] = []

  constructor(firstName: string, lastName: string, pseudo: string, email: string, roleIdList: Number[]) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.pseudo = pseudo;
    this.email = email;
    this.roleIdList = roleIdList;
  }
}
