var fx = require('..');
var task = require('promise-util-task');
var req = function(){
    var keypairs = ['USD','JPY','EUR'];
    return task.seq(keypairs.map(function(pair){
        return function(){ return fx.rate(fx.getSupportPair(pair)) }
    }))
}

req().then(function(data){
    console.log(data);
})
