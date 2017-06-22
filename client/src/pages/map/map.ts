import { Component, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { AlertController } from 'ionic-angular';
import { ChoosingPage } from '../choosing-page/choosing-page';
import { LocationService } from '../../providers/location-service';
import { PathService } from '../../providers/path-service';
import { LoginPage } from '../login-page/login-page';
import { LogoutPage } from '../logout-page/logout-page';
import {TextToSpeech} from '@ionic-native/text-to-speech';
declare var google;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html',
  providers: [LocationService, PathService, LoginPage, LogoutPage]
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
  simulationMode: boolean;
  mapView: any;
  ispathshown: any;
  recordedRoute: any;
  intervalid: any;
  voiceIntervalId: any;
  intervalDest: any;
  parkingAreas: any;
  chosenParkingArea: any;
  loginPage: any;

  useVoice: any;
  curr_step_index: any;
  directionsResponse: any;

  polylineArray:any;
  dstMarker:any;
    
  indoorDescription: any;

  constructor(public navCtrl: NavController, public alertCtrl: AlertController,
     private locService: LocationService, private pathService: PathService,
    public login: LoginPage,  private logout: LogoutPage, private tts: TextToSpeech) {
    this.simulationMode=false;
    this.wantRecordRoute=false;
    this.recordedRoute = [];
    this.parkingAreas = [];
    this.loginPage = login;
    this.tts.speak("hello world");

    this.useVoice = true;
    this.curr_step_index = 0;

    this.polylineArray = [];

  }

  ionViewDidLoad() {
    this.loadMap();
    this.tts.speak("hello world");
    if (this.loginPage.isLogin == false) {
      this.showAlertLogin(this.loginPage);
    }
  }
  showParkingAreas(parkingAreasPositions) {
    let map = this.mapView;
    let array = this.parkingAreas;
    parkingAreasPositions.forEach(function(parkingArea) {
      array.push(parkingArea);
      var circle = new google.maps.Circle({
        map: map,
        radius: 100,
        fillColor: parkingArea.color
      });
      google.maps.event.addListener(circle,"mousemove",function(event){
        google.maps.event.trigger(map,'mousemove',event)    
      });
    });
  }
  showAlertLogin(loginPage) {
    if (loginPage.isLogin == false) {
      this.presentLoginAlert();
    }
  }
  removeWalkingPath(){
    this.polylineArray.forEach(function(line){
        line.setMap(null);
    });
    this.polylineArray=[];
  }
  setIndoorDescription(floor) {
    console.log(floor["id"]);
    console.log(floor["description"]);
    this.indoorDescription = floor["description"];
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
    this.polylineArray.push(drivePath);
  }

  changeLocation() {
    this.navCtrl.push(ChoosingPage,
      {
        googleObj: google,
        mapPage: this
      });
  }
  setSrcPosition(position) {
    this.srcPosition = position;
    console.log(position);
  }
  setDstPosition(position, name) {
    this.dstName = name;
    this.dstPosition = position;
    this.indoorDescription = null;
    console.log(position);
  }
  stopRecording(recordTimeInterval: number) {
    clearInterval(this.intervalid);
    var message = "";
    let mapObj=this;
    mapObj.dstMarker.setMap(null);
    this.presentPrompt(message,function(){
        let toServer = { duration: recordTimeInterval, points: mapObj.recordedRoute, dst: mapObj.dstName, parkingArea: mapObj.chosenParkingArea.name, description: message };
        mapObj.removeWalkingPath();
        mapObj.pathService.sendRecordedPath(toServer);
    });    
  }
  presentPrompt(message,callback) {
    let mapObj=this;
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
         handler: data => {
             mapObj.removeWalkingPath();
          }
        },
        {
          text: 'Submit',
          handler: data => {
            message = data;
            console.log(data);
            callback();
          }
        }
      ]
    });
    alert.present();
  }
  startRecording() {
    this.recordedRoute=[];
    let recordedRoute = this.recordedRoute;
    let dstPosition = this.dstPosition;
    let mapObj = this;
    var geolocation = new Geolocation();
    let startTime = (new Date()).getTime()
    var validTime = (new Date()).getTime();
    if(this.simulationMode){
        google.maps.event.addListener(mapObj.mapView, 'mousemove', function (event) {
            let newTime=(new Date).getTime();
            if(newTime-validTime<=1000){
                return;
            }
            validTime=newTime;
            recordedRoute.push({ lat: event.latLng.lat(), lng: event.latLng.lng() });
            let distance = google.maps.geometry.spherical.computeDistanceBetween(event.latLng, mapObj.dstPosition);
            console.log(distance);
            mapObj.drawPath(recordedRoute.slice(-2));
            if (distance < 5) {
                google.maps.event.clearListeners(mapObj.mapView, 'mousemove');
                mapObj.stopRecording((startTime - (new Date()).getTime()) / 1000);
            }
        });
        return;   
    }
    this.intervalid = setInterval(function() {
      geolocation.getCurrentPosition().then((position) => {
        let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        console.log(recordedRoute);
        recordedRoute.push({ lat: latLng.lat(), lng: latLng.lng() });
        var distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, dstPosition);
        mapObj.drawPath(recordedRoute.slice(-2));
        if (distance < 5) {
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
      this.chosenParkingArea = this.getBestParking();
      let mapObj = this;
      this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay, this.chosenParkingArea,function(){
      let geolocation = new Geolocation();
      if (mapObj.wantRecordRoute) {
        mapObj.dstMarker = new google.maps.Marker({
            position: mapObj.dstPosition,
            map: mapObj.mapView
        });
        if(mapObj.simulationMode){
            google.maps.event.addListener(mapObj.mapView, 'mousemove', function (event) {
                let distance = google.maps.geometry.spherical.computeDistanceBetween(event.latLng, mapObj.chosenParkingArea.position);
                console.log(distance);
                mapObj.readDirections(event.latLng);
                if (distance < 5) {
                  google.maps.event.clearListeners(mapObj.mapView, 'mousemove');
                  mapObj.showReachedDestination('Reached Parking,\n will start recording your path now');
                  let alert = mapObj.alertCtrl.create({
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
        }else{
            
            this.intervalid = setInterval(function() {
              geolocation.getCurrentPosition().then((position) => {
                let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                let distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, mapObj.chosenParkingArea.position);
                console.log(distance);
                mapObj.readDirections(position.coords);
                if (distance < 5) {
                  mapObj.showReachedDestination('Reached Parking,\n will start recording your path now');
                  let alert = mapObj.alertCtrl.create({
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
          }
      } else {
        mapObj.calculateAndDisplayRouteWalking(mapObj.directionsService, mapObj.directionsDisplayWalk, mapObj.chosenParkingArea);
        if(mapObj.simulationMode){
            google.maps.event.addListener(mapObj.mapView, 'mousemove', function (event) {
                let distance = google.maps.geometry.spherical.computeDistanceBetween(event.latLng, mapObj.dstPosition);
                console.log(distance);
                mapObj.readDirections(event.latLng);
                if (distance < 5) {
                    let alert = mapObj.alertCtrl.create({
                        title: 'You Have Reached Your Destination!',
                        message: 'You Have Reached Your Destination!',
                        buttons: [
                          {
                            text: 'OK',
                            handler: () => {google.maps.event.clearListeners(mapObj.mapView, 'mousemove');
                                mapObj.directionsDisplay.setMap(null);
                                mapObj.directionsDisplay.setPanel(null);
                                mapObj.directionsDisplayWalk.setMap(null);
                                mapObj.directionsDisplayWalk.setPanel(null);
                                mapObj.removeWalkingPath();
                            }
                          }
                        ]
                      });
                    alert.present();
                }
            });
        }else{
            mapObj.intervalid = setInterval(function() {
              geolocation.getCurrentPosition().then((position) => {
                let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                let distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, mapObj.dstPosition);
                mapObj.readDirections(position);
                console.log(distance);
                if (distance < 5) {
                   let alert = mapObj.alertCtrl.create({
                        title: 'You Have Reached Your Destination!',
                        message: 'You Have Reached Your Destination!',
                        buttons: [
                          {
                            text: 'OK',
                            handler: () => {
                                clearInterval(mapObj.intervalid);
                                clearInterval(mapObj.voiceIntervalId);
                                mapObj.directionsDisplay.setMap(null);
                                mapObj.directionsDisplay.setPanel(null);
                                mapObj.directionsDisplayWalk.setMap(null);
                                mapObj.directionsDisplayWalk.setPanel(null);
                                mapObj.removeWalkingPath();
                            }
                          }
                        ]
                      });
                    alert.present();
                }
              });
            }, 30000);
        }   
      }
      mapObj.ispathshown = true;
        });
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
      ], 
      cssClass: 'alertLogin'
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
      },
      preserveViewport: true
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
  readDirections(position) {
    if (this.useVoice == false) {
        console.log("no voice");
        return;
    }
    let response = this.directionsResponse;
    let steps = response.routes[0].legs[0].steps;
    if (this.curr_step_index < steps.length - 1) {
    	  let latLng = new google.maps.LatLng(position.lat(), position.lng());
        let latLng2 = new google.maps.LatLng(steps[this.curr_step_index + 1].start_location.lat(),
                                      steps[this.curr_step_index + 1].start_location.lng());
        console.log(latLng2);
        let distance = google.maps.geometry.spherical.computeDistanceBetween(latLng,
        						latLng2);
        console.log(distance);
        if (distance < 50) {
           this.curr_step_index++;
        }
    }
  	let step = steps[this.curr_step_index]
  	var text = step.instructions.replace(/<b>/g, "");
  	text = text.replace(/<\/b>/g, "");
    console.log(text);
    if (this.curr_step_index >= steps.length - 1 && this.indoorDescription != null) {
         console.log(this.indoorDescription);
    }
  }
  voicechanged(e : any) {
     this.useVoice = e.checked;
  }
  calculateAndDisplayRoute(directionsService, directionsDisplay, parkingArea,callback) {
    let mapObj = this;
    directionsService.route({
      origin: this.srcPosition,
      destination: parkingArea.position,
      travelMode: 'DRIVING'
    }, function(response, status) {
      if (status === 'OK') {
      	 mapObj.directionsResponse = response;
         directionsDisplay.setDirections(response);
         parkingArea.position=new google.maps.LatLng(response.routes[0].overview_path.slice(-1)[0].lat(), response.routes[0].overview_path.slice(-1)[0].lng());
      } else {
        window.alert('Directions request failed due to ' + status);
      }
      callback();
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
