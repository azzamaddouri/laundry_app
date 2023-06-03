export class EnableUserDto {
  id: Number = 0
  enabled: Boolean = false


  constructor(id: Number, enabled: Boolean) {
    this.id = id;
    this.enabled = enabled;
  }
}
