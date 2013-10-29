/************************2008-08-27 author 李峰高 **************************
为了响应键盘输入码值查找选项
*************************2008-08-27 author 李峰高 **************************/
/*
document.attachEvent("onblur", searchOptionOnblur);
//var currentSearchSelect = null;
function searchOptionOnblur(currentSearchSelect){
	if(currentSearchSelect != null && window.event.srcElement == currentSearchSelect){
		currentSearchSelect.code = "";
	}
}
示例代码：可以参考 TestOptionSearcher.html 页面

--对于单选的下拉框:

<select name="levy_item" onpropertychange="searchDictFun(this);conculate();" style="width:240px;" validator="num" cnname="税种税目" msg="请选择税种税目！" 
  onkeydown="searchOption(this);"  onkeypress="resetKeyPress();" onblur="clearCode(this);" onchange="clearCode(this);"
 >
其中需要监听四个事件: onkeydown="searchOption(this);"  onkeypress="resetKeyPress();" onblur="clearCode(this);" onchange="clearCode(this);"
onkeydown事件是必须的

--对于多选框，需要另外的文本查询框

<input type="TEXT" name="test" onkeydown="searchOption(levy_item);"  onblur="clearCode(levy_item);" onchange="clearCode(levy_item);"/>
<input type="button" name="btn" value="搜索" onclick="doSearch(test.value,levy_item)"/>
<br>
<select name="levy_item" id="levy_item" size=8 multiple>
 <option name="o1">011-煤油</option>
 <option name="o1">012-原油</option>
</select>
*/
//用户按编码，直接定位select的option
function searchOption(oSelect, defaultValue){
	//与多选还是单选无关，通过oSelect.multiple可以判断单选还是多选
	//alert(oSelect.multiple);
	searchSelect(oSelect, defaultValue);
}

//从多选的select中搜索
function searchSelect(oSelect, defaultValue){
	//alert("searchMultipleSelect");
	/*当没有找到选项的时候，设置默认值*/
	var defaultIndex = oSelect.selectedIndex ;//当输入了错误的码值时，默认为之前的不动。如果要默认为空，则只需给它-1
	if(null == defaultValue || "current" == defaultValue){
		//current为默认方式
	}
	else if("blank" == defaultValue){
		defaultIndex = -1 ;
	}else if("first" == defaultValue){
		defaultIndex = 0 ;
	}else if("last" == defaultValue){
		defaultIndex = oSelect.options.length-1 ;
	}else if(Number.NaN != parseInt(defaultValue)){
		defaultIndex = parseInt(defaultValue) ;
	}
//alert(defaultIndex);
	if(window.event.keyCode == 13){//如果是回车符号，则执行搜索动作	   
		doSearch(oSelect.code,oSelect, defaultIndex) ;
		oSelect.code = "";//每搜索完毕，则将变量置空。
	}else{//否则保存select的属性		
		if(oSelect.code == null){ oSelect.code = "" ;}	

		var tempCode = window.event.keyCode ;
		//因为小键盘上面的code码不同，先转换为小键盘的，从48到57是数字0-9。
		if(window.event.keyCode >= 48 && window.event.keyCode <= 57){
			tempCode += 48;
		}
		//alert(tempCode);
		//如果是文本框
		//if(event.srcElement.type == "text"){
			//oSelect.code = event.srcElement.value;
		//}else
		{
			//删除键
			if(tempCode == 8 && oSelect.code.length>0){				
				oSelect.code = oSelect.code.substring(0,oSelect.code.length-1);			
			}else if(tempCode>=96 && tempCode<=105){
				oSelect.code += new String(tempCode-96) ;//unicode码-96之后就是数字本身
			}
		}
		//alert(oSelect.code);
		doSearch(oSelect.code,oSelect, defaultIndex) ;
	}
}

//搜索定位，根据用户的输入返回搜索到的index
function doSearch(content,oSelect, defaultIndex){	
	var index = defaultIndex ;	//原来想默认为oSelect.selectedIndex，后来用空值替代;	
	for(var i=0; i<oSelect.options.length; i++){
		if(oSelect.options[i].text.indexOf(content) >= 0){
			index = i ;		
			break ;
		}
	}
	oSelect.selectedIndex = index;
	//return index ;
}

//清空select的code值
function clearCode(oSelect){
	if(event.srcElement.type == "text"){
			oSelect.code = event.srcElement.value;
	}else{
		oSelect.code = "";
	}	
}

function resetKeyPress(){
	window.event.returnValue = false;
	//oSelect.focus();
}
/**************************************************/