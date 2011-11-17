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
			src="../include/My97DatePicker/WdatePicker.js"></script>
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
			 
				$("#queryresult").load("sayHelloEnums.page",{sex:$("#sex").val()});
			}
			
			
		
		</script>		

</head>


<body>
			<h3>
				Hello World number bind Examples.
			</h3>
			<form action="sayHelloNumber.page" method="post">
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
						</td>
						

						
					</tr>
					<tr>
						<td><input type="submit" name="确定" value="确定"></td>						
					</tr>
				</tbody>
			</table>
				
			</form>
	
		
			<h3>
				Hello World String bind Examples.
			</h3>
			<form action="sayHelloString.page" method="post">
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
							<pg:empty requestKey="serverHello">
								没有名字，不问候。
							</pg:empty>
							<pg:notempty requestKey="serverHello">
								<common:request name="serverHello"/>
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
				Hello World Time bind Examples.
			</h3>
			<form action="sayHelloTime.page" method="post">
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
				        onclick="WdatePicker({el:'d12'})" src="../include/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
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
				Hello World Time Array bind Examples.
			</h3>
			<form action="sayHelloTimes.page" method="post">
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
				        onclick="WdatePicker({el:'d12s'})" src="../include/My97DatePicker/skin/datePicker.gif" width="16" height="22" align="absmiddle"/>
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
				Hello World Bean bind Examples.
			</h3>
			<form action="sayHelloBean.page" method="post">
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
				Hello World List<PO> bind  Examples.
			</h3>
			<form action="sayHelloBeanList.page" method="post">
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
							<pg:list requestKey="serverHelloListBean" >
								<pg:cell colName="name"/>
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
				Hello World Map<String,PO> bind  Examples.
			</h3>
			<form action="sayHelloBeanMap.page" method="post">
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
							<pg:beaninfo requestKey="serverHelloMapBean" >
								<pg:cell colName="name"/>
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
				Hello World Array bind  Examples.
			</h3>
			<form action="sayHelloArray.page" method="post">
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
				Hello World Enum bind  Examples.
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