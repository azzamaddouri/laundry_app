export class RegisterDto {
  firstName = ""
  lastName = ""
  pseudo = ""
  email = ""
  pwd = ""

  constructor(firstName: string, lastName: string, pseudo: string, email: string, pwd: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.pseudo = pseudo;
    this.email = email;
    this.pwd = pwd;
  }
}
