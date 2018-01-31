const https = require('https');
var all = '';
var readline = require('readline');

var result = '';
var city = 'London'

//var pg = require("pg");
//var conString = "pg://admin:guest@localhost:5432/LivingExpenses";
//var client = new pg.Client(conString);
//client.connect();

var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.question('Enter city: ', (city) => {


let req = https.get('https://www.numbeo.com/cost-of-living/in/'+city, (res) => {
 
  res.on('data', (d) => {
    all = all + d.toString();
  });
  
  res.on('end', function() {
	    myFunction1(all);
	    myFunction2(all);
	    myFunction3(all);
	    myFunction4(all);
	    myFunction5(all);
	    myFunction6(all);
	    myFunction7(all);
	    myFunction8(all);
	    
	    output = JSON.parse(result);
	    console.log(output);
	  });

}).on('error', (e) => {
  console.error(e);
});

});

function myFunction1(str) {
	var param = 'A single person monthly costs';
    var n = str.search(param);
    //console.log(str.substr(n,param.length));
    //console.log(str.substr(n+param.length+27,10));
    result += '{ "'+str.substr(n,param.length)+ '":"'+str.substr(n+param.length+27,10)+'",';

}

function myFunction2(str) {
	var param = 'Monthly Pass';
    var n = str.search(param);
    //console.log(str.substr(n,param.length));
    //console.log(str.substr(n+param.length+88,6));
    result += '"'+str.substr(n,param.length)+ '":"'+str.substr(n+param.length+88,6)+'",';
}

function myFunction3(str) {
	var param = 'Meal, Inexpensive Restaurant';
    var n = str.search(param);
    //console.log(str.substr(n,param.length));
    //console.log(str.substr(n+param.length+57,5));
    result += '"'+str.substr(n,param.length)+ '":"'+str.substr(n+param.length+58,5)+'",';
}

function myFunction4(str) {
	var param = 'in City Centre';
    var n = str.search(param);
    //console.log('Apartment (1 bedroom) in City Centre');
    //console.log(str.substr(n+param.length+57,8));
    result += '"'+'Apartment (1 bedroom) in City Centre'+ '":"'+str.substr(n+param.length+58,8)+'",';
}

function myFunction5(str) {
	var param = 'McMeal at McDonalds';
    var n = str.search(param);
    //console.log(str.substr(n,param.length));
    //console.log(str.substr(n+param.length+84,4));
    result += '"'+str.substr(n,param.length)+ '":"'+str.substr(n+param.length+85,4)+'",';
}

function myFunction6(str) {
	var param = 'Coke/Pepsi';
    var n = str.search(param);
    //console.log('Coke/Pepsi (0.33 liter bottle)	');
    //console.log(str.substr(n+param.length+78,4));
    result += '"'+'Coke/Pepsi (0.33 liter bottle)'+ '":"'+str.substr(n+param.length+78,4)+'",';

}

function myFunction7(str) {
	var param = 'Taxi Start';
    var n = str.search(param);
    //console.log(str.substr(n,param.length));
    //console.log(str.substr(n+param.length+73,4));
    result += '"'+str.substr(n,param.length)+ '":"'+str.substr(n+param.length+74,4)+'",';
}

function myFunction8(str) {
	var param = 'Taxi 1km';
    var n = str.search(param);
    //console.log(str.substr(n,param.length));
    //console.log(str.substr(n+param.length+88,4));
    result += '"'+str.substr(n,param.length)+ '":"'+str.substr(n+param.length+88,4)+'"}';
}