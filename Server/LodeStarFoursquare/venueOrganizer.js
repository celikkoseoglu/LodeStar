const request = require('request');
const express = require('express');
const async = require('async');
//const http = require('http');
const app = express();

/*
 * Jing'an sculpture park coordinates: 31.2339463,121.46388509999997
 * Jing'an Sculpture park venue_id 4c8b49986418a143bff1e7ce
 * esenboga airport coordinates: 40.12443989999999,32.991672600000015
 */

let venueSearchURL = "https://api.foursquare.com/v2/venues/search";
let venueReviewsURL = "https://api.foursquare.com/v2/venues/venue_id/tips"; //replace "venue_id" with the real venue_id of the place
let venuePhotosURL = "https://api.foursquare.com/v2/venues/venue_id/photos"; //replace "venue_id" with the real venue_id of the place

let requestQuery = {
    url: null,
    method: 'GET',
    qs: {
        client_id: 'YUYULQTBMYN3VEFRZLV4UDSWMEQDVNUBOSCTCE2ALE0VIZ12',
        client_secret: 'NMPD43K4MGD2JT4ZFQ5JX2OC1GPT5HR3PU4FZVW1IMRBDIGC',
        //near: 'Ankara,TR', //only required if ll (latitude longitude not provided)
        v: '20180414',
        limit: 1
        //radius: 250 //in meters. Only valid when used with query enabled
    }
};

function foursquareTipsRequest(query, res) {
    request(query,
        function (foursqaureErr, foursqaureRes, foursquareBody) {
            if (foursqaureErr)
                console.error(foursqaureErr);
            else {
                res.send(foursquareBody);
            }
        }
    );
}

function foursquareImagesRequest(query, res) {
    request(query,
        function (foursqaureErr, foursqaureRes, foursquareBody) {
            if (foursqaureErr)
                console.error(foursqaureErr);
            else {
                res.send(foursquareBody);
            }
        }
    );
}

function foursqaureRequest(query, res) {
    request(query,
        function (foursqaureErr, foursqaureRes, foursquareBody) {
            if (foursqaureErr) {
                console.error(foursqaureErr);
            } else {

                let data = JSON.parse(foursquareBody); //this step is required for making modifications on JSON data

                res.send(data);

                let numberOfVenuesInResponse = data.response.groups[0].items.length; // this line will throw error if daily quota exceeded

                let imageRequests = [];
                let reviewRequests = [];

                for (let i = 0; i < numberOfVenuesInResponse; i++) {

                    console.log(data.response.groups[0].items[i].venue.name);

                    let venue_id = data.response.groups[0].items[i].venue.id;
                    let venuePhotoRequestURL = venuePhotosURL.replace("venue_id", venue_id);
                    //create request query
                    let imageRequest = clone(requestQuery);
                    imageRequest.url = venuePhotoRequestURL;
                    imageRequests.push(imageRequest);

                    let venueReviewRequestURL = venueReviewsURL.replace("venue_id", venue_id);
                    //create request query
                    let reviewRequest = clone(requestQuery);
                    reviewRequest.url = venueReviewRequestURL;
                    reviewRequests.push(reviewRequest);

                }

                //crazy complicated sh1te
                function requestImage(imageQueryIter, callback) {

                    request(imageQueryIter,
                        function (foursqaureErr, foursqaureRes, foursquareBody) {
                            callback(foursqaureErr, JSON.parse(foursquareBody).response);
                        }
                    );
                }

                function imageRequestsDone(error, result) {
                    //console.log("map completed. Error: ", error, " result: ", result);

                    for (let i = 0; i < result.length; i++) {

                        let imageLink = result[i].photos.items[0];

                        if (result[i].photos.count !== 0)
                            data.response.groups[0].items[i].venueImage = imageLink.prefix + '300x300' + imageLink.suffix;
                        else //items with no picture will be owned by huseyin
                            data.response.groups[0].items[i].venueImage = "http://lodestarapp.com/images/team/huseyin_beyan_bw_optimized_overlay.jpg";
                    }

                    async.map(reviewRequests, requestReview, reviewRequestDone);
                }

                async.map(imageRequests, requestImage, imageRequestsDone);

                //crazy complicated sh1te
                function requestReview(reviewQueryIter, callback) {

                    request(reviewQueryIter,
                        function (foursqaureErr, foursqaureRes, foursquareBody) {
                            callback(foursqaureErr, JSON.parse(foursquareBody).response);
                        }
                    );
                }

                function reviewRequestDone(error, result) {
                    //console.log("map completed. Error: ", error, " result: ", result);

                    for (let i = 0; i < result.length; i++) {
                        data.response.groups[0].items[i].reviewCount = result[i].tips.count;
                    }

                    delete data.response.suggestedFilters;
                    delete data.response.warning;

                    res.send(data.response);
                }
            }
        });
}

app.get('/', (req, res) => {

    //console.log("request sent to /");
    //console.log(req.body);

    //Cloning the template. Should not modify it because the venue_id replacement header is needed for future requests to be correct.
    let venueSearchQueryClone = clone(requestQuery);

    venueSearchQueryClone.url = venueSearchURL;

    venueSearchQueryClone.qs.query = req.query.query; // coming from request parameter query
    venueSearchQueryClone.qs.ll = req.query.location; // coming from request parameter location
    venueSearchQueryClone.qs.limit = req.query.limit; // return only specified number of results from the query

    foursqaureRequest(venueSearchQueryClone, res);

});

app.get('/reviewsWithVenueID', (req, res) => {

    //console.log("request sent to /");
    //console.log(req.body);

    let venue_id = req.query.venue_id;

    //Cloning the template. Should not modify it because the venue_id replacement header is needed for future requests to be correct.
    let venueTipsQueryClone = clone(requestQuery);
    venueTipsQueryClone.url = venueReviewsURL.url.replace("venue_id", venue_id);

    foursquareTipsRequest(res, venue_id);
});

app.get('/imagesWithVenueID', (req, res) => {

    //console.log("request sent to /");
    //console.log(req.body);

    let venue_id = req.query.venue_id;
    res.send('not ready yet');
});

app.get('/test', (req, res) => {

    //console.log("request sent to /");
    //console.log(req.body);

    //Cloning the template. Should not modify it because the venue_id replacement header is needed for future requests to be correct.
    let venueSearchQueryClone = clone(requestQuery);

    venueSearchQueryClone.url = venueSearchURL;

    venueSearchQueryClone.qs.query = 'coffee'; // coming from request parameter query
    venueSearchQueryClone.qs.ll = req.query.location; // coming from request parameter location
    venueSearchQueryClone.qs.limit = req.query.limit; // return only specified number of results from the query

    foursqaureRequest(venueSearchQueryClone, res);
});


app.listen(3009, () => console.log('LodeStar Foursquare Server listening on port 3009!'));

function clone(a) {
    return JSON.parse(JSON.stringify(a));
}

//sehire göre intrinsics...