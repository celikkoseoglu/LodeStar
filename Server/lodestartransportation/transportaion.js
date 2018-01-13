let url = 'https://maps.googleapis.com/maps/api/directions/json?&mode=transit&origin=frontera+el+hierro&destination=la+restinga+el+hierro&departure_time=1399995076&key=AIzaSyDgv0aGgnEV7DTm7rcg_8LvIM_9zR0yJBg'
var fetch = require('node-fetch');
fetch(url)
    .then(res => res.json())
.then((out) => {
    console.log('Checkout this JSON! ', out);
})
.catch(err => { throw err });