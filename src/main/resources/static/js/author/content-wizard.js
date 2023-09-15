var paperID = null;
let answerList = [];

$(document).ready(function(){
	var current_fs, next_fs, previous_fs; //fieldsets
	var opacity;
	$(".next").click(function(){
		current_fs = $(this).parent();
		next_fs = $(this).parent().next();
		
		//Add Class Active
		$("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
		
		//show the next fieldset
		next_fs.show();    
		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now) {
				// for making fielset appear animation
				opacity = 1 - now;

				current_fs.css({
					'display': 'none',
					'position': 'relative'
				});
				next_fs.css({'opacity': opacity});
			}, 
			duration: 600
		});
	});

	$(".previous").click(function(){
		current_fs = $(this).parent();
		previous_fs = $(this).parent().prev();
		
		//Remove class active
		$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
		
		//show the previous fieldset
		previous_fs.show();

		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now) {
				// for making fielset appear animation
				opacity = 1 - now;

				current_fs.css({
					'display': 'none',
					'position': 'relative'
				});
				previous_fs.css({'opacity': opacity});
			}, 
			duration: 600
		});
	});

	$('.radio-group .radio').click(function(){
		$(this).parent().find('.radio').removeClass('selected');
		$(this).addClass('selected');
	});

	$(".submit").click(function(){
		return false;
	})
	
});

function createQuestion(){
	
	var questionCount = document.getElementById("questionCount").value;
	var answerCount = document.getElementById("answerCount").value;
	var QuestionList = document.getElementById("section1");
	
	QuestionList.innerHTML = null;
	
	for (var i = 0; i < questionCount;i++) {
		
		var newChildNode = document.createElement('div');
		var questionId = i + 1;
		
		newChildNode.className = 'row';
		newChildNode.id = i; 
		
		//newChildNode.innerHTML += '<h1 style = " color:red;">Hi there and greetings!</h1>';
				        		
		newChildNode.innerHTML += 
		 "<div class='col-lg-6'>" +
			"<div class='card'>" +
				"<div class='card-body'>" +
					"<form id = 'newQuestion'>" +
						"<div class='row mb-6'>" +
							"<label for='questionId' class='col-sm-3 col-form-label'>Question No. : </label>" +
							"<div class='col-sm-4'>" +
								"<input  class='form-control' value = '" + questionId + "' type='text' id = 'pq" + questionId + "' style = 'width:30%;' required readonly>" +
		                    "</div>"+
		                    "<label for='module' class='col-sm-2 col-form-label'>Module</label>" +
		                    "<div class='col-sm-3'>" +
			                    "<select class='form-select' aria-label='Default select example' id='moduleId" + questionId + "'>" +
				                    "<option th:each='log:${moduleList}'"+
		                                    "th:value='${log.moduleId}'"+
		                                    "th:text='${log.name}'>"+
		                            "</option>"+
		                        "</select>" +
	                        "</div>"+
					    "</div>" +
											    
						"<div class='row mb-6'>" +
							"<textarea class='form-control' id='question" + questionId + "' rows='3' required></textarea>" +
					    "</div>" +
					"</form>" +
				"</div>" +
			"</div>" +
		"</div>" +
				 
		"<div class='col-lg-6'>" +
			"<div class='card'>" +
				"<div class='card-body'>" +
						"<input type='hidden' class='form-control' id = 'maxAnswerCount' readonly >" +
		              	"<form id = 'newAnswer'>" +
			              	"<div class='row mb-6'>" +
								"<label for='question' class='col-sm-12 col-form-label'>Answers</label>" +
						    "</div>" +
			                "<div class='row mb-6' id = 'answerList" + i + "'></div>" +
						"</form>" +
				"</div>" +
			"</div>" +
		"</div>";
		
		QuestionList.appendChild(newChildNode);
		
		var questionDiv = "answerList" + i;
		
		var AnswerList = document.getElementById(questionDiv);
		for(x = 1;x<=answerCount;x++){
			var newAnswer = document.createElement('div');
    		
			newAnswer.className = 'input-group mb-6';
			newAnswer.id = x; 
    			        		
			newAnswer.innerHTML += "<span class='input-group-text'><input class='form-check-input' type='radio' name = 'rd" + questionId + "' id='pq"+questionId+"ansStatusId"+x+"'>&nbsp;&nbsp;Is Correct</span><input type='text' id = 'pq"+questionId+"answer"+x+"' class='form-control'>";
			AnswerList.appendChild(newAnswer);
		}
		
		var moduleId = "moduleId" + questionId; 
		listModules(moduleId);
	}
	
	validatePaper();
}

function onUpdateGrade() {
	
	var listBox = document.getElementById("subject");
    listBox.options.length = 0;
    
	var selectGrade = document.getElementById('grade').value;
			
	$.ajax({
		url: '/getSubjectsByGrade',
		type: 'GET',
		traditional: true,
		data: {gradeId: selectGrade},
		success: function (subjects) {
			
			if(subjects.length === 0){
				Swal.fire({
				  title: 'Error!',
				  text: 'There are no subjects for selected grade',
				})
				
				document.getElementById("createPaper").disabled = true;
				document.getElementById("questionCount").disabled = true;
				document.getElementById("answerCount").disabled = true;
				document.getElementById("time").disabled = true;
				
			}else{
				document.getElementById("createPaper").disabled = false;
				document.getElementById("questionCount").disabled = false;
				document.getElementById("answerCount").disabled = false;
				document.getElementById("time").disabled = false;
				
				for (let x in subjects) {
					
					var subject = subjects[x];
					for (let y in subject) {
						var subjectId = subject["subjectId"];
						var subjectName = subject["subjectName"];
						var status = subject["active"];
						if(status){
							$('#subject').append(`<option value="${subjectId}">${subjectName} </option>`);
							break;	
						}
					};
					
				};	
			}
		}
	});
}

function onUpdateSubject() {
	
	var selectSubject = document.getElementById('subject').value;
			
	$.ajax({
		url: '/getModuleBySubject',
		type: 'GET',
		traditional: true,
		data: {subjectId: selectSubject},
		success: function (modules) {
			
			if(modules.length === 0){
				Swal.fire({
				  title: 'Error!',
				  text: 'There are no modules for selected subject',
				})
				   
				document.getElementById("createPaper").disabled = true;
				document.getElementById("questionCount").disabled = true;
				document.getElementById("answerCount").disabled = true;
				document.getElementById("time").disabled = true;
				document.getElementById("publisher").disabled = true;
			}else{
				document.getElementById("createPaper").disabled = false;
				document.getElementById("questionCount").disabled = false;
				document.getElementById("answerCount").disabled = false;
				document.getElementById("time").disabled = false;
				document.getElementById("publisher").disabled = false;
			}
		}
	});
}

function listModules(moduleId) {
	
	var listBox = document.getElementById(moduleId);
    listBox.options.length = 0;
    
    var selectSubject = document.getElementById('subject').value;
	
	$.ajax({
		url: '/getModuleBySubject',
		type: 'GET',
		traditional: true,
		data: {subjectId: selectSubject},
		success: function (modules) {
			
			if(modules.length === 0){
				Swal.fire({
				  title: 'Error!',
				  text: 'There are no modules for selected subjects',
				})
			}else{
				for(let a in modules) {
					
					var module = modules[a];
					
					for (let b in module) {
						console.log(module["name"]);
						
						var modId = module["moduleId"];
						
						var modName = module["name"];
						
						var status = module["active"];
						
						if(status){
						
							$('#'+moduleId).append("<option value='" + modId + "'>" + modName + "</option>");
							
							break;	
						}
					};
					
				};	
			}
		}
	});
}

function validatePaper(){
	if(($("#paperName").val() !='') && ($('#subject').val() !='') && ($('#grade').val() !='') && ($("#answerCount").val() != 0) && ($("#questionCount").val() != 0) && ($("#time").val() != 0) && ($('#publisher').val() !='') && ($('#templateId').val() !='') && (document.getElementById("grade").value != '') && (document.getElementById("subject").value != '') && (document.getElementById("publisher").value != '') && (document.getElementById("templateId").value != '')){
		document.getElementById("sec1").disabled = false;
		document.getElementById("sec1").style.opacity = "1";		
	}else{
		Swal.fire(
	      'Something went wrong!',
	      'Enter the required details',
	      'error'
	    );
		
		document.getElementById("sec1").disabled = true;
		document.getElementById("sec1").style.opacity = "0.5";		
    }	
}

function validateQuestion(){
	var questionCount = document.getElementById("questionCount").value
	var answerCount = document.getElementById("answerCount").value
	
	var isVal = false;
	
	for (var i = 1; i <= questionCount;i++){
		var questionIdNo = "question" + i;
		var moduleId = "moduleId" + i;
		
		if((document.getElementById(questionIdNo).value !='') && ((document.getElementById(moduleId).value !=''))){
			isVal = true;
			        			
			for(var j = 1;j <= answerCount;j++){
    			var answerId = "pq" + i + "answer" + j;
    			 
				 if(document.getElementById(answerId).value !=''){
					 isVal = true; 
    			 }else{
    				 isVal = false;
    				 break;
    			 }
    		}
			
		}else{
			isVal = false;
			break;	
        }    				
    }
	
	if(isVal){
		document.getElementById('saveDetails').value = "Submit for Approval";
	}else{
		document.getElementById('saveDetails').value = "Save";
	}
}

$("#saveDetails").click(function () {
	var paper = {
		userId : document.getElementById("userId").value,
		subjectId: document.getElementById("subject").value,
		name	: document.getElementById("paperName").value,
		grade	: document.getElementById("grade").value,
		time	: document.getElementById("time").value,
		publisher: document.getElementById("publisher").value,
		numberOfQuestion: document.getElementById("questionCount").value,
		answersPerQuestion: document.getElementById("answerCount").value,
		active:false,
		user : document.getElementById("loggedUser").value,
		template : document.getElementById("templateId").value
	};
		
	$.ajax({
		url: '/secure/savePaper2',
		type: 'POST',
		data: paper,
		cache: false,
        success:function (response) {
        	
        	paperID = response;
        	
        	var questionCount = document.getElementById("questionCount").value;
        	var answerCount = document.getElementById("answerCount").value;
        			    		        	
        	for (var i = 1; i <= questionCount;i++){
        		
        		var questionId = "pq" + i;
        		var questionIdNo = "question" + i;
        		var moduleId = "moduleId" + i;
        		
        		answerList = [];
        		
        		for(var j = 1;j <= answerCount;j++){
        			var answerId = "pq" + i + "answer" + j;
        			var ansStatusId = "pq" + i + "ansStatusId" + j;
        			
        			var answer = JSON.stringify({
	        			answer	: document.getElementById(answerId).value,
	        			answer_status: document.getElementById(ansStatusId).checked,
	        		});
        			answerList.push(answer);  
        		}
        		
        		var question = {
    				questionId : document.getElementById(questionId).value,
    				paperID : paperID,
    				moduleId : document.getElementById(moduleId).value,
    				question : document.getElementById(questionIdNo).value,
    				active : true,
    				answerList : answerList
        		};
        		        		
        		$.ajax({
					url: '/secure/saveQuestion2',
					type: 'POST',
					data: question,
					cache: false,
					success:function (pqId) {
							    	    						
					},
					error:function(status, error){
						Swal.fire(
					      'Something went wrong!',
					      'Question Can not be save.',
					      'error'
					    );
					}
				});
			
		    }
        }    
	});
});
		        