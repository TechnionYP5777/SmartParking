import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';


/**
 * @author Shahar-Y
 * Created: 18.6.17
 * 
 * This file contains the services for the details of the user
 */

@Injectable()
export class MyDetailsService {
    constructor(public http: Http) {
        console.log('Hello MyDetailsService Provider');
    }


    getUserDetails() {
        return this.http.get('https://spring-boot-nav.herokuapp.com/User/Login').map(res => res.json())
            .catch(this.handleError);

    }

    setUserDetails(userName, password, phoneNum, carNum, eMail, stickerColor, oldCarNum) {
              var value = "name=" + userName + "&pass=" + password + "&phone=" +
         phoneNum + "&car=" + carNum + "&email=" + eMail + "&type=" + stickerColor;
        console.log("in setUserDetails, value = " + value);
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post('https://spring-boot-nav.herokuapp.com/User/ChangeDetails', value, { headers: headers })
        .map(res => res.json()).catch(this.handleError);;
        
    }

    handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }

}