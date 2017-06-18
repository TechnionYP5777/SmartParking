import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';

/**
 * @author Shahar-Y
 * Created: 18.6.17
 * 
 * myDetails page
 */

@Component({
  selector: 'page-myDetails-page',
  templateUrl: 'myDetails-page.html'
})
export class MyDetailsPage {
  
  
  constructor(public navCtrl: NavController, public navParams: NavParams,
   public alertCtrl: AlertController) {

  }
  
  
}

