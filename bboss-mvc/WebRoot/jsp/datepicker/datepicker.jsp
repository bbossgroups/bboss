<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>DatePicker</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/jsp/datepicker/My97DatePicker/WdatePicker.js"></script>
         <link rel="shortcut icon"
		href="${pageContext.request.contextPath}/css/favicon.gif">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/tables.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/main.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/mainnav.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/messages.css"
		type="text/css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/classic/tooltip.css"
		type="text/css">
	</head>
 
	<body>
	<h1>DatePicker控件的使用demo</h1>
	<table class="genericTbl">
	   <tr>
	   <th class="order1 sorted" >demo描述
	   </th>
	   <th class="order1 sorted">演示区
	   </th>
	   </tr>
	 <tr class="even" >
	   
	    <td align="right">
	   普通触发：
	    </td>
	    <td>
	   <input id="d12" type="text"
        onclick="WdatePicker({el:'d12'})" src="${pageContext.request.contextPath}/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
	   </td>
	   </tr>
	   
	   <tr class="even">
	    <td align="right">
	   图标触发：
	   </td>
	    <td>
	   <input id="d123" type="text"/>
       <img onclick="WdatePicker({el:'d123'})" src="${pageContext.request.contextPath}/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle">
	   </td>
	   </tr>
	   <tr class="even">
	    <td align="right">
	   周显示简单应用
	   </td>
	    <td>
	   <input id="d121" type="text" onfocus="WdatePicker({isShowWeek:true})"/>
	   </td>
	   </tr>
		 <tr class="even">
	    <td align="right">
		利用onpicked事件把周赋值给另外的文本框
		</td>
	    <td>
		<input type="text" class="Wdate" id="d122" onFocus="WdatePicker({isShowWeek:true,onpicked:function() {$dp.$('d122_1').value=$dp.cal.getP('W','W');$dp.$('d122_2').value=$dp.cal.getP('W','WW');}})"/>
		您选择了第<input style="width: 20px" type="text" id="d122_1" />(W格式)周，
		另外您可以使用:<input style="width: 20px" type="text" id="d122_2" />(WW格式)周<br/>
		</td>
	   </tr>
		
		<tr class="even">
	    <td align="right">
		精确到日期：
		</td>
	    <td>
		<input class="Wdate" type="text" onClick="WdatePicker()">
		</td>
	   </tr>
	   <tr class="even">
	    <td align="right">
		精确具体时间：
		</td>
	    <td>
		<input class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		</td>
	    </tr>
	    <tr class="even">
	    <td align="right">
		onpicking事件演示:
		</td>
	    <td>
		<input type="text" id="5421"
			onFocus="WdatePicker({onpicking:function(dp){if(!confirm('日期框原来的值为: '+dp.cal.getDateStr()+', 要用新选择的值:' + dp.cal.getNewDateStr() + '覆盖吗?')) return true;}})"
			class="Wdate" />
		</td>
	    </tr>

       <tr class="even">
	    <td align="right">
		使用onpicked实现日期选择联动:
		</td>
	    <td>
		<input id="d5221" class="Wdate" type="text"
			onFocus="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.focus();},maxDate:'#F{$dp.$D(\'d5222\')}'})" />
		至
		<input id="d5222" class="Wdate" type="text"
			onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})" />
		</td>
	    </tr>
		
		
		<tr class="even">
	    <td align="right">
		 以星期一作为第一天
		 </td>
	    <td>
		<input class="Wdate" type="text" id="d17" onfocus="WdatePicker({firstDayOfWeek:1})"/>
        </td>
	    </tr>
        
        <tr class="even">
	    <td align="right">
		通过position属性,自定义弹出位置position:{left:100,top:50}
		</td>
	    <td>
		<input class="Wdate" type="text" id="d16" onfocus="WdatePicker({position:{left:100,top:50}})"/>
        </td>
	    </tr>
        
        <tr class="even">
	    <td align="right">
        禁用清空功能
        </td>
	    <td>
        <input class="Wdate" type="text" id="d15" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/><br/>
        </td>
	    </tr>
        
        <tr class="even">
	    <td align="right">
        高亮每周 周一 周五
        </td>
	    <td>
		<input id="d471" type="text" class="Wdate" onFocus="WdatePicker({specialDays:[1,5]})"/> 
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		只启用 每个月份的 5日 15日 25日
		</td>
	    <td>
		<input id="d46" type="text" class="Wdate" onFocus="WdatePicker({opposite:true,disabledDates:['5$']})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		限制日期的范围是 2006-09-10到2008-12-20
		</td>
	    <td>
		<input id="d411" class="Wdate" type="text" onfocus="WdatePicker({skin:'whyGreen',minDate:'2006-09-10',maxDate:'2008-12-20'})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		限制日期的范围是 20011-3-28 11:30:00 到 2011-4-10 20:59:30
		</td>
	    <td>
		<input type="text" class="Wdate" id="d412" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'2011-03-28 11:30:00',maxDate:'2011-04-10 20:59:30'})" value="20011-03-29 11:00:00"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		限制日期的范围是 2008年2月 到 2008年10月
		</td>
	    <td>
		<input type="text" class="Wdate" id="d413" onfocus="WdatePicker({dateFmt:'yyyy年M月',minDate:'2008-2',maxDate:'2008-10'})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		 限制日期的范围是 8:00:00 到 11:30:00
		 </td>
	    <td>
		<input type="text" class="Wdate" id="d414" onfocus="WdatePicker({dateFmt:'H:mm:ss',minDate:'8:00:00',maxDate:'11:30:00'})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		只能选择今天以前的日期(包括今天)
		</td>
	    <td>
		<input id="d421" class="Wdate" type="text" onfocus="WdatePicker({skin:'whyGreen',maxDate:'%y-%M-%d'})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		只能选择今天以后的日期(不包括今天
		</td>
	    <td>
		<input id="d422" class="Wdate" type="text" onfocus="WdatePicker({minDate:'%y-%M-{%d+1}'})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		只能选择本月的日期1号至本月最后一天
		</td>
	    <td>
		<input id="d423" class="Wdate" type="text" onfocus="WdatePicker({minDate:'%y-%M-01',maxDate:'%y-%M-%ld'})"/>
		</td>
	    </tr>
		
		<tr class="even">
	    <td align="right">
		前面的日期不能大于后面的日期且两个日期都不能大于 2020-10-01
		</td>
	    <td>
		合同有效期从<input id="d4311" class="Wdate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/> 
         到<input id="d4312" class="Wdate" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
        </td>
	    </tr>
        
        <tr class="even">
	    <td align="right">
        禁用 周六 周日 所对应的日期
        </td>
	    <td>
        <input id="d442" type="text" class="Wdate" onFocus="WdatePicker({disabledDays:[0,6]})"/>
        </td>
	    </tr>
		</table>
	</body>
</html>