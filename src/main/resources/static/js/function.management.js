//User Type Management Section

$("#saveFunction").click(function () {
	if(($("#functionName").val() !='') && ($("#functionAction").val() !='') && (($("#functionControl").val() !=''))){
		document.getElementById("saveFunction").disabled = true;
		var form = new FormData($('#newFunction')[0]);
		
		$.ajax({
			url: '/secure/saveFunction',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("saveFunction").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listFunctions";
				 });
			},
			error:function(status, error){
				document.getElementById("saveFunction").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Function has already added',
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

function deleteFunction(id){
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
			url: '/deleteFunction',
			type: 'GET',
			traditional: true,
			data: {functionId: id},
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

function getFunctions(role){
	$.ajax({
		url: '/listFunctionsByRole',
		type: 'GET',
		traditional: true,
		data: {roleName: role},
		success: function (functionList) {
		    setFunctions(functionList);
		},
		error:function(status, error){
			 Swal.fire(
		      'Null data error!',
		      'There are no functions for this role',
		      'error'
		    )
		}
	});
}

function setFunctions(functionList){
	var functions = document.getElementById("functions");
	functions.innerHTML = "";
	
	for (let x in functionList){	
		var newChildNode = document.createElement('div');
		newChildNode.id = 'function'+x; 
		console.log(functionList[x]["functionId"]["parent"]);
		if(functionList[x]["functionId"]["parent"]){
			newChildNode.className = "function row border border-secondary p-2";
			newChildNode.innerHTML = "<div class='col-11'><b><p>" + functionList[x]['functionId']['functionName'] + "</p></b></div><div class='col-1'><i class='bi bi-trash' onclick='removeFunctionLink(" + functionList[x]['uflId'] + ")'></i></div>";
		}else{
			newChildNode.className = "function row border border-secondary p-2";
			newChildNode.innerHTML = "<div class='col-11'><p>" + functionList[x]["functionId"]["functionName"] + "</p></div><div class='col-1'><i class='bi bi-trash' onclick='removeFunctionLink(" + functionList[x]['uflId'] + ")'></i></div>";
		}	        		
		functions.appendChild(newChildNode);
	}		
}

function subFunctions(id) {
	id = 'subFunction' + id;
  	var x = document.getElementById(id);
  	if(x != null){
		if (x.style.display === "none") {
	    	x.style.display = "block";
	  	} else {
	    	x.style.display = "none";
	  	}
	}
} 

function createFunctionLink(functionId) {
	var role = document.getElementById("roles").options[document.getElementById("roles").selectedIndex].text;
	
	$.ajax({
		url: '/addRoleFunction',
		type: 'GET',
		traditional: true,
		data: {functionId : functionId , role : role},
		success: function (response) {
			if(response == "dupplicate"){
				Swal.fire({
				  icon: 'Dupplicate record!',
				  title: 'Function has been allready added to the user role',
				  text: 'Can not added',
				  footer: ''
				})
			}else{
				getFunctions(role);	
			}
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

function removeFunctionLink(uflId) {
	var role = document.getElementById("roles").options[document.getElementById("roles").selectedIndex].text;
	
	$.ajax({
		url: '/deleteRoleFunction',
		type: 'GET',
		traditional: true,
		data: {uflId : uflId},
		success: function (response) {
			getFunctions(role);
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

//Policy Management

$("#savePolicy").click(function () {
	if($("#policy").val() !=''){
		document.getElementById("savePolicy").disabled = true;
		var form = new FormData($('#newPolicy')[0]);
		console.log(form);
		$.ajax({
			url: '/secure/savePolicy',
			type: 'POST',
			data: form,
			processData: false,
	        contentType: false,
			cache: false,
	        success:function (response) {
				document.getElementById("savePolicy").disabled = false;
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listPasswordPolicies";
				 });
			},
			error:function(status, error){
				document.getElementById("savePolicy").disabled = false;
				 Swal.fire(
			      'Can not insert!',
			      'Selected Function has already added or Invalid Request',
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

function deletePolicy(id){
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
			url: '/deletePolicy',
			type: 'GET',
			traditional: true,
			data: {policyId: id},
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

