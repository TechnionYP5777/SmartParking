import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';


/**
 * @author DavidCohen55
 * @author Shahar-Y
 * Created: May 2017
 * 
 * This file contains the services for the login functionality
 */

@Injectable()
export class LoginService {
    constructor(public http: Http) {
        console.log('Hello LoginService Provider');
    }

    handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }

    getDetails() {
        return this.http.get('https://spring-boot-nav.herokuapp.com/User/Login').map(res => res.json())
            .catch(this.handleError);
    }

    userLogin(carNumber, password) {
        var value = "name=" + carNumber + "&pass=" + password;

        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post('https://spring-boot-nav.herokuapp.com/User/Login', value, { headers: headers })
            .map(res => res.json());
    }

}