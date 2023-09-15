$(document).ready(function(){
	//alert(123);
});

function checkout(id,type){
	document.getElementById("checkoutBtn").disabled = true;
	var userId = document.getElementById("userId").value;
	var packageId = document.getElementById("packageId").value;
	
	$.ajax({
		url: '/checkout',
		type: 'GET',
		traditional: true,
		data: {userId: userId,id: id,type: type,packageId: packageId},
		success: function (success) {
			document.getElementById("checkoutBtn").disabled = false;
			Swal.fire(
		      'Successful!',
		      'Purchase order has been successful',
		      'success'
		     ).then(function() {
				if((type == "p") || type == "b"){
					window.location = "/listPurchasePapers";	
				}else if(type == "c"){
					window.location = "/viewClassRoom?classroomId=" + id;
				}
			    
			 });
		},error:function(status, error){
			document.getElementById("checkoutBtn").disabled = false;
			if((type == "p") || type == "b"){
				Swal.fire(
			      'Can not Purchase!',
			      'Selected Item has already purchased with another package',
			      'error'
			    )	
			}else if(type == "c"){
				Swal.fire(
			      'Can not Purchase!',
			      'Something went wrong',
			      'error'
			    )
			}
			
		}
	});
	
}

function addToCart(id,type){	
	$.ajax({
		url: '/addToCart',
		type: 'GET',
		traditional: true,
		data: {id: id,type: type},
		success: function (success) {
			if(success == 'dupplicate'){
				Swal.fire(
			      'Dupplicate!',
			      'Already added to the cart',
			      'error'
			    )	
			}else if(success == 'success'){
				Swal.fire(
			      'Cart Updated!',
			      'New item added to cart',
			      'success'
			     )
			}
			
		},error:function(status, error){
			Swal.fire(
		      'Can not Process!',
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function allocatePurchased(){	
	
	var id = document.getElementById("id").value;
	var type = document.getElementById("type").value;
	var userId = document.getElementById("student").value;
	var packageId = document.getElementById("pckg").value;
		
	$.ajax({
		url: '/allocatePurchased',
		type: 'GET',
		traditional: true,
		data: {id: id,type: type,userId:userId,packageId:packageId},
		success: function (success) {
			if(success == 'dupplicate'){
				Swal.fire(
			      'Dupplicate!',
			      'Already allocated for this student',
			      'error'
			    )	
			}else if(success == 'success'){
				Swal.fire(
			      'Allocated!',
			      'New paper has allocate for student',
			      'success'
			     )
			     window.location.reload();
			}
			
		},error:function(status, error){
			Swal.fire(
		      'Can not Process!',
		      'Something went wrong',
		      'error'
		    )
		}
	});
	
}

function requestPaper(){
	var publisher = document.getElementById("publisher").value;
	var lecturer = document.getElementById("lecturer").value;
	var msg = document.getElementById("message").value;
	
	if(publisher == '')
		publisher =0;
	if(lecturer == '')
		lecturer =0;
	
	$.ajax({
		url: '/sendPaperRequest',
		type: 'GET',
		traditional: true,
		data: {publisherId: publisher,lecturerId:lecturer,msg: msg},
		success: function (success) {
			Swal.fire(
		      	'Successful!',
		      	'Your request has been sent',
		    	'success'
		    ).then(function() {
				window.location = "/paperMarket";
			});
		},error:function(status, error){
			Swal.fire(
		      'Can not Process!',
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function deleteUserPurchase(id){
	Swal.fire({
	  title: 'Are you sure?',
	  text: "You won't be able to revert this!",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
	  if (result.isConfirmed){
			$.ajax({
			url: '/deleteUserPurchase',
			type: 'GET',
			traditional: true,
			data: {userPaperId: id},
			success: function (success) {
				window.location.reload();
			},error:function(status, error){
				Swal.fire(
			      'Can not Process!',
			      'Something went wrong',
			      'error'
			    )
			}
		});
	  }
	})
}






