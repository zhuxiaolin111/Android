app.controller('searchCtrl', function($scope, $rootScope, $http, $location, searchService){
	$rootScope.title = '搜索'
	$scope.searchName = '';
	localStorage.isSao = 0;

	if(localStorage.userid){
		var userid = localStorage.userid
	};
	if(localStorage.agentid){
		var agentid = localStorage.agentid;
	}


	hideLoading();
	var data = {
		method: 'getpersoninfo',
		userid: userid,
		agentid: agentid,
		isweixin: 1
	}


	$http.post('http://'+hostname+'/weixinservice.ashx', data).success(function(res){
	   // alert(JSON.stringify(res));
		company = res[0].companyname;

		Operator = res[0].username;
	    localStorage.Operator = Operator;
		localStorage.userinfo = JSON.stringify(res)
	})

	$scope.changeType = function(t){
		$scope.type = t;
	}

	$scope.selBx = function(){
		$('#bushuMenu').show();
	}

	$scope.bulists = [
		{name:'热站名称',type:'3'},
		{name:'小区名称',type:'0'},
		{name:'大楼名称',type:'1'}
	];

	$scope.selt = function(a){
		$('#bushuMenu').hide();
		$scope.bushuSel = a.name;
		$scope.type = a.type;
	}

	$scope.search = function(searchName){
	//alert(searchName);
		console.log($scope.bushuSel);
		if($scope.bushuSel == undefined){
			alert('请选择部署条件');
		}
		if(searchName == ''){
			if($scope.type == 0){
				alert('请输入小区名称')
			}
			if($scope.type == 1){
				alert('请输入大楼名称')
			}
			if($scope.type == 2){
				alert('请输入房间卡号')
			}
			if($scope.type == 3){
				alert('请输入热站名称')
			}
			return
		}
		if($scope.type == 0){
			var type = '小区'
			showLoading();
			searchService.doSearch(searchName, type).then(function(res){
			  //  alert(JSON.stringify(res));
				localStorage.building = JSON.stringify(res.Data);
				$scope.buildings = res.Data;
			})
			localStorage.userid = userid;
		}
		if($scope.type == 1){
			var type = '大楼'
			showLoading()
			searchService.doSearch(searchName, type).then(function(res){
			
				localStorage.building = JSON.stringify(res.Data);
				$scope.buildings = res.Data;
			})
			localStorage.userid = userid;
		}

		if($scope.type == 3){
			var type = '热站'
			showLoading()
			searchService.doSearch(searchName, type).then(function(res){
				
				localStorage.building = JSON.stringify(res.Data);
				$scope.buildings = res.Data;
			})
			localStorage.userid = userid;
		}

		if($scope.type == 2){
			showLoading();
			searchService.searchRoom(searchName).then(function(res){
			
				if(res.Data.length == 0){
					alert('无此用户')
					return;
				}
				localStorage.userid = userid;
				localStorage.room = JSON.stringify(res.Data[0])
				window.location.href = 'checkinfo.html?userid=' + userid

			})
		}
	}

	$scope.goList = function(build){
		if($scope.type == 0 ){
			
			localStorage.curXiaoqu = JSON.stringify(build);
			$location.path('/buildList')
		}else if($scope.type == 1){
		
			var buildid = build.ID;
			//alert("121312123");

		window.SendJSMessage.sendJSMessage(buildid,localStorage.Operator);

		}else if($scope.type == 3){
			
			localStorage.newbuild = JSON.stringify(build);
			$location.path('/area')
		}
	}

})

app.controller('checkListCtrl', function($scope, $rootScope, $location, checkService){

	$scope.hostname = hostname;
	if(hostname == 'syhf.eheat.com.cn'){
		$('#types').hide();
		$('#types2').show();
	}


	console.log(localStorage.curBuild);
	var build = JSON.parse(localStorage.curBuild);	
	var userinfo = JSON.parse(localStorage.userinfo);
	$scope.hostname = hostname;
	company = userinfo[0].companyname;
	Operator = userinfo[0].username;
	$rootScope.title = build.Name;
	hideLoading();
	var myDate = new Date();
	var newDate = myDate.getMonth()+1;
	showLoading();
	checkService.getRoomList(build.ID).then(function(res){
		$scope.list = [];
		$scope.roomList = res.Data;
		if(hostname == 'syhf.eheat.com.cn'){
			var ttguser = 0;
			var tnhfuser = 0;
			$('#alluser').html(res.Data.length);
			$.each($scope.roomList, function(i, v){			
					if(v.C_Mark_Now == '当年停'){
						v.bgclass = 'c-stop';
						ttguser++
					}else if(v.C_Mark_Now == '当年开'){
						v.bgclass = 'c-do';
						tnhfuser++
					}else if(v.M_Total > 1 && newDate>=11){
						v.bgclass = 'c-dg';
					}
					if(v.C_Mark_Now == '当年停' || v.C_Mark_Now == '当年开' || v.C_Mark_Now == '当年停' || v.bgclass == 'c-dg'){
						$scope.list.push(v)
					}
			})
			$('#ttguser').html(ttguser);
			$('#tnhfuser').html(tnhfuser);
		}else{
			$.each($scope.roomList, function(i, v){
					if(v.C_Mark == '停'){
						v.bgclass = 'b-stop'
					}else if(v.C_Mark == '断'){
						v.bgclass = 'b-break'
					}
				$scope.list.push(v)
			})
		}

		checkService.GetChargeYear().then(function(res){
			$scope.years = res.Data;
			$.each(res.Data, function(i, v){
				if(v.IsNowYear == 1){
					$scope.nowYear = v;
				}
			})
		})
	})
	var wntguser = 0;
	var otherY = myDate.getFullYear()-1;
	var otheryear = otherY+'-'+ myDate.getFullYear();
	checkService.getRoomList(build.ID, otheryear).then(function(res){
			hideLoading();
			$.each(res.Data, function(i, v){	
				if(v.C_Mark == '停' && v.C_Mark_Now == '无变化'){
					wntguser++;
				}
			})
			$('#wntguser').html(wntguser);
	})

	//获取上一年年度停
	
	$scope.getOtherYear = function(){
		$scope.list = [];
		showLoading();
		checkService.getRoomList(build.ID, otheryear).then(function(res){
			hideLoading();
			$.each(res.Data, function(i, v){	
					if(v.C_Mark == '停' && v.C_Mark_Now == '无变化'){
						$scope.list.push(v);
						v.bgclass = 'c-stop';	
					}
			})
		})
		$('#otherY').hide();
		$('#otherD').show();
		$('#types2').hide();
		$('.room-wrap').css('padding-top','32px');
	}

	//获取今年停，开，欠费
	$scope.getToYear = function(){
		$scope.list = [];
		showLoading();
		$.each($scope.roomList, function(i, v){
			hideLoading();
			if(v.C_Mark_Now == '当年停'){
				v.bgclass = 'c-stop';
			}else if(v.C_Mark_Now == '当年开'){
				v.bgclass = 'c-do'
			}else if(v.M_Total > 1 && newDate>=11){
				v.bgclass = 'c-dg';
			}
			if(v.C_Mark_Now == '当年停' || v.C_Mark_Now == '当年开' || v.C_Mark_Now == '当年停' || v.bgclass == 'c-dg'){
				$scope.list.push(v)
			}
		})
		$('#otherY').show();
		$('#otherD').hide();
		$('#types2').show();
		$('.room-wrap').css('padding-top','120px');
	}

	$scope.goallYear = function(){
		$scope.list = [];
		showLoading();
		checkService.getRoomList(build.ID).then(function(res){
			hideLoading();
			$.each(res.Data, function(i, v){	
				if(v.C_Mark == '停'){
					console.log(1);
					v.bgclass = 'c-stop';
					$scope.list.push(v)
				}
			})
		})
		$('#types2').hide();
		$('.room-wrap').css('padding-top','32px')
	}

	$scope.changeYear = function(){
		showShade()
		$('.chargeYear').show();
	}
	$scope.chooseYear = function(year){
		$scope.nowYear = year
		hideShade()
		$('.chargeYear').hide();
		showLoading();
		checkService.getRoomList(build.ID, year.ChargeYear).then(function(res){
			$scope.list = []
			$scope.roomList = res.Data;
			$.each($scope.roomList, function(i, v){
				
			// if(hostname == 'syhf.eheat.com.cn'){
			// 	if(v.C_Mark_Now == '当年停'){
			// 		v.bgclass = 'c-stop'
			// 	}else if(v.C_Mark_Now == '当年开'){
			// 		v.bgclass = 'c-do'
			// 	}
			// }else{
				if(v.C_Mark == '停'){
						v.bgclass = 'b-stop'
					}else if(v.C_Mark == '断'){
						v.bgclass = 'b-break'
					}
					$scope.list.push(v)
				}
			)
		})
	}

	

if(hostname == 'syhf.eheat.com.cn'){
	$('#types2').find('span').on('click', function(){
		console.log(123);
		$scope.list = [];
		var n = $(this).index();
		var e = $(this).find('i')
		if(e.hasClass('checked')){
			$('#types2').find('i').removeClass('checked')
			$.each($scope.roomList, function(i, v){
				$scope.list.push(v)
			})
		}else{
				$('#types2').find('i').removeClass('checked');
				e.addClass('checked');
				$(this).find('i').addClass('checked');
				if(n == 0){
					$.each($scope.roomList, function(i, v){
						if(v.C_Mark_Now == '当年停'){
							$scope.list.push(v)
						}
					})
				}
				if(n == 1){
					$.each($scope.roomList, function(i, v){
						if(v.C_Mark_Now == '当年开'){
							$scope.list.push(v)
						}
					})
				}
				if(n == 2){
					$.each($scope.roomList, function(i, v){
						if(v.M_Total > 1 && newDate>=11){
							$scope.list.push(v)
						}
					})
				}
			
		}
		$scope.$apply()
	})

}else{
	$('#types').find('span').on('click', function(){
		$scope.list = [];
		var n = $(this).index();
		var e = $(this).find('i')
		if(e.hasClass('checked')){
			$('#types').find('i').removeClass('checked')
			$.each($scope.roomList, function(i, v){
				$scope.list.push(v)
			})
		}else{
				$('#types').find('i').removeClass('checked');
				e.addClass('checked');
				$(this).find('i').addClass('checked');
				if(n == 0){
					$.each($scope.roomList, function(i, v){
						if(v.C_Mark == '停'){
							$scope.list.push(v)
						}
					})
				}
				if(n == 1){
					$.each($scope.roomList, function(i, v){
						if(v.C_Mark == '断'){
							$scope.list.push(v)
						}
					})
				}
				if(n == 2){
					$.each($scope.roomList, function(i, v){
						if(v.M_Total != '' && v.M_Total != null){
							$scope.list.push(v)
						}
					})
				}
			
		}
		$scope.$apply()
	})
}
	

	

	$scope.goInfo = function(room){
		localStorage.room = JSON.stringify(room);
		localStorage.userid = args().userid;
		window.location.href = 'checkinfo.html'
	}

	$scope.goCheckBulid = function(){
		localStorage.curBuild = JSON.stringify(build);
		$location.path('/checkBuild');
	}
})

app.controller('buildListCtrl', function($scope, $rootScope, $http, $location, checkService){
	$rootScope.title = '大楼列表';
	var userinfo = JSON.parse(localStorage.userinfo);
	company = userinfo[0].companyname;
	$scope.hostname = hostname;
	var xiaoqu = JSON.parse(localStorage.curXiaoqu);
	console.log(xiaoqu);
	hideLoading();
	var data = {
		m: 'GetBuShuLevelEx',
		Company: company,
		BuShuLevel: xiaoqu.Level,
		NodeID: xiaoqu.ID,
		IsEx: 1
	}
	showLoading();
	$http.post('http://www.heatingpay.com/command.ashx', data).success(function(res){
		hideLoading();
		$scope.list = res.Data;
	})
	//提交离线数据并下载
	$scope.down = function(build){

		//大楼列表
		var nb = {
			method: 'JC_GetBuildingRoomList',
			Company: company,
			BuildingID: build.ID,
			QueryType: ''
		};
		$http.post(ajaxUrl,nb).success(function(e){
			localStorage.xqJson = JSON.stringify(e.Data);
		});
		
		//稽查项目
		var itemData = {
			method:'JC_GetSuperviseItem',
			Company:company
		}
		$http.post(ajaxUrl,itemData).success(function(e){
			localStorage.spitem = JSON.stringify(e.Data);
		})

		//稽查类别
		var typeData = {
			method:'JC_GetSuperviseType',
			Company:company
		}
		$http.post(ajaxUrl,typeData).success(function(e){
			localStorage.sptype = JSON.stringify(e.Data);
		})
		var downloadJson = {
			"userInfo":userinfo,
			"xqjson":JSON.parse(localStorage.xqJson),
			"spitem":JSON.parse(localStorage.spitem),
			"sptype":JSON.parse(localStorage.sptype)
		};
		console.log(downloadJson);
		console.log(JSON.stringify(downloadJson));
	}

	$scope.goInfo = function(build){
		var buildid = build.ID;
				//alert(localStorage.Operator);
               window.SendJSMessage.sendJSMessage(buildid,localStorage.Operator);

	}
})

app.controller('checkBuildCtrl', function($scope, $rootScope, $location, checkService){
	var build = JSON.parse(localStorage.curBuild);
	var userinfo = JSON.parse(localStorage.userinfo);
	hideLoading();

	company = userinfo[0].companyname;
	Operator = userinfo[0].username;
	$rootScope.title = build.Name;

	showLoading();
	checkService.getBuild(build.ID).then(function(res){
		drawBuild(res);
		console.log($scope.roomList)
	})

	function drawBuild(res){
		var rooms = [];
		$scope.roomList = [];
		$scope.roomCells = [];
		$.each(res.Data.Room.FieldValue, function(i, v){
			var fieldData = {};
			$.each(res.Data.Room.fieldName, function(n, m){
				var key = m;
				fieldData[key] = v[n];
			})
			rooms.push(fieldData)
		})
		$scope.rooms = rooms;
		$.each($scope.rooms, function(i, v){
			if(v.C_Mark == '停'){
				v.bgclass = 'b-stop'
			}else if(v.C_Mark == '断'){
				v.bgclass = 'b-break'
			}
			$scope.roomCells.push(v.I_Cell);
			$scope.roomList.push(v);
		})
		localStorage.roomList = JSON.stringify($scope.roomList);

		$scope.roomCells = unique($scope.roomCells);

		$scope.xs = res.Data.cell;
		$scope.ys = res.Data.floor;
		$scope.roomXs = $scope.xs.x;

		$scope.maxX = $scope.xs.x[$scope.xs.x.length - 1]
		$scope.maxY= $scope.ys.y[$scope.ys.y.length - 1]

		$('.build-table').css('width', res.Data.cell.caption.length*110);
		$('.build-table').css('height', res.Data.floor.caption.length*30);
		$('.not-in').css('width', res.Data.cell.caption.length*110);
	}

	checkService.GetChargeYear().then(function(res){
		$scope.years = res.Data;
		$.each(res.Data, function(i, v){
			if(v.IsNowYear == 1){
				$scope.nowYear = v;
			}
		})
	})

	$scope.more = '更多选项';
	$scope.moreOpt = function(){
		showShade()
		$('#more').show();
	}
	$scope.close = function(){
		hideShade()
		$('#more').hide();
	}

	$scope.changeYear = function(){
		$('#more').hide();
		$('#chargeYear').show();
	}
	$scope.chooseYear = function(year){
		$('#types').find('i').removeClass('checked');
		$scope.nowYear = year;
		hideShade();
		$('#chargeYear').hide();
		$scope.more = year.ChargeYear;
		showLoading();
		$scope.roomList = [];
		checkService.getBuild(build.ID, year.ChargeYear).then(function(res){
			drawBuild(res)
		})
	}

	$scope.changeCell = function(){
		$('#more').hide();
		$('#cellList').show();
	}
	$scope.chooseCell = function(c){
		hideShade();
		$('#cellList').hide();
		$scope.more = c + '单元';
		$scope.roomList = JSON.parse(localStorage.roomList);
		$.each($scope.roomList, function(i, v){
			if(v.I_Cell != c){
				$scope.roomList[i].C_RoomNum = ''
			}
		})
	}
	$scope.chooseCellAll = function(){
		hideShade();
		$('#cellList').hide();
		$scope.roomList = []
		$.each($scope.rooms, function(i, v){
			$scope.roomList.push(v)
		})
	}

	$('#types').find('span').click(function(){
		var n = $(this).index();
		var e = $(this).find('i')
		if(n > 2){
			return;
		}
		if(e.hasClass('checked')){
			$(this).find('i').removeClass('checked');
			$('.build').hide();
			$('.build').eq(0).show();
		}else{
			$('#types').find('i').removeClass('checked');
			e.addClass('checked');
			$('.build').hide();
			$('.build').eq(n+1).show();
		}
	})

	$scope.goInfo = function(room){
		localStorage.room = JSON.stringify(room);
		window.location.href = 'checkinfo.html'
	}

	$scope.goCheckList = function(){
		$location.path('/checkList');
	}
})

app.controller('queryCtrl', function($scope, $rootScope, $http, $location, checkService){
	$rootScope.title = '稽查查询'
	hideLoading();

	var data = {
		method: 'getpersoninfo',
		userid: userid,
		agentid: agentid,
		isweixin: 1
	}
	showLoading();
	$http.post('http://'+hostname+'/weixinservice.ashx', data).success(function(res){
		company = res[0].companyname;
		Operator = res[0].username
		localStorage.userinfo = JSON.stringify(res)

		checkService.getSuperviseItem().then(function(res){
			hideLoading()
			$scope.items = res.Data;
		})
	})
	var curYear = new Date().getFullYear();
	var curMonth = new Date().getMonth() + 1;
	var curDay = new Date().getDate();

	if(curMonth < 10){
		curMonth = '0'+ curMonth;
	}
	if(curDay < 10){
		curDay = '0'+ curDay;
	}
	$('#beginDate').val(curYear + '-' + curMonth + '-01');
	$('#endDate').val(curYear + '-' + curMonth + '-' + curDay);

	$scope.jcItem = { c_peccancyName: ''};
	$scope.query = function(){
		var beginDate = $('#beginDate').val();
		var endDate = $('#endDate').val();
		var type = $scope.jcItem.c_peccancyName;

		if(beginDate == ''){
			alert('请选择开始时间');
			return;
		}
		if(endDate == ''){
			alert('请选择结束时间');
			return;
		}
		var beginTimes = new Date(beginDate).getTime();
		var endTimes = new Date(endDate).getTime();
		if(beginTimes > endTimes){
			alert('结束时间不能小于开始时间');
			return;
		}

		endDate = endDate +' 23:59:59'

		showLoading();
		checkService.queryCheck(beginDate, endDate, type).then(function(res){
			if(res.Result == 0){
				localStorage.queryResult = JSON.stringify(res.Data);

				checkService.queryCheckGroup(beginDate, endDate, type).then(function(res){
					localStorage.queryGroup = JSON.stringify(res.Data);
					$location.path('/queryResult');
				})
			}else{
				alert(res.Err)
			}
		})
	}

 	var curr = new Date().getFullYear();
    var opt = {
        'date': {
            preset: 'date',
            dateFormat: 'yy-mm-dd'
        }
    }
    $('#beginDate').scroller('destroy').scroller($.extend(opt['date'], {
        theme: 'android-ics light',
        mode: 'scroller',
        display: 'modal',
        animate: 'pop',
        showNow: true,
        nowText: "今天"
    }));
    $('#endDate').scroller('destroy').scroller($.extend(opt['date'], {
        theme: 'android-ics light',
        mode: 'scroller',
        display: 'modal',
        animate: 'pop',
        showNow: true,
        nowText: "今天"
    }));

})

app.controller('queryResultCtrl', function($scope, $location, $http, $rootScope){
	$rootScope.title = '查询结果';
	var result = JSON.parse(localStorage.queryResult);
	var count = JSON.parse(localStorage.queryGroup);
	$scope.results = result;
	$scope.count = count;
	hideLoading();
	$scope.lookthis = function(res){
		localStorage.res = JSON.stringify(res);
		$location.path('/resultCon');
	}

})

info.controller('checkInfoCtrl', function($scope, $location, $http, $window, $http){
	if(hostname == 'jlly.eheat.com.cn'){
		$('#qzdgnewd').show();
	}
	$('.qzbg').hide();
	$('.qzdgFixCon').hide();
	hideLoading();
	var qzdgHeight = (-$('.qzdgFixCon').height())/2+'px';
	$('.qzdgFixCon').css('margin-top',qzdgHeight);
	var room;
	if(args().userid){
		var userid = args().userid;
	}else{
		var userid = localStorage.userid;
	}
	$scope.hostname = hostname;
	if(hostname == 'syhf.eheat.com.cn'){
		$('#normLb').hide();
		$('#normBz').hide();
		$('#syhfLb').show();
		$('#syhfBz').show();
	}
	var userinfo = JSON.parse(localStorage.userinfo);
	if(localStorage.isSao == 0){
		room = JSON.parse(localStorage.room);
		console.log(room);
		$scope.room = room;
		localStorage.roomId = room.I_RoomID;
		showLoading();
	    var data = {
	        method: 'JC_GetSuperviseItem',
	        Company: userinfo[0].companyname
	    }
	    $http.post(ajaxUrl, data).success(function(res){
	        $scope.items = res.Data;
	        hideLoading();
	    })

	}else{
		showLoading();
	    var data = {
	        method: 'JC_GetSuperviseItem',
	        Company: userinfo[0].companyname
	    }
	    $http.post(ajaxUrl, data).success(function(res){
	        $scope.items = res.Data;
	    })
		var saodata = JSON.parse(localStorage.saoData)
		var ddd = {
			method: 'JC_GetBuildingRoomList',
			Company: saodata.companyname,
			RoomID: saodata.roomid,
			QueryType: 0
		}
		$http.post(ajaxUrl, ddd).success(function(res){
            hideLoading();
			$scope.room = res.Data[0];
			room = res.Data[0];

		})
	}
	var userinfo = JSON.parse(localStorage.userinfo);
	var companyname = userinfo[0].companyname;
	var operator = userinfo[0].username;

    var data = {
        method: 'getjssdk',
        url: window.location.href,
        isweixin: 1
    }
    $http.post('http://'+hostname+'/weixinservice.ashx', data).success(function(res){
        wx.config({
            debug: false,
            appId: res.corpid,
            timestamp: res.timestamp,
            nonceStr: res.noncestr,
            signature: res.signature,
            jsApiList: [
                'chooseImage',
                'previewImage',
                'uploadImage',
                'getLocation'
            ]
        });
     	wx.error(function(res){
	        alert(JSON.stringify(res));
	    })
    })

   var mediaid_01 = '';
   var mediaid_02 = '';
   var mediaid_03 = '';
   $('#mediaid_01').click(function(){
		showLoading();
		wx.chooseImage({
		    count: 1,
		    sizeType: ['original', 'compressed'],
		    sourceType: ['album', 'camera'],
		    success: function (res) {
		        var localIds = res.localIds;
		        localIds = localIds.toString();
		        wx.uploadImage({
				    localId: localIds,
				    isShowProgressTips: 0,
				    success: function (res) {
				        mediaid_01 = res.serverId;
				        $('#mediaid_01').hide();
				        $('#mediaid_01Img').show().css('background-image', 'url('+localIds+')');
				        wx.getLocation({
					      	success: function(res) {
					      		// $('#curLocation p').html('当前位置：' +res.longitude+'（经度），'+res.longitude+'（纬度）')
					        	$scope.locations = res;
					      		hideLoading();
					      	},
					      	cancel: function(res) {
					      		hideLoading();
					        	alert('用户拒绝授权获取地理位置');
					      	}
					    });
				        hideLoading();
				    }
				})
		    }
		});
	})

	$('#mediaid_02').click(function(){
		showLoading();
		wx.chooseImage({
		    count: 1,
		    sizeType: ['original', 'compressed'],
		    sourceType: ['album', 'camera'],
		    success: function (res) {
		        var localIds = res.localIds;
		        localIds = localIds.toString()
		        wx.uploadImage({
				    localId: localIds,
				    isShowProgressTips: 0,
				    success: function (res) {
				        mediaid_02 = res.serverId;
				        $('#mediaid_02').hide();
				        $('#mediaid_02Img').show().css('background-image', 'url('+localIds+')');
				        wx.getLocation({
					      	success: function(res) {
					      		// $('#curLocation p').html('当前位置：' +res.longitude+'（经度），'+res.longitude+'（纬度）')
					        	$scope.locations = res;
					      		hideLoading();
					      	},
					      	cancel: function(res) {
					      		hideLoading();
					        	alert('用户拒绝授权获取地理位置');
					      	}
					    });
				        hideLoading();
				    }
				})
		    }
		});
	})

	$('#mediaid_03').click(function(){
		showLoading();
		wx.chooseImage({
		    count: 1,
		    sizeType: ['original', 'compressed'],
		    sourceType: ['album', 'camera'],
		    success: function (res) {
		        var localIds = res.localIds;
		        localIds = localIds.toString()
		        wx.uploadImage({
				    localId: localIds,
				    isShowProgressTips: 0,
				    success: function (res) {
				        mediaid_03 = res.serverId;
				        $('#mediaid_03').hide();
				        $('#mediaid_03Img').show().css('background-image', 'url('+localIds+')');
				        wx.getLocation({
					      	success: function(res) {
					      		// $('#curLocation p').html('当前位置：' +res.longitude+'（经度），'+res.longitude+'（纬度）')
					        	$scope.locations = res;
					      		hideLoading();
					      	},
					      	cancel: function(res) {
					      		hideLoading();
					        	alert('用户拒绝授权获取地理位置');
					      	}
					    });
				        hideLoading();
				    }
				})
		    }
		});
	})

	$('#mediaid_01Img').click(function(){
		showLoading()
		wx.chooseImage({
		    count: 1,
		    sizeType: ['original', 'compressed'],
		    sourceType: ['album', 'camera'],
		    success: function (res) {
		        var localIds = res.localIds;
		        localIds = localIds.toString();
		        wx.uploadImage({
				    localId: localIds,
				    isShowProgressTips: 0,
				    success: function (res) {
				        mediaid_01 = res.serverId;
				        $('#mediaid_01Img').css('background-image', 'url('+localIds+')');
				         wx.getLocation({
					      	success: function(res) {
					      		// $('#curLocation p').html('当前位置：' +res.longitude+'（经度），'+res.longitude+'（纬度）')
					        	$scope.locations = res;
					      		hideLoading();
					      	},
					      	cancel: function(res) {
					      		hideLoading();
					        	alert('用户拒绝授权获取地理位置');
					      	}
					    });
				        hideLoading();
				    }
				})
		    }
		});
	})


	$('#mediaid_02Img').click(function(){
		showLoading()
		wx.chooseImage({
		    count: 1,
		    sizeType: ['original', 'compressed'],
		    sourceType: ['album', 'camera'],
		    success: function (res) {
		        var localIds = res.localIds;
		        localIds = localIds.toString();
		        wx.uploadImage({
				    localId: localIds,
				    isShowProgressTips: 0,
				    success: function (res) {
				        mediaid_02 = res.serverId;
				        $('#mediaid_02Img').css('background-image', 'url('+localIds+')');
				        wx.getLocation({
					      	success: function(res) {
					      		// $('#curLocation p').html('当前位置：' +res.longitude+'（经度），'+res.longitude+'（纬度）')
					        	$scope.locations = res;
					      		hideLoading();
					      	},
					      	cancel: function(res) {
					      		hideLoading();
					        	alert('用户拒绝授权获取地理位置');
					      	}
					    });
				        hideLoading();
				    }
				})
		    }
		});
	})

	$('#mediaid_03Img').click(function(){
		showLoading()
		wx.chooseImage({
		    count: 1,
		    sizeType: ['original', 'compressed'],
		    sourceType: ['album', 'camera'],
		    success: function (res) {
		        var localIds = res.localIds;
		        localIds = localIds.toString()
		        wx.uploadImage({
				    localId: localIds,
				    isShowProgressTips: 0,
				    success: function (res) {
				        mediaid_03 = res.serverId;
				        $('#mediaid_03Img').css('background-image', 'url('+localIds+')');
				        wx.getLocation({
					      	success: function(res) {
					      		// $('#curLocation p').html('当前位置：' +res.longitude+'（经度），'+res.longitude+'（纬度）')
					        	$scope.locations = res;
					      		hideLoading();
					      	},
					      	cancel: function(res) {
					      		hideLoading();
					        	alert('用户拒绝授权获取地理位置');
					      	}
					    });
				        hideLoading();
				    }
				})
		    }
		});
	})
    $scope.chooseVideo = function(){
    	alert('请在提交稽查之后，回到主页面发送小视频')
    }
    $scope.choosePosition = function(){
    	alert('已获取当前位置')
    }
    $scope.remark = '';
    $scope.jcItem = '';

    var ndata = {
				method: 'JC_GetSuperviseType',
				Company: userinfo[0].companyname
			}
		$http.post(ajaxUrl,ndata).success(function (res) {
			$scope.jcxmChange = function(){
				console.log($scope.jcItem.c_peccancyName);
				if($scope.jcItem.c_peccancyName == '企业热用户'){
					$scope.types =  [{c_peccancyType:"关栓"},{c_peccancyType:"开栓"},{c_peccancyType:"窃热"}]
				}else{
					$scope.types = res.Data;
				}
			}	
		})

	$('.qzbg').click(function () {
		$('.qzbg').hide();
		$('.qzdgFixCon').hide();
	})
	$scope.doqzdg = function () {
		$('.qzdgFixCon').show();
		$('.qzbg').show();
	}
	$scope.dgsub = function () {
		if($scope.dgText == undefined || $scope.dgText == ''){
			alert('请输入工单描述');
			return false;
		}
		var dgData = {
			method:'addstoppipetask',
			userid: localStorage.userid,
			roomid: localStorage.roomId,
			describe: $scope.dgText,
			repairmanid:$('#qdsel').val(),
			servicegroupid:$("input[name='fuwu']:checked").val()
		}
		$http.post(dispatchUrl,dgData).success(function (res) {
			if(res.errmsg){
				alert(res.errmsg);
				return false;
			}else{
				alert('提交成功！');
				$('.qzbg').hide();
				$('.qzdgFixCon').hide();
			}
		})
	}


	$http.post(dispatchUrl,{method:'getservicegrouplist',userid:userid,isweixin:1}).success(function (res) {
		$scope.fuwuList = res;
	})

	$('.fwdTitle').click(function() {
		$('.banzuFixed').show();
	})

	$scope.nchange = function (task) {
		showLoading();
		$('.banzuFixed').hide();
		var iptcheck = $("input[name='fuwu']:checked").next("span").text();
		$('#fwdText').val(iptcheck);
		$http.post(dispatchUrl,{method:'getrepairmanlist',userid:userid,groupid:$("input[name='fuwu']:checked").val(),isweixin:1}).success(function (data) {
			hideLoading();
			$scope.repairList = data
		})
	}
	$scope.lookThis = function(){
		$('#showPic').show();
		$('#closePic').show();
		var Newroomid = JSON.parse(localStorage.room);
		console.log(Newroomid.I_RoomID);
		showLoading();
		$.post( dispatchUrl, { method:'getpubimagelist', userid: localStorage.userid ,billtype:'报停申请附件',roomid:Newroomid.I_RoomID}, function(data){
			hideLoading();
			if(data == ''){
				$('#noPhoto').show();
			}else{
				var base1 = '';
				for(var i = 0;i<data.length;i++){
					 base1 +='<a class="sltPic" href="http://'+hostname+data[i].filepath+'"><img src="http://'+hostname+data[i].filepath+'" /><p>'+data[i].adddate+'</p></a>' 
				}
				 $('#showPic').html(base1);			
			}
		},'json')
	}
	$('#closePic').click(function(){
		$('#showPic').hide();
		$('#closePic').hide();
	});
	$scope.jcType = '请选择';
	//快捷备注弹窗
	$scope.fastWrite = function(){
		if($scope.jcType == '请选择' ){
			alert('请选择稽查类别');
			return;
		}
		$('#androidActionsheet').show();
	}

	$('#adrBg').click(function(){
		$('#androidActionsheet').hide();
	})

	$scope.writeIt = function(fast){
		$scope.remark = fast.name;
		$('#androidActionsheet').hide();
	}

	//切换稽查类别联动快捷备注列表
	$scope.changeJC = function(){
		if($scope.jcType == '断管'){
			$scope.fastlist = [{name:"已断管"},{name:"未断"},{name:"整改（未封堵）"},{name:"经核实已交费"}];
		}else if($scope.jcType == '接管'){
			$scope.fastlist = [{name:"已接管"},{name:"未接"}];
		}else if($scope.jcType == '无法取证'){
			$scope.fastlist = [{name:"物业用房"},{name:"二道门"},{name:"信息不详（已拆迁）"},{name:"其它（无供暖设施）"},{name:"自供采暖"},{name:"垃圾房"},{name:"自来水泵房"},{name:"特殊原因"}];
		}else if($scope.jcType == '窃热（放水）'){
			$scope.fastlist = [{name:"已照相取证"},{name:"已摄像取证"},{name:"经核实已交费"}];
		}else if($scope.jcType == '关栓'){
			$scope.fastlist = [{name:"已关栓"},{name:"整改"},{name:"无防盗箱，无铅封"}];
		}else if($scope.jcType == '开栓'){
			$scope.fastlist = [{name:"已开栓"},{name:"整改"},{name:"无防盗箱，无铅封"}];
		}else if($scope.jcType == '窃热'){
			$scope.fastlist = [{name:"已照相取证"},{name:"已摄像取证"},{name:"照相、摄像已经同时取证"}];
		}
	}
	$scope.submit = function(){
		var roomid = room.I_RoomID;
		var remark = $scope.remark;
		//var image = mediaid;
		if($scope.jcItem == '' || $scope.jcItem == null){
			alert('请选择稽查项目');
			return;
		}
		if(remark == ''){
			alert('请填写备注信息');
			return;
		}
		// if(image == ''){
		// 	alert('请上传图片');
		// 	return;
		// }

		if(localStorage.isSao != 0){
				var data = {
					method: 'JC_WebSupervise',
					Company: userinfo[0].companyname,
					RoomID: roomid,
					Num: '',
					Type: $scope.jcType,
					Item: $scope.jcItem.c_peccancyName,
					Remark: remark,
					media1: mediaid_01,
					media2: mediaid_02,
					media3: mediaid_03,
					Longitude: $scope.locations.longitude,
					Latitude: $scope.locations.latitude,
					Operator: userinfo[0].username,
					qrcode:saodata.qrcode
				}
			}else{
				var data = {
					method: 'JC_WebSupervise',
					Company: userinfo[0].companyname,
					RoomID: roomid,
					Num: '',
					Type: $scope.jcType,
					Item: $scope.jcItem.c_peccancyName,
					Remark: remark,
					media1: mediaid_01,
					media2: mediaid_02,
					media3: mediaid_03,
					Longitude: $scope.locations.longitude,
					Latitude: $scope.locations.latitude,
					Operator: userinfo[0].username
				}
			}

	
		showLoading();
		$http.post(ajaxUrl, data).success(function(res){
			hideLoading()
			if(res.Result == 0){
				alert('稽查成功');
				if(hostname=='syhf.eheat.com.cn'){
					window.history.go(-1);
				}else{
					$window.history.back();
				}
			}else{
				alert(res.Err)
			}
		})
	}
})

app.controller('checkRecordCtrl', function($scope, $location, $routeParams, $rootScope, checkService){
	hideLoading();
	$rootScope.title = '稽查记录';
	var roomid = $routeParams.roomid;
	var userinfo = JSON.parse(localStorage.userinfo);
	var userid = localStorage.userid;
	company = userinfo[0].companyname;
	showLoading();
	checkService.getJcRecord(roomid).then(function(res){
		$scope.list = res.Data;
	})
	$scope.lookThis = function(r){
		$('#showPic').show();
		$('#closePic').show();
		showLoading();
		$.post( dispatchUrl, { method:'getpubimagelist',billid:r.ID, userid: userid ,billtype:'违章监察附件',roomid:r.I_RoomID}, function(data){
			hideLoading();
			if(data == '[]'){
				console.log(1)
			}
			if(data == ''){
				$('#noPhoto').show();
				$('#showPic').html('<p id="noPhoto">暂无附件</p>');	
			}else{
				var base1 = '';
				for(var i = 0;i<data.length;i++){
					 base1 +='<a class="sltPic" href="http://'+hostname+data[i].filepath+'"><img src="http://'+hostname+data[i].filepath+'" /><p>'+data[i].adddate+'</p></a>' 
				}
				 $('#showPic').html(base1);			
			}
		},'json')
	}
	$('#closePic').click(function(){
		$('#showPic').hide();
		$('#closePic').hide();
	})
})

app.controller('listCtrl', function($scope, $location, $http, $routeParams, $rootScope, checkService){
	hideLoading();
	$rootScope.title = '稽查单列表';
	var mediaid = args().mediaid;

	var now = new Date();
	var date = new Date(now.getTime() - 7 * 24 * 3600 * 1000);

	var curYear = now.getFullYear();
	var curMonth = now.getMonth() + 1;
	var curDay = now.getDate();

	var lastWeekYear = date.getFullYear();
	var lastWeekMonth = date.getMonth() + 1;
	var lastWeekDay = date.getDate();

	if(lastWeekMonth < 10){
		lastWeekMonth = '0'+ lastWeekMonth;
	}
	if(lastWeekDay < 10){
		lastWeekDay = '0'+ lastWeekDay;
	}

	if(curMonth < 10){
		curMonth = '0'+ curMonth;
	}
	if(curDay < 10){
		curDay = '0'+ curDay;
	}

	var beginDate = lastWeekYear + '-' + lastWeekMonth + '-' + lastWeekDay;
	var endDate = curYear + '-' + curMonth + '-' + curDay;
	var type = '';

	endDate = endDate +' 23:59:59'

	var data = {
		method: 'getpersoninfo',
		userid: userid,
		agentid: agentid,
		isweixin: 1
	}
	showLoading();
	$http.post('http://'+hostname+'/weixinservice.ashx', data).success(function(res){
		company = res[0].companyname;
		Operator = res[0].username
		localStorage.userinfo = JSON.stringify(res)

		checkService.queryCheck(beginDate, endDate, type).then(function(res2){
			hideLoading();
			if(res2.Result == 0){
				$scope.list = res2.Data;
			}else{
				alert(res2.Err)
			}
		})
	})
	$scope.addMedia = function(li){

		var d = {
			method: 'sendmediafile',
			type: '违章监察附件',
			company: company,
			mediaids: mediaid,
			businessid: li.ID,
			roomid: li.I_RoomID,
			addby: li.C_PeccancyPerson,
			isweixin: 1
		}
		console.log(d);
		showLoading();
		$http.post('http://'+hostname+'/weixinservice.ashx', d).success(function(data){
			hideLoading();
			if(data.result == 0){
				alert('选择成功');
				WeixinJSBridge.call('closeWindow');
			}else{
				alert('选择失败');
			}
		})
	}
})


app.controller('resultConCtrl', function($scope, $location, $routeParams, $rootScope){
	$rootScope.title="详情";
	var info = JSON.parse(localStorage.res);
	$scope.taskInfo = info;

	$('#lookChe').click(function(){
		$('#showPic').show();
		$('#closePic').show();
		showLoading();
		$.post( dispatchUrl, { method:'getpubimagelist',billid:info.ID, userid: userid ,billtype:'违章监察附件',roomid:info.I_RoomID}, function(data){
			hideLoading();
			if(data == ''){
				$('#noPhoto').show();
			}else{
				var base1 = '';
				for(var i = 0;i<data.length;i++){
					 base1 +='<a class="sltPic" href="http://'+hostname+data[i].filepath+'"><img src="http://'+hostname+data[i].filepath+'" /><p>'+data[i].adddate+'</p></a>' 
				}
				 $('#showPic').html(base1);			
			}
		},'json')
		})
		$('#closePic').click(function(){
				$('#showPic').hide();
				$('#closePic').hide();
		})
})




app.controller('areaCtrl', function($scope, $rootScope, $http, $location, checkService){
	$rootScope.title = '小区列表';
	var userinfo = JSON.parse(localStorage.userinfo);
	company = userinfo[0].companyname;
	var xiaoqu = JSON.parse(localStorage.newbuild);
	$scope.type = 0
	hideLoading();
	var data = {
		m: 'GetBuShuLevelEx',
		Company: company,
		BuShuLevel: xiaoqu.Level,
		NodeID: xiaoqu.ID,
		IsEx: 1
	}
	showLoading();
	$http.post('http://www.heatingpay.com/command.ashx', data).success(function(res){
		hideLoading();
		$scope.list = res.Data;
	})

	$scope.goInfo = function(build){
		localStorage.curXiaoqu = JSON.stringify(build);
		$location.path('/buildList')
	}

	$scope.goseeArea = function(build){
		localStorage.curBuild = JSON.stringify(build);
		//console.log(localStorage.area);return;
		$location.path('/checkList');

	}
	$scope.changeType = function(a){
		$scope.type = a;
	}
})
 <!-- android调用js方法 -->
        function showImei(content){
        var imei =content;
        localStorage.imei = imei;
        }

app.controller('loginCtrl', function($scope, $location, $routeParams, $rootScope, $http){

	$rootScope.title="沈阳华发稽查系统";
	$scope.tel = '';
	$scope.login = function(){
		if($scope.tel == ''){
			alert('电话号码不能为空');
			return
		}
		var data = {
			method:'getuserid',
			isweixin:1,
			mobile:$scope.tel,
			imei:localStorage.imei
		}

/*		alert(JSON.stringify(data));*/
		$http.post('http://'+hostname+'/weixinservice.ashx',data).success(function(res){
			if(res.userid == ''){
				alert('无此用户');
				return;
			}else{
				localStorage.userid = res.userid;
				localStorage.agentid = 3;
				$location.path('/search');
			}
			
		})
	}
	
})
