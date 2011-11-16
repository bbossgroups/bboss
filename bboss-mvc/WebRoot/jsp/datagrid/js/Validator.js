//校验是否是一个合法的名字
Validator.isName = function(str){
	var reg = /^[\w\u4e00-\u9fa5，、；‘’“”【】]+$/g;
	return reg.test(str);
}
//检测是否是一个合法的指标名称	
Validator.isItemName = function(str){
	var reg1 = /^[\S]+$/g;	
	var reg2 = /^[^%\'\",;:=+-\\{\\}\[\].]+$/g;
	return ((reg1.test(str)) && (reg2.test(str)));
}