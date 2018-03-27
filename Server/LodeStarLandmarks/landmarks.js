const request = require('request');
const express = require('express');
const app = express();

let API_KEY = 'AIzaSyCnY6cljLR3vRDuCSdI0yOzDVyaLyfseRI';

let requestQuery = {
    url: "https://maps.googleapis.com/maps/api/place/textsearch/json",
    method: 'GET',
    qs: {
        query: '+point+of+interest',
        language: 'en',
        type: 'point_of_interest',
        key: API_KEY
    }
};

let placeDetailsRequestQuery = {
    url: "https://maps.googleapis.com/maps/api/place/details/json",
    method: "GET",
    qs: {
        placeid: "",
        key: API_KEY
    }
}

app.get('/', (req, res) => {

    let requestQueryClone = clone(requestQuery);

    requestQueryClone.qs.query = req.query.city + requestQueryClone.qs.query; // if the request parameter city == ankara; then query = ankara+point+of+interest

    request(requestQueryClone,
        function (foursqaureErr, foursqaureRes, foursquareBody) {
            if (foursqaureErr) {
                console.error(foursqaureErr);
            } else {

                let data = JSON.parse(foursquareBody); //this step is required for making modifications on JSON data.

                delete data.html_attributions;
                delete data.next_page_token;

                res.send(data.results);
            }
        });
});

app.get('/placeDetails', (req, res) => {

    let placeDetailsRequestQueryClone = clone(placeDetailsRequestQuery);

    placeDetailsRequestQueryClone.qs.placeid = req.query.placeid;

    request(placeDetailsRequestQueryClone,
        function (requestErr, response, responseBody) {
            if (requestErr) {
                console.error(requestErr);
            } else {

                let data = JSON.parse(responseBody); //this step is required for making modifications on JSON data.

                delete data.html_attributions;

                res.send(data.result);
            }
        });
});


app.listen(3010, () => console.log('LodeStar Landmarks Server listening on port 3010!'));

function clone(a) {
    return JSON.parse(JSON.stringify(a));
}