<%--

 *  Copyright 2008-2011 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.

 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>

<%@ taglib uri="/WEB-INF/commontag.tld" prefix="common"%>	
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<head>
	<title>bboss-mvc - hello world,data bind!</title>
	<script type="text/javascript"
			src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/WdatePicker.js"></script>
	<pg:config enablecontextmenu="false"/>
		<script type="text/javascript">
		
			function doquery(){
			 
			 //  var value = $("#form1:checkbox").fieldValue();
			 //  alert(value.length);
			 
				$("#queryresult").load("sayHelloEnum.page",{sex:$("#sex").val()});
			}
			
			function domutiquery(){
			 
			 //  var value = $("#form1:checkbox").fieldValue();
			 //  alert(value.length);
			 
				$("#queryresult").load("sayHelloEnums.page",{sex:$("#sex").val(),<pg:dtoken element="json"/>});
			}
			
			
		
		</script>		

</head>


<body>
			<h3>
				Hello World number bind Example.
			</h3>
			<form action="sayHelloNumber.page" method="post">
			<pg:dtoken/>
			
			<table cellspacing="0" >
				<tbody>
					<tr><td>
							请输入您的幸运数字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<common:request name="serverHelloNumber"/>
							<pg:error colName="name"/>
							aaa:<pg:cell actual="${param.name}"/>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			<h3>
				Hello World String  bind Example.
			</h3>
			<form action="sayHelloString.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
						
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:empty requestKey="sayHelloString">
								没有名字，不问候。
							</pg:empty>
							<pg:notempty requestKey="sayHelloString">
								<common:request name="sayHelloString"/>
							</pg:notempty>
						</td>
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
		
			<h3>
				Hello World String with name variable bind Example.
			</h3>
			<form action="sayHelloStringVar.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
						<input name="id" value="0" type="hidden"/>
							请输入您的名字：
						<input name="name0" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:empty requestKey="sayHelloStringVar">
								没有名字，不问候。
							</pg:empty>
							<pg:notempty requestKey="sayHelloStringVar">
								<common:request name="sayHelloStringVar"/>
							</pg:notempty>
						</td>
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World Time List bean bind Example.
			</h3>
			<form action="dataListBeanBind.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td colspan="2">
							请输入您的幸运日期：<input name="id" value="1" type="text">
						
						</td>
					</tr>
					<tr >
	   
					    <td align="right">					   
					     <input id="d12s" name="d12s" type="text"
				        onclick="WdatePicker()" src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
					    </td>
					    <td>
					   <input id="d13s" name="d13s" type="text"
				        onclick="WdatePicker()" src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
					   </td>
					   </tr>
					   
					   <tr>
						
						

						<td colspan="2">
							请输入您的幸运日期：<input name="id" value="2" type="text">
						
						</td>
					</tr>
					<tr >
	   
					    <td align="right">			
					     <input id="d12s" name="d12s" type="text"
				        onclick="WdatePicker()" src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>		   
					    </td>
					    <td>
					   <input id="d13s" name="d13s" type="text"
				        onclick="WdatePicker()" src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
					   </td>
					   </tr>
					   <tr>
						<td colspan="2">
							来自服务器的日期问候：
							<pg:list actual="${dataListBeanBind}">
								<pg:cell colName="id"/>
								<pg:cell colName="d12" dateformat="yyyy-MM-dd"/>
								<pg:cell colName="d14" dateformat="yyyy-MM-dd"/>
								<pg:cell colName="d13s" dateformat="yyyy-MM-dd"/>
							</pg:list>
							
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
			    </tbody>
			</table>
			<h3>
				Hello World Time bind Example.
			</h3>
			<form action="sayHelloTime.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td colspan="2">
							请输入您的幸运日期：
						
						</td>
					</tr>
					<tr >
	   
					    <td align="right">
					   普通日期：
					    </td>
					    <td>
					   <input id="d12" name="d12" type="text"
				        onclick="WdatePicker()" src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
					   </td>
					   </tr>
						
						<tr >
					    <td align="right">
						sql日期：
						</td>
					    <td>
						<input class="Wdate" type="text" name="stringdate" onClick="WdatePicker()">
						</td>
					   </tr>
					   <tr >
					    <td align="right">
						timestamp精确具体时间：
						</td>
					    <td>
						<input class="Wdate" type="text" name="stringdatetimestamp" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH/mm/ss'})">
						</td>
					    </tr>
					
					<tr>
						<td colspan="2">
							来自服务器的日期问候：
							<common:request name="utilDate" dateformat="yyyy-MM-dd"/>
							
							<common:request name="sqlDate" dateformat="yyyy-MM-dd"/>
							
							<common:request name="sqlTimestamp" dateformat="yyyy-MM-dd HH/mm/ss"/>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World Time Array bind Example.
			</h3>
			<form action="sayHelloTimes.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td colspan="2">
							请输入您的幸运日期：
						
						</td>
					</tr>
					<tr >
	   
					    <td align="right">
					   普通日期：
					    </td>
					    <td>
					   <input id="d12s" name="d12s" type="text"
				        onclick="WdatePicker()" src="<%=request.getContextPath() %>/jsp/datepicker/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
					   </td>
					   </tr>
						
						<tr >
					    <td align="right">
						sql日期：
						</td>
					    <td>
						<input class="Wdate" type="text" name="stringdates" onClick="WdatePicker()">
						</td>
					   </tr>
					   <tr >
					    <td align="right">
						timestamp精确具体时间：
						</td>
					    <td>
						<input class="Wdate" type="text" name="stringdatetimestamps" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH/mm/ss'})">
						</td>
					    </tr>
					
					<tr>
						<td colspan="2">
							来自服务器的日期问候：
							<common:request name="utilDates" dateformat="yyyy-MM-dd"/>
							
							<common:request name="sqlDates" dateformat="yyyy-MM-dd"/>
							
							<common:request name="sqlTimestamps" dateformat="yyyy-MM-dd HH/mm/ss"/>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World Bean bind Example.
			</h3>
			<form action="sayHelloBean.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
						<input name="id" type="hidden" value="0">
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate0" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate0" type="radio" value="1"> 篮球
						<input name="favovate0" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:null actual="${serverHelloBean}">
								没有名字，不问候。
							</pg:null>
							<pg:notnull actual="${serverHelloBean}">
								<pg:beaninfo requestKey="serverHelloBean" >
									姓名：<pg:cell colName="name"/>
									爱好：<pg:equal colName="favovate" value="0">乒乓球</pg:equal>
									<pg:equal colName="favovate" value="1">篮球</pg:equal>
									<pg:equal colName="favovate" value="2">排球</pg:equal>
								</pg:beaninfo>
							</pg:notnull>
							
						</td>
						

						
					</tr>
					
					
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World EditorBean bind Example.
			</h3>
			<form action="sayHelloEditorBean.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:null actual="${serverHelloBean}">
								没有名字，不问候。
							</pg:null>
							<pg:notnull actual="${serverHelloBean}">
								<common:request name="serverHelloBean" property="name"/>
							</pg:notnull>
							
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World EditorBeans bind Example.
			</h3>
			<form action="sayHelloEditorBeans.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:null actual="${serverHelloBean}">
								没有名字，不问候。
							</pg:null>
							<pg:notnull actual="${serverHelloBean}">
								<common:request name="serverHelloBean"/>
							</pg:notnull>
							
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World Editor bind Example.
			</h3>
			<form action="sayHelloEditor.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:null actual="${serverHelloBean}">
								没有名字，不问候。
							</pg:null>
							<pg:notnull actual="${serverHelloBean}">
								<common:request name="serverHelloBean"/>
							</pg:notnull>
							
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World ListStringArrayEditor bind Example.
			</h3>
			<form action="sayHelloEditors.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:null actual="${serverHelloBean}">
								没有名字，不问候。
							</pg:null>
							<pg:notnull actual="${serverHelloBean}">
								<common:request name="serverHelloBean"/>
							</pg:notnull>
							
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World ListEditor bind Example.
			</h3>
			<form action="sayHelloListEditor.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						
					</tr>
					
					<tr>
						<td>
							来自服务器的问候：
							<pg:null actual="${serverHelloBean}">
								没有名字，不问候。
							</pg:null>
							<pg:notnull actual="${serverHelloBean}">
								<common:request name="serverHelloBean"/>
							</pg:notnull>
							
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World List PO bind  Example.
			</h3>
			<form action="sayHelloBeanList.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
						<input name="id" type="hidden" value="0">
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate0" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate0" type="radio" value="1"> 篮球
						<input name="favovate0" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					
					<tr>
						
						

						<td>
						<input name="id" type="hidden" value="1">
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate1" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate1" type="radio" value="1"> 篮球
						<input name="favovate1" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						
						

						<td>
							请输入您的名字：
							<input name="id" type="hidden" value="2">
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate2" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate2" type="radio" value="1"> 篮球
						<input name="favovate2" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						
						

						<td>
							请输入您的名字：
							<input name="id" type="hidden" value="3">
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate3" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate3" type="radio" value="1"> 篮球
						<input name="favovate3" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						<td>
							
							<pg:list requestKey="serverHelloListBean" >
							    姓名：<pg:cell colName="name"/>
									爱好：<pg:equal colName="favovate" value="0">乒乓球</pg:equal>
									<pg:equal colName="favovate" value="1">篮球</pg:equal>
									<pg:equal colName="favovate" value="2">排球</pg:equal>
							</pg:list>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World List String bind  Example.
			</h3>
			<form action="sayHelloStringList.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:list requestKey="sayHelloStringList" >
							    <pg:cell />
								<pg:equal expression="{rowid} + {offset}" expressionValue="{rowcount}-1">总记录最后一行</pg:equal>
								
								<pg:equal expression="{rowid}" expressionValue="{pagesize}-1">当页数据最后一行</pg:equal>
								页面记录数：<pg:pagesize/>
							</pg:list>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World List int bind  Example.
			</h3>
			<form action="sayHelloIntList.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的数字问候：
							<pg:list requestKey="sayHelloIntList" >
							    <pg:cell />
								<pg:equal expression="{rowid} + {offset}" expressionValue="{rowcount}-1">总记录最后一行</pg:equal>
								
								<pg:equal expression="{rowid}" expressionValue="{pagesize}-1">当页数据最后一行</pg:equal>
								页面记录数：<pg:pagesize/>
							</pg:list>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World List int bind  Example with param mapping.
			</h3>
			<form action="sayHelloIntListWithNameMapping.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的数字：
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的数字问候：
							<pg:list requestKey="sayHelloIntListWithNameMapping" >
							    <pg:cell />
								<pg:equal expression="{rowid} + {offset}" expressionValue="{rowcount}-1">总记录最后一行</pg:equal>
								
								<pg:equal expression="{rowid}" expressionValue="{pagesize}-1">当页数据最后一行</pg:equal>
								页面记录数：<pg:pagesize/>
							</pg:list>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World List enum bind  Example .
			</h3>
			<form action="sayHelloEnumList.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的性别：
						<select name="sex" id="sex">
							<option value="F">F</option>
							<option value="M">M</option>
							<option value="UN">UN</option>
							</select>
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的性别：
						<select name="sex" id="sex">
							<option value="F">F</option>
							<option value="M">M</option>
							<option value="UN">UN</option>
							</select>
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的性别：
						<select name="sex" id="sex">
							<option value="F">F</option>
							<option value="M">M</option>
							<option value="UN">UN</option>
							</select>
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的性别：
						<select name="sex" id="sex">
							<option value="F">F</option>
							<option value="M">M</option>
							<option value="UN">UN</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的性别问候：
							<pg:list requestKey="sayHelloEnumList" >
							    <pg:cell />
								<pg:equal expression="{rowid} + {offset}" expressionValue="{rowcount}-1">总记录最后一行</pg:equal>
								
								<pg:equal expression="{rowid}" expressionValue="{pagesize}-1">当页数据最后一行</pg:equal>
								页面记录数：<pg:pagesize/>
							</pg:list>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			<h3>
				Hello World Map String,PO bind  Example.
			</h3>
			<form action="sayHelloBeanMap.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
				
						
					
					
					<tr>
						
						

						<td>
						<input name="id" type="hidden" value="0">
							请输入您的名字：
						<input name="name" type="text">
							请输入您的性别：
						<input name="sex" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate0" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate0" type="radio" value="1"> 篮球
						<input name="favovate0" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					
					<tr>
						
						

						<td>
						<input name="id" type="hidden" value="1">
							请输入您的名字：
						<input name="name" type="text">
						请输入您的性别：
						<input name="sex" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate1" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate1" type="radio" value="1"> 篮球
						<input name="favovate1" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						
						

						<td>
							请输入您的名字：
							<input name="id" type="hidden" value="2">
						<input name="name" type="text">	请输入您的性别：
						<input name="sex" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate2" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate2" type="radio" value="1"> 篮球
						<input name="favovate2" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						
						

						<td>
							请输入您的名字：
							<input name="id" type="hidden" value="3">
						<input name="name" type="text">	请输入您的性别：
						<input name="sex" type="text">
						</td>
					</tr>
					<tr>
						
						

						<td>
						
							喜好：
						<input name="favovate3" type="radio" checked="true"  value="0"> 乒乓球
						<input name="favovate3" type="radio" value="1"> 篮球
						<input name="favovate3" type="radio" value="2"> 排球
						</td>
						
						
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:map requestKey="sayHelloBeanMap" >
									<ul>
							       <li> mapkey: <pg:mapkey/></li>
									 <li>name属性值：<pg:cell colName="name"/></li>
									 <li>sex属性值：<pg:cell colName="sex"/></li>
									 
									
									<li>爱好：<pg:equal colName="favovate" value="0">乒乓球</pg:equal>
									<pg:equal colName="favovate" value="1">篮球</pg:equal>
									<pg:equal colName="favovate" value="2">排球</pg:equal>
									</li>
									</ul>
							</pg:map>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World Bean with list property bind  Example.
			</h3>
			<form action="listExampleBean.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
				
						
						<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						<td>
							请输入您的age：
						<input name="age" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						<td>
							请输入您的age：
						<input name="age" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						
						<td>
							请输入您的age：
						<input name="age" type="text">
						</td>
					</tr>
					
					<tr>
						
						

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						<td>
							请输入您的age：
						<input name="age" type="text">
						</td>
					</tr>

						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						<td>
							请输入您的age：
						<input name="age" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:beaninfo requestKey="listExampleBean" >
									<ul>							      
									 <pg:list colName="name"><li>name属性值：<pg:cell/></li></pg:list>
									  <pg:list colName="names"><li>names属性值：<pg:cell/></li></pg:list>
									   <pg:list colName="age"><li>age属性值：<pg:cell/></li></pg:list>
									   <pg:list colName="ages"><li>ages属性值：<pg:cell/></li></pg:list>
									</ul>
							</pg:beaninfo>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World Map String bind  Example.
			</h3>
			<form action="sayHelloStringMap.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
				
						
						
					<tr>
						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						<td>
							请输入您的性别：
						<input name="sex" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:map requestKey="sayHelloStringMap" >
									<ul>
							       <li> mapkey: <pg:mapkey/></li>
									 <li>value属性值：<pg:cell/></li>
									
									</ul>
							</pg:map>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="sub" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World Map String with pattern bind  Example.
			</h3>
			<form action="sayHelloStringMapWithFilter.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
				
						
						
					<tr>
						<td>
							请输入您的名字：
						<input name="name" type="text">
						</td>
						<td>
							请输入您的性别：
						<input name="sex" type="text">
						</td>
						
						<td>
							请输入您的模式名：
						<input name="pre.cc.name" type="text">
						</td>
						
						
						<td>
							请输入您的模式性别：
						<input name="pre.cc.sex" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:map requestKey="sayHelloStringMapWithFilter" >
									<ul>
							       <li> mapkey: <pg:mapkey/></li>
									 <li>value属性值：<pg:cell/></li>
									
									</ul>
							</pg:map>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="sub" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			<h3>
				Hello World Array bind  Example.
			</h3>
			<form action="sayHelloArray.page" method="post">
			<pg:dtoken/>
			<table cellspacing="0" >
				<tbody>
					<tr>
						
						

						<td>
							请输入您的名字两次（一定要两次哦）：
						<input name="name" type="text">
						<input name="name" type="text">
						</td>
					</tr>
					<tr>
						<td>
							来自服务器的问候：
							<pg:list requestKey="serverHelloArray" >
								<pg:rowid increament="1"/> <pg:cell />
							</pg:list>
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
			
			
			<h3>
				Hello World Enum bind  Example.
			</h3>
			
			
			
			<table >
				
				<!--分页显示开始,分页标签初始化-->
				
					<tr >
						<th align="center">
							NAME:<select id="sex">
							<option value="F">F</option>
							<option value="M">M</option>
							<option value="UN">UN</option>
							</select>
						</th>
						 
						<th>
							<input type="button" value="性别查询" onclick="doquery()"/>
							<input type="button" value="多性别查询" onclick="domutiquery()"/>
						</th>
						
						 
					</tr>
					
					<tr >
						<td align="center">
							结果
						</td>
						 
						<td id="queryresult"></td>
						
						 
					</tr>
			</table>	
				
			

</body>