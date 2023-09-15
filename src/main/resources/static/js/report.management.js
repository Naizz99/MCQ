$(document).ready(function() {
  //alert("Page is ready!");
});

document.getElementById("rptDiv").style.margin = '1%';

document.getElementById("printButton").addEventListener("click", function() {
	var printableContent = document.getElementById("rptDiv").innerHTML;
  	
	if(document.getElementById("individualReport")){
		if(document.getElementById("individualReport").style.display == "block"){
			printableContent += 
				"<hr>" + 
				document.getElementById("studentPaperSummeryData").innerHTML + 
				"<hr>" +
				document.getElementById("studentModuleSummeryData").innerHTML;		
		}
	}
	if(document.getElementById("paperSummeryData")){
		if(document.getElementById("rptData2").rows.length > 0)
			printableContent = document.getElementById("paperSummeryData").innerHTML;
	}
	var printWindow = window.open('', '_blank');
	var template = "<!DOCTYPE html>"+
					"<html>"+
					"<head>"+
					"    <title>"+ document.getElementById("rptName").innerText +"</title>"+
					"    <style>"+
					"header, footer {display: block;}"+
					"        header {"+
					"            background-color: #6d9abf;"+
					"            color: #fff;"+
					"            padding: 20px;"+
					"            text-align: center;"+
					"        }"+
					"        h1 {"+
					"            margin: 0;"+
					"        }"+
					"        p {"+
					"            margin: 5px 0;"+
					"        }"+
					"		 table,tr,td,th{"+
					"			border : 1px solid black;border-collapse: collapse;"	+
					"		 }"+
					"    </style>"+
					"</head>"+
					"<body>"+
					"    <header>"+
					"        <h1>"+ document.getElementById("rptName").innerText +"</h1>"+
					"    </header>"+
					printableContent +
					"    <header>"+
					"        <p style='text-align: left;'>Printed By: "+ document.getElementById("sessionName").innerText +"</p>"+
					"        <p style='text-align: left;'>Printed Time: " + new Date() + "</p>"+
					"    </header>"+
					"</body>"+
					"</html>";
	
  	printWindow.document.write(template);

  	printWindow.print();
});

function navigateToURL(object) {
	var url = object.getAttribute("data-url")
    var id = object.getAttribute("data-id")
    window.location.href = url + id;
}

function clearSearch(){
	
	var inputElements = document.getElementById("searchBar").getElementsByTagName("input");
	for (var i = 0; i < inputElements.length; i++) {
	    inputElements[i].value = '';
	}
	
	var selectElements = document.getElementById("searchBar").getElementsByTagName("select");
  
  	for (var i = 0; i < selectElements.length; i++) {
    	selectElements[i].value = '';
  	}
  	
  	document.getElementById("search").click();
}

function searchStudentList(){	
	var studentId = 0;
	var name = 0;
	var gender = 0;
	var email =0;
	var mobile = 0;
	var grade = 0;
	var tatm1 = 0;
	var tatm2 = 0;
	
	if(document.getElementById("studentId").value != ''){
		studentId = document.getElementById("studentId").value;
	}
	if(document.getElementById("name").value != ''){
		name = document.getElementById("name").value;
	}
	if(document.getElementById("gender").value != ''){
		gender = document.getElementById("gender").value;
	}
	if(document.getElementById("email").value != ''){
		email = document.getElementById("email").value;
	}
	if(document.getElementById("mobile").value != ''){
		mobile = document.getElementById("mobile").value;
	}
	if(document.getElementById("grade").value != ''){
		grade = document.getElementById("grade").value;
	}
	if(document.getElementById("tatm1").value != ''){
		tatm1 = document.getElementById("tatm1").value;
	}
	if(document.getElementById("tatm2").value != ''){
		tatm2 = document.getElementById("tatm2").value;
	}
			
	var studentobj = {
		studentId : studentId,
		name : name,
		gender: gender,
		email : email,
		mobile: mobile,
		grade: grade,
		totalAttempts:tatm1,
		attendQues:tatm2
	};
	
	$.ajax({
		url: '/secure/searchStudentList',
		type: 'POST',
		data: studentobj,
		cache: false,
		success: function (dataList) {

			var tableBody = document.getElementById("rptData");
			tableBody.innerHTML = "";
			
			for (var i = 0; i < dataList.length;i++) {
				tableBody.innerHTML += "<tr>" +
											"<td>" + dataList[i]['studentId'] +"</td>"+
											"<td>" + dataList[i]['name'] + "</td>" +
											"<td>" + dataList[i]['gender'] + "</td>" +
											"<td>" + dataList[i]['email'] + "</td>" +
											"<td>" + dataList[i]['mobile'] + "</td>" +
											"<td>" + dataList[i]['grade'] + "</td>" +
											"<td>" + dataList[i]['totalAttempts'] + "</td>" +
											"<td>" + 0 + "</td>" + 
											"<td>" + dataList[i]['active'] + "</td>" +
										"</tr>";	
				if(dataList.length == 1){
					setStudentReport1(dataList[i]['papers'],dataList[i]['moduleResults']);
					document.getElementById("individualReport").style.display = "block"; 
				}else{
					document.getElementById("individualReport").style.display = "none"; 
				}
			}
			
			
			
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not process!',
		      'error'
		    )
		}
	});
		
}

function setStudentReport1(papers,moduleResults){
	var tableBody = document.getElementById("rptData2");
	tableBody.innerHTML = "";
	for (var i = 0; i < papers.length;i++) {
		tableBody.innerHTML += "<tr>" +
									"<td>" + papers[i]['paper'] +"</td>"+
									"<td>" + papers[i]['date'] + "</td>" +
									/*"<td>" + papers[i]['time'] + "</td>" +*/
									"<td>" + papers[i]['questionCount'] + "</td>" +
									"<td>" + papers[i]['answeredCount'] + "</td>" +
									"<td>" + papers[i]['correctCount'] + "</td>" +
									"<td>" + papers[i]['total'] + "%</td>" +
								"</tr>";		
	}
	
	tableBody = document.getElementById("rptData3");
	tableBody.innerHTML = "";
	
	for (var i = 0; i < moduleResults.length;i++) {
		tableBody.innerHTML += "<tr>" +
									"<td>" + moduleResults[i]['subjectId']['subjectName'] +"</td>"+
									"<td>" + moduleResults[i]['paperCount'] +"</td>"+
									"<td colspan = 5></td>" + 
								"<tr>";
		for(var j = 0;j <  moduleResults[i]['moduleResultList'].length;j++){
			tableBody.innerHTML += "<tr>" +
										"<td colspan = 2></td>" +
										"<td>" + moduleResults[i]['moduleResultList'][j]['module']['name'] + "</td>" +
										"<td>" + moduleResults[i]['moduleResultList'][j]['allQuestionCount'] + "</td>" +
										"<td>" + moduleResults[i]['moduleResultList'][j]['attendQuestionCount'] + "</td>" +
										"<td>" + moduleResults[i]['moduleResultList'][j]['correctQuestionCount'] + "</td>" +
										"<td>" + moduleResults[i]['moduleResultList'][j]['result'] + "</td>" +
									"</tr>";
		}									
	}			

}

function searchUserList(){
	var userId = 0;
	var name = 0;
	var gender = 0;
	var userType =0;
	var email = 0;
	var mobile = 0;
	
	if(document.getElementById("userId").value != ''){
		userId = document.getElementById("userId").value
	}
	if(document.getElementById("name").value != ''){
		name = document.getElementById("name").value
	}
	if(document.getElementById("gender").value != ''){
		gender = document.getElementById("gender").value
	}
	if(document.getElementById("email").value != ''){
		email = document.getElementById("email").value
	}
	if(document.getElementById("mobile").value != ''){
		mobile = document.getElementById("mobile").value
	}
	if(document.getElementById("userType").value != ''){
		userType = document.getElementById("userType").value
	}
				
	var userobj = {
		userId : userId,
		name : name,
		gender: gender,
		email : email,
		mobile: mobile,
		roleId: userType
	};

	$.ajax({
		url: '/secure/searchUserList',
		type: 'POST',
		data: userobj,
		cache: false,
		success: function (dataList) {

			var tableBody = document.getElementById("rptData");
			tableBody.innerHTML = "";
			
			for (var i = 0; i < dataList.length;i++) {
				tableBody.innerHTML += "<tr>" +
											"<td>" + dataList[i]['userId'] +"</td>"+
											"<td>" + dataList[i]['name'] + "</td>" +
											"<td>" + dataList[i]['gender'] + "</td>" +
											"<td>" + dataList[i]['email'] + "</td>" +
											"<td>" + dataList[i]['mobile'] + "</td>" +
											"<td>" + dataList[i]['role'] + "</td>" +
											"<td>" + dataList[i]['active'] + "</td>" +
										"</tr>";		
			}
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not process!',
		      'error'
		    )
		}
	});
}


function searchPublisherList(){

	var poId = 0;
	var name = 0;
	var email = 0;
	var mobile = 0;
	var address = 0;

	if(document.getElementById("poId").value != ''){
		poId = document.getElementById("poId").value
	}
	if(document.getElementById("name").value != ''){
		name = document.getElementById("name").value
	}
	if(document.getElementById("email").value != ''){
		email = document.getElementById("email").value
	}
	if(document.getElementById("mobile").value != ''){
		mobile = document.getElementById("mobile").value
	}
	if(document.getElementById("address").value != ''){
		address = document.getElementById("address").value
	}

	var publisgerobj = {
		poId : poId,
		name : name,
		email : email,
		mobile: mobile,
		address: address
	};

	$.ajax({
		url: '/secure/searchPublisherList',
		type: 'POST',
		data: publisgerobj,
		cache: false,
		success: function (dataList) {

			var tableBody = document.getElementById("rptData");
			tableBody.innerHTML = "";
			
			for (var i = 0; i < dataList.length;i++) {
				tableBody.innerHTML += "<tr>" +
											"<td>" + dataList[i]['poId'] +"</td>"+
											"<td>" + dataList[i]['name'] + "</td>" +
											"<td>" + dataList[i]['date'] + "</td>" +
											"<td>" + dataList[i]['email'] + "</td>" +
											"<td>" + dataList[i]['mobile'] + "</td>" +
											"<td>" + dataList[i]['address'] + "</td>" +
											"<td>" + dataList[i]['authors'] + "</td>" +
											"<td>" + dataList[i]['editors'] + "</td>" +
											"<td>" + dataList[i]['paperCount'] + "</td>" +
											"<td>" + dataList[i]['active'] + "</td>" +
										"</tr>";		
			}
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not process!',
		      'error'
		    )
		}
	});
}

function searchPaperList(){

	var paperId = 0;
	var name = 0;
	var grade = 0;
	var subject = 0;
	var publisher = 0;

	if(document.getElementById("paperId").value != ''){
		paperId = document.getElementById("paperId").value
	}
	if(document.getElementById("grade").value != ''){
		grade = document.getElementById("grade").value
	}
	if(document.getElementById("subject").value != ''){
		subject = document.getElementById("subject").value
	}
	if(document.getElementById("publisher").value != ''){
		publisher = document.getElementById("publisher").value
	}
	if(document.getElementById("name").value != ''){
		name = document.getElementById("name").value
	}

	var paperobj = {
		paperId : paperId,
		name : name,
		grade : grade,
		subject: subject,
		publisher: publisher
	};

	$.ajax({
		url: '/secure/searchPaperList',
		type: 'POST',
		data: paperobj,
		cache: false,
		success: function (dataList) {

			var tableBody = document.getElementById("rptData");
			tableBody.innerHTML = "";
			
			for (var i = 0; i < dataList.length;i++) {
				tableBody.innerHTML += "<tr>" +
											"<td>" + dataList[i]['paperId'] +"</td>"+
											"<td>" + dataList[i]['name'] + "</td>" +
											"<td>" + dataList[i]['numberOfQuestion'] + "</td>" +
											"<td>" + dataList[i]['time'] + "</td>" +
											"<td>" + dataList[i]['grade'] + "</td>" +
											"<td>" + dataList[i]['subject'] + "</td>" +
											"<td>" + dataList[i]['publisher'] + "</td>" +
											"<td>" + dataList[i]['attemptCount'] + "</td>" +
											"<td>" + dataList[i]['active'] + "</td>" +
										"</tr>";		
			}
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not process!',
		      'error'
		    )
		}
	});
}

function searchUserPaperList(){

	var userId = 0;

	if(document.getElementById("userId").value != ''){
		userId = document.getElementById("userId").value
	}

	upDto = {
		userId : userId
	}
	
	$.ajax({
		url: '/secure/searchUserPaperList',
		type: 'POST',
		data: upDto,
		cache: false,
		success: function (dataList) {
			
			var tableBody = document.getElementById("rptData");
			tableBody.innerHTML = "<tr>" +
										"<td>" + dataList[0]['userId'] +"</td>"+
										"<td>" + dataList[0]['userName'] + "</td>" +
										"<td>" + dataList[0]['totalCreateCount'] + "</td>" +
										"<td>" + dataList[0]['onActiveCount'] + "</td>" +
										"<td>" + dataList[0]['onDeactiveCount'] + "</td>" +
										"<td>" + dataList[0]['attendedCount'] + "</td>" +
									"</tr>";
			
			var tableBody = document.getElementById("rptData2");
			tableBody.innerHTML = "";
			
			for (var i = 1; i < dataList.length;i++) {
				tableBody.innerHTML += "<tr>" +
											"<td>" + dataList[i]['paperId'] +"</td>"+
											"<td>" + dataList[i]['paperName'] + "</td>" +
											"<td>" + dataList[i]['grade'] + "</td>" +
											"<td>" + dataList[i]['subject'] + "</td>" +
											"<td>" + dataList[i]['publisher'] + "</td>" +
											"<td>" + dataList[i]['active'] + "</td>" +
											"<td>" + dataList[i]['attendedCount'] + "</td>" +
											"<td>" + dataList[i]['averageScore'] + "</td>" +
										"</tr>";		
			}
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not process!',
		      'error'
		    )
		}
	});
}

function searchPurchaseList(){

	var userId = 0;
	var paperId = 0;
	var purchasedDate = 0;
	var expireDate = 0;

	if(document.getElementById("userId").value != ''){
		userId = document.getElementById("userId").value
	}
	if(document.getElementById("paperId").value != ''){
		paperId = document.getElementById("paperId").value
	}
	if(document.getElementById("purchasedDate").value != ''){
		purchasedDate = document.getElementById("purchasedDate").value
	}
	if(document.getElementById("expireDate").value != ''){
		expireDate = document.getElementById("expireDate").value
	}

	opDto = {
		userId : userId,
		id : paperId,
		purchaseDate : purchasedDate,
		expireDate : expireDate		
	}
		
	$.ajax({
		url: '/secure/searchPurchaseList',
		type: 'POST',
		data: opDto,
		cache: false,
		success: function (dataList) {

			var tableBody = document.getElementById("rptData");
			tableBody.innerHTML = "";
			
			for (var i = 0; i < dataList.length;i++) {
				tableBody.innerHTML += "<tr>" +
											"<td>" + dataList[i]['userId'] +"</td>"+
											"<td>" + dataList[i]['userName'] + "</td>" +
											"<td>" + dataList[i]['type'] + "</td>" +
											"<td>" + dataList[i]['id'] + "</td>" +
											"<td>" + dataList[i]['paperName'] + "</td>" +
											"<td>" + dataList[i]['purchaseDate'] + "</td>" +
											"<td>" + dataList[i]['expireDate'] + "</td>" +
											"<td>" + dataList[i]['status'] + "</td>" +
										"</tr>";		
			}
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not process!',
		      'error'
		    )
		}
	});
}

function updateSubjectList() {
	var listBox = document.getElementById("subject");
    listBox.options.length = 0;
    
	var selectGrade = document.getElementById('grade').value;
	
	$.ajax({
		url: '/getSubjectsByGrade',
		type: 'GET',
		traditional: true,
		data: {gradeId: selectGrade},
		success: function (subjects) {
			
			if(subjects.length === 0){
				$('#subject').append(`<option value=" ">NO SUBJECTS </option>`);
			}else{
				$('#subject').append(`<option value=''>SUBJECT</option>`);
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




