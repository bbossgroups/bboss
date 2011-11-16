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
function _1(_2){
$(_2).addClass("validatebox-text");
};
function _3(_4){
var _5=$.data(_4,"validatebox");
_5.validating=false;
var _6=_5.tip;
if(_6){
_6.remove();
}
$(_4).unbind();
$(_4).remove();
};
function _7(_8){
var _9=$(_8);
var _a=$.data(_8,"validatebox");
_a.validating=false;
_9.unbind(".validatebox").bind("focus.validatebox",function(){
_a.validating=true;
_a.value=undefined;
(function(){
if(_a.validating){
if(_a.value!=_9.val()){
_a.value=_9.val();
_11(_8);
}
setTimeout(arguments.callee,200);
}
})();
}).bind("blur.validatebox",function(){
_a.validating=false;
_b(_8);
}).bind("mouseenter.validatebox",function(){
if(_9.hasClass("validatebox-invalid")){
_c(_8);
}
}).bind("mouseleave.validatebox",function(){
_b(_8);
});
};
function _c(_d){
var _e=$(_d);
var _f=$.data(_d,"validatebox").message;
var tip=$.data(_d,"validatebox").tip;
if(!tip){
tip=$("<div class=\"validatebox-tip\">"+"<span class=\"validatebox-tip-content\">"+"</span>"+"<span class=\"validatebox-tip-pointer\">"+"</span>"+"</div>").appendTo("body");
$.data(_d,"validatebox").tip=tip;
}
tip.find(".validatebox-tip-content").html(_f);
tip.css({display:"block",left:_e.offset().left+_e.outerWidth(),top:_e.offset().top});
};
function _b(_10){
var tip=$.data(_10,"validatebox").tip;
if(tip){
tip.remove();
$.data(_10,"validatebox").tip=null;
}
};
function _11(_12){
var _13=$.data(_12,"validatebox").options;
var tip=$.data(_12,"validatebox").tip;
var box=$(_12);
var _14=box.val();
function _15(msg){
$.data(_12,"validatebox").message=msg;
};
var _16=box.attr("disabled");
if(_16==true||_16=="true"){
return true;
}
if(_13.required){
if(_14==""){
box.addClass("validatebox-invalid");
_15(_13.missingMessage);
_c(_12);
return false;
}
}
if(_13.validType){
var _17=/([a-zA-Z_]+)(.*)/.exec(_13.validType);
var _18=_13.rules[_17[1]];
if(_14&&_18){
var _19=eval(_17[2]);
if(!_18["validator"](_14,_19)){
box.addClass("validatebox-invalid");
var _1a=_18["message"];
if(_19){
for(var i=0;i<_19.length;i++){
_1a=_1a.replace(new RegExp("\\{"+i+"\\}","g"),_19[i]);
}
}
_15(_13.invalidMessage||_1a);
_c(_12);
return false;
}
}
}
box.removeClass("validatebox-invalid");
_b(_12);
return true;
};
$.fn.validatebox=function(_1b,_1c){
if(typeof _1b=="string"){
return $.fn.validatebox.methods[_1b](this,_1c);
}
_1b=_1b||{};
return this.each(function(){
var _1d=$.data(this,"validatebox");
if(_1d){
$.extend(_1d.options,_1b);
}else{
_1(this);
$.data(this,"validatebox",{options:$.extend({},$.fn.validatebox.defaults,$.fn.validatebox.parseOptions(this),_1b)});
}
_7(this);
});
};
$.fn.validatebox.methods={destroy:function(jq){
return jq.each(function(){
_3(this);
});
},validate:function(jq){
return jq.each(function(){
_11(this);
});
},isValid:function(jq){
return _11(jq[0]);
}};
$.fn.validatebox.parseOptions=function(_1e){
var t=$(_1e);
return {required:(t.attr("required")?(t.attr("required")=="true"||t.attr("required")==true):undefined),validType:(t.attr("validType")||undefined),missingMessage:(t.attr("missingMessage")||undefined),invalidMessage:(t.attr("invalidMessage")||undefined)};
};
$.fn.validatebox.defaults={required:false,validType:null,missingMessage:"This field is required.",invalidMessage:null,rules:{email:{validator:function(_1f){
return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(_1f);
},message:"Please enter a valid email address."},url:{validator:function(_20){
return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(_20);
},message:"Please enter a valid URL."},length:{validator:function(_21,_22){
var len=$.trim(_21).length;
return len>=_22[0]&&len<=_22[1];
},message:"Please enter a value between {0} and {1}."},remote:{validator:function(_23,_24){
var _25={};
_25[_24[1]]=_23;
var _26=$.ajax({url:_24[0],dataType:"json",data:_25,async:false,cache:false,type:"post"}).responseText;
return _26=="true";
},message:"Please fix this field."}}};
})(jQuery);

