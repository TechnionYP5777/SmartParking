import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {LoginService} from './login-service';
import { AlertController } from 'ionic-angular';


@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})

export class LoginPage {
  str: any;
  num: any;
  serve : any;
  constructor(public navCtrl: NavController, public navParams: NavParams,serve: LoginService, public alertCtrl: AlertController) {
  this.serve = serve;
  //this.num = serve.http.get('https://www.reddit.com/r/gifs/new/.json?limit=10').map(res=>res.json());
  }
  ionViewDidLoad() {
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