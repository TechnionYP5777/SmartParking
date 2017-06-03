import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';


@Injectable()
export class RegisterService {
    constructor(public http: Http) {
        console.log('Hello RegisterService Provider');
    }
   
    userRegister(userName, password, phoneNum, carNum, eMail, stickerColor) {

      var value = "name=" + userName + "&pass=" + password + "&phone=" +
         phoneNum + "&car=" + carNum + "&email=" + eMail + "&type=" + stickerColor;
        console.log("in userRegister, value = " + value);
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post('http://localhost:8080/User/Register', value, { headers: headers })
            .map(res => res.json());
      
    }
}