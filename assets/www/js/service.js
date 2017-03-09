app.service('searchService', function($q, $http, $location){
	return {
		doSearch: function(name, type){
			if(type == '小区'){
				var data = {
					Name: name,
					Company: company,
					bushulevelname: type,
					NodeID: '',
					IsEx: 0
				}
			}else{
				var data = {
					Name: name,
					Company: company,
					bushulevelname: type,
					NodeID: '',
					IsEx: 1
				}
			}
			
			var deferred = $q.defer();

			$http.post('http://www.heatingpay.com/command.ashx?m=GetBuShuLevel', data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		searchRoom: function(cardnum){
			var data = {
				method: 'JC_GetBuildingRoomList',
				Company: company,
				CardNum: cardnum,
				QueryType: 0
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		}
	}
})

app.service('checkService', function($q, $http, $location){
	return {
		getRoomList: function(buildingId, year){
			if(year){
				var data = {
					method: 'JC_GetBuildingRoomList',
					Company: company,
					BuildingID: buildingId,
					QueryType: '',
					ChargeYear: year
				}
			}else{
				var data = {
					method: 'JC_GetBuildingRoomList',
					Company: company,
					BuildingID: buildingId,
					QueryType: ''
				}
			}
			
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		getBuild: function(buildingId, year){
			if(year){
				var data = {
					method: 'JC_GetDBLayer',
					Company: company,
					BuildingID: buildingId,
					ChargeYear: year
				}
			}else{
				var data = {
					method: 'JC_GetDBLayer',
					Company: company,
					BuildingID: buildingId
				}
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		getSuperviseType: function(){
			var data = {
				method: 'JC_GetSuperviseType',
				Company: company
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		getSuperviseItem: function(){
			var data = {
				method: 'JC_GetSuperviseItem',
				Company: company
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		addCheck: function(roomid, num, type, item, remark, image){
			var data = {
				method: 'JC_WebSupervise',
				Company: company,
				RoomID: roomid,
				Num: num,
				Type: type,
				Item: item,
				Remark: remark,
				Image: image,
				Operator: Operator
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		queryCheck: function(BeginDate, EndDate, item){
			var data = {
				method: 'JC_GetSuperviseRecord',
				Company: company,
				BeginDate: BeginDate,
				EndDate: EndDate,
				ItemName: item,
				OperatorName: Operator
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		queryCheckGroup: function(BeginDate, EndDate, item){
			var data = {
				method: 'JC_GetSuperviseRecordGroup',
				Company: company,
				BeginDate: BeginDate,
				EndDate: EndDate,
				ItemName: item,
				OperatorName: Operator
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		getJcRecord: function(roomid){
			var data = {
				method: 'JC_GetSuperviseRecord',
				Company: company,
				RoomID: roomid
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				hideLoading()
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		},
		GetChargeYear: function(){
			var data = {
				method: 'GetChargeYear',
				Company: company
			}
			var deferred = $q.defer();

			$http.post(ajaxUrl, data).success(function(res){
				deferred.resolve(res);
			}).error(function(){
				hideLoading()
				alert('网络异常，请稍候再试')
			})
			return deferred.promise;
		}
	}
})