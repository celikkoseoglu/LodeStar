


var fx = require('money'),
    oxr = require('open-exchange-rates');

oxr.set({
    app_id: '3e6839f0fda04541810a265a41b75183'
});

oxr.latest(function(error) {

    if ( error ) {
        console.log( 'ERROR loading data from Open Exchange Rates API! Error was:' )
        console.log( error.toString() );

        return false;
    }
    console.log( 'USD to HKD: ' + oxr.rates['HKD'] );

    fx.rates = oxr.rates;
    fx.base = oxr.base;
    var amount = fx(10).from('EUR').to('GBP').toFixed(6);
    console.log( '10 EUR to GBP: ' + amount );

});