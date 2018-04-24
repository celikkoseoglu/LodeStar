//install using sudo npm install --unsafe-perm --verbose

const https = require('https');
const http = require('http');
const express = require('express');
const app = express();
let fs = require('fs');
let Stream = require('stream').Transform;
let im = require('imagemagick');
let sim = require('simple-imagemagick');

var id;
var tiles = [];

var radius = 50;
//var currentX = 0;
var totalX = 7;
//var currentY = 0;
var totalY = 3;
var tileCount = 0;
var json;

function base64_encode(file) {

    let bitmap = fs.readFileSync(file);
    return new Buffer(bitmap).toString('base64');
}


// parameters: ll:latitude,longitude ,radius, resol:low,high
app.get('/', (req, res) => {

    let request = https.request("https://cbk0.google.com/cbk?output=json&ll=" + req.query.ll + "&radius=" + req.query.radius, function (response) {

        let replyMessage = "";
        tileCount = 0;

        response.on('data', function (data) {
            replyMessage += data;
        });

        response.on('end', function (data) {
            //console.log(replyMessage);
            json = JSON.parse(replyMessage);

            if (json.Location === undefined) { // I guess this is the response when no panorama is found.
                res.send("{}");
                return;
            }

            id = json.Location.panoId;


            if (req.query.resol === "low")
                getLowRes(res);

            if (req.query.resol === "high") {
                for (i = 0; i < totalX; i++)
                    for (j = 0; j < totalY; j++)
                        getTileNo(i, j, res);
            }
        })
    });

    request.end();

});

app.get('/test', (req, res) => {

    im.identify('a.jpg', function(err, features){
        if (err) throw err;
        res.send(features);
        // { format: 'JPEG', width: 3904, height: 2622, depth: 8 }
    });
});


app.listen(3011, () => console.log('LodeStar panorama is on port 3011!'));

function getLowRes(res) {
    var url = 'http://cbk0.google.com/cbk?output=tile&panoid=' + id + "&x=0&y=0&zoom=0";
    let request1 = http.request(url, function (response) {
        var lowData = new Stream();
        response.on('data', function (lowchunk) {
            lowData.push(lowchunk);
        });

        response.on('end', function () {
            fs.writeFileSync('output.jpg', lowData.read());
            im.crop({
                srcPath: "output.jpg",
                dstPath: 'cropped.jpg',
                width: 200,
                height: 100,
                quality: 1,
                gravity: "North"
            }, function (err) {
                var base64str = base64_encode('cropped.jpg');
                fs.unlinkSync("output.jpg");
                fs.unlinkSync('cropped.jpg');

                json["lowRes"] = base64str;
                res.send(json);
                json = "";
            });

        });
    }).end();

}

function getTileNo(tileX, tileY, res) {

    var url = 'http://cbk0.google.com/cbk?output=tile&panoid=' + id + '&x=' + tileX + '&y=' + tileY + "&zoom=3";
    var name = id + 'im' + tileX + 'x' + tileY;

    let request = http.request(url, function (response) {
        var data = new Stream();
        response.on('data', function (chunk) {
            data.push(chunk);
        });

        response.on('end', function () {

            console.log("Starting iamge write");

            fs.writeFileSync(name + '.jpg', data.read());

            console.log("Starting iamge resize");

            im.resize({
                srcPath: name + '.jpg',
                dstPath: name + '-small.jpg',
                width: 350
            }, function (err, stdout, stderr) { // callback function for when the image resize event is finished. Image resizing seems to be an sync op

                console.log("Finished image resize");

                if (err) throw err;
                fs.unlinkSync(name + '.jpg');
                tiles[totalX * tileY + tileX] = name + '-small.jpg';
                //console.log(totalX*j+i);
                tileCount++;

                if (tileCount >= 21) {
                    tiles[totalX * totalY] = '-tile';
                    tiles[totalX * totalY + 1] = '' + totalX + 'x' + totalY;
                    tiles[totalX * totalY + 2] = '-geometry';
                    tiles[totalX * totalY + 3] = '+0+0';
                    tiles[totalX * totalY + 4] = id + '.jpg';
                    sim.montage(tiles,
                        function (err, metadata) {
                            if (err) throw err;


                            json["highRes"] = base64_encode(id + '.jpg');
                            res.send(json);
                            json = "";
                            fs.unlinkSync(id + '.jpg');

                            for (var i = 0; i < totalX * totalY; i++)
                                fs.unlinkSync(tiles[i]);
                        });
                    return;
                }
            });

        });
    }).end();
}


