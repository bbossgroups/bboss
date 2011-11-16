<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>download by http://www.codefans.net</title>
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
    <link href="App_Files/CSS/default.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        #tb .td{
            text-align: center;
            font-weight: bold;
            background-color: #6699FF;
            color: #FFFFFF;
            border:1px solid #000;
        }
    </style>
    <script src="<%=request.getContextPath() %>/include/jquery-1.4.2.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        
        
        function dosubmit()
        {
        	
        	 $("#form1").submit();
        }
    </script>
</head>
<body>
    <form id="form1" action="update.htm" method="post">
    <div>
        <table cellspacing="0" cellpadding="0" id="tb" class="genericTbl">
            <thead>
            <tr>
                <td style="width:25px;"></td>
                <th class="order1 sorted">
                    <input id="CKA" name="CKA" type="checkbox"/></th>
                
                <th class="order1 sorted">
                    字段名</th>
                <th class="order1 sorted">
                    名称</th>
                <th  class="order1 sorted">
                    数据类型</th>
                <th class="order1 sorted">
                    主键</th>
                <th class="order1 sorted">
                    必填</th>
                <th class="order1 sorted">
                    长度</th>
                <th class="order1 sorted">
                    有效</th>
                <th class="order1 sorted">
                    排序</th>
            </tr>
            </thead>
            <pg:beaninfo requestKey="datas">
            <tr class="odd">
                <td style="text-align: center"></td>
                <td class="leftMostIcon">
                    <input id="CK" type="checkbox" name="CK"/>
                 
                    <input id="id" type="text" name="id" value="<pg:cell colName="id" defaultValue=""/>"/></td>
                <td style="text-align: center">
                    <input id="fieldName" type="text" name="fieldName" value="<pg:cell colName="fieldName" defaultValue=""/>"/></td>
                <td style="text-align: center">
                    <input id="fieldLable" type="text" name="fieldLable" value="<pg:cell colName="fieldLable" defaultValue=""/>"/></td>
                <td style="text-align: center">
                    <select id="fieldType" name="fieldType" style=" width:100px;">
                        <option value="1" 
                        <pg:equal colName="fieldType" value="1">
                        	selected
                        </pg:equal>
                        >1</option>
                        <option value="2" 
                        <pg:equal colName="fieldType" value="2">
                        	selected
                        </pg:equal>
                        >2</option>
                    </select></td>
                <td style="text-align: center">
                    <input id="isprimaryKey" type="checkbox" name="isprimaryKey" <pg:equal colName="isprimaryKey" value="true">
                        	checked="checked" 
                        </pg:equal> 
                    	value="true"/></td>
                <td style="text-align: center">
                    <input id="required" type="checkbox" name="required" <pg:equal colName="required" value="true">
                        	checked="checked" 
                        </pg:equal>
                    	value="true"/></td>
                <td style="text-align: center">
                    <input id="fieldLength" type="text" size="10" name="fieldLength" 
                    	value="<pg:cell colName="fieldLength" defaultValue="10"/>"/></td>
                <td style="text-align: center">
                    <input id="isvalidated" type="checkbox"  <pg:equal colName="isvalidated" value="1">
                        	checked="checked" 
                        </pg:equal> name="isvalidated" 
                    value="1"/></td>
                <td style="text-align: center">
                    <input id="sortorder" type="text" size="4" name="sortorder" value="<pg:cell colName="sortorder" defaultValue=""/>"/></td>
            </tr>
            </pg:beaninfo>
        </table>
    
    </div>
    <div>
    <ul class="options">
                       <li id="abbreviations">
					<a href="#" onClick="dosubmit()" >提交</a>
				</li>
				</ul>
    </div>
    </form>
</body>
</html>
