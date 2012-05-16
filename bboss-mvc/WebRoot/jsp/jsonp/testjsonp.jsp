<%@ page contentType="text/html;charset=UTF-8"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <title>Test Jsonp</title>
   
	<!-- 普通的jsonp调用示例开始，定义跨域回调函数 -->
	<script type="text/javascript">
        	function jsonpCallback(result)
        	{
				alert("aaa:" + result.symbol);//弹出跨站 请求返回的json数据对象的symbol属性的值
        	}
    	</script>
    <!-- 普通的jsonp调用示例，向另一个应用demoproject发起mvc请求，并指参数callback（参数名字可任意指定）指定回调函数jsonpCallback-->
	<script type="text/javascript" src="http://localhost:8081/demoproject/examples/jsonp.page?jsonp_callback=jsonpCallback"></script>
	<!-- 普通的jsonp调用示例结束-->
	
	<!-- 采用jquery实现jsonp调用示例开始-->
	<script src="<%=request.getContextPath() %>/include/jquery-1.4.2.min.js" type="text/javascript"></script>   
	<!-- 采用jquery实现jsonp调用示例--> 
	<script type="text/javascript">
        $(function() {
            $.getJSON("http://localhost:8081/demoproject/examples/jsonpwithjquery.page?jsonp_callback=?", function(data) {
            	alert("bbb:" + data.symbol);//弹出跨站 请求返回的json数据对象的symbol属性的值
            });
            //jsonp1337140657188({"postalcodes":[{"adminName2":"Westchester","adminCode2":"119","postalcode":"10504","adminCode1":"NY","countryCode":"US","lng":-73.700942,"placeName":"Armonk","lat":41.136002,"adminName1":"New York"}]});
            $.getJSON("http://www.geonames.org/postalCodeLookupJSON?postalcode=10504&country=US&callback=?", function(data) {
            	alert( data.postalcodes[0].adminName2);//弹出跨站 请求返回的json数据对象的symbol属性的值
            }); 


        });        
    </script>
	<!-- 采用jquery实现jsonp调用示例结束-->
</head>
<body>
</body>
</html>&nbsp;