/**
 * 	DataGrid封装类
 *  params :对DataGrid初始化对象参数
 */
var DataGrid = function(id, _params) {
	try {
		//DataGrid实例公共参数
		this.params = _params;
		if (null == this.params) {
			this.params = {};
		}
		
		if (null == id || null == this.params.tableInfoId) {
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
		$.messager.alert('错误信息',DataGrid.msg_init_error+e.description,'error');
		return ;
	}
	
	/**成员方法**/
	/*加载数据*/
	this.loadData = function(params) {
		if (typeof(params) == "string") {
				
		} else {
			params = jQuery.param(params);
		}
		//$.messager.alert('params',params,'info');
		var id = this.id;
		//alert(DataGrid.getDataUrl+jQuery.param(this.params)+'&'+params);
		$.getJSON(DataGrid.getDataUrl+jQuery.param(this.params),
					params,
					function(data){
						if(null != data){
							$(id).datagrid('loadData', data);
						}else{
							$.messager.alert('错误信息','对不起！加载数据失败！','error');
						};
		});
	}
	/*保存数据*/
	this.saveData = function() {
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
	}
	
	/*提交数据*/
	this.submitData = function(data){
		var deleted_rows = $(this.id).datagrid('getChanges','deleted');
		var updated_rows = $(this.id).datagrid('getChanges','updated');
		var inserted_rows = $(this.id).datagrid('getChanges','inserted');	
		
		var deleted_data = this.object2json(deleted_rows);
		var updated_data = this.object2json(updated_rows);
		var inserted_data = this.object2json(inserted_rows);
		
		var data = "{\"deleted\":"+deleted_data+","
					+"\"updated\":"+updated_data+","
					+"\"inserted\":"+inserted_data+"}";
		//var obj = JSON.parse(data);
		var url = DataGrid.saveDataUrl+jQuery.param(this.params);
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
		$.messager.alert('错误信息',"对不起！保存失败:"+textStatus+"",'error');		
		//保存失败取消客户端本次更改
		//$('#tt').datagrid('rejectChanges');
	}
	/*保存数据出错*/
	this.onSaveDataSuccess = function(data, textStatus) {
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
		$(this.id).datagrid('endEdit', this.lastIndex);
		$(this.id).datagrid('appendRow',params);
		this.lastIndex = $(this.id).datagrid('getRows').length-1;
		//$(this.id).datagrid('select', this.lastIndex);
		$(this.id).datagrid('beginEdit', this.lastIndex);	
	}
	/*添加一行*/
	this.deleteRow = function(params) {
		var row = $(this.id).datagrid('getSelected');
		if (row){
			var index = $(this.id).datagrid('getRowIndex', row);
			$(this.id).datagrid('deleteRow', index);
		}	
	}
	/*点击一行*/
	this.onClickRow = function(rowIndex) {
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
		$(this.id).datagrid('rejectChanges');
	}
	/*将对象转换为jsoncode*/
	this.object2json = function(jsonObj){
		var jsoncode =  JSON.stringify(jsonObj); 
		//alert(jsoncode);			
		return jsoncode;
	}
	/*方法代理*/
	this.run = function(){
	//alert(1);
	//$(this.id).datagrid;
	};
}
/*静态方法设置*/
DataGrid.editers = {};

//截取上下文
DataGrid.getContextPath = function() {		
	var cp = location.pathname ;	
	cp = cp.substring(0,cp.indexOf("/",1));	
	if (cp.substring(0, 1) != "/") {
        cp = "/" + cp;
    }
	return cp;
}

DataGrid.log = function(msg) {

}

DataGrid.showLogs = function() {

}

//查找editer
DataGrid.getEditer = function(id) {
return DataGrid.editers.id;
}
/*静态属性设置*/
DataGrid.msg_init_error = "构造DataGrid失败:";
DataGrid.msg_init_lostparam = "缺少参数";
DataGrid.getDataUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/getData.htm?";
DataGrid.saveDataUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/saveData.htm?";
DataGrid.getNextKeyUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/getNextKey.htm?";
DataGrid.getTableInfoUrl = window.location.protocol+"//"+window.location.host+DataGrid.getContextPath()+"/datagrid/getTableInfo.htm?";

/*
 * 封装参数
 */
DataGrid.encodeParams = function(params) {
	return encodeURIComponent(params);
}
