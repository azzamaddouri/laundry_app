export class ArchiveUserDto {
  id: Number = 0
  archived: Boolean = false

  constructor(id: Number, archived: Boolean) {
    this.id = id;
    this.archived = archived;
  }
}

