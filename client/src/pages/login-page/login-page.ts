import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LoginService } from './login-service';
import { AlertController } from 'ionic-angular';
import { RegisterPage } from '../register-page/register-page';

@IonicPage()
@Component({
  selector: 'page-login-page',
  templateUrl: 'login-page.html',
})
export class LoginPage {
  str: any;
  num: any;
  serve: any;
  isLogin: any;

  constructor(public navCtrl: NavController, public navParams: NavParams, serve: LoginService, public alertCtrl: AlertController) {
    this.serve = serve;
    //this.num = serve.http.get('https://www.reddit.com/r/gifs/new/.json?limit=10').map(res=>res.json());
    this.isLogin = false;
  }

  ionViewDidLoad() {
  }

  //TODO: better testing if the user is not logged in.
  getInfo() {
    this.serve.getDetails().subscribe(data => {
      if (data.name == "") {
        console.log("NOT logged in.");
        this.isLogin = false;
        this.presentAlert("Wrong car number or password. please try again.", "Error Connecting");
      }
      else {
        console.log(data.name + " is logged in.");
      this.isLogin = true;
      this.presentAlert("You have sucsessfully logged in.\nWelcome, " + data.name, "Connected");
      //TODO: Change the page it is navigated to, with Zahi's guidance.
      this.navCtrl.push(RegisterPage);
      }
    }, err => {
      console.log(err);
    });
  }

  Login(carNumber, password) {
    let ref = this;

    this.serve.userLogin(carNumber, password).subscribe(() => {
      console.log("use data here");
    }, err => {
      //console.log(err);
    });

    setTimeout(function() { ref.getInfo(); }, 5000);
    
  }

  usingGet() {
    this.serve.callHttp().subscribe(data => {
      this.presentAlert("Connecting to server...", "Please wait");
      this.str = data.str;
      this.num = data.num;
    });
  }

  presentAlert(str, myTitle) {
    let alert = this.alertCtrl.create({
      title: myTitle,
      subTitle: str,
      buttons: ['OK'],
      cssClass: 'alertLogin'
    });
    alert.present();
  }
}