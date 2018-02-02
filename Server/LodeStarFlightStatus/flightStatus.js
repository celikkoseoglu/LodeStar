const express = require('express');
const http = require('http');
const app = express();

app.get('/', (req, res) => {

		var flightData;

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

        client.registerMethod('weatherConditions', fxmlUrl + 'WeatherConditions', 'GET');
        client.registerMethod('flightInfoStatus', fxmlUrl + 'FlightInfoStatus', 'GET');
        
        var flightInfoStatusArgs = {
        	parameters: {
        		ident: flightNumber,
        		include_ex_data: false,
        		howMany: 1
        	}
        };

        var weatherConditionsArgs = {
            parameters: {
                airport_code: originAirportCode,
                howMany: 1
            }
        };

        client.methods.flightInfoStatus(flightInfoStatusArgs, function (data, response) {

        	delete data.FlightInfoStatusResult.flights[0].faFlightID;
        	delete data.FlightInfoStatusResult.flights[0].tailnumber;
        	delete data.FlightInfoStatusResult.flights[0].type;
        	delete data.FlightInfoStatusResult.flights[0].codeshares;

            flightData = data.FlightInfoStatusResult.flights[0];

            client.methods.weatherConditions(weatherConditionsArgs, function (data, response) {
	            delete data.WeatherConditionsResult.conditions[0].airport_code;
	            delete data.WeatherConditionsResult.conditions[0].time;
	            delete data.WeatherConditionsResult.conditions[0].temp_dewpoint;
	            delete data.WeatherConditionsResult.conditions[0].wind_speed_gust;
	            delete data.WeatherConditionsResult.conditions[0].raw_data;
	            flightData.weather = data.WeatherConditionsResult.conditions[0];
	            res.send(flightData);
        	});
        });
});

app.listen(3006, () => console.log('LodeStar FlightStatus listening on port 3006!'));