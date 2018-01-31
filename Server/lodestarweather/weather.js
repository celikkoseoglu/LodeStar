const http = require('http');
const express = require('express');
const app = express();

const APIKey = "c00ba395bae7607aaa0cf79dd388bb2d";

app.get('/', (req, res) => {

    let request = http.request("http://api.openweathermap.org/data/2.5/forecast?q=" + req.query.city + "&appid=" + APIKey, function(response) {

    console.log("Weather information for: " + req.query.city);

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
            console.log((new Date(jsonMessage["list"][0]["dt_txt"])).getHours());

            responseMessage.push(jsonMessage["list"][0]);

            for (i; i < jsonMessage["list"].length; i+=8) {
                responseMessage.push((jsonMessage["list"][i]));
                //var weatherDate = new Date(jsonMessage["list"][i]["dt_txt"]);
                //console.log(weatherDate.getHours());
            }

            console.log(responseMessage);

            res.send(JSON.stringify(responseMessage, null, 4));

            //console.log(jsonMessage["list"][1]);
        });

    });

    request.end();
    
});

app.listen(3005, () => console.log('LodeStar Weather listening on port 3005!'));

//kelvin to celcius - fahrenheidt
//unix time to date
//feels like temperature
function kelvinTo(what, temp) {
    if (what === 0)
        return temp - 273.15;
    return temp * (9 / 5) - 459.67;
}