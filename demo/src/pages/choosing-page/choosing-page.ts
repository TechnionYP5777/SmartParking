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
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  	this.sources = [
	{title:"taub",position:null},
	{title:"ulman",position:null}];
	this.dests = [
        {title:"taub",position:null},
        {title:"ulman",position:null}];
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ChoosingPage');
  }
  goback() {
     console.log("here");
     this.navCtrl.pop();
  }

}
