//User Type Management Section

function deleteType(id){
	
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
			url: 'deleteUserType',
			type: 'GET',
			traditional: true,
			data: {utId: id},
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

$("#saveType").click(function () {
	
	if($("#name").val() !=''){
		document.getElementById("saveType").disabled = true;
		var form = new FormData($('#newType')[0]);
		
		$.ajax({
			url: '/secure/saveUserType',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveType").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listUserTypes";
				 });
			},
			error:function(status, error){
				document.getElementById("saveType").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Type has already added',
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
