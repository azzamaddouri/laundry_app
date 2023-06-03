export class ForgotPasswordDto {
  email = ""

  constructor(email: string) {
    this.email = email;
  }
}
