import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import { MyApp } from '../../app/app.component';


/**
 * @author DavidCohen55
 * @author Shahar-Y
 * Created: May 2017
 * 
 * This file contains the services for the login functionality
 */

@Injectable()
export class LoginService {
    constructor(public http: Http) {
        console.log('Hello LoginService Provider');
        console.log("MyApp.id: " + MyApp.id);
    }

    handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }

    getDetails() {
        var i=0
        while(!MyApp.idLoaded){
            if(i%1000==0){
                console.log('here4');
            }
            i+=1;
        }
        
        console.log('here5');
        return this.http.get('https://spring-boot-nav.herokuapp.com/User/LoginDemo/' + MyApp.id).map(res => res.json())
            .catch(this.handleError);
    }

    
    //The service that logs the user into the system.
    //Sends a post request to the heroku in order to update the condition of the user.
    userLogin(carNumber, password) {
        var value = "name=" + carNumber + "&pass=" + password;

        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');


        return this.http.post('https://spring-boot-nav.herokuapp.com/User/LoginDemo/' + MyApp.id, value, { headers: headers })
            .map(res => res.json());
    }

    //This service updates the user details from MyDetailsEdit page.
    setUserDetails(userName, phoneNum, carNum, eMail, stickerColor, oldCarNum) {
        
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        var value2 : string;
        value2 = "name=" + userName + "&phone=" + phoneNum + "&newCar=" +
            carNum + "&email=" + eMail + "&type=" + stickerColor + "&oldCar=" + oldCarNum;

        console.log("in Login setUserDetails, value = " + value2);

        return this.http.post('https://spring-boot-nav.herokuapp.com/User/ChangeDetailsDemo/' + MyApp.id, value2, { headers: headers })
            .map(res => res.json());
    }




}