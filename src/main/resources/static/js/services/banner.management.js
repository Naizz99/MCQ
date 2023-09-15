var date = new Date();
var extenstion = null;
var serial = null;
var imageData = null;

$(document).ready(function() {
	//document.getElementById("customize").hidden = true; 
	//document.getElementById("imageDiv").hidden = true; 
	$("#news-slider").owlCarousel({
		items : 3,
		itemsDesktop:[1199,3],
		itemsDesktopSmall:[980,2],
		itemsMobile : [600,1],
		navigation:true,
		navigationText:["",""],
		pagination:true,
		autoPlay:true
	});
});


var imageUploaded = function(event) {
    var output = document.getElementById('imagePreview');
    output.src = URL.createObjectURL(event.target.files[0]);
    output.onload = function() {
      URL.revokeObjectURL(output.src) // free memory
    }
    
    extenstion = document.getElementById("uploadImage").value.split('.').pop();
	serial = "Banner-Image" + date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + "-" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + "-" + date.getMilliseconds();
	
	var file = document.querySelector('input[type=file]')['files'][0];
	var reader = new FileReader();		
	reader.onload = function () {
		imageData = reader.result.replace("data:", "").replace(/^.+,/, "");
	}
	reader.readAsDataURL(file);
};

function saveBannerContent() {
	if(($("#publisherId").val() !='') && (document.getElementById("mainHeader").value !='') && ($("#content").val() !='')){
		var bannerId = 0;
		if(document.getElementById("bannerId").value == ""){
			bannerId = 0;	
		}else{
			bannerId = document.getElementById("bannerId").value;
		}

		var grade = null;		

		if(document.getElementById("grade").value == 0){
			grade = null;
		}else{
			grade = document.getElementById("grade").value;
		}

		var bannerDto = {
			bannerId	: bannerId,
			userId		: document.getElementById("createBy").value,
			publisherId	: document.getElementById("publisherId").value,
			contentType	: document.getElementById("contentType").value,
			width		: document.getElementById("width").value,
			height		: document.getElementById("height").value,
			content		: document.getElementById("content").value,
			extension	: extenstion,
			serial		: serial,
			header		: document.getElementById("mainHeader").value,
			subHeader	: document.getElementById("subHeader").value,
			image		: imageData,
			active		: document.getElementById("active").value,
			createDate	: document.getElementById("createDate").value,
			updateBy	: document.getElementById("updateBy").value,
			updateDate	: document.getElementById("updateDate").value,
			grade		: grade
		};
		
		$.ajax({
			url: '/secure/saveBanner',
			type: 'POST',
			data: bannerDto,
			cache: false,
	        success:function (response) {
				Swal.fire(
			      'Successful!',
			      'Your record has been added',
			      'success'
			     ).then(function() {
				    window.location = "/listBanners";
				 });
			},
			error:function(status, error){
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
}


function deleteBanner(id){
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
			url: 'deleteBanner',
			type: 'GET',
			traditional: true,
			data: {bannerId: id},
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

function showContentDiv(selected){
	
	if((selected == 'Image') || (selected == 'Video')){
		document.getElementById("customize").hidden = true; 
		document.getElementById("imageDiv").hidden = false;
		changeResolusion(6);
	}else if(selected == 'Customize'){
		document.getElementById("customize").hidden = false; 
		document.getElementById("imageDiv").hidden = true;
		changeResolusion(1);
	}
}

function setImage(){
	document.getElementById('imgInsert2').addEventListener('change', readURL, true);
   	var file = document.getElementById("imgInsert2").files[0];
   	var reader = new FileReader();
   	reader.onloadend = function(){
		document.getElementById('edited').style.backgroundImage = "url(" + reader.result + ")";      
		document.getElementById('edited').style.backgroundRepeat = 'no-repeat';         
   	}
   	if(file){
		reader.readAsDataURL(file);
	}else{
	}
}

function readURL(){
	
}

function changeResolusion(resolution){
	if(resolution == 1){
		document.getElementById('edited').style.width= '100%';
		document.getElementById('edited').style.height = '100%';
		document.getElementById('bannerContent').style.width= '100%';
		document.getElementById('bannerContent').style.height = '100%';
	}else if(resolution == 2){
		document.getElementById('edited').style.width= '50%';
		document.getElementById('edited').style.height = '50%';
		document.getElementById('bannerContent').style.width= '50%';
		document.getElementById('bannerContent').style.height = '50%';
	}else if(resolution == 3){
		document.getElementById('edited').style.width= '75%';
		document.getElementById('edited').style.height = '100%';
		document.getElementById('bannerContent').style.width= '75%';
		document.getElementById('bannerContent').style.height = '100%';
	}else if(resolution == 4){
		document.getElementById('edited').style.width= '40%';
		document.getElementById('edited').style.height = '75%';
		document.getElementById('bannerContent').style.width= '40%';
		document.getElementById('bannerContent').style.height = '75%';
	}else if(resolution == 5){
		document.getElementById('edited').style.width= '100%';
		document.getElementById('edited').style.height = '150%';
		document.getElementById('bannerContent').style.width= '100%';
		document.getElementById('bannerContent').style.height = '150%';
	}else if(resolution == 6){
		document.getElementById('edited').style.width= '50%';
		document.getElementById('edited').style.height = '100%';
		document.getElementById('bannerContent').style.width= '50%';
		document.getElementById('bannerContent').style.height = '100%';
	}
	
}

function addText(input){
	const newParagraph = document.createElement("p");
	newParagraph.innerText = input;
	newParagraph.setAttribute('id','para1');
	newParagraph.setAttribute('onclick','selectParagraph(this.id)');
	
	document.getElementById("textContent").appendChild(newParagraph);
}

function addClassRoomBanner(classroomId){
	var bannerId = document.getElementById("bannerId").value;
	
	$.ajax({
		url: '/addClassRoomBanner',
		type: 'GET',
		traditional: true,
		data: {classroomId:classroomId,bannerId: bannerId},
		success: function (response) {
			if(response == 'success'){
				window.location.reload();
			}else if(response == 'dupplicate'){
				Swal.fire(
			      'Dupplicate!',
			      'Group has been Allready Linked',
			      'error'
			    ).then(function() {
				    window.location.reload();
				 });
			}
		    
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong!',
		      'Group cannot Link',
		      'error'
		    )
		}
	});
}

function removeClassRoomBanner(classroomId,bannerId){
	$.ajax({
		url: '/removeClassRoomBanner',
		type: 'GET',
		traditional: true,
		data: {classroomId:classroomId,bannerId: bannerId},
		success: function (response) {
			window.location.reload();		    
		},
		error:function(status, error){
			 Swal.fire(
		      'Something went wrong!',
		      'Group cannot Link',
		      'error'
		    )
		}
	});
}






