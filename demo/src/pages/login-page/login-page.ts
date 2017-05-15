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

  ionViewDidLoad() { }

  getInfo() {
    this.serve.getDetails().subscribe(data => {
      console.log("get data: " + data.name);
    });
  }

  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  Login(carNumber, password) {
    let ref = this;
  
    this.serve.userLogin(carNumber, password).subscribe(() => {
      console.log("use data here");
    }, err => {
      console.log(err);
    });

    setTimeout(function() {
      ref.getInfo();
      
    }, 5000);
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