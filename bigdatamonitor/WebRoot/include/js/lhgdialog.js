/*!
 * lhgcore Dialog Plugin v3.4.3
 * Date : 2011-05-31 09:35:11
 * Copyright (c) 2009 - 2011 By Li Hui Gang
 */

;(function(J){

var top = window, doc, cover, ZIndex,
    ie6 = (J.browser.msie && J.browser.version < 7) ? true : false,

iframeTpl = ie6 ? '<iframe hideFocus="true" ' + 
	'frameborder="0" src="about:blank" style="position:absolute;' +
	'z-index:-1;width:100%;height:100%;top:0px;left:0px;filter:' +
	'progid:DXImageTransform.Microsoft.Alpha(opacity=0)"><\/iframe>' : '',

compat = function( doc )
{
    doc = doc || document;
	return doc.compatMode == 'CSS1Compat' ? doc.documentElement : doc.body;
},

getZIndex = function()
{
    if( !ZIndex ) ZIndex = 1976;
	
	return ++ZIndex;
},

getScrSize = function()
{
	if( 'pageXOffset' in top )
	{
	    return {
		    x: top.pageXOffset || 0,
			y: top.pageYOffset || 0
		};
	}
	else
	{
	    var d = compat( doc );
		return {
		    x: d.scrollLeft || 0,
			y: d.scrollTop || 0
		};
	}
},

getDocSize = function()
{
	var d = compat( doc );
	
	return {
	    w: d.clientWidth || 0,
		h: d.clientHeight || 0
	};
},

getUrl = (function()
{
    var sc = document.getElementsByTagName('script'), bp = '',
	    i = 0, l = sc.length, re = /lhgdialog(?:\.min)?\.js/i;
	
	for( ; i < l; i++ )
	{
	    if( re.test(sc[i].src) )
		{
		    bp = !!document.querySelector ?
			    sc[i].src : sc[i].getAttribute('src',4);
			break;
		}
	}
	
	return bp.split('?');
})(),

getPath = getUrl[0].substr( 0, getUrl[0].lastIndexOf('/') + 1 ),

getArgs = function( name )
{
    if( getUrl[1] )
	{
	    var param = getUrl[1].split('&'), i = 0, l = param.length, aParam;
		
		for( ; i < l; i++ )
		{
		    aParam = param[i].split('=');
			
			if( name === aParam[0] ) return aParam[1];
		}
	}
	
	return null;
},

dgSkin = getArgs('s') || 'default',

reSizeHdl = function()
{
    var rel = compat( doc );
	
	J(cover).css({
	    width: Math.max( rel.scrollWidth, rel.clientWidth || 0 ) - 1 + 'px',
		height: Math.max( rel.scrollHeight, rel.clientHeight || 0 ) - 1 + 'px'
	});
};

while( top.parent && top.parent != top )
{
    try{
	    if( top.parent.document.domain != document.domain ) break;
	}catch(e){ break; }
	
	top = top.parent;
}

if( getArgs('t') === 'self' || top.document.getElementsByTagName('frameset').length > 0 )
    top = window;
	
doc = top.document;

try{
	doc.execCommand( 'BackgroundImageCache', false, true );
}catch(e){}

/*J('head',doc).append( '<link href="' + getPath + 'lhgdialog.css" rel="stylesheet" type="text/css"/>' );*/

J.fn.fixie6png = function()
{
    var els = J('*',this), bgIMG, pngPath;
	
	for( var i = 0, l = els.length; i < l; i++ )
	{
	    bgIMG = J(els[i]).css('ie6png');
		pngPath = getPath + bgIMG;
		
		if( bgIMG )
		{
			els[i].style.backgroundImage = 'none';
			els[i].runtimeStyle.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + pngPath + "',sizingMethod='scale')";
		}
	}
};

J.fn.dialog = function( opts )
{
    var dialog = false;
	
	if( this[0] )
	{
	    dialog = new J.dialog( opts );
		this.bind( 'click', dialog.ShowDialog );
	}
	
	return dialog;
};

J.dialog = function( opts )
{
    var S = this, loadObj, inboxObj, xbtnObj, dragObj, dropObj,
    	bodyObj, btnBarObj, regWindow, timer,
	
	opt = S.opt = J.extend({
		title: 'lhgdialog \u5F39\u51FA\u7A97\u53E3',
		cover: false,
		titleBar: true,
		btnBar: true,
		xButton:true,
		maxBtn:false,
		minBtn: false, // 暂时只提供个接口
		cancelBtn: true,
		width: 400,
		height: 300,
		id: 'lhgdgId',
		link: false,
		html: null,
		page: null,
		parent: null,
		dgOnLoad: null,
		onXclick: null,
		onCancel: null,
		onMinSize: null, // 暂时只提供这个最小化的接口
		fixed: false,
		top: 'center',
		left: 'center',
		drag: true,
		resize: true,
		autoSize: false,
		rang: false,
		timer: null,
		iconTitle: true,
		bgcolor: '#f6f6f6',
		opacity: 0.5,
		autoPos: false,
		autoCloseFn: null,
		cancelBtnTxt: '\u53D6\u6D88',
		loadingText: '\u7A97\u53E3\u5185\u5BB9\u52A0\u8F7D\u4E2D\uFF0C\u8BF7\u7A0D\u7B49...'
	}, opts || {} ),
	
	maxBtnObj, max = {}, maxed = false,
	minBtnObj,
	
	SetFixed = function( elem )
	{
		var style = elem.style,
			dd = compat( doc ),
			left = parseInt(style.left) - dd.scrollLeft,
			top = parseInt(style.top) - dd.scrollTop;
		
		style.removeExpression('left');
		style.removeExpression('top');
		
		style.setExpression( 'left', 'this.ownerDocument.documentElement.scrollLeft' + left );
		style.setExpression( 'top', 'this.ownerDocument.documentElement.scrollTop + ' + top );
	},
	
	SetIFramePage = function()
	{
	    var innerDoc, dialogTpl;
		
		if( opt.html )
		{
		    if( typeof opt.html === 'string' )
				innerDoc = '<div id="lhgdg_inbox_' + opt.id + '" class="lhgdg_inbox" style="display:none">' + opt.html + '</div>';
			else
				innerDoc = '<div id="lhgdg_inbox_' + opt.id + '" class="lhgdg_inbox" style="display:none"></div>';
		}
		else if( opt.page )
		{
		    innerDoc = '<iframe id="lhgfrm_' + opt.id + '" name="lhgfrm_'+opt.id+'" frameborder="0" src="' + opt.page + '" ' +
				       'scrolling="auto" style="display:none;width:100%;height:100%;"><\/iframe>';
		}
		
		dialogTpl = [
		    '<div id="lhgdlg_', opt.id, '" class="lhgdialog" style="width:', opt.width, 'px;height:', opt.height, 'px;">',
			    '<table border="0" cellspacing="0" cellpadding="0" width="100%">',
				'<tr>',
					'<td class="lhgdg_leftTop"></td>',
					'<td id="lhgdg_drag_', opt.id, '" class="lhgdg_top">', opt.titleBar ?
						('<div class="lhgdg_title_icon">' + (opt.iconTitle ? '<div class="lhgdg_icon"></div>' : '') +
						'<div class="lhgdg_title">' + opt.title + '</div>' +
						(opt.minBtn ? ('<a class="lhgdg_minbtn" id="lhgdg_minbtn_' + opt.id + '" href="javascript:void(0);" target="_self"></a>') : '') +
						(opt.maxBtn ? ('<a class="lhgdg_maxbtn" id="lhgdg_maxbtn_' + opt.id + '" href="javascript:void(0);" target="_self"></a>') : '') +
						(opt.xButton ? ('<a class="lhgdg_xbtn" id="lhgdg_xbtn_' + opt.id + '" href="javascript:void(0);" target="_self"></a>') : '') + '</div>') : '',
					'</td>',
					'<td class="lhgdg_rightTop"></td>',
				'</tr>',
				'<tr>',
					'<td class="lhgdg_left"></td>',
					'<td>',
						'<table border="0" cellspacing="0" cellpadding="0" width="100%">',
						'<tr>',
						    '<td id="lhgdg_content_', opt.id, '" style="background-color:#fff">',
							    innerDoc, '<div id="lhgdg_load_', opt.id, '" class="lhgdg_load"><span>', opt.loadingText, '</span></div>',
							'</td>',
						'</tr>',
						opt.btnBar ? ('<tr><td id="lhgdg_btnBar_' + opt.id + '" class="lhgdg_btnBar"><div class="lhgdg_btn_div"></div></td></tr>') : '',
						'</table>',
					'</td>',
					'<td class="lhgdg_right"></td>',
				'</tr>',
				'<tr>',
					'<td class="lhgdg_leftBottom"></td>',
					'<td class="lhgdg_bottom"></td>',
					'<td id="lhgdg_drop_', opt.id, '" class="lhgdg_rightBottom"></td>',
				'</tr>',
				'</table>', iframeTpl,
			'</div>'
		].join('');
		
		return dialogTpl;
	},
	
	ShowCover = function()
	{
	    cover = J('#lhgdgCover',doc)[0];
		
		if( !cover )
		{
			var html = '<div id="lhgdgCover" style="position:absolute;top:0px;left:0px;' +
					'background-color:' + opt.bgcolor + ';">' + iframeTpl + '</div>';
			
			cover = J(html,doc).css('opacity',opt.opacity).appendTo(doc.body)[0];
		}
		
		J(top).bind( 'resize', reSizeHdl );
		reSizeHdl();
		J(cover).css({ display: '', zIndex: getZIndex() });
	},
	
	iPos = function( dg, tp, lt, fix )
	{
	    var cS = getDocSize(),
		    sS = getScrSize(),
			dW = dg.offsetWidth,
			dH = dg.offsetHeight, x, y,
			lx, rx, cx, ty, by, cy;
		
		if( fix )
		{
			lx = ie6 ? sS.x : 0;
			rx = ie6 ? cS.w + sS.x - dW : cS.w - dW;
			cx = ie6 ? ( rx + sS.x - 20 ) / 2 : ( rx - 20 ) / 2;
			
			ty = ie6 ? sS.y : 0;
			by = ie6 ? cS.h + sS.y - dH : cS.h - dH;
			cy = ie6 ? ( by + sS.y - 20 ) / 2 : ( by - 20 ) / 2;
		}
		else
		{
			lx = sS.x;
			cx = sS.x + ( cS.w - dW - 20 ) / 2;
			rx = sS.x + cS.w - dW;
			
			ty = sS.y;
			cy = sS.y + ( cS.h - dH - 20 ) / 2;
			by = sS.y + cS.h - dH;
		}
		
		switch( lt )
		{
		    case 'center':
				x = cx;
				break;
			case 'left':
				x = lx;
				break;
			case 'right':
				x = rx;
				break;
			default:
			    if(fix && ie6) lt = lt + sS.x;
				x = lt; break;
		}
		
		switch( tp )
		{
		    case 'center':
				y = cy;
			    break;
			case 'top':
			    y = ty;
				break;
			case 'bottom':
			    y = by;
				break;
			default:
			    if(fix && ie6) tp = tp + sS.y;
				y = tp; break;
		}
		
		J(dg).css({ top: y + 'px', left: x + 'px' });
		
		if( fix && ie6 ) SetFixed( dg );
	},
	
	SetDialog = function( dg )
	{
	    S.curWin = window;
		S.curDoc = document;
		
		J(dg).bind('contextmenu',function(ev){
		    ev.preventDefault();
		}).bind( 'mousedown', setIndex );
		
		if( opt.html && opt.html.nodeType )
		{
		    J(inboxObj).append( opt.html );
			opt.html.style.display = '';
		}
		
		regWindow = [window];
		
		if( top != window )
		    regWindow.push( top );
		
		if( opt.page )
		{
		    S.dgFrm = J('#lhgfrm_'+opt.id,doc)[0];
			
		    if( !opt.link )
			{
			    S.dgWin = S.dgFrm.contentWindow;
				S.dgFrm.lhgDG = S;
			}
			
			J(S.dgFrm).bind('load',function(){
				this.style.display = 'block';
				
				if( !opt.link )
				{
				    var indw = J.browser.msie ?
					    S.dgWin.document : S.dgWin;
					
					J(indw).bind( 'mousedown', setIndex );
					
					regWindow.push( S.dgWin );
				    S.dgDoc = S.dgWin.document;
					
				    if( opt.autoSize ) autoSize();
				    J.isFunction( opt.dgOnLoad ) && opt.dgOnLoad.call( S );
				}
				
				loadObj.style.display = 'none';
			});
		}
		
		if( opt.xButton && opt.titleBar )
		    J(xbtnObj).bind( 'click', opt.onXclick || S.cancel );
		
		if( opt.maxBtn && opt.titleBar )
		{
		    J(maxBtnObj).bind( 'click', S.maxSize );
			J(dragObj).bind( 'dblclick', S.maxSize );
		}
		
		if( opt.minBtn && opt.titleBar && J.isFunction(opt.onMinSize) )
		    J(minBtnObj).bind( 'click', opt.onMinSize );
	},
	
	reContentSize = function( dg )
	{
	    var tH = dragObj.offsetHeight,
		    bH = dropObj.offsetHeight,
			nH = opt.btnBar ? btnBarObj.offsetHeight : 0,
			iH = parseInt(dg.style.height,10) - tH - bH - nH;
		
		loadObj.style.lineHeight = iH + 'px';
		bodyObj.style.height = iH + 'px';
	},
	
	autoSize = function()
	{
	    var tH = dragObj.offsetHeight,
		    bH = dropObj.offsetHeight,
			nH = opt.btnBar ? btnBarObj.offsetHeight : 0,
			bW = dropObj.offsetWidth * 2, sH, sW, comDoc;
	    
		if( opt.html )
		{
			sH = Math.max( inboxObj.scrollHeight, inboxObj.clientHeight || 0 );
			sW = Math.max( inboxObj.scrollWidth, inboxObj.clientWidth || 0 );
		}
		else if( opt.page && !opt.link )
		{
		    if( !S.dgDoc ) return;
			comDoc = compat( S.dgDoc );
			
			sH = Math.max( comDoc.scrollHeight, comDoc.clientHeight || 0 );
			sW = Math.max( comDoc.scrollWidth, comDoc.clientWidth || 0 );
		}
		
		sH = sH + tH + bH + nH;
		sW = sW + bW;
		
		S.reDialogSize( sW, sH ); iPos( S.dg, 'center', 'center', opt.fixed );
	},
	
	initDrag = function( elem )
	{
	    var lacoor, maxX, maxY, curpos, regw = regWindow, cS, sS;
		
		function moveHandler(ev)
		{
			var curcoor = { x: ev.screenX, y: ev.screenY };
		    curpos =
		    {
		        x: curpos.x + ( curcoor.x - lacoor.x ),
			    y: curpos.y + ( curcoor.y - lacoor.y )
		    };
			lacoor = curcoor;
			
			if( opt.rang )
			{
			    if( curpos.x < sS.x ) curpos.x = sS.x;
				if( curpos.y < sS.y ) curpos.y = sS.y;
				if( curpos.x > maxX ) curpos.x = maxX;
				if( curpos.y > maxY ) curpos.y = maxY;
			}
			
			S.dg.style.top = opt.fixed && !ie6 ? curpos.y - sS.y + 'px' : curpos.y + 'px';
			S.dg.style.left = opt.fixed && !ie6 ? curpos.x - sS.x + 'px' : curpos.x + 'px';
		};
		
		function upHandler(ev)
		{
			for( var i = 0, l = regw.length; i < l; i++ )
			{
				J( regw[i].document ).unbind( 'mousemove', moveHandler );
				J( regw[i].document ).unbind( 'mouseup', upHandler );
			}
			
			lacoor = null; elem = null;
			
			if( curpos.y < 0 ) S.dg.style.top = '0px';
			
			if( opt.fixed && ie6 ) SetFixed( S.dg );
		    if( J.browser.msie ) S.dg.releaseCapture();
		};
		
		J(elem).bind( 'mousedown', function(ev){
		    if( ev.target.id === 'lhgdg_xbtn_'+S.opt.id ) return;

			cS = getDocSize();
			sS = getScrSize();
			
			var lt = S.dg.offsetLeft,
			    tp = S.dg.offsetTop,
			    dW = S.dg.clientWidth,
			    dH = S.dg.clientHeight;
			
			curpos = opt.fixed && !ie6 ?
			    { x: lt + sS.x, y: tp + sS.y } : { x: lt, y: tp };
			
			lacoor = { x: ev.screenX, y: ev.screenY };
			
			maxX = cS.w + sS.x - dW;
			maxY = cS.h + sS.y - dH;
			
			S.dg.style.zIndex = parseInt( ZIndex, 10 ) + 1;
			
			for( var i = 0, l = regw.length; i < l; i++ )
			{
				J( regw[i].document ).bind( 'mousemove', moveHandler );
				J( regw[i].document ).bind( 'mouseup', upHandler );
			}
			
			ev.preventDefault();
			
			if( J.browser.msie ) S.dg.setCapture();
		});
	},
	
	initSize = function( elem )
	{
	    var lacoor, dH, dW, curpos, regw = regWindow, dialog, cS, sS;
		
		function moveHandler(ev)
		{
		    var curcoor = { x : ev.screenX, y : ev.screenY };
			dialog = {
				w: curcoor.x - lacoor.x,
				h: curcoor.y - lacoor.y
			};
			
			if( dialog.w < 200 ) dialog.w = 200;
			if( dialog.h < 100 ) dialog.h = 100;
			
			S.dg.style.top = opt.fixed ? curpos.y - sS.y + 'px' : curpos.y + 'px';
			S.dg.style.left = opt.fixed ? curpos.x - sS.x + 'px' : curpos.x + 'px';
			
			S.reDialogSize( dialog.w, dialog.h );
		};
		
		function upHandler(ev)
		{
			for( var i = 0, l = regw.length; i < l; i++ )
			{
			    J( regw[i].document ).unbind( 'mousemove', moveHandler );
				J( regw[i].document ).unbind( 'mouseup', upHandler );
			}
			
			lacoor = null; elem = null;
			
		    if( J.browser.msie ) S.dg.releaseCapture();
		};
	
	    J(elem).bind( 'mousedown', function(ev){
			dW = S.dg.clientWidth;
			dH = S.dg.clientHeight;
			
			dialog = { w: dW, h: dH };
			
			cS = getDocSize();
			sS = getScrSize();
			
			var lt = S.dg.offsetLeft,
			    tp = S.dg.offsetTop;
			
			curpos = opt.fixed ?
			    { x: lt + sS.x, y: tp + sS.y } : { x: lt, y: tp };
				
			lacoor = { x: ev.screenX - dW, y: ev.screenY - dH };
			
			S.dg.style.zIndex = parseInt( ZIndex, 10 ) + 1;
			
			for( var i = 0, l = regw.length; i < l; i++ )
			{
			    J( regw[i].document ).bind( 'mousemove', moveHandler );
				J( regw[i].document ).bind( 'mouseup', upHandler );
			}
			
			ev.preventDefault();
			
			if( J.browser.msie ) S.dg.setCapture();
		});
	},
	
	setIndex = function(ev)
	{
		S.dg.style.zIndex = parseInt(ZIndex,10) + 1;
		ZIndex = parseInt( S.dg.style.zIndex, 10 );
		
		ev.stopPropagation();
	},
	
	dgAutoPos = function()
	{
		iPos( S.dg, opt.autoPos.top, opt.autoPos.left, opt.fixed );
	},
	
	removeDG = function()
	{
		if( S.dgFrm )
		{
			if( !opt.link )
				J(S.dgFrm).unbind( 'load' );
				
			S.dgFrm.src = 'about:blank';
			S.dgFrm = null;
		}
		
		if( opt.html && opt.html.nodeType )
		{
		    J(S.curDoc).append( opt.html );
			opt.html.style.display = 'none';
		}
		
		if( opt.autoPos )
			J(top).unbind( 'resize', dgAutoPos );
		
		regWindow = [];
		J(S.dg).remove();
		
		S.dg = null; maxed = false; max = {};
		loadObj = inboxObj = xbtnObj = dragObj = dropObj = btnBarObj = bodyObj = maxBtnObj = minBtnObj = null;
	};
	
	S.ShowDialog = function()
	{
		if( J('#lhgdlg_'+opt.id,doc)[0] )
		    return S;
		
		if( opt.cover )
		    ShowCover();
			
		if( opt.fixed )
		{
		    opt.maxBtn = false;
			opt.minBtn = false;
		}
		
		var fixpos = opt.fixed && !ie6 ? 'fixed' : 'absolute',
		    html = SetIFramePage();
		
		S.dg = J(html,doc).css({
		    position: fixpos, zIndex: getZIndex()
		}).appendTo(doc.body)[0];		
		
		loadObj = J('#lhgdg_load_'+opt.id,doc)[0];
		inboxObj = J('#lhgdg_inbox_'+opt.id,doc)[0];
		xbtnObj = J('#lhgdg_xbtn_'+opt.id,doc)[0];
		dragObj = J('#lhgdg_drag_'+opt.id,doc)[0];
		dropObj = J('#lhgdg_drop_'+opt.id,doc)[0];
		btnBarObj = J('#lhgdg_btnBar_'+opt.id,doc)[0];
		bodyObj = J('#lhgdg_content_'+opt.id,doc)[0];
		maxBtnObj = J('#lhgdg_maxbtn_'+opt.id,doc)[0];
		minBtnObj = J('#lhgdg_minbtn_'+opt.id,doc)[0];
		
		reContentSize( S.dg );
		iPos( S.dg, opt.top, opt.left, opt.fixed );
		SetDialog( S.dg );
	
	    if( opt.drag )
		    initDrag( dragObj );
		else
		    dragObj.style.cursor = 'default';
		
		if( opt.resize )
		    initSize( dropObj );
		else
		    dropObj.style.cursor = 'default';
		
		if( ie6 && J(dropObj).css('ie6png') )
			J(S.dg).fixie6png();
		
		if( opt.btnBar && opt.cancelBtn )
		    S.addBtn( 'dgcancelBtn', opt.cancelBtnTxt, opt.onCancel || S.cancel );
		
		if( opt.html )
		{
		    loadObj.style.display = 'none';
			inboxObj.style.display = '';
			if( opt.autoSize ) autoSize();
		}
		
		if( opt.timer ) S.closeTime( opt.timer, opt.autoCloseFn );
		
		if( opt.html && J.isFunction(opt.dgOnLoad) ) opt.dgOnLoad.call( S );
		
		if( opt.autoPos ) J(top).bind( 'resize', dgAutoPos );
	};
	
	S.reDialogSize = function( width, height )
	{
		J(S.dg).css({
		    'width': width + 'px', 'height': height + 'px'
		});
		
		reContentSize( S.dg );
	};
	
	S.maxSize = function()
	{
	    var cS, sS;
		
		cS = getDocSize();
		sS = getScrSize();
		
		if( !maxed )
		{
		    max.dgW = S.dg.offsetWidth;
		    max.dgH = S.dg.offsetHeight;
		    max.dgT = S.dg.style.top;
		    max.dgL = S.dg.style.left;
		    
			J(S.dg).css({ top: sS.y + 'px', left: sS.x + 'px' });
			S.reDialogSize( cS.w, cS.h );
			
			J(maxBtnObj).addClass('lhgdg_rebtn').removeClass('lhgdg_maxbtn');
			
			if( opt.drag )
			    J(dragObj).unbind('mousedown').css('cursor','default');
			
			if( opt.resize )
			    J(dropObj).unbind('mousedown').css('cursor','default');
			
			maxed = true;
		}
		else
		{
		    S.reDialogSize( max.dgW, max.dgH );
			J(S.dg).css({ top: max.dgT, left: max.dgL });
			
			J(maxBtnObj).addClass('lhgdg_maxbtn').removeClass('lhgdg_rebtn');
			
			if( opt.drag )
			{
			    initDrag( dragObj );
				dragObj.style.cursor = 'move';
			}
			
			if( opt.resize )
			{
			    initSize( dropObj );
				dropObj.style.cursor = 'nw-resize';
			}
			
			maxed = false;
		}
	};
	
	S.SetMinBtn = function( fn )
	{
	    if( opt.minBtn && opt.titleBar )
		{
		    if( J.isFunction(fn) )
			    J(minBtnObj).unbind( 'click' ).bind( 'click', fn );
		}
	};
	
	S.addBtn = function( id, txt, fn, pos )
	{
	    pos = pos || 'left';
		
		if( opt.btnBar )
		{
			if( J('#lhgdg_'+opt.id+'_'+id,doc)[0] )
				J('#lhgdg_'+opt.id+'_'+id,doc).html( '<em>' + txt + '</em>' ).unbind('click').bind('click',fn);
			else
			{
				var html = '<a id="lhgdg_' + opt.id + '_' + id + '" class="lhgdg_button" href="javascript:void(0);" target="_self"><em>' + txt + '</em></a>',
					btn = J(html,doc).bind( 'click', fn )[0];
				
				if( pos === 'left' )
				    J('.lhgdg_btn_div',btnBarObj).prepend(btn);
				else
				    J('.lhgdg_btn_div',btnBarObj).append(btn);
			}
		}
	};
	
	S.removeBtn = function( id )
	{
	    if( J('#lhgdg_'+opt.id+'_'+id,doc)[0] )
		    J('#lhgdg_'+opt.id+'_'+id,doc).remove();
	};
	
	S.SetIndex = function()
	{
		S.dg.style.zIndex = parseInt(ZIndex,10) + 1;
		ZIndex = parseInt( S.dg.style.zIndex, 10 );
	};
	
	S.SetXbtn = function( fn, noShow )
	{
	    if( opt.xButton && opt.titleBar )
		{
		    if( J.isFunction(fn) )
			    J(xbtnObj).unbind( 'click' ).bind( 'click', fn );
			
			if( noShow )
			    xbtnObj.style.display = 'none';
			else
			    xbtnObj.style.display = '';
		}
	};
	
	S.SetTitle = function( txt )
	{
	    if( opt.titleBar && typeof txt === 'string' )
		    J('.lhgdg_title',S.dg).html( txt );
	};
	
	S.cancel = function()
	{
		removeDG();
		
		if( cover )
		{
		    if( opt.parent && opt.parent.opt.cover )
			{
			    var Index = opt.parent.dg.style.zIndex;
				cover.style.zIndex = parseInt(Index,10) - 1;
			}
			else
			    cover.style.display = 'none';
		}
	};
	
	S.cleanDialog = function()
	{
		if( S.dg )
		    removeDG();
		
		if( cover )
		{
		    J(cover).remove();
			cover = null;
		}
	};
	
	S.closeTime = function( second, fn )
	{
	    if( timer ) clearTimeout(timer);
		
		if( J.isFunction(fn) ) fn.call( S );
		
		if( second )
		    timer = setTimeout(function(){
				S.cancel();
				clearTimeout(timer);
			},1000 * second );
	};
	
	S.SetPosition = function( left, top )
	{
	    iPos( S.dg, top, left, opt.fixed );
	};
	
	S.iWin = function( id )
	{
		if( J('#lhgfrm_'+id,doc)[0] )
		    return J('#lhgfrm_'+id,doc)[0].contentWindow;
		
		return null;
	};
	
	S.iDoc = function( id )
	{
		if( J('#lhgfrm_'+id,doc)[0] )
		    return J('#lhgfrm_'+id,doc)[0].contentWindow.document;
		
		return null;
	};
	
	S.iDG = function( id )
	{
	    return doc.getElementById('lhgdlg_'+id) || null;
	};
	
	S.SetCancelBtn = function( txt, fn )
	{
	    fn = fn || S.cancel;
		
		if( J('#lhgdg_'+opt.id+'_dgcancelBtn',doc)[0] )
		    J('#lhgdg_'+opt.id+'_dgcancelBtn',doc).html( '<em>' + txt + '</em>' ).unbind('click').bind('click',fn);
	};
	
	J(window).bind( 'unload', S.cleanDialog );
};

J(function(){
	var lhgDY = setTimeout(function(){
	    new J.dialog({ id:'reLoadId', html:'lhgdialog', width:100, title:'reLoad', height:100, left:-9000, btnBar:false }).ShowDialog(); clearTimeout(lhgDY);
	}, 150);
});

})(window.lhgcore||window.jQuery);

function suitHeight(id){
	var iframe1=document.frames["lhgfrm_"+id];
    $("#lhgfrm_"+id).css("height",iframe1.document.body.scrollHeight);
	
   }
function opdg(text) {
	  
	  var dg = new $.dialog({ id:'opdg', title:'参数错误！', width:250, height:150, fixed:true, top:200, html:"<div style='text-align:center;padding-top:25px'>" + text + "</div>"}); 
	  dg.ShowDialog(); 
}

function opdgForOrder(text) {
	
	  var dg2 = new $.dialog({ id:'opdgForOrder', title:'出库状态报表', width:700, height:500, fixed:true, top:200, html:text});
	  dg2.ShowDialog(); 
}