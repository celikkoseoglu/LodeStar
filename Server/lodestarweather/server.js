const readline = require('readline');
const http = require('http');
const moment = require('moment');


const APIKey = "c00ba395bae7607aaa0cf79dd388bb2d";

const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];


const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.question('Enter city name: ', (city) => {

    let request = http.request("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + APIKey, function(response){

        console.log("Weather information for: " + city);

        let replyMessage = "";
        let jsonMessage  = "";

        response.on('data', function(data){
            replyMessage = replyMessage + data;
        });

        response.on('end', function(){
            jsonMessage = JSON.parse(replyMessage);
            //console.log(JSON.stringify(jsonMessage, null, 4));

            //console.log(jsonMessage["list"][1]);

            let date1 = jsonMessage["list"][0].dt;

            let temp1C = kelvinTo(0, jsonMessage["list"][0].main["temp"]);
            let temp1K = kelvinTo(1, jsonMessage["list"][0].main["temp"]);

            console.log(days[moment.unix(date1).day() - 1] + " " + parseFloat(temp1C).toFixed(2) +
                "C , " + parseFloat(temp1K).toFixed(2) + "F");
        });

        //console.log("" + ${jsonMessage.list.dt});

    });

request.end();

//kelvin to celcius - fahrenheidt
//unix time to date
//feels like temperature

rl.close();
});

function kelvinTo(what, temp) {
    if (what === 0)
        return temp - 273.15;
    return temp * (9 / 5) - 459.67;
}