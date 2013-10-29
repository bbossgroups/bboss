<%
/**
 * 简单的右键菜单测试树
 */
 %>

 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%
String treetype = request.getParameter("treetype");
String id = treetype+"root";
%>
		<div id="treebody" class="shadow">
				<div class="info">
					<p>
		        <!-- 
		        	通过一整套的树标签，生成树
		        	tree 属性指定树的唯一名称
		        	imageFolder 指定树节点的图标目录
		        	collapse 指定树节点是否全部铺开，并且不能折叠，true可以折叠，false全部展开但是不能折叠
		        	includeRootNode 是否包含根节点
		        	href 节点全局url地址
		        	target 节点url弹出窗口
		        	mode 控制树的展示模式，为动静模式
		         -->
		         <tree:tree tree="TreeWithContextMenu"
		    	           node="TreeWithContextMenu.node"
		    	           imageFolder="/jsp/tree/tree_images"
		    	           collapse="true"
		    			   includeRootNode="true"			   
		    			   href="/jsp/tree/testtreenode.jsp"    			   
		    			   target="_blank"
		    			   mode="static-dynamic"
		    			   jquery="true"
		    			   > 
		    			   <!--
		    			   		树的展开和折叠时  保持页面的参数
		    			   -->               
		                  <tree:param name="treetype"/>
		                   <tree:radio name="test_radio"/>
		                   <!-- 指定树的数据加载器和根节点信息
		                   		treetype-数据加载器的实现类，这里是test.tree.TestTree
		                   		scope 数据加载器对象的存储范围，一般是request级别
		                   		
		                   		指定根节点的信息：
		                   		rootid 根节点的id
		                   		rootName 根节点的名称
		                   		
		                   		expandLevel 默认展开多少级
		                   		enablecontextmenu 是否启用右键菜单，true启用，false不启用
		                    -->
		                    
		                    
		    			   <tree:treedata treetype="org.frameworkset.web.tree.TreeWithContextMenu1"
		    	                   scope="request"
		    	                   rootid="<%=id %>"  
		    	                   rootName="测试树"
		    	                   expandLevel="2"
		    	                   showRootHref="true"
		    	                   needObserver="false"
		    	                   refreshNode="false"
		    	                   enablecontextmenu="true" 
		    	                   />
		
		    	</tree:tree>
		        	</p>
				</div>
			</div>
