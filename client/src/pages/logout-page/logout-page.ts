import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { LogoutService } from '../../providers/logout-service';
import { LoginPage } from '../login-page/login-page';
import { MyApp } from '../../app/app.component';


/**
 * @author Shahar-Y
 * Created: 14.6.17
 * 
 * The logout page
 */

@Component({
    selector: 'page-logout-page',
    templateUrl: 'logout-page.html'
})
export class LogoutPage {

    serve: any;
    page: any;
    constructor(public navCtrl: NavController, public navParams: NavParams,
        serve: LogoutService, public alertCtrl: AlertController) {
        this.serve = serve;
        this.page = this.navParams.get("mapPage");
        console.log("BEFORE this.navParams.get(mapPage): " + this.navParams.get("mapPage"));
    }

    Logout() {
        this.serve.userLogout().subscribe(data => {

        }, err => {
            //console.log(err);
        });
        console.log("Logging out");
        console.log("AFTER this.navParams.get(mapPage): " + this.navParams.get("mapPage"));
        MyApp.isLoggedIn = false;
        this.navCtrl.setRoot(LoginPage);
    }

}

