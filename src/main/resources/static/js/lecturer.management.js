$("#saveLecturer").click(function () {
	if(($("#lecturerName").val() !='') && ($("#username").val() !='') && ($("#email").val() !='') 
	&& ($("#nic").val() !='') && ($("#institute").val() !='') && ($("#educationQualification").val() !='') && ($("#description").val() !='') 
	&& ($("#mobile").val() !='') && ((document.getElementById("male").checked) || (document.getElementById("female").checked))){
		
		document.getElementById("saveLecturer").disabled = true;
		
		var form = new FormData($('#newLecturer')[0]);
		
		$.ajax({
			url: '/secure/saveLecturer',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveLecturer").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listLecturers";
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				document.getElementById("saveLecturer").disabled = false;
				Swal.fire(
			      'Can not insert!',
			      'Selected Lecturer has already added',
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

function deleteLecturer(lecturerId,userId){
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
			url: '/deleteLecturer',
			type: 'GET',
			traditional: true,
			data: {lecturerId: lecturerId,userId: userId},
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