import { Component, OnInit } from '@angular/core';

import { UsersService } from '../services/users.service';

import { FormBuilder } from '@angular/forms';

import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

userForm;

  constructor(
    private usersService: UsersService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router, ) {
    this.userForm = this.formBuilder.group({
      name: '',
      email: ''
    });
  }

  ngOnInit() {
   
  }

  onSubmit(userData) {
    this.usersService.save(userData).subscribe(
      response => this.goHome(response),
      err => console.log(err),
    );

    console.warn('User has been submitted', userData);
  }

  goHome(response) {
    console.log(response);
    this.router.navigate(['']);
  }

}
