import { Component, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { AlertController} from 'ionic-angular';
import { ChoosingPage } from '../choosing-page/choosing-page';
declare var google;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})
export class MapPage {

  @ViewChild('map') mapElement: ElementRef;

  map: any;
 // geolocation: Geolocation
  source: any;
  drivingDestination: any;
  walkingDestination: any;
  directionsService: any;
  directionsDisplay: any;
  directionsDisplayWalk: any;
  i: number;
  srcPosition: any;
  dstPosition: any;
  constructor(public navCtrl: NavController, public alertCtrl: AlertController) {}

  ionViewDidLoad(){
    this.loadMap2();
  }
  changeLocation() {
     //this.self = this;
     this.navCtrl.push(ChoosingPage, 
              { googleObj: google,
                mapPage: this
              });
  }
  go() {
     this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay);
  }
  setSrcPosition(position) {
     this.srcPosition = position;
     console.log(position);
  }
  setDstPosition(position) {
     this.dstPosition = position;
     console.log(position);
  }
  loadMap2() {
     this.directionsService = new google.maps.DirectionsService;
     this.directionsDisplay = new google.maps.DirectionsRenderer;
     var currentLocationMarker;
     var map = new google.maps.Map(this.mapElement.nativeElement, {
        zoom: 15,
        center: {lat: 41.85, lng: -87.65}
     });
     var geolocation = new Geolocation();
     geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        map.setCenter(latLng);
     	currentLocationMarker = new google.maps.Marker({
        	position: latLng,
        	icon: {
                	path: google.maps.SymbolPath.CIRCLE,
        	scale: 10
        	},
        	draggable: false,
        	map: map
     	});
     });
     this.i = 0;
     map.addListener('click',(e) => this.placeMarkerAndPanTo(e.latLng, map));
     this.directionsDisplay.setMap(map);
     let panel=document.getElementsByName("test_over_map")[0];
     panel.style.backgroundColor="white";
     this.directionsDisplay.setPanel(panel);
     this.directionsDisplayWalk = new google.maps.DirectionsRenderer({
        polylineOptions: {
          strokeColor: "red"
        }
     });
     this.directionsDisplayWalk.setMap(map);
     setInterval(function(){
  	geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
	currentLocationMarker.setPosition(latLng);
		console.log("new postion",latLng);
	});
     }, 5000);
  }
  calculateAndDisplayRoute(directionsService, directionsDisplay) {
        directionsService.route({
          origin: this.srcPosition,
          destination: this.dstPosition,
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
