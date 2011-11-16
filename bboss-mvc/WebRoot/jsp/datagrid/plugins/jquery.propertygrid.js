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
var _3=$.data(_2,"propertygrid").options;
$(_2).datagrid($.extend({},_3,{view:(_3.showGroup?_4:undefined),onClickRow:function(_5,_6){
if(_3.editIndex!=_5){
var _7=$(this).datagrid("getColumnOption","value");
_7.editor=_6.editor;
_8(_3.editIndex);
$(this).datagrid("beginEdit",_5);
$(this).datagrid("getEditors",_5)[0].target.focus();
_3.editIndex=_5;
}
_3.onClickRow.call(_2,_5,_6);
}}));
$(_2).datagrid("getPanel").panel("panel").addClass("propertygrid");
$(_2).datagrid("getPanel").find("div.datagrid-body").unbind(".propertygrid").bind("mousedown.propertygrid",function(e){
e.stopPropagation();
});
$(document).unbind(".propertygrid").bind("mousedown.propertygrid",function(){
_8(_3.editIndex);
_3.editIndex=undefined;
});
function _8(_9){
if(_9==undefined){
return;
}
var t=$(_2);
if(t.datagrid("validateRow",_9)){
t.datagrid("endEdit",_9);
}else{
t.datagrid("cancelEdit",_9);
}
};
};
$.fn.propertygrid=function(_a,_b){
if(typeof _a=="string"){
var _c=$.fn.propertygrid.methods[_a];
if(_c){
return _c(this,_b);
}else{
return this.datagrid(_a,_b);
}
}
_a=_a||{};
return this.each(function(){
var _d=$.data(this,"propertygrid");
if(_d){
$.extend(_d.options,_a);
}else{
$.data(this,"propertygrid",{options:$.extend({},$.fn.propertygrid.defaults,$.fn.propertygrid.parseOptions(this),_a)});
}
_1(this);
});
};
$.fn.propertygrid.methods={};
$.fn.propertygrid.parseOptions=function(_e){
var t=$(_e);
return $.extend({},$.fn.datagrid.parseOptions(_e),{showGroup:(t.attr("showGroup")?t.attr("showGroup")=="true":undefined)});
};
var _4=$.extend({},$.fn.datagrid.defaults.view,{render:function(_f,_10,_11){
var _12=$.data(_f,"datagrid").options;
var _13=$.data(_f,"datagrid").data.rows;
var _14=$(_f).datagrid("getColumnFields",_11);
var _15=[];
var _16=0;
var _17=this.groups;
for(var i=0;i<_17.length;i++){
var _18=_17[i];
_15.push("<div class=\"datagrid-group\" group-index="+i+">");
_15.push("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"height:100%\"><tbody>");
_15.push("<tr>");
_15.push("<td style=\"border:0;\">");
if(!_11){
_15.push("<span>");
_15.push(_12.groupFormatter.call(_f,_18.fvalue,_18.rows));
_15.push("</span>");
}
_15.push("</td>");
_15.push("</tr>");
_15.push("</tbody></table>");
_15.push("</div>");
_15.push("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>");
for(var j=0;j<_18.rows.length;j++){
var cls=(_16%2&&_12.striped)?"class=\"datagrid-row-alt\"":"";
var _19=_12.rowStyler?_12.rowStyler.call(_f,_16,_18.rows[j]):"";
var _1a=_19?"style=\""+_19+"\"":"";
_15.push("<tr datagrid-row-index=\""+_16+"\" "+cls+" "+_1a+">");
_15.push(this.renderRow.call(this,_f,_14,_11,_16,_18.rows[j]));
_15.push("</tr>");
_16++;
}
_15.push("</tbody></table>");
}
$(_10).html(_15.join(""));
},onAfterRender:function(_1b){
var _1c=$.data(_1b,"datagrid").options;
var _1d=$(_1b).datagrid("getPanel").find("div.datagrid-view");
var _1e=_1d.children("div.datagrid-view1");
var _1f=_1d.children("div.datagrid-view2");
$.fn.datagrid.defaults.view.onAfterRender.call(this,_1b);
if(_1c.rownumbers||_1c.frozenColumns.length){
var _20=_1e.find("div.datagrid-group");
}else{
var _20=_1f.find("div.datagrid-group");
}
$("<td style=\"border:0\"><div class=\"datagrid-row-expander datagrid-row-collapse\" style=\"width:25px;height:16px;cursor:pointer\"></div></td>").insertBefore(_20.find("td"));
_1d.find("div.datagrid-group").each(function(){
var _21=$(this).attr("group-index");
$(this).find("div.datagrid-row-expander").bind("click",{groupIndex:_21},function(e){
var _22=_1d.find("div.datagrid-group[group-index="+e.data.groupIndex+"]");
if($(this).hasClass("datagrid-row-collapse")){
$(this).removeClass("datagrid-row-collapse").addClass("datagrid-row-expand");
_22.next("table").hide();
}else{
$(this).removeClass("datagrid-row-expand").addClass("datagrid-row-collapse");
_22.next("table").show();
}
$(_1b).datagrid("fixRowHeight");
});
});
},onBeforeRender:function(_23,_24){
var _25=$.data(_23,"datagrid").options;
var _26=[];
for(var i=0;i<_24.length;i++){
var row=_24[i];
var _27=_28(row[_25.groupField]);
if(!_27){
_27={fvalue:row[_25.groupField],rows:[row],startRow:i};
_26.push(_27);
}else{
_27.rows.push(row);
}
}
function _28(_29){
for(var i=0;i<_26.length;i++){
var _2a=_26[i];
if(_2a.fvalue==_29){
return _2a;
}
}
return null;
};
this.groups=_26;
var _2b=[];
for(var i=0;i<_26.length;i++){
var _27=_26[i];
for(var j=0;j<_27.rows.length;j++){
_2b.push(_27.rows[j]);
}
}
$.data(_23,"datagrid").data.rows=_2b;
}});
$.fn.propertygrid.defaults=$.extend({},$.fn.datagrid.defaults,{singleSelect:true,remoteSort:false,fitColumns:true,loadMsg:"",frozenColumns:[[{field:"f",width:16,resizable:false}]],columns:[[{field:"name",title:"Name",width:100,sortable:true},{field:"value",title:"Value",width:100,resizable:false}]],showGroup:false,groupField:"group",groupFormatter:function(_2c){
return _2c;
}});
})(jQuery);

