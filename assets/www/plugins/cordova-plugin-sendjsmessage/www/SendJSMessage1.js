cordova.define("cordova-plugin-sendOperator.SendJSMessage1", function(require, exports, module) {

var exec = require('cordova/exec');
var myFunc = function(){};

myFunc.prototype.sendJSMessage=function(operator) {
    exec(null, null, "sendJSMessagePlugin", "sendJSMessage1", [operator]);
};

var showt = new myFunc();
module.exports = showt;
});
