const https = require('https');
var all = '';

var pg = require("pg");
var conString = "pg://admin:guest@localhost:5432/LivingExpenses";
var client = new pg.Client(conString);
client.connect();


https.get('https://www.numbeo.com/cost-of-living/in/London', (res) => {
 
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
	  });

}).on('error', (e) => {
  console.error(e);
});

function myFunction1(str) {
	var param = 'A single person monthly costs';
    var n = str.search(param);
    console.log(str.substr(n,param.length));
    console.log(str.substr(n+param.length+27,10));
}


function myFunction2(str) {
	var param = 'Monthly Pass';
    var n = str.search(param);
    console.log(str.substr(n,param.length));
    console.log(str.substr(n+param.length+88,6));
}

function myFunction3(str) {
	var param = 'Meal, Inexpensive Restaurant ';
    var n = str.search(param);
    console.log(str.substr(n,param.length));
    console.log(str.substr(n+param.length+57,5));
}

function myFunction4(str) {
	var param = 'in City Centre ';
    var n = str.search(param);
    console.log('Apartment (1 bedroom) in City Centre');
    console.log(str.substr(n+param.length+57,8));
}

function myFunction5(str) {
	var param = 'McMeal at McDonalds ';
    var n = str.search(param);
    console.log(str.substr(n,param.length));
    console.log(str.substr(n+param.length+84,4));
}

function myFunction6(str) {
	var param = 'Coke/Pepsi';
    var n = str.search(param);
    console.log('Coke/Pepsi (0.33 liter bottle)	');
    console.log(str.substr(n+param.length+78,4));
}

function myFunction7(str) {
	var param = 'Taxi Start ';
    var n = str.search(param);
    console.log(str.substr(n,param.length));
    console.log(str.substr(n+param.length+73,4));
}

function myFunction8(str) {
	var param = 'Taxi 1km';
    var n = str.search(param);
    console.log(str.substr(n,param.length));
    console.log(str.substr(n+param.length+88,4));
}