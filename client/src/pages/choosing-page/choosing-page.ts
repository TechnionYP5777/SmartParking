import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
//import { Geolocation } from '@ionic-native/geolocation';
import { LocationService } from '../../providers/location-service';
/**
 * Generated class for the ChoosingPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-choosing-page',
  templateUrl: 'choosing-page.html',
  providers: [LocationService]
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
  constructor(public navCtrl: NavController, public navParams: NavParams, private locService: LocationService ) {
        this.googleObj = navParams.get('googleObj');
        this.sources=[];
        this.dests=[];
        locService.getLocations(this.sources,this.dests,this.googleObj);
        this.mapPage = navParams.get('mapPage');
   
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
        this.mapPage.setDstPosition(dict["position"]);
  	console.log(dict);
  }
  rememberSrc(dict:any){
        this.mapPage.setSrcPosition(dict["position"]);
  	console.log(dict);
  }
  changeToRecord(value:any){
	this.mapPage.recordRoute=value.checked;
  	console.log(value);
  }
}
