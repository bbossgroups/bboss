var TableAdvanced = function () {


    var initTable5 = function () {

        var table = $('#sample_5');

      
        var oTable = table.dataTable({
            "dom": "<'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>", // datatable layout without  horizobtal scroll
            "scrollY": "300",
            "deferRender": true,
            "order": [
                [1, 'asc']
            ],
            "columns": [{
                "orderable": false
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": true
            }, {
                "orderable": false
            }],
            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],
            "pageLength": 10 // set the initial value            
        });

        $('#group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).attr("checked", true);
                    $(this).parents('tr').addClass("active");
                } else {
                    $(this).attr("checked", false);
                    $(this).parents('tr').removeClass("active");
                }
            });
            jQuery.uniform.update(set);
        });

        table.on('change', 'tbody tr .checkboxes', function () {
            $(this).parents('tr').toggleClass("active");
        });
        var tableWrapper = $('#sample_5_wrapper'); // datatable creates the table wrapper by adding with id {your_table_jd}_wrapper
        var tableColumnToggler = $('#sample_5_column_toggler');
        //tableWrapper.find('.dataTables_length select').select2(); // initialize select2 dropdown
        
        tableWrapper.find('.dataTables_length select').addClass("form-control input-xsmall input-inline"); // modify table per page dropdown
       

        /* handle show/hide columns*/
        $('input[type="checkbox"]', tableColumnToggler).change(function () {
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
        
        

        

        
    };

     
    return {

        //main function to initiate the module
        init: function () {

            if (!jQuery().dataTable) {
                return;
            }

            console.log('me 1');

            
            initTable5();
           

            console.log('me 2');
        },
    	regencode:function (gencodeid,url,e) {
            e.preventDefault();
            Metronic.scrollTop();

            
            var formid = $(this).attr("formid");
            var pageContentBody = $('.page-content .page-content-body');

            Metronic.startPageLoading();

            if (Metronic.getViewPort().width < Layout.getResBreakpointMd() && $('.page-sidebar').hasClass("in")) { // close the menu on mobile view while laoding a page 
                $('.page-header .responsive-toggler').click();
            }
            
            $.ajax({
                type: "POST",
                cache: false,
                url: url,	
                data:{gencodeid:gencodeid},
                dataType: "html",
                success: function (res) {
                    Metronic.stopPageLoading();
                    pageContentBody.html(res);
                    Layout.fixContentHeight(); // fix content height
                    Metronic.initAjax(); // initialize core stuff
                   
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    pageContentBody.html('<h4>Could not load the requested content.</h4>');
                    Metronic.stopPageLoading();
                }
            });
            
        }

    };

}();