import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';

import { User } from '../entities/user';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private usersUrl: string;	

  constructor( private http: HttpClient ) {
     this.usersUrl = 'http://localhost:8080/users';
  }

  public getUsers(): Observable<JSON> {
    return this.http.get<JSON>(this.usersUrl);
  }
 
  public save(user: User) {
    console.warn('User to be saved', user);
    return this.http.post<User>(this.usersUrl, user);
  }
  
  public getUsersByName(userName): Observable<JSON> {
    return this.http.get<JSON>(this.usersUrl + '/search/findByNameContaining?name=' + userName);
  }

}
