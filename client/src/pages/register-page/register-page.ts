import { RegisterService } from '../../providers/register-service';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { HelloIonicPage } from '../hello-ionic/hello-ionic';
import { MyApp } from '../../app/app.component';


/**
 * Generated class for the RegisterPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
/**
 * @author zahimizrahi 
 * @author DavidCohen55
 * @author Shahar-Y
 * Created: May 2017
 * 
 * This file contains the methods for the register functionality
 */
@IonicPage()
@Component({
    selector: 'page-register-page',
    templateUrl: 'register-page.html',
})
export class RegisterPage {
    serve: any;
    response: any;
    regiStarted: any;
    constructor(public navCtrl: NavController, public navParams: NavParams, serve: RegisterService, public alertCtrl: AlertController) {
        this.serve = serve;
        this.regiStarted=false;
    }

    ionViewDidLoad() {
        console.log('ionViewDidLoad RegisterPage');
    }

    getRegisterInfo() {
        this.serve.getRegisterData().subscribe(data => {
            if (data.status == "Success") {
                this.presentAlert("Register is successful!", "Register");
                MyApp.currPage='About';
                this.navCtrl.push(HelloIonicPage);
                MyApp.isLoggedIn = true;
                //MyApp.updateMenu();
            } else {
                this.presentAlert(data.error + ". Please try again.", "Register");
            }
        });
        this.regiStarted=false;
        
    }

    Register(userName, password, phoneNum, carNum, eMail, stickerColor) {
        this.regiStarted=true;
        let ref = this;
        this.response = false;
        setTimeout(function() {
            if (!ref.response)
                ref.presentAlert("It seems like you are not connected to the internet!", "Connection Error");
        }, 15000);
        this.serve.userRegister(userName, password, phoneNum, carNum, eMail, stickerColor).subscribe(data => {
            ref.response = true;
        }, err => {
            console.log(err);
            //this means the error is not because of bad connection
            if (err != "Server error") {
                ref.response = true;
            }
        });

        setTimeout(function() { ref.getRegisterInfo(); }, 5000);

    }



    presentAlert(str, myTitle) {
        let alert = this.alertCtrl.create({
            title: myTitle,
            subTitle: str,
            buttons: ['OK']
        });
        alert.present();
    }

}