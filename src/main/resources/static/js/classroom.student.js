var ansQues = 0.
var time = 0;
var result = 0;
clicked = false;

$(document).ready(function(){

	var time = 60 * document.getElementById("paperTime").value;
	
  	//document.getElementById("availableAtp").value = document.getElementById("totAtp").value;
  	
  	timeCalc(time);
  	
});

function mark(id){
	clicked = true;
	document.getElementById("done").style.display="none";
	//document.getElementById("availableAtp").value = document.getElementById("availableAtp").value - 1;
	
	var allQues = 0;
	var correctQues = 0;
	
	$.ajax({
		url: '/getPaperWithCorrectAnswers',
		type: 'GET',
		traditional: true,
		data: {paperId: id},
		success: function (answeredPaper) {
			for (let x in answeredPaper["questionList"]){

				allQues++;
				var index = (answeredPaper["questionList"][x])["paId"];
				if(document.getElementById(index).checked){
					correctQues++;					
				}	
				
				var answerList = (answeredPaper["questionList"][x])["answerList"];
				var givenAnswer = 0;
				for(y=0;y<answerList.length;y++){
					var ansId = answerList[y]["paId"];
					if(document.getElementById(ansId).checked){
						givenAnswer = ansId; 		
					}
				}

				var ClassroomPaperReviewDetailDto = {
					paperID	: answeredPaper["paperID"],
					questionId	: (answeredPaper["questionList"][x])["pqId"],
					moduleId	: (answeredPaper["questionList"][x])["moduleId"],
					correctAnswer: (answeredPaper["questionList"][x])["paId"],
					givenAnswer	: givenAnswer,
					studentId	: document.getElementById("studentId").value,
					reviewId	: document.getElementById("cprId").value,
					result	: 0,
					status	:document.getElementById(index).checked
				}
				
				$.ajax({
					url: '/secure/saveClassRoomReviewDetail',
					type: 'POST',
					data: ClassroomPaperReviewDetailDto,
					cache: false,
					success:function (response) {
						console.log("Success");
					},error:function(status, error){
						console.log("Error");
					}
				});		
			}	
			
			if(correctQues == 0 & allQues == 0){
				result = 0;	
			}else{
				result = Math.round(((correctQues / allQues) * 100), 2);	
			}
					
			document.getElementById("atpResults1").value = result;
			document.getElementById("atpResults2").value = result;
			saveReview();
			
			if((correctQues != 0) && (correctQues == allQues)){
				Swal.fire({
				  text: 'Total Marks ' + result + '. You have done well!',
				  title: "Your Result is " + correctQues + "/" + allQues,
				  imageUrl: '/img/clapping.jpg',
				  imageWidth: 400,
				  imageHeight: 200,
				  imageAlt: 'Custom image',
				}).then((result) => {
					//window.location = "/student/classroomLogin?classroomStudentId=" + document.getElementById("classRoomStudentId").value;
					window.location = "/reviewClassRoomPaper?reviewId="+document.getElementById("cprId").value;
				})
				
				document.getElementById("try1").style.display="none";
				document.getElementById("done").disabled = true;
			}else { 
				
				Swal.fire({
				  text: 'Total Marks ' + result + '. Please Try again.',
				  title: "Your Result is " + correctQues + "/" + allQues,
				  imageUrl: '/img/tryAgain.jpg',
				  imageWidth: 400,
				  imageHeight: 200,
				  imageAlt: 'Custom image',
				}).then((result) => {
				  	//window.location = "/student/classroomLogin?classroomStudentId=" + document.getElementById("classRoomStudentId").value;
				  	window.location = "/reviewClassRoomPaper?reviewId="+document.getElementById("cprId").value;
				})
				
				document.getElementById("quizpage").style.display="none";
				document.getElementById("try1").style.display="block";
			}
		}
			
	});
}

function tryagain(){
  clicked = false;
  document.getElementById("quizpage").style.display="block";
  document.getElementById("try1").style.display="none";
  quizpage.removeAttribute('disabled');
  document.getElementById("done").style.display="block";
  //location.reload(true);
  
  var time = 60 * document.getElementById("paperTime").value;
  timeCalc(time);  
}

function timeCalc(time){
	countDown(time);
  	
  	setTimeout(() => {
		
	  const box = document.getElementById('box');	 
	}, (time * 1000)); // 👈️ time in milliseconds
}

function timeStop(){
	clearInterval(myInterval);
}

function countDown(counter){
	var span = document.querySelector("#count");
	var interval = setInterval(function(){
		
		var hours = ((counter / 3600) | 0) + "";
		var minutes = (((counter-(hours * 3600)) / 60) | 0) + "";
		var seconds = (counter % 60) + "";
		var format = ""
					  + new Array(3-hours.length).join("0") + hours 
					  + ':' 
					  + new Array(3-minutes.length).join("0") + minutes 
					  + ':' 
					  + new Array(3-seconds.length).join("0") + seconds;

		span.innerHTML = format;

		if(counter <= 60){
			//span.style.color=='red'?'white':'red';
			let color = span.style.color;
	         if (color == "red") { // if button color is red change it green otherwise change it to red.
	            span.style.color = 'white';
	         } else {
	            span.style.color = 'red';
	         }
		}else if(counter <= 600){
			span.style.color = "red";
		}else{
			span.style.color = "green";
		}
		
		counter--;
		
		if(counter === 0){
			clearInterval(interval);
			
			if(!clicked){
				let timerInterval
				Swal.fire({
				  title: 'Time Has Been Finished!',
				  html: 'Please Try Again',
				  timer: 3000,
				  timerProgressBar: true,
				  didOpen: () => {
				    Swal.showLoading()
				    const b = Swal.getHtmlContainer().querySelector('b')
				    timerInterval = setInterval(() => {
				      //b.textContent = Swal.getTimerLeft()
				    }, 100)
				  },
				  willClose: () => {
				    clearInterval(timerInterval)
				  }
				}).then((result) => {
				  /* Read more about handling dismissals below */
				  if (result.dismiss === Swal.DismissReason.timer) {
				    console.log('I was closed by the timer')
				    
				    $('quizpage').click(false);
				
					var id = document.getElementById("paperId").value;
					document.getElementById("done").addEventListener("click", mark(id));
					
					document.getElementById("done").style.display="none";
				  }
				})	
			}
		}else if(clicked){
			clearInterval(interval);
		}
		
	},1e3)
}

function saveReview(){
	
	var form = new FormData($('#newReview')[0]);
			
	$.ajax({
		url: '/secure/saveClassRoomReview',
		type: 'POST',
		data: form,
		processData: false,
        contentType: false,
		cache: false,
        success:function (response) {
			
		},error:function(status, error){
			
		}
	});
}

let slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
	var next = Number(slideIndex) + Number(n);
	document.getElementById(slideIndex).style.border = "1px outset";
	
    showSlides(slideIndex = Number(slideIndex) + Number(n));
}

function currentSlide(QID) {
	document.getElementById(slideIndex).style.border = "1px outset";
	
	document.getElementById("prev").style.display="block";
	document.getElementById("next").style.display="block";
	showSlides(slideIndex = QID);
}

function showSlides(n) {
  let i;
  let slides = document.getElementsByClassName("mySlides");
  let dots = document.getElementsByClassName("dot");
  if (n > slides.length) {
	  slideIndex = 1;
	  for (i = 0; i < slides.length; i++) {
	    slides[i].style.display = "none";  
	  }
	  slides[slideIndex-1].style.display = "block";
  }
  if (n < 1) {
	  slideIndex = slides.length;
	  for (i = 0; i < slides.length; i++) {
	    slides[i].style.display = "none";  
	  }
	  slides[slideIndex-1].style.display = "block";
  }

  document.getElementById(slideIndex).style.border = "solid #0000FF";
  
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";  
  }
  
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  
  slides[slideIndex-1].style.display = "block";  
  dots[slideIndex-1].className += " active";
  
}

function flagQuestion(QID){
	if(document.getElementById(QID).value == QID ){
		document.getElementById(QID).value = QID + ' ';
		document.getElementById('flag'+QID).textContent = "Remove Remark";
		document.getElementById(QID).style.backgroundColor = '#ff4d4d';
	}else{
		document.getElementById(QID).value = QID;	
		document.getElementById('flag'+QID).textContent = "Do it Later"
		document.getElementById(QID).style.backgroundColor = '#e6faff';
	}
}

function review(){
	let i;
	let slides = document.getElementsByClassName("mySlides");
	
	document.getElementById("prev").style.display="none";
	document.getElementById("next").style.display="none";
	
	for (i = 0; i < slides.length; i++) {
	   slides[i].style.display = "block";  
	}
}

function hideShow(id){
	const classCheck = document.getElementById(id);
	const questionDiv = document.getElementById("questionWizardDiv")
	const questionNumberDiv = document.getElementById("numberWizardDiv")

	if (classCheck.classList.contains('bi-eye-slash-fill')) {
		classCheck.classList.remove("bi-eye-slash-fill");
		classCheck.classList.add("bi-eye-fill");
		questionDiv.classList.remove("col-lg-8");
		questionDiv.classList.add("col-lg-11");
		questionNumberDiv.classList.remove("col-lg-4");
		questionNumberDiv.classList.add("col-lg-1");
	} else {
		classCheck.classList.add("bi-eye-slash-fill");
		classCheck.classList.remove("bi-eye-fill");
		questionDiv.classList.add("col-lg-8");
		questionDiv.classList.remove("col-lg-11");
		questionNumberDiv.classList.add("col-lg-4");
		questionNumberDiv.classList.remove("col-lg-1");
	}
}

