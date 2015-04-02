/*******************************************************************************
 * EasyUI 已有校验： email
 * 
 * 
 */

$.extend($.fn.validatebox.defaults.rules, {
	url : {
		validator : function(value, param) {
			if (value) {
				return /(((https?)|(ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i
						.test(value);
			} else {
				return true;
			}
		},
		message : '请输入有效的URL地址.'
	},
	length : {
		validator : function(value, param) {
			var len = $.trim(value).length;
			return len >= param[0] && len <= param[1];
		},
		message : "输入内容长度必须介于{0}和{1}之间."
	},
	timeCheck : {
		validator : function(value, param) {
			var s = $("input[name=" + param[0] + "]").val();
			// 因为日期是统一格式的所以可以直接比较字符串 否则需要Date.parse(_date)转换
			return value >= s;
		},
		message : '非法数据'
	},
	/**
	 * 判断开始时间和结束时间 value为自身值，param为传入参数集合，可以传多个参数，
	 * 此处传入两个参数，第一个为比较的另外一个日期对象，第二个参数为判断是否为新增的对象，
	 * 如果是新增，则要判断时间必须晚于当前时间，如果为修改数据，则不需要判断与当前时间的大小
	 */
	compare : {
		validator : function(value, param) {
			var now = new Date();
			var compVal = $(param[0]).val();
			var bg = null;
			var ed = null;
			var newId = null;
			if (param.length > 1 && param[1] != null) {
				newId = $(param[1]).val();
			}
			if ($(param[0]).attr('name') == 'bgDate') {
				if (compVal != null && compVal != '') {
					bg = new Date(compVal.replace(/-/g, '/'));
				}
				if (value != null && value != '') {
					ed = new Date(value.replace(/-/g, '/'));
				}
			} else {
				if (value != null && value != '') {
					bg = new Date(value.replace(/-/g, '/'));
				}
				if (compVal != null && compVal != '') {
					ed = new Date($(param[0]).val().replace(/-/g, '/'));
				} else {
					if (newId == null || newId == '') {
						return bg > now;
					} else {
						return true;
					}
				}
				if ((newId == null || newId == '')
						&& ((bg != null && bg <= now) || (ed != null && ed <= now))) {
					return false;
				}
			}
			if (bg != null && ed != null) {
				return bg < ed;
			}
			return true;
		},
		message : '时间输入有误'
	},
	radio : {
		validator : function(value, param) {
			var frm = param[0], groupname = param[1], ok = false;
			$('input[name="' + groupname + '"]', document[frm]).each(
					function() { // 查找表单中所有此名称的radio
						if (this.checked) {
							ok = true;
							return false;
						}
					});

			return ok
		},
		message : '需要选择一项！'
	},
	checkbox : {
		validator : function(value, param) {
			var frm = param[0], groupname = param[1], checkNum = 0;
			$('input[name="' + groupname + '"]', document[frm]).each(
					function() { // 查找表单中所有此名称的checkbox
						if (this.checked)
							checkNum++;
					});

			return checkNum > 0;
		},
		message : '选择1~n项！'
	},
	// 验证汉子
	CHS : {
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/.test(value);
		},
		message : '只能输入汉字'
	},
	
	// 国内邮编验证
	zipcode : {
		validator : function(value) {
			var reg = /^[1-9]\d{5}$/;
			return reg.test(value);
		},
		message : '邮编必须是非0开始的6位数字.'
	},
	// 用户账号验证(只能包括 _ 数字 字母)
	account : {// param的值为[]中值
		validator : function(value, param) {
			if (value.length < param[0] || value.length > param[1]) {
				$.fn.validatebox.defaults.rules.account.message = '用户名长度必须在'
						+ param[0] + '至' + param[1] + '范围';
				return false;
			} else {
				if (!/^[\w]+$/.test(value)) {
					$.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';
					return false;
				} else {
					return true;
				}
			}
		},
		message : ''
	},
	minLength : { // 判断最小长度
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '最少输入 {0} 个字符。'
	},
	length : {
		validator : function(value, param) {
			var len = $.trim(value).length;
			return len >= param[0] && len <= param[1];
		},
		message : "内容长度介于{0}和{1}之间."
	},
	phone : {// 验证电话号码
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i
					.test(value);
		},
		message : '格式不正确,请使用下面格式:020-88888888'
	},
	mobile : {// 验证手机号码
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value);
		},
		message : '手机号码格式不正确(正确格式如：13450774432)'
	},
	phoneOrMobile : {// 验证手机或电话
		validator : function(value) {
			return /^(13|15|18|17)\d{9}$/i.test(value)
					|| /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i
							.test(value);
		},
		message : '请填入手机或电话号码,如13688888888或020-8888888'
	},
	idcard : {// 验证身份证
		validator : function(value) {
			return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
		},
		message : '身份证号码格式不正确'
	},
	floatOrInt : {// 验证是否为小数或整数
		validator : function(value) {
			return /^(\d{1,3}(,\d\d\d)*(\.\d{1,3}(,\d\d\d)*)?|\d+(\.\d+))?$/i
					.test(value);
		},
		message : '请输入数字，并保证格式正确'
	},
	currency : {// 验证货币
		validator : function(value) {
			return /^d{0,}(\.\d+)?$/i.test(value);
		},
		message : '货币格式不正确'
	},
	qq : {// 验证QQ,从10000开始
		validator : function(value) {
			return /^[1-9]\d{4,9}$/i.test(value);
		},
		message : 'QQ号码格式不正确(正确如：453384319)'
	},
	integer : {// 验证整数
		validator : function(value) {
			return /^[+]?[1-9]+\d*$/i.test(value);
		},
		message : '请输入整数'
	},
	chinese : {// 验证中文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value);
		},
		message : '请输入中文'
	},
	english : {// 验证英语
		validator : function(value) {
			return /^[A-Za-z]+$/i.test(value);
		},
		message : '请输入英文'
	},
	unnormal : {// 验证是否包含空格和非法字符
		validator : function(value) {
			return /.+/i.test(value);
		},
		message : '输入值不能为空和包含其他非法字符'
	},
	username : {// 验证用户名
		validator : function(value) {
			return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
		},
		message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
	},
	faxno : {// 验证传真
		validator : function(value) {
			// return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[
			// ]){1,12})+$/i.test(value);
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i
					.test(value);
		},
		message : '传真号码不正确'
	},
	zip : {// 验证邮政编码
		validator : function(value) {
			return /^[1-9]\d{5}$/i.test(value);
		},
		message : '邮政编码格式不正确'
	},
	ip : {// 验证IP地址
		validator : function(value) {
			return /d+.d+.d+.d+/i.test(value);
		},
		message : 'IP地址格式不正确'
	},
	name : {// 验证姓名，可以是中文或英文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value)
					| /^\w+[\w\s]+\w+$/i.test(value);
		},
		message : '请输入姓名'
	},
	carNo : {
		validator : function(value) {
			return /^[\u4E00-\u9FA5][\da-zA-Z]{6}$/.test(value);
		},
		message : '车牌号码无效（例：粤J12350）'
	},
	carenergin : {
		validator : function(value) {
			return /^[a-zA-Z0-9]{16}$/.test(value);
		},
		message : '发动机型号无效(例：FG6H012345654584)'
	},
	email : {
		validator : function(value) {
			return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
		},
		message : '请输入有效的电子邮件账号(例：abc@126.com)'
	},
	msn : {
		validator : function(value) {
			return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
		},
		message : '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
	},
	department : {
		validator : function(value) {
			return /^[0-9]*$/.test(value);
		},
		message : '请输入部门排序号(例：1)'
	},
	same : {
		validator : function(value, param) {
			if ($("#" + param[0]).val() != "" && value != "") {
				return $("#" + param[0]).val() == value;
			} else {
				return true;
			}
		},
		message : '两次输入的密码不一致！'
	}

});

function check(f) {
	var o = $(f.radio1[0]);
	if (!o.validatebox('isValid'))
		o.parent().addClass('validatebox-invalid');
	else
		o.parent().removeClass('validatebox-invalid');

	o = $(f.cb1[0]);
	if (!o.validatebox('isValid'))
		o.parent().addClass('validatebox-invalid');
	else
		o.parent().removeClass('validatebox-invalid');
	return false
}
