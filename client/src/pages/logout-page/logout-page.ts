import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { LogoutService } from '../../providers/logout-service';

/**
 * @author Shahar-Y
 * Created: 6.14.17
 * 
 * The logout page
 */

@Component({
  selector: 'page-logout-page',
  templateUrl: 'logout-page.html'
})
export class LogoutPage {
  
  serve: any;
  
  constructor(public navCtrl: NavController, public navParams: NavParams,
     serve: LogoutService, public alertCtrl: AlertController) {
    this.serve = serve;
  }
  
  Logout(){
    this.serve.userLogout();
  }
  
}

