<script type="text/javascript">
//----------------跨域支持代码开始(非跨域环境请删除这段代码)----------------
var JSON = JSON || {};
JSON.stringify = JSON.stringify || function (obj) {
	var t = typeof (obj);
	if (t != "object" || obj === null) {
		if (t == "string")obj = '"'+obj.replace(/(["\\])/g,'\\$1')+'"';
		return String(obj);
	}
	else { 
		var n, v, json = [], arr = (obj && obj.constructor == Array);
		for (n in obj) {
			v = obj[n]; t = typeof(v);
			if (t == "string") v = '"'+v.replace(/(["\\])/g,'\\$1')+'"';
			else if (t == "object" && v !== null) v = JSON.stringify(v);
			json.push((arr ? "" : '"' + n + '":') + String(v));
		}
		return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
	}  
};
var callback = callback || function(v){
	v=JSON.stringify(v);
	window.name=escape(v);
	window.location='http://<?php echo $_POST['parenthost'];?>/xheditorproxy.html';//这个文件最好是一个0字节文件，如果无此文件也会正常工作	
}
//----------------跨域支持代码结束----------------

var url='test2.zip';

setTimeout(function(){callback(url);},100);
//跨域模式下可直接调用callback，不需要setTimeout延迟
//callback(url);

</script>