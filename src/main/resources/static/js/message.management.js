var selectedChatId = 0;
var stompClient = null;
var currentChat = null;

$(document).ready(function () {
	connect();
});

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	var socket = new SockJS('/mcq-websocket');
	stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
		setConnected(true);
		stompClient.subscribe('/topic/newMessage', function (message) {
			var obj=JSON.parse(message.body);
			addMessage(obj);
        });
    });
}

function showMessages(id){
	selectedChatId = id;
	currentChat = document.getElementById(id).value;
	var sentFrom = document.getElementById("email").value; //logged user
	var sentTo = document.getElementById(id).value;
	
	//alert(cmId + sentFrom + sentTo);
	
	document.getElementById("sentTo").value = sentFrom;
	document.getElementById("sentFrom").value = sentTo;
	
	$.ajax({
		url: '/getMessages',
		type: 'GET',
		traditional: true,
		data: {sentFrom: sentFrom , sentTo: sentTo},
		success: function (response) {
			//window.location.reload();
			openChat(response);
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not go to chat',
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function getMessages(sentFrom){
	currentChat = sentFrom;
	var sentTo = document.getElementById("email").value;
	
	document.getElementById("sentFrom").value = sentTo;
	document.getElementById("sentTo").value = sentFrom;
	
	$.ajax({
		url: '/getMessages',
		type: 'GET',
		traditional: true,
		data: {sentFrom: sentFrom , sentTo: sentTo},
		success: function (response) {
			//window.location.reload();
			console.log();			
			openChat(response);
		},
		error:function(status, error){
			 Swal.fire(
		      'Can not go to chat',
		      'Something went wrong',
		      'error'
		    )
		}
	});
}

function openChat(messageList){
	var messageHistory = document.getElementById("msg_history"); 	
	messageHistory.innerHTML = null;
	var newChildNode = document.createElement('div');
	for (let message in messageList){
		
		if(messageList[message]["type"] == "incoming"){
			newChildNode.innerHTML += 
				"<div class='incoming_msg'>" +
			      	"<div class='incoming_msg_img'>" + 
			      		"<img src='/img/profile.png' >" + 
			      	" </div>" +
			      	"<div class='received_msg'>" +
			        	"<div class='received_withd_msg'>" +
			          		"<p>" + messageList[message]["message"] + "</p>" +
			          		"<span class='time_date'>" + messageList[message]["sentTime"] + "    |    " + messageList[message]["sentDate"] + "</span>" + 
			      		"</div>" +
			      	"</div>" +
				 "</div>";				
		}else if(messageList[message]["type"] == "outgoing"){
			
			newChildNode.innerHTML += 
			    "<div class='outgoing_msg'>" +
			    	"<div class='sent_msg'>" +
			        	"<p>" + messageList[message]["message"] + "</p>" +
			        	"<span class='time_date'>" + messageList[message]["sentTime"] + "    |    " + messageList[message]["sentDate"] + "</span>" + 
			    	" </div>" +
				 "</div>";				
		}
		
		messageHistory.appendChild(newChildNode); 
		//document.getElementById(messageList[message]["cmId"]).classList.remove("active_chat");	
	}
	
	var messageBody = document.querySelector('#msg_history');
	messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
}

function addMessage(message){
	
	var messageHistory = document.getElementById("msg_history"); 	
	
	var newChildNode = document.createElement('div');
	
	if((currentChat == message["sentFrom"]) || (currentChat == message["sentTo"])){
		if(document.getElementById("email").value == message["sentTo"]){
			newChildNode.innerHTML += 
				"<div class='incoming_msg'>" +
			      	"<div class='incoming_msg_img'>" + 
			      		"<img src='/img/profile.png' >" + 
			      	" </div>" +
			      	"<div class='received_msg'>" +
			        	"<div class='received_withd_msg'>" +
			          		"<p>" + message["message"] + "</p>" +
			          		"<span class='time_date'>" + message["sentTime"] + "    |    " + message["sentDate"] + "</span>" + 
			      		"</div>" +
			      	"</div>" +
				 "</div>";				
		}else if(document.getElementById("email").value == message["sentFrom"]){
			
			newChildNode.innerHTML += 
			    "<div class='outgoing_msg'>" +
			    	"<div class='sent_msg'>" +
			        	"<p>" + message["message"] + "</p>" +
			        	"<span class='time_date'>" + message["sentTime"] + "    |    " + message["sentDate"] + "</span>" + 
			    	" </div>" +
				 "</div>";				
		}	
	}
		
	messageHistory.appendChild(newChildNode); 
}

function sendMessage(classroomId){
	
	var message = document.getElementById("messageContent").value;
	
	var sentFrom = document.getElementById("email").value;
	if(sentFrom == document.getElementById("sentFrom").value){
		var sentTo = document.getElementById("sentTo").value;
	}else{
		var sentTo = document.getElementById("sentFrom").value;
	}
	
	/*
	var sentTo = document.getElementById("sentTo").value;
	var sentFrom = document.getElementById("sentFrom").value;
		*/
	//alert(message != "" && sentTo != "" && sentFrom != "");
	//alert(message + '|' + sentTo + '|' + sentFrom );
	
	if(message != "" && sentTo != "" && sentFrom != ""){
		$.ajax({
			url: '/secure/sendMessages',
			type: 'POST',
			traditional: true,
			data: {message: message , sentFrom: sentFrom , sentTo: sentTo , classroomId : classroomId},
			success: function (message) {
				
				//window.location.reload();
				document.getElementById("messageContent").value = " ";
				//showMessages(selectedChatId);
				
			},
			error:function(status, error){
				 Swal.fire(
			      'Can not send mesaage!',
			      'Something went wrong',
			      'error'
			    )
			}
		});	
	}else{
		document.getElementById("messageContent").value = "";
	}
		
}




