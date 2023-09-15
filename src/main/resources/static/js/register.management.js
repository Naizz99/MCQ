function studentRegister(){
	if($("#grade").val() !='' && $("#stdName").val() !='' && $("#stdGender").val() !='' && $("#stdUsername").val() !='' && $("#password").val() !='' && $("#confirmPassword").val() !='' && ($("#password").val() == $("#confirmPassword").val())){
		var student = {
			stdGrade : document.getElementById("grade").value,
			stdGPA 	 : 0,
			stdName  : document.getElementById("stdName").value,
			prtName  : document.getElementById("prtName").value,
			stdGender: document.getElementById("stdGender").value,
			prtGender: document.getElementById("prtGender").value,
			stdEmail : document.getElementById("stdEmail").value,
			prtEmail : document.getElementById("prtEmail").value,
			stdMobile : document.getElementById("stdMobile").value,
			prtMobile : document.getElementById("prtMobile").value,
			stdUsername : document.getElementById("stdUsername").value,
			stdPassword : document.getElementById("password").value
		};
		$(':button').prop('disabled', true);
		$.ajax({
			url: '/secure/registerStudent',
			type: 'POST',
			data: student,
			cache: false,
	        success:function (response) {
				$(':button').prop('disabled', false);
				Swal.fire(
			      'Successful!',
			      'You have been registered',
			      'success'
			     ).then(function() {
				    window.location = "/login";
				 });
			},
			error:function(status, error){
				 $(':button').prop('disabled', false);
				 Swal.fire({
					  icon: 'error',
					  title: 'Something went wrong!',
					  text: 'Can not register!',
					  footer: ''
				 })
			}
		});	
	}else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Fill the required fields',
		  footer: ''
		})
	}	
}

function lectureRegister(){
	if($("#lecName").val() !='' && $("#instituteName").val() !='' && $("#lecGender").val() !='' && $("#lecNic").val() !='' && $("#lecEmail").val() !='' && $("#lecMobile").val() !='' && $("#lecUsername").val() !='' && $("#educationQualifycation").val() !='' && $("#description").val() !='' && ($("#lecPassword").val() == $("#lecConfirmPassword").val())){
		var lecturer = {
			lecName : document.getElementById("lecName").value,
			instituteName 	 : document.getElementById("instituteName").value,
			lecGender  : document.getElementById("lecGender").value,
			lecNic  : document.getElementById("lecNic").value,
			lecEmail: document.getElementById("lecEmail").value,
			lecMobile: document.getElementById("lecMobile").value,
			lecUsername : document.getElementById("lecUsername").value,
			educationQualifycation : document.getElementById("educationQualification").value,
			description : document.getElementById("description").value,
			lecPassword : document.getElementById("lecPassword").value
		};
		$(':button').prop('disabled', true);
		$.ajax({
			url: '/secure/registerLecturer',
			type: 'POST',
			data: lecturer,
			cache: false,
	        success:function (response) {
				$(':button').prop('disabled', false);
				Swal.fire(
			      'Successful!',
			      'You have been registered',
			      'success'
			     ).then(function() {
				    window.location = "/login";
				 });
			},
			error:function(status, error){
				$(':button').prop('disabled', false);
				Swal.fire({
					  icon: 'error',
					  title: 'Something went wrong!',
					  text: 'Can not register!',
					  footer: ''
				 })
			}
		});	
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Fill the required fields',
		  footer: ''
		})
	}	
}

function companyRegister(){
	if($("#cmpName").val() !='' && $("#cmpDescription").val() !='' && $("#cmpAddress").val() !='' && $("#cmpMobile").val() !='' && $("#cmpEmail").val() !='' && $("#cmpPassword").val() !='' && ($("#cmpPassword").val() == $("#cmpConfirmPassword").val())){
		var publisher = {
			cmpName : document.getElementById("cmpName").value,
			cmpDescription 	 : document.getElementById("cmpDescription").value,
			cmpAddress  : document.getElementById("cmpAddress").value,
			cmpMobile  : document.getElementById("cmpMobile").value,
			cmpEmail: document.getElementById("cmpEmail").value,
			cmpPassword: document.getElementById("cmpPassword").value
		};
		$(':button').prop('disabled', true);
		$.ajax({
			url: '/secure/registerPublisher',
			type: 'POST',
			data: publisher,
			cache: false,
	        success:function (response) {
				$(':button').prop('disabled', false);
				Swal.fire({
				  position: 'top-end',
				  icon: 'success',
				  title: 'Your request has been sent to the admin',
				  showConfirmButton: false,
				  //timer: 3000
				}).then(function() {
				    window.location = "/login";
				 });
				/*
				Swal.fire(
			      'Successful!',
			      'You have been registered',
			      'success'
			     ).then(function() {
				    window.location = "/login";
				 });/*/
			},
			error:function(status, error){
				$(':button').prop('disabled', false);
				 Swal.fire({
					  icon: 'error',
					  title: 'Something went wrong!',
					  text: 'Your request did not sent to the admin',
					  footer: ''
				 })
			}
		});	
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Fill the required fields',
		  footer: ''
		})
	}	
}

var password = document.getElementById("password") , confirm_password = document.getElementById("confirmPassword");
		
function validatePassword(){
	if(password.value != confirm_password.value) {
	  confirm_password.setCustomValidity("Passwords Don't Match");
	} else {
	  confirm_password.setCustomValidity('');
	}
}

password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;

function validateUser(para){
	if((document.getElementById("stdEmail").value) == (document.getElementById("prtEmail").value)){
		document.getElementById("sts").style.color = 'red';
		document.getElementById("sts").innerText = '*' + " Email cannot be dupplicate please enter another email or set as parent email.";
		document.getElementById("btnRegisterStd").disabled = true;
		document.getElementById("btnRegisterPrt").disabled = true;
	}else{
		
		$.ajax({
			url: '/secure/userValidation',
			type: 'POST',
			data: {user : para},
			cache: false,
	        success:function (response) {
				
				if(response == 'Success'){
					document.getElementById("sts").innerText = '';	
					document.getElementById("sts2").innerText = '';	
					document.getElementById("btnRegisterStd").disabled = false;
					document.getElementById("btnRegisterPrt").disabled = false;
				}else{
					document.getElementById("sts").innerText = '*' + response;
					document.getElementById("sts").style.color = 'red';
					
					document.getElementById("sts2").innerText = '*' + response;
					document.getElementById("sts2").style.color = 'red';
					
					document.getElementById("btnRegisterStd").disabled = true;
					document.getElementById("btnRegisterPrt").disabled = true;
				}
				//alert(response);
			},
			error:function(status, error){
				//document.getElementById("sts").innerText = error;
				//alert(status + " | " + error);
			}
		});	
	}
	
}

function gradeChange(para){
	$.ajax({
		url: '/secure/getGrade',
		type: 'POST',
		data: {gradeId : para},
		cache: false,
        success:function (grade) {
			if((grade > 0) && (grade < 12)){
				document.getElementById("prtName").style.display = "block";
				document.getElementById("prtGender").style.display = "block";
				document.getElementById("prtEmail").style.display = "block";
				document.getElementById("prtMobile").style.display = "block";
			}else{
				document.getElementById("prtName").style.display = "none";
				document.getElementById("prtGender").style.display = "none";
				document.getElementById("prtEmail").style.display = "none";
				document.getElementById("prtMobile").style.display = "none";
			}
		}
	});		
}

function useParentEmail(data){
	if(data.checked){
		Swal.fire({
		  title: "Do you want to use the parent's email as default",
		  showDenyButton: true,
		  showCancelButton: true,
		  confirmButtonText: 'Yes',
		  denyButtonText: `No`,
		}).then((result) => {
		  if (result.isConfirmed) {
		    document.getElementById("stdEmail").disabled = true;
		    document.getElementById("stdEmail").value = " ";
		  } else{
		    document.getElementById("stdEmail").disabled = false;
		    data.checked = false;
		  }
		})
	}else{
		document.getElementById("stdEmail").disabled = false;
	}
}

function useParentMobile(){
	alert("Under Construction");
}

var policyList = [];
$(document).ready(function(){
	$.ajax({
		url: '/secure/getPolicies',
		type: 'POST',
		traditional: true,
        success:function (response) {
			policyList = response;
		}
	});	
});

$( "#password" ).focus(function() {
  var popup = document.getElementById("myPopup1");
	popup.classList.toggle("show");
});

$( "#lecPassword" ).focus(function() {
  var popup = document.getElementById("myPopup2");
	popup.classList.toggle("show");
});

$( "#cmpPassword" ).focus(function() {
  var popup = document.getElementById("myPopup3");
	popup.classList.toggle("show");
});

function checkPolicy(type,input){
	var sts = 0;
	var activeLength = 0;
	for (let x in policyList){
		let contains = new RegExp((policyList[x])["data"], "g");
		var policyId = 'policy'+ type + (policyList[x])["policyId"];
		var policy = document.getElementById(policyId);
		
		if(((policyList[x])["active"] == true)){
			activeLength++;
			if(((policyList[x])["data"]).includes("[")){
				if (input.match(contains)) {  
					policy.classList.remove("invalid");
					policy.classList.add("valid");
					sts++;
				} else {
					policy.classList.remove("valid");
					policy.classList.add("invalid");
				}	
			}else if(((policyList[x])["data"]).includes("length")){
				if(input.length >= ((policyList[x])["data"]).match(/(\d+)/g)) {
					policy.classList.remove("invalid");
					policy.classList.add("valid");
					sts++;
				} else {
					policy.classList.remove("valid");
					policy.classList.add("invalid");
				}
			}				
		}else{
			
		}
	}
	if(input == ""){
		$(':button').prop('disabled', false);
	}else if(sts < activeLength){
		$(':button').prop('disabled', true);
	}else{
		$(':button').prop('disabled', false);
	}
}





