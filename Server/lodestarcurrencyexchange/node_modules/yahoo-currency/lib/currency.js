"use strict";
var api = require('./api');
var CONSTANT = require('./constant');

var ticker = exports.ticker = function(pairs){
    return api.ticker(pairs)
}

var rate = exports.rate = function(pairs){
    return exports.ticker(pairs).
        reduce(function(r, v){
            r[v['id']] = parseFloat(v['Rate']);
            return r;
        }, {});
}

var calcReverseRate = exports.calcReverseRate = function(rate){
    return 1 / rate;
}

var getSupportCurrency = exports.getSupportCurrency = function(){
    return CONSTANT.SUPPORT_CURRENCY;
}

var getSupportPair = exports.getSupportPair = function(base){
    return getSupportCurrency().
        filter(function(v){ return v !== base }).
        map(function(v){return v + base});
}

var calcRate = function(keycurrency, target, rates){
    var reverse = calcReverseRate(rates[target + keycurrency]);
    var r = Object.keys(rates).
        map(function(v){return v.split(keycurrency).shift()}).
        filter(function(v){return v !== target}).
        reduce(function(r, v){
            r[v + target] = reverse * rates[v + keycurrency];
            return r;
        }, {})
    r[keycurrency + target] = reverse;
    return r;
}

var fullRate = exports.fullRate = function(){
    var keycurrency = 'USD';
    return rate(getSupportPair(keycurrency)).then(function(rates){
        getSupportCurrency().
            filter(function(v){return v !== keycurrency}).
            map(function(v){
                return calcRate(keycurrency, v, rates);
            }).forEach(function(v){
                Object.keys(v).forEach(function(k){rates[k]=v[k]});
            });
        return rates;
    })
}
