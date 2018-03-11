const readline = require('readline');
const https = require('https');
const http = require('http');

var fs = require('fs');
var Stream = require('stream').Transform; 

var im = require('imagemagick');
var sim = require('simple-imagemagick');


const API = "AIzaSyDgv0aGgnEV7DTm7rcg_8LvIM_9zR0yJBg";

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

var id;
var tiles = [];

var radius = 350;
var currentX = 0;
var totalX = 7;
var currentY = 0;
var totalY = 3;

rl.question('Enter longitude: ', (longitude) => {
    rl.question('Enter latitude: ', (latitude) => {

        //console.log("Longitude is: " + longitude);
        //console.log("Latitude is: " + latitude);

	//var lat = 47.85783227207914;
      	//var lng = 2.295226175151347;

        let request = https.request("https://cbk0.google.com/cbk?output=json&" +
           "ll=" + latitude + "," + longitude + "&radius=" + radius
    , function (response) {

            let replyMessage = "";
            let json;

            response.on('data', function (data) {
                replyMessage += data;
            });

            response.on('end', function (data) {
                json = JSON.parse(replyMessage);
                //console.log(JSON.stringify(json, null, 4));


                console.log("id: " + json.Location.panoId);
                console.log("angle: " + json.Location.best_view_direction_deg);
		id = json.Location.panoId;
		if (fs.existsSync(id+'.jpg')) {
		    return;
		}

		for(i in json.Links){
			console.log("link to: " + json.Links[i].panoId);
			console.log("by  degree: " + json.Links[i].yawDeg);
		}
			

		getNextTile();
		
		
            })
        });
	
	request.end();

	
	


		
        //rl.close();
    });
    //rl.close();
});

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

