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


@Component({
  templateUrl: 'app.html'
})


export class MyApp {
  @ViewChild(Nav) nav: Nav;
  map = MapPage;
  // make HelloIonicPage the root (or first) page
  rootPage = MapPage;
  pages: Array<{title: string, component: any}>;

  constructor(
    public platform: Platform,
    public menu: MenuController,
    public statusBar: StatusBar,
    public splashScreen: SplashScreen,
    public file : File,
    public plt: Platform
  ) {

    this.initializeApp();
    MyExceptionHandler.isCordova=this.plt.is('cordova');
    MyExceptionHandler.file=file;
      
    // set our app's pages
    this.pages = [
      { title: 'Map', component: MapPage },
      { title: 'About', component: HelloIonicPage },
      { title: 'Login', component: LoginPage },
      { title: 'Register', component: RegisterPage },
      { title: 'Logout', component: LogoutPage },
      { title: 'My Details', component: MyDetailsPage }
    ];
  }
  initializeApp() {
    window.onerror = function myErrorHandler(errorMsg, url, lineNumber) {
        window.alert("Hey.. it's seems like you have and error...\nThe error message is: " + errorMsg+"\n\n\nWe hope it help you!");//or any message
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
}
