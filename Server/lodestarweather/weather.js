const http = require('http');
const express = require('express');
const app = express();

const APIKey = "c00ba395bae7607aaa0cf79dd388bb2d";

app.get('/', (req, res) => {

    //units=metric in request params to get celcius, imperial to get fahrenheit
    let request = http.request("http://api.openweathermap.org/data/2.5/forecast?units=" + req.query.units + "&q=" + req.query.city + "&appid=" + APIKey, function(response) {

        let replyMessage = "";
        let jsonMessage  = "";

        response.on('data', function(data) {
            replyMessage = replyMessage + data;
        });

        response.on('end', function() {
            jsonMessage = JSON.parse(replyMessage);

            var responseMessage = [];

            //if the next available weather info is at 15:00, skip the day and start searching for the other dates after the first day
            var i = (24 - (new Date(jsonMessage["list"][0]["dt_txt"])).getHours()) / 3 + 4;

            responseMessage.push(jsonMessage["list"][0]);

            //the API returns forecasts for every 3 hours in a day. 24 / 3 = 8. i is the most recent one for today's weather
            for (i; i < jsonMessage["list"].length; i+=8)
                responseMessage.push((jsonMessage["list"][i]));

            res.send(JSON.stringify(responseMessage, null, 4));
        });

    });

    request.end();
    
});

app.listen(3005, () => console.log('LodeStar Weather listening on port 3005!'));