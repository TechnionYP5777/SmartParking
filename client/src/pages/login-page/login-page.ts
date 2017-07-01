/**
 * @author DavidCohen55
 * @author Shahar-Y
 * @author zahimizrahi
 * @author sefialbo
 * @author shaysegal 
 * Created: 2017-03-27
 * 
 * This file contains the methods for the login functionality
 */

import { Component } from '@angular/core';
import { IonicPage, NavController } from 'ionic-angular';
import { LoginService } from './login-service';
import { AlertController } from 'ionic-angular';
import { HelloIonicPage } from '../hello-ionic/hello-ionic'
import { RegisterPage } from '../register-page/register-page';
import { LogoutService } from '../../providers/logout-service';
import { MyApp } from '../../app/app.component';



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
    response: any;
    loginStarted :any;
    constructor(public navCtrl: NavController,
        serve: LoginService, public alertCtrl: AlertController, logoutService: LogoutService) {
        this.serve = serve;
        this.logoutService = logoutService;
        this.idfy = MyApp.id;
        this.response = false;
        this.loginStarted = false;
    }

    ionViewDidLoad() {
        this.carNumber = null;
        console.log('ionViewDidLoad LoginPage');
    }


    //pronotes a GET method to get the user data from the heroku, and update it.
    getUserData(myData: {
        name: string, phoneNumber: string, carNumber: string,
        email: string, sticker: string
    }): Promise<boolean> {
        return new Promise((resolve, reject) => {
            this.serve.getDetails().subscribe(data => {
                MyApp.isLoggedIn = true;
                //MyApp.updateMenu();
                myData.name = data.name;
                myData.phoneNumber = data.phoneNumber;
                myData.carNumber = data.carNumber;
                myData.email = data.email;
                myData.sticker = data.sticker;
                resolve(true);
            }, err => {
                console.log("getUserData error: " + err);
                MyApp.isLoggedIn = false;
                //MyApp.updateMenu();
                reject(true);
            });
        });
    }

    //Checks if the user entered valid details and presents an alert accordingly.
    //If the details are correct, it logs in.
    getInfo() {
        this.serve.getDetails().subscribe(data => {
            this.response = true;
            if (data.name == "") {
                console.log("NOT logged in.");
                this.presentAlert("Wrong car number or password. please try again.", "Error Connecting");
            }
            else {
                console.log(data.name + " is logged in.");
                MyApp.isLoggedIn = true;
                //MyApp.updateMenu();
                MyApp.currPage='About';
                this.navCtrl.setRoot(HelloIonicPage);
            }
        }, err => {
            console.log(err);
            //this means the error is not because of bad connection
            if (err != "Server error") {
                this.response = true;
            }

        });
        this.loginStarted = false;
    }
    getCarNumber() {
        return this.carNumber;
    }
    
    //The main login method, called after the user enters the details, 
    // and presses "SIGN IN" button.
    Login(carNumber, password) {
        this.loginStarted = true;
        this.response = false;
        let ref = this;
        ref.carNumber = carNumber;
        setTimeout(function() {
            if (!ref.response)
                ref.presentAlert("It seems like you are not connected to the internet!", "Connection Error");
        }, 15000);

        this.serve.userLogin(carNumber, password).subscribe(() => {
            ref.response = true;
        }, err => {
            console.log(err);
            //this means the error is not because of bad connection
            if (err != "Server error") {
                ref.response = true;
            }
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