//Paper Management Section
$("#savePaper").click(function () {
	if(($("#name").val() !='') && ($('#subject').has('option').length > 0) && ($('#grade').has('option').length > 0) && ($("#answerCount").val() != 0) && ($("#questionCount").val() != 0) && ($("#time").val() != 0) && ($('#publisher').has('option').length > 0) && (document.getElementById("grade").value != '') && (document.getElementById("subject").value != '') && (document.getElementById("publisher").value != '')){
		document.getElementById("savePaper").disabled = true;
		var form = new FormData($('#newPaper')[0]);
		
		$.ajax({
			url: '/secure/savePaper',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("savePaper").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listPapers";
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				document.getElementById("savePaper").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected paper has already added',
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

function deletePaper(id){
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
			url: 'deletePaper',
			type: 'GET',
			traditional: true,
			data: {paperId: id},
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
						var status = subject["active"];
						if(status){
							$('#subject').append(`<option value="${subjectId}">${subjectName} </option>`);
							break;	
						}
					};
					
				};	
			}
		}
	});
}

function hoverFunction(){
	let timerInterval
	Swal.fire({
	  title: 'Auto close alert!',
	  html: 'I will close in <b></b> milliseconds.',
	  timer: 2000,
	  timerProgressBar: true,
	  didOpen: () => {
	    Swal.showLoading()
	    const b = Swal.getHtmlContainer().querySelector('b')
	    timerInterval = setInterval(() => {
	      b.textContent = Swal.getTimerLeft()
	    }, 100)
	  },
	  willClose: () => {
	    clearInterval(timerInterval)
	  }
	}).then((result) => {
	  /* Read more about handling dismissals below */
	  if (result.dismiss === Swal.DismissReason.timer) {
	    console.log('I was closed by the timer')
	  }
	})

	Swal.fire({
	  imageUrl: '/img/questions/QNo-11-1662008313076.PNG',
	  imageAlt: 'Custom image',
	})
}

//Package Management Section
$("#savePaperPackage").click(function () {
	if(($("#description").val() !='') && ($("#duration").val() != 0) && ($("#studentCount").val() != 0) && ($("#price").val() != 0)){
		document.getElementById("savePaperPackage").disabled = true;
		var form = new FormData($('#newPackage')[0]);
		
		$.ajax({
			url: '/secure/savePaperPackage',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("savePaperPackage").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listPaperPackages";
				 });
			},
			error:function(status, error){
				document.getElementById("savePaperPackage").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected paper has already added',
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

$("#saveClassRoomPackage").click(function () {
	if(($("#description").val() !='') && ($("#duration").val() != 0) && ($("#studentCount").val() != 0) && ($("#price").val() != 0)){
		document.getElementById("saveClassRoomPackage").disabled = true;
		var form = new FormData($('#newPackage')[0]);
		
		$.ajax({
			url: '/secure/saveClassRoomPackage',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveClassRoomPackage").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listClassRoomPackages";
				 });
			},
			error:function(status, error){
				document.getElementById("saveClassRoomPackage").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected paper has already added',
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