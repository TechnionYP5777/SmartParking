import { Component, ChangeDetectorRef  } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { MyDetailsService } from '../../providers/myDetails-service';
import { LoginPage } from '../login-page/login-page';



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
    public storeArray: Array<any> = [];
    public cdr: any;


    constructor(public navCtrl: NavController, public navParams: NavParams, MDServe: MyDetailsService,
        public alertCtrl: AlertController, private CDR: ChangeDetectorRef) {
        this.MDServe = MDServe;
        this.MDServe.getUserDetails().subscribe(data => {
            this.storeArray = [data.name, data.phoneNumber, data.carNumber, data.email, data.sticker];
            this.cdr = CDR;
            this.cdr.detectChanges();

        });
    }

    ionViewDidLoad() {
        console.log('ionViewDidLoad MyDetailsEditPage');
    }

    ChangeDetails(name, phoneNum, carNum, eMail, sticker) {
        this.MDServe.setUserDetails(name, 123456, phoneNum, carNum, eMail, sticker, this.storeArray[2]);
        this.navCtrl.push(LoginPage);
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

