"use strict";
var CONSTANT = require('./constant');
var lp = require('./system').lp;

var createGetOption = function(url){
    return {
        url : url,
        timeout : Math.floor(CONSTANT.OPT_TIMEOUT_SEC * 1000),
    }
}

var req = function(url){
    return lp.req(createGetOption(url))
}

var query = exports.query = function(pairs){
    var w = pairs.map(function(v){return '"' + v + '"'}).join(',');
    var param = [
        'select * from yahoo.finance.xchange where pair in (' + w + ')',
        'format=json',
        'env=store://datatables.org/alltableswithkeys'
    ].join('&');
    return req(CONSTANT.YAHOO_API_URL + '?q=' + param).then(JSON.parse)
}

var ticker = exports.ticker = function(pairs){
    if(!(pairs instanceof Array)) pairs = [pairs];
    return query(pairs).then(function(v){
        if(v.query['count'] !== pairs.length) throw new Error('query error');
        else return (v.query.results.rate instanceof Array) ? v.query.results.rate : [v.query.results.rate];
    });
}

