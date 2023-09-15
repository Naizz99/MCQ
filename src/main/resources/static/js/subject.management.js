function deleteSubject(id){
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
			url: 'deleteSubject',
			type: 'GET',
			traditional: true,
			data: {subjectId: id},
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

$("#saveSubject").click(function () {
	
	if(($("#subjectName").val() !='') && (document.getElementById("grade").value != '')){
		
		document.getElementById("saveSubject").disabled = true;
		
		var form = new FormData($('#newSubject')[0]);
		
		$.ajax({
			url: '/secure/saveSubject',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveSubject").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listSubjects";
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				document.getElementById("saveSubject").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Subject has already added',
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
