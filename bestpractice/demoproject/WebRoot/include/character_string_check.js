/**----------------------------------------------------------------------------------
*
* input的一些为空判断，输入空字符的判断，特殊字符判断
*
*----------------------------------------------------------------------------------*/

//value = value.trim()--将value值的前后空格去掉
String.prototype.trim = function() 
{ 
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
};

/*
	清除文本框中的前后空格
	<input type="text" name="clean" value="" onblur="cleanSpace(this)" />
	当失去焦点时执行
*/
function cleanSpace(obj){
	obj.value = obj.value.trim(); 
}
/*
	汉字检验，不能输入汉字
	输入的字符串中含有汉字，则返回true
*/
function funcChina(s){
    if(/[^\x00-\xff]/g.test(s)){
    	//含有汉字
    	return true;
    }else{
    	//全是字符
		return false;
	}
}

/**
	MQ服务名称只能是：a-z或A-Z或0-9或"."或"_"或"-"这些字符
	如果包含了其他字符则返回true
*/
function mqBrokerNameCheck(obj){
	var re = /[a-zA-Z0-9\\.\\_\\-]/;
	var val = obj.value;
	var len = val.length;
	//alert(val + ".length = " + len);
	for(var i = 0; i < len; i++){
		var c = val.charAt(i);
		if(!re.test(c)){
			return true;
		}
	}
	return false;
	
	
}



/**   
 * 校验所有输入域是否含有特殊符号   
 * 所要过滤的符号写入正则表达式中，注意，一些符号要用'\'转义.   
 * 要转义的字符包括：1， 点号 .   
 *                   2,  中括号 []   
 *                   3,  大括号 {}   
 *                   4,  加号   +   
 *                   5,  星号   *   
 *                   6,  减号   -   
 *                   7,  斜杠   \   
 *                   8,  竖线   |   
 *                   9,  尖号   ^   
 *                   10, 钱币   $   
 *                   11, 问号   ？   
 * 试例：   
 * if(checkAllTextValid(document.forms[0]))   
 *  alert("表单中所有文本框通过校验！");   
 */   
function checkAllTextValid(form)    
{    
    //记录不含引号的文本框数量    
 	var resultTag = 0;    
    //记录所有text文本框数量
    var flag = 0;    
 	for(var i = 0; i < form.elements.length; i ++)
 	{    
  		if(form.elements[i].type=="text")
  		{    
            flag = flag + 1;
   			//此处填写所要过滤的特殊符号    
   			//注意：修改####处的字符，其它部分不许修改.    
   			//if(/^[^####]*$/.test(form.elements[i].value))
   			if(/^[^\|"'<>{}+-?&]*$/.test(form.elements[i].value))
                resultTag = resultTag+1;   
   			else   
    			form.elements[i].select();   
  		}
 	}   
  
    /**   
     * 如果含引号的文本框等于全部文本框的值，则校验通过   
     */
 	if(resultTag == flag)
  		return true;
 	else
 	{   
  		alert("文本框中不能含有以下特殊字符\n \\ \| \" ' < > { } + - ? & , . \n请检查输入！");    
  		return false;    
 	}    
}