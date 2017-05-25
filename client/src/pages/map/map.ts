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
  mapView: any;
  ispathshown: any;
  recordedRoute: any;
  intervalid: any;
  intervalDest: any;
  parkingAreas: any;
  constructor(public navCtrl: NavController, public alertCtrl: AlertController,private locService: LocationService) {
     this.recordedRoute = [];
     this.parkingAreas = [];
  }

  ionViewDidLoad(){
    this.loadMap();
  }
  showParkingAreas(parkingAreasPositions){
    let map = this.mapView;
    let array = this.parkingAreas;
    parkingAreasPositions.forEach(function(parkingArea){    
        var marker = new google.maps.Marker({
         position: parkingArea.position,
         map: map
        });
        array.push(parkingArea.position);
        var circle = new google.maps.Circle({
            map: map,
            radius: 100,
            fillColor: parkingArea.color
        });
        circle.bindTo('center', marker, 'position');
    });

  }

  drawPath( listLocsToDraw ) {
     var drivePath = new google.maps.Polyline({
          path: listLocsToDraw,
          geodesic: true,
          strokeColor: '#FF0000',
          strokeOpacity: 1.0,
          strokeWeight: 2
        });

        drivePath.setMap(this.mapView);
  }

  changeLocation() {
     //this.self = this;
     this.navCtrl.push(ChoosingPage, 
              { googleObj: google,
                mapPage: this
              });
  }
  stopRecording() {
     clearInterval(this.intervalid);
     this.presentPrompt();
     // post path to server;
  }
  presentPrompt() {
    var userDirections = null;
    let alert = this.alertCtrl.create({
      title: 'Directions',
      message: 'Please provide general directions',
      inputs: [
      {
        name: 'direction',
        placeholder: 'Direction'
      },
    ],
    buttons: [
      {
        text: 'Cancel',
        role: 'cancel'
      },
      {
        text: 'Submit',
        handler: data => {
          userDirections = data;
          console.log(data);
        }
      } 
    ]
    });
    alert.present();
  }
  startRecording() {
     let recordedRoute = this.recordedRoute;
     let dstPosition = this.dstPosition;
     let mapObj = this;
     var geolocation = new Geolocation();
     this.intervalid = setInterval(function(){
        geolocation.getCurrentPosition().then((position) => {
         let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
         //this.currentLocationMarker.setPosition(latLng);
         console.log(recordedRoute);
         recordedRoute.push(latLng);
         var distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, dstPosition);
         if (distance < 0.1) {
             mapObj.stopRecording();
         } 
         console.log(distance);
        });
     }, 5000);
  }
  suggestRoute() {
    this.directionsDisplay.setMap(null);
    this.directionsDisplay.setPanel(null); 
    document.getElementsByName("panelLabel")[0].innerHTML = "No Directions To Show";
    this.startRecording();
  }
  go() {
     if(this.recordRoute){
	   console.log("want to record")	
     }
     else{
     	if(this.srcPosition && this.dstPosition ) {
          // send to server Src and Destination
          // Server look for a path and return a result
          document.getElementById("DirectionPanelLabel").style.display = "none";    
	  this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay);
          this.ispathshown = true;
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
     //setInterval(function(){
     //	geolocation.getCurrentPosition().then((position) => {
     //   let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
     //	currentLocationMarker.setPosition(latLng);
     //		console.log("new postion",latLng);
     //	});
     // }, 5000);
     this.locService.getParkingAreas(google,this);
     // test draw path:
     var testCoordinates = [
          {lat: 32.773518, lng: 35.030413},
          {lat: 32.776282, lng: 35.026513}
     ];
     this.drawPath(testCoordinates);
     this.showReachedDestination();
     this.presentPrompt();
  }
  showReachedDestination() {
      let alert = this.alertCtrl.create({
         title: 'Reached Destination !',
       });
       alert.present();  
       setTimeout(() => alert.dismiss(),2000);
  }
  reachedFirstDest() {
     clearInterval(this.intervalDest);
     this.directionsDisplay.setMap(null);
     this.directionsDisplay.setPanel(null);
     this.showReachedDestination();
     var min = -1;
     var minArea = null;
     for (var i=0; i < this.parkingAreas.length; i++ ) {
           var area = this.parkingAreas[i];
           var distance = google.maps.geometry.spherical.computeDistanceBetween(area, this.dstPosition);
            if (distance > min) {
                distance = min;
                minArea = area;
            }
     } 
     this.directionsDisplay.setMap(this.mapView);
     this.directionsDisplay.setMap(this.mapView);
     this.calculateAndDisplayRouteWalking(this.directionsService, this.directionsDisplay, minArea);
  }
  calculateAndDisplayRoute(directionsService, directionsDisplay) {
        let mapObj = this;
        var geolocation = new Geolocation();
        var dst = this.dstPosition;
        this.intervalDest = setInterval(function() {
          geolocation.getCurrentPosition().then((position) => {
          let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
          var distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, dst);
          if (distance < 0.1) { 
               mapObj.reachedFirstDest();         
          }
        })}, 5000);
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
  calculateAndDisplayRouteWalking(directionsService, directionsDisplay, position) {
        directionsService.route({
          origin: this.dstPosition,
          destination: position,
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
