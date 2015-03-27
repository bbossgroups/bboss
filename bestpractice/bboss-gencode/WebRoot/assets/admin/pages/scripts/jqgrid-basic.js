var jqGridBasic = function() {

    var grid;

    var demo1 = function() {

        grid = $("#jqgrid");

        // adjust grid width on resize
        var adjustGridWith = function() {
            grid.jqGrid('setGridWidth', $(".ui-jqgrid").parent().width());
        }

        // datepicker init function
        var initDatepicker = function( cellvalue, options, cell ) {
            setTimeout(function(){
                $(cell).find('input[type=text]').datepicker({format:'yyyy-mm-dd' , autoclose:true}); 
            });
        }

        var data = [
            { id: "1", invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
            { id: "2", invdate: "2007-10-02", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
            { id: "3", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "4", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
            { id: "5", invdate: "2007-10-05", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
            { id: "6", invdate: "2007-09-06", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "7", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" },
            { id: "8", invdate: "2007-10-03", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00" },
            { id: "9", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "10", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "11", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "12", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "13", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "14", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "15", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "16", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "17", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "18", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "19", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "20", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "21", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "22", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" },
            { id: "23", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00" }
        ];

        grid.jqGrid({

            datatype: "local",
            data: data,
            height: 300,
            
            rowNum : 10,
            rowList : [10, 20, 30],
            pager : '#jqgrid_pager',

            sortname : 'id',
            toolbarfilter: true,

            multiselect : true,
            multiselectWidth: 34,
            autowidth : true,            
            viewrecords: true, // show the current page, data rang and total records on the toolbar

            gridComplete: function(){
                Metronic.initUniform();
                adjustGridWith();

                var ids = grid.jqGrid('getDataIDs');
                for(var i=0;i < ids.length;i++){
                    var edit    = "<button class='btn btn-xs btn-default btn-quick' title='Edit Row' onclick=\"jqGridBasic.getGrid().editRow('"+ids[i]+"');\"><i class='fa fa-pencil'></i></button>"; 
                    var save    = "&nbsp;<button class='btn btn-xs btn-default btn-quick' title='Save Row' onclick=\"jqGridBasic.getGrid().saveRow('"+ids[i]+"');\"><i class='fa fa-check'></i></button>";
                    var cancel  = "&nbsp;<button class='btn btn-xs btn-default btn-quick' title='Cancel' onclick=\"jqGridBasic.getGrid().restoreRow('"+ids[i]+"');\"><i class='fa fa-times'></i></button>";  
                    grid.jqGrid('setRowData',ids[i],{act: edit + save + cancel});
                }   
            },

            colModel: [
                {label: 'Inv No', name: 'id', width: 75, key: true, editable : true}, 
                {label: 'Date', name: 'invdate', width: 90, editable : true, unformat: initDatepicker}, 
                {label: 'Client', name: 'name', width: 100, editable : true}, 
                {label: 'Amount', name: 'amount', width: 80, editable : true}, 
                {label: 'Tax', name: 'tax', width: 80, editable : true}, 
                {label: 'Total', name: 'total', width: 80, editable : true}, 
                {label: 'Notes', name: 'note', width: 150, editable : true},
                {label: 'Actions', name : 'act', index:'act', width: 80, sortable:false }, 
            ]
        });

        // add navigation bar with some built in actions for the grid
        grid.navGrid('#jqgrid_pager', {
            edit: true,
            add: true,
            del: true,
            search: true,
            refresh: true,
            view: true,
            position: "left",
            cloneToTop: false
        });

        adjustGridWith();

        Metronic.addResizeHandler(function(){
            adjustGridWith();
        });
    }

    return {

        //main function to initiate the module
        init: function() {
            demo1();
        },

        getGrid: function() {
            return grid;
        }

    };

}();