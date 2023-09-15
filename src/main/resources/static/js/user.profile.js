var correctOtp = null;

$(document).ready(function(){
	document.getElementById("save").readOnly = true;
	//document.getElementById("otpButton").readOnly = true;
	    
	document.getElementById("ssoLabel").style.display = "none";
	document.getElementById("male").disabled = true;
	document.getElementById("female").disabled = true;
	document.getElementById("isSso").disabled = true;
});



function enableProfile(){
	document.getElementById("updateProfile").readOnly = true;
	document.getElementById("changePassword").readOnly = true;
	document.getElementById("save").readOnly = false;
	 
	document.getElementById("fullName").readOnly = false;
	//document.getElementById("username").readOnly = false;
	document.getElementById("email").readOnly = false;
	document.getElementById("phone").readOnly = false;
	document.getElementById("isSso").readOnly = false;
	document.getElementById("bio").readOnly = false;
	document.getElementById("male").disabled = false;
	document.getElementById("female").disabled = false;
	document.getElementById("isSso").disabled = false;
	
	document.getElementById("fullName").style.border = "1px solid gray";
	document.getElementById("username").style.border = "1px solid gray";
	document.getElementById("email").style.border = "1px solid gray";
	document.getElementById("phone").style.border = "1px solid gray";
	document.getElementById("bio").style.border = "1px solid gray";
	
	if(document.getElementById("newStudent") != null){
		document.getElementById("grade").readOnly = false;
		document.getElementById("grade").style.border = "1px solid gray";
	}
	
	if(document.getElementById("newLecturer") != null){
		document.getElementById("description").readOnly = false;
		document.getElementById("eduQualification").readOnly = false;
		document.getElementById("instituteName").readOnly = false;
		document.getElementById("nicNumber").readOnly = false;
		
		document.getElementById("description").style.border = "1px solid gray";
		document.getElementById("eduQualification").style.border = "1px solid gray";
		document.getElementById("educationQualification").style.border = "1px solid gray";
		document.getElementById("instituteName").style.border = "1px solid gray";
		document.getElementById("nicNumber").style.border = "1px solid gray";
	}
	
}

function cancel(){
	if(document.getElementById("newUser") != null){ document.getElementById("newUser").reset(); }
	if(document.getElementById("newStudent") != null){ document.getElementById("newStudent").reset();}
	
	document.getElementById("updateProfile").readOnly = false;
	document.getElementById("changePassword").readOnly = false;
	document.getElementById("save").readOnly = true;
	
	document.getElementById("fullName").readOnly = true;
	document.getElementById("username").readOnly = true;
	document.getElementById("ssoEmail").readOnly = true;
	document.getElementById("email").readOnly = true;
	document.getElementById("phone").readOnly = true;
	document.getElementById("isSso").readOnly = true;
	document.getElementById("bio").readOnly = true;
	document.getElementById("male").disabled = true;
	document.getElementById("female").disabled = true;
	document.getElementById("isSso").disabled = true;
	
	document.getElementById("fullName").style.border = "none";
	document.getElementById("username").style.border = "none";
	document.getElementById("ssoEmail").style.border = "none";
	document.getElementById("email").style.border = "none";
	document.getElementById("phone").style.border = "none";
	document.getElementById("bio").style.border = "none";
	
	if(document.getElementById("newStudent") != null){
		document.getElementById("grade").readOnly = true;
		document.getElementById("grade").style.border = "none";
	}
	
	if(document.getElementById("newLecturer") != null){
		document.getElementById("description").readOnly = true;
		document.getElementById("eduQualification").readOnly = true;
		document.getElementById("instituteName").readOnly = true;
		document.getElementById("nicNumber").readOnly = true;
		
		document.getElementById("description").style.border = "none";
		document.getElementById("eduQualification").style.border = "none";
		document.getElementById("educationQualification").style.border = "none";
		document.getElementById("instituteName").style.border = "none";
		document.getElementById("nicNumber").style.border = "none";
	}
}

function canselField(){
	cancel();
}

function saveProfile(){
	if(($("#fullName").val() !='') && ($("#email").val() !='') && ($("#phone").val() !='') && ($("#confirmPassword").val() =='')){
		var user = new FormData($('#newUser')[0]);
		$.ajax({
			url: '/secure/updateUser',
			type: 'POST',
			data: user,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				if(document.getElementById("newStudent") != null){
					var student = new FormData($('#newStudent')[0]);
					$.ajax({
						url: '/secure/updateStudent',
						type: 'POST',
						data: student,
						processData: false,
				        contentType: false,
						cache: false,
				        success:function (response) {
							saveGrade(($("#grade").val()),($("#userId").val()));
							Swal.fire(
						      'Successful!',
						      'Your record has been added',
						      'success'
						     ).then(function() {
								cancel();
						    	document.getElementById("ssoLabel").style.display = "block";
							 });
						}
					});
				}
				if(document.getElementById("newLecturer") != null){
					var lecturer = new FormData($('#newLecturer')[0]);
					$.ajax({
						url: '/secure/saveLecturer',
						type: 'POST',
						data: lecturer,
						processData: false,
				        contentType: false,
						cache: false,
				        success:function (response) {
							Swal.fire(
						      'Successful!',
						      'Your record has been added',
						      'success'
						     ).then(function() {
								cancel();
						    	document.getElementById("ssoLabel").style.display = "block";
							 });
						}
					});
				}else if((document.getElementById("newStudent") == null) && (document.getElementById("newLecturer") == null)){
					Swal.fire(
				      'Successful!',
				      'Your record has been added',
				      'success'
				     ).then(function() {
						cancel();
				    	document.getElementById("ssoLabel").style.display = "block";
					 });
				}
			}
		});	
			
	}else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
}

function saveTheam(type){
	
	var profile = {
		profileId : document.getElementById("profileId").value,
		bodyBgColor : document.getElementById("bodyBgColor").value,
		bodyFontFamily : document.getElementById("bodyFontFamily").value,
		sideBgColor : document.getElementById("sideBgColor").value,
		sideMenuTextColor : document.getElementById("sideMenuTextColor").value,
		sideSubMenuTextColor : document.getElementById("sideSubMenuTextColor").value,
		fontColor : document.getElementById("fontColor").value,
		pageTitle : document.getElementById("pageTitle").value,
		cardBgColor : document.getElementById("cardBgColor").value,
		cardSideColor1 : document.getElementById("cardSideColor1").value,
		cardSelectedColor : document.getElementById("cardSelectedColor").value,
		type:type
	}
	
	$.ajax({
		url: '/secure/updateProfile',
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

function saveGrade(grade,userId){
	$.ajax({
		url: '/updateGrade',
		type: 'GET',
		traditional: true,
		data: {grade:grade,userId:userId},
		success: function (response) {
			
		},
		error:function(status, error){
			
		}
	});
}

function sendOtp() { 
	correctOtp = null;
	if($("#username").val() !=''){
		
		//var emailAddress = document.getElementById("email").value;
		var username = document.getElementById("username").value;
		$.ajax({
			url: '/sendOtp',
			type: 'GET',
			traditional: true,
			data: {username: username},
			success: function (response) {
				document.querySelector('#otpButton').style.backgroundColor = 'orange';
				document.querySelector('#otpButton').style.color = 'black';
				document.querySelector('#otpButton').innerHTML = "Resend";
				document.getElementById('otp').readOnly = false;
				
				correctOtp = response["otp"];
				
			},
			error:function(status, error){
				 Swal.fire(
			      'Can not process!',
			      'There are no user for this email',
			      'error'
			    )
			}
		});
	}
}

function checkOtp(){
	if(document.getElementById("otp").value == correctOtp){
		document.getElementById('otpButton').readOnly = false;
		document.getElementById('otp').readOnly = true;
		
		document.getElementById("password").readOnly = false;
		document.getElementById("confirmPassword").readOnly = false;
	}else{
		document.getElementById("otp").setCustomValidity("OTP Don't Match");
		document.getElementById("password").readOnly = true;
		document.getElementById("confirmPassword").readOnly = true;
	}
}

function checkPassword(){
	if(document.getElementById("password").value == document.getElementById("confirmPassword").value){
		//document.getElementById('save').readOnly = false;
		//document.getElementById("password").readOnly = true;
		//document.getElementById("confirmPassword").readOnly = true;
		document.getElementById("passordStatus").innerHTML = "<i style='color:green' class='bi bi-check-circle-fill'></i>";
	}else if(document.getElementById("confirmPassword").value == ""){
		document.getElementById("passordStatus").innerHTML = "";
	}else{
		//document.getElementById('save').readOnly = true;
		document.getElementById("passordStatus").innerHTML = "<i style='color:red' class='bi bi-x-circle-fill'>Password Don't Match</i>";
	}
}

function enableSSO(value){
	if(value){
		document.getElementById('ssoEmail').readOnly = false;
		document.getElementById("ssoEmail").style.border = "1px solid gray";
		
		document.getElementById("ssoLabel").value = "";
	}else{
		document.getElementById('ssoEmail').value = "";
		document.getElementById('ssoEmail').readOnly = true;
		document.getElementById("ssoEmail").style.border = "none";
		
		document.getElementById("ssoLabel").innerHTML = "* Change the password to login with username & password";
		document.getElementById("ssoLabel").style.color = "red";
	}
	
}