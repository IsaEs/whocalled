var stompClient = null;
var jwtToken = null
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        disableOrEnableForms(!connected)
    }
    else {
        $("#conversation").hide();
        disableOrEnableForms(!connected)
    }
    $("#notifications").html("");
}

function connect() {
    var socket = new SockJS('/wsocket');
    stompClient = Stomp.over(socket);
    var loginToken = 'Bearer ' + jwtToken;
    console.log(loginToken);
    stompClient.connect({'Authorization': loginToken}, function (frame) {
        setConnected(true);
        var url = stompClient.ws._transport.url;
        console.log('Socket Url',url);
        console.log('Connected: ' + frame);
        var sessionId = getSessionId(url);
        console.log("Your current session is: " + sessionId);
        stompClient.subscribe('/user/queue/notifications', function (msg) {
            showNotification(msg.body);
        });
        setTimeout(registerForNotification, 1000);
    });
}

function getSessionId(url){
    url = url.replace(
        "ws://localhost:8080/wsocket/",  "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    return url
}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

//JSON.stringify({'name': $("#name").val()}));
function registerForNotification() {
    console.log("Register for notification...")
    stompClient.send("/app/register",{'phone': $( "#username" ).val()},'');
    sCallElemens(false)
}

function callNumber() {
    stompClient.send("/app/call",{'phone': $("#dialed").val()},'');
}

function showNotification(message) {
    $("#notifications").append("<tr><td>" + message + "</td></tr>");
}

function disableOrEnableForms(conn){
    $("#call-over-socket :input").prop( "disabled", conn );
    $("#call-over-rest :input").prop( "disabled", conn );
    $("#login-form :input").prop( "disabled", !conn );
}




function login(){
    console.log("Sign In...")
    $.ajax({
        type: "POST",
        url: '/api/signin',
        contentType: "application/json",
        dataType : "json",
        data: JSON.stringify({
            phoneNo: $("#username").val(),
            password: $("#password").val()
        }),
        success: function(data) {
            jwtToken = data.token;
            connect();
        },
        error: function (){
            alert("Login Failed");
        }
    });
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    disableOrEnableForms(true)

    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { registerForNotification(); });
    $( "#call" ).click(function() { callNumber(); });
    $( "#login" ).click(function() { login(); });
});