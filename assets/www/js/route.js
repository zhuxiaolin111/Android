
app.config(['$routeProvider',function ($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl: 'views/login.html',
			controller: 'loginCtrl'
		})
		.when('/checkList', {
			templateUrl: 'views/check-list.html'
		})
		.when('/checkBuild', {
			templateUrl: 'views/check-build.html'
		})
		.when('/search', {
			templateUrl: 'views/search.html'
		})
		.when('/buildList', {
			templateUrl: 'views/build-list.html'
		})
		.when('/query', {
			templateUrl: 'views/query.html'
		})
		.when('/queryResult', {
			templateUrl: 'views/query-result.html'
		})
		.when('/checkRecord/:roomid', {
			templateUrl: 'views/check-record.html'
		})
		.when('/list', {
			templateUrl: 'views/list.html'
		})
		.when('/resultCon', {
			templateUrl: 'views/resultCon.html',
			controller: 'resultConCtrl'
		})
		.when('/area', {
			templateUrl: 'views/area.html',
			controller: 'areaCtrl'
		})
		.when('/login', {
			templateUrl: 'views/login.html',
			controller: 'loginCtrl'
		})
		.otherwise({
            redirectTo: '/'
        })
}]);


