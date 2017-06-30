/**

app.component.ts - check the entries of the menu 
@author zahimizrahi
@author Shahar-Y
@author shaysegal 
@author DavidCohen55
@author sefialbo 
@since 2017-03-27

**/


import { Component, ViewChild } from '@angular/core';

import { Platform, MenuController, Nav } from 'ionic-angular';

import { HelloIonicPage } from '../pages/hello-ionic/hello-ionic';
import { LoginPage } from '../pages/login-page/login-page';
import { RegisterPage } from '../pages/register-page/register-page';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { MapPage } from '../pages/map/map';
import { LogoutPage } from '../pages/logout-page/logout-page';
import { MyDetailsPage } from '../pages/myDetails-page/myDetails-page';
import { File } from '@ionic-native/file';
import { MyExceptionHandler } from '../providers/errorHandler';
declare function require(name: string);

@Component({
    templateUrl: 'app.html'
})


export class MyApp {
    @ViewChild(Nav) nav: Nav;
    map = MapPage;
    // make HelloIonicPage the root (or first) page
    rootPage = MapPage;
    pages: Array<{ title: string, component: any }>;
    NotLoggedPages: Array<{ title: string, component: any }>;
    static isLoggedIn: any;
    static id: string;
    static menuCtrl: MenuController;
    
    content:any;
    constructor(
        public platform: Platform,
        public menu: MenuController,
        public statusBar: StatusBar,
        public splashScreen: SplashScreen,
        public file: File,
        public plt: Platform,
        menuCtrl: MenuController
    ) {

        this.initializeApp();
        MyExceptionHandler.isCordova = this.plt.is('cordova');
        MyExceptionHandler.file = file;
        // set our app's pages
        this.pages = [
            { title: 'Map', component: MapPage },
            { title: 'About', component: HelloIonicPage },
            { title: 'My Details', component: MyDetailsPage },
            { title: 'Logout', component: LogoutPage },
        ];
        this.NotLoggedPages = [
            { title: 'Map', component: MapPage },
            { title: 'About', component: HelloIonicPage },
            { title: 'Register', component: RegisterPage },
            { title: 'Login', component: LoginPage },
        ];
        
        MyApp.menuCtrl = menuCtrl;
        setTimeout(function(){
            MyApp.updateMenu();
        },1000);

    }


    static updateMenu() {
        MyApp.menuCtrl.enable(!MyApp.isLoggedIn, 'NotLogged');
        MyApp.menuCtrl.enable(MyApp.isLoggedIn, 'Menu');
    }



    initializeApp() {
        this.getUniqueID(this.file, this.plt.is('cordova'));
        window.onerror = function myErrorHandler(errorMsg, url, lineNumber) {
            window.alert("Hey.. it's seems like you have and error...\nThe error message is: " + errorMsg + "\n\n\nWe hope it help you!");//or any message
            return false;
        }
        this.platform.ready().then(() => {
            // Okay, so the platform is ready and our plugins are available.
            // Here you can do any higher level native things you might need.

            this.statusBar.styleDefault();
            this.splashScreen.hide();
        });
    }

    openPage(page) {
        // close the menu when clicking a link from the menu
        this.menu.close();
        // navigate to the new page if it is not the current page
        this.nav.setRoot(page.component);
    }
    getUniqueID(file: File, isCordova: boolean) {
        if (!isCordova) {
            let id = require("../identity");
            MyApp.id = id;
        } else {
            file.checkFile(file.externalApplicationStorageDirectory, "identity").then((res) => {
                if (res) {
                    file.readAsText(file.externalApplicationStorageDirectory, "identity").then((res) => {
                        MyApp.id = res;
                    });
                }
            }).catch((err) => {
                let c = require('crypto');
                let id = c.randomBytes(Math.ceil(48)).toString('base64').slice(0, 64).replace(/\+/g, '0').replace(/\//g, '0');
                file.writeFile(file.externalApplicationStorageDirectory, "identity", id, { replace: true });
                MyApp.id = id;
            });
        }
    }

}
