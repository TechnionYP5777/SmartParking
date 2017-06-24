import {ErrorHandler} from '@angular/core';

export class MyExceptionHandler implements ErrorHandler {
  static file:any;
  static isCordova:boolean
  handleError(error) {
    let time = new Date();
    if(MyExceptionHandler.isCordova){
        MyExceptionHandler.file.writeFile(MyExceptionHandler.file.externalApplicationStorageDirectory, "log"+time.toString(), error, null)
    
    }else{
        console.log(error)
    }
    window.alert("there's some kind of error:\n"+error+"\n probably need to restart\n");
  }
}