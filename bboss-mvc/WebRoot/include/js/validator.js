/**
 * @fileoverview 表单验证类
 * @author creator | creator
 * @version 0.1
 */
(function(){

    /**
     * @constructor
     * @param {Object} 表单验证类 构造函数
     * @author xs
     */
    var $vdt = function(config){
		/**
		* 类指针，当前类
		* @type {Object}
		*/	
        var me = this;
		
		/**
		* 类配置对象, 所有的验证规则及提示信息都是从这里读取
		* @type {Object}
		*/			
        this.opt = config;

		/**
		* 对象获得焦点时的样式处理
		* @type {String}
		*/		
		this.focFun = function(node) {
			me.setState(node, 1);
		};
		/**
		* 验证出错时的样式处理
		* @type {String}
		*/			
        this.errFun = function(node) {
			me.setState(node, 2);
		};
		/**
		* 验证正确时的样式处理
		* @type {String}
		*/
        this.finFun = function(node) {
			me.setState(node, 0);
		};

		/**
		* 分隔符号
		* @type {String}
		*/
		var splitSign = ':';
		
		/**
		 * 设置表单项的样式
         * @param {Object} 表单对象
         * @param {Num} 状态类型 0-默认 1-绿 2-红
		 */
		this.setState = function(node, type) {
			var input = node;
			while(node && node != document) {
				//tagName是否定义
				if(node.tagName){
					if(node.tagName.toLowerCase() == "span" && hasClass(node, "input")) {
						switch(type) {
							case 0:
								$removeClassName(node, "inputGreen");
								$removeClassName(node, "inputRed");
								return;
							case 1:
								$removeClassName(node, "inputRed");
								$addClassName(node, "inputGreen");
								return;
							case 2:
								$removeClassName(node, "inputGreen");
								$addClassName(node, "inputRed");
								return;
							default:
								$removeClassName(node, "inputGreen");
								$removeClassName(node, "inputRed");
								return;
						}
					}
				}
				node = node.parentNode;
			}
		};
		
	   /**
		 * 默认验证函数(正则验证模式)
         * @param {Object} 表单对象
         * @param {Object} Validator 引用实例
		 */
		this.defaultRegFn = function(e, v){
			//trace( 'regFlag:'+ this.regFlag +' ||' +this.reg + '|this.reg.test('+e.value+'):' + this.reg.test(e.value));
			if(this.regFlag) {
				return this.reg.test(e.value) ? this.msg : '';
			}else {
				return !this.reg.test(e.value) ? this.msg : '';
			}
        };

	   /**
		 * 默认验证函数(范围验证模式)
         * @param {Object} 表单对象
         * @param {Object} Validator 引用实例
		 */
		this.defaultRangeFn = function(e, v){
			var len = e.value.getBytes();
			if(!len) return '';
			var alt = (e.alt || e.getAttribute("alt"));
			var range =  /长度{(.+?)}/.exec(alt)[1];
			var l = range.split('-')[0];
			var r = range.split('-')[1];
			if(len < l || len > r) return this.msg.replace('{range}', range);
			return '';
        };

	   /**
		 * 默认验证函数(验证是否与目标值相同[一般用于确认密码])
         * @param {Object} 表单对象
         * @param {Object} Validator 引用实例
		 */
		this.defaultSameFn = function(e, v){
			var val = e.value;
			if(!val) return '';
			var alt = (e.alt || e.getAttribute("alt"));
			var id =  /相同{(.+?)}/.exec(alt)[1];
			if(!$(id)) return '';
			return ($(id).value != val)?this.msg:'';
        };
        
        /**
         * 类初始化，根据表单名为表单下需要验证的对象添加事件
         * @param {String} 表单id
         */
        this.init = function(id){
			var fm = $(id);
			if(!fm) {
				alert('表单 [' + id + '] 未找到！');
				return;
			}
            var fe = fm.elements;
            var fl = fe.length;
            for (var i = 0; i < fl; i++) {
                var cur = fe[i];
                var alt = ((cur.alt || cur.getAttribute("alt")) + '');
                if (alt.indexOf(splitSign) != -1) {
                    cur.onfocus = this.chkFocus;
                    cur.onblur = this.check;
					/**
					 * 如果是密码，则增加绑定输入动作的事件，用来判断密码强度
					 */
					if (cur.id.toLowerCase() == "password") {
						cur.onkeydown = this.chkKeyboard;
						cur.onkeyup = this.chkKeyboard;
					}
					if (cur.id.toLowerCase() == "door") {
						cur.value = "点此显示验证码";
						cur.style.color = "#999999";
					}
					/**
					 * 当前类指针传递给表单域对象，以便调用
					 * @type {Object}
					 */
                    cur.v = this;
                }
            }
			fm.v = this;
            fm.onsubmit = this.checkSubmit;
        };


        /**
         * 判断当前需要使用规则的对象
         * @param {String} alt值中当前规则名
         * @param {Object} 当前规则配置对象
         */
		this.getCur = function(c, conf){
			var cur = conf[c];
			if (c.indexOf('长度')!=-1){
				cur = conf['长度'];
				cur.fn = this.defaultRangeFn;
			}
			if (c.indexOf('相同')!=-1){
				cur = conf['相同'];
				cur.fn = this.defaultSameFn;
			}
			if(!cur) return null;
			if (!cur.fn) cur.fn = this.defaultRegFn;
			return cur;
		};
        
        /**
         * 根据对象alt属性参数设置及config的配置进行表单元素验证
         * @param {Object} fireFox 的事件源(暂时没用到)
         * @param {Object} 检查指定的表单对象
         */		
        this.check = function(e, el){
            var el = el || this;
            var v = el.v;
            var o = v.opt;
            var alt = el.alt || el.getAttribute('alt');
            var args = alt.split(splitSign)[1].split('/');
            var l = args.length;
            for (var i = 0; i < l; i++) {
                var c = args[i];
				var cur = v.getCur(c, o);
                if (cur) {
					try {
						trace(cur.fn);
						var result = cur.fn(el, v);
						if(result == 'custom'){
							break;
						}
						if(result){
							v.showErr(el, result);
							if(result == 'clear') continue;
							return false;
						}
					}catch (e) {
						return false;
					}
                }
            }
			if(result == 'clear') {
				v.showErr(el, result);
			}else {
				v.showErr(el,'hide');
			}
			return true;
        };

        /**
         * 对象获取焦点时执行的事件
         */
        this.chkFocus = function(){
			if(this.type == 'password' || this.type == 'text') {
				me.focFun(this);
			}
            var el = this;
            var v = el.v;
			var cur = v.opt;
            alt = el.alt || el.getAttribute('alt');
			try{
				if(alt.indexOf('focusFn')!= -1){
					//alert(cur.focusFn);
					if(cur.focusFn) cur.focusFn(el, v);
				}
				try {
					me.showErr(el, "clear");
					if (el.id == "password") {
						if (!$isVisible($("passW"))) $show($("passW"));
					}
					if (el.id == "door") {
						if (el.value == "点此显示验证码") {
							el.value = "";
							el.style.color = "#000000";
						}
						if (!$isVisible($("door_img"))) {
							$show($("door_img"));
							con_code();
						}
					}
				}catch (e) {}
			}catch(e){ alert(e.description);}
        };
		
		/**
         * 对象键盘输入时执行的事件
         */
		this.chkKeyboard = function(){
            var el = this;
            var v = el.v;
			var cur = v.opt;
            alt = el.alt || el.getAttribute('alt');
			try{
				if(alt.indexOf('keyFn') != -1) {
					var arg = /keyFn{([^}].+?)}/.exec(alt);
					arg = (arg == null) ? false : arg[1];
					var args = arg.split('/');
					var l = args.length;
					for (var i = 0; i < l; i++) {
						var c = args[i];
						var cur = v.getCur(c, cur);
						if (cur) {
							try {
								trace(cur.fn);
								if(cur.fn) cur.fn(el, v);
							}catch (e) {
								return false;
							}
						}
					}
				}
			}catch(e){ alert(e.description);}
        };

        /**
         * 提交时将表单下所有需要验证的元素都检查一遍，以保证数据全部合格
         */
        this.checkSubmit = function(e){
			var fm = this;
			var v   = fm.v;
			var fe = fm.elements;
			var fl = fe.length;
			var flag = true;
			try{
				for (var i = 0; i < fl; i++) {
						var cur = fe[i];
						var alt = ((cur.alt || cur.getAttribute("alt")) + '');
						if (alt.indexOf(splitSign) != -1){
							if (!$isVisible(cur) && cur.id != "selectQid") continue;
							if (v.opt.submitFn){
								if(v.opt.submitFn(cur)){
									flag = true;
									continue;
								}
							}
							if (!v.check(e, cur)) {
								flag = false;
							}
						}
				}
			}catch(e){ 
				flag = false;
			};
			if(!flag){
				alert("您填写的信息有误，请根据页面红字更改！");
				flag = false;
			}
			return flag;
        };
		
        /**
         * 根据错误类型显示相应的错误提示
         */					
        this.showErr = function(e, msg){
			var alt	= (e.alt || e.getAttribute("alt")).split(splitSign);
			var name = alt[0];
			var args   = alt[1];
			var msg = msg.replace('{name}', name);
			/**
	         *  根据alt参数，判断错误提示输出在哪
	         */
			var eid, errArea ;
			if(args.indexOf('errArea')!=-1) var eid = /errArea{(.+?)}/.exec(alt)[1];
			try {
				$(eid).innerHTML = "";
				e.errNode = null;
			}catch (e) {}
			errArea = $(eid) ? $(eid) : e.parentNode;
            if (!e.errNode) {
                var etips = $C('span');
                errArea.appendChild(etips);
                e.errNode = etips;
            }else {
                var etips = e.errNode;
            }
	        /**
	         *  根据msg判断是否显示错误提示
	         */
			switch(msg) {
				case "hide":
					etips.innerHTML = '<span class="inputacc"><span class="yes"></span></span>';
					if(e.type == 'password' || e.type == 'text') {
						this.finFun(e);
					}
					return;
				case "custom":
					etips.innerHTML = "";
					//errArea.removeChild(e.errNode);
					//e.errNode = null;
					return;
				case "clear":
					etips.innerHTML = "";
					return;
				default:
					etips.innerHTML = '<span class="inputacc link"><span class="error"></span><span class="red">'+ msg +'</span></span>';
					if(e.type == 'password' || e.type == 'text') {
						this.errFun(e);
						try {
							if (e.id == "password") $hide($("passW"));
						}catch (e) {}
					}
					return;
			}
        };
    };

	/**
	 *  注册全局对象
	 */	
    if (window.Validator == null) window.Validator = $vdt;
})();
