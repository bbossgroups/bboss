/**
 * @fileoverview 表单验证类 验证规则配置对象，与validator.js配合实现表单验证
 * @author creator
**/
(function(){

	/**
	* SSO check user exist
	* author creator
	* date 2007.5.8
	* last modify by creator
	*/
	var checkUrl = "/signup/check_user.php";
	var checkDomainUrl = "/signup/check_domain.php";
	var memberYes = "用户名已被注册";
	var memberNo = "用户名可用";
	var error = "异步通信错误";
	var defer = "您的查询次数过多，请一分钟后再查询";
	var type1 = "freemail",type2 = "vipmail",type3 = "sinauser",type4 = "2008mail";
	
	/* ajax engine */
	function ajaxCheck(url,from,name, callBack) {
		var XHR;
		var date = new Date();
		var parameter = "from=" + from + "&name=" + name + "&timeStamp= " + date.getTime();
		try {
			try{
				XHR=new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e){
					try{
						XHR=new XMLHttpRequest();
					} catch (e){ }
			}
			XHR.open("POST",url);
			XHR.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			XHR.onreadystatechange = function(){
				if(XHR.readyState==4) {
					if(XHR.status==200) {
						if(callBack) callBack(from,XHR.responseText);
					}
				}
			}
			XHR.send(parameter);
		}catch (e) {
			alert(e.toString());
		}
	}
	
	/* checkUserExist */
	function checkUserExist(from,name, callBack) {
		ajaxCheck(checkUrl,from,name, callBack);
	}				
	function checkDomainExist(from,name,callBack) {
		ajaxCheck(checkDomainUrl,from,name,callBack);
	}

	/**
	 * 判断用户名是否有重复，上面已经引入方法
	 */	
	function checkUsername(e, v) {
		checkUserExist('othermail', e.value, function(from, responseText){
			if(from == 'othermail'){
				var msg = "";
				if(responseText.search("no")>=0){
					msg = 'hide';
				}else if(responseText.search("yes")>=0){
					msg = '******************';
				}else{
					msg = '异步通信错误';
				}
				v.showErr(e, msg);
			}
		});
	}

	/**
	 * 判断用户名是否有重复，上面已经引入方法
	 */	
	function checkFreemail(e, v) {
		checkUserExist('freemail', e.value, function(from, responseText){
			if(from == 'freemail'){
				var msg = "";
				if(responseText.search("no")>=0){
					msg = 'hide';
				}else if(responseText.search("yes")>=0){
					msg = '该邮箱名已被占用';
				}else{
					msg = '异步通信错误';
				}
				v.showErr(e, msg);
			}
		});
	}

	/**
	 *  通过 Ajax 判断域名是否有重复，上面已经引入方法
	 */
	function checkDomain(e, v) {
		checkDomainExist('sinauser', e.value, function(from, responseText){
			if(from == 'sinauser'){
				var msg = "";
				if(responseText.search("no")>=0){
					msg = 'hide';
				}else if(responseText.search("yes")>=0){
					msg = '域名已被用';
				}else{
					msg = '异步通信错误';
				}
				v.showErr(e, msg);
			}
		});
	}
	
	/**
	 *  计算密码强度方法1
	 */
	function CharMode(iN) {
		if (iN >= 65 && iN <= 90) return 2;
		if (iN >= 97 && iN <= 122) return 4;
		else return 1;
	}
	
	/**
	 *  计算密码强度方法2
	 */
	function bitTotal(num) {
		var modes = 0;
		for (var i=0;i<3;i++) {
			if (num & 1) modes++;
			num >>>= 1;
		}
		return modes;
	}
	
	
	
	/**
	 * 表单验证类 验证规则配置对象
	 */
	var conf = {
		/**
		 *  当表单提交时执行的函数
		 */
		//'submitFn': function(el){
			
		//},
		/**
		 *  获取焦点时执行的函数
		 *  其中判断强度需要有相应HTML结构支持
		 */
		'focusFn': function(el, v){
			var alt = el.alt;
			var arg = /focusFn{([^}].+?)}/.exec(alt);
			arg = (arg == null) ? false : arg[1];
			$removeClassName($(arg), 'hide');
		},
		'长度': {						
			msg: '{name}长度应为{range}位'
		},
		'相同': {						
			msg: '{name}不一致'
		},
		"无内容": {
			msg: '请输入{name}',
			reg: /./
		},
		"无内容sel": {
			msg: '请选择{name}',
			reg: /./
		},
		"全数字": {
			msg: '{name}不能为全数字',
			reg: /[^\d]+/
		},
		"有数字": {
			msg: '{name}不能有数字',
			reg: /^[^\d]+$/
		},
		"有空格": {
			msg: '{name}不能包含空格符',
			reg: /^[^ 　]+$/
		},
		"邮箱地址": {
			msg: '邮箱地址格式不正确',
			reg: /^[0-9a-z][_.0-9a-z-]{0,31}@([0-9a-z][0-9a-z-]{0,30}\.){1,4}[a-z]{2,4}$/
		},
		"手机号码": {
			msg: '{name}不正确',
			reg: /^1(3\d{1}|5[389])\d{8}$/
		},
		"证件号码": {
			msg: '{name}不正确',
			reg: /^(d){5,18}$/
		},
		"有大写": {
			msg: '{name}不能有大写字母',
			reg: /[A-Z]/,
			regFlag: true
		},
		"有全角": {
			msg: '{name}不能包含全角字符',
			reg: /[\uFF00-\uFFFF]/,
			regFlag: true
		},
		"首尾不能是空格": {
			msg: '首尾不能是空格',
			reg: /(^\s+)|(\s+$)/,
			regFlag: true
		},
		"怪字符": {
			msg: '{name}不能包含特殊字符',
			reg: />|<|,|\[|\]|\{|\}|\?|\/|\+|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|`/i ,
			regFlag : true
		},
		"怪字符pwd": {
			msg: '密码请勿使用特殊字符',
			reg: />|<|\+|,|\[|\]|\{|\}|\/|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|`/i,
			regFlag : true
		},
		"全部怪字符": {
			msg: '{name}不能包含特殊字符',
			reg: />|<|,|\[|\]|\{|\}|\?|\/|\+|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|\-|\—|\.|`/i ,
			regFlag : true
		},
		"目标地址怪字符": {
			msg: '{name}不能包含 . 以外的特殊字符',
			reg: />|<|,|\[|\]|\{|\}|\?|\/|\+|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|\-|\—|`/i ,
			regFlag : true
		},
		"服务地址怪字符": {
			msg: '{name}不能包含 :/,.() 以外的特殊字符',
			reg: />|<|\[|\]|\{|\}|\?|\+|=|\||\'|\\|\"|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\-|\—|`/i ,
			regFlag : true
		},
		"部分怪字符": {
			msg: '{name}不能包含特殊字符,可以包含:> . ',
			reg: /<|,|\[|\]|\{|\}|\?|\/|\+|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|\-|\—|`/i ,
			regFlag : true
		},
		"有中文": {
			msg: '{name}不支持中文',
			reg: /[\u4E00-\u9FA5]/i,
			regFlag : true
		},
		"特殊字符": {
			msg: '{name}不支持特殊字符',
			reg: /[^a-zA-Z\.·\u4E00-\u9FA5\uFE30-\uFFA0]/i,
			regFlag : true
		},
		"下划线": {
			msg: '下划线不能在最后',
			fn:  function(e, v){
				var val = e.value;
				return (val.slice(val.length-1)=="_") ? this.msg : '';
			}
		},
		"首尾不能是下划线": {
			msg: '首尾不能是下划线',
			reg: /(^_+)|(_+$)/,
			regFlag: true
		},
		"有下划线": {
			msg: '不能包含下划线',
			fn:  function(e, v){
				var val = e.value;
				return (val.search("_") >= 0) ? this.msg : '';
			}
		},
		"可为空": {
			fn:  function(e, v){
				if(!e.value){
					e.style.background = '';
					return 'custom';
				}else { 
					return ''; 
				}
			}
		},
		"数字字母": {
			msg: '不能包含数字和英文字母以外的字符',
			reg: /[^0-9a-zA-Z]/i,
			regFlag : true
		},
		"数字": {
			msg: '不能包含数字以外的字符',
			reg: /[^0-9]/i,
			regFlag : true
		},
		"数字字母中文": {
			msg: '不能包含数字、英文字母和汉字以外的字符',
			reg: /[^0-9a-zA-Z\u4E00-\u9FA5]/,
			regFlag : true
		},
		"数字字母中文空格下划线": {
			msg: '不能包含全角字符',
			reg: /[^0-9a-zA-Z\u4E00-\u9FA5\_\ ]/,
			regFlag : true
		},
		"无选中": {
			msg: '请选择{name}',
			fn: function(e,v) {
				switch (e.type.toLowerCase()) {
					case 'checkbox':
						return e.checked ? 'clear' : this.msg;
					case 'radio':
						var radioes = document.getElementsByName(e.name);
						for(var i=0; i<radioes.length; i++) {
							if(radioes[i].checked) return 'clear';
						}
						return this.msg;
					default:
						return 'clear';
				}
			}
		},
			"无选择": {
			msg: '请选择{name}',
			fn: function(e,v) {
				switch (e.type.toLowerCase()) {
					case 'select-one':
							return e.value ? 'clear': this.msg;
					default:
						return 'clear';
				}
			}
		},
		"条款": {
			msg: '{name}',
			fn: function(e,v) {
				switch (e.type.toLowerCase()) {
					case 'checkbox':
						return e.checked ? 'clear' : this.msg;
					case 'radio':
						var radioes = document.getElementsByName(e.name);
						for(var i=0; i<radioes.length; i++) {
							if(radioes[i].checked) return 'clear';
						}
						return this.msg;
					default:
						return 'clear';
				}
			}
		},
		"判断强度": {
			fn: function(e,v) {
				for (var i=1;i<=3;i++) {
					try {
						$removeClassName($("passW" + i), "passWcurr");
					}catch (e) {}
				}
				var password = e.value;
				var Modes = 0;
				var n = password.length;
				for (var i=0;i<n;i++) {
					Modes |= CharMode(password.charCodeAt(i));
				}
				var btotal = bitTotal(Modes);
				if (n >= 10) btotal++;
				switch(btotal) {
					case 1:
						try {
							$addClassName($("passW1"), "passWcurr");
						}catch (e) {}
						return;
					case 2:
						try {
							$addClassName($("passW2"), "passWcurr");
						}catch (e) {}
						return;
					case 3:
						try {
							$addClassName($("passW3"), "passWcurr");
						}catch (e) {}
						return;
					case 4:
						try {
							$addClassName($("passW3"), "passWcurr");
						}catch (e) {}
						return;
					default:
						return;
				}
			}
		},
		"判断验证码": {
			fn: function(e,v) {
				if (/^[0-9a-zA-Z]/.test(e.value)) {
					if (/[^0-9a-zA-Z]/.test(e.value)) return "验证码错误";
				}else if (/^[\u4E00-\u9FA5]/.test(e.value)) {
					if (/[^\u4E00-\u9FA5]/.test(e.value)) return "验证码错误";
				}
				return "";
			}
		},
		"排重": { fn: checkUsername },
		"邮箱注册排重": { fn: checkFreemail },
		"查域名": { fn: checkDomain }
	}
	
	//注册全局conf对象
	if (window.$vconf == null) window.$vconf = conf;
})();
