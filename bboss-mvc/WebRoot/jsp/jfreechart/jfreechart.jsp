<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>JFreeChart使用demo</title>
	<style type="text/css">
	.tip {
    color: #fff;
    background:#000000;
    display:none; /*--Hides by default--*/
    padding:10px;
    position:absolute;    
    z-index:1000;
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    border-radius: 3px;
}
	</style>
	<script type="text/javascript"
	 src="<%=request.getContextPath() %>/include/smartupload/jquery-1.5.js"></script>
	 
	</head>
	
	<script type="text/javascript">
	
	$(document).ready(function(){
              $("#mapinfo").load("getmaphot.htm");
              $("#mapbarinfo").load("getbarmaphot.htm");
              $("#mappiehot").load("getpiemaphot.htm");
              $("#mappiehot3D").load("getpiemaphot3D.htm");
              $("#mapbarhot2").load("getpiemaphot1.htm");
        });
	 function showTooltip(obj,e){
	    var detail = obj.title;
	    $(".mytip").html(detail);
	    var tipid = $(obj).attr("spanid");
        tip = $("#"+tipid);
        var mousex = e.pageX + 20; //Get X coodrinates
        var mousey = e.pageY + 20; //Get Y coordinates
        var tipWidth = tip.width(); //Find width of tooltip
        var tipHeight = tip.height(); //Find height of tooltip

        //Distance of element from the right edge of viewport
        var tipVisX = $(window).width() - (mousex + tipWidth);
        //Distance of element from the bottom of viewport
        var tipVisY = $(window).height() - (mousey + tipHeight);

        if ( tipVisX < 20 ) { //If tooltip exceeds the X coordinate of viewport
            mousex = e.pageX - tipWidth - 20;
        } if ( tipVisY < 20 ) { //If tooltip exceeds the Y coordinate of viewport
            mousey = e.pageY - tipHeight - 20;
        }
         tip.css({  top: mousey, left: mousex });
        tip.show(); //Show tooltip
	 }
	 
	 function UnTip(obj){
	 	var tipid = $(obj).attr("spanid");
        var tip = $("#"+tipid);
	  	tip.hide(); //Hide tooltip
	 }
	 
	 
	 
	 
	
	</script>
	
	<body >
	 <div align="center">
	 <span id="spanid" class="tip" style="width: 450px; display: none; top: 81px; left: 51px;"><img src="<%=request.getContextPath() %>/jsp/jfreechart/amazeelabs_thumb.gif" style="float: left; margin-right: 20px;" alt="">
			<span class="mytip">We build fresh websites and amazing community solutions. confidence and a dash of spice! </span></span>
	  
		<img src="jfreechart.htm" width=500 height=300 border=0
			usemap="#maphot">
			
		
	  </div>
	  <div id="mapinfo"></div>	
	  <br /><br /><br/>
	  <div align="center">
	    <img src="chartbar.htm" width=500 height=300 border=0
			usemap="#mapbarhot">
	  </div> 
	  <div id="mapbarinfo"></div>	
	   <br /><br /><br/>
	  
	  <div align="center">
	    <img src="chartbar2.htm" width=500 height=300 border=0
			usemap="#mapbarhot2">
	  </div> 
	  <div id="mapbarhot2"></div>
	  
	  <br /><br /><br/>
	  <div align="center">
	    <img src="piechart.htm" width=500 height=300 border=0
			usemap="#mappiehot">
	  </div> 
	  <div id="mappiehot"></div>
	  <br /><br /><br/>
	  
	  <div align="center">
	    <img src="piechart3D.htm" width=500 height=300 border=0
			usemap="#mappiehot3D">
	  </div> 
	  <div id="mappiehot3D"></div>
	 
	</body>
</html>


