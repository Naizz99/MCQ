var pbId = null;

$(document).ready(function(){
	if(document.getElementById("poId")){
		pbId = document.getElementById("poId").value;	
	}
});

function deletePublisher(id){
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
			url: 'deletePublisher',
			type: 'GET',
			traditional: true,
			data: { poId: id},
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

function deleteTempPublisher(id){
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
			url: '/deleteTempPublisher',
			type: 'GET',
			traditional: true,
			data: { poId: id},
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

$("#savePublisher").click(function () {
	if(($("#name").val() !='') && ($("#description").val() !='') && ($("#email").val() !='') && ($("#mobile").val() !='') && (($("#address").val() !=''))){		
		document.getElementById("savePublisher").disabled = true;
		
		/*
		var imageData = "";
		var file = document.querySelector('input[type=file]')['files'][0];
		var reader = new FileReader();		
		reader.onload = function () {
			imageData = reader.result.replace("data:", "").replace(/^.+,/, "");
		}
		reader.readAsDataURL(file);
		var image = {
			reference : document.getElementById("savePublisher").value,
			extention : document.getElementById("image").value.split('.').pop(),
			serial	  : "Publisher-Image" + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds(),
			image     : imageData
		};
		*/
		var form = new FormData($('#newPublisher')[0]);	
		console.log(form);
		$.ajax({
			url: '/secure/savePublisher',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
	
	        success:function (response) {
				document.getElementById("savePublisher").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listPublishers";
				 });
			     //window.location = "/listGrades";
			},
			error:function(status, error){
				document.getElementById("savePublisher").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Something went wrong!',
			      'error'
			    )
			}
		});	
	}else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
		
});

function approvePublisher(){
	//alert("approvePublisher : " + pbId);
	$.ajax({
		url: '/approvePublisher',
		type: 'GET',
		data: {pbId : pbId},
		success:function (response) {
			Swal.fire(
		      'Successful!',
		      'Publisher has been approved',
		      'success'
		     ).then(function() {
			    window.location = "/viewPublisherRequests";
			    //window.location.reload();
			 });
		},
		error:function(status, error){
			Swal.fire({
			  icon: 'error',
			  title: 'Something went wrong!',
			  text: 'Enter Required Details',
			  footer: ''
			})
		}
	});	
}

function rejectPublisher(){
	//alert("rejectPublisher : " + pbId);
	var note = document.getElementById("note").value;
	if(note != ""){
		$.ajax({
			url: '/rejectPublisher',
			type: 'GET',
			data: {pbId : pbId,note : note},
			success:function (response) {
				Swal.fire(
			      'Rejected!',
			      'Publisher has been Rejected',
			      'success'
			     ).then(function() {
				    window.location = "/viewPublisherRequests";
				 });
			},
			error:function(status, error){
				Swal.fire({
				  icon: 'error',
				  title: 'Something went wrong!',
				  text: 'Enter Required Details',
				  footer: ''
				})
			}
		});	
	}else {
		Swal.fire(
	      'Note can not be empty!',
	      'Rejected reason should inform to the requested publisher',
	      'error'
	    )
	}
	
}

function activePublisherUser(pbUserId){
	$.ajax({
		url: '/activePublisherUser',
		type: 'GET',
		data: {pbUserId : pbUserId},
        success:function (response) {
			Swal.fire(
		      'Activated!',
		      'Selected user has been activated',
		      'success'
		     ).then(function() {
			    window.location.reload();
			 });	
		},
		error:function(status, error){
			Swal.fire({
			  icon: 'error',
			  title: 'Something went wrong!',
			  text: 'Enter Required Details',
			  footer: ''
			})	
		}
	});	
}

function deactivePublisherUser(pbUserId){
	$.ajax({
		url: '/deactivePublisherUser',
		type: 'GET',
		data: {pbUserId : pbUserId},
        success:function (response) {
			Swal.fire(
		      'Activated!',
		      'Selected user has been Deactivated',
		      'success'
		     ).then(function() {
			    window.location.reload();
			 });	
		},
		error:function(status, error){
			Swal.fire({
			  icon: 'error',
			  title: 'Something went wrong!',
			  text: 'Enter Required Details',
			  footer: ''
			})	
		}
	});	
}

function removePublisherUser(pbUserId){
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
				url: '/removePublisherUser',
				type: 'GET',
				data: {pbUserId : pbUserId},
		        success:function (response) {
					Swal.fire(
				      'Removed!',
				      'Selected user has been Romoved',
				      'success'
				     ).then(function() {
					    window.location.reload();
					 });	
				},
				error:function(status, error){
					Swal.fire({
					  icon: 'error',
					  title: 'Something went wrong!',
					  text: 'Enter Required Details',
					  footer: ''
					})	
				}
			});		
	  }
	})	
}

function activeAllPublisherUsers(){
	$.ajax({
		url: '/activeAllPublisherUsers',
		type: 'GET',
		data: {pbId : pbId},
        success:function (response) {
			Swal.fire(
		      'Activated!',
		      'All users have been activated',
		      'success'
		     ).then(function() {
			    //window.location = "/listPublishers";
			    window.location.reload();
			 });	
		},
		error:function(status, error){
			Swal.fire({
			  icon: 'error',
			  title: 'Something went wrong!',
			  text: 'Enter Required Details',
			  footer: ''
			})	
		}
	});	
}

function deactiveAllPublisherUsers(){
	$.ajax({
		url: '/deactiveAllPublisherUsers',
		type: 'GET',
		data: {pbId : pbId},
        success:function (response) {
			Swal.fire(
		      'Deactivated!',
		      'All users have been deactivated',
		      'success'
		     ).then(function() {
			    //window.location = "/listPublishers";
			    window.location.reload();
			 });	
		},
		error:function(status, error){
			Swal.fire({
			  icon: 'error',
			  title: 'Something went wrong!',
			  text: 'Enter Required Details',
			  footer: ''
			})		
		}
	});	
}

function removeAllPublisherUsers(){
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
				url: '/removeAllPublisherUsers',
				type: 'GET',
				data: {pbId : pbId},
		        success:function (response) {
					Swal.fire(
				      'Removed!',
				      'All users have been removed from publisher',
				      'success'
				     ).then(function() {
					    //window.location = "/listPublishers";
					 });		
				},
				error:function(status, error){
					Swal.fire({
					  icon: 'error',
					  title: 'Something went wrong!',
					  text: 'Enter Required Details',
					  footer: ''
					})	
				}
			});	
	  }
	})	
}

/*Publisher Profile Managment*/

function savePhoto(temp,data,type,name){
	Swal.fire({
		title: 'Do you want to save the changes?',
		showDenyButton: true,
		showCancelButton: true,
		confirmButtonText: 'Save',
		denyButtonText: `Don't save`,
	}).then((result) => {
		if (result.isConfirmed) {
			var publisherId = document.getElementById('publisherId').value;
			$.ajax({
				url: '/secure/savePublisherPhoto',
				type: 'POST',
				data: {type:type,data:data,name:name,publisherId:publisherId},
		        success:function (response) {
		        	 Swal.fire('Saved!', '', 'success').then(function() {
		        	 	window.location.reload();
					 });
				},
				error:function(status, error){
					if(type == 'cover'){
						CoverImage.src = temp;
					}else if(type == 'profile'){
						ProfileImage.src = temp;
					}
					Swal.fire(
				      'Can not save!',
				      'Something went wrong',
				      'error'
				    )
				}
			});		
		} else if (result.isDenied) {
			if(type == 'cover'){
				CoverImage.src = temp;
			}else if(type == 'profile'){
				ProfileImage.src = temp;
			}
			Swal.fire('Changes are not saved', '', 'info')
		}
	})
}

var coverImageDate = null;
var profileImageDate = null;
function imageUploader(id,type) {
	var name = "pub-"+document.getElementById('publisherId').value + "." + document.getElementById(id).value.split('.').pop();
	var file = document.getElementById(id)['files'][0];
	var reader = new FileReader();		
	var coverImageDate = null;
	var profileImageDate = null;
	reader.onload = function () {
		if(type == 'cover'){
			coverImageDate = reader.result.replace("data:", "").replace(/^.+,/, "");
			var CoverImage = document.getElementById("CoverImage");
			var temp = CoverImage.src; 
			CoverImage.src = reader.result;
			savePhoto(temp,coverImageDate,'cover',name);
		}else if(type == 'profile'){
			profileImageDate = reader.result.replace("data:", "").replace(/^.+,/, "");	
			var ProfileImage = document.getElementById("ProfileImage");
			var temp = ProfileImage.src;
			ProfileImage.src = reader.result;
			savePhoto(temp,profileImageDate,'profile',name);
		}
	}
	reader.readAsDataURL(file);
}
					
function enableProfile(){
	document.getElementById("updateProfile").readOnly = true;
	document.getElementById("save").readOnly = false;
	 
	document.getElementById("publisherName").readOnly = false;
	document.getElementById("description").readOnly = false;
	document.getElementById("email").readOnly = false;
	document.getElementById("phone").readOnly = false;
	document.getElementById("address").readOnly = false;
	
	document.getElementById("publisherName").style.border = "1px solid gray";
	document.getElementById("description").style.border = "1px solid gray";
	document.getElementById("email").style.border = "1px solid gray";
	document.getElementById("phone").style.border = "1px solid gray";
	document.getElementById("address").style.border = "1px solid gray";		
}

function cancelUpdate(){
	if(document.getElementById("publisher") != null){ document.getElementById("publisher").reset(); }
	
	document.getElementById("updateProfile").readOnly = false;
	document.getElementById("save").readOnly = true;
	 
	document.getElementById("publisherName").readOnly = true;
	document.getElementById("description").readOnly = true;
	document.getElementById("email").readOnly = true;
	document.getElementById("phone").readOnly = true;
	document.getElementById("address").readOnly = true;
	
	document.getElementById("publisherName").style.border = "none";
	document.getElementById("description").style.border = "none";
	document.getElementById("email").style.border = "none";
	document.getElementById("phone").style.border = "none";
	document.getElementById("address").style.border = "none";		
}
	
function saveProfile(){
	if(($("#publisherName").val() !='') && ($("#email").val() !='') && ($("#phone").val() !='') && ($("#address").val() !='')){
		var publisher = new FormData($('#publisher')[0]);
		console.log("publisher : " + publisher);
		$.ajax({
			url: '/secure/savePublisher',
			type: 'POST',
			data: publisher,
			processData: false,
	        contentType: false,
	        success:function (response) {
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
			      'Can not update!',
			      'Something went wrong!',
			      'error'
			    )
			}
		});		
	}else{
		Swal.fire({
		  icon: 'error',
		  title: 'Something went wrong!',
		  text: 'Enter Required Details',
		  footer: ''
		})
	}
}

function saveTheam(type){
	
	var profile = {
		publisherId : document.getElementById("publisherId").value,
		bodyBgColor : document.getElementById("bodyBgColor").value,
		bodyFontFamily : document.getElementById("bodyFontFamily").value,
		fontColor : document.getElementById("fontColor").value,
		cardBgColor : document.getElementById("cardBgColor").value,
		cardSideColor1 : document.getElementById("cardSideColor").value,
		cardSelectedColor : document.getElementById("cardSelectedColor").value,
		type:type
	}
	
	$.ajax({
		url: '/secure/updatePublisherProfile',
		type: 'POST',
		data: profile,
		cache: false,
        success:function (response) {
			window.location.reload();	
		},
		error:function(status, error){
			Swal.fire({
				  icon: 'error',
				  title: 'Something went wrong!',
				  text: 'Can not Update!',
				  footer: ''
			 })
		}
	});
}







