var assert = require('assert');
var Promise = require('bluebird');
var fx = require('..');

fx.ticker = function(pairs){
    return Promise.resolve(
        [{ id: 'USDJPY',
            Name: 'USD/JPY',
            Rate: '112.0750',
            Date: '2/25/2016',
            Time: '8:46am',
            Ask: '112.1200',
            Bid: '112.0750'
        }]
    );
}

describe('main', function() {
    it('simple', function(done){
        fx.rate('USDJPY').then(function(res){
            assert(res['USDJPY'] === 112.075);
            done();
        }).catch(function(e){ done(e) })
    })
})
