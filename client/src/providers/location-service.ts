import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

@Injectable()
export class LocationService {
    constructor(public http: Http) {
        console.log('Hello LocationService Provider');
    }
   
    getLocations(callbackArray) {
        return this.http.get('http://localhost:8080/Locations').map(res => res.json()).subscribe(data => {
            console.log("get data: " + data);
            callbackArray=data;
        });
    }
}