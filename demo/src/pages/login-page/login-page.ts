import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LoginService } from './login-service';
import { AlertController } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-login-page',
  templateUrl: 'login-page.html',
})
export class LoginPage {
  str: any;
  num: any;
  serve: any;
  
  constructor(public navCtrl: NavController, public navParams: NavParams, serve: LoginService, public alertCtrl: AlertController) {
    this.serve = serve;
    //this.num = serve.http.get('https://www.reddit.com/r/gifs/new/.json?limit=10').map(res=>res.json());
  }

  ionViewDidLoad() {       
    //this.serve.tempLogin("3209654").then(data => {});
      //if (data) {
      //  this.str = data.json().str;
      //  this.num = data.json().num;
      //}
  }
  
  
  Login(carNumber,password){
        this.str = "BLABLA"
        this.serve.userLogin(carNumber,password).then(data => {});
        this.serve.getDetails().subscribe(data => {
          this.str = data.email;
        });
        this.presentAlert("Hello " + this.str);   
  }

  usingGet() {
    this.serve.callHttp().subscribe(data => {
      this.presentAlert("Connecting to server...");
      this.str = data.str;
      this.num = data.num;
    });
  }


  presentAlert(str) {
    let alert = this.alertCtrl.create({
      title: 'Alert',
      subTitle: str,
      buttons: ['OK']
    });
    alert.present();
  }
}
