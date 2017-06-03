import { RegisterService } from '../../providers/register-service';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AlertController } from 'ionic-angular';
import { LoginPage } from '../login-page/login-page';


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
    let ref = this; 
    this.serve.userRegister(userName, password, phoneNum, carNum, eMail, stickerColor).subscribe(data => {
      
    }, err => {
      console.log(err);
    });
    this.navCtrl.push(LoginPage);
    //setTimeout(function() { ref.getRegisterInfo(); }, 5000);
    
  }
    
    getRegisterInfo(){
      this.serve.getRegisterData().subscribe(data => {
      this.presentAlert("Register is successful!" + data, "Register");
      this.navCtrl.push(LoginPage);
      });
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