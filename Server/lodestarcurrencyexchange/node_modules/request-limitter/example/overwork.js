var createLimitter = require('..');
var check = createLimitter(20, 1); // 秒間２０回まで

var work = function(){
    if(!check()){
        console.log("WAIT")
        return false;
    }
    console.log("OK")
    return true;
}

for(var i=0; i<30; ++i){
    if(!work()){
        setTimeout(function(){
            work();
        }, 1000);
    }
}
