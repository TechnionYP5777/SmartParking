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
    response: any;
    serve: any;
    page: any;
    constructor(public navCtrl: NavController, public navParams: NavParams,
        serve: LogoutService, public alertCtrl: AlertController) {
        this.serve = serve;
        this.page = this.navParams.get("mapPage");
        console.log("BEFORE this.navParams.get(mapPage): " + this.navParams.get("mapPage"));
    }

    Logout() {
        this.response = false;
        setTimeout(function() {
            if (!ref.response)
                ref.presentAlert("It seems like you are not connected to the internet!", "Connection Error");
        }, 15000);
        let ref = this;

        this.serve.userLogout().subscribe(data => {
            ref.response = true;
        }, err => {
            console.log(err);
            //this means the error is not because of bad connection
            if (err != "Server error") {
                ref.response = true;
            }
        });
        console.log("Logging out");
        console.log("AFTER this.navParams.get(mapPage): " + this.navParams.get("mapPage"));
        MyApp.isLoggedIn = false;
        MyApp.currPage = 'Login';
        this.navCtrl.setRoot(LoginPage);
    }

    
        presentAlert(str, myTitle) {
        let alert = this.alertCtrl.create({
            title: myTitle,
            subTitle: str,
            buttons: ['OK'],
            cssClass: 'alertLogin'
        });
        alert.present();
    }
}

