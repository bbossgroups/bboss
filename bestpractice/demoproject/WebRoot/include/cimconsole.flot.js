	 /**
	  按默认方式，显示单组百分比数据的趋势图,以时间为横轴	  
	 */	 
	 function plotPercTrend(target_, data_, threshold_){
	 	 //调用plotTrend方法，指定y轴最大值为100,数值单位为%   
	 	 var plot = plotTrend(target_, data_, threshold_, 100, "%");           
               
         return plot;
	 }
	
	/**
	  按默认方式，显示单组数据的趋势图,以时间为横轴
	  @param target_ 显示图形的div对象（jQuery对象）
	  @param data_ json标准格式的单组或多组数据
	  	单组数据格式举例：{"label":"CPU使用信息","data":[[1270621129000,18.78121878121878],[1270621437000,0]]}
	  @param threshold_ y轴分界线的阀值
	  @param ymax_ y轴的最大值
	  @param unit_ y轴数据的单位
	 */	 
	 function plotTrend(target_, data_, threshold_, ymax_, unit_){
	 	 //数据
	 	 var plotData = [];
	 	 
	 	 //如果传入的数据对象不为数组，则构建成数组
	 	 if ($.isArray(data_)){
	 	 	plotData = data_;	 	 	
	 	 }
	 	 else{
	 	 	plotData.push(data_);
	 	 }
	 	 
	 	 //分界线的设置
	 	 var crosshairOption = {};	 	 
	 	 //y轴设置
	 	 var yaxisOption = {min:0 };
	 	 
	 	 //如果指定了分界线阀值，则启用y轴分界线
	 	 if (threshold_){         	
          	crosshairOption = { mode: "y" };
         }   
         
         //如果指定了y轴最大值，则进行设置
         if (ymax_){
         	yaxisOption = {min:0 ,max:ymax_};
         }
	 	 
	 	 //组装plot需要的设置参数
	 	 var options = {
	               series: {
	                   lines: { show: true },
	                   points: { show: false }
	               },	               
	               grid: { hoverable: true, clickable: true },
	               xaxis: { mode: 'time' },
	               yaxis: yaxisOption,               
               	   crosshair: crosshairOption  
            	 }
         
         //生成plot图形   	 
	 	 var plot = $.plot(target_,
	          			   plotData,
	          			   options
	          			  );
	          			  
         //鼠标移动到数据点时，显示提示信息    
         bindTooltip(target_, unit_);
         
         //如果指定了分界线阀值，则划一条线
         if (threshold_){         	
          	plot.lockCrosshair({ y: threshold_ });
         }   
               
         return plot;
	 }
	 
	 /**显示数据点的提示信息
	 	@param x x坐标值
	 	@param y y坐标值
	 	@param contents 提示信息内容
	 */
	 function showTooltip(x, y, contents) {
        $('<div id="tooltip">' + contents + '</div>').css( {
            position: 'absolute',
            display: 'none',
            top: y + 5,
            left: x + 5,
            border: '1px solid #fdd',
            padding: '2px',
            'background-color': '#fee',
            'font-size': '12px',
            opacity: 0.80            
        }).appendTo("body").fadeIn(200);
    }
    
    //前一次鼠标所在数据点
    var toolTipPreviousPoint = null;
    
    function bindTooltip(target_, unit_){
    	target_.bind("plothover", function (event, pos, item) {      
	        if (true) {
	            if (item) {
	                if (toolTipPreviousPoint != item.datapoint) {
	                    toolTipPreviousPoint = item.datapoint;
	                    
	                    $("#tooltip").remove();
	                    var x = item.datapoint[0].toFixed(2),
	                        y = item.datapoint[1].toFixed(2);
	                    
	                    var dateTime = new Date();
	                    dateTime.setTime(x);	
	                    //将long型的时间转换成本地时间格式
						var dateTimeStr = dateTime.toLocaleString();
						dateTimeStr = dateTimeStr.replace(' ','');						
						
						if(unit_){
							y = y + unit_;
						}						
						var content =  item.series.label + "(" + dateTimeStr + " = " + y+  ")";					
	                    
	                    showTooltip(item.pageX, item.pageY,
	                                content);
	                }
	            }
	            else {
	                $("#tooltip").remove();
	                toolTipPreviousPoint = null;            
	            }
	        }
   	 	});
   	}   	
   	
