export class EditUserDto {
  id : Number = 0
  firstName = ""
  lastName = ""
  pseudo = ""
  email = ""
  roleIdList : Number [] = []

  constructor(id: Number,firstName: string, lastName: string, pseudo: string, email: string, roleIdList: Number[]) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.pseudo = pseudo;
    this.email = email;
    this.roleIdList = roleIdList;
  }
}
