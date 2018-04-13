const https = require('https');
const http = require('http');
const express = require('express');
const app = express();
let fs = require('fs');
let Stream = require('stream').Transform;
let im = require('imagemagick');
let sim = require('simple-imagemagick');
const sharp = require('sharp');

const API = "AIzaSyDgv0aGgnEV7DTm7rcg_8LvIM_9zR0yJBg";

var id;
var tiles = [];

var radius = 50;
var currentX = 0;
var totalX = 7;
var currentY = 0;
var totalY = 3;
var tileCount = 0;
var result;
var json;


// parameters: ll:latitude,longitude ,radius, resol:low,high
app.get('/', (req, res) => {

    //console.log("Longitude is: " + longitude);
    //console.log("Latitude is: " + latitude);

    //var lat = 47.85783227207914;
    //var lng = 2.295226175151347;

    let request = https.request("https://cbk0.google.com/cbk?output=json&ll=" + req.query.ll + "&radius=" + req.query.radius, function (response) {

            let replyMessage = "";
            tileCount = 0;

            response.on('data', function (data) {
                replyMessage += data;
            });

            response.on('end', function (data) {
                //console.log(replyMessage);
                json = JSON.parse(replyMessage);

                if (json.Locatio = n === undefined)
                    json = "{}";

                //console.log("id: " + json.Location.panoId);
                //console.log("angle: " + json.Location.best_view_direction_deg);

                result = replyMessage;

                id = json.Location.panoId;

                for (i in json.Links) {
                    //console.log("link to: " + json.Links[i].panoId);
                    //console.log("by  degree: " + json.Links[i].yawDeg);
                }

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

app.listen(3000, () => console.log('LodeStar panorama is on port 3000!'));

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
                // foo
                var base64str = base64_encode('cropped.jpg');
                fs.unlinkSync("output.jpg");
                fs.unlinkSync('cropped.jpg');

                json["lowRes"] = base64str;
                res.send(json);
            });

        });
    }).end();

}

function base64_encode(file) {

    var bitmap = fs.readFileSync(file);
    return new Buffer(bitmap).toString('base64');
}


function getTileNo(tileX, tileY, res) {

    var url = 'http://cbk0.google.com/cbk?output=tile&panoid=' + id + '&x=' + tileX + '&y=' + tileY + "&zoom=3";
    var name = 'im' + tileX + 'x' + tileY;

    let request2 = http.request(url, function (response) {
        var data = new Stream();
        response.on('data', function (chunk) {
            data.push(chunk);
        });

        response.on('end', function () {
            fs.writeFileSync(name + '.jpg', data.read());

            im.resize({
                srcPath: name + '.jpg',
                dstPath: name + '-small.jpg',
                width: 350
            }, function (err, stdout, stderr) {
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
                            if (err) throw err


                            json["highRes"] = base64_encode(id + '.jpg');
                            res.send(json);
                            fs.unlinkSync(id + '.jpg');

                            for (var i = 0; i < totalX * totalY; i++)
                                fs.unlinkSync(tiles[i]);
                        });
                    return;
                }
                ;
            });

        });
    }).end();
}


/*
function getNextTile(){
	if(currentX >= totalX) {
		currentY++;
		currentX = 0;
	}

	if(currentY >= totalY) {
		tiles[totalX*totalY] = '-tile' ;
		tiles[totalX*totalY+1] =  ''+totalX + 'x' + totalY;
		tiles[totalX*totalY+2] =  '-geometry';
		tiles[totalX*totalY+3] = '+0+0';
		tiles[totalX*totalY+4] =  id + '.jpg';
		sim.montage(tiles,
		function(err, metadata){
		  if (err) throw err
		  for(var i=0;i<totalX*totalY;i++)
			fs.unlinkSync(tiles[i]);
		})
		return;
	};

	var j = currentY;
	var i = currentX++;
	var url = 'http://cbk0.google.com/cbk?output=tile&panoid='+ id +'&zoom=3&x='+i+'&y='+j;
	var name = 'im' + i + 'x' + j;

	
    	
	let request2 = http.request(url, function(response) {                                        
		var data = new Stream();
		response.on('data', function(chunk) {                                       
		data.push(chunk);                                                         
		});                                                                         

		response.on('end', function() {                                             
		fs.writeFileSync(name+ '.jpg', data.read());       
		
		im.resize({
		srcPath: name + '.jpg',
		dstPath: name + '-small.jpg',
		width:   350
		}, function(err, stdout, stderr){
		if (err) throw err
			fs.unlinkSync(name + '.jpg');
			tiles[totalX*j+i] = name + '-small.jpg';
			//console.log(totalX*j+i);
				
			getNextTile();		
		});
				        
	});                                                                         
	}).end();
}
*/
