const request = require('request');
const express = require('express');
const app = express();

let requestQuery = {
    url: "https://maps.googleapis.com/maps/api/place/textsearch/json",
    method: 'GET',
    qs: {
        query: '+point+of+interest',
        language: 'en',
        type: 'point_of_interest',
        key: 'AIzaSyCnY6cljLR3vRDuCSdI0yOzDVyaLyfseRI'
    }
};

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


app.listen(3010, () => console.log('LodeStar Landmarks Server listening on port 3010!'));

function clone(a) {
    return JSON.parse(JSON.stringify(a));
}