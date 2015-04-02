
/**
* 在$(document).ready函数中调用
* @auther 
* @date 2011-04-15 
*/
function commonInit(){
	addInputCss();
	copyNameToId();	
}

/**
* 给输入框加入样式效果
* @auther 
* @date 2011-04-15 
*/
function addInputCss(){
		$(".input_default").bind('focus',function(){	 			 
			 $(this).removeClass("input_off");			
			 $(this).removeClass("input_out");
			 $(this).removeClass("input_move");
			 $(this).addClass("input_on");
		});
		
		$(".input_default").bind('blur',function(){	
			 $(this).removeClass("input_on");			
			 $(this).removeClass("input_out");
			 $(this).removeClass("input_move");
			 $(this).addClass("input_off");
		});
		
		$(".input_default").bind('mousemove',function(){	
			 $(this).removeClass("input_on");			
			 $(this).removeClass("input_out");
			 $(this).removeClass("input_off");			 
			 $(this).addClass("input_move");
		});
	
		$(".input_default").bind('mouseout',function(){
			 $(this).removeClass("input_on");			
			 $(this).removeClass("input_move");
			 $(this).removeClass("input_off");				 
			 $(this).addClass("input_out");
		}); 
	}

/**
* 请输入框的name值赋给id
* @auther 
* @date 2011-04-15  
*/
function copyNameToId(){
	$("input").each(function(){
		if (this.id == ''){
			this.id = this.name;
		}		
	});
	
	$("select").each(function(){
		if (this.id == ''){
			this.id = this.name;
		}		
	});
	
	$("textarea").each(function(){
		if (this.id == ''){
			this.id = this.name;
		}		
	});
	
	$("form").each(function(){
		if (this.id == ''){
			this.id = this.name;
		}		
	});
}

/**
* 显示遮罩层
* @auther 
* @date 2011-04-15 
* @msgContent 提示信息，不指定则按默认值
*/
function blockUI(msgContent){
	if (!msgContent){
		msgContent = '正在处理，请稍候...';
	}
	$("<div class=\"datagrid-mask\"></div>").css({display:"block", width:"100%",height:$(window).height()}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\"></div>").html(msgContent).appendTo("body").css({display:"block",left: ($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
}

/**
* 去掉遮罩层
* @auther 
* @date 2011-04-15 
*/
function unblockUI(){
	$(".datagrid-mask-msg").remove();
	$(".datagrid-mask").remove();
}


/**
* 将form里的字段值组装成可用于ajax提交的json对象
* @auther 
* @date 2011-04-15
* @param formTag jquery定义form的表达式如'#queryForm'
*/
function formToJson(formId){				
	var o = {};				
    var fields = $(formId).serializeArray();
    
    $.each(fields, function(i, field){
    	
    	if (o[field.name] || o[field.name] == "") {
            if (!o[field.name].push) {
                o[field.name] = [ o[field.name] ];
            }
            o[field.name].push(field.value || '');
        } else {
            o[field.name] = field.value || '';
        }			    	 			    	  
    });		    				       
    return o;	
}

/**
* 检查是否仅选择一个条目，返回一个已选checkbox的jquery数组对象
* @auther 
* @date 2011-04-15
* @param checkboxName 列表中checkbox的name
* @param maxSelSize 最大允许选择的记录数，小于1表示不限制
*/
function checkSelCheckItem(checkboxName, maxSelSize){
	var cks = $("#ds_list input[name=" + checkboxName + "]").filter("input:checked");
	if (cks.length == 0){
		$.messager.alert("提示信息", "请选择一条记录!");
		return;
	}
	//如果所选个数超过最大允许个数
	if (maxSelSize > 0 && cks.length > maxSelSize){
		$.messager.alert("提示信息", "只允许选择" + maxSelSize + "条记录!");
		return;
	}
	return cks;
}

/**
* ajax提交返回后，刷新查询列表
* @auther 
* @date 2011-04-15
* @param responseText ajax请求响应内容
* @param textstatus ajax响应状态
* @param queryMethod 查询列表函数名
* @param successMsg 操作成功提示信息
*/
function ajaxCallBack(responseText, textStatus, successCallBackFun, successMsg){
	
	var obj;
	try {							
		obj = jQuery.parseJSON(responseText);
	} catch(err){
		$.messager.alert("操作失败" , "异常如下："
				+ responseText, "error");						
		return false;
	}			
	
	if (obj && obj.status){		
		if (obj.status == "success"){
			if (successMsg){
				$.messager.alert("提示对话框" , successMsg );
			}			
			if (successCallBackFun){
				successCallBackFun(obj.data);	
			}						    		
    	}
    	else {
    		$.messager.alert("操作失败" , "原因如下："
					+ decodeURI(obj.data), "error");	
    	}
	}	
}


/**
* ajax提交返回后，刷新查询列表
* @auther 
* @date 2011-04-15
* @param responseText ajax请求响应内容
* @param textstatus ajax响应状态
* @param queryMethod 查询列表函数名
* @param successMsg 操作成功提示信息
*/
function ajaxCallBackNew(responseText,businessStatus, textStatus, successCallBackFun, successMsg){
	
		
	
	if (businessStatus){		
		if (businessStatus == "success"){
			if (successMsg){
				$.messager.alert("提示对话框" , successMsg );
			}			
			if (successCallBackFun){
				successCallBackFun(responseText);	
			}						    		
    	}
    	else {
    		$.messager.alert("操作失败" , "原因如下："
					+ responseText, "error");	
    	}
	}	
}

/**
* 将form包装成easyui form
* @auther 
* @date 2011-04-20
* @param formTag jquery定义form的表达式如'#queryForm'
* @param url form提交地址
* @param successCallBackFun 操作成功后的回调方法
* @param successMsg 操作成功提示信息
*/
function easyuiForm(formTag, url, successCallBackFun, successMsg){
	$(formTag).form({
	    "url": url,
	    onSubmit:function(){						
	      	var validated = $(this).form('validate');
	      	if (validated){
	      		blockUI();	
	      	}
	      	return validated;
	    },
	    success:function(responseText){	
	    	//去掉遮罩	
			unblockUI();				
			ajaxCallBack(responseText, '' ,successCallBackFun, successMsg);						    			    					    											
	    }
	});	
}

/**
* 将form包装成easyui form
* @auther 
* @date 2011-04-20
* @param formTag jquery定义form的表达式如'#queryForm'
* @param url form提交地址
* @param successCallBackFun 操作成功后的回调方法
* @param successMsg 操作成功提示信息
*/
function easyuiFormNew(formTag, url, successCallBackFun, successMsg){
	$(formTag).form({
	    "url": url,
	    onSubmit:function(){						
	      	var validated = $(this).form('validate');
	      	if (validated){
	      		blockUI();	
	      	}
	      	return validated;
	    },
	    success:function(responseText){	
	    	//去掉遮罩	
			unblockUI();	
			ajaxCallBackNew(responseText.data,responseText.status, '' ,successCallBackFun, successMsg);						    			    					    											
	    }
	});	
}

function easyuiFormSubmit(formTag, url, successCallBackFun, successMsg){	
	$(formTag).form('submit', {
	    "url": url,
	    onSubmit:function(){			
	      	var validated = $(this).form('validate');
	      	if (validated){
	      		blockUI();	
	      	}
	      	return validated;
	    },
	    success:function(responseText){	
	    	
	    	//去掉遮罩	
			unblockUI();				
			ajaxCallBack(responseText, '' ,successCallBackFun, successMsg);						    			    					    											
	    }
	});	
}

function easyuiFormSubmitNew(formTag, url, successCallBackFun, successMsg){	
	$(formTag).form('submit', {
	    "url": url,
	    onSubmit:function(){			
	      	var validated = $(this).form('validate');
	      	if (validated){
	      		blockUI();	
	      	}
	      	return validated;
	    },
	    success:function(responseText){	
	    	
	    	//去掉遮罩	
			unblockUI();				
			ajaxCallBackNew(responseText.data,responseText.status, '' ,successCallBackFun, successMsg);						    			    					    											
	    }
	});	
}

/**
 * 将字符串转换为json对象
 * @param O
 * @returns
 */
function  StringToJSonObject(O) {
	return eval("("+O+")");
}


