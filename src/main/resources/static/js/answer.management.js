$(document).ready(function(){
		
	var active = document.getElementById("active").value;
	
	document.getElementById("data1").value = document.getElementById("paperID").value;
	document.getElementById("data2").value = document.getElementById("question").value;
	if(active){
		document.getElementById("data3").value = "Active";
	}else{
		document.getElementById("data3").value = "Closed";
	}
	 
	document.getElementById("data4").value = document.getElementById("createBy").value;
	document.getElementById("data5").value = document.getElementById("createDate").value;
	document.getElementById("data6").value = document.getElementById("updateBy").value;
	document.getElementById("data7").value = document.getElementById("updateDate").value;
	
});

$("#saveAnswer").click(function () {
	if($("#answer").val() !=''){
		document.getElementById("saveAnswer").disabled = true;
		var form = new FormData($('#newAnswer')[0]);
		var pqId = document.getElementById("pqId").value;
		
		$.ajax({
			url: '/secure/saveAnswer',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveAnswer").disabled = false;
				Swal.fire(
			      'Successful!',
			      'New Answer has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listAnswersByPqId?pqId="+pqId;
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				 document.getElementById("saveAnswer").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Answer can not be null',
		  footer: ''
		})
	}
});

$("#updateAnswer").click(function () {
	if($("#answer").val() !=''){
		
		var form = new FormData($('#newAnswer')[0]);
		var pqId = document.getElementById("pqId").value;
		
		$.ajax({
			url: '/secure/updateAnswer',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				Swal.fire(
			      'Successful!',
			      'New Answer has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listAnswersByPqId?pqId="+pqId;
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				
				 Swal.fire(
			      'Can not update!',
			      'error'
			    )
			}
		});		
	}
	else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Answer can not be null',
		  footer: ''
		})
	}
});

function deleteAnswer(id){
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
			url: '/deleteAnswer',
			type: 'GET',
			traditional: true,
			data: {paId: id},
			success: function (response) {
			    window.location.reload();
			},
			error:function(status, error){
				 Swal.fire(
			      'Can not delete!',
			      'Something went wrong',
			      'error'
			    )
			}
		});
	  }
	})
}

function correctAnswer(id){
	$.ajax({
		url: '/correctAnswer',
		type: 'GET',
		traditional: true,
		data: {paId: id},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function incorrectAnswer(id){
	$.ajax({
		url: '/incorrectAnswer',
		type: 'GET',
		traditional: true,
		data: {paId: id},
		success: function (response) {
		    window.location.reload();
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong',
		      'error'
		    )
		}
	});
}
