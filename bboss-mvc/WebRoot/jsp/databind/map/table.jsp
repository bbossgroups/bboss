<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Map集合po对象绑定实例</title>
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
        $(document).ready(function() {
            //隐藏模板tr
            $("#tb tr").eq(2).hide();
            var i = 0;
            $("#size").click(function() {
　　　　　//复制一行
                var tr = $("#tb tr").eq(2).clone();
                tr.find("td").get(0).innerHTML = ++i;
                tr.show();
                tr.appendTo("#tb");
            });
            $("#abbreviations").click(function() {
                $("#tb tr:gt(2)").each(function() {
                    if ($(this).find("#CK").get(0).checked == true) {
                        $(this).remove();
                    }
                });
                i = 0;
                $("#tb tr:gt(2)").each(function() {
                    $(this).find("td").get(0).innerHTML = ++i;
                });
                $("#CKA").attr("checked", false);
            });
            $("#CKA").click(function() {
                $("#tb tr:gt(2)").each(function() {
                    $(this).find("#CK").get(0).checked = $("#CKA").get(0).checked;
                });
            });
        })
        
        function dosubmit(action)
        {
        	if(!action)
        	{
	        	 $("#tb tr").eq(2).remove();
	        	 $("#form1").attr("action","mapbean.htm") ;
	        	 $("#form1").submit();
	        }
	        else
	        {
	        	$("#tb tr").eq(2).remove();
	        	$("#form1").attr("action","deletebatch.htm") ;
	        	$("#form1").submit();
	        }
        }
    </script>
</head>
<body>
<div>
<pg:errors><pg:error/></pg:errors>
</div>
    <form id="form1" action="mapbean.htm" method="post">
 
        <table cellspacing="0" cellpadding="0" id="tb" class="genericTbl">
           <thead><tr>
                <td colspan="10" style="text-align:left">
                
					<ul class="options">
						<li id="size">
							<a href="#">添加</a>
						</li>
						<li id="abbreviations">
							<a href="#">删除</a>
						</li>
					</ul>
				</td>
            </tr>
            <tr>
                <td style="width:25px;"></td>
                <td style="width:25px;">
                    <input id="CKA" name="CKA" type="checkbox"/></td>
                
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
            <tr class="odd">
                <td style="text-align: center"></td>
                <td class="leftMostIcon">
                    <input id="CK" type="checkbox" name="CK"/>
                 
                    <input id="id" type="hidden" name="id" /></td>
                <td style="text-align: center">
                    <input id="fieldName" type="text" name="fieldName" /></td>
                <td style="text-align: center">
                    <input id="fieldLable" type="text" name="fieldLable" /></td>
                <td style="text-align: center">
                    <select id="fieldType" name="fieldType" style=" width:100px;">
                        <option>1</option>
                        <option>2</option>
                    </select></td>
              
                <td style="text-align: center">
                <select id="isprimaryKey" name="isprimaryKey" style=" width:100px;">
                        <option value="true" 
                       
                        >是</option>
                        <option value="2" 
                        
                        >否</option>
                    </select>
                    </td>
                <td style="text-align: center">
                <select id="required" name="required" style=" width:100px;">
                        <option value="true" 
                        
                        >是</option>
                        <option value="2" 
                        
                        >否</option>
                    </select>
                    </td>
                <td style="text-align: center">
                    <input id="fieldLength" type="text" size="10" name="fieldLength" /></td>
                <td style="text-align: center">
                <select id="isvalidated" name="isvalidated" style=" width:100px;">
                        <option value="1"                        >是</option>
                        <option value="0" >否</option>
                    </select>
                   </td>
                <td style="text-align: center">
                    <input id="sortorder" type="text" size="4" name="sortorder" /></td>
            </tr>
            <pg:list requestKey="datas">
            <tr class="even">
                <td style="text-align: center"></td>
                <td style="text-align: center">
                    <input id="CK" type="checkbox" name="CK" value="<pg:cell colName="id" defaultValue=""/>"/>
                    <input id="id" type="hidden" name="id" value="<pg:cell colName="id" defaultValue=""/>"/></td>
                <td style="text-align: center">
                   <a href="showbean.htm?id=<pg:cell colName="id" defaultValue=""/>">明细 </a>
                   <a href="delete.htm?id=<pg:cell colName="id" defaultValue=""/>">删除 </a>
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
                <select id="isprimaryKey" name="isprimaryKey" style=" width:100px;">
                        <option value="true" 
                        <pg:equal colName="isprimaryKey" value="true">
                        	selected
                        </pg:equal>
                        >是</option>
                        <option value="2" 
                        <pg:equal colName="isprimaryKey" value="false">
                        	selected
                        </pg:equal>
                        >否</option>
                    </select>
                    </td>
                <td style="text-align: center">
                <select id="required" name="required" style=" width:100px;">
                        <option value="true" 
                        <pg:equal colName="required" value="true">
                        	selected
                        </pg:equal>
                        >是</option>
                        <option value="2" 
                        <pg:equal colName="required" value="false">
                        	selected
                        </pg:equal>
                        >否</option>
                    </select>
                    </td>
                <td style="text-align: center">
                    <input id="fieldLength" type="text" size="10" name="fieldLength" 
                    	value="<pg:cell colName="fieldLength" defaultValue="10"/>"/></td>
                <td style="text-align: center">
                <select id="isvalidated" name="isvalidated" style=" width:100px;">
                        <option value="1" 
                        <pg:equal colName="isvalidated" value="1">
                        	selected
                        </pg:equal>
                        >是</option>
                        <option value="0" 
                        <pg:equal colName="isvalidated" value="0">
                        	selected
                        </pg:equal>
                        >否</option>
                    </select>
                   </td>
                <td style="text-align: center">
                    <input id="sortorder" type="text" size="4" name="sortorder" value="<pg:cell colName="sortorder" defaultValue=""/>"/></td>
            </tr>
            </pg:list>
            
        </table>
        <div>
				<ul class="options">
					<li id="size">
						<a href="#" onClick="dosubmit()">提交</a>
					</li>
					<li id="abbreviations">
						<a href="#" onClick="dosubmit('delete')">批量删除</a>
					</li>
				</ul>
		</div>
        
    
    </form>
</body>
</html>
