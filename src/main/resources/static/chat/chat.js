var stompClient = null;
var subscription = null;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#contents").html("");
}

function connect() {
    var socket = new SockJS('/coder'); //构建一个SockJS对象
    stompClient = Stomp.over(socket); //用Stomp将SockJS进行协议封装
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


/* 加入聊天室 */
function enterRoom() {

    console.log("join room :" + $("#room").val());

    // 订阅该地址，有消息时显示
    subscription = stompClient.subscribe(
        '/chat/' + $("#room").val(),
        function (message) {
            var body = JSON.parse(message.body);
            showGreeting(body.content + body.time);
        }
    );

    stompClient.send(
        "/app/enter",
        {},
        JSON.stringify(
            {
                'content': $("#name").val(),
                'room': $("#room").val()
            }
        )
    );

}

/* 退出聊天室 */
function exitRoom() {

    stompClient.send(
        "/app/exit",
        {},
        JSON.stringify(
            {
                'content': $("#name").val(),
                'room': $("#room").val()
            }
        )
    );

    // 取消订阅
    subscription.unsubscribe();
    console.log("exit room : " + $("#room").val());


}

/* 显示消息 */
function showGreeting(message) {
    $("#contents").append("<tr><td>" + message + "</td></tr>");
}

/* 发送消息 */
function sendMessage() {
    stompClient.send(
        "/app/message",
        {},
        JSON.stringify(
            {
                'room': $("#room").val(),
                'content': $("#name").val() + " : " + $("#mysend").val()
            }
        )
    );
}


$(function () {

    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connect();
    });
    $( "#disconnect" ).click(function() {
        disconnect();
    });
    $( "#enter" ).click(function() {
        enterRoom();
    });
    $( "#exit" ).click(function() {
        exitRoom();
    });
    $( "#mybutton" ).click(function (){
        sendMessage();
    });
});