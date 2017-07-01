/**

map - the map view of the navigation, main page of application
@author zahimizrahi
@author shaysegal
@author sefialbo
@since 2017-03-27

**/

import { Component, ViewChild, ElementRef } from '@angular/core';
import {NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { AlertController } from 'ionic-angular';
import { ChoosingPage } from '../choosing-page/choosing-page';
import { LocationService } from '../../providers/location-service';
import { PathService } from '../../providers/path-service';
import { LoginPage } from '../login-page/login-page';
import { LoginService } from '../login-page/login-service';
import { LogoutPage } from '../logout-page/logout-page';
import {TextToSpeech} from '@ionic-native/text-to-speech';
import { Platform } from 'ionic-angular';
import { MyApp } from '../../app/app.component';
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
    loginService: any;
    lastSearches: any;

    useVoice: any;
    curr_step_index: any;
    directionsResponse: any;

    polylineArray: any;
    dstMarker: any;

    indoorDescription: any;
    lastTimeVoice: any;

    bestParking: any;
    srcName: any;
    didNavigate: any;
    inNav: boolean;
    currentLocationMarker: any;
    voiceEnabled: any;

    constructor(public navCtrl: NavController, public alertCtrl: AlertController,
        private locService: LocationService, private pathService: PathService,
        public login: LoginPage, private logout: LogoutPage, private tts: TextToSpeech, loginService: LoginService,
        public plt: Platform) {

        this.simulationMode = false;
        this.wantRecordRoute = false;
        this.recordedRoute = [];
        this.parkingAreas = [];
        this.lastSearches = [];
        this.loginPage = login;
        this.voiceEnabled = !this.plt.is('core') && !this.plt.is('mobileweb');
        console.log("use voice :" + this.voiceEnabled);
        if (this.voiceEnabled == true) {
            this.tts.speak("Welcome to Smart Parking");
        }
        this.didNavigate = false;
        MyApp.isLoggedIn = false;
        this.loginService = loginService;

        this.useVoice = true;
        this.curr_step_index = 0;

        this.polylineArray = [];
        this.bestParking = null;
        this.inNav = false;

    }
    ionViewDidLoad() {
        this.loadMap();
        this.loginService.getDetails().subscribe(data => {
            if (data == undefined) {
                console.log("mydata undefined");
            } else {
                console.log("getUserData() myData: " + JSON.stringify(data));
                this.loginPage.carNumber = data.carNumber;
                MyApp.isLoggedIn = (data.name != "");
            }

        }, err => {
            console.log("getUserData error: " + err);
        });
        if (!MyApp.isLoggedIn) {
            //document.getElementsByClassName("item item-block item-md")[0].disabled = true;

        }
    }

    /*
    this.loginPage.getUserData(myData).then((result) => {
        setTimeout(function() {
            console.log("in map page, myData = " + JSON.stringify(myData));
            if (myData == undefined) {
                console.log("in map page, myData is undefined");
                ref.showAlertLogin(ref.loginPage);
                return;
            }
        }, 8000);
    });*/

    searchLastSearches() {
        let mapObj = this;
        this.pathService.getLastPaths(function(lastSearch) {
            if (lastSearch.Status) {
                let alert = mapObj.alertCtrl.create();
                alert.setTitle('Choose Path');
                alert.setMessage(lastSearch.Status)
                alert.addButton({
                    text: 'OK',
                    handler: data => {}
                });
                alert.present();
                return;
            }
            let alert = mapObj.alertCtrl.create();
            alert.setTitle('Choose Path');
            for (var i = 0; i < lastSearch.SavedPaths.length; i += 1) {
                alert.addInput({
                    type: 'radio',
                    label: lastSearch.SavedPaths[i].src.name + " , " + lastSearch.SavedPaths[i].dst.name,
                    value: lastSearch.SavedPaths[i],
                    checked: false
                });
            }
            alert.addButton('Cancel');
            alert.addButton({
                text: 'OK',
                handler: data => {
                    mapObj.srcName = data.src.name;
                    mapObj.dstName = data.dst.name;
                    mapObj.dstPosition = new google.maps.LatLng(data.dst.lat, data.dst.lon);
                    if (data.src.name == "Current Location") {
                        console.log(mapObj.currentLocationMarker.postion);
                        mapObj.srcPosition = mapObj.currentLocationMarker.position;
                    } else {
                        mapObj.srcPosition = new google.maps.LatLng(data.src.lat, data.src.lon);
                    }
                    mapObj.go()
                }
            });
            alert.present();
        });
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
            google.maps.event.addListener(circle, "mousemove", function(event) {
                google.maps.event.trigger(map, 'mousemove', event)
            });
        });
    }
    showAlertLogin(loginPage) {
        if (!MyApp.isLoggedIn) {
            this.presentLoginAlert();
        }
    }
    removeWalkingPath() {
        this.polylineArray.forEach(function(line) {
            line.setMap(null);
        });
        this.polylineArray = [];
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




        if (!MyApp.isLoggedIn) {
            this.presentLoginAlert();
        }
        else {
            this.navCtrl.push(ChoosingPage,
                {
                    googleObj: google,
                    mapPage: this
                });
        }
    }
    setSrcPosition(position, name) {
        this.srcPosition = position;
        this.srcName = name;
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
        let mapObj = this;
        mapObj.dstMarker.setMap(null);
        this.inNav = false;
        this.presentPrompt(function(message) {
            let toServer = { duration: recordTimeInterval, path: mapObj.recordedRoute, destination: mapObj.dstName, parkingArea: mapObj.chosenParkingArea.title, description: message };
            mapObj.removeWalkingPath();
            mapObj.pathService.sendRecordedPath(toServer);
        });
    }
    presentPrompt(callback) {
        let mapObj = this;

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
                        //message = data;
                        console.log(data);
                        callback(data.direction);
                    }
                }
            ]
        });
        alert.present();
    }
    startRecording() {
        this.recordedRoute = [];
        let recordedRoute = this.recordedRoute;
        let dstPosition = this.dstPosition;
        let mapObj = this;
        var geolocation = new Geolocation();
        let startTime = (new Date()).getTime()
        var validTime = (new Date()).getTime();
        if (this.simulationMode) {
            google.maps.event.addListener(mapObj.mapView, 'mousemove', function(event) {
                mapObj.currentLocationMarker.setPosition(event.latLng);
                let newTime = (new Date).getTime();
                if (newTime - validTime <= 1000) {
                    return;
                }
                validTime = newTime;
                recordedRoute.push({ lat: event.latLng.lat(), lon: event.latLng.lng() });
                let distance = google.maps.geometry.spherical.computeDistanceBetween(event.latLng, mapObj.dstPosition);
                console.log(distance);
                mapObj.drawPath(recordedRoute.slice(-2));
                if (distance < 5) {
                    google.maps.event.clearListeners(mapObj.mapView, 'mousemove');
                    mapObj.stopRecording(((new Date()).getTime() - startTime) / 1000);
                }
            });
            return;
        }
        this.intervalid = setInterval(function() {
            geolocation.getCurrentPosition().then((position) => {

                let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                mapObj.currentLocationMarker.setPosition(latLng);
                console.log(recordedRoute);
                recordedRoute.push({ lat: latLng.lat(), lon: latLng.lng() });
                var distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, dstPosition);
                mapObj.drawPath(recordedRoute.slice(-2));
                if (distance < 5) {
                    mapObj.stopRecording(((new Date()).getTime() - startTime) / 1000);//get the time that the record took in seconds(may be minutes are better)
                }
                console.log(distance);
            });
        }, 30000);
    }
    suggestRoute() {
        clearInterval(this.intervalid);
        this.directionsDisplay.setMap(null);
        this.directionsDisplay.setPanel(null);
        this.directionsDisplayWalk.setPanel(null);
        document.getElementsByName("panelLabel")[0].innerHTML = "No Directions To Show";
        document.getElementsByName("panelLabel2")[0].innerHTML = "No Directions To Show";
        this.startRecording();
    }
    goAux() {
        if (this.srcPosition && this.dstPosition) {
            // send to server Src and Destination
            // Server look for a path and return a result
            document.getElementById("DirectionPanelLabel").style.display = "none";
            document.getElementById("DirectionPanelLabel2").style.display = "none";
            let mapObj = this;
            this.calculateAndDisplayRoute(this.directionsService, this.directionsDisplay, this.chosenParkingArea, function() {
                let geolocation = new Geolocation();
                if (mapObj.wantRecordRoute) {
                    mapObj.dstMarker = new google.maps.Marker({
                        position: mapObj.dstPosition,
                        map: mapObj.mapView
                    });
                    if (mapObj.simulationMode) {
                        google.maps.event.addListener(mapObj.mapView, 'mousemove', function(event) {
                            mapObj.currentLocationMarker.setPosition(event.latLng);
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
                    } else {

                        this.intervalid = setInterval(function() {
                            geolocation.getCurrentPosition().then((position) => {
                                let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                                let distance = google.maps.geometry.spherical.computeDistanceBetween(latLng, mapObj.chosenParkingArea.position);
                                mapObj.currentLocationMarker.setPosition(latLng);
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
                    if (mapObj.simulationMode) {
                        google.maps.event.addListener(mapObj.mapView, 'mousemove', function(event) {
                            mapObj.currentLocationMarker.setPosition(event.latLng);
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
                                            handler: () => {
                                                mapObj.inNav = false;
                                                google.maps.event.clearListeners(mapObj.mapView, 'mousemove');
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
                    } else {
                        mapObj.intervalid = setInterval(function() {
                            geolocation.getCurrentPosition().then((position) => {
                                let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                                mapObj.currentLocationMarker.setPosition(latLng);
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
                                                    mapObj.inNav = false;
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
    go() {
        if (this.srcPosition && this.dstPosition) {
            this.inNav = true;
            // send to server Src and Destination
            // Server look for a path and return a result
            document.getElementById("DirectionPanelLabel").style.display = "none";
            document.getElementById("DirectionPanelLabel2").style.display = "none";
            let page = this;
            var carNumber = this.loginPage.getCarNumber();
            // what about srcName == currentLocation ? 
            this.leavePark(carNumber).then((result) => {
                var src = page.srcName;
                if (src == "Current Location") {
                    src = src + "$" + page.srcPosition.toString();
                }
                page.getBestParking(src, page.dstName, google).then((result) => {
                    page.goAux();
                });
            });
        } else {
            console.log("src or dst not defined");
        }

    }
    freeSlot() {
        //let page = this;
        var carNumber = this.loginPage.getCarNumber();
        let mapObj = this;
        this.leavePark(carNumber).then((result) => {
            console.log("left park")
            let alert = mapObj.alertCtrl.create({
                title: 'Free Parking Slot',
                message: 'You are free to go',
                buttons: [
                    {
                        text: 'OK',
                    }
                ]
            });
            alert.present();
        });
    }

    getBestParking(srcPosition, dstPosition, googleObj): Promise<boolean> {
        var devMode = true;
        var carNumber = this.loginPage.getCarNumber()
        if (!devMode && (carNumber == 'undefined' || this.loginPage.getCarNumber() == null)) {
            console.log("User not logged in !");
            return;
        }
        let mapPage = this;
        return new Promise((resolve, reject) => {
            this.locService.getBestParkingArea(carNumber, srcPosition, dstPosition, mapPage, resolve, googleObj);
        });
    }
    leavePark(carNumber): Promise<boolean> {
        return new Promise((resolve, reject) => {
            this.locService.leavePark(carNumber, resolve);
        });
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
                        this.navCtrl.push(LoginPage, { mapPage: this });
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

        var map = new google.maps.Map(this.mapElement.nativeElement, {
            zoom: 15,
            center: { lat: 32.776878, lng: 35.023106 }
        });
        this.mapView = map;
        let mapObj = this;
        var geolocation = new Geolocation();
        geolocation.getCurrentPosition().then((position) => {
            let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            map.setCenter(latLng);
            if (mapObj.currentLocationMarker) {
                mapObj.currentLocationMarker.setMap(null);
            }
            mapObj.currentLocationMarker = new google.maps.Marker({
                position: latLng,
                draggable: false,
                map: map
            });
        });
        setInterval(function() {
            if (mapObj.inNav) {
                return;
            }
            geolocation.getCurrentPosition().then((position) => {
                let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(latLng);
                if (mapObj.currentLocationMarker) {
                    mapObj.currentLocationMarker.setMap(null);
                }
                mapObj.currentLocationMarker = new google.maps.Marker({
                    position: latLng,
                    draggable: false,
                    map: map
                });
            });
        }, 30000);
        this.i = 0;
        this.directionsDisplay.setMap(map);
        this.directionsDisplay.setOptions({ suppressMarkers: true });
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
        this.directionsDisplayWalk.setOptions({ suppressMarkers: true });
        let panel2 = document.getElementsByName("test_over_map2")[0];
        panel2.style.backgroundColor = "white";
        this.directionsDisplayWalk.setPanel(panel2);
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
        if (this.voiceEnabled == false || this.useVoice == false) {
            console.log("no voice");
            return;
        }
        let currentTime = (new Date()).getTime();
        if (this.lastTimeVoice == 'undefined' || this.lastTimeVoice == null) {
            this.lastTimeVoice = currentTime;
        } else {
            if (currentTime - this.lastTimeVoice < 5000) {
                this.lastTimeVoice = currentTime;
                return;
            }
        }
        this.lastTimeVoice = currentTime;
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
            if (distance < 100) {
                this.curr_step_index++;
            }
        }
        let step = steps[this.curr_step_index]
        var text = step.instructions.replace(/<b>/g, "");
        text = text.replace(/<\/b>/g, "");
        this.tts.speak(text);
        console.log(text);
        if (this.curr_step_index >= steps.length - 1 && this.indoorDescription != null) {
            this.tts.speak(this.indoorDescription);
            console.log(this.indoorDescription);
        }
    }
    voicechanged(e: any) {
        this.useVoice = e.checked;
    }
    calculateAndDisplayRoute(directionsService, directionsDisplay, parkingArea, callback) {
        let mapObj = this;
        mapObj.directionsDisplay.setMap(mapObj.mapView);
        mapObj.directionsDisplay.setPanel(document.getElementsByName("test_over_map")[0]);
        directionsService.route({
            origin: this.srcPosition,
            destination: parkingArea.position,
            travelMode: 'DRIVING'
        }, function(response, status) {
            if (status === 'OK') {
                mapObj.directionsResponse = response;
                directionsDisplay.setDirections(response);
                parkingArea.position = new google.maps.LatLng(response.routes[0].overview_path.slice(-1)[0].lat(), response.routes[0].overview_path.slice(-1)[0].lng());
            } else {
                window.alert('Directions request failed due to ' + status);
            }
            callback();
        });
    }
    calculateAndDisplayRouteWalking(directionsService, directionsDisplay, parkingArea) {
        let mapObj = this;
        mapObj.directionsDisplayWalk.setMap(mapObj.mapView);
        mapObj.directionsDisplayWalk.setPanel(document.getElementsByName("test_over_map2")[0]);
        directionsService.route({
            origin: parkingArea.position,
            destination: this.dstPosition,
            travelMode: 'WALKING'
        }, function(response, status) {
            if (status === 'OK') {
                var data = { error: null, duration: -1, path: [], description: null };
                mapObj.pathService.getRecordedPath(parkingArea.title, mapObj.dstName, data, google, function() {
                    if (data.error || data.duration >= response.routes[0].legs[0].duration) {
                        directionsDisplay.setDirections(response);
                    } else {
                        mapObj.drawPath(data.path);
                        document.getElementsByName("panelLabel2")[0].style.display = "block";
                        document.getElementsByName("panelLabel2")[0].innerHTML = data.description;
                    }
                });
            } else {
                window.alert('Directions request failed due to ' + status);
            }
        });
    }
}