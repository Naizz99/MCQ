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
	document.getElementById("saveAdmin").disabled = true;
	if($("#adminName").val() !=''){
		
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
			     //window.location = "/listGrades";
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