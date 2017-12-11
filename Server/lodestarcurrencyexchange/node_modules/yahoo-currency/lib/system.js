"use strict";
var LimitRequestPromise = require('limit-request-promise');
var MAX = 1;
var SEC = 1;
var lp = exports.lp = new LimitRequestPromise(MAX, SEC);
