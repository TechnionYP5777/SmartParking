import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Geolocation } from '@ionic-native/geolocation';
import { Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

@Injectable()
export class LocationService {
     
    srces: Array<{title: string, position:any}>;
    dsts: Array<{title: string, position:any}>;
 
    constructor(public http: Http) {
        console.log('Hello LocationService Provider');
        this.srces = []
        this.dsts = []
    }
   
    getLocations(srcsArray,dstsArray,googleObj) {
        if (this.srces.length!=0 && this.dsts.length != 0) {
            var i = 0;
            for (var i = 0; i < this.srces.length;i++) {
               srcsArray.push(this.srces[i]);
            }
            for (var i = 0; i < this.dsts.length;i++) {
               dstsArray.push(this.dsts[i]);
            }
            var geolocation = new Geolocation();
            geolocation.getCurrentPosition().then((position) => {
                srcsArray.push({title:"Current Location",position:new googleObj.maps.LatLng(position.coords.latitude, position.coords.longitude)});
            });
            return;
        }
        var srces = this.srces;
        var dsts = this.dsts;
        return this.http.get('https://spring-boot-nav.herokuapp.com/Locations').map(res => res.json()).subscribe(data => {
            Object.keys(data).forEach(function(key){
                    srcsArray.push({title:key,position: new googleObj.maps.LatLng(data[key].lat,data[key].lon)});
                    srces.push({title:key,position: new googleObj.maps.LatLng(data[key].lat,data[key].lon)});
                    dstsArray.push({title:key,position: new googleObj.maps.LatLng(data[key].lat,data[key].lon)});
                    dsts.push({title:key,position: new googleObj.maps.LatLng(data[key].lat,data[key].lon)});
            });
            
            var geolocation = new Geolocation();
            geolocation.getCurrentPosition().then((position) => {
                srcsArray.push({title:"Current Location",position:new googleObj.maps.LatLng(position.coords.latitude, position.coords.longitude)});
            });
        });
    }
    getFloors(floorsObj, page) {
      return this.http.get('https://spring-boot-nav.herokuapp.com/Floors').map(res => res.json()).subscribe(data => {
           page.gotFloors = true;
           Object.keys(data).forEach(function(key){
                    floorsObj[key] = data[key];
            });
      });
    }
    getParkingAreas(googleObj,mapViewer) {
        
        return this.http.get('https://spring-boot-nav.herokuapp.com/ParkingAreas').map(res => res.json()).subscribe(data => {
            var areasArray=[];
            Object.keys(data).forEach(function(key){
                    areasArray.push({name:data[key].name,color:data[key].color,position : new googleObj.maps.LatLng(data[key].location.lat,data[key].location.lon)});                    
            });
            mapViewer.showParkingAreas(areasArray);
            
        });
    }
    getBestParkingArea(carNumber, src, dst, mapPage, resolve, googleObj) {
        var value = "car=" + carNumber + "&src=" + src + "&dest=" + dst;
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        return this.http.post('http://localhost:8080/FindPark', value, { headers: headers })
        .map(res => res.json()).subscribe(data => {
              console.log("done");
              mapPage.chosenParkingArea = {title:"slot", position: new googleObj.maps.LatLng(data.lat, data.lon)}
              resolve(true);
        });
    }
}
