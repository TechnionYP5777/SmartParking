import { Component, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
declare var google;
 
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
 
  @ViewChild('map') mapElement: ElementRef;
  map: any;
  geolocation: Geolocation
  source: any;
  destination: any;
  directionsService: any;
  directionsDisplay: any;
  i: number;
  constructor(public navCtrl: NavController) {}
  
  ionViewDidLoad(){
    this.loadMap2();
  }
  loadMap2() {
     this.directionsService = new google.maps.DirectionsService;
     this.directionsDisplay = new google.maps.DirectionsRenderer;
     var map = new google.maps.Map(this.mapElement.nativeElement, {
        zoom: 15,
        center: {lat: 41.85, lng: -87.65}
     });
     this.geolocation = new Geolocation();
     this.geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        map.setCenter(latLng);
     });
     this.i = 0;
     map.addListener('click',(e) => this.placeMarkerAndPanTo(e.latLng, map));
     this.directionsDisplay.setMap(map);
  }
  calculateAndDisplayRoute(directionsService, directionsDisplay) {
        directionsService.route({
          origin: this.source.position,
          destination: this.destination.position,
          travelMode: 'DRIVING'
        }, function(response, status) {
          if (status === 'OK') {
            directionsDisplay.setDirections(response);
          } else {
            window.alert('Directions request failed due to ' + status);
          }
        });
  }  
  loadMap(){
	this.geolocation = new Geolocation();
	this.geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        let mapOptions = {
            center: latLng,
            zoom: 15,
            mapTypeId: google.maps.MapTypeId.ROADMAP
         }
        this.map = new google.maps.Map(this.mapElement.nativeElement, mapOptions);
        this.map.addListener('click',(e) => this.placeMarkerAndPanTo(e.latLng, this.map));
       }, (err) => {
           console.log(err);
       });
  }
  placeMarkerAndPanTo(latLng, map){
    var marker = new google.maps.Marker({
     position: latLng,
     map: map
    });
    if ( this.i%2 == 0) {
        this.source = marker;
    } else {
        this.destination = marker;
    }
    if ( this.i > 0 ) {
       this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay);
    }
    this.i++;
    map.panTo(latLng);
  } 
}
