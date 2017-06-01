import { Component, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { AlertController } from 'ionic-angular';
import { ChoosingPage } from '../choosing-page/choosing-page';
import { LocationService } from '../../providers/location-service';
import { PathService } from '../../providers/path-service';
import { LoginPage } from '../login-page/login-page';
declare var google;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html',
  providers: [LocationService, PathService, LoginPage]
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
  dstName: any;
  wantRecordRoute: boolean;
  mapView: any;
  ispathshown: any;
  recordedRoute: any;
  intervalid: any;
  intervalDest: any;
  parkingAreas: any;
  chosenParkingArea: any;
  loginPage: any;
  constructor(public navCtrl: NavController, public alertCtrl: AlertController, private locService: LocationService, private pathService: PathService,
    public login: LoginPage) {
    this.recordedRoute = [];
    this.parkingAreas = [];
    this.loginPage = login;
  }

  ionViewDidLoad() {
    this.loadMap();
    if (this.loginPage.isLogin == false) {
      this.showAlertLogin(this.loginPage);
    }
  }
  showParkingAreas(parkingAreasPositions) {
    let map = this.mapView;
    let array = this.parkingAreas;
    parkingAreasPositions.forEach(function(parkingArea) {
      var marker = new google.maps.Marker({
        position: parkingArea.position,
        map: map
      });
      array.push(parkingArea);
      var circle = new google.maps.Circle({
        map: map,
        radius: 100,
        fillColor: parkingArea.color
      });
      circle.bindTo('center', marker, 'position');
    });
  }
  showAlertLogin(loginPage) {
    if (loginPage.isLogin == false) {
      this.presentLoginAlert();
    }
  }
  drawPath(listLocsToDraw) {
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
    this.navCtrl.push(ChoosingPage,
      {
        googleObj: google,
        mapPage: this
      });
  }
  stopRecording(recordTimeInterval: number) {
    clearInterval(this.intervalid);
    var message = "";
    this.presentPrompt(message);
    let toServer = { duration: recordTimeInterval, points: this.recordedRoute, dst: this.dstName, parkingArea: this.chosenParkingArea.name, description: message };
    this.pathService.sendRecordedPath(toServer);
  }
  presentPrompt(message) {
    let alert = this.alertCtrl.create({
      title: 'You Have Reached Your Destination!',
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
            message = data;
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
    let startTime = (new Date()).getTime()

    this.intervalid = setInterval(function() {
      geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        console.log(recordedRoute);
        recordedRoute.push({ lat: latLng.latitude, lon: latLng.latitude.longitude });
        var distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, dstPosition);
        if (distance < 0.1) {
          mapObj.stopRecording((startTime - (new Date()).getTime()) / 1000);//get the time that the record took in seconds(may be minutes are better)
        }
        console.log(distance);
      });
    }, 30000);
  }
  suggestRoute() {
    clearInterval(this.intervalid);
    this.directionsDisplay.setMap(null);
    this.directionsDisplay.setPanel(null);
    document.getElementsByName("panelLabel")[0].innerHTML = "No Directions To Show";
    this.startRecording();
  }
  go() {
    if (this.srcPosition && this.dstPosition) {
      // send to server Src and Destination
      // Server look for a path and return a result
      document.getElementById("DirectionPanelLabel").style.display = "none";
      let parkingArea = this.getBestParking();
      this.chosenParkingArea = parkingArea;
      this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay, parkingArea.position);
      let mapObj = this;
      if (this.wantRecordRoute) {
        var geolocation = new Geolocation();
        this.intervalid = setInterval(function() {
          geolocation.getCurrentPosition().then((position) => {
            let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            var distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, parkingArea.position);
            if (distance < 0.1) {
              mapObj.showReachedDestination('Reached Parking,\n will start recording your path now');
              let alert = this.alertCtrl.create({
                title: 'You Have Reached Your Parking!',
                message: 'press OK to start record the route',
                buttons: [
                  {
                    text: 'OK',
                    handler: () => { mapObj.suggestRoute(); }
                  }
                ]
              });
              alert.present();
            }
          });
        }, 30000);
      } else {
        this.calculateAndDisplayRouteWalking(this.directionsService, this.directionsDisplayWalk, parkingArea);
      }
      this.ispathshown = true;
    } else {
      console.log("src or dst not defined");
    }

  }
  getBestParking() {
    var min = google.maps.geometry.spherical.computeDistanceBetween(this.parkingAreas[0].position, this.dstPosition);
    let dst = this.dstPosition;
    var min_pa = this.parkingAreas[0];
    this.parkingAreas.forEach(function(pa) {
      let distance = google.maps.geometry.spherical.computeDistanceBetween(pa.position, dst);
      if (distance < min) {
        min = distance;
        min_pa = pa;
      }
    });
    return min_pa;
  }
  presentLoginAlert() {
    let alert = this.alertCtrl.create({
      title: 'WAIT...',
      message: 'Please Login to our system before you proceed',
      buttons: [
        {
          text: 'Go to Login',
          role: 'goToLogin',
          handler: () => {
            this.navCtrl.push(LoginPage);
          }
        }
      ]
    });
    alert.present();
  }
  loadMap() {
    this.directionsService = new google.maps.DirectionsService;
    this.directionsDisplay = new google.maps.DirectionsRenderer;
    var currentLocationMarker;
    var map = new google.maps.Map(this.mapElement.nativeElement, {
      zoom: 15,
      center: { lat: 32.776878, lng: 35.023106 }
    });
    this.mapView = map;
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
    let panel = document.getElementsByName("test_over_map")[0];
    panel.style.backgroundColor = "white";
    this.directionsDisplay.setPanel(panel);
    this.directionsDisplayWalk = new google.maps.DirectionsRenderer({
      polylineOptions: {
        strokeColor: "red"
      }
    });
    this.directionsDisplayWalk.setMap(map);
    this.locService.getParkingAreas(google, this);
  }
  showReachedDestination(message) {
    let alert = this.alertCtrl.create({
      title: message,
    });
    alert.present();
    setTimeout(() => alert.dismiss(), 2000);
  }
  calculateAndDisplayRoute(directionsService, directionsDisplay, parkingArea) {
    directionsService.route({
      origin: this.srcPosition,
      destination: parkingArea,
      travelMode: 'DRIVING'
    }, function(response, status) {
      if (status === 'OK') {
        directionsDisplay.setDirections(response);
      } else {
        window.alert('Directions request failed due to ' + status);
      }
    });
  }
  calculateAndDisplayRouteWalking(directionsService, directionsDisplay, parkingArea) {
    let mapObj = this;
    directionsService.route({
      origin: parkingArea.position,
      destination: this.dstPosition,
      travelMode: 'WALKING'
    }, function(response, status) {
      if (status === 'OK') {
        var data = { error: null, duration: -1, path: [], description: null };
        mapObj.pathService.getRecordedPath(parkingArea.name, mapObj.dstName, data, google, function() {
          if (data.error || data.duration >= response.routes[0].legs[0].duration) {
            directionsDisplay.setDirections(response);
          } else {
            mapObj.drawPath(data.path);
            document.getElementsByName("panelLabel")[0].innerHTML = data.description;
          }
        });
      } else {
        window.alert('Directions request failed due to ' + status);
      }
    });
  }
}
