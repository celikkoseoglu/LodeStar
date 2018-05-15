const express = require('express');
const http = require('http');
const app = express();


var mockResponse = "{\"ident\":\"THY26\",\"airline\":\"THY\",\"airline_iata\":\"TK\",\"flightnumber\":\"26\",\"blocked\":false,\"diverted\":false,\"cancelled\":false,\"origin\":{\"code\":\"LTBA\",\"city\":\"Istanbul\",\"alternate_ident\":\"IST\",\"airport_name\":\"Istanbul Ataturk Int'l\"},\"destination\":{\"code\":\"ZSPD\",\"city\":\"Shanghai\",\"alternate_ident\":\"PVG\",\"airport_name\":\"Shanghai Pudong Int'l\"},\"filed_ete\":36300,\"filed_airspeed_kts\":417,\"distance_filed\":4994,\"filed_departure_time\":{\"epoch\":1518824700,\"tz\":\"+03\",\"dow\":\"Saturday\",\"time\":\"02:45AM\",\"date\":\"02/17/2018\",\"localtime\":1518835500},\"estimated_departure_time\":{\"epoch\":1518821700,\"tz\":\"+03\",\"dow\":\"Saturday\",\"time\":\"01:55AM\",\"date\":\"02/17/2018\",\"localtime\":1518832500},\"actual_departure_time\":{\"epoch\":0},\"departure_delay\":-3000,\"filed_arrival_time\":{\"epoch\":1518861000,\"tz\":\"CST\",\"dow\":\"Saturday\",\"time\":\"05:50PM\",\"date\":\"02/17/2018\",\"localtime\":1518889800},\"estimated_arrival_time\":{\"epoch\":1518858000,\"tz\":\"CST\",\"dow\":\"Saturday\",\"time\":\"05:00PM\",\"date\":\"02/17/2018\",\"localtime\":1518886800},\"actual_arrival_time\":{\"epoch\":0},\"arrival_delay\":-3000,\"status\":\"Scheduled\",\"progress_percent\":-1,\"aircrafttype\":\"B77W\",\"full_aircrafttype\":\"B77W\",\"adhoc\":false,\"weather\":{\"cloud_friendly\":\"Scattered clouds\",\"clouds\":[{\"symbol\":\"FEW013\",\"type\":\"FEW\",\"altitude\":1300},{\"symbol\":\"BKN070\",\"type\":\"BKN\",\"altitude\":7000}],\"conditions\":\"\",\"pressure\":1018,\"pressure_units\":\"mb\",\"temp_air\":8,\"temp_relhum\":78,\"temp_perceived\":\"4\",\"visibility\":9999,\"visibility_units\":\"meters\",\"wind_friendly\":\"Windy\",\"wind_direction\":40,\"wind_speed\":15,\"wind_units\":\"KT\"}}";
var ist_london = "{\"ident\":\"THY1979\",\"airline\":\"THY\",\"airline_iata\":\"TK\",\"flightnumber\":\"1979\",\"blocked\":false,\"diverted\":false,\"cancelled\":false,\"origin\":{\"code\":\"LTBA\",\"city\":\"Istanbul\",\"alternate_ident\":\"IST\",\"airport_name\":\"Istanbul Ataturk Int'l\"},\"destination\":{\"code\":\"EGLL\",\"city\":\"London, England\",\"alternate_ident\":\"LHR\",\"airport_name\":\"London Heathrow\"},\"filed_ete\":13500,\"filed_airspeed_kts\":333,\"distance_filed\":1564,\"filed_departure_time\":{\"epoch\":1525754700,\"tz\":\"+03\",\"dow\":\"Tuesday\",\"time\":\"07:45AM\",\"date\":\"05/08/2018\",\"localtime\":1525765500},\"estimated_departure_time\":{\"epoch\":1525755300,\"tz\":\"+03\",\"dow\":\"Tuesday\",\"time\":\"07:55AM\",\"date\":\"05/08/2018\",\"localtime\":1525766100},\"actual_departure_time\":{\"epoch\":0},\"departure_delay\":600,\"filed_arrival_time\":{\"epoch\":1525768200,\"tz\":\"BST\",\"dow\":\"Tuesday\",\"time\":\"09:30AM\",\"date\":\"05/08/2018\",\"localtime\":1525771800},\"estimated_arrival_time\":{\"epoch\":1525768800,\"tz\":\"BST\",\"dow\":\"Tuesday\",\"time\":\"09:40AM\",\"date\":\"05/08/2018\",\"localtime\":1525772400},\"actual_arrival_time\":{\"epoch\":0},\"arrival_delay\":600,\"status\":\"Scheduled\",\"progress_percent\":-1,\"aircrafttype\":\"B77W\",\"full_aircrafttype\":\"B77W\",\"adhoc\":false,\"originWeather\":{\"cloud_friendly\":\"Scattered clouds\",\"clouds\":[{\"symbol\":\"FEW004\",\"type\":\"FEW\",\"altitude\":400},{\"symbol\":\"BKN010\",\"type\":\"BKN\",\"altitude\":1000},{\"symbol\":\"BKN030\",\"type\":\"BKN\",\"altitude\":3000}],\"conditions\":\"BR\",\"pressure\":1010,\"pressure_units\":\"mb\",\"temp_air\":16,\"temp_relhum\":94,\"temp_perceived\":\"16\",\"visibility\":4000,\"visibility_units\":\"meters\",\"wind_friendly\":\"Light winds\",\"wind_direction\":200,\"wind_speed\":6,\"wind_units\":\"KT\"}}";

app.get('/', (req, res) => {

		var flightData;

        var originAirportCode = req.query.originAirportCode;
        var destinationAirportCode = req.query.destinationAirportCode;
        var flightNumber = req.query.flightNumber;

        var Client = require('node-rest-client').Client;

        //var username = 'celikk';
        //var apiKey = 'da281dea3e4f760167cab8801a9c7c2d516acd5a';

        var username = 'huseyinbeyan'
        var apiKey = '174d3147d2c1d6829e2eeb328a11f19dfb438e2f';

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

        /*client.methods.flightInfoStatus(flightInfoStatusArgs, function (flightData, response) {

        	delete flightData.FlightInfoStatusResult.flights[0].faFlightID;
        	delete flightData.FlightInfoStatusResult.flights[0].tailnumber;
        	delete flightData.FlightInfoStatusResult.flights[0].type;
        	delete flightData.FlightInfoStatusResult.flights[0].codeshares;

            responseData = flightData.FlightInfoStatusResult.flights[0];

            var weatherConditionsArgs = {
                parameters: {
                    airport_code: originAirportCode,
                    howMany: 1
                }
            };

            client.methods.weatherConditions(weatherConditionsArgs, function (weatherData, response) {

	            delete weatherData.WeatherConditionsResult.conditions[0].airport_code;
	            delete weatherData.WeatherConditionsResult.conditions[0].time;
	            delete weatherData.WeatherConditionsResult.conditions[0].temp_dewpoint;
	            delete weatherData.WeatherConditionsResult.conditions[0].wind_speed_gust;
	            delete weatherData.WeatherConditionsResult.conditions[0].raw_data;
	            responseData.originWeather = weatherData.WeatherConditionsResult.conditions[0];
	            res.send(responseData);
        	});
        });*/

        res.send(ist_london);
});

app.listen(3006, () => console.log('LodeStar FlightStatus listening on port 3006!'));