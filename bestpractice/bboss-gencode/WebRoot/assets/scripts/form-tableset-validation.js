var FormValidation = function () {

    

    var handleValidation2 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form2 = $('#tableset');
            var error2 = $('.alert-danger', form2);
            var success2 = $('.alert-success', form2);

            form2.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                focusInvalid: true, // do not focus the last invalid input
                ignore: "",
                rules: {
                	tableName: {
                        minlength: 2,
                        required: true
                    },
                    moduleName: {
                    	minlength: 2,
                        required: true
                    },
                    moduleCNName: {
                    	minlength: 2,
                        required: true
                    },
                    packagePath: {
                    	minlength: 2,
                        required: true
                    },
                    sourcedir: {
                    	minlength: 2,
                        required: true
                    },
                    theme: {
                    	
                        required: true
                    },
                    author: {
                    	minlength: 2,
                        required: true
                    },
                    company: {
                    	minlength: 2,
                        required: true
                    },
                    version: {
                    	minlength: 2,
                        required: true
                    }
                    
                },

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success2.hide();
                    error2.show();
                    //App.scrollTo(error2, -200);
                },

                errorPlacement: function (error, element) { // render error placement for each input type
                    var icon = $(element).parent('.input-icon').children('i');
                    icon.removeClass('fa-check').addClass("fa-warning"); 
                    
                    icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
                    $(element).closest('.form-group').removeClass('has-success').addClass('has-error'); // set success class to the control group
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group   
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    
                },

                success: function (label, element) {
                    var icon = $(element).parent('.input-icon').children('i');
                    $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                    icon.removeClass("fa-warning").addClass("fa-check");
                },

                submitHandler: function (form) {
                    //success2.show();
                    error2.hide();
                    var pageContent = $('.page-content');
                    App.blockUI(pageContent, false,'../..');

                    $.ajax({
                        type: "POST",
                        cache: false,
                        url: "gencode.page",
                        data :App.formToJson("#tableset") ,
                        dataType: "json",
                        success: function (res) {
                            App.unblockUI(pageContent);
                            //pageContentBody.html(res);
                            //App.fixContentHeight(); // fix content height
                            //App.initAjax(); // initialize core stuff
                            if(res.result == 'success')
                            {
                            	App.scrollTo(success2, -200);
                            	success2.show();
                            	error2.hide();
                            }
                            	
                            else
                            {
                            	App.scrollTo(error2, -200);
                            	error2.show();
                            	success2.hide();
                            }
                            	
                            
                            
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            //pageContentBody.html('<h4>Could not load the requested content.</h4>');
                           // App.unblockUI(pageContent);
                        },
                        async: false
                    });
                    return false;
                }
            });
            //apply validation on select2 dropdown value change, this only needed for chosen dropdown integration.
            $('.select2me', form2).change(function () {
            	form2.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
            });

    }

   

    return {
        //main function to initiate the module
        init: function () {           
            handleValidation2();
        }

    };

}();