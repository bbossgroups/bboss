/*
	查询select列表框中的数据 select名称必须为"allist"
	
*/

/*存放select索引数组*/
var eleArray = new Array();
/*模糊查询下一条记录*/
var next = 0;
function blurryQuary(){
	var count = 0;
	var seRole = document.all("selectRoles").value;
	if(seRole==""){
		alert("查询条件不能为空！");
		return false;
	}
	var obj = document.all("allist");
	for(var i = 0; i < obj.length; i++){
		obj.options[i].selected = false;
		if(obj.options[i].text.indexOf(seRole) != -1){
			eleArray[count] = i;
			count ++;
		}
	}
	
	if(eleArray.length > 0){
		var j = eleArray[0];
		obj.options[j].selected = true;
		document.all.que.style.display="none";
 		document.all.nextQue.style.display="";
		next ++;
	}else{
		alert("没有符合的条件");
		return false;
	}
}


function nextQuery(){
	var obj = document.all("allist");
	if(eleArray.length > next){
		for(var i = 0; i < obj.length; i++){
			obj.options[i].selected = false;
		}
		var curCount = eleArray[next];
		obj.options[curCount].selected = true;
		next++;
	}else{
		alert("查询完成！");
		onBlurryQueryChange();
		next = 0;
	}
}

/**文本属性改变**/
function onBlurryQueryChange()
{
 count = 0;
 eleArray.length = 0;
 document.all.que.style.display="";
 document.all.nextQue.style.display="none";
}

function enterKeydowngo(){ 
	if(window.event.keyCode == 13){
		if(document.all.que.style.display==""){
			blurryQuary();
		}else{
			nextQuery();
		}
	}
}

/*
  <tr>
  	<td width="40%" align="left">
  		<input name="selectRoles" type="text" value="" onkeydown="enterKeydowngo()" onpropertychange="onBlurryQueryChange()" ondblclick=""  />
  		<input name="que" type="button" value="查询" onclick="blurryQuary()" class="input" />
  		<input name="nextQue" style="display:none" type="button" value="查找下一个" onclick="nextQuery()" class="input" />
  	</td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="left">
    	
    </td>
  </tr>
*/
