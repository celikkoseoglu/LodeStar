const express = require('express');
const http = require('http');
const app = express();

app.get('/', (req, res) => {

        var originAirportCode = req.query.originAirportCode;
        var destinationAirportCode = req.query.destinationAirportCode;
        var flightNumber = req.query.flightNumber;

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
        var findFlightArgs = {
            parameters: {
                origin: originAirportCode,
                destination: destinationAirportCode
            }
        };

        client.registerMethod('weatherConditions', fxmlUrl + 'WeatherConditions', 'GET');

        client.methods.findFlights(findFlightArgs, function (data, response) {
            //console.log(JSON.stringify(data.FindFlightResult.num_flights, null, 4));
            //console.log(JSON.stringify(data.FindFlightResult.flights[0], null, 4));

            console.log(data.FindFlightResult.flights[0].segments[0].ident);
            console.log(data);

            for (var i = 0; i < Object.keys(data.FindFlightResult.flights[0].segments).length; i++) {
                if (data.FindFlightResult.flights[0].segments[i].ident == flightNumber)
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
});

app.listen(3006, () => console.log('Example app listening on port 3006!'));