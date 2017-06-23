import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { MyDetailsService } from '../../providers/myDetails-service';

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
    }

    ChangeDetails() {

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

