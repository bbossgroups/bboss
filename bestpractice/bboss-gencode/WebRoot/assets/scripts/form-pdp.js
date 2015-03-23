var FormPDP = function () {


    return {
        //main function to initiate the module
        init: function () {

           
            $(".select2_sample3").select2({
                tags: ["geni18n", "clearSourcedir","genRPC"]
            });
            
            $(".select2_sample4").select2({
                tags: ["default", "bootstrap"]
            });

        }

    };

}();