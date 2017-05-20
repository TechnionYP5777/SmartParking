import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
//import { Geolocation } from '@ionic-native/geolocation';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

@Injectable()
export class LocationService {
    constructor(public http: Http) {
        console.log('Hello LocationService Provider');
    }
   
    getLocations(srcsArray,dstsArray,googleObj) {
        return this.http.get('http://localhost:8080/Locations').map(res => res.json()).subscribe(data => {
            Object.keys(data).forEach(function(key){
                    srcsArray.push({title:key,position: new googleObj.maps.LatLng(data[key].lon,data[key].lat)});
                    dstsArray.push({title:key,position: new googleObj.maps.LatLng(data[key].lon,data[key].lat)});
            });
            srcsArray.push({title:"Current Location",position:null});
        });
    }
}