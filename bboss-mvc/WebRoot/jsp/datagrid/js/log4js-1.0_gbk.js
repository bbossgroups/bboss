/**
 * 	Log封装类
 */
var Logger = function(debug) {
	//默认不开启调试功能
	if (null == debug) {debug = false};
	this.debug = debug;
	//记录日志
	this.logs = "";
	//是否有错误日志
	this.hasError = false;
	
	/*重载jQuery的内部错误处理*/
	this.error = function(message) {
		this.log("jquery.error()="+message,"error");
		this.hasError = true;
	}
	//暂时设置不拦截
	//jQuery.error = this.error;
	/*记录日志信息*/
	this.log = function(msg,level) {
		if (null == level || "" == level) {
			level = "info";
		}
		if (this.debug) { 
			var prefix = level+">>";
			if (typeof(msg) == "string") {
				var html =  prefix+msg+"<br>";
				//错误信息用红色表示
				if ("error" == level) {
					html = "<font color='red'>"+html+"</font>";
					this.hasError = true;
				} else if ("message" == level) {
					//调试信息用蓝色表示
					html = "<font color='blue'>"+html+"</font>";
				}
				this.logs += "<"+(new Date()).toTimeString()+"><br>"+html;		
			} else {
				//params = jQuery.param(params);
			}
			
		}
	}
	/*显示日志信息*/
	this.showLogs = function() {
		if (true != this.debug) {
			this.logs = "<font color='red'>需要设置为debug=true后才可记录调试信息！</font>";
		}
		var fn = "<center><a href='javascript:void(0)' onclick=\"Logger.mailtoAdmin($('#logdiv').html())\">发送邮件</a>&nbsp;&nbsp;&nbsp;&nbsp;"
		fn += "<a href='javascript:void(0)' onclick=\"Logger.copyToClipboard($('#logdiv').text())\">复制</a></center>"
		var logdiv = "<div id='logdiv'>"+this.logs+"</div>"
		$.messager.alert('调试信息',logdiv+fn);
		this.clearLogs();
	}
	/*清空日志*/
	this.clearLogs = function() {
		this.logs = "";
		this.hasError = false;
	}
}
/**静态设置开始**/
/*截取上下文*/
Logger.getContextPath = function() {	
	//this.log("this.getContextPath()");	
	var cp = location.pathname ;	
	cp = cp.substring(0,cp.indexOf("/",1));	
	if (cp.substring(0, 1) != "/") {
        cp = "/" + cp;
    }
	return cp;
}
/*静态属性*/
Logger.mailtoAdminUrl = window.location.protocol+"//"+window.location.host+Logger.getContextPath()+"/log/mailtoAdmin.htm?";
/*静态方法*/
/*发送邮件*/
Logger.mailtoAdmin = function(msg) {
	//alert(msg);
	var url = Logger.mailtoAdminUrl;
	$.messager.alert('提示信息','恭喜您，邮件发送成功，我们将尽快处理这个问题','info');
}
/*复制到剪切板*/
Logger.copyToClipboard = function(msg) {
	//alert(msg);
	if ($.browser.msie){//判断IE
		window.clipboardData.setData('text', msg);
		$.messager.alert('提示信息','恭喜您，已经复制到了剪贴板！','info');
	}else{
		$.messager.alert('提示信息','您的浏览器不支持剪贴板操作，请自行复制！','error');
	}
			
	
}
