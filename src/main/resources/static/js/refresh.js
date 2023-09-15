//'use strict';
$(document).ready(function () {

	connectWebSocket();

});

var interval1 = null;
var interval2 = null;

//Websocket subcribe
var stompClient = null;

function connectWebSocket() {
	console.log("1");
	var socket = new SockJS('/mcq-websocket');
	console.log("2");
	stompClient = webstomp.over(socket);
	console.log("3");
	stompClient.connect({}, function (frame) {
		stompClient.subscribe('/topic/display', function (message) {
				//alert("Changed");
				//window.location.reload();
			
			/*
			var obj=JSON.parse(message.body);
			var next=document.getElementById('nextToken');
				next.innerHTML = obj.nextToken;

				var counter=document.getElementById(obj.counter+'text');
				counter.innerHTML = obj.tokenNumber;
				counter.style.display='';
				if(obj.blink && obj.tokenNumber !='__'){
					interval1=setInterval(function() {counter.style.display = (counter.style.display == 'none' ? '' : 'none');}, 1000);
				}else{
					clearInterval(interval1);
				}	*/
		});
	});
	 
	
}


function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
		stompClient = null;
	}
}

