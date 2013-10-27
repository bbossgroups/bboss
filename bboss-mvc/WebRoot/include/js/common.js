/**
 * @fileoverview  基础函数库
 * @author xs | creator
 */

/**
 *  debug box
 */
function trace(txt) {
	return;
	if(!$('debugbox')) {
		var debug = $C('textarea');
		debug.id = 'debugbox';
		debug.rows = '20';
		debug.cols = '150';
		document.body.appendChild(debug);
	}else{
		var debug = $('debugbox');
	}
	debug.value += (txt + ' \n--------------------------------------------------\n\n');
}

/**
 * 给Function对象添加bind方法
 * @param {Object} 目标绑定对象
 */
Function.prototype.bind = function(object) {
	var __method = this;
	return function() {
		__method.apply(object, arguments);
	}
};

/**
 * 给String对象添加getBytes方法，以获取实际字节长度(中文算2字节)
 */
String.prototype.getBytes = function() {
	return this.replace(/[^\x00-\xff]/ig,"oo").length;
};

/**
 * 在指定节点上绑定相应的事件
 * @method $addEvent2
 * @param {String} elm 需要绑定的节点id
 * @param {Function} func 事件发生时响应的函数
 * @param {String} evType 事件的类型如:click, mouseover
 * @global $addEvent2
 * @update 2008-2-28
 * @author	creator
 * @example
 * 		//鼠标点击testEle则alert "clicked"
 * 		$addEvent2("testEle",function () {
 * 			alert("clicked")
 * 		},'click');
 */
function $addEvent2(elm, func, evType, useCapture) {
	var elm = $(elm);
	if(!elm) return;
	var useCapture = useCapture || false;
	var evType = evType || 'click';
	if (elm.addEventListener) {
		elm.addEventListener(evType, func, useCapture);
		return true;
	}
	else if (elm.attachEvent) {
		var r = elm.attachEvent('on' + evType, func);
		return true;
	}
	else {
		elm['on' + evType] = func;
	}
};

/**
 * 非IE浏览器增加contains方法
 * @example obj.contains(obj);
 */
if(!(/msie/).test(navigator.userAgent.toLowerCase())) {
	if(typeof(HTMLElement) != "undefined") {
		HTMLElement.prototype.contains = function (obj) {
			while(obj != null && typeof(obj.tagName) != "undefind") {
				if(obj == this) return true;
				obj = obj.parentNode;
			}
			return false;
		};
	}
}

/**
 * document.getElementById 的快捷方式
 * @param {String} id字串
 */
function $(oID) {
	var node = typeof oID == "string" ? document.getElementById(oID) : oID;
	if (node != null) {
		return node;
	}
	return null;
}

/**
 * document.createElement 的快捷方式
 * @param {String} tagName字串
 */
function $C(tagName){
    return document.createElement(tagName);
}

/**
 * 判断某字符串是否在目标数组中
 * @param {String}  目标关键字
 * @param {Array}   目标数组
 * @param {Boolean}   是否全等(即完全相同)
 */
function $inArr(key, arr, same) {
	if(!same) return arr.join(' ').indexOf(key) != -1;
	for (var i=0, l = arr.length; i< l ; i++) if(arr[i]==key) return true;
	return false;
}

/**
 * 为对象添加 className
 * @param {Object} 需要添加className的节点
 * @param {String}  要添加的 className
 */
function $addClassName(el, cls) {
	var el = $(el);
	if(!el) return;
	var clsNames = el.className.split(/\s+/);
	if(!$inArr(cls, clsNames, true)) el.className += ' '+cls;
};

/**
 * 为对象删除 className
 * @param {Object} 需要删除className的节点
 * @param {String}  要删除的 className
 */
function $removeClassName(el, cls) {
	var el = $(el);
	if(!el) return;
	el.className = el.className.replace(new RegExp("(^|\\s+)" + cls + "(\\s+|$)"), ' ');
};

/**
 * 判断对象是否存在该 className
 * @param {Object} 需要判断className的节点
 * @param {String}  要判断的 className
 */
function hasClass(node, className) {
	var elementClassName = node.className;
	return (elementClassName.length > 0 && (elementClassName == className || new RegExp("(^|\\s)" + className + "(\\s|$)").test(elementClassName)));
}

/**
 * 取得元素的Left
 * @param {Object} 需要取值的元素节点
 */
function getLeft(node) {
	var obj = node;
	var aLeft = obj.offsetLeft;
	while(obj = obj.offsetParent) {
		aLeft += obj.offsetLeft;
	}
	return aLeft;
}

/**
 * 取得元素的Top
 * @param {Object} 需要取值的元素节点
 */
function getTop(node) {
	var obj = node;
	var aTop = obj.offsetTop;
	while(obj = obj.offsetParent) {
		aTop += obj.offsetTop;
	}
	return aTop;
}

/**
 * 获取当前对象所应用的样式
 * @param {Object} 目标对象
 * @param {String}  需要获取的样式属性
 */
function GetCurrentStyle(el, prop) {
	if (el.currentStyle) {
		return el.currentStyle[prop];
	}else if (window.getComputedStyle) {
		prop = prop.replace(/([A-Z])/g, "-$1");
		prop = prop.toLowerCase();
		return window.getComputedStyle(el, "").getPropertyValue(prop);
	}
	return null;
}

/**
 * 切换HTML元素显示/隐藏状态
 * @param {String} 元素id
 * @param {String} 元素 display 状态字符 ( 'none' , 'block', '');
 */
function $toggle(el, flag){
	var el = $(el);
	if(!el) return;
	var curState = GetCurrentStyle(el, 'display');
	if(typeof flag == 'undefined') flag = (curState == 'none' ? 'block' : 'none');
	el.style.display = flag;
}

/**
 * 显示一个HTML元素
 * @param {String} 元素id
 */
function $show(el){
	$toggle(el, '');
}

/**
 * 隐藏一个HTML元素
 * @param {String} 元素id
 */
function $hide(el){
	$toggle(el, 'none');
}

/**
 * 判断元素是否可见
 * @param {Object} 要判断的页面元素
 */
function $isVisible(el){
	while(el && el != document) {
		if (GetCurrentStyle(el, 'visibility') == 'hidden' || GetCurrentStyle(el, 'display') == 'none') return false;
		el = el.parentNode;
    }
	return true;
}

/**
 * 当文档载入后执行一段函数
 * @param {Function} 要执行的函数
 */
function onReady(fn) {
	if (typeof fn != "function") return;
	if (window.addEventListener) {
		window.addEventListener("load", fn, false);
	}else {
		window.attachEvent("onload", fn);
	}
}