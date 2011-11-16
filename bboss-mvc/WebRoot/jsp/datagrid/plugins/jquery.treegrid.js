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
var _3=$.data(_2,"treegrid").options;
$(_2).datagrid($.extend({},_3,{url:null,onLoadSuccess:function(){
},onResizeColumn:function(_4,_5){
_11(_2);
_3.onResizeColumn.call(_2,_4,_5);
},onBeforeEdit:function(_6,_7){
if(_3.onBeforeEdit.call(_2,_7)==false){
return false;
}
},onAfterEdit:function(_8,_9,_a){
_25(_2);
_3.onAfterEdit.call(_2,_9,_a);
},onCancelEdit:function(_b,_c){
_25(_2);
_3.onCancelEdit.call(_2,_c);
}}));
if(_3.pagination){
var _d=$(_2).datagrid("getPager");
_d.pagination({pageNumber:_3.pageNumber,pageSize:_3.pageSize,pageList:_3.pageList,onSelectPage:function(_e,_f){
_3.pageNumber=_e;
_3.pageSize=_f;
_10(_2);
}});
_3.pageSize=_d.pagination("options").pageSize;
}
};
function _11(_12,_13){
var _14=$.data(_12,"datagrid").options;
var _15=$.data(_12,"datagrid").panel;
var _16=_15.children("div.datagrid-view");
var _17=_16.children("div.datagrid-view1");
var _18=_16.children("div.datagrid-view2");
if(_14.rownumbers||(_14.frozenColumns&&_14.frozenColumns.length>0)){
if(_13){
_19(_13);
_18.find("tr[node-id="+_13+"]").next("tr.treegrid-tr-tree").find("tr[node-id]").each(function(){
_19($(this).attr("node-id"));
});
}else{
_18.find("tr[node-id]").each(function(){
_19($(this).attr("node-id"));
});
if(_14.showFooter){
var _1a=$.data(_12,"datagrid").footer||[];
for(var i=0;i<_1a.length;i++){
_19(_1a[i][_14.idField]);
}
$(_12).datagrid("resize");
}
}
}
if(_14.height=="auto"){
var _1b=_17.children("div.datagrid-body");
var _1c=_18.children("div.datagrid-body");
var _1d=0;
var _1e=0;
_1c.children().each(function(){
var c=$(this);
if(c.is(":visible")){
_1d+=c.outerHeight();
if(_1e<c.outerWidth()){
_1e=c.outerWidth();
}
}
});
if(_1e>_1c.width()){
_1d+=18;
}
_1b.height(_1d);
_1c.height(_1d);
_16.height(_18.height());
}
_18.children("div.datagrid-body").triggerHandler("scroll");
function _19(_1f){
var tr1=_17.find("tr[node-id="+_1f+"]");
var tr2=_18.find("tr[node-id="+_1f+"]");
tr1.css("height","");
tr2.css("height","");
var _20=Math.max(tr1.height(),tr2.height());
tr1.css("height",_20);
tr2.css("height",_20);
};
};
function _21(_22){
var _23=$.data(_22,"treegrid").options;
if(!_23.rownumbers){
return;
}
$(_22).datagrid("getPanel").find("div.datagrid-view1 div.datagrid-body div.datagrid-cell-rownumber").each(function(i){
var _24=i+1;
$(this).html(_24);
});
};
function _25(_26){
var _27=$.data(_26,"treegrid").options;
var _28=$(_26).datagrid("getPanel");
var _29=_28.find("div.datagrid-body");
_29.find("span.tree-hit").unbind(".treegrid").bind("click.treegrid",function(){
var tr=$(this).parent().parent().parent();
var id=tr.attr("node-id");
_90(_26,id);
return false;
}).bind("mouseenter.treegrid",function(){
if($(this).hasClass("tree-expanded")){
$(this).addClass("tree-expanded-hover");
}else{
$(this).addClass("tree-collapsed-hover");
}
}).bind("mouseleave.treegrid",function(){
if($(this).hasClass("tree-expanded")){
$(this).removeClass("tree-expanded-hover");
}else{
$(this).removeClass("tree-collapsed-hover");
}
});
_29.find("tr[node-id]").unbind(".treegrid").bind("mouseenter.treegrid",function(){
var id=$(this).attr("node-id");
_29.find("tr[node-id="+id+"]").addClass("datagrid-row-over");
}).bind("mouseleave.treegrid",function(){
var id=$(this).attr("node-id");
_29.find("tr[node-id="+id+"]").removeClass("datagrid-row-over");
}).bind("click.treegrid",function(){
var id=$(this).attr("node-id");
if(_27.singleSelect){
_2c(_26);
_7a(_26,id);
}else{
if($(this).hasClass("datagrid-row-selected")){
_7e(_26,id);
}else{
_7a(_26,id);
}
}
_27.onClickRow.call(_26,_43(_26,id));
}).bind("dblclick.treegrid",function(){
var id=$(this).attr("node-id");
_27.onDblClickRow.call(_26,_43(_26,id));
}).bind("contextmenu.treegrid",function(e){
var id=$(this).attr("node-id");
_27.onContextMenu.call(_26,e,_43(_26,id));
});
_29.find("div.datagrid-cell-check input[type=checkbox]").unbind(".treegrid").bind("click.treegrid",function(e){
var id=$(this).parent().parent().parent().attr("node-id");
if(_27.singleSelect){
_2c(_26);
_7a(_26,id);
}else{
if($(this).attr("checked")){
_7a(_26,id);
}else{
_7e(_26,id);
}
}
e.stopPropagation();
});
var _2a=_28.find("div.datagrid-header");
_2a.find("input[type=checkbox]").unbind().bind("click.treegrid",function(){
if(_27.singleSelect){
return false;
}
if($(this).attr("checked")){
_2b(_26);
}else{
_2c(_26);
}
});
};
function _2d(_2e,_2f){
var _30=$.data(_2e,"treegrid").options;
var _31=$(_2e).datagrid("getPanel").children("div.datagrid-view");
var _32=_31.children("div.datagrid-view1");
var _33=_31.children("div.datagrid-view2");
var tr1=_32.children("div.datagrid-body").find("tr[node-id="+_2f+"]");
var tr2=_33.children("div.datagrid-body").find("tr[node-id="+_2f+"]");
var _34=$(_2e).datagrid("getColumnFields",true).length+(_30.rownumbers?1:0);
var _35=$(_2e).datagrid("getColumnFields",false).length;
_36(tr1,_34);
_36(tr2,_35);
function _36(tr,_37){
$("<tr class=\"treegrid-tr-tree\">"+"<td style=\"border:0px\" colspan=\""+_37+"\">"+"<div></div>"+"</td>"+"</tr>").insertAfter(tr);
};
};
function _38(_39,_3a,_3b,_3c){
var _3d=$.data(_39,"treegrid").options;
var _3e=$.data(_39,"datagrid").panel;
var _3f=_3e.children("div.datagrid-view");
var _40=_3f.children("div.datagrid-view1");
var _41=_3f.children("div.datagrid-view2");
var _42=_43(_39,_3a);
if(_42){
var _44=_40.children("div.datagrid-body").find("tr[node-id="+_3a+"]");
var _45=_41.children("div.datagrid-body").find("tr[node-id="+_3a+"]");
var cc1=_44.next("tr.treegrid-tr-tree").children("td").children("div");
var cc2=_45.next("tr.treegrid-tr-tree").children("td").children("div");
}else{
var cc1=_40.children("div.datagrid-body").children("div.datagrid-body-inner");
var cc2=_41.children("div.datagrid-body");
}
if(!_3c){
$.data(_39,"treegrid").data=[];
cc1.empty();
cc2.empty();
}
if(_3d.view.onBeforeRender){
_3d.view.onBeforeRender.call(_3d.view,_39,_3a,_3b);
}
_3d.view.render.call(_3d.view,_39,cc1,true);
_3d.view.render.call(_3d.view,_39,cc2,false);
if(_3d.showFooter){
_3d.view.renderFooter.call(_3d.view,_39,_40.find("div.datagrid-footer-inner"),true);
_3d.view.renderFooter.call(_3d.view,_39,_41.find("div.datagrid-footer-inner"),false);
}
if(_3d.view.onAfterRender){
_3d.view.onAfterRender.call(_3d.view,_39);
}
_3d.onLoadSuccess.call(_39,_42,_3b);
if(!_3a&&_3d.pagination){
var _46=$(_39).datagrid("getPager");
if(_46.pagination("options").total!=_3b.total){
_46.pagination({total:_3b.total});
}
}
_11(_39);
_21(_39);
_47();
_25(_39);
function _47(){
var _48=_3f.find("div.datagrid-header");
var _49=_3f.find("div.datagrid-body");
var _4a=_48.find("div.datagrid-header-check");
if(_4a.length){
var ck=_49.find("div.datagrid-cell-check");
if($.boxModel){
ck.width(_4a.width());
ck.height(_4a.height());
}else{
ck.width(_4a.outerWidth());
ck.height(_4a.outerHeight());
}
}
};
};
function _10(_4b,_4c,_4d,_4e,_4f){
var _50=$.data(_4b,"treegrid").options;
var _51=$(_4b).datagrid("getPanel").find("div.datagrid-body");
if(_4d){
_50.queryParams=_4d;
}
var _52=$.extend({},_50.queryParams);
var row=_43(_4b,_4c);
if(_50.onBeforeLoad.call(_4b,row,_52)==false){
return;
}
if(!_50.url){
return;
}
var _53=_51.find("tr[node-id="+_4c+"] span.tree-folder");
_53.addClass("tree-loading");
$(_4b).treegrid("loading");
$.ajax({type:_50.method,url:_50.url,data:_52,dataType:"json",success:function(_54){
_53.removeClass("tree-loading");
$(_4b).treegrid("loaded");
_38(_4b,_4c,_54,_4e);
if(_4f){
_4f();
}
},error:function(){
_53.removeClass("tree-loading");
$(_4b).treegrid("loaded");
_50.onLoadError.apply(_4b,arguments);
if(_4f){
_4f();
}
}});
};
function _55(_56){
var _57=_58(_56);
if(_57.length){
return _57[0];
}else{
return null;
}
};
function _58(_59){
return $.data(_59,"treegrid").data;
};
function _5a(_5b,_5c){
var row=_43(_5b,_5c);
if(row._parentId){
return _43(_5b,row._parentId);
}else{
return null;
}
};
function _5d(_5e,_5f){
var _60=$.data(_5e,"treegrid").options;
var _61=$(_5e).datagrid("getPanel").find("div.datagrid-view2 div.datagrid-body");
var _62=[];
if(_5f){
_63(_5f);
}else{
var _64=_58(_5e);
for(var i=0;i<_64.length;i++){
_62.push(_64[i]);
_63(_64[i][_60.idField]);
}
}
function _63(_65){
var _66=_43(_5e,_65);
if(_66&&_66.children){
for(var i=0,len=_66.children.length;i<len;i++){
var _67=_66.children[i];
_62.push(_67);
_63(_67[_60.idField]);
}
}
};
return _62;
};
function _68(_69){
var _6a=_6b(_69);
if(_6a.length){
return _6a[0];
}else{
return null;
}
};
function _6b(_6c){
var _6d=[];
var _6e=$(_6c).datagrid("getPanel");
_6e.find("div.datagrid-view2 div.datagrid-body tr.datagrid-row-selected").each(function(){
var id=$(this).attr("node-id");
_6d.push(_43(_6c,id));
});
return _6d;
};
function _6f(_70,_71){
if(!_71){
return 0;
}
var _72=$.data(_70,"treegrid").options;
var _73=$(_70).datagrid("getPanel").children("div.datagrid-view");
var _74=_73.find("div.datagrid-body tr[node-id="+_71+"]").children("td[field="+_72.treeField+"]");
return _74.find("span.tree-indent,span.tree-hit").length;
};
function _43(_75,_76){
var _77=$.data(_75,"treegrid").options;
var _78=$.data(_75,"treegrid").data;
var cc=[_78];
while(cc.length){
var c=cc.shift();
for(var i=0;i<c.length;i++){
var _79=c[i];
if(_79[_77.idField]==_76){
return _79;
}else{
if(_79["children"]){
cc.push(_79["children"]);
}
}
}
}
return null;
};
function _7a(_7b,_7c){
var _7d=$(_7b).datagrid("getPanel").find("div.datagrid-body");
var tr=_7d.find("tr[node-id="+_7c+"]");
tr.addClass("datagrid-row-selected");
tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",true);
};
function _7e(_7f,_80){
var _81=$(_7f).datagrid("getPanel").find("div.datagrid-body");
var tr=_81.find("tr[node-id="+_80+"]");
tr.removeClass("datagrid-row-selected");
tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",false);
};
function _2b(_82){
var tr=$(_82).datagrid("getPanel").find("div.datagrid-body tr[node-id]");
tr.addClass("datagrid-row-selected");
tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",true);
};
function _2c(_83){
var tr=$(_83).datagrid("getPanel").find("div.datagrid-body tr[node-id]");
tr.removeClass("datagrid-row-selected");
tr.find("div.datagrid-cell-check input[type=checkbox]").attr("checked",false);
};
function _84(_85,_86){
var _87=$.data(_85,"treegrid").options;
var _88=$(_85).datagrid("getPanel").find("div.datagrid-body");
var row=_43(_85,_86);
var tr=_88.find("tr[node-id="+_86+"]");
var hit=tr.find("span.tree-hit");
if(hit.length==0){
return;
}
if(hit.hasClass("tree-collapsed")){
return;
}
if(_87.onBeforeCollapse.call(_85,row)==false){
return;
}
hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
hit.next().removeClass("tree-folder-open");
row.state="closed";
tr=tr.next("tr.treegrid-tr-tree");
var cc=tr.children("td").children("div");
if(_87.animate){
cc.slideUp("normal",function(){
_11(_85,_86);
_87.onCollapse.call(_85,row);
});
}else{
cc.hide();
_11(_85,_86);
_87.onCollapse.call(_85,row);
}
};
function _89(_8a,_8b){
var _8c=$.data(_8a,"treegrid").options;
var _8d=$(_8a).datagrid("getPanel").find("div.datagrid-body");
var tr=_8d.find("tr[node-id="+_8b+"]");
var hit=tr.find("span.tree-hit");
var row=_43(_8a,_8b);
if(hit.length==0){
return;
}
if(hit.hasClass("tree-expanded")){
return;
}
if(_8c.onBeforeExpand.call(_8a,row)==false){
return;
}
hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
hit.next().addClass("tree-folder-open");
var _8e=tr.next("tr.treegrid-tr-tree");
if(_8e.length){
var cc=_8e.children("td").children("div");
_8f(cc);
}else{
_2d(_8a,row[_8c.idField]);
var _8e=tr.next("tr.treegrid-tr-tree");
var cc=_8e.children("td").children("div");
cc.hide();
_10(_8a,row[_8c.idField],{id:row[_8c.idField]},true,function(){
_8f(cc);
});
}
function _8f(cc){
row.state="open";
if(_8c.animate){
cc.slideDown("normal",function(){
_11(_8a,_8b);
_8c.onExpand.call(_8a,row);
});
}else{
cc.show();
_11(_8a,_8b);
_8c.onExpand.call(_8a,row);
}
};
};
function _90(_91,_92){
var _93=$(_91).datagrid("getPanel").find("div.datagrid-body");
var tr=_93.find("tr[node-id="+_92+"]");
var hit=tr.find("span.tree-hit");
if(hit.hasClass("tree-expanded")){
_84(_91,_92);
}else{
_89(_91,_92);
}
};
function _94(_95,_96){
var _97=$.data(_95,"treegrid").options;
var _98=_5d(_95,_96);
if(_96){
_98.unshift(_43(_95,_96));
}
for(var i=0;i<_98.length;i++){
_84(_95,_98[i][_97.idField]);
}
};
function _99(_9a,_9b){
var _9c=$.data(_9a,"treegrid").options;
var _9d=_5d(_9a,_9b);
if(_9b){
_9d.unshift(_43(_9a,_9b));
}
for(var i=0;i<_9d.length;i++){
_89(_9a,_9d[i][_9c.idField]);
}
};
function _9e(_9f,_a0){
var _a1=$.data(_9f,"treegrid").options;
var ids=[];
var p=_5a(_9f,_a0);
while(p){
var id=p[_a1.idField];
ids.unshift(id);
p=_5a(_9f,id);
}
for(var i=0;i<ids.length;i++){
_89(_9f,ids[i]);
}
};
function _a2(_a3,_a4){
var _a5=$.data(_a3,"treegrid").options;
if(_a4.parent){
var _a6=$(_a3).datagrid("getPanel").find("div.datagrid-body");
var tr=_a6.find("tr[node-id="+_a4.parent+"]");
if(tr.next("tr.treegrid-tr-tree").length==0){
_2d(_a3,_a4.parent);
}
var _a7=tr.children("td[field="+_a5.treeField+"]").children("div.datagrid-cell");
var _a8=_a7.children("span.tree-icon");
if(_a8.hasClass("tree-file")){
_a8.removeClass("tree-file").addClass("tree-folder");
var hit=$("<span class=\"tree-hit tree-expanded\"></span>").insertBefore(_a8);
if(hit.prev().length){
hit.prev().remove();
}
}
}
_38(_a3,_a4.parent,_a4.data,true);
};
function _a9(_aa,_ab){
var _ac=$.data(_aa,"treegrid").options;
var _ad=$(_aa).datagrid("getPanel").find("div.datagrid-body");
var tr=_ad.find("tr[node-id="+_ab+"]");
tr.next("tr.treegrid-tr-tree").remove();
tr.remove();
var _ae=del(_ab);
if(_ae){
if(_ae.children.length==0){
tr=_ad.find("tr[node-id="+_ae[_ac.treeField]+"]");
var _af=tr.children("td[field="+_ac.treeField+"]").children("div.datagrid-cell");
_af.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
_af.find(".tree-hit").remove();
$("<span class=\"tree-indent\"></span>").prependTo(_af);
}
}
_21(_aa);
function del(id){
var cc;
var _b0=_5a(_aa,_ab);
if(_b0){
cc=_b0.children;
}else{
cc=$(_aa).treegrid("getData");
}
for(var i=0;i<cc.length;i++){
if(cc[i][_ac.treeField]==id){
cc.splice(i,1);
break;
}
}
return _b0;
};
};
$.fn.treegrid=function(_b1,_b2){
if(typeof _b1=="string"){
var _b3=$.fn.treegrid.methods[_b1];
if(_b3){
return _b3(this,_b2);
}else{
return this.datagrid(_b1,_b2);
}
}
_b1=_b1||{};
return this.each(function(){
var _b4=$.data(this,"treegrid");
if(_b4){
$.extend(_b4.options,_b1);
}else{
$.data(this,"treegrid",{options:$.extend({},$.fn.treegrid.defaults,$.fn.treegrid.parseOptions(this),_b1),data:[]});
}
_1(this);
_10(this);
});
};
$.fn.treegrid.methods={options:function(jq){
return $.data(jq[0],"treegrid").options;
},resize:function(jq,_b5){
return jq.each(function(){
$(this).datagrid("resize",_b5);
});
},fixRowHeight:function(jq,_b6){
return jq.each(function(){
_11(this,_b6);
});
},loadData:function(jq,_b7){
return jq.each(function(){
_38(this,null,_b7);
});
},reload:function(jq,id){
return jq.each(function(){
if(id){
var _b8=$(this).treegrid("find",id);
if(_b8.children){
_b8.children.splice(0,_b8.children.length);
}
var _b9=$(this).datagrid("getPanel").find("div.datagrid-body");
var tr=_b9.find("tr[node-id="+id+"]");
tr.next("tr.treegrid-tr-tree").remove();
var hit=tr.find("span.tree-hit");
hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
_89(this,id);
}else{
_10(this);
}
});
},reloadFooter:function(jq,_ba){
return jq.each(function(){
var _bb=$.data(this,"treegrid").options;
var _bc=$(this).datagrid("getPanel").children("div.datagrid-view");
var _bd=_bc.children("div.datagrid-view1");
var _be=_bc.children("div.datagrid-view2");
if(_ba){
$.data(this,"treegrid").footer=_ba;
}
if(_bb.showFooter){
_bb.view.renderFooter.call(_bb.view,this,_bd.find("div.datagrid-footer-inner"),true);
_bb.view.renderFooter.call(_bb.view,this,_be.find("div.datagrid-footer-inner"),false);
if(_bb.view.onAfterRender){
_bb.view.onAfterRender.call(_bb.view,this);
}
$(this).treegrid("fixRowHeight");
}
});
},loading:function(jq){
return jq.each(function(){
$(this).datagrid("loading");
});
},loaded:function(jq){
return jq.each(function(){
$(this).datagrid("loaded");
});
},getData:function(jq){
return $.data(jq[0],"treegrid").data;
},getFooterRows:function(jq){
return $.data(jq[0],"treegrid").footer;
},getRoot:function(jq){
return _55(jq[0]);
},getRoots:function(jq){
return _58(jq[0]);
},getParent:function(jq,id){
return _5a(jq[0],id);
},getChildren:function(jq,id){
return _5d(jq[0],id);
},getSelected:function(jq){
return _68(jq[0]);
},getSelections:function(jq){
return _6b(jq[0]);
},getLevel:function(jq,id){
return _6f(jq[0],id);
},find:function(jq,id){
return _43(jq[0],id);
},select:function(jq,id){
return jq.each(function(){
_7a(this,id);
});
},unselect:function(jq,id){
return jq.each(function(){
_7e(this,id);
});
},selectAll:function(jq){
return jq.each(function(){
_2b(this);
});
},unselectAll:function(jq){
return jq.each(function(){
_2c(this);
});
},collapse:function(jq,id){
return jq.each(function(){
_84(this,id);
});
},expand:function(jq,id){
return jq.each(function(){
_89(this,id);
});
},toggle:function(jq,id){
return jq.each(function(){
_90(this,id);
});
},collapseAll:function(jq,id){
return jq.each(function(){
_94(this,id);
});
},expandAll:function(jq,id){
return jq.each(function(){
_99(this,id);
});
},expandTo:function(jq,id){
return jq.each(function(){
_9e(this,id);
});
},append:function(jq,_bf){
return jq.each(function(){
_a2(this,_bf);
});
},remove:function(jq,id){
return jq.each(function(){
_a9(this,id);
});
},refresh:function(jq,id){
return jq.each(function(){
var _c0=$.data(this,"treegrid").options;
_c0.view.refreshRow.call(_c0.view,this,id);
});
},beginEdit:function(jq,id){
return jq.each(function(){
$(this).datagrid("beginEdit",id);
$(this).treegrid("fixRowHeight",id);
});
},endEdit:function(jq,id){
return jq.each(function(){
$(this).datagrid("endEdit",id);
});
},cancelEdit:function(jq,id){
return jq.each(function(){
$(this).datagrid("cancelEdit",id);
});
}};
$.fn.treegrid.parseOptions=function(_c1){
var t=$(_c1);
return $.extend({},$.fn.datagrid.parseOptions(_c1),{treeField:t.attr("treeField"),animate:(t.attr("animate")?t.attr("animate")=="true":undefined)});
};
var _c2=$.extend({},$.fn.datagrid.defaults.view,{render:function(_c3,_c4,_c5){
var _c6=$.data(_c3,"treegrid").options;
var _c7=$(_c3).datagrid("getColumnFields",_c5);
var _c8=this;
var _c9=_ca(_c5,this.treeLevel,this.treeNodes);
$(_c4).append(_c9.join(""));
function _ca(_cb,_cc,_cd){
var _ce=["<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
for(var i=0;i<_cd.length;i++){
var row=_cd[i];
if(row.state!="open"&&row.state!="closed"){
row.state="open";
}
var _cf=_c6.rowStyler?_c6.rowStyler.call(_c3,row):"";
var _d0=_cf?"style=\""+_cf+"\"":"";
_ce.push("<tr node-id="+row[_c6.idField]+" "+_d0+">");
_ce=_ce.concat(_c8.renderRow.call(_c8,_c3,_c7,_cb,_cc,row));
_ce.push("</tr>");
if(row.children&&row.children.length){
var tt=_ca(_cb,_cc+1,row.children);
var v=row.state=="closed"?"none":"block";
_ce.push("<tr class=\"treegrid-tr-tree\"><td style=\"border:0px\" colspan="+(_c7.length+(_c6.rownumbers?1:0))+"><div style=\"display:"+v+"\">");
_ce=_ce.concat(tt);
_ce.push("</div></td></tr>");
}
}
_ce.push("</tbody></table>");
return _ce;
};
},renderFooter:function(_d1,_d2,_d3){
var _d4=$.data(_d1,"treegrid").options;
var _d5=$.data(_d1,"treegrid").footer||[];
var _d6=$(_d1).datagrid("getColumnFields",_d3);
var _d7=["<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
for(var i=0;i<_d5.length;i++){
var row=_d5[i];
row[_d4.idField]=row[_d4.idField]||("foot-row-id"+i);
_d7.push("<tr node-id="+row[_d4.idField]+">");
_d7.push(this.renderRow.call(this,_d1,_d6,_d3,0,row));
_d7.push("</tr>");
}
_d7.push("</tbody></table>");
$(_d2).html(_d7.join(""));
},renderRow:function(_d8,_d9,_da,_db,row){
var _dc=$.data(_d8,"treegrid").options;
var cc=[];
if(_da&&_dc.rownumbers){
cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">0</div></td>");
}
for(var i=0;i<_d9.length;i++){
var _dd=_d9[i];
var col=$(_d8).datagrid("getColumnOption",_dd);
if(col){
var _de=col.styler?(col.styler(row[_dd],row)||""):"";
var _df=col.hidden?"style=\"display:none;"+_de+"\"":(_de?"style=\""+_de+"\"":"");
cc.push("<td field=\""+_dd+"\" "+_df+">");
var _df="width:"+(col.boxWidth)+"px;";
_df+="text-align:"+(col.align||"left")+";";
_df+=_dc.nowrap==false?"white-space:normal;":"";
cc.push("<div style=\""+_df+"\" ");
if(col.checkbox){
cc.push("class=\"datagrid-cell-check ");
}else{
cc.push("class=\"datagrid-cell ");
}
cc.push("\">");
if(col.checkbox){
if(row.checked){
cc.push("<input type=\"checkbox\" checked=\"checked\"/>");
}else{
cc.push("<input type=\"checkbox\"/>");
}
}else{
var val=null;
if(col.formatter){
val=col.formatter(row[_dd],row);
}else{
val=row[_dd]||"&nbsp;";
}
if(_dd==_dc.treeField){
for(var j=0;j<_db;j++){
cc.push("<span class=\"tree-indent\"></span>");
}
if(row.state=="closed"){
cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
cc.push("<span class=\"tree-icon tree-folder "+(row.iconCls?row.iconCls:"")+"\"></span>");
}else{
if(row.children&&row.children.length){
cc.push("<span class=\"tree-hit tree-expanded\"></span>");
cc.push("<span class=\"tree-icon tree-folder tree-folder-open "+(row.iconCls?row.iconCls:"")+"\"></span>");
}else{
cc.push("<span class=\"tree-indent\"></span>");
cc.push("<span class=\"tree-icon tree-file "+(row.iconCls?row.iconCls:"")+"\"></span>");
}
}
cc.push("<span class=\"tree-title\">"+val+"</span>");
}else{
cc.push(val);
}
}
cc.push("</div>");
cc.push("</td>");
}
}
return cc.join("");
},refreshRow:function(_e0,id){
var row=$(_e0).treegrid("find",id);
var _e1=$.data(_e0,"treegrid").options;
var _e2=$(_e0).datagrid("getPanel").find("div.datagrid-body");
var _e3=_e1.rowStyler?_e1.rowStyler.call(_e0,row):"";
var _e4=_e3?"style=\""+_e3+"\"":"";
var tr=_e2.find("tr[node-id="+id+"]");
tr.attr("style",_e4);
tr.children("td").each(function(){
var _e5=$(this).find("div.datagrid-cell");
var _e6=$(this).attr("field");
var col=$(_e0).datagrid("getColumnOption",_e6);
if(col){
var _e7=col.styler?(col.styler(row[_e6],row)||""):"";
var _e8=col.hidden?"style=\"display:none;"+_e7+"\"":(_e7?"style=\""+_e7+"\"":"");
$(this).attr("style",_e8);
var val=null;
if(col.formatter){
val=col.formatter(row[_e6],row);
}else{
val=row[_e6]||"&nbsp;";
}
if(_e6==_e1.treeField){
_e5.children("span.tree-title").html(val);
var cls="tree-icon";
var _e9=_e5.children("span.tree-icon");
if(_e9.hasClass("tree-folder")){
cls+=" tree-folder";
}
if(_e9.hasClass("tree-folder-open")){
cls+=" tree-folder-open";
}
if(_e9.hasClass("tree-file")){
cls+=" tree-file";
}
if(row.iconCls){
cls+=" "+row.iconCls;
}
_e9.attr("class",cls);
}else{
_e5.html(val);
}
}
});
$(_e0).treegrid("fixRowHeight",id);
},onBeforeRender:function(_ea,_eb,_ec){
var _ed=$.data(_ea,"treegrid").options;
if(_ec.length==undefined){
if(_ec.footer){
$.data(_ea,"treegrid").footer=_ec.footer;
}
_ec=this.transfer(_ea,_eb,_ec.rows);
}else{
function _ee(_ef,_f0){
for(var i=0;i<_ef.length;i++){
var row=_ef[i];
row._parentId=_f0;
if(row.children&&row.children.length){
_ee(row.children,row[_ed.idField]);
}
}
};
_ee(_ec,_eb);
}
var _f1=_43(_ea,_eb);
if(_f1){
if(_f1.children){
_f1.children=_f1.children.concat(_ec);
}else{
_f1.children=_ec;
}
}else{
$.data(_ea,"treegrid").data=$.data(_ea,"treegrid").data.concat(_ec);
}
this.treeNodes=_ec;
this.treeLevel=$(_ea).treegrid("getLevel",_eb);
},transfer:function(_f2,_f3,_f4){
var _f5=$.data(_f2,"treegrid").options;
var _f6=[];
for(var i=0;i<_f4.length;i++){
_f6.push(_f4[i]);
}
var _f7=[];
for(var i=0;i<_f6.length;i++){
var row=_f6[i];
if(!_f3){
if(!row._parentId){
_f7.push(row);
_f6.remove(row);
i--;
}
}else{
if(row._parentId==_f3){
_f7.push(row);
_f6.remove(row);
i--;
}
}
}
var _f8=[];
for(var i=0;i<_f7.length;i++){
_f8.push(_f7[i]);
}
while(_f8.length){
var _f9=_f8.shift();
for(var i=0;i<_f6.length;i++){
var row=_f6[i];
if(row._parentId==_f9[_f5.idField]){
if(_f9.children){
_f9.children.push(row);
}else{
_f9.children=[row];
}
_f8.push(row);
_f6.remove(row);
i--;
}
}
}
return _f7;
}});
$.fn.treegrid.defaults=$.extend({},$.fn.datagrid.defaults,{treeField:null,animate:false,singleSelect:true,view:_c2,editConfig:{getTr:function(_fa,id){
return $(_fa).datagrid("getPanel").find("div.datagrid-body tr[node-id="+id+"]");
},getRow:function(_fb,id){
return $(_fb).treegrid("find",id);
}},onBeforeLoad:function(row,_fc){
},onLoadSuccess:function(row,_fd){
},onLoadError:function(){
},onBeforeCollapse:function(row){
},onCollapse:function(row){
},onBeforeExpand:function(row){
},onExpand:function(row){
},onClickRow:function(row){
},onDblClickRow:function(row){
},onContextMenu:function(e,row){
},onBeforeEdit:function(row){
},onAfterEdit:function(row,_fe){
},onCancelEdit:function(row){
}});
})(jQuery);

