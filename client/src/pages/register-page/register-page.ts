import { RegisterService } from '../../providers/register-service';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
/**
 * Generated class for the RegisterPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-register-page',
  templateUrl: 'register-page.html',
})
export class RegisterPage {
    serve: any;
  constructor(public navCtrl: NavController, public navParams: NavParams, serve: RegisterService, public alertCtrl: AlertController) {
    this.serve = serve;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');
  }

  Register(userName, password, phoneNum, carNum, eMail, stickerColor) {
   // let ref = this;
    
    this.serve.userRegister(userName, password, phoneNum, carNum, eMail, stickerColor).subscribe(() => {
            console.log("Register working");
        }, err => {
            console.log(err);
        });

    //setTimeout(function() { ref.getInfo(); }, 5000);
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