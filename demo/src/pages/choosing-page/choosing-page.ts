import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

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
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  	this.sources = [
	{title:"Taub Building",position:20},
	{title:"Ulman Building",position:30}];
	this.dests = [
        {title:"Taub Building",position:20},
        {title:"Ulman Building",position:30}];
        this.srcCallBack = navParams.get('srcCallBack');
        this.destCallBack = navParams.get('destCallBack');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ChoosingPage');
  }
  goback() {
     console.log("here");
     this.navCtrl.pop();
  }
  rememberDest(dict:any){
        this.destCallBack(dict["position"]);
  	console.log(dict);
  }
  rememberSrc(dict:any){
        this.srcCallBack(dict["position"]);
  	console.log(dict);
  }

}
