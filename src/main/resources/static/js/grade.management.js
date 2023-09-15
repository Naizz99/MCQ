//Grade Management Section

function deleteGrade(id){
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
			url: 'deleteGrade',
			type: 'GET',
			traditional: true,
			data: { gradeId: id},
			success: function (response) {
				//alert(response);
				/*
				Swal.fire(
			      'Deleted!',
			      'Your file has been deleted.',
			      'success'
			    )*/
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

$("#saveGrade").click(function () {
	//var title=$("#printerHeadName").text();
	if(($("#gradeName").val() !='') && (document.getElementById("grade").value != '')){
		
		document.getElementById("saveGrade").disabled = true;

		var form = new FormData($('#newGrade')[0]);

		$.ajax({
			url: '/secure/saveGrade',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
	
	        success:function (response) {
				document.getElementById("saveGrade").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been updated',
			      'success'
			     ).then(function() {
				    window.location = "/listGrades";
				});
			},
			error:function(status, error){
				document.getElementById("saveGrade").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected grade has already added',
			      'error'
			    )
			}
		});		
	}else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Select Grade',
		  footer: ''
		})
	}
});

