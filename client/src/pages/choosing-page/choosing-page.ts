import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LocationService } from '../../providers/location-service';
declare var google;
 /**
 * Generated class for the ChoosingPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-choosing-page',
  templateUrl: 'choosing-page.html'
})
export class ChoosingPage {
  sources:Array<{title: string, position:any}>;
  dests:Array<{title: string, position:any}>;
  sourceLoc: any;
  destLoc: any;
  srcCallBack: any;
  destCallBack: any;
  goCallBack: any;
  mapPage: any;
  googleObj: any;
  constructor(public navCtrl: NavController, public navParams: NavParams, public locService: LocationService ) {
        this.googleObj = navParams.get('googleObj');
        this.sources=[];
        this.dests=[];
        locService.getLocations(this.sources,this.dests,this.googleObj);
        this.mapPage = navParams.get('mapPage');
        google.maps.event.clearListeners(this.mapPage.mapView, 'mousemove');
   
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ChoosingPage');
  }
  goback() {
     console.log("here");
     this.mapPage.go();
     this.navCtrl.pop();
  }
  rememberDest(dict:any){
        this.mapPage.setDstPosition(dict["position"],dict["title"]);
  }
  rememberSrc(dict:any){
        this.mapPage.setSrcPosition(dict["position"]);
  }
  changeToRecord(value:any){
	this.mapPage.wantRecordRoute=value.checked;
  	console.log(value);
  }
  changeToSimulation(value:any){
    if(value.checked){
         google.maps.event.addListener(this.mapPage.mapView, 'mousemove', function (event) {
                  console.log(event.latLng);
         }); 
    }
    else{
        google.maps.event.clearListeners(this.mapPage.mapView, 'mousemove');
    }
  }
}
