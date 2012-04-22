var layerTop=0;       //菜单顶边距
var layerLeft=0;      //菜单左边距
var layerWidth=234;    //菜单总宽度
var contentHeight; //内容区高度
var stepNo=10;         //移动步数，数值越大移动越慢
var titleHeight=stepNo*2;    //标题栏高度


var toItemIndex;
var onItemIndex;
var oldClickedItem= -1;
var isMoving=0;
var copyrightHeight=0;

var itemNodes = new Array; //菜单项数组
var itemNo=0;			//菜单计数器

function itemNode(itemId,itemTitle,action,isClicked)
{	
	this.itemId=itemId;
	this.itemTitle=itemTitle;
	this.action=action;
	this.isClicked=isClicked;
}

function init(n)			//建立一个名为"itemsLayer"的层，用它限制所有菜单的显示范围：
{
	document.write('<div id="itemsLayer" '+
		'style="position:absolute;overflow:hidden;'+
		'border:1px solid #1954BD;left:'+layerLeft+';top:'+
		layerTop+';width:'+layerWidth+';">');
	copyrightHeight=stepNo*3+document.body.offsetHeight%stepNo;
	contentHeight=document.body.offsetHeight - n*titleHeight-copyrightHeight;
}

function finishIt()
{
	document.write('</div>');   //结束"itemsLayer"层
	//计算"itemsLayer"层的高度：
	itemsLayer.style.height = itemNo*titleHeight+contentHeight+2;
	var copyrightTop=itemsLayer.style.height;
	
	document.write('<div style="position:absolute;top:'+copyrightTop+';left:0;width:'+(layerWidth)+';'+
	';color:white;"><iframe name=copyright width='+(layerWidth)+
	' height='+copyrightHeight+' border=0 frameborder=no scrolling=no hidefocus=true></iframe></div>');
	var doc;
	doc=copyright.window.document;
	doc.write('<html><body bgcolor=#1954BD leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" oncontextmenu="return false" onselectstart="return false" ondragstart="return false">'+
	'<img style="cursor:default;" src="../images/itemBottom.gif" width=100% height=100% border=0>'+
	'<span style="position:absolute;font-size:9pt;COLOR: #FFE074;top:2;left:0;border:0;width:100%;height:100%;text-align:center;cursor:default;'+
	'vertical-align:center;FILTER:DropShadow(Color=#001E88, OffX=-1, OffY=1, Positive=1);">&#169;版权所有&nbsp;2003<br>ChinaCreator</span></body></html>');	

	doc.close();
}

function addItem(itemId,itemTitle,action)  //这个函数准备接受菜单标题和内容的写入
{
	itemNodes[itemNodes.length] = new itemNode(itemId,itemTitle,action,0);

	itemHTML='<div id=item'+itemNo+' itemIndex='+itemNo+
	' style="position:relative;left:0;top:-'+(contentHeight*itemNo)+
	';width:'+layerWidth+';z-index:'+(itemNo+1)+';background-color:#B9CEF0">'+
    '<table width=100% cellspacing="0" cellpadding="0" border=0>'+
    '<tr><td height='+titleHeight+' align=center>'+
	'<iframe scrolling=no STYLE="background-color: #B9CEF0" allowTransparency="true" FRAMEBORDER="no" width='+(layerWidth-1)+' height=100% border=0 name=title_'+itemNo+' hidefocus=true></iframe>'+
	'</td></tr><tr><td height='+contentHeight+' class="contentStyle">'+
    '<iframe class="contentdiv1" scrolling=auto STYLE="background-color: #B9CEF0" allowTransparency="true" FRAMEBORDER="no" border=0 name=content_'+itemNo+' hidefocus=true></iframe></td></tr></table></div>';
	document.write(itemHTML);
	var doc;
	eval('doc=title_'+itemNo+'.window.document');
	doc.write('<html><body bgcolor=#B9CEF0 leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" oncontextmenu="return false" onselectstart="return false" ondragstart="return false">'+
	'<img style="cursor:hand;" onclick="parent.clickItem('+itemNo+')" src="../images/item.gif" width=100% height=100% border=0>'+
	'<span onclick="parent.clickItem('+itemNo+')" style="position:absolute;font-size:10pt;COLOR: #ffffff;top:4;left:0;border:0;width:100%;height:100%;text-align:center;cursor:hand;'+
	'vertical-align:center;FILTER:DropShadow(Color=#001E88, OffX=-1, OffY=1, Positive=1);">'+itemTitle+'</span></body></html>');
	
	itemNo++;

	toItemIndex=itemNo-1;
	onItemIndex=itemNo-1;
}


//编写点击标题时移动相应的层：
//初始化变量"toItemIndex"和"onItemIndex"，它们分别用于记录"应该显示的层"和"现在显示的层":

var runtimes=0;  //"runtimes"用于记录层移动次数
//菜单标题被点击时调用这个函数：
function clickItem(itemIndex)
{
	if(itemNodes[itemIndex].isClicked == 0)
	{
		itemNodes[itemIndex].isClicked = 1;
		var FrameName='window.frames['+(itemIndex*2+1)+']';
		//var url = itemNodes[itemIndex].action+"&FrameName="+FrameName;
		var url = itemNodes[itemIndex].action;
		eval(FrameName+'.location.href="'+url+'"');
	}

	if(oldClickedItem==itemIndex)
		return;
	
	if(!isMoving)
	{
		changeItem(itemIndex);
		oldClickedItem=itemIndex;
	}
}

function changeItem(clickItemIndex)
{
	isMoving=1;
	//判断相应的层应上移还是下移：
	toItemIndex=clickItemIndex;
	if(toItemIndex-onItemIndex>0) 
		moveUp();
	else 
		moveDown();
    //一定的时间间隔后继续移动，直到移了设定的步数stepNo:
   	runtimes++;
   	if(runtimes>=stepNo)
	{
     	onItemIndex=toItemIndex;
     	runtimes=0;
		isMoving=0;
	}
   	else
     	setTimeout("changeItem(toItemIndex)",20);
}
//相应菜单上移：
function moveUp()
{
	//alert(onItemIndex+';'+toItemIndex);
	//判断应一起上移的菜单，并让它(们)每次移动contentHeight/stepNo的距离：
	for(i=onItemIndex+1;i<=toItemIndex;i++)
    	eval('document.all.item'+i+'.style.top=	parseInt(document.all.item'+i+'.style.top)-contentHeight/stepNo;');
}
  //相应菜单下移：
function moveDown()
{
	//alert(onItemIndex+';'+toItemIndex);
	for(i=onItemIndex;i>toItemIndex;i--)
    	eval('document.all.item'+i+'.style.top=	parseInt(document.all.item'+i+'.style.top)+contentHeight/stepNo;');
}