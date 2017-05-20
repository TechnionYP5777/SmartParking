import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';

import { HelloIonicPage } from '../pages/hello-ionic/hello-ionic';
import { LoginPage } from '../pages/login-page/login-page';
import { RegisterPage } from '../pages/register-page/register-page';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { MapPage } from '../pages/map/map';
import { ChoosingPage } from '../pages/choosing-page/choosing-page';


import {LoginService} from '../pages/login-page/login-service';
import { RegisterService } from '../providers/register-service';
import { HttpModule } from '@angular/http';

@NgModule({
  declarations: [
    MyApp,
    HelloIonicPage,
    LoginPage,
    RegisterPage,
    MapPage,
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
    RegisterPage,
    MapPage,
    ChoosingPage
  ],
  providers: [
    StatusBar,
    LoginService,
    RegisterService,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}