const express = require('express');
const http = require('http');
const app = express();

function kelvinTo(what, temp) {
    if (what === 0)
        return temp - 273.15;
    return temp * (9 / 5) - 459.67;
}

app.get('/', (req, res) => {

    if(req.query.dataType == "weather") {

        const APIKey = "c00ba395bae7607aaa0cf79dd388bb2d";

        let request = http.request("http://api.openweathermap.org/data/2.5/forecast?q=" + req.query.city + "&appid=" + APIKey, function(response) {

        console.log("Weather information for: " + req.query.city);

        let replyMessage = "";
        let jsonMessage  = "";

        response.on('data', function(data) {
            replyMessage = replyMessage + data;
        });

        response.on('end', function() {
            jsonMessage = JSON.parse(replyMessage);

            res.send(JSON.stringify(jsonMessage, null, 4));

            //console.log(jsonMessage["list"][1]);

            let date1 = jsonMessage["list"][0].dt;

            let temp1C = kelvinTo(0, jsonMessage["list"][0].main["temp"]);
            let temp1K = kelvinTo(1, jsonMessage["list"][0].main["temp"]);

            //console.log(days[moment.unix(date1).day() - 1] + " " + parseFloat(temp1C).toFixed(2) + "C , " + parseFloat(temp1K).toFixed(2) + "F");
        });

        //console.log("" + ${jsonMessage.list.dt});

        });

        request.end();

        //res.send('you requested weather info for city: ' + req.query.city);


    }

    else if (req.query.dataType == "flightInfo") {

        //res.send('you requested flight info for no: ' + req.query.flightNo);

        var Client = require('node-rest-client').Client;

        var username = 'celikk';
        var apiKey = 'da281dea3e4f760167cab8801a9c7c2d516acd5a';
        var fxmlUrl = 'https://flightxml.flightaware.com/json/FlightXML3/';

        var client_options = {
            user: username,
            password: apiKey
        };

        var client = new Client(client_options);

        client.registerMethod('findFlights', fxmlUrl + 'FindFlight', 'GET');
        //client.registerMethod('weatherConditions', fxmlUrl + 'WeatherConditions', 'GET');

        var findFlightArgs = {
            parameters: {
                origin: 'ESB',
                destination: 'PVG'
            }
        };

        client.methods.findFlights(findFlightArgs, function (data, response) {
            //console.log(JSON.stringify(data.FindFlightResult.num_flights, null, 4));
            //console.log(JSON.stringify(data.FindFlightResult.flights[0], null, 4));

            console.log(data.FindFlightResult.flights[0].segments[0].ident);

            for (var i = 0; i < Object.keys(data.FindFlightResult.flights[0].segments).length; i++) {
                if (data.FindFlightResult.flights[0].segments[i].ident == "THY26")
                    res.send(JSON.stringify(data.FindFlightResult.flights[0].segments[i], null, 4));
            }
        });

        /*var weatherConditionsArgs = {
            parameters: {
                airport_code: 'PVG'
            }
        };

        client.methods.weatherConditions(weatherConditionsArgs, function (data, response) {
            console.log(JSON.stringify(data.WeatherConditionsResult.conditions[0], null, 4));
        });*/
    }

    else if (req.query.dataType == "nearbyVenues") {
        var coordinates = "40.7243,-74.0018"; //is to be replaced with req.query.coordinates
    }
});

app.listen(3001, () => console.log('Example app listening on port 3001!'));