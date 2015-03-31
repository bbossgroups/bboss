var ComponentsDropdowns = function () {

     
    

    return {
    
    	loadtables:function(event,targetE)
    	{
    		 Metronic.startPageLoading();
    		var dbname = $(event.currentTarget).val();
    		$.ajax({
                type: "POST",
                cache: false,
                url: "loadtables.page",
                data:{dbname:dbname},
                dataType: "json",
                success: function (tables) {
                	 Metronic.stopPageLoading();
                	 var hcontent = "";
                	 for(var table in tables)
                	{
                		 hcontent = hcontent+"<option value='"+tables[table]+"'>"+tables[table]+"</option>";
                	}
                    $("#"+targetE).html(hcontent);
                    
                },
                error: function (xhr, ajaxOptions, thrownError) {
                	 alert(thrownError);
                	Metronic.stopPageLoading();
                },
                async: false
            });
    	}
        
        
    };

}();