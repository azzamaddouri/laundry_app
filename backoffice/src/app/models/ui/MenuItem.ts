
export class MenuItem {
  id = 0
  libelle = ""
  icon = ""
  routerPath = ""

  constructor(id: number, libelle:string, icon:string, routerPath:string) {
    this.id = id;
    this.libelle = libelle;
    this.icon = icon;
    this.routerPath = routerPath;
  }

}
