<script language="javascript">    
	var selectNode;
</script>
		<script language="javascript" src="/creatorcms/include/prototype-1.4.0.js"></script>
		<a id="tree_node_bridge"></a>
		<a id="tree_node_localbridge"></a>
		<iframe height='0' width='0' name='tree_content_bridge' FRAMEBORDER='no' border=0></iframe>
		<script language="javascript"> 
	function doClickTreeNode(linkUrl,selected,target,oldselected,eventLink,nodeid) {      
		if(oldselected && document.all.item(oldselected))         
			document.all.item(oldselected).className = "unselectedTextAnchor";     
		if(selectNode && document.all.item(selectNode))         
			document.all.item(selectNode).className = "unselectedTextAnchor";      
		selectNode = selected; 
		if(document.all.item(selectNode))      
			document.all.item(selectNode).className = "selectedTextAnchor";      
		tree_node_bridge.href=linkUrl; 
		tree_node_bridge.target=target; 
		tree_node_bridge.click(); 
		if(eventLink)
		{ 
			doClickImageIcon(eventLink,nodeid);
		} 
	}  
	function doClickImageIcon(linkUrl,eventNode)
	{
		if(eventNode)
		{
			var firsted = $("icon_" + eventNode).firsted; 
			if(firsted == "true")
			{
				nodetoggle(eventNode); 
			}
			else 
			{ 
				if(selectNode) 
				{
					linkUrl += "&selectedNode=" + selectNode; 
				}
				var indent = $("icon_" + eventNode).indent;
				linkUrl += "&node_parent_indent=" + indent;
				document.all.item("icon_" + eventNode).firsted = "true"; 
				getSonOfNode(linkUrl,eventNode); 
			} 
		}
		 else  
		 {
		 	if(selectNode)
		 	{
		 		linkUrl += "&selectedNode=" + selectNode; 
		 	}
		 	tree_node_localbridge.href=linkUrl;  
		 	tree_node_localbridge.target=""; 
		 	tree_node_localbridge.click();
		 }
	}
		 function nodetoggle(eventNode) 
		 {
		 	if(Element.visible("div_parent_" + eventNode)) 
		 	{
		 		$("icon0_" + eventNode).src = $("icon_" + eventNode).collapsedimg;
		 		$("icon1_" + eventNode).src = $("icon_" + eventNode).closedimg; 
		 	}
			else  
		 	{
		 		$("icon0_" + eventNode).src = $("icon_" + eventNode).expandedimg; 
		 		$("icon1_" + eventNode).src = $("icon_" + eventNode).openedimg; 
		 	}
		 	Element.toggle("div_parent_" + eventNode); 
		 } 
		 function getSonOfNode(linkUrl,eventNode) 
		 {  
		 	$("tree_content_bridge").src = linkUrl; 
		 } 
		 
		 function setSon(father,sons,sonids)
		 {
		 	new Insertion.After("div_" + father,sons); 
		 	nodetoggle(father); 
		 	var checkNode = document.all.item("checkbox_" + father);
		 	if(!checkNode)     
		 	return;   
		 	var recursive = checkNode.recursive;
		 	if(recursive && recursive == "true")
		 	{   
		 		checkNode.sonids = sonids;   
		 		checkSonCheckbox(checkNode,sonids);
		 	}
		 }
		 function treeNodeCheckboxChange(checkNode)
		 {
		 	if(!checkNode)     
		 		checkNode = event.srcElement;
		 	var sonids = checkNode.sonids;
		 	var recursive = checkNode.recursive;
		 	if(recursive && recursive == "true")
		 		checkSonCheckbox(checkNode,sonids);
		 }
		 function checkSonCheckbox(checkNode,sonids)
		 { 
		 	if(sonids && sonids != "") 
		 	{   var sons = sonids.split("##");   
		 		if(sons.length)  	
			 		for(var i = 0; i < sons.length; i ++)  	
			 		{      	
			 			$("checkbox_" + sons[i]).checked = checkNode.checked;      	
			 			checkSonCheckbox($("checkbox_" + sons[i]),$("checkbox_" + sons[i]).sonids);
			 	  	}	  
			 	else   
			 	{  		
			 		$("checkbox_" + sons).checked = checkNode.checked;      	
			 		checkSonCheckbox($("checkbox_" + sons),$("checkbox_" + sons).sonids);			 		
		 		} 
		 	}
		 }
	</script>