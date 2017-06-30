import { Injectable } from '@angular/core';

import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import { MyExceptionHandler } from './errorHandler';
import { File } from '@ionic-native/file';
import { Platform } from 'ionic-angular';
declare function require(name: string);


/**
 * @author Shahar-Y
 * Created: 30.6.17
 * 
 * This file contains the service of getting the unique identifier of the device.
 */

@Injectable()
export class IdentifierService {
    id: any;
    constructor(public file: File, public plt: Platform) {

        console.log('Hello IdentifierService Provider');
        MyExceptionHandler.isCordova = this.plt.is('cordova');
        MyExceptionHandler.file = file;
        console.log("");
    }
    getID() {
        console.log("MyExceptionHandler.file: " + MyExceptionHandler.file + "MyExceptionHandler.isCordova: " + MyExceptionHandler.isCordova);
        this.getUniqueID(MyExceptionHandler.file, MyExceptionHandler.isCordova)
    }

    getUniqueID(file: File, isCordova: boolean) {
        if (!isCordova) {
            let id = require("../identity");
            this.id = id;
        } else {
            file.checkFile(file.externalApplicationStorageDirectory, "identity").then((res) => {
                if (res) {
                    file.readAsText(file.externalApplicationStorageDirectory, "identity").then((res) => {
                        this.id = res;
                    });
                }
            }).catch((err) => {
                let c = require('crypto');
                let id = c.randomBytes(Math.ceil(48)).toString('base64').slice(0, 64).replace(/\+/g, '0').replace(/\//g, '0');
                file.writeFile(file.externalApplicationStorageDirectory, "identity", id, { replace: true });
                this.id = id;
            });
        }
    }





    handleError(error) {
        console.error(error);
        return Observable.throw('Server error');
    }

}