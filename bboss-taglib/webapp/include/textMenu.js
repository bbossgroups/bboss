
//---------------------------------------------->
// 		[弹出式功能菜单]
//		作者：biaoping.yin
////---------------------------------------------->
var MenuCount=0;
var MenuCollections = new Array();
var MenuItemCount = 0;
var MenuItemCollections = new Array();
var MenuItemCloneNode = null;
var MainMenu = null;
var st_winscroll;
var fadeInst;
var startFadeInst;
var oFadeIn;
var Timer = 20;   
var alphaIn = 60; 
var cloneOut = 30;
var cmdResponseTime = 800; 

var collections = new Array();


window.onload = windowOnload;



function windowOnload()
{	
  
    InitializedDocEvent();	
	    
}

//---------------------------------
//	初始化事件
//---------------------------------
function InitializedDocEvent()
{
    
    document.onclick = documentOnclick;
    
	document.oncontextmenu = documentOnContextMenu;
	
	
}
//
function documentOnclick()
{
	setMenuClick(event.srcElement);	
}


function documentOnContextMenu()
{	
    
	setMenuContextMenu(event.srcElement,event.x,event.y);
	
	return false;
}

function MenuItem(label,jscmd,icon)
{	
    
	this.id = MenuItemCount++;
	this.stringId = "MenuItem" + this.id;
	this.jsCmd = jscmd;
	this.item = null;
	this.haveChildMenu = false;
	this.childMenu = null;
	this.parentMenu = null;
	this.tagName = "MENUITEM";
	
	this.addMenu = addMenu;
	this.onMouseOver= MenuItemMouseOver;
	this.onMouseOut = MenuItemMouseOut;
	this.runJsCmd = runJsCmd;
	this.runJs = runJs;
	this.dispose = MenuItemDispose;
	MenuItemCollections[this.id] = this;
	
	oMenuItem = document.createElement("TABLE");
	oMenuItem.id = this.stringId;
	oMenuItem.cellPadding = oMenuItem.cellSpacing = 0;
	oMenuItem.style.width = "100%";
	oMenuItemRow = oMenuItem.insertRow();
       	oMenuItemRow.className = "MenuItem";
	oMenuItemIconCell = oMenuItemRow.insertCell();
	oMenuItemIconCell.style.pixelWidth =22;
	oMenuItemIconCell.style.padding = "0px 0px 0px 3px";
	if(icon != null)
	{
		ItemIcon = document.createElement("IMG");ItemIcon.src = icon;ItemIcon.align="absmiddle";oMenuItemIconCell.appendChild(ItemIcon);			
	}		
	oMenuItemLabelCell = oMenuItemRow.insertCell();
	oMenuItemLabelCell.innerText = label;
	oMenuItemLabelCell.style.padding = "0px";
	
	oMenuItemArrowCell = oMenuItemRow.insertCell();
	oMenuItemArrowCell.style.padding = "0px";	
	with(oMenuItemArrowCell)
	{	
		align="right";style.pixelWidth = "16";style.font = "12px webdings";		
	}
	
	oMenuItemRow.attachEvent("onmouseover",this.onMouseOver);
	oMenuItemRow.attachEvent("onmouseout",this.onMouseOut);
	oMenuItemRow.attachEvent("onclick",this.runJsCmd);
	
	this.item = oMenuItem;	
}



function addMenuItem(menuitem)
{
	this.MenuItems[this.ItemCount++] = menuitem;
	this.selfMenu.appendChild(menuitem.item);
	menuitem.parentMenu = this;
}

function addSeperate()
{
	for(var times=0;times < 2;times++)
	{
		thread = document.createElement("TABLE");
		thread.cellPadding=0;
		thread.cellSpacing=0;
		thread.className=(times == 0)?"MenuSeparatorUp":"MenuSeparatorDown";
		thread.style.width="100%";
		if(times == 0)
		{	thread.style.marginTop = "4px";		}
		else if(times == 1)
		{	
			thread.style.marginBottom = "4px";	
		}
		this.selfMenu.appendChild(thread);
		thread.insertRow().insertCell();
	}
}

function showMenu(MousePosX,MousePosY)
{
	this.MenuBody.style.display="block";
	
	if(MainMenu == this)  
	{		
		var MenuPosY = MousePosY + this.MenuBody.offsetHeight;
		if(MenuPosY < (document.body.clientHeight))
		{
			this.MenuBody.style.top = document.body.scrollTop + MousePosY;
		}
		else
		{
			this.MenuBody.style.top = document.body.scrollTop +MousePosY - this.MenuBody.offsetHeight;
		}
		
		var MenuPosX = MousePosX + this.MenuBody.offsetWidth;
		if(MenuPosX < (document.body.clientWidth))
		{
			this.MenuBody.style.left = document.body.scrollLeft + MousePosX;
		}
		else
		{
			this.MenuBody.style.left = document.body.scrollLeft + MousePosX - this.MenuBody.offsetWidth;
		}
		oFadeIn = this;
		oFadeIn.fadeIn();		
		return;		
	}
	
	this.setZIndex(this.parentMenuItem.parentMenu.zIndex + 2)
	
	var MenuPosY = this.parentMenuItem.parentMenu.MenuBody.offsetTop + this.parentMenuItem.item.offsetTop + this.MenuBody.offsetHeight;	
	if(MenuPosY < (document.body.scrollTop + document.body.clientHeight))
	{	
		this.MenuBody.style.top = this.parentMenuItem.parentMenu.MenuBody.offsetTop + this.parentMenuItem.item.offsetTop -2;
	}
	else
	{
		this.MenuBody.style.top = this.parentMenuItem.parentMenu.MenuBody.offsetTop + this.parentMenuItem.item.offsetTop - this.MenuBody.offsetHeight + this.parentMenuItem.item.offsetHeight + 5;
	}
	
	var MenuPosX =this.parentMenuItem.parentMenu.MenuBody.offsetLeft + this.parentMenuItem.item.offsetWidth + this.MenuBody.offsetWidth;
	if(MenuPosX < (document.body.scrollLeft + document.body.clientWidth))
	{
		this.MenuBody.style.left = this.parentMenuItem.parentMenu.MenuBody.offsetLeft + this.parentMenuItem.parentMenu.MenuBody.offsetWidth - 5;
	}
	else
	{
		this.MenuBody.style.left = this.parentMenuItem.parentMenu.MenuBody.offsetLeft - this.MenuBody.offsetWidth + 5;
	}	
	oFadeIn = this;
	strtFadeInst = setTimeout("oFadeIn.fadeIn()",300);
}

function menuFadeIn()
{	
	if((oFadeIn.MenuBody.filters.alpha.opacity += alphaIn)<100)	
	{
		if(oFadeIn.MenuBody.filters.alpha.opacity>30&&oFadeIn.menuShadow.style.display == "none")
		{
			oFadeIn.setShadow();
		}
		fadeInst = setTimeout("oFadeIn.fadeIn()",Timer);			
	}
	else
	{
		oFadeIn.setShadow();
		clearTimeout(fadeInst);

	}
}

function setZIndex(value)
{
	this.zIndex = value;
	this.MenuBody.style.zIndex = value;
}

function createShadow()
{

	try
	{
		MenuShadow = document.createElement("TABLE");
		MenuShadow.className = "MenuShadow";
		MenuShadow.insertRow().insertCell();
		document.body.appendChild(MenuShadow);
		this.menuShadow = MenuShadow;
	}
	catch(e)
	{	}
}

function setShadow()
{
	if(navigator.userAgent.indexOf("MSIE 6.0") == -1)
	return;
	try
	{
		this.menuShadow.style.top = this.MenuBody.offsetTop + 4;
		this.menuShadow.style.left = this.MenuBody.offsetLeft +4;
		this.menuShadow.style.width = this.MenuBody.offsetWidth - 7;
		this.menuShadow.style.height = this.MenuBody.offsetHeight - 7;
		this.menuShadow.style.zIndex = this.zIndex - 1;
		this.menuShadow.style.display = "block";
	}
	catch(e)
	{
		this.menuShadow.style.display = "none";
	}
}

function unactiveChildMenu()
{
	for(var item in this.MenuItems)
	{
		if(this.MenuItems[item].childMenu != null)
		{
			if(this.MenuItems[item].childMenu.MenuBody.style.display != "none")
			{				
				this.MenuItems[item].childMenu.MenuBody.style.display = "none";
				this.MenuItems[item].childMenu.MenuBody.filters.alpha.opacity = 0;
				if(this.MenuItems[item].childMenu.menuShadow != null)
				this.MenuItems[item].childMenu.menuShadow.style.display = "none";
				var ChildMenuItem = this.MenuItems[item].childMenu.MenuItems;
				for(var cmi in ChildMenuItem)
				{
					ChildMenuItem[cmi].item.rows[0].className= "MenuItem";
				}
				this.MenuItems[item].childMenu.unactiveChildMenu();
			}
		}
	}
} 

function MenuDispose()
{
	try
	{
		this.MenuBody.removeNode(true);
		this.menuShadow.removeNode(true);
	}
	catch(e)
	{	}
}

function addMenu(childMenu)
{
	if(this.parentMenu == null)
	{
		alert("菜单条目还没有被添加到菜单.");
		return false;
	}
	else if(this.parentMenu.stringId == childMenu.stringId)
	{
		alert("菜单条目的父菜单与子菜单相同.");
		return false;
	}
	this.item.rows[0].detachEvent("onclick",this.runJsCmd);
	this.haveChildMenu = true;
	this.childMenu = childMenu;
	this.item.rows[0].cells[2].innerText = "4";
	childMenu.parentMenuItem = this;
}

function MenuItemMouseOver()
{	
	
	var Pcell = event.srcElement.parentElement;
	if(event.srcElement.tagName == "IMG")  
	{
		Pcell = Pcell.parentElement;
	}
	Pcell.className = "MenuItemOver"; 	
	var MenuItemLink = MenuItemCollections[parseInt(Pcell.parentElement.parentElement.id.substr(8))];
	var childMenuItems = MenuItemLink.childMenu;	
	
	if(MenuItemLink.childMenu != null)
	{	
		MenuItemLink.childMenu.unactiveChildMenu();
		for(var cmi in MenuItemLink.childMenu.MenuItems)
		{
			MenuItemLink.childMenu.MenuItems[cmi].item.rows[0].className = "MenuItem";
		}
		
		MenuItemLink.parentMenu.MenuBody.filters.alpha.opacity=100;
		if(MenuItemLink.childMenu.MenuBody.style.display == "block")
		{
			return;
		}
	}
	
	var ContainerMenu = MenuItemLink.parentMenu;
	ContainerMenu.unactiveChildMenu();  
	
	for(var subitem in ContainerMenu.MenuItems)
	{
		if(ContainerMenu.MenuItems[subitem] == MenuItemLink)
		{
			continue;
		}
		ContainerMenu.MenuItems[subitem].item.rows[0].className = "MenuItem";
	}

	if(MenuItemLink.childMenu != null&&MenuItemLink.childMenu.MenuBody.style.display == "none")
	{	
		MenuItemLink.childMenu.showMenu();			
	}
}

function MenuItemMouseOut()
{
	var Pcell = event.srcElement.parentElement;
	if(event.srcElement.tagName == "IMG")
	{
		Pcell = Pcell.parentElement;
	}
	MenuItemLink = MenuItemCollections[parseInt(Pcell.parentElement.parentElement.id.substr(8))];
	if(MenuItemLink.childMenu != null)
	{
		if(MenuItemLink.childMenu.MenuBody.style.display == "none")
		{
			return;	
		}	
	}
	if(MenuItemLink.childMenu == null)	
	{
		Pcell.className = "MenuItem";
	}
	else if(MenuItemLink.childMenu.MenuBody.style.display == "none")
	{
		Pcell.className = "MenuItem";
	}
	else 
	{	
		Pcell.className = "MEnuItemOver";
	}
}

function runJsCmd()
{	
	var Pcell = event.srcElement.parentElement;
	if(event.srcElement.tagName == "IMG")  
	{
		Pcell = Pcell.parentElement;
	}
	var MenuItemLink = MenuItemCollections[parseInt(Pcell.parentElement.parentElement.id.substr(8))];
	
	try
	{
		var cloneNode = document.createElement("TABLE");
		document.body.appendChild(cloneNode);
		cloneNode.className = "MenuItemOver";
		cloneNode.cellPadding = cloneNode.cellSpacing = 0;
		cloneNode.style.position = "absolute";
		nodeRow = cloneNode.insertRow(0);
		nodeRow.className = "MenuItemOver";
	
		IconCell = nodeRow.insertCell();
		IconCell.innerHTML=MenuItemLink.item.rows[0].cells[0].innerHTML;
		IconCell.style.pixelWidth = 22;
		IconCell.style.padding = "0px 0px 0px 3px";

		LabelCell = nodeRow.insertCell();
		LabelCell.innerHTML = MenuItemLink.item.rows[0].cells[1].innerHTML;
		LabelCell.style.pixelWidth = MenuItemLink.item.rows[0].cells[1].offsetWidth;
		LabelCell.style.padding = "0px";

		ArrowCell = nodeRow.insertCell();
		ArrowCell.align = "right";
		ArrowCell.style.font="11.5px webdings";
		ArrowCell.innerHTML= MenuItemLink.item.rows[0].cells[2].innerText;
		ArrowCell.style.padding= "0px";
		ArrowCell.style.pixelWidth = MenuItemLink.item.rows[0].cells[2].offsetWidth;

		cloneNode.style.left = MenuItemLink.parentMenu.MenuBody.offsetLeft + 4;
		cloneNode.style.top = MenuItemLink.parentMenu.MenuBody.offsetTop + MenuItemLink.item.offsetTop +2;
		MenuItemCloneNode = null;		
		MenuItemCloneNode = cloneNode;
	}
	catch(e)
	{
		MenuItemCloneNode = null;
	}
	finally
	{
		fadeOutCloneNode()
		if(MenuItemLink.jsCmd != null)
		{
			setTimeout("MenuItemLink.runJs()",cmdResponseTime);
		}
	}
}

function MenuItemDispose()
{
	try
	{
		this.item.removeNode(true);
	}
	catch(e)
	{	}
}

function runJs()
{
	try
	{
		eval(this.jsCmd);
	}
	catch(e)
	{
		alert("Error Command:\n\""+this.jsCmd+"\".\n"+e.description);
	}
}

function resetall()
{
	
	clearTimeout(fadeInst);
	clearTimeout(startFadeInst);

	for(rseti=0;rseti<MenuItemCount;rseti++)
	{
		MenuItemCollections[rseti].item.rows[0].className="MenuItem";
	}
	for(r=0;r<MenuCount;r++)
	{
		MenuCollections[r].MenuBody.filters.alpha.opacity = 0;
		MenuCollections[r].MenuBody.style.display = "none";
		MenuCollections[r].menuShadow.style.display = "none";;
	}

}

function disposeAll()
{
	clearTimeout(fadeInst);
	clearTimeout(startFadeInst);
	MainMenu = null;
	MenuItemCloneNode = null;
	MenuCount = 0;
	MenuItemCount = 0;

	try
	{
		for(var item in MenuItemCollections)
		{
			MenuItemCollections[item].dispose();
			MenuItemCollections[item] = null;
		}
		for(var menu in MenuCollections)
		{
			MenuCollections[menu].dispose();
			MenuCollections[menu] = null;
		}
	}
	catch(e)
	{}
}
	

function fadeOutCloneNode()
{
	
	if(MenuItemCloneNode == null)
	{
		return false;
	}
	if((MenuItemCloneNode.filters.alpha.opacity -= cloneOut) > 0)
	{
		setTimeout("fadeOutCloneNode()",Timer);
	}
	else
	{
		MenuItemCloneNode.removeNode(true);
		MenuItemCloneNode = null;
	}
}

function setMainMenu(menu)
{
	if(menu.parentMenuItem == null)
	{
		MainMenu = menu;
		menu.setZIndex(100);
	}
	else
	{
		alert("Menu父对象不为空.无法成为主菜单");
	}
}

function setMenuClick(srcElement)
{
	try
	{
	     	var Pcell=srcElement.parentElement.parentElement.parentElement;
		if(srcElement.tagName == "IMG")
		{
			Pcell = Pcell.parentElement;
		}
       		MenuItemLink = MenuItemCollections[parseInt(Pcell.id.substr(8))];
		if(MenuItemLink.tagName == "MENUITEM"&&MenuItemLink.childMenu != null)
		{
			return false;
		}
	}
	catch(e)
	{	
		resetall();
		return false;	
	}
		resetall();
}

function setMenuContextMenu(srcElement,posX,posY)
{	
        
    
    	try
    	{
    	    var Pcell=srcElement.parentElement.parentElement.parentElement;
    		if(srcElement.tagName == "IMG")
    		{
    			Pcell = Pcell.parentElement;
    		}
           	MenuItemLink = MenuItemCollections[parseInt(Pcell.id.substr(8))];
    		if(MenuItemLink.tagName == "MENUITEM")
    		{
    			return;
    		}
    	}
    	catch(e)
    	{

    		resetall();
    		disposeAll();
    		//init();
    		InitializedTextMenu(srcElement);
    		if(MainMenu)
    			MainMenu.showMenu(posX,posY);
    		return;
    	}
    
    	
    	resetall();
    	disposeAll();
    	//init();
    	InitializedTextMenu(srcElement);
    	
    	if(MainMenu)
    		MainMenu.showMenu(posX,posY);	
   
}

function window.onscroll()
{
	if(MainMenu == null)
		return;
	if(MainMenu.MenuBody.style.display == "none")
		return;
	resetall();
}


function InitializedTextMenu(srcElement)
{
    var id = srcElement.id;
    var params = srcElement.params;
	if(id == null || id == "")
	    return null;
	var menu = getContextMenuByID(id);		
	if(menu != null)
	{
	    var Menu1 = buildMenu(menu,params);       	
		setMainMenu(Menu1);
    }
}
/**
 * 右键菜单点击事件
 */
function menuclick(link,target)
{
    if(link == 'null')
        return;
    menu_bridge.href = link;
    menu_bridge.target = target;
    menu_bridge.click();
}


function textMenu()
{	
    
    	this.id       = MenuCount++;
    	this.stringId = "textMenu" + this.id;
    	this.selfMenu = null;
    	this.parentMenuItem = null;
    	this.MenuBody = null;
    	this.ItemCount = 0;
    	this.MenuItems = new Array();
    	this.zIndex = 0;
    	this.menuShadow = null;
    	MenuCollections[this.id] = this;
    	
    	this.addMenuItem = addMenuItem
    	this.addSeperate = addSeperate;
    	this.showMenu = showMenu;
    	this.fadeIn = menuFadeIn;
    	this.setZIndex = setZIndex;
    	this.createShadow = createShadow;
    	this.setShadow = setShadow;
    	this.unactiveChildMenu = unactiveChildMenu;
    	this.dispose = MenuDispose;
    	
    	oMenu = document.createElement("TABLE"); 
    	oMenu.id=this.stringId;
    	oMenu.cellPadding = 0;
    	oMenu.cellSpacing = 0;
    	oMenu.className = "Menuoutline";
    	oMenuRow = oMenu.insertRow();
    	oMenuCell= oMenuRow.insertCell();
    	oMenuCell.className = "Menuinline";
    	document.body.appendChild(oMenu);    
    	
    	this.MenuBody = oMenu;
   
    	this.selfMenu = oMenuCell;
    	this.createShadow();
	
}
/**
 * 为连接添加参数
 */
function refactorLink(link,params)
{
	
	if(link == null || params == null || params == "")
		return link;

	var idx = link.indexOf("?");
	if(idx == -1)
		link += "?" + params;
	else
		link += "&" + params;
	return link;
}
function buildMenu(menu,params)
{

   		var Menu1=new textMenu();
   		var menuitems = menu.getItems();

   		for(var i = 0; i < menuitems.length; i ++)
   		{
   		    var menuitem = menuitems[i];
   		    if(!menuitem.seperate)
   		    {
   		
       		    var t_menuitem = new MenuItem(menuitem.label,"menuclick('" + refactorLink(menuitem.link,params) + "','" + menuitem.target + "')",menuitem.icon);
       	
       		    Menu1.addMenuItem(t_menuitem);
       		    if(menuitem.hasSubMenu())
       		    {
       		        var tempMenu = buildMenu(menuitem.submenu,params);
       		        t_menuitem.addMenu(tempMenu);
       		    }        		
        	}
        	else
        	{
        	    Menu1.addSeperate();
        	}
    	}
		return Menu1;
	
}


function getContextMenuByID(identity)
{
    for(var i = 0; i < collections.length; i ++)	
    {
        
        if(collections[i].identity == identity)
            return collections[i];  
    }
    return null;
}

/**
 *  构建菜单信息缓冲区
 *
 */
function Menu(identity)
{
    
    this.collectionItems = new Array();
    this.identity = identity;
    this.addItem = addItem;
    this.addMenuSeperate = addMenuSeperate;

    this.getItems = getItems;
}


function buildItem(label,link,target,icon)
{
    var item = new Item(false,label,link,target,icon);
    return item;
}


function buildSeperate()
{
    var item = new Item(true);
    return item;
}
function addItem(item)
{    
    this.collectionItems.push(item);
}
function addMenuSeperate()
{
    this.collectionItems.push(buildSeperate());
}


function Item(seperate,label,link,target,icon)
{
    
    this.seperate = seperate;
    this.label = label;
    this.link = link;
    this.target = target;
    this.icon = icon;  
    this.submenu = null;  
    
    this.addSubMenu = addSubMenu;
    this.hasSubMenu = hasSubMenu;
}

function addSubMenu(menu)
{
    this.submenu = menu;
}

function hasSubMenu()
{
    return !(this.submenu == null);
}


function getItems()
{
    return this.collectionItems;
}



function init()
{
    try
    {
        var menu = new Menu("firstmenu");
        menu.addItem(buildItem("我的数码故事"));
            
    		menu.addMenuSeperate();
    		menu.addItem(buildItem("我的数码故事","http://www.cookieszone.com","location","sharefiles/logoff.gif"));
    		//Menu1.addMenuItem(new MenuItem("酷客地带首页","location.href='http://www.cookieszone.com'","sharefiles/logoff.gif"));
    		
    		menu.addItem(buildItem("酷客论坛","http://www.cookieszone.com","location","sharefiles/logoff.gif"));
    		menu.addItem(buildItem("FLASH文化","http://www.cookieszone.com/culture/cookiesflash.asp","location","sharefiles/logoff.gif"));
    		
    		menu.addItem(buildItem("photoshop设计","http://www.cookieszone.com/designsky/designsky.asp?boardid=301","location","sharefiles/logoff.gif"));
    		menu.addMenuSeperate();
    		menu.addItem(buildItem("个人作品","http://www.cookieszone.com/aboutus/works.asp","location"));
    		var menu1 = new Menu("secondmenu_1");
    		
    		menu1.addItem(buildItem("最新数码","http://www.cookieszone.com/digitalstory/list.asp?boardid=501","location","sharefiles/logoff.gif"));
    		menu1.addItem(buildItem("数码知识库","http://www.cookieszone.com/digitalstory/list.asp?boardid=502","location","sharefiles/logoff.gif"));
    		menu.collectionItems[0].addSubMenu(menu1);
    		collections.push(menu);
    	
    		
    		var t_Menu1 = new Menu("secondmenu");
    
    		t_Menu1.addItem(buildItem("我的数码故事1"));
    		t_Menu1.addMenuSeperate();
    		t_Menu1.addItem(buildItem("酷客地带首页1","http://www.cookieszone.com","location","sharefiles/logoff.gif"));
    		t_Menu1.addItem(buildItem("酷客论坛1","http://www.cookieszone.com/bbs","location","sharefiles/logoff.gif"));
    		t_Menu1.addItem(buildItem("FLASH文化1","http://www.cookieszone.com/culture/cookiesflash.asp","location","sharefiles/logoff.gif"));
    		t_Menu1.addItem(buildItem("photoshop设计1","http://www.cookieszone.com/designsky/designsky.asp?boardid=301","location","sharefiles/logoff.gif"));
    		t_Menu1.addMenuSeperate();
    		t_Menu1.addItem(buildItem("个人作品1","http://www.cookieszone.com/aboutus/works.asp","location"));
    		t_Menu2 = new Menu("secondmenu_i");
    
    		t_Menu2.addItem(buildItem("最新数码1","http://www.cookieszone.com/digitalstory/list.asp?boardid=501","location","sharefiles/logoff.gif"));
    		t_Menu2.addItem(buildItem("数码知识库1","http://www.cookieszone.com/digitalstory/list.asp?boardid=502","location","sharefiles/logoff.gif"));
    		t_Menu1.collectionItems[0].addSubMenu(t_Menu2);
    		collections.push(t_Menu1);
	}
	catch(e)
	{
	    alert("error:" + e.description);
	}
	
}		
init();