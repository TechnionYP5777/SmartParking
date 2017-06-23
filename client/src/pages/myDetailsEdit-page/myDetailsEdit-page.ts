import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { MyDetailsService } from '../../providers/myDetails-service';

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
    
     ionViewDidLoad() {
        console.log('ionViewDidLoad MyDetailsEditPage');
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

