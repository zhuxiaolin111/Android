cordova.define("cordova-plugin-sendjsmessage.SendJSMessage", function(require, exports, module) {

var exec = require('cordova/exec');
var myFunc = function(){};

myFunc.prototype.sendJSMessage=function(buildid,operator) {
    exec(null, null, "sendJSMessagePlugin", "sendJSMessage", [buildid,operator]);
};

var showt = new myFunc();
module.exports = showt;
});
