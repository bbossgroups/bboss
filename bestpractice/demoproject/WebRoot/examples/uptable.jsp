<%@ page language="java" import="java.util.*,java.io.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<table class="genericTbl">
				<tr >
						<!--设置分页表头-->

					<th  style="width:50%" class="order1 sorted">请选择需上传的文件：					</th>
					<th  style="width:25%" class="order1 sorted">
					<input type="radio" name="overide" checked id="overide" >覆盖存在文件
					<input type="button" name="button" onclick="uploadFile()" value="上传"> 					
					
					</th>
					<th colspan="100"><input type="text" name="dir" id="dir" >
					<input type="button" name="button" onclick="mkdir()" value="创建目录"></th>
					
					
				</tr>
				<tr>
				<td colspan="100">
					<input name="test" type="file" class="multi" maxlength="2000" accept="*.doc,*.zip,*.rar,*.ppt,*.pptx,*.doc,*.docx,*.pdf,*.flv,*.jpg,*.swf"/>
				</td>
				</tr>
			</table>