import { Component, ChangeDetectorRef  } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { MyDetailsService } from '../../providers/myDetails-service';
import { LoginService } from '../login-page/login-service';
import { HelloIonicPage } from '../hello-ionic/hello-ionic';
import { MyApp } from '../../app/app.component';


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

    //Shows the current values of the user details in the boxes of MyDetailsEdit page
    updateFields() {
        this.MDServe.getUserDetails().subscribe(data => {
            this.storeArray = [data.name, data.phoneNumber, data.carNumber, data.email, data.sticker];
            this.cdr.detectChanges();
        });

    }

    //This method is called when the user presses the 'Change Details' button. 
    ChangeDetails(name, phoneNum, carNum, eMail, sticker) {
        this.LoginServe.setUserDetails(name, phoneNum, carNum, eMail, sticker, this.storeArray[2]).subscribe(() => {
            var newDetails = '<br\><br\>' + "User Name: " + name + '<br\><br\>' + "Phone: " + phoneNum + '<br\><br\>' + "Car: " + carNum + '<br\><br\>' +
                "Mail: " + eMail + '<br\><br\>' + "Sticker: " + sticker;
            this.presentAlert("Your Details were changed." + '\n' + newDetails, "Change Details Success!");
            this.updateFields();
        }, err => {
            this.presentAlert("There was an error with the request. Please try again.", "Error");
            console.log(err);
        });
        MyApp.currPage='About';
        this.navCtrl.push(HelloIonicPage);

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

