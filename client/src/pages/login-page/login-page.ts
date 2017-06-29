import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LoginService } from './login-service';
import { AlertController } from 'ionic-angular';
import { HelloIonicPage } from '../hello-ionic/hello-ionic'
import { RegisterPage } from '../register-page/register-page';
import { LogoutService } from '../../providers/logout-service';

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

    constructor(public navCtrl: NavController, public navParams: NavParams,
        serve: LoginService, public alertCtrl: AlertController, logoutService: LogoutService) {
        this.serve = serve;
        this.logoutService = logoutService;
        this.page = this.navParams.get("mapPage");
        //this.num = serve.http.get('https://www.reddit.com/r/gifs/new/.json?limit=10').map(res=>res.json());
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
                    this.page.isLogin = true;
                    myData.name=data.name;
                    myData.phoneNumber=data.phoneNumber;
                    myData.carNumber=data.carNumber;
                    myData.email=data.email;
                    myData.sticker=data.sticker;
                    resolve(true);      
                    console.log("getUserData() myData after: " + JSON.stringify(myData));     
            }, err => {
                console.log("getUserData error: " + err);
                this.page.isLogin = false;
                reject(true);
            });
        });
    }


    //TODO: better testing if the user is not logged in.
    getInfo() {
        this.serve.getDetails().subscribe(data => {
            if (data.name == "") {
                console.log("NOT logged in.");
                this.presentAlert("Wrong car number or password. please try again.", "Error Connecting");
            }
            else {
                console.log(data.name + " is logged in.");
                this.page.isLogin = true;
                //this.presentAlert("You have sucsessfully logged in.\nWelcome, " + data.name, "Connected");
                this.navCtrl.push(HelloIonicPage);
            }
        }, err => {
            console.log(err);
        });

        /*if (!this.isLogin) {
            console.log("line 60 in");
            this.logoutService.userLogout().subscribe(() => {
                console.log("un-log in");
            }, err => {
            console.log(err);
        });
        }*/

    }
    getCarNumber() {
      return this.carNumber;
    }
    
    Login(carNumber, password) {
        let ref = this;
        ref.carNumber = carNumber;
        this.serve.userLogin(carNumber, password).subscribe(() => {
            console.log("use data here");
        }, err => {
            //console.log(err);
        });

        setTimeout(function() { ref.getInfo(); }, 6000);

    }


    navigateRegister() {
        this.navCtrl.push(RegisterPage);
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