$(document).ready(function(){
 	update();
 	if(document.getElementById("package")){
		if(document.getElementById("package").selectedIndex){
			selectPackage(document.getElementById("package").options[document.getElementById("package").selectedIndex]);
	 	}else{
			changeDate();
		}
	}
});

function update() {
	var listBox = document.getElementById("subject");
	if(listBox != null){
		listBox.options.length = 0;
		var selectGrade = document.getElementById('grade').value;
		if(selectGrade != ''){
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
					}else{
						for (let x in subjects) {
							var subject = subjects[x];
							for (let y in subject) {
								var subjectId = subject["subjectId"];
								var subjectName = subject["subjectName"];
								
					            $('#subject').append(`<option value="${subjectId}">${subjectName} </option>`);
								break;
							};
							
						};	
					}
				}
			});	
		}
	}
}

$("#saveClassRoom").click(function () {
	if(($("#lecturerId").val() !='') && ($("#name").val() !='') && ($("#password").val() !='') && ($("#grade").val() !='') && ($("#subject").val() !='')){
		document.getElementById("saveClassRoom").disabled = true;
		var form = new FormData($('#newClassRoom')[0]);
		$.ajax({
			url: '/secure/saveClassRoom',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (classroom) {
				document.getElementById("saveClassRoom").disabled = false;
				window.location = "/paymentCheckout?type=classroom&id="+classroom["classroomId"];
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				 document.getElementById("saveClassRoom").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Required fields can not be null',
		  footer: ''
		})
	}
});

function deleteClassRoom(id){
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
				url: '/deleteClassRoom',
				type: 'GET',
				traditional: true,
				data: {classroomId: id},
				success: function (response) {
				    window.location = "/listClassRooms";
				},
				error:function(status, error){
					 Swal.fire(
				      'Can not delete!',
				      'Something went wrong',
				      'error'
				    )
				}
			});	
		}
	})
}

function classRoomActive(id){
	
	$.ajax({
		url: '/classRoomActive',
		type: 'GET',
		traditional: true,
		data: {classroomId: id},
		success: function (response) {
			if(response == "expired"){
				Swal.fire(
				  'Expired!',
				  'Can not Activate the Classroom!',
				  'error'
				)
			}else{
				window.location.reload();	
			}
		},
		error:function(status, error){
		}
	});
}

function classRoomDeactive(id){
	$.ajax({
		url: '/classRoomDeactive',
		type: 'GET',
		traditional: true,
		data: {classroomId: id},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
		}
	});
}

function stopClassRoom(id){
	$.ajax({
		url: '/stopClassRoom',
		type: 'GET',
		traditional: true,
		data: {classroomId: id},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
		}
	});
}

function startClassRoom(id){
	$.ajax({
		url: '/startClassRoom',
		type: 'GET',
		traditional: true,
		data: {classroomId: id},
		success: function (response) {
			if(response == "expired"){
				Swal.fire(
				  'Expired!',
				  'Can not Start the Group!',
				  'error'
				)
			}else if(response == "notActive"){
				Swal.fire(
				  'Deactive!',
				  'Can not Start until activate the Group!',
				  'error'
				)
			}else{
				window.location.reload();	
			}
		},
		error:function(status, error){
		}
	});
}

function addClassRoomStudent(classroomid){
	var classroom = classroomid;
	var student = document.getElementById("stdEmail").value;
	var userid = document.getElementById("stdId").value;
	//alert("Group | " + classroom + " student | " + student);
	
	$.ajax({
		url: '/addClassRoomStudent',
		type: 'GET',
		traditional: true,
		data: {classroomId: classroom,studentEmail: student,userid: userid},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
	
}

function removeStudent(id){
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
				url: '/removeStudent',
				type: 'GET',
				traditional: true,
				data: {classroomStudentId: id},
				success: function (response) {
				    window.location.reload();
				},
				error:function(status, error){
					 Swal.fire(
				      'Something went wrong',
				      'error'
				    )
				}
			});	
		}
	})
}

function selectDiv(select){
	if(select.dataset.isselect == "false"){
		select.style.backgroundColor = "#ffcc99";	
		select.dataset.isselect = "true";
	}else{
		select.style.backgroundColor = "#fff";	
		select.dataset.isselect = "false";
	}
}

function addPapers(type){
	var parent = document.getElementById("left");
	var papers = [];
	var bundles = [];
	//console.log(parent.childNodes);
	
	var childs = document.getElementById('left').childNodes;
	var len = childs.length, i = -1;
	if(++i < len) do {
	    if(childs[i].nodeName == "DIV"){
			//console.log(childs[i].getAttribute("data-isselect") + " | " + childs[i].getAttribute("data-value"));
			if(childs[i].getAttribute("data-isselect") == "true"){
				if(childs[i].getAttribute("data-type") == "p"){
					paper = {
							paperId : childs[i].getAttribute("data-value")
					}

					papers.push(paper);	
				}else if(childs[i].getAttribute("data-type") == "b"){
					bundles.push(childs[i].getAttribute("data-value"));	
				}
			}
		}
	} while(++i < len);
	
	var selectedPapers = {
		papers : papers
	}
	
	var selectedBundles = {
		idList : bundles
	}
	/*
	$.ajax({
		url: '/addClassRoomPaperList',
		type: 'GET',
		traditional: true,
		data: {classroomId: document.getElementById("classroomId").value,selectedPapers:selectedPapers,selectedBundles:selectedBundles},
		success: function (response) {
			Swal.fire(
		      'Successful!',
		      'Selected Papers has been added to the group',
		      'success'
		    ).then(function() {
			    window.location.reload();
			});
		},
		error:function(status, error){
			Swal.fire(
		      'Something went wrong!',
		      'error'
		    )
		}
	});
	*/
	/*
	$.ajax({
		url: '/secure/addClassRoomPaperList',
		type: 'GET',
		data: {papers:papers},
		processData: false,
        contentType: false,
		cache: false,
        success:function (classroom) {
			Swal.fire(
		      'Successful!',
		      'Selected Papers has been added to the group',
		      'success'
		    ).then(function() {
			    window.location.reload();
			});
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong!',
		      'error'
		    )
		}
	});	
	*/	
	
	var paperList ={}; 
	paperList["papers"] = papers;
	$.ajax({
		url: "/secure/addClassRoomPaperList",
        type: "POST",
        ContentType: "json",
        data: JSON.stringify(paperList),
        processData: false,
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        success: function(data){
        	window.location.reload();
     	},
 		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	 });
	 /*
	 $.ajax({
		url: '/addClassRoomPaperList',
		type: 'GET',
		traditional: true,
		data: {papers:papers},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});	
	*/
 }

function addClassRoomPaper(classroom,paper,bundle){
	$.ajax({
		url: '/addClassRoomPaper',
		type: 'GET',
		traditional: true,
		data: {classroomId: classroom,paperId: paper,bundleId:bundle},
		success: function (response) {
			Swal.fire(
		      'Successful!',
		      'New Paper has been added to the group',
		      'success'
		    ).then(function() {
			    window.location.reload();
			});
		},
		error:function(status, error){
			Swal.fire(
		      'Paper has allready added to the group',
		      'error'
		    )
		}
	});
}

function deactiveClassRoomPaper(classroomPaper){
	$.ajax({
		url: '/deactiveClassRoomPaper',
		type: 'GET',
		traditional: true,
		data: {classroomPaperId: classroomPaper},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function activeClassRoomPaper(classroomPaper){
	$.ajax({
		url: '/activeClassRoomPaper',
		type: 'GET',
		traditional: true,
		data: {classroomPaperId: classroomPaper},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function removeClassRoomPaper(classroomPaper){
	Swal.fire({
	  title: 'Are you sure?',
	  text: "You won't be able to revert this!",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes, remove it!'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: '/removeClassRoomPaper',
				type: 'GET',
				traditional: true,
				data: {classroomPaperId: classroomPaper},
				success: function (response) {
				    window.location.reload();
				},
				error:function(status, error){
					 Swal.fire(
				      'Something went wrong',
				      'error'
				    )
				}
			});	
		}
	})
}

function sessionClose(id){
	$.ajax({
		url: '/sessionClose',
		type: 'GET',
		traditional: true,
		data: {classroomStudentId: id},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function sendRequest(id){
	$.ajax({
		url: '/sendRequest',
		type: 'GET',
		traditional: true,
		data: {classroomStudentId: id},
		success: function (response) {
		    Swal.fire(
		      'Successful!',
		      'Request has been sent!',
		      'success'
		     ).then(function() {
			    window.location.reload();
			 });
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function generateReport(id){
	//alert(id);
}

function selectPackage(duration){
	document.getElementById('packageDuration').value = duration.getAttribute("data-duration");
	changeDate();
}

$('#startDate').attr('min', new Date().toISOString().split('T')[0]);

function changeDate(){
	var sDate = document.getElementById("startDate").value;
	var duration = parseInt(document.getElementById("packageDuration").value);
	var startDate = new Date(sDate);
	//$('#startDate1').val(startDate.getFullYear() + "/" + (parseInt(startDate.getMonth()) + 1) + "/" + startDate.getDate());
	startDate.setDate(startDate.getDate() + duration);
	var endDate = startDate.getFullYear() + "/" + (parseInt(startDate.getMonth()) + 1) + "/" + startDate.getDate();
	var displayDate = (parseInt(startDate.getMonth()) + 1) + " / " + startDate.getDate() + " / " + startDate.getFullYear();
	$('#endDate').val(displayDate);
	
}

function getStudents(email){
	
	var resultDiv = document.getElementById("studentDiv");
	
	if(email != ''){
		$.ajax({
			url: '/getStudentListByEmail',
			type: 'GET',
			traditional: true,
			data: {email: email},
			success: function (studentList) {
			    if (studentList === undefined || studentList.length == 0) {
				    resultDiv.innerHTML = "<p>There are no students for this mail</p>";
				    document.getElementById("stdId").value = 0;
				}else{
					resultDiv.innerHTML = "";
					for(x=0; x < studentList.length; x++){
						resultDiv.innerHTML += "</br><p class='card border border-secondary p-2' onclick=setStudent('" + studentList[x]["userId"] +"')>" + studentList[x]["name"] + "</p>";
					}
				}
			},
			error:function(status, error){
				 document.getElementById("stdId").value = 0;
			}
		});	
	}
}

function setStudent(id){
	document.getElementById("stdId").value = id;		
	document.getElementById("add").click();
}

var dd = false;
function checkClassRoomStudentAvailability(email){
	
	var classroomId = document.getElementById("classroomId").value;
	
	$.ajax({
		url: '/checkClassRoomStudentAvailability',
		type: 'GET',
		traditional: true,
		data: {email: email,classroomId: classroomId},
		success: function (dupplicate) {
			dd = dupplicate;
		    if (dupplicate) {
			    document.getElementById("sts").innerText = "Email is already use in the classroom. Use another email.";
			    document.getElementById("sts").style.color="red";
			    document.getElementById("add").disabled = true;
			}else{
				document.getElementById("sts").innerText = "Enter valid email address. Login details will be send to this email.";
				document.getElementById("sts").style.color="#a6a6a6";
			    document.getElementById("add").disabled = false;
			}
		},
		error:function(status, error){ 
		}
	});	
}

function registerClassRoomStudent(classroomId,email){
	checkClassRoomStudentAvailability(email);
	if(!dd){
		$.ajax({
			url: '/registerClassRoomStudent',
			type: 'GET',
			traditional: true,
			data: {classroomId: classroomId,email: email},
			success: function (response) {
				if(response == "Dupplicate"){
					document.getElementById("sts").innerText = "Email is already register in the system.Please login to the system for join classroom.";
				    document.getElementById("sts").style.color="red";
				}else{
					if(!dd)
						window.location.reload();	
				}
			},
			error:function(status, error){
				 Swal.fire(
			      'Something went wrong',
			      'error'
			    )
			}
		});	
	}	
}

function saveTheam(classroomId,type){
	//alert("GroupId : " + classroomId + " | type : " + type);
	
	var profile = {
		profileId : classroomId,
		bodyBgColor : document.getElementById("bodyBgColor").value,
		bodyFontFamily : document.getElementById("bodyFontFamily").value,
		fontColor : document.getElementById("fontColor").value,
		cardBgColor : document.getElementById("cardBgColor").value,
		cardSideColor : document.getElementById("cardSideColor").value,
		cardSelectedColor : document.getElementById("cardSelectedColor").value,
		type:type
	}
	//console.log("profile | " + profile);
	$.ajax({
		url: '/secure/updateClassRoomTheme',
		type: 'POST',
		data: profile,
		cache: false,
        success:function (response) {
			window.location.reload();	
		},
		error:function(status, error){
			Swal.fire({
				  icon: 'error',
				  title: 'Something went wrong!',
				  text: 'Can not Update!',
				  footer: ''
			 })
		}
	});
	
}



