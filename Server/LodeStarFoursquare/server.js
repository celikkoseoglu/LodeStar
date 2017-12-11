const request = require('request');

var coordinates1 = 40.7243;
var coordinates2 = -74.0018;

var coorString = coordinates1 + ',' + coordinates2

request({
    url: 'https://api.foursquare.com/v2/venues/search',
    method: 'GET',
    qs: {
        client_id: '2ZXD11VBHAMIQQMD02LAPSMDUFF0HA5TMG0CERZQKRTT3GB2',
        client_secret: '3JXYJDDAXS4ULVFZR2Z4EVIILRBREM0K11LNTURJRXVDAIVY',
        ll: '40.7243,-74.0018',
        //near: 'Ankara,TR',
        query: 'coffee',
        v: '20170801',
        limit: 7
    }
}, function(err, res, body) {
    if (err) {
        console.error(err);
    } else {

        var data = JSON.parse(body);

        console.log(JSON.stringify(data.response.venues, null, 4));

    }
});