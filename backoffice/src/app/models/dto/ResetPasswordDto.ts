export class ResetPasswordDto {
  token = ""
  newPassword = ""


  constructor(token: string, newPassword: string) {
    this.token = token;
    this.newPassword = newPassword;
  }
}
