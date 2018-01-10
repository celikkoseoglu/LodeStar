const readline = require('readline');
const https = require('https');

const API = "AIzaSyDgv0aGgnEV7DTm7rcg_8LvIM_9zR0yJBg";

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.question('Enter longitude: ', (longitude) => {
    rl.question('Enter altitude: ', (altitude) => {

        console.log("Longitude is: " + longitude);
        console.log("Altitude is: " + altitude);

        let request = https.request("https://maps.googleapis.com/maps/api/streetview/metadata?" +
            "size=600x300&location=" + longitude + "," + altitude + "&heading=-45&pitch=42&fov=110&key=" +
    API, function (response) {

            let replyMessage = "";
            let json;

            response.on('data', function (data) {
                replyMessage += data;
            });

            response.on('end', function (data) {
                json = JSON.parse(replyMessage);
                console.log(JSON.stringify(json, null, 4));

                console.log(json.pano_id);
            })
        });

        request.end();
        //rl.close();
    });
    //rl.close();
});
