var otpNumber = 0;

$(document).ready(function() {
  hideFields();
});

function getStudents(email,parentId){
	hideFields();
	$.ajax({
		url: '/getStudentsByEmail',
		type: 'GET',
		data: {input : email , parentId : parentId},
		cache: false,
		success:function (studentList) {
			const select = document.getElementById("students"); 
			select.innerHTML = '';
			
			for (let x in studentList) {	
				console.log(studentList[x]);	
				if(!studentList[x]['linked']){
					const newOption = document.createElement('option');
					const optionText = document.createTextNode(studentList[x]['name'] + " | " + studentList[x]['grade']);
					newOption.appendChild(optionText);
					newOption.setAttribute('value',studentList[x]['studentId']);
					newOption.setAttribute('onClick','searchStudent(this.value,"' + studentList[x]["parentEmail"] + '")');
					
					const select = document.querySelector('select'); 
					select.appendChild(newOption);
				}	
			}
		},
		error:function(status, error){
			Swal.fire(
		      'Student not found!',
		      'There are no students registered to this email',
		      'error'
		    );
		}
	});	
}

function searchStudent(studentId,parentEmail){
	otpNumber = 0;
	$.ajax({
		url: '/searchStudent',
		type: 'GET',
		data: {studentId : studentId},
		cache: false,
		success:function (student) {
			
			document.getElementById("stdName").value = student['userId']['name'];
			document.getElementById("sEmail").value = student['userId']['email']
			document.getElementById("sId").value = student['userId']['userId'];;
			document.getElementById("pEmail").value = parentEmail;
			document.getElementById("stdName").value = student['userId']['name'];
			document.getElementById("stdMobile").value = student['userId']['mobile'];
			document.getElementById("stdGrade").value = student['grade']['gradeName'];	
			
			document.getElementById('stdDetails').style.display = 'block';
			document.getElementById('btnOtp').style.display = 'block';
			document.getElementById('btnOtp').disabled = false;
			document.getElementById('otp').value = "";
			document.getElementById('otp').disabled = false;
		     			
		},
		error:function(status, error){
			Swal.fire(
		      'Student not found!',
		      'There are no students registered to this email',
		      'error'
		    );
		}
	});	
}

function confirmDetails(){
	studentEmail = document.getElementById('sEmail').value;
	parentEmail = document.getElementById('pEmail').value;
	document.getElementById('btnOtp').innerText = "Send Again";
	$.ajax({
		url: '/confirmStudent',
		type: 'GET',
		data: {studentEmail : studentEmail , parentEmail : parentEmail},
		cache: false,
		success:function (otp) {
			otpNumber = otp;
			document.getElementById('otpDetails').style.display = 'block'; 	
			document.getElementById('btnOtp').innerText = "Confirm Details";	
			document.getElementById('btnOtp').disabled = true;	
		},
		error:function(status, error){
			Swal.fire(
		      'Something went wrong!',
		      'Pease contact the administration',
		      'error'
		    );
		}
	});	
}

function addStudent(parentId,studentId,email){
	$.ajax({
		url: '/linkStudent',
		type: 'GET',
		data: {parentId : parentId , studentId : studentId , email : email},
		cache: false,
		success:function (res) {
			if(res == "Success"){
				window.location.reload();
			}else if(res == "Dupplicate"){
				Swal.fire(
			      'Dupplicate student!',
			      'User has already added to the system',
			      'error'
			    );
			}
			
		},
		error:function(status, error){
			Swal.fire(
		      'Something went wrong!',
		      'Pease contact the administration',
		      'error'
		    );
		}
	});	
	hideFields();
}

function unLinkStudent(parentId,studentId){
	Swal.fire({
	  title: 'Are you sure to remove student from parent?',
	  text: "You won't be able to revert this!",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes, remove it!'
	}).then((result) => {
	  if (result.isConfirmed) {
		$.ajax({
			url: '/unLinkStudent',
			type: 'GET',
			data: {parentId : parentId , studentId : studentId},
			cache: false,
			success:function (res) {
				window.location.reload();
			},
			error:function(status, error){
				Swal.fire(
			      'Something went wrong!',
			      'Pease contact the administration',
			      'error'
			    );
			}
		});	
		hideFields();
	  }
	})
}

function hideFields(){
	document.getElementById('stdDetails').style.display = 'none';
	document.getElementById('otpDetails').style.display = 'none';
	document.getElementById('btnOtp').style.display = 'none';
	
	document.getElementById('addStudent').disabled = true;
}

function checkOtp(){
	if((otpNumber != 0) && (document.getElementById("otp").value == otpNumber)){
		document.getElementById('addStudent').disabled = false;
		document.getElementById('otp').disabled = true;
	}else{
		document.getElementById("otp").setCustomValidity("OTP Don't Match");
	}
}