var foursquare = (require('foursquareVenues.js'))('2ZXD11VBHAMIQQMD02LAPSMDUFF0HA5TMG0CERZQKRTT3GB2', '3JXYJDDAXS4ULVFZR2Z4EVIILRBREM0K11LNTURJRXVDAIVY');

var params = {
    "ll": "Ankara, TR"
};

foursquare.getVenues(params, function(error, venues) {
    if (!error) {
        console.log(venues);
    }
});

foursquare.exploreVenues(params, function(error, venues) {
    if (!error) {
        console.log(venues);
    }
});