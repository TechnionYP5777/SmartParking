import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

@Injectable()
export class RegisterService {
    constructor(public http: Http) {
        console.log('Hello RegisterService Provider');
    }
   
    userRegister() {
        return this.http.get('http://localhost:8080/Register').map(res => res.json());
    }
}