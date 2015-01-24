<%@ page language="java" import="java.util.*,java.io.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


     <table class="genericTbl">
       <tr>
       <th class="order1 sorted">
            文件名
       </th>
       <th class="order1 sorted">
          文件类型
       </th>
       <th class="order1 sorted">
           文件大小
       </th>
       <th class="order1 sorted">
           最新修改日期
       </th>
       <th class="order1 sorted">
             下载
       </th>
       </tr>
       <pg:list requestKey="files">
       <tr class="even">
         <td >
            <pg:equal actual="${filetype }" value="file">      
            <a href="<%=request.getContextPath() %>/file/download.htm?fileName=<pg:cell colName="fileName"/>"><pg:cell colName="fileName"/></a>
            </pg:equal>
            <pg:equal actual="${filetype }" value="vidio">      
            <a href="<%=request.getContextPath() %>/vidio/download.htm?fileName=<pg:cell colName="fileName"/>"><pg:cell colName="fileName"/></a>
            </pg:equal>  
            <pg:equal actual="${filetype }" value="tool">      
            <a href="<%=request.getContextPath() %>/tool/download.htm?fileName=<pg:cell colName="fileName"/>"><pg:cell colName="fileName"/></a>
            </pg:equal>
         </td>
         <td >
            <pg:cell colName="fileType"></pg:cell>
         </td>
         <td >
            <pg:cell colName="fileSize"></pg:cell>&nbsp;byte
         </td>
         <td>
		    <pg:cell colName="lastModified"></pg:cell>
         </td>
         <td >
          <pg:equal actual="${filetype }" value="file">     
            <a href="<%=request.getContextPath() %>/file/download.htm?fileName=<pg:cell  colName="fileName"/>">下载此文件</a>
           </pg:equal>
           <pg:equal actual="${filetype }" value="vidio">     
            <a href="<%=request.getContextPath() %>/vidio/download.htm?fileName=<pg:cell  colName="fileName"/>">下载此文件</a>
           </pg:equal>
           <pg:equal actual="${filetype }" value="tool">     
            <a href="<%=request.getContextPath() %>/tool/download.htm?fileName=<pg:cell  colName="fileName"/>">下载此文件</a>
           </pg:equal>
         </td>
         
        </tr>
        </pg:list>
     </table>
  
  
