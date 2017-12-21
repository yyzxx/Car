/**
 * 商品概况分析后台交互接口
 */
var gGoodSurveyAnalysis = {	
	/**
	 * 获取增降幅数据
	 * @param {Object} successBack：接口调用成功回调函数
	 * @param {Object} errorCallBack：接口调用失败回调函数
	 * @param {Object} userId：用户ID(默认为空)
	 * @param {Object} type：类型：'91'生鲜、'92'食品、'93'百货、'916'餐饮等
	 * @param {Object} storeId：门店ID(默认为空)
	 * @param {Object} dateStr： 选择要查询的日期
	 */
	getIncAndDecApp:function(successBack, gUserId, gType, gStoreId, gDateStr){	
		var body = new Object();
			body.deptId = gType;
			body.userId = gUserId;
			body.selectDate = gDateStr;
			body.shopId = gStoreId;
		//请求参数
		var paramData = new Object();
			paramData.body = body;
		//请求地址
		var url = baseUrl+'/SQBusiness/goodsController/selectIncAndDecApp';
		//请求失败回调函数
		var errorCallBack = function(xhr, textStatus, errorThrown){
				console.log("错误",xhr, textStatus, errorThrown);
		}
		//发送ajax请求
		ajaxFun.ajaxPostFunBodyParam(successBack, errorCallBack, paramData, url);
	}	
};
