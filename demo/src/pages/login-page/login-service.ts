import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';

@Injectable()
export class LoginService {
  constructor(public http: Http) {
    console.log('Hello LoginService Provider');
  }
  callHttp() {
    return this.http.get('http://localhost:8080/shahar').map(res => res.json())
      .catch(this.handleError);
  }
  
  handleError(error) {
    console.error(error);
    return Observable.throw('Server error');
  }

  
    getDetails() {
    return this.http.get('http://localhost:8080/User').map(res => res.json())
      .catch(this.handleError);
  }
  
    userLogin(carNumber,password) {
    var value = "name=" + carNumber+"&pass="+password;

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return new Promise(resolve => {
      this.http.post('http://localhost:8080/User', value, { headers: headers }).subscribe(data => {
        if (data.status == 200) {
          resolve(true);
        }
        else
          resolve(false);
      });
    });
  }
  
  tempLogin(user) {
    var value = "name=" + user;

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return new Promise(resolve => {
      this.http.post('http://localhost:8080/User', value, { headers: headers }).subscribe(data => {
        if (data.status == 200) {
          resolve(true);
        }
        else
          resolve(false);
      });
    });
  }

}