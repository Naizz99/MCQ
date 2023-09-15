var stompClient = null;

$(document).ready(function () {
	connect();
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
		stompClient.subscribe('/topic/sessionClose', function (classRoomStudentId) {
			console.log("Logout Student : " + classRoomStudentId.body);
			if(classRoomStudentId.body == document.getElementById("studentId").value){
				window.location = "/student/classroomLogout?classroomStudentId=" + classRoomStudentId.body + "&activeSessionId=" + document.getElementById("sessionId").value;
			}
        });
    });
}


