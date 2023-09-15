//Module Management Section

function deleteModule(id){
	
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
			url: 'deleteModule',
			type: 'GET',
			traditional: true,
			data: {moduleId: id},
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

$("#saveModule").click(function () {
	if(($("#name").val() !='') && (document.getElementById("grade").value != '') && (document.getElementById("subject").value != '')){
		document.getElementById("saveModule").disabled = true;
		var form = new FormData($('#newModule')[0]);
		
		$.ajax({
			url: '/secure/saveModule',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveModule").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listModules";
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				 document.getElementById("saveModule").disabled = false;
				 Swal.fire(
				      'Can not insert!',
				      'Selected module has already added',
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

function update() {
	
	var listBox = document.getElementById("subject");
    listBox.options.length = 0;
    
	var selectGrade = document.getElementById('grade').value;
			
	$.ajax({
		url: 'getSubjectsByGrade',
		type: 'GET',
		traditional: true,
		data: {gradeId: selectGrade},
		success: function (subjects) {
			
			if(subjects.length === 0){
				Swal.fire({
				  title: 'Error!',
				  text: 'There are no subjects for selected grade',
				})
			}else{
				for (let x in subjects) {
					var subject = subjects[x];
					for (let y in subject) {
						var subjectId = subject["subjectId"];
						var subjectName = subject["subjectName"];
						
			            $('#subject').append(`<option value="${subjectId}">${subjectName} </option>`);
						break;
					};
					
				};	
			}
		}
	});
}

