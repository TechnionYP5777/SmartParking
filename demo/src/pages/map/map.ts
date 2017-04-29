import { Component, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
declare var google;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})
export class MapPage {

  @ViewChild('map') mapElement: ElementRef;
  @ViewChild('rightPanel') panelElement: ElementRef;
  map: any;
  geolocation: Geolocation
  source: any;
  drivingDestination: any;
  walkingDestination: any;
  directionsService: any;
  directionsDisplay: any;
  directionsDisplayWalk: any;
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
     this.directionsDisplay.setPanel(this.panelElement.nativeElement);
     this.directionsDisplayWalk = new google.maps.DirectionsRenderer({
        polylineOptions: {
          strokeColor: "red"
        }
     });
     this.directionsDisplayWalk.setMap(map);
  }
  calculateAndDisplayRoute(directionsService, directionsDisplay) {
        directionsService.route({
          origin: this.source.position,
          destination: this.drivingDestination.position,
          travelMode: 'DRIVING'
        }, function(response, status) {
          if (status === 'OK') {
            directionsDisplay.setDirections(response);
          } else {
            window.alert('Directions request failed due to ' + status);
          }
        });
  }
  calculateAndDisplayRouteWalking(directionsService, directionsDisplay) {
        directionsService.route({
          origin: this.drivingDestination.position,
          destination: this.walkingDestination.position,
          travelMode: 'WALKING'
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
    if ( this.i%3 == 0) {
        this.source = marker;
        setTimeout(function(){
          var circle = new google.maps.Circle({
             map: map,
             radius: 500,
             fillColor: '#AA0000'
          });
          circle.bindTo('center', marker, 'position');
        },1000);
    } else if (this.i%3 == 1) {
        this.drivingDestination = marker;
    } else {
        this.walkingDestination = marker;
    }
    if ( this.i > 1 ) {
       this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay);
       //this.calculateAndDisplayRouteWalking(this.directionsService, this.directionsDisplayWalk);
    }
    this.i++;
    map.panTo(latLng);
  }
}
