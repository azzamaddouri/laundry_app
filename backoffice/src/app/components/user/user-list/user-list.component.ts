import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {UserEditComponent} from "../user-edit/user-edit.component";
import {User} from "../../../models/business/User";
import {UserDetailsComponent} from "../user-details/user-details.component";
import {UserAddComponent} from "../user-add/user-add.component";
import {MatTableDataSource} from "@angular/material/table";
import {Subscription} from "rxjs";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort, Sort} from "@angular/material/sort";
import {MatDialog} from "@angular/material/dialog";
import {MessageManagerService} from "../../../services/utilities/message-manager.service";
import {UserService} from "../../../services/webservices/user.service";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['id', 'email' , 'firstName', 'lastName','editAction'];
  dataSource: MatTableDataSource<User>;

  dataList : User[] = []
  sortedDataList: User[] =[];

  userListSubscription? : Subscription


  isLoading = false

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  constructor(private dialog: MatDialog,private  messageManager: MessageManagerService,private  userService: UserService ) {

    this.loadData()
    this.sortedDataList = this.dataList.slice();
    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(this.dataList);
  }

  loadData(){
    this.isLoading = true
    this.userListSubscription = this.userService.userList().subscribe(
      data=>{
        this.isLoading = false
        console.log(data)
        this.dataList = data.result.content as User[]
        this.sortedDataList = this.dataList.slice();
        this.dataSource.data = this.sortedDataList

      },
      error => {
        console.log(error)
        this.isLoading = false
        if (error.status == 401){
          this.messageManager.logOutExpired()
        } else {
          this.messageManager.showErrorSB("Problème de chargement des utilisateurs")
        }
      })

  }




  ngOnInit() {
  }


  ngAfterViewInit() {

    this.dataSource.filterPredicate = (data: User, filter: string) => !filter
      || data.id.toString().includes(filter)
      || data.userInfo.firstName.toLowerCase().includes(filter)
      || data.userInfo.lastName.toLowerCase().includes(filter)
      || data.userInfo.userName.toLowerCase().includes(filter)
      || data.userInfo.email.toLowerCase().includes(filter);

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnDestroy() {
    this.userListSubscription?.unsubscribe()
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  sortData(sort: Sort) {
    const data = this.dataList.slice();

    if (!sort.active || sort.direction === '') {
      this.sortedDataList = data;
      this.dataSource.data = this.sortedDataList
      return;
    }
    this.sortedDataList = data.sort((a:User, b:User) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'id': return this.compare(a.id, b.id, isAsc);
        case 'email': return this.compare(a.userInfo.email, b.userInfo.email, isAsc);
        case 'firstName': return this.compare(a.userInfo.firstName, b.userInfo.firstName, isAsc);
        case 'lastName': return this.compare(a.userInfo.lastName, b.userInfo.lastName, isAsc);
        default: return 0;
      }
    });

    this.dataSource.data = this.sortedDataList
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  adddUser(){
    const dialogRef = this.dialog.open(UserAddComponent, {
      width: '40%',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed'+result);
      if (result?.data.refresh){
        this.loadData()
      }
    });
  }

  displayUserDetails(user:User){
    const dialogRef = this.dialog.open(UserDetailsComponent, {
      width: '40%',data : user
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed'+result);
      if (result?.data.refresh){
        this.loadData()
      }
    });
  }

  editUser(user:User){
    const dialogRef = this.dialog.open(UserEditComponent, {
      width: '40%',data : user
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed'+result);
      if (result?.data.refresh){
        this.loadData()
      }
    });
  }

}
