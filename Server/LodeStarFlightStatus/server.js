const express = require('express');
const app = express();

app.get('/', (req, res) => {

    if(req.query.dataType == "weather") {

        res.send('your requested weather info for city: ' + req.query.city);
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

app.listen(3001, () => console.log('Example app listening on port 3000!'));