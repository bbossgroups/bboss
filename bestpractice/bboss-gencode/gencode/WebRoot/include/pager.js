var bboss = function () {};
bboss.pager =  {
		/**
		 * 定义分页插件的处理事件，导航前事件:beforeload，ajaxload后事件:afterload。
		 */
		pagerevent:{},
		/**
		 * @param containerid
		 *            jquery容器id
		 * @param selector
		 *            jquery内容选择器
		 * @param url
		 *            页面地址
		 * @param pages
		 *            当前页面总数
		 * @param maxPageItem
		 *            每页显示的最大记录数
		 * @param hasParam
		 *            判断当前页地址是否带参数
		 * @param sortKey
		 *            页面排序字段
		 * @param desc
		 *            列表排序顺序
		 * @param id
		 *            pagerid
		 * @param formName
		 *            页面表单form
		 * @param params
		 *            页面参数
		 * @param promotion
		 *            是否提示页面信息
		 * @return
		 */
		goTo: function (gotopageid,gopageerror_msg,containerid, selector, url, pages, maxPageItem, hasParam,
				sortKey, desc, id, formName, params, promotion) {
		
			var regx = /\d+/g;
			var gotopage = document.getElementById(gotopageid);
		
			var goPage = "NaN";
			if (gotopage.length) {
				for ( var i = 0; i < gotopage.length; i++) {
					// var temp = parseInt(gotopage[i].value);
					// if(!isNaN(temp))
					var temp = gotopage[i].value;
					if (regx.test(temp))
						goPage = temp;
		
				}
			} else {
				// var temp = parseInt(gotopage.value);
				var temp = gotopage.value;
				// if(!isNaN(temp))
				if (regx.test(temp)) {
					goPage = temp;
				}
		
			}
		
			if (isNaN(goPage) || goPage.indexOf(".") != -1) {
				alert(gopageerror_msg); 
				return;
			}
			// 如果跳转页比总页数大时，将跳转页指定为pages
		
			if (pages < goPage) {
				goPage = pages;
			}
		
			var offset = (goPage - 1) * maxPageItem;
			var offset_key = "";
			var desc_key = "";
			var sort_key = "";
			if (id) {
				offset_key = id + ".offset";
				desc_key = id + ".desc"
				sort_key = id + ".sortKey";
			} else {
				offset_key = "pager.offset";
				desc_key = "desc";
				sort_key = "sortKey";
			}
			var forwardurl = url + (hasParam ? "&" : "?") + offset_key + "=" + offset;
		
			if (sortKey)
				forwardurl = forwardurl + "&" + sort_key + "=" + sortKey + "&"
						+ desc_key + "=" + desc;
			// params = (hasParam?"&":"") + offset_key + "=" + offset + "&"+ sort_key +
			// "=" + sortKey + "&" + desc_key + "=" + desc;
			if (sortKey) {
				params += (hasParam ? "&" : "") + offset_key + "=" + offset + "&"
						+ sort_key + "=" + sortKey + "&" + desc_key + "=" + desc;
			} else {
				params += (hasParam ? "&" : "") + offset_key + "=" + offset
			}
		
			var flag = false;
			var executeflag = "false";
			if (containerid == null && this.pagerevent["beforeload"] && (typeof (eval(this.pagerevent["beforeload"])) == "function" )) {
				executeflag = "true";
				try
				{
					flag = this.pagerevent["beforeload"]({"gotopageid":gotopageid,"gopageerror_msg":gopageerror_msg,"containerid":containerid, "selector":selector, 
						"url":url,
						"pages": pages, 
						"maxPageItem":maxPageItem, 
						"hasParam":hasParam,
						sort_key:sortKey, desc_key:desc, "id":id, "formName":formName, "params":params, "promotion":promotion,
						"goPage":goPage,
						offset_key:offset});
				}
				catch(e)
				{
					try
					{
						flag = this.pagerevent["beforeload"]();
					}
					catch(e)
					{
						alert(e);
					}
				}
			}
		
			if (formName && flag) {
		
				this.pageSubmit(formName, params, forwardurl, promotion, id, executeflag,
						flag);
			} else {
		
				if (!containerid) {
		
					location.replace(forwardurl);
				} else {
					this.loadPageContent(forwardurl, containerid, selector);
				}
			}
		
		},
		
		/**
		 * 实现分页标签中翻页按钮提交表单功能 formName:表单名称 params:页面参数 forwardurl:跳转页面 promotion:是否提示信息
		 */
		pageSubmit:function (formName, params, forwardurl, promotion, id, executeflag,
				flag_) {
		
			// 如果需要提示是否保存页面数据的修改，则弹出提示窗口，
			var paramsName = "PAGE_QUERY_STRING";
			if (id)
				paramsName = id + ".PAGE_QUERY_STRING";
			var flag = flag_;
			if (this.pagerevent["beforeload"] && (typeof (eval(this.pagerevent["beforeload"])) == "function")  && (executeflag == null || executeflag == "false")) {
				try
				{
					flag = this.pagerevent["beforeload"]({"formName":formName,"params":params,"promotion":promotion, "id":id});
				}
				catch(e)
				{
					try
					{
						flag = this.pagerevent["beforeload"]();
					}
					catch(e)
					{
						alert(e);
					}
				}
			}
		
			if (promotion && flag) {
		
				if (!confirm("是否保存当前页面修改的数据？\r\n是，点击'确定'；否，点击'取消'.")) {
		
					location.replace(forwardurl);
					// document.all.item(paramsName).value = params;
					// if(document.all.sendOrgId){
					// document.all.sendOrgId.value = "";
					// }
					// document.forms(formName).submit();
				} else {
					// 未知原因，先注销
					// if(commitevent){
					// try
					// {
					// eval(commitevent);
		
					// }
					// catch(e)
					// {
					// alert("Error Command:\n\""+commitevent+"\".\n"+e.description);
					// }
		
					// }
					document.getElementById(paramsName).value = params;
					if (document.getElementById(formName).action
							&& document.getElementById(formName).action != "") {
						document.getElementById(formName).submit();
					} else {
						document.getElementById(formName).action = forwardurl;
						document.getElementById(formName).submit();
					}
				}
			} else {
		
				if (flag == 0) {
		
					document.getElementById(paramsName).value = params;
					if (document.getElementById(formName).action
							&& document.getElementById(formName).action != "") {
						document.getElementById(formName).submit();
					} else {
						document.getElementById(formName).action = forwardurl;
						document.getElementById(formName).submit();
					}
				} else if (flag == 1) {
		
					location.replace(forwardurl);
				} else {
					document.getElementById(paramsName).value = params;
					if (document.getElementById(formName).action
							&& document.getElementById(formName).action != "") {
						document.getElementById(formName).submit();
					} else {
						document.getElementById(formName).action = forwardurl;
						document.getElementById(formName).submit();
					}
		
				}
			}
		},
		___keydowngo:function (event, goid) {
		
			if (event.keyCode == 13) {
				//alert(goid);
				var tets = document.getElementById(goid);
				
				document.getElementById(goid).click();
				
				
			}
		
			return false;
		},
		
		checkAll:function (totalCheck, checkName) {
			var selectAll = document.getElementsByName(totalCheck);
			var o = document.getElementsByName(checkName);
			if (selectAll[0].checked == true) {
				for ( var i = 0; i < o.length; i++) {
					if (!o[i].disabled) {
						o[i].checked = true;
					}
				}
			} else {
				for ( var i = 0; i < o.length; i++) {
					o[i].checked = false;
				}
			}
		}
		,
		checkOne:function (totalCheck, checkName) {
			var selectAll = document.getElementsByName(totalCheck);
			var o = document.getElementsByName(checkName);
			var cbs = true;
			for ( var i = 0; i < o.length; i++) {
				if (!o[i].disabled) {
					if (o[i].checked == false) {
						cbs = false;
					}
				}
			}
			if (cbs) {
				selectAll[0].checked = true;
			} else {
				selectAll[0].checked = false;
			}
		},
		
		convertValue:function (object, needconvert) {
		
			if (needconvert) {
				var ret = object.replace(/:/g, "\\:");
				ret = ret.replace(/\./g, "\\.");
				ret = ret.replace(/\//g, "\\/");
				ret = ret.replace(/\$/g, "\\$");
				ret = ret.replace(/\[/g, "\\[");
				ret = ret.replace(/\]/g, "\\]");
				ret = ret.replace(/#/g, "\\#");
				ret = ret.replace(/;/g, "\\;");
				ret = ret.replace(/&/g, "\\&");
				ret = ret.replace(/,/g, "\\,");
				ret = ret.replace(/\+/g, "\\+");
				ret = ret.replace(/\*/g, "\\*");
				ret = ret.replace(/~/g, "\\~");
				ret = ret.replace(/'/g, "\\'");
				// ret = ret.replace(/"/g,"\\"");
				ret = ret.replace(/!/g, "\\!");
				ret = ret.replace(/\^/g, "\\^");
				ret = ret.replace(/\(/g, "\\(");
				ret = ret.replace(/\)/g, "\\)");
				ret = ret.replace(/=/g, "\\=");
				ret = ret.replace(/>/g, "\\>");
				ret = ret.replace(/\|/g, "\\|");
				ret = ret.replace(/ /g, "\\ ");
				return ret;
		
			} else {
				return object;
			}
		},
		
		/**
		 * jquery方式加载页面分页内容
		 * 
		 * @param pageurl
		 *            页面地址
		 * @param containerid
		 *            存放页面内容的容器
		 * @param selector
		 *            选择页面内容范围的选择器
		 * @return
		 */
		//function loadPageContent(pageurl, containerid, selector) {
		//	containerid = convertValue(containerid, true);
		//	
		//	var idx = pageurl.indexOf("?");
		//	
		//	var queryString = null;
		//	var params = null;
		//	var tempurl = null;
		//	
		//	if(idx >= 0 )
		//	{
		//		
		//		queryString = pageurl.substring(idx+1);
		//		
		//		tempurl = pageurl.substring(0,idx)
		//			
		//		params = ___parserParams(queryString);
		//		
		//	}
		//	else
		//	{
		//		tempurl = pageurl;
		//	}
		//
		//	
		//	
		//	if (selector && selector != "") {
		//		$("#" + containerid).load(tempurl + " #" + selector,params,function(){
		//			setTable_grayCss();
		//			if($.parser)	$.parser.parse("#" + containerid);
		//		});
		//		
		//	} else {
		//		$("#" + containerid).load(tempurl,params,function(){
		//			setTable_grayCss();
		//			if($.parser)	$.parser.parse("#" + containerid);
		//		});
		//	}
		//}
		
		
		loadPageContent:function (pageurl, containerid, selector) {
			containerid = this.convertValue(containerid, true);	
			pagerevent = this.pagerevent;
			setTable_grayCss = this.setTable_grayCss;
			var flag = true;
			if (pagerevent["beforeload"] && (typeof (eval(pagerevent["beforeload"])) == "function" )) {
				
				try
				{
					flag = pagerevent["beforeload"]({"pageurl":pageurl, "containerid":containerid, "selector":selector});
				}
				catch(e)
				{
					try
					{
						flag = pagerevent["beforeload"]();
					}
					catch(e)
					{
						alert(e);
					}
				}
			}
			
			if(flag === false)
				return;
			if (selector && selector != "") {
				$("#" + containerid).load(pageurl + " #" + selector,function(){
					//setTable_grayCss();
					if($.parser)	$.parser.parse("#" + containerid);
					try{
				          if(typeof(eval(loadjs))=="function")  
				          {
				        	  loadjs();
				          }        
						}
						catch(e){}
						
						if (pagerevent["afterload"] && (typeof (eval(pagerevent["afterload"])) == "function" )) {
							try
							{
								pagerevent["afterload"]({"pageurl":pageurl, "containerid":containerid, "selector":selector});  
							}
							catch(e)
							{
								try{pagerevent["afterload"]();}catch(e){alert(e);}
							}
						}	
				});
				
			} else {
				$("#" + containerid).load(pageurl,function(){
					try{
						setTable_grayCss()
					}catch(e){
						
					};
					if($.parser)	$.parser.parse("#" + containerid);
					try{
			          if(typeof(eval(loadjs))=="function")  
			          {
			        	  loadjs();
			          }        
					}
					catch(e){}	
					if (pagerevent["afterload"] && (typeof (eval(pagerevent["afterload"])) == "function") ) {						
						try
						{
							pagerevent["afterload"]({"pageurl":pageurl, "containerid":containerid, "selector":selector});
						}
						catch(e)
						{
							try{pagerevent["afterload"]();}catch(e){alert(e);}
						}
					}	
				});
			}
			
		},
		
		___parserParams:function (queryString)
		{
			
			if(queryString == null )
			{
				return null;
			}
			
			// 如果链接没有参数，或者链接中不存在我们要获取的参数，直接返回空
			var params = new Array();
			
		
			// 分离参数对 ?key=value&key2=value2
			var parameters = queryString.split("&");
			if(parameters.length)
			{
				var pos, paraName, paraValue;
				var datas = "{";
				for(var i=0; i<parameters.length; i++)
				{
					// 获取等号位置
					pos = parameters[i].indexOf('=');
					if(pos == -1) { continue; }
			
					// 获取name 和 value
					if(i > 0)
						datas += ",";
					datas += "'"+parameters[i].substring(0, pos)+"'";
					datas += ":";
					datas += "'" + parameters[i].substring(pos + 1)+ "'";
					
				}
				datas += "}";
			}
			else
			{
				var pos = parameters.indexOf('=');
				if(pos == -1) { return null }
		
				// 获取name 和 value
				datas += "{'" + parameters.substring(0, pos);
				datas += "':";
				datas += "'" + parameters.substring(pos + 1)+ "'}";
			}
			
			return this._____parseObj(datas);
		
		},
		_____parseObj:function ( strData ){
			
			try
			{
		        return (new Function( "return " + strData ))();
			}
			catch(e)
			{
				alert(e);
			}
			return null;
		
		},
		
		
		
		
		
		
		/**
		 * 切换页面记录大小函数
		 * 
		 * @param cookieid
		 *            存放页面记录大小的cookie名称
		 */
		__chagePageSize:function (event, cookieid, selectkey,pageurl,selector,containerid) {
			if (event) {
		
				var select_ = event.srcElement || event.target;
		
				var pagesize = select_.options[select_.selectedIndex].value;
				// var Days = 100; //此 cookie 将被保存 30 天
				// var exp = new Date(); //new Date("December 31, 9998");
				// exp.setTime(exp.getTime() + Days*24*60*60*1000);
				// document.cookie = name + "="+ pagesize +";expires="+
				// exp.toGMTString();
				this.cookiehandle(cookieid, pagesize, {
					path : '/',
					expires : 100
				});
				
				if(containerid && containerid != "")
				{
					this.loadPageContent(pageurl, containerid, selector);
				}
				else
				{
					location.replace(pageurl);
				}
			} else {
				var select_ = window.event.srcElement;
		
				var pagesize = select_.options[select_.selectedIndex].value;
				this.cookiehandle(cookieid, pagesize, {
					path : '/',
					expires : 100
				});
				
				if(containerid && containerid != "")
				{
					this.loadPageContent(pageurl, containerid, selector);
				}
				else
				{
					location.replace(pageurl);
				}
			}
		
		},
		
		cookiehandle:function (name, value, options) {
			if (typeof value != 'undefined') {
				options = options || {};
				if (value === null) {
					value = '';
					options = $.extend( {}, options);
					options.expires = -1;
				}
				var expires = '';
				if (options.expires
						&& (typeof options.expires == 'number' || options.expires.toUTCString)) {
					var date;
					if (typeof options.expires == 'number') {
						date = new Date();
						date.setTime(date.getTime()
								+ (options.expires * 24 * 60 * 60 * 1000));
					} else {
						date = options.expires;
					}
					expires = '; expires=' + date.toUTCString();
				}
				var path = options.path ? '; path=' + (options.path) : '';
				var domain = options.domain ? '; domain=' + (options.domain) : '';
				var secure = options.secure ? '; secure' : '';
				document.cookie = [ name, '=', encodeURIComponent(value), expires,
						path, domain, secure ].join('');
			} else {
				var cookieValue = null;
				if (document.cookie && document.cookie != '') {
					var cookies = document.cookie.split(';');
					for ( var i = 0; i < cookies.length; i++) {
						var cookie = jQuery.trim(cookies[i]);
						if (cookie.substring(0, name.length + 1) == (name + '=')) {
							cookieValue = decodeURIComponent(cookie
									.substring(name.length + 1));
							break;
						}
					}
				}
				return cookieValue;
			}
		},
		
		/**
		 * 设置表格的鼠标移动及点击样式
		 * 表格ID或class，例如：setTable_grayCss(".table_gray") || setTable_grayCss("#tbid") 
		 * @return
		 */
		setTable_grayCss:function (idorClass) {
			if(idorClass==undefined || idorClass=="") idorClass = ".table_gray";
		
			$(idorClass + ' tbody tr:even').addClass('space_color');
			$(idorClass + ' tbody tr').hover(
			   function() {$(this).addClass('highlight');},
			   function() {$(this).removeClass('highlight');}
			);
		
			// 复选框 
		
			$(idorClass + ' tbody tr').click(
			   function() {
			    if ($(this).hasClass('selected')) {
			     $(this).removeClass('selected');
			     $(this).find('input[type="checkbox"]').removeAttr('checked');
			    } else {
			     $(this).addClass('selected');
			    $(this).find('input[type="checkbox"]').attr('checked','checked');
			    }
			   }
			);
		}
};

var checkAll = bboss.pager.checkAll;
var checkOne = bboss.pager.checkOne;