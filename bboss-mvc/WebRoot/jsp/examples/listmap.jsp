<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>listmap复杂标签演示示例</title>

</head>
<body>
	<div data-role="page">
	
		
		<form>
			<div class="detail">
				<div class="tab">
				1.直接通过list标签输出nameList中的所有key
					<ul id="tabul">
						<pg:list requestKey="nameList">
							<li class="current"><a href="javascript:void(0);"><pg:cell /></a></li>
						</pg:list>
					</ul>
					<div class="tabdiv">
						<span></span>
					</div>
				</div>
				<div class="pannle">
				2.根据list中key的顺序有序遍历并输出map中list中保持的bean的属性值
				<ol>
					<pg:list requestKey="nameList">
						
							<pg:equal value="handlerModel"><!-- 经办列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>												
											</ul>
									</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="applyUnionModel"><!-- 关联申请 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
									</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="billLoanModel"><!-- 借款列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
										</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="loanPayModel"><!-- 冲账列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
										</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="budgetModel"><!-- 预算列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
										</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="outgoModel"><!-- 分摊列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
										</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="billItemModel"><!-- 收款列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
										</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="billAttachment"><!-- 附件列表 -->
								<pg:map requestKey="billDataMap" keycell="true">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
										</pg:list></li>
								</pg:map>
							</pg:equal>
							<pg:equal value="billSapModel"><!-- SAP列表信息 演示key属性，通过key属性直接获取map中的list值-->
								<pg:map requestKey="billDataMap" key="billSapModel">
									<li class="pannelol"><span></span> 
									<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
											<ul>
												<li><label>名称：</label><pg:cell colName="name" /></li>
												<li><label>性别：</label><pg:cell colName="sex" /></li>	
											</ul>
									</pg:list></li>
								</pg:map>
								
								
								
							</pg:equal>
						
					</pg:list>
					</ol>
				</div>
				<div class="pannle">
				3.直接遍历并输出map中list中保持的bean的属性值
				<ol>
					<pg:map requestKey="billDataMap">
						<li class="pannelol"><span></span> 
						<pg:list><!-- 直接遍历输出map中存储的list,同理也可以直接遍历输出map中存储的map和数组 -->
								<ul>
									<li><label>名称：</label><pg:cell colName="name" /></li>
									<li><label>性别：</label><pg:cell colName="sex" /></li>	
								</ul>
						</pg:list></li>
					</pg:map>
				</ol>
				</div>
			</div>
		</form>
	</div>
</body>
</html>