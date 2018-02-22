const request = require('request');

request({
    url: 'https://api.foursquare.com/v2/venues/explore',
    method: 'GET',
    qs: {
        client_id: 'YUYULQTBMYN3VEFRZLV4UDSWMEQDVNUBOSCTCE2ALE0VIZ12',
        client_secret: 'NMPD43K4MGD2JT4ZFQ5JX2OC1GPT5HR3PU4FZVW1IMRBDIGC',
        //ll: '40.7243,-74.0018',
        //near: 'Ankara,TR',
        ll: '40.115078394119855,32.99340509342176',
        //query: 'coffee',
        v: '20180221'
        //limit: 2
    }
}, function(err, res, body) {
    if (err) {
        console.error(err);
    } else {

        var data = JSON.parse(body);

        console.log(JSON.stringify(data.response, null, 4));

    }
});