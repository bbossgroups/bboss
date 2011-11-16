/**
 * jQuery EasyUI 1.2.3
 * 
 * Licensed under the GPL terms
 * To use it on other terms please contact us
 *
 * Copyright(c) 2009-2011 stworthy [ stworthy@gmail.com ] 
 * 
 */
(function($){
function _1(_2,_3){
_3=_3||{};
if(_3.onSubmit){
if(_3.onSubmit.call(_2)==false){
return;
}
}
var _4=$(_2);
if(_3.url){
_4.attr("action",_3.url);
}
var _5="easyui_frame_"+(new Date().getTime());
var _6=$("<iframe id="+_5+" name="+_5+"></iframe>").attr("src",window.ActiveXObject?"javascript:false":"about:blank").css({position:"absolute",top:-1000,left:-1000});
var t=_4.attr("target"),a=_4.attr("action");
_4.attr("target",_5);
try{
_6.appendTo("body");
_6.bind("load",cb);
_4[0].submit();
}
finally{
_4.attr("action",a);
t?_4.attr("target",t):_4.removeAttr("target");
}
var _7=10;
function cb(){
_6.unbind();
var _8=$("#"+_5).contents().find("body");
var _9=_8.html();
if(_9==""){
if(--_7){
setTimeout(cb,100);
return;
}
return;
}
var ta=_8.find(">textarea");
if(ta.length){
_9=ta.val();
}else{
var _a=_8.find(">pre");
if(_a.length){
_9=_a.html();
}
}
if(_3.success){
_3.success(_9);
}
setTimeout(function(){
_6.unbind();
_6.remove();
},100);
};
};
function _b(_c,_d){
if(!$.data(_c,"form")){
$.data(_c,"form",{options:$.extend({},$.fn.form.defaults)});
}
var _e=$.data(_c,"form").options;
if(typeof _d=="string"){
var _f={};
if(_e.onBeforeLoad.call(_c,_f)==false){
return;
}
$.ajax({url:_d,data:_f,dataType:"json",success:function(_10){
_11(_10);
},error:function(){
_e.onLoadError.apply(_c,arguments);
}});
}else{
_11(_d);
}
function _11(_12){
var _13=$(_c);
for(var _14 in _12){
var val=_12[_14];
$("input[name="+_14+"]",_13).val(val);
$("textarea[name="+_14+"]",_13).val(val);
$("select[name="+_14+"]",_13).val(val);
var cc=["combo","combobox","combotree","combogrid","datebox","datetimebox"];
for(var i=0;i<cc.length;i++){
_15(cc[i],_14,val);
}
}
_e.onLoadSuccess.call(_c,_12);
_20(_c);
};
function _15(_16,_17,val){
var _18=$(_c);
var c=_18.find("."+_16+"-f[comboName="+_17+"]");
if(c.length&&c[_16]){
if(c[_16]("options").multiple){
c[_16]("setValues",val);
}else{
c[_16]("setValue",val);
}
}
};
};
function _19(_1a){
$("input,select,textarea",_1a).each(function(){
var t=this.type,tag=this.tagName.toLowerCase();
if(t=="text"||t=="hidden"||t=="password"||tag=="textarea"){
this.value="";
}else{
if(t=="file"){
var _1b=$(this);
_1b.after(_1b.clone().val(""));
_1b.remove();
}else{
if(t=="checkbox"||t=="radio"){
this.checked=false;
}else{
if(tag=="select"){
this.selectedIndex=-1;
}
}
}
}
});
if($.fn.combo){
$(".combo-f",_1a).combo("clear");
}
if($.fn.combobox){
$(".combobox-f",_1a).combobox("clear");
}
if($.fn.combotree){
$(".combotree-f",_1a).combotree("clear");
}
if($.fn.combogrid){
$(".combogrid-f",_1a).combogrid("clear");
}
};
function _1c(_1d){
var _1e=$.data(_1d,"form").options;
var _1f=$(_1d);
_1f.unbind(".form").bind("submit.form",function(){
setTimeout(function(){
_1(_1d,_1e);
},0);
return false;
});
};
function _20(_21){
if($.fn.validatebox){
var box=$(".validatebox-text",_21);
if(box.length){
box.validatebox("validate");
box.trigger("blur");
var _22=$(".validatebox-invalid:first",_21).focus();
return _22.length==0;
}
}
return true;
};
$.fn.form=function(_23,_24){
if(typeof _23=="string"){
return $.fn.form.methods[_23](this,_24);
}
_23=_23||{};
return this.each(function(){
if(!$.data(this,"form")){
$.data(this,"form",{options:$.extend({},$.fn.form.defaults,_23)});
}
_1c(this);
});
};
$.fn.form.methods={submit:function(jq,_25){
return jq.each(function(){
_1(this,$.extend({},$.fn.form.defaults,_25||{}));
});
},load:function(jq,_26){
return jq.each(function(){
_b(this,_26);
});
},clear:function(jq){
return jq.each(function(){
_19(this);
});
},validate:function(jq){
return _20(jq[0]);
}};
$.fn.form.defaults={url:null,onSubmit:function(){
},success:function(_27){
},onBeforeLoad:function(_28){
},onLoadSuccess:function(_29){
},onLoadError:function(){
}};
})(jQuery);

