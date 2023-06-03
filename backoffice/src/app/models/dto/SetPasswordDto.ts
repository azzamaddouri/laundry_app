export class SetPasswordDto {
  token = ""
  password = ""

  constructor(token: string, password: string) {
    this.token = token;
    this.password = password;
  }
}
