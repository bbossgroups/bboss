(function($){
		$.extend($.fn.validatebox.defaults.rules, {
			number : {
				validator : function(value, param) {
					return /^[0-9]+$/.test(value);
				},
				message : '* 请输入数字.'
			},
			float :{
				validator : function(value, param) {
					return /^\d+(\.\d+)?$/.test(value);
				},
				message : '* 请输数字.'
			} ,
			landline :{
				validator : function(value, param) {
					if(value.length < 12){
						return false;
					}
					return /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(value);
				},
				message : '* 请输入正确的座机格式.'
			} ,
			photo :{
				validator : function(value, param) {
					return /^1[3458]\d{9}$/.test(value);
				},
				message : '* 请输入正确的手机号码.'
			} ,
			ip : {
				validator : function(value, param) {
					return /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(value);
				},
				message : '* 请输入有效的IP.'
			},
			serverURL : {
				validator : function(value, param) {
					return /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\:[0-9]{1,5}([0-9a-zA-Z|.]*)$/.test(value);
				},
				message : '* 请输入有效的URL,例如:127.0.0.1:8080'
			},
			date : {
				validator : function(value, param) {
					return /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29))$/.test(value);
				},
				message : '* 请输入有效的日期.'
			},
			letter : {
				validator : function(value, param) {
				return /^[a-zA-Z]+$/.test(value);
				},
				message : '* 只能输入英文字母.'
			},
			Caracters : {
				validator:function(value,param)
				{
					if(value=="") return true;
					return /^[0-9a-zA-Z|\u4e00-\u9fa5]+$/.test(value);
				},
				message : '* 只能输入英文字母、数字和中文.'
			},
			noSpecialCaracters : {
				validator : function(value, param) {
				return /^[0-9a-zA-Z]+$/.test(value);
				},
				message : '* 只能输入英文字母或数字.'
			},
			zipcode : {
				validator : function(value, param) {
				return /^[1-9]\d{5}$/.test(value);
				},
				message : '* 请输入有效的邮政编码.'
			},
			keyVale:{
				validator : function(value,param)
				{
					var valid = true;
					var arrs = value.split("\n");
					var item;
					var kevals;
					
					for(var i=0;i<arrs.length;i++)
					{
						item = arrs[i];
						kevals= item.split("=");
						if(kevals.length!=2)
						{
							valid = false;
							break;
						}
						
						//验证key与value的正确性 \s*[.A-Za-z0-9_-]
						valid = /^[0-9a-zA-Z|_]+$/.test(kevals[0]) && /^[0-9a-zA-Z|_|:|//|.]+$/.test(kevals[1].replace(/[\r\n]/g,""));
						if(!valid)
						{
						 	break;
						} 
					} 
					return valid;
				},
				message : '输入的参数格式不正确.'
			}
		});	
})(jQuery);