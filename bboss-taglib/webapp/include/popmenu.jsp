<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<% String rootpath = request.getContextPath();%>
<script language="javascript">
//标识是否是树的右键菜单，如果是树则为true，否则为false
//var istree = false;


//建立菜单与显示的HTML之间的关系
var _MenuCache_ = 
{
	m_Count : 0,
	m_IdPrefix : "_MenuCache_",
	m_ActiveItem : null,
	
	NewId : function ()
	{
		return this.m_IdPrefix + this.m_Count++;
	},
	
	Remove : function (mi)
	{
		delete this[mi.m_Id];
	}
	
}

//var styleURL_blue = "popmenu_blue.css";                               //样式
var imgURL_blue = "<%=rootpath%>/include/menu_mouse_dis_blue.gif";                      //分割线

var styleURL = "<%=rootpath%>/include/popmenu_window.css";                     //样式,提供popmenu_window.css,popmenu_blue.css两个样式
var imgURL_window = "<%=rootpath%>/include/menu_mouse_dis_window.jpg";           //分割线
var menuWidth = 130;//菜单整体宽度

function Menu() 
{ 
    this.m_Items = [];                  //菜单条目集合 
    this.m_Popup = null;                //显示菜单的popup窗口 
    this.m_Invalidate = false;          //是否失效标志 
    this.m_Drawn = false;               //菜单是否已输出 
    this.m_Opener = null;               //菜单的父窗口window对象 
    this.m_ParentMenu = null;           //菜单的父菜单MenuItem对象 
    this.m_ActiveItem = null;           //被激活(highlighting)的MenuItem 
    this.m_ShowTimer = null;            //鼠标停留在有子菜单的条目上,子菜单显示延迟计时器 
    this.m_Bounds = null;               //菜单的bounds 
    this.m_ShowHeaderBlank = true;      //是否显示MenuItem前的空白区域 
    this.m_IsEventAttached = false;     //事件是否attached
	this.path = "parent";				//父级路径
	this.m_Id = "";
	this.m_SeparatorAmount = 0;         //Separator的数量
    //this.m_Id = _MenuCache_.NewId();    //或取菜单对象的唯一标识
    //_MenuCache_[this.m_Id] = this;     //把菜单放入__MenuCache__ 
 
    this.toString = function() 
    { 
        return '[class Menu]'; 
    }; 
}

//菜单类的方法有： 
Menu.prototype.BuildMenu = function(identity) 
{ 
    // 添加菜单条目到菜单中
	this.m_Id = identity;
	_MenuCache_[this.m_Id] = this;
}; 
//菜单类的方法有： 
Menu.prototype.Add = function(mi) 
{ 
    // 添加菜单条目到菜单中
	this.m_Items.push(mi);
	mi.m_Menu = this;
}; 
 
Menu.prototype.AddAt = function(mi, index) 
{ 
    // 把菜单条目添加到指定的数组索引上
	this.m_Items[index] = mi;
}; 
 
Menu.prototype.AddSeparator = function() 
{ 
    // 添加一个Separator Item,就是我们在window菜单里的"-";
	var m_Id = m_Id = _MenuCache_.NewId();
	_MenuCache_[m_Id] = new MenuItem(null,null,null,null,null,false);
	_MenuCache_[this.m_Id].Add(_MenuCache_[m_Id]);
	this.m_SeparatorAmount++;
}; 
 
Menu.prototype.Remove = function(mi) 
{ 
    // 删除菜单中的一个菜单条目 
}; 
 
Menu.prototype.Contains = function(menu) 
{ 
    // 判断已构建的菜单中是否已包含了menu 
}; 
 
Menu.prototype.Render = function() 
{ 
    // 生成菜单UI显示需要的DHTML
	var outerHTML = "";
	var menuObj = this;
	outerHTML += "<html>\n<body scroll=no >\n<LINK href=\""+styleURL+"\" type=text/css rel=StyleSheet />\n";
	outerHTML += "<table id=\""+menuObj.m_Id+"\" class='Pop_Table' cellpadding='0' cellspacing='0' >\n";
	for (var i=0;i<menuObj.m_Items.length;i++)
	{
		var mi = menuObj.m_Items[i];
		outerHTML += mi.Render();
	}
	outerHTML += "</table>\n</body>\n</html>";
	
	return outerHTML;
}; 
 
Menu.prototype.__generatePaddingTR = function(doc) 
{ 
    // 为了美化菜单UI生成的一个TR element 
}; 
 
Menu.prototype.AttachEvents = function(menuHtml) 
{ 
    //attach事件处理函数到菜单事件上
	var menuObj = _MenuCache_[menuHtml.id];
    if ( menuObj.m_IsEventAttached ) 
    { 
        return; 
    } 
    var doc = menuObj.m_Popup.document;
	var path = menuObj.findPath(menuObj,"");
	menuObj.path = path;
    for ( var i=0 ; i < menuObj.m_Items.length ; ++i ) 
    { 
        var mItem = menuObj.m_Items[i];
        var trItem = doc.getElementById(mItem.m_Id);
		if ( mItem.m_Action && mItem.m_ChildMenu == null )
		{
			if (!mItem.IsSeparator())
			{
				if (isJavsscriptFun(mItem.m_Action))
				{
					var prex = mItem.m_Action.substring(0,11);
   
				    if(prex ==  "javascript:")
				    {
				    	
					    mItem.m_Action = mItem.m_Action.substring(11,mItem.m_Action.length);
					   
					}
					trItem.attachEvent('onclick',new Function(path+mItem.m_Action+";"+path+"closeAllPopup()"));
					//trItem.attachEvent('onmouseup',new Function(path+"closeAllPopup()"));
				}
				else
				{
					trItem.attachEvent('onclick',new Function(path+"Loade(\""+mItem.m_Id+"\")"+";"+path+"closeAllPopup()"));
					//trItem.attachEvent('onmouseup',new Function(path+"closeAllPopup()"));
					//trItem.attachEvent('onclick',new Function(path+"Loade(\""+mItem.m_Id+"\")"));
				}
			}
		}
		if(!mItem.IsSeparator())
		{
			//alert(document.body.outerHTML)
			trItem.attachEvent('onmouseover', new Function (path+"Menu.prototype.InnerShow('"+mItem.m_Id+"')"));
			trItem.attachEvent('onmouseout', new Function (path+"Menu.prototype.ResumeItem('"+mItem.m_Id+"')"));
		}
        //if ( !mItem.IsSeparator() ) 
        //{ 
            //trItem.attachEvent('onmouseover', this.ActiveItem); 
        //}
    }
    //if ( doc ) 
    //{ 
        //doc.attachEvent('onkeydown', this.Keydown); 
    //} 
    menuObj.m_IsEventAttached = true; 
}; 
 
Menu.prototype.ActiveItem = function(evt) 
{ 
    // 处理菜单itme被Active后的UI和动作等 
}; 
 
Menu.prototype.Hide = function() 
{ 
    // 隐藏菜单
	if (this.m_Popup != null && this.m_Popup != "" && this.m_Popup.hide)
	{
		this.m_Popup.hide();
	}
}; 
 
Menu.prototype.Keydown = function(evt) 
{ 
    // 处理键盘按键 
}; 
 
Menu.prototype.ResumeItem = function(id) 
{ 
    // 恢复菜单,取消active和恢复UI
	// 显示submenu,用于菜单内部触发的菜单显示 
	var mItem = _MenuCache_[id];
	var doc = mItem.m_Menu.m_Popup.document;
	var trHtml = doc.getElementById(id);
	var tdHtml = trHtml.childNodes[1];
	

	if (mItem.m_ChildMenu == null || !mItem.m_ChildMenu.m_Popup.isOpen)
	{
		trHtml.className = "Pop_TrOut";
	}
	
	//alert(tdHtml.innerHTML)
	//trHtml.style.setAttribute("cursor","default");
	//trHtml.style.setAttribute("background","white");
}; 
 
Menu.prototype.__resumeItem = function() 
{ 
    // 执行UI恢复
}; 
 
Menu.prototype.__resumeAll = function() 
{ 
    // 执行批量UI恢复
	for (var i=0; i<this.m_Items.length; i++)
	{
		var mi = this.m_Items[i];
		var trHtml = this.m_Popup.document.getElementById(mi.m_Id);
		if(trHtml.name=="hr") continue;
		trHtml.className = "Pop_TrOut";
	}
}; 
 
Menu.prototype.__activeItem = function() 
{ 
    // 执行UI激活 
}; 
 
Menu.prototype.HasSubMenuExpanded = function() 
{ 
    // 判断菜单是否有展开的submenu
}; 
 
Menu.prototype.__isEllipsis = function(menuObj, menuHtml) 
{ 
    // 在菜单item的text过长时将截断并显示""
}; 
 
Menu.prototype.Show = function(win) 
{
	
    if ( !win ) 
    { 
        return; 
    } 
    var menuObj = this; 
    menuObj.m_Opener = win; 
    menuObj.__resumeItem(); 
    var win = menuObj.m_Opener; 
    var popup, popwin, popdoc; 
    // 判断菜单的容器popup是否建立 
    if ( !menuObj.m_Popup ) 
    { 
        popup = win.createPopup();
        menuObj.m_Popup = popup; 
    } 
    else 
    { 
        popup = menuObj.m_Popup;
        menuObj.__resumeAll();
    } 
    popdoc = popup.document; 
    popwin = popdoc.parentWindow; 
    // 判断是否需要重绘菜单的内容 
    if ( menuObj.m_Invalidate || !menuObj.m_Drawn ) 
    {
        popdoc.body.innerHTML = menuObj.Render();
        menuObj.m_Invalidate = false; 
        menuObj.m_Drawn = true; 
    } 
    // 获取菜单的主table(菜单是使用table来实现的) 
    var menuHtml = popup.document.getElementById(menuObj.m_Id);
    var hrAmount = menuObj.m_SeparatorAmount;
    var hrHeight = 4;
    var w = widthByString(menuObj);
    var h = 20*(menuObj.m_Items.length-hrAmount)+hrHeight*hrAmount+3;
    //var x = win.event.clientX + win.screenLeft; 
    //var y = win.event.clientY + win.screenTop;
    var x = win.event.screenX; 
    var y = win.event.screenY;
    popup.show(x, y, w, h);
    
    menuObj.m_Bounds =  
    { 
        top: x, left: y, 
        width: menuHtml.offsetWidth, 
        height: menuHtml.offsetHeight 
    };
    // 把菜单操作的事件attach到菜单上，鼠标和键盘操作等
    menuObj.AttachEvents(menuHtml);
    
}; 
 
Menu.prototype.InnerShow = function(id) 
{
    // 显示submenu,用于菜单内部触发的菜单显示 
	var mItem = _MenuCache_[id];
	var doc = mItem.m_Menu.m_Popup.document;
	var trHtml = doc.getElementById(id);
	var tdHtml = trHtml.childNodes[1];
	trHtml.className = "Pop_TrOver";

	if (mItem.m_ChildMenu != null && mItem.m_ChildMenu != "")
	{
		var tdHtml = trHtml.getElementsByTagName("td")[1];
		tdHtml.style.setAttribute("cursor","default");
		Menu.prototype.__show(mItem);
	}
	else
	{
		for (var i=0; i<mItem.m_Menu.m_Items.length; i++)
		{
			var mi = mItem.m_Menu.m_Items[i];
			if (mi.m_Id == mItem.m_Id)
			{
				continue ;
			}
			if(mi.m_ChildMenu != null && mi.m_ChildMenu.m_Popup != null)
			{
				mi.m_ChildMenu.m_Popup.hide();
				Menu.prototype.ResumeItem (mi.m_Id);
			}
		}
	}
}; 
 
Menu.prototype.__show = function(mItem) 
{ 
    // 执行菜单显示
	var win = mItem.m_Menu.m_Popup.document.parentWindow;
	var m_Bounds = mItem.m_Menu.m_Bounds;
	var trHtml = win.document.getElementById(mItem.m_Id);
	if ( !win ) 
    { 
        return; 
    }
	var menuObj = mItem.m_ChildMenu;
    menuObj.m_Opener = win; 
    menuObj.__resumeItem(); 
    var win = menuObj.m_Opener; 
    var popup, popwin, popdoc; 
    // 判断菜单的容器popup是否建立 
    if ( !menuObj.m_Popup ) 
    { 
        popup = win.createPopup();
        menuObj.m_Popup = popup; 
    } 
    else 
    { 
        popup = menuObj.m_Popup;
        menuObj.__resumeAll();
    } 
    popdoc = popup.document; 
    popwin = popdoc.parentWindow; 
    // 判断是否需要重绘菜单的内容 
    if ( menuObj.m_Invalidate || !menuObj.m_Drawn ) 
    {
        popdoc.body.innerHTML = menuObj.Render();
        menuObj.m_Invalidate = false; 
        menuObj.m_Drawn = true;
    }
    // 获取菜单的主table(菜单是使用table来实现的)
   var menuHtml = popup.document.getElementById(menuObj.m_Id);
   var hrAmount = menuObj.m_SeparatorAmount;
   var hrHeight = 4;
   var x = widthByString(mItem.m_Menu) + win.screenLeft;
   var y = trHtml.offsetTop + win.screenTop;
   var w = widthByString(menuObj);
   var h = 20*(menuObj.m_Items.length-hrAmount)+hrHeight*hrAmount+3;
   popup.show(x, y, w, h);
   menuObj.m_Bounds =  
   { 
       top: x, left: y, 
       width: menuHtml.offsetWidth,
       height: menuHtml.offsetHeight 
   };  
   // 把菜单操作的事件attach到菜单上，鼠标和键盘操作等
   menuObj.AttachEvents(menuHtml);
}; 
 
Menu.prototype.FadeinEffect = function(effect) 
{ 
    // 菜单显示式的特效,是用filter来实现,只在Show菜的时候调用 
}; 


//类MenuItem比Menu类简单很多，定义如下： 
function MenuItem(text, action, icon, shortcut, menu, Disabled) 
{
	//菜单文本
    this.m_Text = text;
	//if (text != null && text != "")
	//{
		//this.m_Text = text.len(5); 
	//}
    this.m_Action = action;            //菜单条目被触发时执行的函数 
    this.m_Icon = icon;                //菜单条目前的图标路径 
    this.m_ChildMenu = menu;           //子菜单,类型为Menu对象 
    this.m_Menu = null;                //本菜单条目所在的菜单对象实例 
    this.m_ShortCut = shortcut;        //快捷方式(保留,未实现) 
    this.m_Disabled = Disabled;       
    this.m_Mnemonic = null; 
    this.m_Tooltip = null; 
    this.m_Attributes = [];            // 附加属性集合,由SetAttribute设置 
	this.m_Frame = null;
	this.m_RightIcon = "<%=rootpath%>/include/leftbtn.png";
    
    this.m_Id = _MenuCache_.NewId(); 
    _MenuCache_[this.m_Id] = this; 
 
    this.toString = function() 
    { 
        return '[class MenuItem]'; 
    }; 
} 

//MenuItem类的方法如下： 
MenuItem.prototype.Contains = function(menu) 
{ 
    //子菜单中是否已添加menu
	
};
 
MenuItem.prototype.SetAttribute = function(key, value) 
{ 
    // 设置用户定义的属性 
}; 
 
MenuItem.prototype.GetAttribute = function(key) 
{ 
    // 或取用户定义的属性 
}; 
 
MenuItem.prototype.Invalidate = function() 
{ 
    // 失效 
}; 

MenuItem.prototype.IsSeparator = function() 
{ 
    // 判断MenuItem是否为Separator,就是其m_Text == '-'
	var mi = this;
	if (mi.m_Text == null && mi.m_Action == null && mi.m_Icon == null)
		return true ;
	else return false ;
} 
 
MenuItem.prototype.Render = function() 
{ 
    // 生成菜单UI的DHTML
	var outerHTML = "";
	var mi = this;
	if (mi.m_Text == null && mi.m_Action == null && mi.m_Icon == null)
	{
		var imgWidth = widthByString(mi.m_Menu);
		imgWidth = imgWidth-imgWidth*0.09*2;
		outerHTML += "<tr ";
		if(mi.m_Disabled){
			outerHTML += " disabled='true' ";
		}
		outerHTML += " id='"+mi.m_Id+"' name='hr'><td align='center' colspan='3' class='dis'><img width='"+imgWidth+"px' src='"+imgURL_window+"'></td></tr>";
	}
	else
	{
		outerHTML += "<tr ";
		if(mi.m_Disabled){
			outerHTML += " disabled='true' ";
		}
		outerHTML += " class='Pop_Tr' id='"+mi.m_Id+"'>\n<td align='center' width='5%'>";
		if(mi.m_Icon != null && mi.m_Icon != "" && mi.m_Icon != undefined && mi.m_Icon != "null")
		{
			outerHTML += "<img src=\""+mi.m_Icon+"\"></td>\n";
		}
		else
		{
			outerHTML += "&nbsp;</td>\n";
		}
		outerHTML += "<td align='left' style=\"cursor:hand\" >";
		outerHTML += "&nbsp;"+mi.m_Text;
		outerHTML += "</td>\n<td align='left' width='5%'>";
		if (mi.m_ChildMenu != null && mi.m_ChildMenu != "")
		{
			outerHTML += "<img src=\""+mi.m_RightIcon+"\">";
		}
		outerHTML += "</td>\n</tr>\n";
	}
	return outerHTML;
}; 
 
MenuItem.prototype.SetBorderColor = function(miHtml, width, borderColor) 
{ 
    // 设置菜单条目的边框颜色 
};
//实现页面跳转
var Loade = function (id)
{
	var mItem = _MenuCache_[id];
	if( mItem == null )
	{
		alert("错误：没有找到该菜单项");
		return ;
	}
	
	if(mItem.m_Frame == null || mItem.m_Frame.src == "" || mItem.m_Frame.src == undefined)
	{
		mItem.m_Frame = MenuItem.prototype.Click(mItem,"",mItem.m_ShortCut,mItem.m_Action);
	}
	else
	{
		mItem.m_Frame.src = mItem.m_Action;
	}
	closeAllPopup();
}
//关闭所有菜单
var closeAllPopup = function ()
{
	for(obj in _MenuCache_)
	{
		if(obj != null && _MenuCache_[obj] != null && _MenuCache_[obj].m_Popup != null)
			_MenuCache_[obj].m_Popup.hide();
		
	}
}

function findBridge(win)
{
	menu_bridge;
	alert("bridge:" + bridge);
	if(bridge)
	{

		findBridge(win.opener());
	}
	return bridge;
}
//实现页面跳转
MenuItem.prototype.Click = function(mItem,path,tag,src)
{
	var win = "";
	var fra = "";
	src = refactorLink(src,_node_params);
	menu_bridge.href = src;
	menu_bridge.target = tag;
	
	menu_bridge.click();
	return;
}

//找出顶级页面
Menu.prototype.findPath = function (menuObj,path)
{
	var win = "";
	var fra = "";
	if (menuObj.m_ParentMenu != null && menuObj.m_ParentMenu != undefined)
	{
		path += "parent.";
		this.findPath(menuObj.m_ParentMenu,path);
		return path;
	}
	else
	{
		return path;
	}
}

/**
 * 判断当前的地址是否是javascript函数
 */
function isJavsscriptFun(linkUrl)
{
	var regex = "\s*javascript:.+";
	re = new RegExp("\s*javascript:.+","i");  
	return linkUrl.match(re);
}

//展开/折叠
function expandNode(node)
{
	document.getElementById(node).click();
}
//根据字符串长度确定窗体宽
function widthByString(menu)
{
	var mis = menu.m_Items;
	var maxSize = 0;
	for(var i=0;i<mis.length;i++)
	{
		
		var str = mis[i].m_Text;
		var size = 0;
		if(str != "" && str != null)
			size = str.length;
		if(maxSize < size) maxSize = size;
	}
	if((maxSize-6)*13 <= 0)
		return 120;
	else
		return 120+(maxSize-6)*13;
}

//截取字符串
String.prototype.len=function(size)   
{   
	
	if (this.replace(/[^\x00-\xff]/g,".").length > size*2)
	{
		return this.slice(0,size)+"...";
	}
	else
	{
		return this;
	}
	
} 

function show(id)
{
	
	_MenuCache_[id].Show(window);
}



function reappendMenus(ids)
{
	if(true)
	{
		return ;
	}
	if(!ids)
		return ;
	
	if(!ids.length)
	{
		if(ids != null && ids != "")
		{
			var obj = document.getElementById(ids);
			alert(obj);
			var menu = _MenuCache_[ids];
			if(menu != undefined && menu != null && menu != "" && menu.m_Id != null && menu.m_Id != "")
			{
				var id = menu.m_Id;
				obj.attachEvent('oncontextmenu',new Function("show('"+id+"');return false;"));
			}
		}
	}
	else
	{
		for(var i=0;i<ids.length;i++)
		{
			if(ids[i] != null && ids[i] != "")
			{
				var obj = document.getElementById(ids[i]);
				alert("obj.id:" + obj.id);
				alert("obj.length:"+obj.length);
				var menu = _MenuCache_[ids[i]];
				alert("menu:" + menu);
				alert("menu.m_Id:" + menu.m_Id);
				if(menu != undefined && menu != null && menu != "" && menu.m_Id != null && menu.m_Id != "")
				{
					var id = menu.m_Id;
					
					
					obj.attachEvent('oncontextmenu',new Function("show('"+id+"');return false;"));
				}
			}
		}
	}
}



document.oncontextmenu = InitializedDocEvent;
var _node_params ;
function InitializedDocEvent()
{
   
    var obj = event.srcElement;
    var t_id = obj.id;
   
  	_node_params = obj.params;
    if(t_id)
    {
    	
    	
    	var menu = _MenuCache_[t_id];
   
    	if(menu != undefined && menu != null && menu != "" && menu.m_Id != null && menu.m_Id != "")
		{
			var id = menu.m_Id;
			show(id);
		}
    }
    
	return false;
	
	
}


/**
 * 为连接添加参数，主要是树节点右键点击链接的添加参数
 */
function refactorLink(linkUrl,params)
{
	
	if(linkUrl == null || params == null || params == "")
		return linkUrl;
   
	var idx = linkUrl.indexOf("?");
	if(idx == -1)
		linkUrl += "?" + params;
	else
		linkUrl += "&" + params;
	return linkUrl;
}

</script>