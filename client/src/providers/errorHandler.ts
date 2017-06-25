import {ErrorHandler} from '@angular/core';

export class MyExceptionHandler implements ErrorHandler {
  static file:any;
  static isCordova:boolean
  handleError(error) {
    let time = new Date();
    if(MyExceptionHandler.isCordova){
        MyExceptionHandler.file.writeFile(MyExceptionHandler.file.externalApplicationStorageDirectory, 
           "log", error.toString(),
           {replace: true});
        console.log(MyExceptionHandler.file.externalApplicationStorageDirectory);
        console.log("gt log");
    }else{
        console.log(error)
    }
    window.alert("there's some kind of error:\n"+error+"\n probably need to restart\n");
  }
}