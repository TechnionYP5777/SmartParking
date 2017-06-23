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

    
    //In this case, logout means that the "eampty user" is logged in.
    userLogout() {
        var value = "name=" + "" + "&pass=" + "";
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        /*this.presentAlert(this.http.post('https://spring-boot-nav.herokuapp.com/User/Try', value, { headers: headers })
              .map(res => res.json()).toArray, "Connected");*/
        //this.http.delete('https://spring-boot-nav.herokuapp.com/User');
        return this.http.post('https://spring-boot-nav.herokuapp.com/User/Login', value, { headers: headers })
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
          return this.http.get('https://spring-boot-nav.herokuapp.com/User/Register').map(res => res.json())
              .catch(this.handleError);
    }*/
  
    handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }

}