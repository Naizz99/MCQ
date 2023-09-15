function classroomLogin(){
	
	var classroomId = document.getElementById("classroomId").value;
	var username = document.getElementById("studentId").value;
	var passcode = document.getElementById("passcode").value;
	//window.location = "/classRoomLogin?classroomId="+ classroomId + "&studentEmail=" + studentEmail +  "&passcode=" + passcode;
		
	$.ajax({
		url: '/classRoomLogin',
		type: 'GET',
		traditional: true,
		data: {logginId : classroomId, username : username , passcode : passcode },
		success:function (response) {
			if(response == 'not-started'){
				Swal.fire(
			      'Can not login to group!',
			      'Group did not start yet',
			      'error'
			    )
			}else if(response == 'password-incorrect'){
				Swal.fire(
			      'Can not login to group!',
			      'Entered passcode is invalid',
			      'error'
			    )
			}else if(response != null){
				window.location = "/student/classroomLogin?classroomStudentId=" + response;
				//window.location = "/forgottenPassword";
			}else{
				Swal.fire(
			      'Can not process!',
			      'Something went wrong',
			      'error'
			    )
			}
		},
		error:function(status, error){
			Swal.fire(
		      'Can not login to group!',
		      'Something went wrong',
		      'error'
		    )
		}
	});	
}

function sendOtp() { 
	if($("#username").val() !=''){
		//var emailAddress = document.getElementById("emailAddress").value;
		var username = document.getElementById("username").value;
		$.ajax({
			url: '/sendOtp',
			type: 'GET',
			traditional: true,
			data: {username: username},
			success: function (response) {
				
				if(response["isActive"] == 'false'){
					document.getElementById("alert").innerText = '* Account has been Deactivated! Please contact the admin.';
					document.getElementById("alert").style.color = 'red';
				}else{
					document.getElementById("alert").innerText = '';
				}
				document.querySelector('#sendOtp').style.backgroundColor = 'orange';
				document.querySelector('#sendOtp').style.color = 'black';
				document.querySelector('#sendOtp').innerHTML = "Resend";
				document.getElementById('otp').disabled = false;
				
				document.getElementById("correctOtp").value = response["otp"];
				document.getElementById("email").value = response["email"];
				document.getElementById("userId").value = response["userId"];
			},
			error:function(status, error){
				document.getElementById("alert").innerText = '* There are no account for this username.';
				document.getElementById("alert").style.color = 'red';
				 /*
				 Swal.fire(
			      'Can not process!',
			      'There are no user for this email',
			      'error'
			    )*/
			}
		});
	}
}

function checkOtp(){
	if(document.getElementById("otp").value == document.getElementById("correctOtp").value){
		document.getElementById('password').disabled = false;
		document.getElementById('confirmPassword').disabled = false;
		document.getElementById('submit').disabled = false;
		document.getElementById('otp').disabled = true;
	}else{
		document.getElementById("otp").setCustomValidity("OTP Don't Match");
		document.getElementById('password').disabled = true;
		document.getElementById('confirmPassword').disabled = true;
	}
}

function passwordChange(){
	if((document.getElementById("password").value != '') && (document.getElementById("confirmPassword").value != '') && (document.getElementById("confirmPassword").value == document.getElementById("password").value)){
		
		userId = document.getElementById("userId").value;
		password = document.getElementById("password").value;
		
		$.ajax({
			url: '/changePassword',
			type: 'GET',
			traditional: true,
			data: {userId: userId , pwd: password , type: 'forgot'},
			success: function (response) {
				Swal.fire(
			      'Successful!',
			      'Your password has been changed',
			      'success'
			     ).then(function() {
				    window.location = "/login";
				 });
			},
			error:function(status, error){
				 Swal.fire(
			      'Can not process!',
			      'Something went wrong',
			      'error'
			    )
			}
		});
		
	}else{
		Swal.fire(
	      'Can not submit!',
	      'Password mismatch',
	      'error'
	    )
	}
}















