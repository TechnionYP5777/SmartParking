import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { Logout } from './logout';

@NgModule({
  declarations: [
    Logout,
  ],
  imports: [
    IonicPageModule.forChild(Logout),
  ],
  exports: [
    Logout
  ]
})
export class LogoutModule {}
