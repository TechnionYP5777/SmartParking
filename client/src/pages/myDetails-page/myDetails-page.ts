import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { MyDetailsService } from '../../providers/myDetails-service';
import { MyDetailsEditPage } from '../myDetailsEdit-page/myDetailsEdit-page';


/**
 * @author Shahar-Y
 * Created: 18.6.17
 * 
 * myDetails page
 */

@Component({
    selector: 'page-myDetails-page',
    templateUrl: 'myDetails-page.html'
})
export class MyDetailsPage {
    serve: any;
    public storeArray: Array<any> = [];


    constructor(public navCtrl: NavController, public navParams: NavParams, serve: MyDetailsService,
        public alertCtrl: AlertController) {
        this.serve = serve;
        this.serve.getUserDetails().subscribe(data => {
            //this.presentAlert("Your Data: " + JSON.stringify(data), "MyData");
            this.storeArray = [data.name, data.phoneNumber, data.carNumber, data.email, data.sticker];
        });

        this.serve.getParkingArea().subscribe(data => {
            //this.presentAlert("Your Data: " + JSON.stringify(data), "MyData");
            this.storeArray[5] = [data.name];
        });

    }

    EditDetails() {
        this.navCtrl.push(MyDetailsEditPage);
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

