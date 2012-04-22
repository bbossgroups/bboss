/**----------------------------------------------------------------------------------
*
* 外观div事件 js文件定义函数，实现对div内置的html元素宽度和高度的缩放功能
*
*----------------------------------------------------------------------------------*/
var z=1;
function win_doInit(idName){
    //var room = document.getElementById("win"+x);
    var room = document.getElementById(idName);
    room.x0=0;room.y0=0;
    room.x1=0;room.y1=0;
    room.w0=0;room.h0=0;
    room.state="normal";
    room.moveable=false;
    room.resizable=false;
    room.MoveTo=_win_doMoveTo;
    room.ResizeTo=_win_doResizeTo;
        
    room.onmousedown=_win_doMdown;
    room.onmouseup=_win_doMup;
    room.onmousemove=_win_doMmove;
    room.style.display="block";
    var w=room.clientWidth;
    var h=room.clientHeight;
    //alert(w+","+h);
    var l=200;
    var t=100;
    room.style.display="block";
    room.MoveTo(l,t);
    room.ResizeTo(w,h);
    
    room.style.zIndex=z;   
    z++;

   
}
function _win_doMdown(evt){
    var e=evt?evt:window.event;
    if(this.style.cursor!="default"&&this.state=="normal"){
         document.captureEvents?document.captureEvents("mousemove",this):this.setCapture();
         this.x0=e.clientX;
         this.y0=e.clientY;
                this.x1=parseInt(this.offsetLeft);
        this.y1=parseInt(this.offsetTop);
        this.w0=parseInt(this.offsetWidth);
        this.h0=parseInt(this.offsetHeight);
         this.resizable = true;
    }
}
function _win_doMup(evt){
    var e=evt?evt:window.event;
    if(this.resizable){
        document.releaseEvents?document.releaseEvents("mousemove",this):this.releaseCapture();
            this.resizable = false;
    }
}    
function _win_doMmove(evt){
    var e=evt?evt:window.event;
    if(this.resizable){
        var xxx=this.style.cursor.substring(0,2).match(/[we]/); 
        var yyy=this.style.cursor.substring(0,2).match(/[ns]/);
        l=this.offsetLeft;
        t=this.offsetTop;
        w=parseInt(this.style.width);
        h=parseInt(this.style.height);
        if(xxx=="w"){
            l=this.x1+e.clientX - this.x0;
            w=this.w0+this.x0-e.clientX;
            if(l<0){w+=l;l=0;}
            if(w<100){l=l+w-100;w=100;}
        }
        if(xxx=="e"){
            w=this.w0+e.clientX-this.x0;
            w=w<100?100:w;
        }
        if(yyy=="n"){
            t=this.y1+e.clientY - this.y0;
            h=this.h0+this.y0-e.clientY;
            if(t<0){h+=t;t=0;}
            if(h<20){t=t+h-20;h=20;}
        }
        if(yyy=="s"){
            h=this.h0+e.clientY-this.y0;
            h=h<20?20:h;
        }
        this.MoveTo(l,t);
        this.ResizeTo(w,h);
        
        return(true);
    }
    if(this.state=="normal"){
        var cc="";
        x=window.getRealLeft(this);
        //y=window.getRealTop(this);
        w=parseInt(this.offsetWidth);
        h=parseInt(this.offsetHeight);
        //if(e.clientY-y<10)cc+="n";
        //if(y+h-e.clientY<10)cc+="s";
        if(e.clientX-x<10)cc+="w";
        if(x+w-e.clientX<10)cc+="e";
        if(cc!=""){
            this.style.cursor=cc+"-resize";
            return(true);
        }
        if(this.style.cursor!="default"){
            this.style.cursor="default";
        }
    }
}
function _win_doMoveTo(x,y){
    x=isNaN(x)?0:parseInt(x);
    y=isNaN(y)?0:parseInt(y);
    x=x<0?0:x;
    y=y<0?0:y;
    this.style.left=x+"px";
    this.style.top=y+"px";
}
function _win_doResizeTo(w,h){
    w=isNaN(w)?100:parseInt(w);
    h=isNaN(h)?20:parseInt(h);
    w=w<100?100:w;
    h=h<20?20:h;
    
    this.style.width=w+"px";
    this.style.height=h+"px";
}
function getRealLeft(o){
    var l=o.offsetLeft-o.scrollLeft;
    while(o=o.offsetParent){
        l+=o.offsetLeft-o.scrollLeft;
    }
    return(l);
}
function getRealTop(o){
    var t=o.offsetTop-o.scrollTop;
    while(o=o.offsetParent){
        t+=o.offsetTop-o.scrollTop;
    }
    return(t);
}

function loadDragDiv(){
	var divs = document.body.getElementsByTagName("div");
	for(i=0;i<divs.length;i++){
		var s= divs[i].id.split("_");
		if(s[0]!=null && s[0]=="dd")
			win_doInit(divs[i].id);
	}
}