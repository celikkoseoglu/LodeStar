var http = require('http');

var options = {
    host: 'esenbogaairport.com',
    path: '/tr-TR/Ucus_Bilgileri/Pages/Anasayfa.aspx'
}
var request = http.request(options, function (res) {
    var data = '';
    res.on('data', function (chunk) {
        data += chunk;
    });
    res.on('end', function () {
        console.log(data);

    });
});
request.on('error', function (e) {
    console.log(e.message);
});
request.end();