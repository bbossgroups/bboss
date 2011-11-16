<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Flexigrid</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/include/flexigrid/css/flexigrid.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/flexigrid/flexigrid.js"></script>
<style>

	body
		{
		font-family: Arial, Helvetica, sans-serif;
		font-size: 12px;
		}
		
	.flexigrid div.fbutton .add
		{
			background: url(${pageContext.request.contextPath}/include/flexigrid/css/images/add.png"/>) no-repeat center left;
		}	

	.flexigrid div.fbutton .delete
		{    
			background: url(${pageContext.request.contextPath}/include/flexigrid/css/images/close.png"/>) no-repeat center left;
		}	

		
</style>
</head>

<body>

<b>&gt;This is a sample implementation attached to a form, to add additional parameters</b>

<

<table id="flex1" style="display:none"></table>

<script type="text/javascript">

			$("#flex1").flexigrid
			(
			{
			url: '${pageContext.request.contextPath}/json/ajax.html',
			dataType: 'json',
			colModel : [
				{display: 'id', name : 'id', width : 40, sortable : true, align: 'center'},
				{display: 'Name', name : 'name', width : 180, sortable : true, align: 'left'},
				{display: 'Title', name : 'title', width : 120, sortable : true, align: 'left'}
				
				],
			searchitems : [
				{display: 'Name', name : 'name', isdefault: true},
				{display: 'Title', name : 'title'}
				],
			sortname: "name",
			sortorder: "asc",
			usepager: true,
			title: '人员',
			useRp: true,
			pagestat: '显示 {from} to {to} of {total} items',
			rp: 15,
			showTableToggleBtn: true,
			width: 700,
			onSubmit: false,
			height: 200
			}
			);

</script>

</body>
</html>
