$(document).ready(function(){

});

function filterBySubject(subjectId){
	var  studentId = document.getElementById("studentId").value;
	
	if(subjectId == "all"){
		window.location.reload();
	}else{
		$.ajax({
			url: '/getResultsBySubjectAndStudent',
			type: 'GET',
			data: {subjectId : subjectId, studentId:studentId},
			cache: false,
			success:function (resultList) {
				console.log(resultList);		
				var row = document.getElementById("row");
				row.innerHTML = "";
				
				var result = "";
				
				for(x=0;x<resultList.length;x++){
					if(resultList[x]["result"] <= 20){
						result = "<p style='color:red;'>"+ resultList[x]["result"] +" % </p>";
					}else{
						result = "<p>"+ resultList[x]["result"] +" % </p>";
					}
					
					row.innerHTML += 
						"<div class='col-3'>" + 
							"<a href='/reviewPaper?reviewId='"+ resultList[x]["sprId"]+">" + 
								"<div class='card'>" + 
									"<div class='card-body row'>" + 
										"<div class='col-3'>" + 
											"<h1 style='border: 1px solid #CCC;border-radius:50%;text-align:center;font-weight: bold;'>"+ (resultList[x]["paperID"]["name"]).charAt(0).toUpperCase() +"</h1>"+
										"</div>" + 
										"<div class='col-9 activity card'>" + 
											"<div>" + 
												"<h4>"+ resultList[x]["paperID"]["name"] +"</h4>"+
												"<h4>" + resultList[x]["paperID"]["subjectId"]["subjectName"] +"</h4>"+
												result + 
												"<p>"+ resultList[x]["createDate"] + " | " + resultList[x]["updateDate"] +"</p>" + 
											"</div>" + 
										"</div>" + 
									"</div>" + 
								"</div>" + 
							"</a>" + 
						"</div>";	
				}
				
			},
			error:function(status, error){
				Swal.fire(
			      'Something went wrong!',
			      'Cannont proceed.',
			      'error'
			    );
			}
		});	
	}	
}

var button = document.getElementById("exportPdf");
var makepdf = document.getElementById("rptDiv");
 
button.addEventListener("click", function () {
	var mywindow = window.open("", "PRINT","height=400,width=600");
 
    mywindow.document.write(makepdf.innerHTML);
 
    mywindow.document.close();
    mywindow.focus();
 			
    mywindow.print();
    mywindow.close();
 
	return true;
});

