import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';
import { AlertController } from 'ionic-angular';

/**
 * @author Shahar-Y
 * Created: 14.6.17
 * 
 * This file contains all the services required for the logout functionality
 */

@Injectable()
export class LogoutService {
    constructor(public http: Http, public alertCtrl: AlertController) {
        console.log('Hello LogoutService Provider');
    }
   
    userLogout() {

      var value = "name=" + "" + "&pass=" + "";
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

      /*this.presentAlert(this.http.post('http://localhost:8080/User/Try', value, { headers: headers })
            .map(res => res.json()).toArray, "Connected");*/
      //this.http.delete('http://localhost:8080/User');
       return this.http.post('http://localhost:8080/User/Login', value, { headers: headers })
            .map(res => res.json());
      
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
  /*getRegisterData() {
        return this.http.get('http://localhost:8080/User/Register').map(res => res.json())
            .catch(this.handleError);
    }*/
  
  handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }
  
}