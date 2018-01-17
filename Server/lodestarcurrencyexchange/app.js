const express = require('express');
const app = express();


var fx = require('money');
var oxr = require('open-exchange-rates');

oxr.set({
    app_id: '3e6839f0fda04541810a265a41b75183'
});

app.get('/', (req, res) => {

    oxr.latest(function(error) {

        if ( error ) {
            console.log( 'ERROR loading data from Open Exchange Rates API! Error was:' + error.toString() );
            res.send( 'ERROR loading data from Open Exchange Rates API! Error was:' + error.toString() );
            return false;
        }

        //res.send(oxr.rates);

        console.log( 'USD to HKD: ' + oxr.rates['HKD'] );

        fx.rates = oxr.rates;
        fx.base = oxr.base;
        var amount = fx(1).from('EUR').to('GBP').toFixed(6);
        console.log( '10 EUR to GBP: ' + amount );


        var result = "{ \"result\": " + fx(1).from(req.query.currency).to(req.query.foreignCurrency).toFixed(4) + " }";
        res.send(result);

    });
   
});

app.listen(3001, () => console.log('Example app listening on port 3001!'));

