
$(function() {

  $('#create-poll-container form').not("#poll-metadata").submit(function(event){
  if(this.checkValidity())
  {
    event.preventDefault();
    $(this).parents('.tab-content:first').siblings(".nav-tabs").children("li.active").next().children("a").tab('show');
  }
});

 $("#poll-metadata :submit").click(function(e){
  var form;
  if($(this).attr("form"))form = $("#"+$(this).attr("form"));
  else form = $(this).parents("form");
  if($(form).valid()){
    $(e.target).parents('.tab-content:first').siblings(".nav-tabs").children("li.active").next().children("a").tab('show');
  };
});

 jQuery.validator.addMethod("greaterThan", 
  function(value, element, params) {

    if (!/Invalid|NaN/.test(new Date(value))) {
      return new Date(value) > new Date($(params).val());
    }

    return isNaN(value) && isNaN($(params).val()) 
    || (Number(value) > Number($(params).val())); 
  },'End Date must be greater than from Date.');

 jQuery.validator.addMethod("lessThan", 
  function(value, element, params) {

    if (!/Invalid|NaN/.test(new Date(value))) {
      return new Date(value) < new Date($(params).val());
    }

    return isNaN(value) && isNaN($(params).val()) 
    || (Number(value) < Number($(params).val())); 
  },'From date Must be less than end Date.');

 var highlightFunction  = function(element, errorClass, validClass) {
  $(element).parent("div").removeClass("has-success has-feedback");
  $(element).parent("div").addClass("has-error has-feedback");
  $(element).siblings("span").removeClass("glyphicon-ok");
  if($(element).attr("name")&&!$(element).attr("name").endsWith('Date')){
    $(element).siblings("span").addClass("glyphicon-remove");  
  }
};

var unhighlightFunction = function(element, errorClass, validClass) {
  $(element).parent("div").removeClass("has-error has-feedback");
  $(element).parent("div").addClass("has-success has-feedback");
  $(element).siblings("span").removeClass("glyphicon-remove");
  if($(element).attr("name")&&!$(element).attr("name").endsWith('Date')){
    $(element).siblings("span").addClass("glyphicon-ok");  
  }
};

var errorPlacementFunction = function(label, element) {
 console.log(element);
 label.addClass('alert alert-danger');
 if($(element).attr("name")&&$(element).attr("name").endsWith('Date')){
  $(label).appendTo("#date-errors");
}
else label.insertAfter(element);
}

$("form#poll-metadata").validate({
  highlight: highlightFunction,
  unhighlight: unhighlightFunction,
  errorPlacement: errorPlacementFunction,
  wrapper: 'div',

    // Specify validation rules
    rules: {

      pollName: "required",
      toDate: { greaterThan: "#fromDate" },
      fromDate: { lessThan: "#toDate" },
      "pollQuestions[][question]": "required"
    },
    // Specify validation error messages
    messages: {
      pollName: "Please, provide poll Name!",
      "pollQuestions[][question]": "Please enter your question text"
    },
    // Make sure the form is submitted to the destination defined
    // in the "action" attribute of the form when valid
    submitHandler: function(form) {
      //form.submit();
    }
  });
});