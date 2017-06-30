/**

login-page - logins to the application 
@author zahimizrahi
@author davidcohen55
@author Shahar-Y
@author sefialbo
@author shaysegal 
@since 2017-03-27

**/

import { Component } from '@angular/core';
import { IonicPage, NavController } from 'ionic-angular';
import { LoginService } from './login-service';
import { AlertController } from 'ionic-angular';
import { HelloIonicPage } from '../hello-ionic/hello-ionic'
import { RegisterPage } from '../register-page/register-page';
import { LogoutService } from '../../providers/logout-service';
import { MyApp } from '../../app/app.component';

/**
 * @author DavidCohen55
 * @author Shahar-Y
 * Created: May 2017
 * 
 * This file contains the methods for the login functionality
 */

@IonicPage()
@Component({
    selector: 'page-login-page',
    templateUrl: 'login-page.html',
})
export class LoginPage {
    str: any;
    num: any;
    serve: any;
    logoutService: any;
    carNumber: any;
    page: any;
    idfy: string;
    constructor(public navCtrl: NavController,
        serve: LoginService, public alertCtrl: AlertController, logoutService: LogoutService) {
        this.serve = serve;
        this.logoutService = logoutService;
        this.idfy = MyApp.id;

    }

    ionViewDidLoad() {
        this.carNumber = null;
        console.log('ionViewDidLoad LoginPage');
    }



    getUserData(myData: {
        name: string, phoneNumber: string, carNumber: string,
        email: string, sticker: string
    }): Promise<boolean> {
        console.log("getUserData() myData before: " + JSON.stringify(myData));
        return new Promise((resolve, reject) => {
            this.serve.getDetails().subscribe(data => {
                console.log("getUserData() data: " + JSON.stringify(data));
                MyApp.isLoggedIn = true;
                MyApp.updateMenu();
                myData.name = data.name;
                myData.phoneNumber = data.phoneNumber;
                myData.carNumber = data.carNumber;
                myData.email = data.email;
                myData.sticker = data.sticker;
                resolve(true);
                console.log("getUserData() myData after: " + JSON.stringify(myData));
            }, err => {
                console.log("getUserData error: " + err);
                MyApp.isLoggedIn = false;
                MyApp.updateMenu();
                reject(true);
            });
        });
    }


    getInfo() {
        this.serve.getDetails().subscribe(data => {
            if (data.name == "") {
                console.log("NOT logged in.");
                this.presentAlert("Wrong car number or password. please try again.", "Error Connecting");
            }
            else {
                console.log(data.name + " is logged in.");
                MyApp.isLoggedIn = true;
                MyApp.updateMenu();
                this.navCtrl.setRoot(HelloIonicPage);
            }
        }, err => {
            console.log(err);
        });


    }
    getCarNumber() {
        return this.carNumber;
    }

    Login(carNumber, password) {

        /*console.log("BEFORE this.navParams.get(mapPage) : " + this.navParams.get("mapPage").isLogin + ",this.page.isLogin: " + this.page.isLogin);
        this.page.isLogin = true;
        console.log("AFTER this.navParams.get(mapPage) : " + this.navParams.get("mapPage").isLogin + ",this.page.isLogin: " + this.page.isLogin);
*/


        let ref = this;
        ref.carNumber = carNumber;
        this.serve.userLogin(carNumber, password).subscribe(() => {
            console.log("use data here");
        }, err => {
            console.log(err);
        });

        setTimeout(function() { ref.getInfo(); }, 6000);

    }


    navigateRegister() {
        this.navCtrl.setRoot(RegisterPage);
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