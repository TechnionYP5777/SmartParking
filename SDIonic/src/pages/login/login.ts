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
  //this.str="hi";
  this.presentAlert("BOOMMMMM");
  //this.num="hello";
  this.serve.callHttp().subscribe(data => {
  this.presentAlert("BLIBLIBLI");
    this.str = data.str;
  this.num = data.num;
  this.presentAlert("blablabla");
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