/**
 * 	封装DBUtil 数据库底层类，用于Ajax调用
 *	参数说明：
 	sql				：	要执行的sql语句
 	outerCallBack	：	当该参数不为空时，DBUtil以异步方式执行sql语句，需要外部提供一个js方法，否则以同步方式执行sql语句，一般都以同步方式执行
 *	返回参数：
 	当执行查询语句时，如果成功返回二维数组，否则抛出异常
 	当执行其他数据库方法时，如果成功返回"true"，否则抛出异常
 	
 *  DBUtil用法: 可参照页面 testDBHelper.jsp
 	同步方式：
	 	try{
	 		var rs = dbUtil().executeSelect("select * from dual");
	 	}catch(e){
	 		alert(e);
	 	}
	 	等同于
	 	try{
	 		var rs = dbUtil().executeSql("select * from dual");
	 	}catch(e){
	 		alert(e);
	 	} 	
	 	执行批处理：
	 	try{//先定义sql数组
	 		var sqls = ["update ta_test_dbutil t set t.value='v3'","insert into ta_test_dbutil values('n2','v2',4)"];
	 		var rs = dbUtil().executeSql(sqls);
	 	}catch(e){
	 		alert(e);
	 	} 		
 	异步方式：
	 	//doSomeThing为只有一个参数的回调方法
	 	dbUtil().executeSql("select * from dual",doSomeThing);
	 	
 *  by:fenggao.li
 *	version 1.0
 */
 var DBUtil = function(){
 	//以DBUtil开头的就是静态变量
 	//新增
	DBUtil.OP_TYPE_INSERT = "1";
	//删除
	DBUtil.OP_TYPE_DELETE = "2";
	//修改
	DBUtil.OP_TYPE_UPDATE = "3";
	//查询
	DBUtil.OP_TYPE_SELECT = "4";
	//批处理
	DBUtil.OP_TYPE_BATCH = "5";
 	//是否调试
 	DBUtil.debug = false;
 	
 	//数据库操作实现类
	this.dbImplName = "DBUtil";	
	//数据源名称
	this.dbName = "bspf";	
	
	//操作类型：1新增2删除3修改4查询
	this.opType = "";	
	//待执行的sql语句
	this.sql = "";	
 	
 	this.rs = "" ;
 	
 	//外部回调方法
 	//this.outerCallBack ;
 	
 	//上下文，默认为空
 	this.contextPath = ""; 		
 	
 	/*执行新增语句*/
 	this.executeInsert = function(sql,outerCallBack){ 		
 		return this.execute(DBUtil.OP_TYPE_INSERT,sql,outerCallBack);
 	}
 	
 	/*执行删除语句*/
 	this.executeDelete = function(sql,outerCallBack){ 		
 		return this.execute(DBUtil.OP_TYPE_DELETE,sql,outerCallBack);
 	}
 	
 	/*执行修改语句*/
 	this.executeUpdate = function(sql,outerCallBack){
 		return this.execute(DBUtil.OP_TYPE_UPDATE,sql,outerCallBack);
 	}
 	
 	/*执行查询语句*/
 	this.executeSelect = function(sql,outerCallBack){ 		
 		return this.execute(DBUtil.OP_TYPE_SELECT,sql,outerCallBack);
 	}
 	
 	/*执行批处理语句*/
 	this.executeBatch = function(sqls,outerCallBack){ 	
 		//传入的sql必须是数组	
 		return this.execute(DBUtil.OP_TYPE_BATCH,sqls,outerCallBack);
 	}
 	
 	/*执行数据库方法*/
 	this.executeSql = function(sql,outerCallBack){
 		if(null == sql){
 			throw new Error("对不起！SQL语句不能为空！");
 		} 		
 		if(sql instanceof Array){ 			
 			return this.execute(DBUtil.OP_TYPE_BATCH,sql,outerCallBack);
 		}else{
 			sql = jstrim(sql).toLowerCase(); 			
 			if(sql.indexOf("select") == 0){ 				
 				return this.execute(DBUtil.OP_TYPE_SELECT,sql,outerCallBack);
 			}else if(sql.indexOf("insert") == 0){ 				
 				return this.execute(DBUtil.OP_TYPE_INSERT,sql,outerCallBack);
 			}else if(sql.indexOf("delete") == 0){ 				
 				return this.execute(DBUtil.OP_TYPE_DELETE,sql,outerCallBack);
 			}else if(sql.indexOf("update") == 0){ 				
 				return this.execute(DBUtil.OP_TYPE_UPDATE,sql,outerCallBack);
 			}else{
 				throw new Error("对不起！请输入合法的SQL语句！");
 			}
 		}
 	}
 	
 	/*执行数据库方法*/
 	this.execute = function(opType,sql,outerCallBack){ 	
 		//var contextPathTemp = (contextPath==null || contextPath=="") ? contextPath : contextPath+"/";
 		//当没有设置上下文的时候，截取默认值
 		this.contextPath = getContextPathByClient(); 		
 		try{
	 		var url = window.location.protocol+"//"+window.location.host+this.contextPath+"/commons/common/dbHelper.jsp";
	 		//sql = encodeParams(sql); //在传输给JSP页面的时候，将含有+号的SQL参数进行统一编码，可以将特殊字符传输过去 		
	 		var params = "dbImplName="+this.dbImplName+"&dbName="+this.dbName+"&opType="+opType;
	 		if(opType == DBUtil.OP_TYPE_BATCH){
	 			for(var i=0; i<sql.length; i++){
	 				params += "&sql="+encodeParams(sql[i]);
	 			}
	 		}else{
	 			params += "&sql="+encodeParams(sql);
	 		}
	 		var mAjaxer = new Ajaxer(url,params,outerCallBack,errorFunction);
	 		var r = mAjaxer.send();
	 		return DBUtil.callBackFunction(r); 		
 		}catch(e){
 			if(DBUtil.debug){alert("操作出错！SQL：\n"+sql);} 	
 			throw e;		
 		}	
 	}
 	
 	//回调方法
 	DBUtil.callBackFunction = function(res){
 		if(null==res || ""==res) return "";
 		var rs = "";
 		//alert("进入了回调方法:"+res);		
  		//alert("1");
  		var myJsonObject = eval('('+res+')');
  		//alert("myJsonObject="+myJsonObject);
  		//alert("2");
  		var opType = myJsonObject.opType;
  		//alert("opType="+opType);
  		//alert("3");
  		var errCode = myJsonObject.errCode;  		 		
		//如果调用过程中遇到错误
		if(null != errCode){
			//alert(errCode);
			throw new Error(errCode);
			//rs = errCode;
			//return rs;
		}
		//如果是插入类型
		if(opType == DBUtil.OP_TYPE_SELECT){
			rs = myJsonObject.resultSet;
			//alert("callBackFunction.rs="+rs);
			/**
			var rs = myJsonObject.resultSet;		
			for(var i=0; i<rs.length; i++){
				alert(rs[i][0]+","+rs[i][1]+","+rs[i][2]+","+rs[i][3]);
			}
			*/
			
		}else{
			rs = "";
		}
		return rs;
		//eval("doSomeThing(1);");					
 	};
 	//错误处理方法	
	errorFunction = function(error){
		this.rs = "";
		alert("操作失败:"+error);
	};
 }
 
/*定义一个系统变量dbUitl*/
var dbUtil = new DBUtil();	
/*定义一个系统变量pdbUitl*/
var pdbUtil = new DBUtil();	

/**
 * 	封装Ajax 传输类
 * 	URL:传过去处理的页面地址
 *  params :要传过去的参数，。如："reportId=1&templetId=2"
 *	callBackFunction:成功后调用的方法
 *	errorFunction:失败后调用的方法
 *	Ajaxer用法: 
 *	同步方式： var rs = new Ajaxer(url,params).send();，也可使用异步方式，
 *	异步方式： var mAjaxer = new Ajaxer(url,params,callBackFunction,errorFunction).send();
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
	//上下文，默认为空
 	this.contextPath = ""; 
 	//当没有设置上下文的时候，截取默认值
 	if(this.contextPath == null){ 		
 		this.contextPath = getContextPathByClient();
 	}
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
}//截取上下文
function getContextPathByClient(){		
	var cp = location.pathname ;	
	cp = cp.substring(0,cp.indexOf("/",1));	
	if (cp.substring(0, 1) != "/") 
        cp = "/" + cp;
    /*
	if("/creatorepp"==cp){
    	return cp;
    } else {
    	return "";
    }
    */
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