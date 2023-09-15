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
  var popup = document.getElementById("policyPopup");
	popup.classList.toggle("show");
});

function checkPolicy(input){
	var sts = 0;
	var activeLength = 0;
	for (let x in policyList){
		let contains = new RegExp((policyList[x])["data"], "g");
		var policyId = 'policy' + (policyList[x])["policyId"];
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

//Admin Management Section

function deleteAdmin(id){
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
			url: '/deleteAdmin',
			type: 'GET',
			traditional: true,
			data: {adminId: id},
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

$("#saveAdmin").click(function () {
	if(($("#adminName").val() !='') && ($("#username").val() !='') && ($("#mobile").val() !='') && ($("#email").val() !='') && ((document.getElementById("male").checked) || (document.getElementById("female").checked))){
		
		document.getElementById("saveAdmin").disabled = true;
		
		var form = new FormData($('#newAdmin')[0]);
		
		$.ajax({
			url: '/secure/saveAdmin',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveAdmin").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listAdmins";
				 });
			},
			error:function(status, error){
				document.getElementById("saveAdmin").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Admin has already added',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
});

//Editor Management Section

function deleteEditor(id){
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
			url: '/deleteEditor',
			type: 'GET',
			traditional: true,
			data: {userId: id},
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

$("#saveUser").click(function () {
	
	if($("#userName").val() !=''){
		document.getElementById("saveUser").disabled = true;
		var form = new FormData($('#newUser')[0]);
		
		$.ajax({
			url: '/secure/saveEditor',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveUser").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listSystemEditors";
				 });
			},
			error:function(status, error){
				document.getElementById("saveUser").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected User has already added',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
});

//Student Management Section

function deleteStudent(studentId,userId){
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
			url: '/deleteStudent',
			type: 'GET',
			traditional: true,
			data: {studentId: studentId,userId: userId},
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

function changeParent(data1,data2){
	document.getElementById("parentId").value = data2;
	document.getElementById("parentName").value = data1; 
	
	$.ajax({
		url: '/secure/searchParent',
		type: 'GET',
		traditional: true,
		data: {userId: data2},
        success:function (parent) {
			document.getElementById("parentEmail").value = parent.email;
			document.getElementById("parentMobile").value = parent.mobile;
			document.getElementById("parentGender").value = parent.gender;
		},
		error:function(status, error){
			Swal.fire({
			  icon: 'error',
			  title: 'Something went wrong!',
			  text: '',
			  footer: ''
			})
		}
	});
}

$("#saveStudent").click(function () {
	
	if(document.getElementById("grade").value != '' && $("#studentName").val() !='' && document.getElementById("studentGender").value != '' && $("#studentEmail").val() !='' && $("#username").val() !='' && $("#studentMobile").val() !=''){
		document.getElementById("saveStudent").disabled = true;
		var student = {
			userId   : document.getElementById("userId").value,
			studentId: document.getElementById("studentId").value,
			stdGrade : document.getElementById("grade").value,
			stdGPA 	 : document.getElementById("studentGPA").value,
			stdName  : document.getElementById("studentName").value,
			prtName  : document.getElementById("parentName").value,
			stdGender: document.getElementById("studentGender").value,
			prtGender: document.getElementById("parentGender").value,
			stdEmail : document.getElementById("studentEmail").value,
			prtEmail : document.getElementById("parentEmail").value,
			stdMobile : document.getElementById("studentMobile").value,
			prtMobile : document.getElementById("parentMobile").value,
			stdUsername	: document.getElementById("username").value,
			stdPassword	: "1qaz@WSX",
			createUser 	: document.getElementById("loggedUser").value
		};
		
		$.ajax({
			url: '/secure/saveStudent',
			type: 'POST',
			data: student,
			cache: false,
	        success:function (response) {
				document.getElementById("saveStudent").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listStudents";
				 });
			},
			error:function(status, error){
				document.getElementById("saveStudent").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Student has already added',
			      'error'
			     )
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
	
});

//Parent Management Section

function deleteParent(id){
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
			url: '/deleteParent',
			type: 'GET',
			traditional: true,
			data: {parentId: id},
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

$("#saveParent").click(function () {
	
	if($("#parentName").val() !=''){
		document.getElementById("saveParent").disabled = true;
		var form = new FormData($('#newParent')[0]);
		console.log(form);
		$.ajax({
			url: '/secure/saveParent',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveParent").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listParents";
				 });
			},
			error:function(status, error){
				document.getElementById("saveParent").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Parent has already added',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
});


//Author Management Section

function deleteAuthor(id){
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
			url: '/deleteAuthor',
			type: 'GET',
			traditional: true,
			data: {authorId: id},
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

$("#saveAuthor").click(function () {
	if(($("#authorName").val() !='') && (document.getElementById("publisher").value != '') && ($("#username").val() !='') && ($("#email").val() !='') && ($("#mobile").val() !='') && ((document.getElementById("male").checked) || (document.getElementById("female").checked))){
		document.getElementById("saveAuthor").disabled = true;
		
		var form = new FormData($('#newAuthor')[0]);

		$.ajax({
			url: '/secure/saveAuthor',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveAuthor").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listAuthors";
				 });
			},
			error:function(status, error){
				document.getElementById("saveAuthor").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Author has already added',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
});

$("#saveAuthorByPublisher").click(function () {
	
	if(($("#authorName").val() !='') && (document.getElementById("publisher").value != '') && ($("#username").val() !='') && ($("#email").val() !='') && ($("#mobile").val() !='')){
		document.getElementById("saveAuthorByPublisher").disabled = true;
		var form = new FormData($('#newAuthor')[0]);
		
		$.ajax({
			url: '/secure/saveAuthor',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveAuthorByPublisher").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listAuthorsByPublisher?poId=" + document.getElementById("publisher").value;
				 });
			},
			error:function(status, error){
				document.getElementById("saveAuthorByPublisher").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Author has already added',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
});

function validatePassword(){
	var password = document.getElementById("password")
	var confirmPassword = document.getElementById("confirmPassword");
	
	if(password.value != confirmPassword.value) {
		document.getElementById("passordStatus").innerHTML = "<i style='color:red' class='bi bi-x-circle-fill'>Password Don't Match</i>";
	}else {
		document.getElementById("passordStatus").innerHTML = "<i style='color:green' class='bi bi-check-circle-fill'></i>";
	}
}

function savePassword(){
	var password = document.getElementById("password")
	var confirmPassword = document.getElementById("confirmPassword");
	
	if(password.value != confirmPassword.value) {
		document.getElementById("passordStatus").innerHTML = "<i style='color:red' class='bi bi-x-circle-fill'>Password Don't Match</i>";
	}else if((password.value == '') || (confirmPassword.value == '')) {
		document.getElementById("passordStatus").innerHTML = "<i style='color:red' class='bi bi-x-circle-fill'>Password field can not be null</i>";
	}else {
		document.getElementById("passordStatus").innerHTML = "";
		
		userId = document.getElementById("userId").value;
		password = document.getElementById("password").value;
		
		$.ajax({
			url: '/changePassword',
			type: 'GET',
			traditional: true,
			data: {userId: userId , pwd: password , type: 'change'},
			success: function (response) {
				Swal.fire(
			      'Successful!',
			      'Your password has been changed',
			      'success'
			     ).then(function() {
				    window.location.reload();
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
	}
}

function linkUserRole(userId,roleId){
	$.ajax({
		url: '/createUserRole',
		type: 'GET',
		traditional: true,
		data: {userId:userId,roleId:roleId},
		success: function (response) {
			window.location.reload();
			//window.location = "/userProfile";
		},
		error:function(status, error){
			 Swal.fire(
		      'Dupplicate Value!',
		      'Role already added',
		      'error'
		    )
		}
	}); 
}

function removeUserRole(uRoleId){
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
			url: '/deleteUserRole',
			type: 'GET',
			traditional: true,
			data: {uRoleId: uRoleId},
			success: function (response) {
				window.location.reload();
				//window.location = "/logout";
			},
			error:function(status, error){
				 Swal.fire(
			      'Can not process!',
			      'Something went wrong',
			      'error'
			    )
			}
		});
	  }
	})
	
}



















