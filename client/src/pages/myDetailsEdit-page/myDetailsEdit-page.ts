import { Component, ChangeDetectorRef  } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { MyDetailsService } from '../../providers/myDetails-service';
import { LoginPage } from '../login-page/login-page';
import { LoginService } from '../login-page/login-service';




/**
 * @author Shahar-Y
 * Created: 23.6.17
 * 
 * myDetailsEdit page
 */

@Component({
    selector: 'page-myDetailsEdit-page',
    templateUrl: 'myDetailsEdit-page.html'
})
export class MyDetailsEditPage {
    MDServe: any;
    LoginServe: LoginService;
    public storeArray: Array<any> = [];
    public cdr: any;


    constructor(public navCtrl: NavController, public navParams: NavParams, MDServe: MyDetailsService, LoginServe: LoginService,
        public alertCtrl: AlertController, private CDR: ChangeDetectorRef) {
        this.MDServe = MDServe;
        this.LoginServe = LoginServe;
        this.cdr = CDR;
        this.updateFields();

    }

    ionViewDidLoad() {
        console.log('ionViewDidLoad MyDetailsEditPage');
    }

    updateFields() {
        this.MDServe.getUserDetails().subscribe(data => {
            this.storeArray = [data.name, data.phoneNumber, data.carNumber, data.email, data.sticker];
            this.cdr.detectChanges();
        });

    }

    ChangeDetails(name, phoneNum, carNum, eMail, sticker) {

        this.LoginServe.setUserDetails(name, "123456", phoneNum, carNum, eMail, sticker, this.storeArray[2]).subscribe(() => {
            console.log("ChangeDetails: " + name);
        }, err => {
            console.log(err);
        });

        
        //this.navCtrl.push(LoginPage);
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

