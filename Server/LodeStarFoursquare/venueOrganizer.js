const request = require('request');
const express = require('express');
const http = require('http');
const app = express();

/*
 * Jing'an sculpture park coordinates: 31.2339463,121.46388509999997
 * Jing'an Sculpture park venue_id 4c8b49986418a143bff1e7ce
 */

var venueSearchQuery = {
    url: 'https://api.foursquare.com/v2/venues/explore',
    method: 'GET',
    qs: {
        client_id: 'YUYULQTBMYN3VEFRZLV4UDSWMEQDVNUBOSCTCE2ALE0VIZ12',
        client_secret: 'NMPD43K4MGD2JT4ZFQ5JX2OC1GPT5HR3PU4FZVW1IMRBDIGC',
        ll: null,
        //near: 'Ankara,TR', //only required if ll (latitude longitude not provided)
        query: null,
        v: '20180304',
        limit: 0
        //radius: 250 //in meters. Only valid when used with query enabled
    }
};

var venueTipsQuery = {
    url: "https://api.foursquare.com/v2/venues/venue_id/tips", //replace "venue_id" with the real venue_id of the place
    method: 'GET',
    qs: {
        client_id: 'YUYULQTBMYN3VEFRZLV4UDSWMEQDVNUBOSCTCE2ALE0VIZ12',
        client_secret: 'NMPD43K4MGD2JT4ZFQ5JX2OC1GPT5HR3PU4FZVW1IMRBDIGC',
        v: '20180304',
        limit: 20
    }
};

var venueImagesQuery = {
    url: "https://api.foursquare.com/v2/venues/venue_id/photos", //replace "venue_id" with the real venue_id of the place
    method: 'GET',
    qs: {
        client_id: 'YUYULQTBMYN3VEFRZLV4UDSWMEQDVNUBOSCTCE2ALE0VIZ12',
        client_secret: 'NMPD43K4MGD2JT4ZFQ5JX2OC1GPT5HR3PU4FZVW1IMRBDIGC',
        v: '20180304',
        limit: 20
    }
};

function foursquareTipsRequest(res, venue_id) {

    //Cloning the template. Should not modify it because the venue_id replacement header is needed for future requests to be correct.
    var venueTipsQueryClone = Object.assign(venueTipsQuery);
    venueTipsQueryClone.url = venueTipsQueryClone.url.replace("venue_id", venue_id);

    request(venueTipsQueryClone,

        function (foursqaureErr, foursqaureRes, foursquareBody) {

            venueTipsQueryClone = null; // set arc reference to zero for garbage collection

            if (foursqaureErr)
                console.error(foursqaureErr);
            else {
                //var data = JSON.parse(foursquareBody);

                //pre.tips = data.response;

                //res.send(pre);

                res.send(foursquareBody);
            }
        }
    );
}

function foursqaureSingleRequest(res) {
    request(venueSearchQuery,

        function (foursqaureErr, foursqaureRes, foursquareBody) {

            if (foursqaureErr)
                console.error(foursqaureErr);
            else {
                let data = JSON.parse(foursquareBody);
                res.send(data.response/*["groups"][0]["items"][0]*/); // comment this

                //foursquareTipsRequest(res, data.response["groups"][0]["items"][0]);
            }
        });
}

function foursqaureRequest(query ,res) {
    request(query,

        function (foursqaureErr, foursqaureRes, foursquareBody) {
            if (foursqaureErr) {
                console.error(foursqaureErr);
            } else {

                let data = JSON.parse(foursquareBody);
                res.send(data.response);

            }
        });
}

app.get('/', (req, res) => {

    var venueSearchQueryClone = Object.assign(venueSearchQuery);

    venueSearchQueryClone.qs.query = req.query.query; // coming from request parameter query
    venueSearchQueryClone.qs.ll = req.query.location; // coming from request parameter location
    venueSearchQueryClone.qs.limit = req.query.limit; // return only specified number of results from the query

foursqaureRequest(venueSearchQueryClone ,res);

});

app.get('/reviewsWithVenueID', (req, res) => {
    let venue_id = req.query.venue_id;
    foursquareTipsRequest(res, venue_id);
});

app.get('/imagesWithVenueID', (req, res) => {
    let venue_id = req.query.venue_id;
    res.send('not ready yet');
});

app.listen(3008, () => console.log('LodeStar Foursquare Server listening on port 3008!'));