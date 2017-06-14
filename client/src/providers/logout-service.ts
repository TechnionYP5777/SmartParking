import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';

/**
 * @author Shahar-Y
 * Created: 14.6.17
 * 
 * This file contains all the services required for the logout functionality
 */

@Injectable()
export class LogoutService {
    constructor(public http: Http) {
        console.log('Hello LogoutService Provider');
    }
   
    userLogout() {

      var value = "name=" + "1" + "&pass=" + "1" + "&phone=" +
         "1" + "&car=" + "2" + "&email=" + "1" + "&type=" + "2";
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

      //this.http.delete('http://localhost:8080/User');
        return this.http.post('http://localhost:8080/User', value, { headers: headers })
            .map(res => res.json());
      
    }
  
  /*getRegisterData() {
        return this.http.get('http://localhost:8080/User/Register').map(res => res.json())
            .catch(this.handleError);
    }*/
  
  handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }
  
}