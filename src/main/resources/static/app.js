var stompClient = null;

$(document).ready(function () {

	connect();

});

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
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
		stompClient.subscribe('/topic/greetings1', function (greeting) {
			showGreeting(greeting.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	stompClient.send("/topic/greetings", {}, $("#name").val());
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    //$( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});


