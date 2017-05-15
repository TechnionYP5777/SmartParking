import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';

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
  constructor(public navCtrl: NavController, public navParams: NavParams) {
        this.googleObj = navParams.get('googleObj');
  	this.sources =  
	[{title:"Taub Building", position: new this.googleObj.maps.LatLng(32.7787,35.0)},
	{title:"Ulman Building",position: new this.googleObj.maps.LatLng(18.210885,-67.140884)}];
	this.dests = [
        {title:"Taub Building", position: new this.googleObj.maps.LatLng(18.210885,-67.140884)},
        {title:"Ulman Building", position:  new this.googleObj.maps.LatLng(32.7,35.0)}];
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
