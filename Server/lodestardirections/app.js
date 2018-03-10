var googleMapsClient = require("@google/maps").createClient({
   key: "AIzaSyA5rQ8krz1RIVi5OqkbRRrZ_u_3gcupgrw"
});

googleMapsClient.directions({
    origin: [39.874484, 32.747589],
    destination: [39.909506, 32.778300]
},function (err, response) {
    if(!err){
        console.log(response);
    }
});
