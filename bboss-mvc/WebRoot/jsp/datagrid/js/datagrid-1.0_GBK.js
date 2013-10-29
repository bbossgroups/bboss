/**
 * 	DataGrid封装类
 *  id :形如"#table1"
 *  params :对DataGrid初始化对象参数
 */
var DataGrid = function(id, statParams) {
	DataGrid.log("constructor().id="+id);
	try {
		//statParams:扩展参数，主要是用于统计数据
		this.statParams = statParams;
		if (null == this.statParams) {
			this.statParams = {};
		}
		
		if (null == id) {
			throw new Error(DataGrid.msg_init_lostparam);
			//jQuery.error(DataGrid.msg_init_lostparam)
		} else {
			//alert("data="+jQuery.data(editers,"productid"));
			this.id = id;
		}
		
		//当前正在编辑行
		//this.currRowIndex = 0;
		//最后编辑行
		this.lastIndex = 0;
		this.hasError = false;
		
		/*注册editer*/
		//$(this.id).editer = this;
		//alert($(this.id).editer);
	} catch(e) {
		DataGrid.log("constructor().error="+DataGrid.msg_init_error+e.description,'error');
		$.messager.alert('错误信息',DataGrid.msg_init_error+e.description,'error');
		return ;
	}
	
	/**成员方法**/
	/*加载数据，支持四种参数，字符串，jquery对象，普通对象，自定义函数*/
	this.loadData = function(params) {
		DataGrid.log("查询数据开始......","message");
		DataGrid.log("loadData().params="+params);
		$(this.id).datagrid('options').url = DataGrid.getDataUrl;
		var queryParams = $(this.id).datagrid('options').queryParams;
		var params_type = typeof(params);
		DataGrid.log("loadData().params_type="+params_type);
		if (params_type == "string") {
			params = $(params);
			DataGrid.addQueryParams(queryParams, params);
		} else if (params_type == "object") {
			DataGrid.addQueryParams(queryParams, params);
		} else if (params_type == "function") {
			//jQuery.isFunction(params);
			params(queryParams);
		}
		
		DataGrid.log("loadData().queryParams="+jQuery.param(queryParams));
		//清空数据，或者可以将columns=null
		$(this.id).datagrid('loadData',{"total":0,"rows":[]});
		$(this.id).datagrid('load',queryParams);
		DataGrid.log("查询数据结束......","message");
		/*
		var id = this.id;
		$.getJSON(DataGrid.getDataUrl+jQuery.param(this.params),
					params,
					function(data){
						if(null != data){
							$(id).datagrid('loadData', data);
						}else{
							$.messager.alert('错误信息','对不起！加载数据失败！','error');
						};
		});
		*/
		//$(this.id).datagrid('loaded');
	}
	/*保存数据*/
	this.saveData = function() {
		DataGrid.log("保存数据开始......","message");
		DataGrid.log("saveData()");
		try{
			//保存当前更改
			//alert("this.lastIndex="+this.lastIndex);			
			$(this.id).datagrid('endEdit', this.lastIndex);	
			//转换为jsoncode码并提交到后台数据库				
			var rows = $(this.id).datagrid('getChanges');				
			if(rows.length>0){					
				this.submitData();
			}else{
				$.messager.alert('提示信息',"对不起！数据没有更改或者仍有错误数据，请填好数据！",'info');
				return false;
			}				
		}catch(e){
			this.onSaveDataError(null, e.description, null);				
		}
		DataGrid.log("保存数据结束......","message");
	}
	
	/*提交数据*/
	this.submitData = function(data){
		DataGrid.log("submitData()");
		var deleted_rows = $(this.id).datagrid('getChanges','deleted');
		var updated_rows = $(this.id).datagrid('getChanges','updated');
		var inserted_rows = $(this.id).datagrid('getChanges','inserted');	
		
		var deleted_data = DataGrid.obj2json(deleted_rows);
		var updated_data = DataGrid.obj2json(updated_rows);
		var inserted_data = DataGrid.obj2json(inserted_rows);
		
		var data = "{\"deleted\":"+deleted_data+","
					+"\"updated\":"+updated_data+","
					+"\"inserted\":"+inserted_data+"}";
		DataGrid.log("submitData().data="+data);
		//var obj = JSON.parse(data);
		var url = DataGrid.saveDataUrl+jQuery.param($(this.id).datagrid('options').queryParams);
		param = "data="+data;
		//var data = encodeURIComponent(data);
		var r = $.ajax({
						type: "post",
						url: url,
						contentType: "application/x-www-form-urlencoded; charset=utf-8",
						dataType: "json",
						async: false,
						cache: false,
						timeout: 3000,	
						data: {data: data},						
						beforeSend: function(XMLHttpRequest){
							//ShowLoading();
						},
						success: this.onSaveDataSuccess,
						complete: function(XMLHttpRequest, textStatus){
							//HideLoading();
						},
						error: this.onSaveDataError
				});
		//var r = $.post(url,param,onSaveDataSuccess,"json");	
	}
	/*保存数据出错*/
	this.onSaveDataError = function(XMLHttpRequest, textStatus, errorThrown) {	
		DataGrid.log("onSaveDataError().textStatus="+textStatus,'error');
		$.messager.alert('错误信息',"对不起！保存失败:"+textStatus+"",'error');		
		//保存失败取消客户端本次更改
		//$('#tt').datagrid('rejectChanges');
	}
	/*保存数据出错*/
	this.onSaveDataSuccess = function(data, textStatus) {
		DataGrid.log("onSaveDataSuccess().data="+data);
		if (data.status == 1) {
			//保存成功确认客户端本次更改
			$(this.id).datagrid('acceptChanges');
			$.messager.alert('错误信息',data.msg,'info');
		} else {
			$.messager.alert('提示信息',data.msg,'error');
		}
		//$.messager.show(0, data.msg);  
	}
	/*添加一行*/
	this.appendRow = function(params) {
		DataGrid.log("appendRow().params="+params);
		$(this.id).datagrid('endEdit', this.lastIndex);
		$(this.id).datagrid('appendRow',params);
		this.lastIndex = $(this.id).datagrid('getRows').length-1;
		//$(this.id).datagrid('select', this.lastIndex);
		$(this.id).datagrid('beginEdit', this.lastIndex);	
	}
	/*插入一行*/
	this.insertRow = function(params) {alert(22);
		DataGrid.log("insertRow().params="+params);
		//$(this.id).datagrid('endEdit', this.lastIndex);
		alert(3);
		$(this.id).datagrid('insertRow',params);alert(33);
		//this.lastIndex = $(this.id).datagrid('getRows').length-1;
		//$(this.id).datagrid('select', this.lastIndex);
		//$(this.id).datagrid('beginEdit', this.lastIndex);	
	}
	/*删除一行*/
	this.deleteRow = function(params) {
		DataGrid.log("deleteRow().params="+params);
		var row = $(this.id).datagrid('getSelected');
		if (row){
			var index = $(this.id).datagrid('getRowIndex', row);
			$(this.id).datagrid('deleteRow', index);
		}	
	}
	/*点击一行*/
	this.onClickRow = function(rowIndex) {
		DataGrid.log("onClickRow().rowIndex="+rowIndex);
		if (this.lastIndex != rowIndex){
			$(this.id).datagrid('endEdit', this.lastIndex);
			$(this.id).datagrid('beginEdit', rowIndex);
		}else{
			$(this.id).datagrid('beginEdit', rowIndex);
		}
		this.lastIndex = rowIndex;	
	}
	/*拒绝修改*/
	this.rejectChanges = function() {
		DataGrid.log("rejectChanges()...");
		$(this.id).datagrid('rejectChanges');
	}
	/*获取查询参数*/
	this.getQueryParams = function(){
		DataGrid.log("getQueryParams()...");
		var queryParams = $(this.id).datagrid('options').queryParams;
		DataGrid.log("getQueryParams().queryParams="+jQuery.param(queryParams));
		return queryParams;
	}
	/*方法代理*/
	this.run = function(){
		DataGrid.log("run()");
		//alert(1);
		//$(this.id).datagrid;
	};
	/*数据过滤器*/
	this.onLoadSuccess = function(data) {
		//alert("statParams="+this.statParams);
		//return data;
	}
	/*自定义展现*/
	this.customDisplay = function() {
		DataGrid.log("customDisplay()");
		$.messager.alert('提示信息','正在开发中......','info');
		var columns = $(this.id).datagrid('options').columns;
		var columns_json = DataGrid.obj2json(columns);
		alert(columns_json);
		
	}
}
/**静态设置开始**/
//内置一个日志记录器
DataGrid.logger = new Logger();

/*重载jQuery的内部错误处理*/
DataGrid.error = function(message) {
	DataGrid.log("jquery.error()="+message,"error");
}
//暂时设置不拦截
//jQuery.error = DataGrid.error;
/*记录日志信息*/
DataGrid.debug = function(debug) {
	DataGrid.logger.debug = debug;
}
DataGrid.log = function(msg,level) {
	DataGrid.logger.log(msg,level);
}
/*显示日志信息*/
DataGrid.showLogs = function() {
	DataGrid.logger.showLogs();
}
/*清空日志*/
DataGrid.clearLogs = function() {
	DataGrid.logger.clearLogs();
}
/*截取上下文*/
DataGrid.getContextPath = function() {	
	DataGrid.log("DataGrid.getContextPath()");	
	var cp = location.pathname ;	
	cp = cp.substring(0,cp.indexOf("/",1));	
	if (cp.substring(0, 1) != "/") {
        cp = "/" + cp;
    }
	return cp;
}
/*静态属性*/
DataGrid.msg_init_error = "构造DataGrid失败:";
DataGrid.msg_init_lostparam = "缺少参数";
DataGrid.getDataUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/getData.htm?";
DataGrid.saveDataUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/saveData.htm?";
DataGrid.getNextKeyUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/getNextKey.htm?";
DataGrid.getTableInfoUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/getTableInfo.htm?";
DataGrid.editers = {};
/*静态方法*/
//查找editer
DataGrid.getEditer = function(id) {
	return DataGrid.editers.id;
}
/*因为只能够将参数设置到queryParams中，带进来的参数可能是jQuery对象，或者一般，那么此时需要遍历该对象的属性，将其设置到queryParams中*/
DataGrid.addQueryParams = function(queryParams, params) {
	try {
		//jQuery.isPlainObject(params);测试是否一个纯粹的对象
		if (params instanceof jQuery) {
			DataGrid.log("DataGrid.addQueryParams().params is a jQuery Object");
			var json = "{";			
			$.each(params, function(i, o){
				json += "\""+o.name+"\":\""+o.value+"\",";
				//eval函数不安全，在ie上可以执行，同时在ff浏览器上也无法运行，可以考虑用其他方法取代				
				//window.eval("queryParams."+o.name+"=\""+o.value+"\"");
				
			});
			if (params.length>0) {
				json = json.substring(0,json.length-1);
			}
			json += "}"; 
			//重新处理该参数
			params = jQuery.parseJSON(json);
		} 
		
		DataGrid.log("DataGrid.addQueryParams()合并前的params="+jQuery.param(params));
		//深度递归合并两个对象
		jQuery.extend(true,queryParams, params);
		DataGrid.log("DataGrid.addQueryParams()合并后的queryParams="+jQuery.param(queryParams));
	} catch(e) {
  		DataGrid.log("DataGrid.addQueryParams().params can not be eval!","error");
  		$.messager.alert('错误信息','对不起！查询失败，在传递查询参数的时候遇到错误：'+e,'error');
  		throw e;
	}
}
/*封装参数*/
DataGrid.encodeParams = function(params) {
	DataGrid.log("DataGrid.encodeParams().params="+params);
	return encodeURIComponent(params);
}
/*将对象转换为jsoncode*/
DataGrid.obj2json = function(jsonObj){
	DataGrid.log("DataGrid.obj2json().jsonObj="+jsonObj);
	var jsoncode =  JSON.stringify(jsonObj); 
	//alert(jsoncode);			
	return jsoncode;
}
