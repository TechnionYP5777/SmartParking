import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule } from 'ionic-angular';
import { MyApp } from './app.component';

import { HelloIonicPage } from '../pages/hello-ionic/hello-ionic';
import { LoginPage } from '../pages/login-page/login-page';
import { RegisterPage } from '../pages/register-page/register-page';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { MapPage } from '../pages/map/map';
import { ChoosingPage } from '../pages/choosing-page/choosing-page';
import { LogoutPage } from '../pages/logout-page/logout-page';
import { MyDetailsPage } from '../pages/myDetails-page/myDetails-page';
import { MyDetailsEditPage } from '../pages/myDetailsEdit-page/myDetailsEdit-page';

import { LocationService } from '../providers/location-service';
import {LoginService} from '../pages/login-page/login-service';
import { RegisterService } from '../providers/register-service';
import { LogoutService } from '../providers/logout-service';
import { MyDetailsService } from '../providers/myDetails-service';
import { MyExceptionHandler }from '../providers/errorHandler';

import { TextToSpeech } from '@ionic-native/text-to-speech';
import { HttpModule } from '@angular/http';
import {File} from '@ionic-native/file';

@NgModule({
  declarations: [
    MyApp,
    HelloIonicPage,
    LoginPage,
    RegisterPage,
    MapPage,
    LogoutPage,
    MyDetailsPage,
    MyDetailsEditPage,
    ChoosingPage
  ],
  imports: [
    BrowserModule,
    HttpModule,
    IonicModule.forRoot(MyApp),
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HelloIonicPage,
    LoginPage,
    LogoutPage,
    MyDetailsPage,
    MyDetailsEditPage,
    RegisterPage,
    MapPage,
    ChoosingPage
  ],
  providers: [
    StatusBar,
    LocationService,
    MyDetailsService,
    LoginService,
    RegisterService,
    LogoutService,
    SplashScreen,
    {provide: ErrorHandler, useClass: MyExceptionHandler},
    TextToSpeech,
    File
  ]
})
export class AppModule {}
