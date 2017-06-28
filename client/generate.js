var c=require('crypto');
var str=c.randomBytes(Math.ceil(48)).toString('base64').slice(0,64).replace(/\+/g,'0').replace(/\//g,'0');
var fs = require('fs');
fs.writeFile("src/identity.js","define([],function(){return '"+str+"';});",function(err){
	if(err){console.log(err);}
	else{
		console.log("ok");
	}
});


