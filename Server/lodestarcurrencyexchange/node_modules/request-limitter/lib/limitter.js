var uptime = process.uptime;

var createLimitter = module.exports = function(max, reset_sec){
    var reset_time = uptime() + reset_sec;
    var count = 0;
    return function(){
        if(uptime() >= reset_time){
            reset_time = uptime() + reset_sec;
            count = 0;
        }
        if(count >= max){
            return false;
        }
        count++;
        return true;
    };
};
