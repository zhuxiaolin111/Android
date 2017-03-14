//var hostname = window.location.host;
var hostname = 'syhf.eheat.com.cn';
var ajaxUrl = 'http://'+hostname+'/handler/inspectionservice.ashx';
var dispatchUrl = 'http://'+hostname+'/dispatchservice.ashx';
var app = angular.module('check', ["ngRoute"]).run(function($rootScope) {
    $rootScope.title = '';
})
var userid = args().userid;
var agentid = args().agentid;
var company;
var Operator;

app.config([
	'$httpProvider',
	function($httpProvider) {
	    $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	    $httpProvider.defaults.transformRequest.unshift(function(data, headersGetter) {
	        var key, result = [];
	        for (key in data) {
	            if (data.hasOwnProperty(key)) {
	                result.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
	            }
	        }
	        return result.join("&");
	    });
	}
]);

var info = angular.module('checkInfo', ["ngRoute"]);
info.config([
    '$httpProvider',
    function($httpProvider) {
        $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
        $httpProvider.defaults.transformRequest.unshift(function(data, headersGetter) {
            var key, result = [];
            for (key in data) {
                if (data.hasOwnProperty(key)) {
                    result.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
                }
            }
            return result.join("&");
        });
    }
]);
        
function args() {
    var s = location.href;
    var o = new Object();
    var i = s.indexOf("?");
    if (i > 0) {
        s = s.substring(i + 1, s.length);
        i = s.indexOf("#");
        if (i > 0) {
            o.page = s.substring(i + 1, s.length);
            s = s.substring(0, i);
        }
        var params = s.split("&");
        for (var x = 0; x < params.length; x++) {
            var a = params[x].indexOf("=");
            var k = params[x].substring(0, a);
            var v = unescape(params[x].substring(a + 1, params[x].length));
            o[k] = v;
        }
    }
    return o;
}

function showShade(){
    var shadow = document.createElement('div');
    shadow.id = 'shadow';
    $('body').append(shadow);
}
function hideShade(){
    $('#shadow').remove()
}

function showLoading(){
    showShade()
    var load = document.createElement('div');
    load.id = 'loading';
    load.innerHTML = '<img src="img/loading.png" height="66" width="66" class="load-img"><p> 正在加载...</p>';
    $('body').append(load);
}
function hideLoading(){
    hideShade()
    $('#loading').remove()
}

//数组去重
function unique(arr){
    var tmp = new Array();
    for(var i in arr){
        if(tmp.indexOf(arr[i])==-1){
            tmp.push(arr[i]);
        }
    }
    return tmp;
}