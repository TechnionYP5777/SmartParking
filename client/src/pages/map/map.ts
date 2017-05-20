import { Component, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { AlertController} from 'ionic-angular';
import { ChoosingPage } from '../choosing-page/choosing-page';
import { LocationService } from '../../providers/location-service';
declare var google;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html',
  providers: [LocationService]
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
  recordRoute: boolean;
  mapView:any;
  constructor(public navCtrl: NavController, public alertCtrl: AlertController,private locService: LocationService) {
  }

  ionViewDidLoad(){
    this.loadMap();
  }
  showParkingAreas(parkingAreasPositions){
    let map = this.mapView;
    parkingAreasPositions.forEach(function(parkingArea){    
        var marker = new google.maps.Marker({
         position: parkingArea.position,
         map: map
        });
        var circle = new google.maps.Circle({
            map: map,
            radius: 100,
            fillColor: parkingArea.color
        });
        circle.bindTo('center', marker, 'position');
    });

  }
  changeLocation() {
     //this.self = this;
     this.navCtrl.push(ChoosingPage, 
              { googleObj: google,
                mapPage: this
              });
  }
  go() {
     if(this.recordRoute){
	   console.log("want to record")	
     }
     else{
     	if(this.srcPosition && this.dstPosition ){
		this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay);
     	}else{
		console.log("src or dst not defined");
	}
     }

  }
  setSrcPosition(position) {
     this.srcPosition = position;
     console.log(position);
  }
  setDstPosition(position) {
     this.dstPosition = position;
     console.log(position);
  }
  loadMap() {
     this.directionsService = new google.maps.DirectionsService;
     this.directionsDisplay = new google.maps.DirectionsRenderer;
     var currentLocationMarker;
     var map = new google.maps.Map(this.mapElement.nativeElement, {
        zoom: 15,
        center: {lat: 32.776878, lng:35.023106}
     });
     this.mapView=map;
     var geolocation = new Geolocation();
     geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        map.setCenter(latLng);
     	currentLocationMarker = new google.maps.Marker({
        	position: latLng,
        	icon: {
                	path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
        	scale: 10
        	},
        	draggable: false,
        	map: map
     	});
     });
     this.i = 0;
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
     this.locService.getParkingAreas(google,this);
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
}
