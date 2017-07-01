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
import {TextToSpeech} from '@ionic-native/text-to-speech';
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
    AllPages: any;
    static isLoggedIn: any;
    static id: string;
    static menuCtrl: MenuController;
    static currPage: any;

    content: any;
    constructor(
        public platform: Platform,
        public menu: MenuController,
        public statusBar: StatusBar,
        public splashScreen: SplashScreen,
        public file: File,
        public plt: Platform,
        menuCtrl: MenuController,
        private tts: TextToSpeech
    ) {
        this.getUniqueID(this.file, this.plt.is('cordova'));
        this.initializeApp();
        MyExceptionHandler.isCordova = this.plt.is('cordova');
        MyExceptionHandler.file = file;

        // set our app's pages
        let condition = !this.plt.is('core') && !this.plt.is('mobileweb')
        if (condition == true) {
            this.tts.speak("Welcome to Smart Parking");
        }
        MyApp.currPage = 'Map';
        this.AllPages = [
            { title: 'Map', component: MapPage },
            { title: 'About', component: HelloIonicPage },
            { title: 'My Details', component: MyDetailsPage },
            { title: 'Logout', component: LogoutPage },
            { title: 'Login', component: LoginPage },
            { title: 'Register', component: RegisterPage },
        ];
        // set our app's pages
        this.pages = [
            this.AllPages[this.pageToIdx('Map')],
            this.AllPages[this.pageToIdx('About')],
            this.AllPages[this.pageToIdx('My Details')],
            this.AllPages[this.pageToIdx('Logout')],
        ];
        this.NotLoggedPages = [
            this.AllPages[this.pageToIdx('Map')],
            this.AllPages[this.pageToIdx('About')],
            this.AllPages[this.pageToIdx('Login')],
            this.AllPages[this.pageToIdx('Register')],
        ];

        MyApp.menuCtrl = menuCtrl;
        setTimeout(function() {
            MyApp.updateMenu();
        }, 5000);
    }


    pageToIdx(page) {
        switch (page) {
            case 'Map':
                return 0;
            case 'About':
                return 1;
            case 'My Details':
                return 2;
            case 'Logout':
                return 3;
            case 'Login':
                return 4;
            case 'Register':
                return 5;
        }
        return 6;
    }

    idxToPage(idx: string) {
        switch (idx) {
            case '0':
                return 'Map';
            case '1':
                return 'About';
            case '2':
                return 'My Details';
            case '3':
                return 'Logout';
            case '4':
                return 'Login';
            case '5':
                return 'Register';
        }
        return 'NonePage';
    } 

    getCorrectMenuPages(logged){
        let pagesArray :Array<{ title: string, component: any }> ;
        pagesArray=[];
        if(logged){
            for(let i in [0,1,2,3]){
                if(MyApp.currPage!=this.idxToPage(i)){
                    pagesArray.push(this.AllPages[i]);
                }
            }
        }    
        else{
            for(let i in [0,1,4,5]){
                if(MyApp.currPage!=this.idxToPage(i)){
                    pagesArray.push(this.AllPages[i]);
                }
            }
        }
        return pagesArray;
    }

    static updateMenu() {
        MyApp.menuCtrl.enable(!MyApp.isLoggedIn, 'NotLogged');
        MyApp.menuCtrl.enable(MyApp.isLoggedIn, 'Menu');
    }



    initializeApp() {
        
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
        MyApp.currPage = page.title;

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
            file.readAsText(file.externalApplicationStorageDirectory, "identity").then((res) => {
                MyApp.id = res;
            }).catch((err) => {
                let c = require('crypto');
                let id = c.randomBytes(Math.ceil(48)).toString('base64').slice(0, 64).replace(/\+/g, '0').replace(/\//g, '0');
                file.writeFile(file.externalApplicationStorageDirectory, "identity", id, { replace: true });
                MyApp.id = id;
            });
        }
    }

}
