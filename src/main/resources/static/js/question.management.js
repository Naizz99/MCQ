var id = null;
var currentQuestion = null;
var previousQuestion = null;
var questionId = null;

var answerCount = 0;
var questionCount = 0;
var currentQuestionCount = 0;
var allAnswers = 0;

var qImage = null;

$( document ).ready(function() {	
	answerCount = document.getElementById("answersPerQuestion").value;
    questionCount = document.getElementById("numberOfQuestion").value
    currentQuestionCount = document.getElementById("questionCount").value;  
    allAnswers = answerCount * currentQuestionCount;
   
   if(document.getElementById("maxAnswerCount") != null)
   {   
	document.getElementById("maxAnswerCount").value = document.getElementById("answersPerQuestion").value;
	}
    
});

//Show update form
function showUpdate(id,question){
	 //alert(id + question);
	 
	 //document.getElementById(q1).value = id;
	 document.getElementById(question).value = question;
}


//Set the correct answer for questions
function confirmation(obj) {
   		
	Swal.fire({
		  title: 'Are you sure?',
		  text: "Selected answer set as correct answer!",
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes, select it!'
		}).then((result) => {
		  if (result.isConfirmed) {
		    
		  }else{
			obj.checked = false
		  }
		})
}

//Enable the question box and submit button
function enableInputs(){
	document.getElementById('question').removeAttribute('disabled');
	
	document.getElementById('saveQuestion').removeAttribute('disabled');
}

//Disable the question box and submit button
function disableInputs(){
	document.getElementById("question").disabled = true;
	
	document.getElementById("saveQuestion").disabled = true;
}

function selectQuestion(pqId){
	
	document.getElementById('updateQuestion').removeAttribute('disabled');
	document.getElementById('deleteQuestion').removeAttribute('disabled');
	document.getElementById('answerList').removeAttribute('disabled');
	
	//document.getElementById(previousQuestion).setAttribute('readonly', true);
	
	
	previousQuestion = "question" + currentQuestion;
	currentQuestion = pqId;
	questionId = "question" + currentQuestion;
	var viewAnswersId = null;
	
	var viewAnswersId = "viewAnswers" + currentQuestion;
	document.getElementById(viewAnswersId).click();
	
	if((!(previousQuestion.includes("questionnull"))) && (previousQuestion != questionId)){
		document.getElementById(previousQuestion).setAttribute('readonly', true);
		
	}
	
	document.getElementById("currentQuestion").value = currentQuestion;
	document.getElementById("currentAnswer").value = "";
}

function selectAnswer(paId){	
	document.getElementById("currentAnswer").value = paId;
}

function questionUpdateButton(){
	
	document.getElementById(questionId).removeAttribute('readonly');
	
	document.getElementById("updateQuestion").setAttribute('disabled', '');
	document.getElementById('saveChanges').removeAttribute('disabled');
	//window.location = "/updateQuestionByQuestionId?pqId="+currentQuestion;	
}

function questionDeleteButton(){
	var pqIdId = "pqId" + currentQuestion;
	deleteQuestion(document.getElementById(pqIdId).value);
}

function questionAnswerButton(){
	var pqIdId = "pqId" + currentQuestion;
	window.location = "/listAnswersByPqId?pqId="+document.getElementById(pqIdId).value;	
}

function saveChanges(){
	   
	if(document.getElementById(questionId) != null){
		document.getElementById(questionId).setAttribute('readonly', true);
	}
	
	document.querySelector('#saveChanges').innerHTML = "Saving...";
    document.querySelector('#saveChanges').classList.add('spinning');
    
	questions = [];
	
	//Questions 
	for(var x = 1;x <= currentQuestionCount ; x++){
		var pqIdId = "pqId" + x;
		var questionIdId = "questionId" + x;
		var moduleIdId = "moduleId" + x;
		var queId = "question" + x;
		
		answers = [];
		
		//Answers
		for(var y = 0;y <= allAnswers;y++){
			var ansId = "question" + x + "answer"+ y;
			var ansIdId = "question" + x + "answerId"+ y;
			var ansStsId = "pq" + x + "rd"+ y;
			
			if(ansId.includes(queId)){
				try {
				  if(document.getElementById(ansId).value != null){
						var answer = JSON.stringify({
							paId		:	document.getElementById(ansIdId).value,
							answer		:	document.getElementById(ansId).value,
							answer_status		:	document.getElementById(ansStsId).checked
						});
						
						answers.push(answer);
				   }
				}
				catch(err) {
				  //alert(err);
				}
			}
		}
		
		var question = JSON.stringify({
			pqId		:	document.getElementById(pqIdId).value,
			questionId	:	document.getElementById(questionIdId).value,
			paperID		:	document.getElementById("PaperID").value,
			moduleId	:	document.getElementById(moduleIdId).value,
			question	:	document.getElementById(queId).value,
			active		:	null,
			answerList	:	answers
		});
		
		questions.push(question);
	}
		
	var questionList = {
		paperID : document.getElementById("PaperID").value,
		questionList : questions
	};
	
		
	$.ajax({
		url: '/secure/saveAllQuestion',
		type: 'POST',
		data: questionList,
		cache: false,
		success:function (res) {
			Swal.fire(
		      'Successful!',
		      'Your record has been updated',
		      'success'
		     ).then(function() {
			    document.querySelector('#saveChanges').classList.remove('spinning');
	         	document.querySelector('#saveChanges').innerHTML = "Save Changes";
			});			
		     			
		},
		error:function(status, error){
			Swal.fire(
		      'Something went wrong!',
		      'Questions Can not be save.',
		      'error'
		    );
		}
	});
	
}

$("#saveQuestion").click(function(){
	if(true){
		var isSuccess = false;
		var isAddQ = false;
		var pqId = null;
		
		var a = new FormData(); // using additional FormData object
		var b = [];             // using an array
		
		for(var i = 0; i < document.forms.length; i++){
			
			var f = document.forms[i];
			if(f.id == "newQuestion"){
			
				document.getElementById("saveQuestion").disabled = true;
				
				Swal.fire({
				  position: 'top-end',
				  icon: 'success',
				  title: 'New Question has been saved',
				  showConfirmButton: false,
				  timer: 10
				})
				var form = new FormData(document.forms[i]);
				$.ajax({
					url: '/secure/saveQuestion',
					type: 'POST',
					data: form,
					processData: false,
			        contentType: false,
					cache: false,
			        success:function (response) {
						document.getElementById("saveQuestion").disabled = false;
			        	isAddQ = true;
			        	saveAnswers();
					},
					error:function(status, error){
						document.getElementById("saveQuestion").disabled = false;
						isAddQ = false;
						Swal.fire(
					      'Something went wrong!',
					      'Question ID can not dupplicate or null.',
					      'error'
					    )
					}
				});	
				
			}
		}
		//document.getElementById("section").disabled = 'true';
	}
});

function saveAnswers(){
	for(var j = 0; j < document.forms.length; j++){
		var f = document.forms[j];
		if(f.id == "newAnswer"){
			Swal.fire({
			  position: 'top-end',
			  icon: 'success',
			  title: 'New Answer has been saved',
			  showConfirmButton: false,
			  timer: 5
			})
			var form = new FormData(document.forms[j]);
			$.ajax({
				url: '/secure/saveAnswer',
				type: 'POST',
				data: form,
				processData: false,
		        contentType: false,
				cache: false,
		        success:function (response) {
		        	isSuccess = true;
				},
				error:function(status, error){
					isSuccess = false;
				}
			});	
			Swal.fire({
			  position: 'top-end',
			  icon: 'success',
			  title: 'New Answer has been saved',
			  showConfirmButton: false,
			  timer: 5
			})
		}
	}
	window.location = "/listQuestionsByPaperId?paperId="+id;	
}

function deleteQuestion(id){
	
	Swal.fire({
	  title: 'Are you sure?',
	  text: "You won't be able to revert this!",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
	  if (result.isConfirmed) {
		$.ajax({
			url: 'deleteQuestion',
			type: 'GET',
			traditional: true,
			data: {pqId: id},
			success: function (response) {
			    window.location.reload();
			},
			error:function(status, error){
				 Swal.fire(
			      'Can not delete!',
			      'Record has relation data',
			      'error'
			    )
			}
		});
	  }
	})
}

//Read word
function readWord(){
	document.getElementById("PdfDiv").style.display = "none";
	document.getElementById("ImgDiv").style.display = "none";
	document.getElementById("SetImg").style.display = "none";
    document.getElementById("wordDiv").style.display = "block";
    
	var file = new FormData($('#fileData')[0]);
	
	console.log(file);
	
	$.ajax({
      url: '/secure/readWordFile',
      type: 'POST',
      data: file,
      async: false,
      cache: false,
      contentType: false,
      enctype: 'multipart/form-data',
      processData: false,
      success: function (res) {
	     	console.log(res)
			$("#wordDiv").show();
			var html="";
			html+=res.content;
			$("#wordDiv").html(html);
      },
      error:function(){
		Swal.fire(
	      'Can not Upload File!',
	      'Maximum count exceed',
	      'error'
	    )
	  }
   });
}

var readPDF = function(event) {
	document.getElementById("PdfDiv").style.display = "block";
	document.getElementById("ImgDiv").style.display = "none";
    document.getElementById("wordDiv").style.display = "none";
    document.getElementById("SetImg").style.display = "none";
    
    document.getElementById("PdfDiv").src = URL.createObjectURL(event.target.files[0]);
}

var readIMG = function(event) {
	document.getElementById("PdfDiv").style.display = "none";
	document.getElementById("ImgDiv").style.display = "block";
    document.getElementById("wordDiv").style.display = "none";
    document.getElementById("SetImg").style.display = "none";
    
    //alert(URL.createObjectURL(event.target.files[0]));
    document.getElementById("ImgDiv").src = URL.createObjectURL(event.target.files[0]);
};

function openQuestionImage(pqId){
	
	$.ajax({
		url: '/getQuestionImage',
		type: 'GET',
		traditional: true,
		data: {pqId: pqId},
		success: function (imagePath) {
			if(imagePath != null){
				//imagePath = "data:image/jpeg;base64," + imagePath;
								
				Swal.fire({
				  imageUrl: imagePath,
				  imageAlt: 'Custom image',
				})
				
			}else{
				Swal.fire(
			      'There are no image uploaded for question!',
			      'Upload image',
			      'error'
			    )	
			}
		},
		error:function(){
			Swal.fire(
		      'There are no image uploaded for question!',
		      'Upload image',
		      'error'
		    )
		}
	});
}

function openAnswerImage(paId){
	$.ajax({
		url: '/getAnswerImage',
		type: 'GET',
		traditional: true,
		data: {paId: paId},
		success: function (imagePath) {
			if(imagePath != null){
				//imagePath = "data:image/jpeg;base64," + imagePath;
								
				Swal.fire({
				  imageUrl: imagePath,
				  imageAlt: 'Custom image',
				})
				
			}else{
				Swal.fire(
			      'There are no image uploaded for question!',
			      'Upload image',
			      'error'
			    )	
			}
			
			
		},
		error:function(){
			Swal.fire(
		      'There are no image uploaded for question!',
		      'Upload image',
		      'error'
		    )
		}
	});
}

function openImageModel(quesId){
	var modelIdId = "imageModel" + quesId;
	var modelButtonId = "modelButton" + quesId; 
	document.getElementById(modelButtonId).dataset.target = "#" + modelIdId;
	document.getElementById(modelButtonId).click();
}

function uploadQuestionImage(){
	if((document.getElementById("currentQuestion").value != "") && ($('#SetImg').attr('src') != "")){
		var imageupload = $('#SetImg').attr('src');
		
		var file = document.getElementById("SetImg").src;
		var imageonly = file.split(';');
	    var contenttype = imageonly[0].split(':')[1];
	    fileformat = contenttype.split('/')[1];
	    var realdata = imageonly[1].split(',')[1];
	    var blob = b64toBlob(realdata, contenttype);
	    
		if(document.getElementById("currentAnswer").value != ""){
			var paIdValue = document.getElementById(questionId + "answerId" + (document.getElementById("currentAnswer").value - 1)).value;
			
			var date = new Date();
			var serial = "AnsNo" + paIdValue + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds();
			
			var img = {
				answerNo  : paIdValue,
				extention : document.getElementById("queImg").value.split('.').pop(),
				serial	  : serial,
				image	  : realdata
			}
			
			console.log(img);
			
			$.ajax({
				url: '/secure/saveAnswerImage',
				type: 'POST',
				data: img,
				cache: false,
				success:function (res) {
					Swal.fire(
				      'Successful!',
				      'Your record has been updated',
				      'success'
				     ).then(function() {
					    document.querySelector('#saveChanges').classList.remove('spinning');
			         	document.querySelector('#saveChanges').innerHTML = "Save Changes";
			         	document.getElementById("SetImg").style.display = "none";
					});			
				     			
				},
				error:function(status, error){
					Swal.fire(
				      'Something went wrong!',
				      'Questions Can not be save.',
				      'error'
				    );
				}
			});	
		}else{
			var date = new Date();
			var serial = "QNo" + document.getElementById("pqId"+currentQuestion).value + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds();
			
			var img = {
				reference   : document.getElementById("pqId"+currentQuestion).value,
				extention	: document.getElementById("queImg").value.split('.').pop(),
				serial		: serial,
				image		: realdata
			}
			
			console.log(img);
			
			$.ajax({
				url: '/secure/saveQuestionImage',
				type: 'POST',
				data: img,
				cache: false,
				success:function (res) {
					Swal.fire(
				      'Successful!',
				      'Your record has been updated',
				      'success'
				     ).then(function() {
					    document.querySelector('#saveChanges').classList.remove('spinning');
			         	document.querySelector('#saveChanges').innerHTML = "Save Changes";
			         	document.getElementById("SetImg").style.display = "none";
					});			
				     			
				},
				error:function(status, error){
					Swal.fire(
				      'Something went wrong!',
				      'Questions Can not be save.',
				      'error'
				    );
				}
			});	
		}
				   		
			
	}else{
		Swal.fire(
	      'Something went wrong!',
	      'Select Question and Upload Image',
	      'error'
	    );
	}
			
}









