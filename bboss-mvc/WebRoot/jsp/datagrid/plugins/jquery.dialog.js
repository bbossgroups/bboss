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
var t=$(_2);
t.wrapInner("<div class=\"dialog-content\"></div>");
var _3=t.children("div.dialog-content");
_3.attr("style",t.attr("style"));
t.removeAttr("style").css("overflow","hidden");
_3.panel({border:false,doSize:false});
return _3;
};
function _4(_5){
var _6=$.data(_5,"dialog").options;
var _7=$.data(_5,"dialog").contentPanel;
if(_6.toolbar){
if(typeof _6.toolbar=="string"){
$(_6.toolbar).addClass("dialog-toolbar").prependTo(_5);
$(_6.toolbar).show();
}else{
$(_5).find("div.dialog-toolbar").remove();
var _8=$("<div class=\"dialog-toolbar\"></div>").prependTo(_5);
for(var i=0;i<_6.toolbar.length;i++){
var p=_6.toolbar[i];
if(p=="-"){
_8.append("<div class=\"dialog-tool-separator\"></div>");
}else{
var _9=$("<a href=\"javascript:void(0)\"></a>").appendTo(_8);
_9.css("float","left");
_9[0].onclick=eval(p.handler||function(){
});
_9.linkbutton($.extend({},p,{plain:true}));
}
}
_8.append("<div style=\"clear:both\"></div>");
}
}else{
$(_5).find("div.dialog-toolbar").remove();
}
if(_6.buttons){
if(typeof _6.buttons=="string"){
$(_6.buttons).addClass("dialog-button").appendTo(_5);
$(_6.buttons).show();
}else{
$(_5).find("div.dialog-button").remove();
var _a=$("<div class=\"dialog-button\"></div>").appendTo(_5);
for(var i=0;i<_6.buttons.length;i++){
var p=_6.buttons[i];
var _b=$("<a href=\"javascript:void(0)\"></a>").appendTo(_a);
if(p.handler){
_b[0].onclick=p.handler;
}
_b.linkbutton(p);
}
}
}else{
$(_5).find("div.dialog-button").remove();
}
var _c=_6.href;
_6.href=null;
$(_5).window($.extend({},_6,{onOpen:function(){
_7.panel("open");
if(_6.onOpen){
_6.onOpen.call(_5);
}
},onResize:function(_d,_e){
var _f=$(_5).panel("panel").find(">div.panel-body");
_7.panel("resize",{width:_f.width(),height:(_e=="auto")?"auto":_f.height()-_f.find(">div.dialog-toolbar").outerHeight()-_f.find(">div.dialog-button").outerHeight()});
if(_6.onResize){
_6.onResize.call(_5,_d,_e);
}
}}));
_7.panel({closed:_6.closed,href:_c,onLoad:function(){
if(_6.height=="auto"){
$(_5).window("resize");
}
_6.onLoad.apply(_5,arguments);
}});
_6.href=_c;
};
function _10(_11,_12){
var _13=$.data(_11,"dialog").contentPanel;
_13.panel("refresh",_12);
};
$.fn.dialog=function(_14,_15){
if(typeof _14=="string"){
var _16=$.fn.dialog.methods[_14];
if(_16){
return _16(this,_15);
}else{
return this.window(_14,_15);
}
}
_14=_14||{};
return this.each(function(){
var _17=$.data(this,"dialog");
if(_17){
$.extend(_17.options,_14);
}else{
$.data(this,"dialog",{options:$.extend({},$.fn.dialog.defaults,$.fn.dialog.parseOptions(this),_14),contentPanel:_1(this)});
}
_4(this);
});
};
$.fn.dialog.methods={options:function(jq){
var _18=$.data(jq[0],"dialog").options;
var _19=jq.panel("options");
$.extend(_18,{closed:_19.closed,collapsed:_19.collapsed,minimized:_19.minimized,maximized:_19.maximized});
var _1a=$.data(jq[0],"dialog").contentPanel;
return _18;
},dialog:function(jq){
return jq.window("window");
},refresh:function(jq,_1b){
return jq.each(function(){
_10(this,_1b);
});
}};
$.fn.dialog.parseOptions=function(_1c){
var t=$(_1c);
return $.extend({},$.fn.window.parseOptions(_1c),{toolbar:t.attr("toolbar"),buttons:t.attr("buttons")});
};
$.fn.dialog.defaults=$.extend({},$.fn.window.defaults,{title:"New Dialog",collapsible:false,minimizable:false,maximizable:false,resizable:false,toolbar:null,buttons:null});
})(jQuery);

