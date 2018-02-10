const https = require('https');

const express = require('express');
const app = express();

var result = '';
var all = '';

app.get('/', (req, res) => {
    let request = https.request("https://www.numbeo.com/cost-of-living/in/" + req.query.city, function(response) {
        all = '';
        response.on('data', function(data) {
            all = all + data.toString();
        });

        response.on('end', function() {
            myFunction1(all);
            myFunction2(all);
            myFunction3(all);
            myFunction4(all);
            myFunction5(all);
            myFunction6(all);
            myFunction7(all);
            myFunction8(all);

            if(all.includes("Our system cannot find city with named with"))
                result = '{}';

            //there has to be a better way to create a json object rather than relying on string operations. Added to future work list.
            res.send(JSON.parse(result));
        });
    });

    request.end();
});

app.listen(3007, () => console.log('LodeStar Living Expenses listening on port 3007!'));

function myFunction1(str) {
    var param = 'A single person monthly costs';
    var n = str.search(param);
    result = '{ "' + str.substr(n, param.length) + '":"' + str.substr(n + param.length + 27, 10) + '",';
    
}

function myFunction2(str) {
    var param = 'Monthly Pass';
    var n = str.search(param);
    result += '"' + str.substr(n, param.length) + '":"' + str.substr(n + param.length + 88, 6) + '",';
}

function myFunction3(str) {
    var param = 'Meal, Inexpensive Restaurant';
    var n = str.search(param);
    result += '"' + str.substr(n, param.length) + '":"' + str.substr(n + param.length + 58, 5) + '",';
}

function myFunction4(str) {
    var param = 'in City Centre';
    var n = str.search(param);
    result += '"' + 'Apartment (1 bedroom) in City Centre' + '":"' + str.substr(n + param.length + 58, 8) + '",';
}

function myFunction5(str) {
    var param = 'McMeal at McDonalds';
    var n = str.search(param);
    result += '"' + str.substr(n, param.length) + '":"' + str.substr(n + param.length + 85, 4) + '",';
}

function myFunction6(str) {
    var param = 'Coke/Pepsi';
    var n = str.search(param);
    result += '"' + 'Coke/Pepsi (0.33 liter bottle)' + '":"' + str.substr(n + param.length + 78, 4) + '",';

}

function myFunction7(str) {
    var param = 'Taxi Start';
    var n = str.search(param);
    result += '"' + str.substr(n, param.length) + '":"' + str.substr(n + param.length + 74, 4) + '",';
}

function myFunction8(str) {
    var param = 'Taxi 1km';
    var n = str.search(param);
    result += '"' + str.substr(n, param.length) + '":"' + str.substr(n + param.length + 88, 4) + '"}';
}