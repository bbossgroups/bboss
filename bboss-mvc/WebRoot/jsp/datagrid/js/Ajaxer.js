/**
 * 	封装Ajax 传输类
 * 	URL:传过去处理的页面地址
 *  params :要传过去的参数，。如："reportId=1&templetId=2"
 *	callBackFunction:成功后调用的方法
 *	errorFunction:失败后调用的方法
 *	用法： var mAjaxer = new Ajaxer(url,params,callBackFunction,errorFunction)
 *		  mAjaxer.send();
 *  by:haibo.liu
 *	version 1.0
 */
var Ajaxer = function(URL,params,callBackFunction,errorFunction){
	this.AjaxMethod = "POST";							//默认传输方式 POST;
	this.SendObject = params;								//传输的内容	
	this.ResponseType = "Text";						//返回值类型 
	//async - 是否异步，true为异步，false为同步，默认为true	
	this.Async = true;										//是否异步方式
	
 	
	function createXMLHttp(){
	    var xmlhttp;
	    try{
	        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	    }catch(ex){
	        try{
	            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	         }catch(ex){
	            xmlhttp = new XMLHttpRequest();
	          }
	    	}
	    return xmlhttp;
	}
	
	var xmlHttp;
	
 	this.send =	function ()
	{	
		this.Async = (typeof(callBackFunction) == 'function');
		//如果是同步，则直接返回结果。
		if(!this.Async){
			return this.sendSynchroMethod(URL,params);
		}
		//下面执行的是异步
	    xmlHttp = null;
	    xmlHttp = createXMLHttp();
	    if(xmlHttp == null)
	    {
	        alert("创建xmlHTTP失败！");
	    }else{
	        xmlHttp.onreadystatechange = this.SendBack;	         
	        xmlHttp.open(this.AjaxMethod,URL,this.Async);	       
	        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");	       
	        xmlHttp.send(this.SendObject);
	    }	
	    return "";   
	}
	//如果是同步方式，就采用直接返回结果
	this.sendSynchroMethod = function(URL,params){
		//如果是同步方式，使用单独的_xmlHttp对象	
		//alert("URL="+URL+"\nparams="+params);	
	    var _xmlHttp = createXMLHttp();
	    if(_xmlHttp == null)
	    {
	        alert("创建_xmlHttp失败！");
	    }else{	         
	        _xmlHttp.open(this.AjaxMethod,URL,this.Async);	       
	        _xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");	       
	        _xmlHttp.send(this.SendObject);
	        if(_xmlHttp.status == 200){
	        	return _xmlHttp.responseText;
	        }
	        //alert("服务器发生了"+_xmlHttp.status+"错误！");	
	        throw new Error(_xmlHttp.status,"服务器发生了"+_xmlHttp.status+"错误！");         
	    }	   
	}	
	//如果是异步方式，就采用回调方法
	this.SendBack = function (){
		try{
			if(xmlHttp.readyState == 4){				
				if(xmlHttp.status == 200){
					var res;
					res = xmlHttp.responseText;
					//在将res返回给调用方法之前，需要先处理res，为了不影响其他人使用Ajaxer类，去掉这行代码，因为这行代码耦合了DBUtil类
					//callBackFunction(DBUtil.callBackFunction(res));
					callBackFunction(res);
							
				}else{
					var error = eval(error + "=" + "{\"code\":\"" + xmlHttp.status + "\",\"message\":\"" + xmlHttp.statusText + "\"}" );
					errorFunction(error);
				}
			}
		}catch(e){
			alert(" 服务器发生了"+xmlHttp.status+"错误！"+e);
		}

	}
}
/**
 * 	日志记录类
 * 	logInfo:操作内容
 *  logModule :操作日志模块，从页面中引入 ReportDBTools中定义的模块名称，如：ReportDBTools.MODULE_BQXTBB
 *	log_type 操作类型	(无操作 0 ; 新增 1 ; 更新 2 ; 删除 3 ; 其他 4)
 *  url 引用页面与log.jsp文件的相对路径
 *	用法： new Log().log(logInfo, logModule, log_type);
 *  by:haibo.liu
 *	version 1.0
 */
var Log = function (){
	//上下文，默认为空，如果不是空则设置为null可以自动获取到。
 	this.contextPath = ""; 
 	//当没有设置上下文的时候，截取默认值
 	this.contextPath = getContextPathByClient();
	this.log =	function (logInfo, logModule, log_type){
		var params = "logInfo="+logInfo+"&logModule="+logModule+"&log_type="+log_type;
		url = window.location.protocol+"//"+window.location.host+this.contextPath+"/ynstjj/report/log/logManage.jsp";
		//异步记录日志
		new Ajaxer(url,params,callBackFunction,errorFunction).send();
	}
	callBackFunction = function(){};
	errorFunction = function(){};
}
/*
 * 封装参数
 */
function encodeParams(params){
	return encodeURIComponent(params);
}
//截取上下文
function getContextPathByClient(){		
	var cp = location.pathname ;	
	cp = cp.substring(0,cp.indexOf("/",1));	
	if (cp.substring(0, 1) != "/") 
        cp = "/" + cp;
	if("/creatorepp"==cp){
    	return cp;
    } else {
    	return "";
    }
	return cp;
}
//去空格
String.prototype.trim= function(){
	// 用正则表达式将前后空格
	// 用空字符串替代。 
	return this.replace(/(^\s*)|(\s*$)/g, "");
}	
//去空格
function jstrim(str){
	if(null == str) return null;
	try{				
		return str.trim();
	}catch(e){
		return str;
	}
}
//常用js方法
var Validator = function(){
	
}
//校验是否是一个合法的名字
Validator.isName = function(str){
	var reg = /^[\w\u4e00-\u9fa5，、；‘’“”【】]+$/g;
	return reg.test(str);
}
//检测是否是一个合法的指标名称	
Validator.isItemName = function(str){
	var reg1 = /^[\S]+$/g;	
	var reg2 = /^[^%\'\",;:=+-\\{\\}\[\].]+$/g;
	return ((reg1.test(str)) && (reg2.test(str)));
}