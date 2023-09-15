var paperID = null;
let answerList = [];

$(document).ready(function(){

    //document.getElementById("createPaper").disabled = true;
    	
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
	
	if(!(($("#answerCount").val() <= 0) || ($("#questionCount").val() <= 0))){
		for (var i = 0; i < questionCount;i++) {
    		
    		var newChildNode = document.createElement('div');
    		var questionId = i + 1;
    		
    		newChildNode.className = '                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   row';
    		newChildNode.id = i; 
    		
    		//newChildNode.innerHTML += '<h1 style = " color:red;">Hi there and greetings!</h1>';
    				        		
    		newChildNode.innerHTML += 
    		"<div class='card' style='padding:1%;margin-top:2%;background-color:#e6f2ff;border-left: 6px solid #1a8cff;'>" + 
			 "<div class='col-lg-12'>" +
				"<form id = 'newQuestion'>" +
					"<div class='row mb-6'>" +
						"<div class='col-lg-2'>" +
							"<input  class='form-control' value = 'Question No. : " + questionId + "' type='text' readonly>" +
							"<input  class='form-control' value = '" + questionId + "' type='hidden' id = 'pq" + questionId + "' readonly>" +
	                    "</div>"+
	                    "<div class='col-lg-3'>" +
		                    "<select class='form-select form-control' aria-label='Default select example' id='moduleId" + questionId + "'>" +
		                    	"<option value=''>-- SELECT MODULE --</option>"+
		                    	"<option th:each='log:${moduleList}'"+
	                                    "th:value='${log.moduleId}'"+
	                                    "th:text='${log.name}'>"+
	                            "</option>"+
	                        "</select>" +
                        "</div>"+
				    "</div>" +	
				    "<br><br>"+
					"<div class='row mb-3'>" +
						"<div class='col-7'>" +
							"<textarea class='form-control' id='question" + questionId + "' rows='3' required></textarea>" +
					    "</div>" +
					    "<div class='col-4' onClick = 'selectQuestion(" + i + " )'>" +
							"<div id='cropzee-hidden-canvas" + i + "' class='image-previewer' data-cropzee='crop" + i + "'> </div>"+
					    "</div>" +
					    "<div class = 'col-1'>" + 
					    	"<i class='bi bi-image' onClick = 'selectQuestion(" + i + " )' id='crop" + i + "' title='Add Image'></i>&nbsp;"+
							"<i class='bi bi-x-lg' onClick = clearImage('cropzee-hidden-canvas" + i + "','image-data" + i + "') title='Remove Image'></i>"+
							"<input type='hidden' id='image-data" + i + "'>" +
							"<input type='hidden' id='extension" + i + "'>" +
						"</div>" +
					"</div>" +
				"</form>" +
			"</div>" +
					 
			"<div class='col-lg-12'>" +
				"<input type='hidden' class='form-control' id = 'maxAnswerCount' readonly>" +
              	"<form id = 'newAnswer'>" +
              		"<br>" + 
	              	"<div class='row mb-6' id = 'answerList" + i + "'></div>" +
				"</form>" +
			"</div>" + 
		    "</div>";
			
			QuestionList.appendChild(newChildNode);
			
			var questionDiv = "answerList" + i;
			
			var AnswerList = document.getElementById(questionDiv);
    		for(x = 1;x<=answerCount;x++){
				var newAnswer = document.createElement('div');
        		
				newAnswer.className = 'input-group mb-6';
				newAnswer.id = x; 
        		
				var parseId = i + "-ans" + x;
				
				newAnswer.innerHTML += 
						"<div class = 'row' style='margin-top:1%;'>" +
							"<div class = 'col-1'>" + 
								"<span><input class='form-check-input' type='radio' name = 'rd" + questionId + "' id='pq"+questionId+"ansStatusId"+x+"'>&nbsp;&nbsp;Is Correct</span>" + 
							"</div>" +
							"<div class = 'col-6'>" + 
								"<input type='text' id = 'pq"+questionId+"answer" + x + "' class='form-control' style='border:none;background-color:#e6f2ff;border-bottom: 2px solid #1a8cff;'>" +
							"</div>" +
							"<div class='col-3' onClick = selectQuestion('" + parseId + "')>" +
								"<div id='cropzee-hidden-canvas" + parseId + "' class='image-previewer' data-cropzee='crop" + parseId + "'> </div>"+
						    "</div>" +
						    "<div class = 'col-1'>" + 
						    	"<i class='bi bi-image' onClick = selectQuestion('" + parseId + "') id='crop" + parseId + "' title='Add Image'></i>&nbsp;"+
						    	"<i onClick = clearImage('cropzee-hidden-canvas" + parseId + "','image-data" + parseId + "') class='bi bi-x-lg'></i>" +
						    	"<input type='hidden' id='image-data" + parseId + "'> " +
						    	"<input type='hidden' id='extension" + parseId + "'> " +
							"</div>" +
						"</div>";
					
				AnswerList.appendChild(newAnswer);
			}
    		
    		var moduleId = "moduleId" + questionId; 
    		listModules(moduleId);
    	}
	}
	validatePaper();
}

function clearImage(id1,id2) {
	var c = document.getElementById(id1);
	var ctx = c.getContext("2d");
	//ctx.fillStyle = "red";
	//ctx.fillRect(0, 0, 300, 150);
	ctx.clearRect(0, 0, c.width, c.height);
	
	document.getElementById(id2).value = '';
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
				document.getElementById("publisher").disabled = true;
				
			}else{
				document.getElementById("createPaper").disabled = false;
				document.getElementById("questionCount").disabled = false;
				document.getElementById("answerCount").disabled = false;
				document.getElementById("time").disabled = false;
				document.getElementById("publisher").disabled = false;
				
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
	if(($("#answerCount").val() <= 0) || ($("#questionCount").val() <= 0)){
		Swal.fire(
		  'Question count or Answer count is not valid',
		  'Something went wrong!',
			      'error'
		).then(function() {
   			document.getElementById("sec1Prev").click();
   			document.getElementById("sec1").disabled = true;
   			document.getElementById("sec1").style.opacity = "0.5";		
		});
		
	}else if(($("#paperName").val() !='') && ($('#subject').val() !='') && ($('#grade').val() !='') && ($("#answerCount").val() > 1) && ($("#questionCount").val() > 0) && ($("#time").val() != 0) && ($('#publisher').val() !='') && ($('#templateId').val() !='') && (document.getElementById("grade").value != '') && (document.getElementById("subject").value != '') && (document.getElementById("publisher").value != '') && (document.getElementById("templateId").value != '')){
		document.getElementById("sec1").disabled = false;
		document.getElementById("sec1").style.opacity = "1";		
	}else{
		Swal.fire(
		  'Enter the required details',
	      'Something went wrong!',
	      'error'
		).then(function() {
   			document.getElementById("sec1Prev").click();
   			document.getElementById("sec1").disabled = true;
   			document.getElementById("sec1").style.opacity = "0.5";		
		});
    }	
}

function validateQuestion(){
	var questionCount = document.getElementById("questionCount").value
		var answerCount = document.getElementById("answerCount").value
		
		var isVal = false;
	var isDone = false;
	
	for (var i = 1; i <= questionCount;i++){
		var questionIdNo = "question" + i;
		var moduleId = "moduleId" + i;
		
		if((document.getElementById(questionIdNo).value !='') && ((document.getElementById(moduleId).value !=''))){
			isVal = true;
			        			
			for(var j = 1;j <= answerCount;j++){
    			var answerId = "pq" + i + "answer" + j;
    			var answerStatusName = "rd" + i;
    			
				 if(document.getElementById(answerId).value !=''){
					 isVal = true; 
    			 }else{
    				 isVal = false;
    				 isDone = true;
    				 break;
    			 }
				 
				 if(j == answerCount){
					 isDone = true;
				 }
    		}
			if(isDone){
				var radios = document.getElementsByName(answerStatusName);
				 for (var x = 0, length = radios.length; x < length; x++) {
					if (radios[x].checked) {
						isVal = true;
				     	break;
				   	}else{
				   		isVal = false;		       					   		
				   	}
				 }
			}
			
		}else{
			isVal = false;
			break;	
        }    				
	}
	
	if(isVal){
		document.getElementById("saveDetails").value = "Submit for Approval";
	}else{
		document.getElementById("saveDetails").value = "Save Paper";
	}
}

$("#saveDetails").click(function () {
	var loadingCount=1;
	document.getElementById('successDiv').style.display = 'none';
	document.getElementById('loadingDiv').style.display = 'block';
	var paper = {
		subjectId: document.getElementById("subject").value,
		name	: document.getElementById("paperName").value,
		grade	: document.getElementById("grade").value,
		time	: document.getElementById("time").value,
		publisher: document.getElementById("publisher").value,
		numberOfQuestion: document.getElementById("questionCount").value,
		answersPerQuestion: document.getElementById("answerCount").value,
		active:false,
		user : document.getElementById("loggedUser").value,
		template : document.getElementById("templateId").value,
		availableForBuy : document.getElementById("availableForBuy").value
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
        		var qImageDataId = "image-data" + (i-1);
    			var qExtensionId = "extension" + (i-1);
    			var questionImage = "";
    			
        		answerList = [];
        			   		        		
        		for(var j = 1;j <= answerCount;j++){
        			console.log(5);
        			var answerId = "pq" + i + "answer" + j;
        			var ansStatusId = "pq" + i + "ansStatusId" + j;
        			var aImageDataId = "image-data" + (i-1) + "-ans" + j
        			var aExtensionId = "extension" + (i-1) + "-ans" + j;
        			answerImage = "";
        			
        			if(document.getElementById(aImageDataId) != null){
        				if(document.getElementById(aImageDataId).value != ""){
        					var file = document.getElementById(aImageDataId).value;
	        				var imageonly = file.split(';');
	        			    var contenttype = imageonly[0].split(':')[1];
	        			    fileformat = contenttype.split('/')[1];
	        			    var realdata = imageonly[1].split(',')[1];
	        			    var blob = b64toBlob(realdata, contenttype);
	        			 	answerImage = realdata;	
	        			}
        			}
        			var date = new Date();
     				var serial = paperID + "-" + i + "-" + j + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds();
        			var answer = JSON.stringify({
	        			answer	: document.getElementById(answerId).value,
	        			answer_status: document.getElementById(ansStatusId).checked,
	        			image : answerImage,
	        			serial : serial,
	        			extention : document.getElementById(aExtensionId).value
	        		});
        			answerList.push(answer);  
        		}
    		
        		if(document.getElementById(qImageDataId) != null){
    				if(document.getElementById(qImageDataId).value != ""){
    					var file = document.getElementById(qImageDataId).value;
        				var imageonly = file.split(';');
        			    var contenttype = imageonly[0].split(':')[1];
        			    fileformat = contenttype.split('/')[1];
        			    var realdata = imageonly[1].split(',')[1];
        			    var blob = b64toBlob(realdata, contenttype);
        			 	questionImage = realdata;
        			 	
        			}
    			}
        		var date = new Date();
     			var serial = paperID + "-" + i + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds();
        		var question = {
    				questionId : document.getElementById(questionId).value,
    				paperID : paperID,
    				moduleId : document.getElementById(moduleId).value,
    				question : document.getElementById(questionIdNo).value,
    				active : true,
    				image : questionImage,
	        		serial : serial,
	        		extention : document.getElementById(qExtensionId).value,
    				answerList : answerList
        		};
        		
        		$.ajax({
					url: '/secure/saveQuestion2',
					type: 'POST',
					data: question,
					cache: false,
					success:function (pqId) {
						
						loadingCount++;
						
   		        		if(i == loadingCount){
   							document.getElementById('successDiv').style.display = 'block';
   		   		        	document.getElementById('loadingDiv').style.display = 'none';
   						}
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

function b64toBlob(b64Data, contentType, sliceSize){
	contentType = contentType || '';
    sliceSize = sliceSize || 512;
    var byteCharacters = atob(b64Data);
    var byteArrays = [];
    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);
        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    var blob = new Blob(byteArrays, { type: contentType }, { fileName: "ppp.JPG" });
    return blob;
}