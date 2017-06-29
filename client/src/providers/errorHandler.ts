import {ErrorHandler} from '@angular/core';

export class MyExceptionHandler implements ErrorHandler {
  static file:any;
  static isCordova:boolean
  handleError(error) {
    //let time = new Date();
    if(MyExceptionHandler.isCordova){
        MyExceptionHandler.file.writeFile(MyExceptionHandler.file.externalApplicationStorageDirectory, 
           "log",(new Date()).getTime() +"/n"+ error.stack.toString(),
           {replace: true});
        console.log(MyExceptionHandler.file.externalApplicationStorageDirectory);
    }else{
        console.log(error.stack)
    }
    if(String(error).indexOf("google")!=-1){
        window.alert("The app don't recognize google.\nyou probably not connected to the internet.\nplease connect and restart the app.");
    }if(String(error).indexOf("PositionError")!=-1){
        window.alert("The app can't find your location.\nplease enable GPS or internet ");
    }
    else if(String(error).indexOf("URL")!=-1){
        window.alert("problem with Server,\nplease check that you are connected to the internet.\nif you are connected please try to restart");
    }  
    else{  
        window.alert("there's some kind of error:\n"+error+"\n probably need to restart\n");
    }
  }
}