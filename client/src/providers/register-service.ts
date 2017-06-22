import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';

/**
 * @author DavidCohen55
 * @author Shahar-Y
 * Created: May 2017
 * 
 * This file contains all the services required for the register functionality
 */

@Injectable()
export class RegisterService {
    constructor(public http: Http) {
        console.log('Hello RegisterService Provider');
    }
   
    userRegister(userName, password, phoneNum, carNum, eMail, stickerColor) {

      var value = "name=" + userName + "&pass=" + password + "&phone=" +
         phoneNum + "&car=" + carNum + "&email=" + eMail + "&type=" + stickerColor;
        //console.log("in userRegister, value = " + value);
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post('https://spring-boot-nav.herokuapp.com/User/Register', value, { headers: headers })
        .map(res => res.json()).catch(this.handleError);;
      
    }
  
  getRegisterData() {
        return this.http.get('https://spring-boot-nav.herokuapp.com/User/Register').map(res => res.json())
            .catch(this.handleError);
      
    }
  
  handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }
  
}