import { Component, OnInit } from '@angular/core';


import { UsersService } from '../services/users.service';

import { User } from '../entities/user';

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrls: ['./list-users.component.css']
})
export class ListUsersComponent implements OnInit {

  users: User[];

  searchFilter: string;

  constructor(
    private usersService: UsersService,
  ) { }

  ngOnInit(): void {
    this.usersService.getUsers().subscribe(data => {
      this.parseJSON(data);
    });
  }

  search() {
    this.users = [];
    if (this.searchFilter === '') {
      this.usersService.getUsers().subscribe(data => {
        this.parseJSON(data);
      });
    } else {
      this.usersService.getUsersByName(this.searchFilter).subscribe(data => {
        this.parseJSON(data);
      });
    }
  }

  parseJSON(data: JSON) {
    data = data['_embedded'];
    if (data) {
      this.users = data['users'];
    }
  }

}
