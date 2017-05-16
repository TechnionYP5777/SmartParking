import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';



@Injectable()
export class LocationsService {
  constructor(public http: Http) {
    console.log('Hello LocationsService Provider');
  }
  
  getLocations() {
   
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post('http://localhost:8080/Locations', value, { headers: headers })
      .map(res => res.json());
  }

  
}
