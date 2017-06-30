/**

choosing-page-module 
@author zahimizrahi
@since 2017-03-27

**/

import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ChoosingPage } from './choosing-page';

@NgModule({
  declarations: [
    ChoosingPage,
  ],
  imports: [
    IonicPageModule.forChild(ChoosingPage),
  ],
  exports: [
    ChoosingPage
  ]
})
export class ChoosingPageModule {}                                         
