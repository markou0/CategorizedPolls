$(document).ready(function(){

	$("[name$='question-wraper']").attr('title', 'Sort Questions with mouse drag');
	$("[name$='answers-wraper']").attr('title', 'Sort Answers with mouse drag');

	//make poll answers sortable
	$("[name$='question-list-wraper']").sortable();
	$("[name$='question-list-wraper']").disableSelection();

	//ui components initialization
	var datetimepickerInit = {format: 'YYYY-MM-DD HH:mm:ss'};
	$('#from-date-wraper').datetimepicker(datetimepickerInit);
	$('#to-date-wraper').datetimepicker(datetimepickerInit);

	$(document).on('change', "#test-outcomes-tab [type='number']", function(e) {
		var testOutcomeScoreField = $(e.target);
		var answerIndex = $("#test-outcomes-tab [type='number']").index(testOutcomeScoreField);
		var questionIndex = $("[name='test-outcome-wraper']").index($(testOutcomeScoreField).parents("[name='test-outcome-wraper']"));
		$('[name="testQuestions[][answers][][categoriesScores][]"]').eq(answerIndex).val(testOutcomeScoreField.val());
	});

	$(document).on('click change', ':file, .imageupload [name="remove-image"]', function(e) {
		var fileInput = e.target;
		var filename = $(fileInput).val();
		if (filename.substring(3,11) == 'fakepath') {
			filename = filename.substring(12);
        } // Remove c:\fake at beginning from localhost chrome
        $(fileInput).parents(".imageupload").find("[name$='[imageName]']").val(filename);
    });

	$('[name$="answer-image-change"]').click(function(event){
		$(this).parents("[name$='answer-wraper']").find("[name='image-select-panel']").collapse("toggle");
	});

	$('[name$="answer-remove"]').click(function(event){
		var remAnwrBttns = $(this).parents("[name$='answers-wraper']").find('[name$="answer-remove"]');
		if(remAnwrBttns.length > 2){
			$(event.target).parents("[name$='answer-wraper']").remove();
		}
		if(remAnwrBttns.length <= 3)
		{
			$(remAnwrBttns).addClass("disabled");
		} 
	});

	$("[id$='poll-answer-add']").click(function(event){
		pollAnswer.clone(true).insertBefore(event.target);
	});

	$("[id$='test-answer-add']").click(function(event){
		testAnswer.clone(true).insertBefore(event.target);
	});

	$("[id$='answer-add']").click(function(event){
		var remAnwrBttns = $(this).parents("[name$='answers-wraper']").find('[name$="answer-remove"]');
		if(remAnwrBttns.length > 2){
			$(remAnwrBttns).removeClass("disabled");
		}
	});





	$('[name="test-category-remove"]').click(function(e){
		var categoriesAmount = $('[name="test-category-wraper"]').length;
		var categoryIndex = $('[name="test-category-wraper"]').index($(e.target).parents('[name="test-category-wraper"]'));
		var scoreFieldsAmount = $("#test-questions [name='testQuestions[][answers][][scores][]']").length;
		var scoreFields =  $("#test-questions [name='testQuestions[][answers][][scores][]']");
		for(i=categoryIndex;i<scoreFieldsAmount;i+=categoriesAmount){
			scoreFields[i].remove();
		}
		var remAnwrBttns = $(this).parents("#test-categories").find("[name='test-categories-wraper']").find('[name="test-category-remove"]');
		if(remAnwrBttns.length > 2){
			$(event.target).parents("[name='test-category-wraper']").remove();
		}
		if(remAnwrBttns.length <= 3)
		{
			$(remAnwrBttns).addClass("disabled");
		} 
	});

	$('#test-category-add').click(function(event){
		$('[name="test-answer-wraper"]').append(testOutcomeField.clone());
		$(this).parents("#test-categories").find("[name='test-categories-wraper']").append(testCategory.clone(true));
		var remAnwrBttns = $(this).parents("#test-categories").find("[name='test-categories-wraper']").find('[name="test-category-remove"]');
		if(remAnwrBttns.length > 2){
			$(remAnwrBttns).removeClass("disabled");
		}
	});


	$('#test-answer-add').click(function(e){
		var categoriesAmont = $("#test-categories [name='test-category-wraper']").length;
		for(i=0;i<categoriesAmont;i++){
			var scoreFieldsInAnswerWraperAmount = $(e.target).parents('[name="test-answers-wraper"]').find('[name="test-answer-wraper"]:last').find('[name="testQuestions[][answers][][categoriesScores][]"]').length;
			if(scoreFieldsInAnswerWraperAmount < categoriesAmont){

				$(e.target).parents('[name="test-answers-wraper"]').find('[name="test-answer-wraper"]:last').append(testOutcomeField.clone());
			}
			else break;
		}

	});


	$('.nav-tabs a[href="#test-outcomes-tab"]').on('show.bs.tab', function(){
		calcOutcomesTable();
	});

	$(document).on('click change', '[name="test-category-remove"], #test-category-add , [name="testCategories[]"], [name="test-answer-remove"], #test-answer-add, [name="test-question-add"], [name="test-question-remove"]', function(e) {
		//calcOutcomesTable();
	});

	$( '[name="test-answers-wraper"]' ).sortable({
		deactivate: function( event, ui ) {
			//calcOutcomesTable();
		}
	});

	$( "[name='test-question-list-wraper']" ).sortable({
		deactivate: function( event, ui ) {
			//calcOutcomesTable();
			$( "[name$='question-list-wraper'] [name$='question-wraper'] [name$='question-add']" ).hide();
			$( "[name$='question-list-wraper'] [name$='question-wraper']:last-child [name$='question-add']" ).show();
		}
	});

	$('[name$="question-remove"]').click(function(event){
		var questionListWraper = $(event.target).parents("[name$='question-list-wraper']");
		if($(questionListWraper).find("[name$='question-wraper']").length > 1){
			$(event.target).parents("[name$='question-wraper']").remove();
		}
		if($(questionListWraper).find("[name$='question-wraper']").length < 2){
			$(questionListWraper).find("[name$='question-add']").show();
			$(questionListWraper).find('[name$="question-remove"]').attr("disabled",true);
		}
	});

	$("[name$='poll-question-add']").click(function(event){
		var questionListWraper = $(event.target).parents("[name$='question-list-wraper']");
		$(event.target).hide();
		pollQuestionForm.clone(true).appendTo($(event.target).parents("[name$='question-list-wraper']"));
		if($(questionListWraper).find("[name$='question-wraper']").length > 1)
			$(questionListWraper).find('[name$="question-remove"]').attr("disabled",false);
	});

	$("[name$='test-question-add']").click(function(event){
		var questionListWraper = $(event.target).parents("[name$='question-list-wraper']");
		$(event.target).hide();
		testQuestionForm.clone(true).appendTo($(event.target).parents("[name$='question-list-wraper']"));
		if($(questionListWraper).find("[name$='question-wraper']").length > 1)
			$(questionListWraper).find('[name$="question-remove"]').attr("disabled",false);
	});

	//new imageupload and sortable componets initialization 
	$("[id$='answer-add']").click(function(event){
		$(this).parents("[name$='question-wraper']").find('.imageupload:last').imageupload();
	});

	$("[name$='question-add']").click(function(event){
		var questionListWraper = $(event.target).parents("[name$='question-list-wraper']");
		$(questionListWraper).find("[name$='question-wraper']:last").find('.imageupload:first').imageupload();
		$(questionListWraper).find("[name$='question-wraper']:last").find('.imageupload:last').imageupload();
		$(questionListWraper).find("[name$='question-wraper']:last").find("[id$='answer-add']").trigger("click");
		$(questionListWraper).find('[name$="answers-wraper"]:last').sortable();
		$(questionListWraper).find('[name$="answers-wraper"]:last').disableSelection();
	});

	$('[name="test-question-add"]').click(function(e){
		var categoriesAmont = $("#test-categories [name='test-category-wraper']").length;
		for(i=0;i<categoriesAmont;i++){
			var scoreFieldsInFirstAnswerWraperAmount = $('[name="test-question-wraper"]:last').find('[name="test-answer-wraper"]:first').find('[name="testQuestions[][answers][][categoriesScores][]"]').length;
			if(scoreFieldsInFirstAnswerWraperAmount < categoriesAmont){
				$('[name="test-question-wraper"]:last').find('[name="test-answer-wraper"]:first').append(testOutcomeField.clone());
			}
			else break;
		}

	});

	var pollAnswer = $("[name='poll-answer-wraper']").clone(true);
	var pollQuestionForm = $("[name=poll-question-wraper]").clone(true);
	var testAnswer = $("[name='test-answer-wraper']").clone(true);
	var testQuestionForm = $("[name='test-question-wraper']").clone(true);
	var testCategory = $("[name='test-category-wraper']").clone(true);
	var testOutcomeRow = $('[name="test-outcome-wraper"] tbody tr').clone();
	$('[name="test-outcome-wraper"] tbody tr').remove()
	var testOutcomeWraper =  $("[name='test-outcome-wraper']").clone();
	var testOutcomeField = $('[name="testQuestions[][answers][][categoriesScores][]"]').clone();
	$("[name$='answers-wraper']").sortable();
	$("[name$='answers-wraper']").disableSelection();
	$('.imageupload').imageupload();
	$("[id$='answer-add'],#test-category-add").trigger("click");
	
	//ajax forms
	$("#submit-poll-forms").click(function (event) {
		if(checkPollFormValidity()){
			$("#submit-poll-forms").removeAttr("title");
			$("#submit-poll-forms").attr("disabled", false);
			event.preventDefault();
			fire_ajax_submit();
		}	
		else {
			$("#submit-poll-forms").attr("disabled", true);
			$("#submit-poll-forms").attr("title", "check forms for invalid inputs!")
		}
    });

	$("#create-poll-container :submit").click(function(event){
		if(checkPollFormValidity()){
			$("#submit-poll-forms").removeAttr("title");
			$("#submit-poll-forms").attr("disabled", false);
		}
		else {
			$("#submit-poll-forms").attr("disabled", true);
			$("#submit-poll-forms").attr("title", "check forms for invalid inputs!")
		}
	});



	$(":submit[form='poll-theme']").click(function(event){
		var formDomEl = $("form#poll-theme");
		var formObject = $(formDomEl).serializeJSON();
		event.preventDefault();
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/poll-theme/create",
			data : JSON.stringify(formObject),
			dataType : 'json',
			timeout : 100000,
			beforeSend: function() {
				console.log("SENDING :"+JSON.stringify(formObject));
			}
			,
			success : function(theme) {
				$(formDomEl).find("#create-error").hide();
				$(formDomEl).find("#create-success").show();
				$(formDomEl).find("#create-success").html("Poll theme created!");
				$("select#pollThemeId option").attr('selected',false)
				$("select#pollThemeId").prepend("<option selected='true' value="+theme.id+"> "+theme.name+" </option>")
				console.log("SUCCESS: ", theme);
			},
			error : function(e) {
				$(formDomEl).find("#create-error").show();
				$(formDomEl).find("#create-success").hide();
				$(formDomEl).find("#create-error").html("<strong>Fail to create Theme!</strong> theme name must be provided");
				console.log("ERROR: ", e.responseText);
			}
		})
	})

    //functions

    function checkPollFormValidity(){
    	var forms = $("form");
    	if(!$("form#poll-metadata").valid())return false;
    	else 
    		for(var form of forms){
    			if(!$(form)[0].checkValidity()){
    				return false;
    			}
    		}
    		return true;	
    	}

    	function calcOutcomesTable(){
    		$("[name='test-outcome-wraper']").remove();
    		var testCategories = $("#test-categories").serializeJSON()["testCategories"];
    		var testQuestions = $("#test-questions").serializeJSON()["testQuestions"];

    		for (i = 0; i < testQuestions.length; i++) {
    			var currTestOutcomeWraper = testOutcomeWraper.clone();
    			$(testOutcomeWraper).find("tbody tr").remove();
    			var testQuestion = testQuestions[i];
    			$(currTestOutcomeWraper).find("#question").text(testQuestion["question"]);
    			var testAnswers = testQuestion["answers"];
    			for(j=0;j<testAnswers.length;j++){
    				var testAnswer = testAnswers[j];
    				for(k=0;k<testCategories.length;k++){
    					var currTestOutcomeRow = testOutcomeRow.clone();
    					$(currTestOutcomeRow).find("td:eq(0)").text(testAnswer["answer"]);
    					$(currTestOutcomeRow).find("td:eq(1)").text(testCategories[k]);
    					$(currTestOutcomeRow).find('[name="testOutcomes[]"]').val(testAnswer["categoriesScores"][k]);
    					$(currTestOutcomeRow).appendTo($(currTestOutcomeWraper).find("tbody"));
    				}
    			}
    			$(currTestOutcomeWraper).appendTo("[name='test-outcomes-wraper']")
    		}
    	}
    });

function fire_ajax_submit() {
	var files = new FormData($("#poll-questions")[0]);
	var formObject = $.extend($("#test-categories").serializeJSON(),$("#test-questions").serializeJSON(),$("#poll-questions").serializeJSON(),$("#poll-metadata").serializeJSON());
	$(formObject).serializeJSON();
	var pollId;

	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/poll/create",
		data : JSON.stringify(formObject),
		dataType : 'json',
		timeout : 100000,
		beforeSend: function() {
			$("#result").html("Sending : <br>"+JSON.stringify(formObject, null, 4));
			console.log("SENDING :"+JSON.stringify(formObject));
		}
		,
		success : function($pollId) {
			pollId=$pollId;
			$("#create-error").hide();
			$("#create-success").show();
			$("#create-success").html("Poll successfuly created! You can participate in this poll <a href='/poll/"+Number(pollId)+"/test-questions/take'>here</a>");
			console.log("SUCCESS: ", pollId);
			console.log("pollId = "+pollId);
			files.append("pollId", pollId);

			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: "/api/upload/multi",
				data: files,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (e) {
        	
        	console.log("SUCCESS : ", e);

        },
        error: function (e) {
        	$("#create-success").hide();
        	$("#create-error").show();
        	$("#create-error").append("<b>Error!<b> poll not created due to : "+e.responseText);
        	console.log("ERROR : ", e);

        }
    });
		},
		error : function(e) {
			console.log("ERROR: ", e.responseText);
		}
	})

}
