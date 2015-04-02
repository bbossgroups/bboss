(function($) {	
	//提示信息
    $.fn.aToolTip = function(options) {    
    	var defaults = {
    		clickIt: false,
    		closeTipBtn: 'aToolTipCloseBtn',
    		fixed: false,
    		inSpeed: 400,
    		outSpeed: 100,
    		tipContent: '',
    		toolTipClass: 'aToolTip',
    		xOffset: 5,
    		yOffset: 5
    	},
    
    	// This makes it so the users custom options overrides the default ones
    	settings = $.extend({}, defaults, options);
    
		return this.each(function() {
			var obj = $(this);
			// Decide weather to use a title attr as the tooltip content
			if(obj.attr('title')){
				// set the tooltip content/text to be the obj title attribute
				var tipContent = obj.attr('title');	 
			} else {
				// if no title attribute set it to the tipContent option in settings
				var tipContent = settings.tipContent;
			}
			
			// check if obj has a title attribute and if click feature is off
			if(tipContent && !settings.clickIt){	
				// Activate on hover	
				obj.hover(function(el){
					obj.attr({title: ''});						  
					$('body').append("<div class='"+ settings.toolTipClass +"'><p class='aToolTipContent'>"+ tipContent +"</p></div>");
					var leftwidth=obj.offset().left + obj.outerWidth() + settings.xOffset;
					$('.' + settings.toolTipClass).css({
						position: 'absolute',
						display: 'none',
						zIndex: '5000',
						clear:'both',
						top: (obj.offset().top - $('.' + settings.toolTipClass).outerHeight() - settings.yOffset) + 'px'					
						})
						.stop().fadeIn(settings.inSpeed);
					if(obj.offset().left + 2*obj.outerWidth() + settings.xOffset>$(document.body).width()){						
						$('.' + settings.toolTipClass).css({						
						right: $(document.body).width()-obj.offset().left + settings.xOffset + 'px'
						})
					}
					else{
						$('.' + settings.toolTipClass).css({						
						left: leftwidth + 'px'
						})						
					}
			    },
				function(){ 
					// Fade out
					//$('.' + settings.toolTipClass).stop().fadeOut(settings.outSpeed, function(){$(this).remove();});
					$('.' + settings.toolTipClass).stop().remove();
			    });	
		    }
		    
		    // Follow mouse if fixed is false and click is false
		    if(!settings.fixed && !settings.clickIt){
				obj.mousemove(function(el){
					$('.' + settings.toolTipClass).css({
						top: (el.pageY - $('.' + settings.toolTipClass).outerHeight() - settings.yOffset),
						left: (el.pageX + settings.xOffset)
					})
				});			
			} 		    
		    
		    // check if click feature is enabled
		    if(tipContent && settings.clickIt){
				// Activate on click	
				obj.click(function(el){
					obj.attr({title: ''});						  
					$('body').append("<div class='"+ settings.toolTipClass +"'><p class='aToolTipContent'>"+ tipContent +"</p></div>");
					$('.' + settings.toolTipClass).append("<a class='"+ settings.closeTipBtn +"' href='#' alt='close'>close</a>");
					$('.' + settings.toolTipClass).css({
						position: 'absolute',
						display: 'none',
						zIndex: '50000',
						top: (obj.offset().top - $('.' + settings.toolTipClass).outerHeight() - settings.yOffset) + 'px',
						left: (obj.offset().left + obj.outerWidth() + settings.xOffset) + 'px'
					})
					.fadeIn(settings.inSpeed);	
					// Click to close tooltip
					$('.' + settings.closeTipBtn).click(function(){
						$('.' + settings.toolTipClass).fadeOut(settings.outSpeed, function(){$(this).remove();});
						return false;
					});		 
					return false;			
			    });
		    }
		  
		}); // END: return this
		
		// returns the jQuery object to allow for chainability.  
        return this;
    };
	
})(jQuery);

$(function(){
	loadjs();	
}); 

function  loadjs(){
	/*$('a.normalTip').aToolTip();*/		
	if($('.toolTip').length > 0)
	{
		$('.toolTip').aToolTip({
				fixed: true
			});
	}
	/*$('a.clickTip').aToolTip({
			clickIt: true,
			tipContent: 'Hello I am aToolTip with content from the "tipContent" param'
		});	*/	
	//表格隔行、滑过、点击变色
	$("#changeColor").each(function(){
		$(this).find('tr:even').addClass("tr_gray"); 
		$(this).find('tr:odd').addClass("tr_white"); 
		$(this).find('tr').mouseover(function(){
			$(this).addClass("tr_highlight");									  
		}).mouseout(function(){
			$(this).removeClass("tr_highlight");		
			});
		$(this).find('tr').click(
				 function() {$(this).toggleClass('tr_selected');});	
	});	
	if(window.parent.frames["rightFrame"]){window.parent.loadFrame("rightFrame");}
	if(window.top.frames["indexFrame"]){window.top.loadFrame("indexFrame");}
	//$("fieldset.collapsible").collapse();
	//$("fieldset.startClosed").collapse( { closed: true } );
}


function closeDlg(){
	this.frameElement.api.close();
}
//标签页效果
function setTab(m,n,options){

 var lit = document.getElementById("menu"+m).getElementsByTagName("a");
 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
 for (i=0;i<lit.length;i++){
	 
	 lit[i].className=i==n?"current":"";
	 
	 if(options )
	 {
		 
		 		
		 if(options.framesrc && options.framesrc != "")
		 {
			
			if(document.getElementById(options.frameid).src == null ||  document.getElementById(options.frameid).src == "")
			 {
				document.getElementById(options.frameid).src = options.framesrc;
				
			 }
		 }
	 }
	lim[i].style.display=i==n?"block":"none";
 }
}