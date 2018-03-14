const express = require('express');
const app = express();
const fx = require('money');
const oxr = require('open-exchange-rates');
var fs = require('fs');

oxr.set({
    app_id: '3e6839f0fda04541810a265a41b75183'
});


var obj;
fs.readFile('common-currency.json', 'utf8', function (err, data) {
    if (err) throw err;
    obj = JSON.parse(data);
});

app.get('/', (req, res) => {

    let localCurrency = req.query.localCurrency;
    let foreignCurrency = req.query.foreignCurrency;
    let standardCurrecny1 = 'EUR';
    let standardCurrency2 = 'USD';
    let precision = 4; //number of decimals after .

    if (localCurrency == null || foreignCurrency == null)
        res.send("localCurrency or foreignCurrency param null");
    else {
        oxr.latest(function (error) {

            if (error) {
                console.log('ERROR loading data from Open Exchange Rates API! Error was:' + error.toString());
                res.send('ERROR loading data from Open Exchange Rates API! Error was:' + error.toString());
                return false;
            }

            fx.rates = oxr.rates;
            fx.base = oxr.base;

            let response = new Object();

            response.foreignOverLocal = parseFloat(fx(1).from(localCurrency).to(foreignCurrency).toFixed(precision));
            response.localOverForeign = parseFloat(fx(1).from(foreignCurrency).to(localCurrency).toFixed(precision));
            response.eurosOverForeign = parseFloat(fx(1).from(standardCurrecny1).to(foreignCurrency).toFixed(precision));
            response.usdOverForeign = parseFloat(fx(1).from(standardCurrency2).to(foreignCurrency).toFixed(precision));
            response.localCurrencySymbol = obj[localCurrency]['symbol'];
            response.foreignCurrencySymbol = obj[foreignCurrency]['symbol'];

            res.send(JSON.stringify(response));

        });
    }
});

app.listen(3008, () => console.log('LodeStar Currency Exchange Listening on Port 3008'));

