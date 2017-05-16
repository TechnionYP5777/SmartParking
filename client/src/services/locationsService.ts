import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

@Injectable()
export class LocationsService {
    constructor(public http: Http) {
        console.log('Hello LocationsService Provider');
    }

    
    getLocations() {
        return this.http.get('http://localhost:8080/Locations').map(res => res.json()).subscribe(data => {
            console.log("get data: " + data['Civil Engineering Faculty'].lon);
        });
    }
}