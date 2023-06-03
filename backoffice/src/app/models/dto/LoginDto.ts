export class LoginDto {
  email = ""
  password = ""

  constructor(email: string,password: string) {
    this.email = email;
    this.password = password;
  }
}
