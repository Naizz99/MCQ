var imageData,imageName,extension="";

$("#saveBundle").click(function () {
	if($("#bundleName").val() !='' && $("#grade").val() !='' && $("#publisher").val() !=''){
	
		document.getElementById("saveBundle").disabled = true;
		
		var form = new FormData($('#newBundle')[0]);
		var image = {
			bundleId : document.getElementById("bundleId").value,
			name : document.getElementById("bundleName").value,
			description : document.getElementById("description").value,
			grade : document.getElementById("grade").value,
			publisher : document.getElementById("publisher").value,
			user : document.getElementById("userId").value,
			availableForBuy : document.getElementById("availableForBuy").checked, 
			imageData : imageData,
			imageName : imageName,
			extension : extension
		};
		
		$.ajax({
			url: '/secure/savePaperBundle',
			type: 'POST',
			data: image,
			cache: false,
	        success:function (response) {
				document.getElementById("saveBundle").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
					document.getElementById("bundleId").value = response["bundleId"];
					document.getElementById("userId").value = response["userId"]["userId"];
				    window.location = "/showPaperBundleUpdate?bundleId=" + response["bundleId"];
				 });
			},
			error:function(status, error){
				document.getElementById("saveBundle").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Something went wrong',
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

function deleteBundle(id){
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
			url: '/deletePaperBundle',
			type: 'GET',
			traditional: true,
			data: {bundleId: id},
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

function imageLoad(){
	extension = document.getElementById("bundleImage").value.split('.').pop();
	imageName = "Bundle-Image" + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds();
	var file = document.querySelector('input[type=file]')['files'][0];
	var reader = new FileReader();		
	reader.onload = function () {
		imageData = reader.result.replace("data:", "").replace(/^.+,/, "");
	}
	reader.readAsDataURL(file);
}

function getPaper(paperId){
	var publisherId = document.getElementById("publisher").value;
	$.ajax({
		url: '/getPaper',
		type: 'GET',
		traditional: true,
		data: {paperId: paperId},
		success: function (paper) {
			if(publisherId == paper["publisher"]["poId"]){
				document.getElementById("paperName").value = paper["name"];
			    document.getElementById("paperGrade").value = paper["grade"]["gradeName"];
			    document.getElementById("subject").value = paper["subjectId"]["subjectName"];
			    document.getElementById("paperPublisher").value = paper["publisher"]["name"];
			}else{
				document.getElementById("paperName").value = "";
			    document.getElementById("paperGrade").value = "";
			    document.getElementById("subject").value = "";
			    document.getElementById("paperPublisher").value = "";
				Swal.fire(
			      'Selected paper is not match with current publisher',
			      'Please select paper with same publisher',
			      'error'
			    )
			}
		    
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not find paper for given id!',
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function addPaper(){
	var paperName = document.getElementById("paperName").value;
	var paperId = document.getElementById("paperId").value;
	var bundleId = document.getElementById("bundleId").value;
	
	if(paperName != ""){
		if(bundleId != ""){
			$.ajax({
				url: '/addBundlePaper',
				type: 'GET',
				traditional: true,
				data: {paperId: paperId,bundleId:bundleId},
				success: function (paper) {
				    Swal.fire(
				      'Successful!',
				      'Your record has been added',
				      'success'
				     ).then(function() {
					    window.location.reload();
					 });
				},
				error:function(status, error){
					 Swal.fire(
				      'Paper is already added or invalid details',
				      'Something went wrong',
				      'error'
				    )
				}
			});	
		}else{
			Swal.fire(
		      'Save bundle details to add papers',
		      'There are no selected bundles',
		      'error'
		    )	
		}		
	}else{
		Swal.fire(
	      'Select paper',
	      'There is no selected paper',
	      'error'
	    )	
	}
	
}

function removePaperLink(pbLinkId){
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
			url: '/removeBundlePaper',
			type: 'GET',
			traditional: true,
			data: {pbLinkId: pbLinkId},
			success: function (paper) {
			    Swal.fire(
			      'Successful!',
			      'Record has been removed',
			      'success'
			     ).then(function() {
				    window.location.reload();
				 });
			},
			error:function(status, error){
				 Swal.fire(
			      'Recorde has relation data or invalid details',
			      'Something went wrong',
			      'error'
			    )
			}
		});	
	  }
	})
	
}
