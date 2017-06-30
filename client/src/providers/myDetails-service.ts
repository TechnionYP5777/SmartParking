import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import { MyApp } from '../../app/app.component';



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
        return this.http.get('https://spring-boot-nav.herokuapp.com/User/Login/' + MyApp.id).map(res => res.json())
            .catch(this.handleError);

    }


    handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }

}