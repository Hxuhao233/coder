var sessionId = null;
var stompClient = null;
var msgSubscription = null;     // 订阅房间
var recordSubscription = null;  // 订阅消息记录

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    clearMsg();
}

function connect() {

    sessionId = guid();
    var socket = new SockJS('/coderRoom', [], {
        sessionId: () => {
        return sessionId
        }
    }); //构建一个SockJS对象
    stompClient = Stomp.over(socket); //用Stomp将SockJS进行协议封装
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + sessionId);
        recordSubscription = stompClient.subscribe(
            '/user/' + sessionId + '/self',
            function (response) {
                var recordList = JSON.parse(response.body);
                console.log(recordList);
                for (var i=recordList.length-1;i>=0;i--){
                    var message = recordList[i];
                    appendMsg(message.username + " : " + message.content + " at " + message.createdAt);
                }
            }
        );
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
    msgSubscription = stompClient.subscribe(
        '/chat/' + $("#room").val(),
        function (response) {
            var record = JSON.parse(response.body);
            handleMsg(record);
        }
    );

    stompClient.send(
        "/app/enter",
        {},
        JSON.stringify(
            {
                'content': $("#name").val(),
                'roomId': $("#room").val()
            }
        )
    );

    // getMessageRecord($("#room").val(),1,10);

}

/* 退出聊天室 */
function exitRoom() {

    stompClient.send(
        "/app/exit",
        {},
        JSON.stringify(
            {
                'content': $("#name").val(),
                'roomId': $("#room").val()
            }
        )
    );

    // 取消该房间订阅
    msgSubscription.unsubscribe();
    console.log("exit room : " + $("#room").val());
    clearMsg();

}

/* 处理消息 */
function handleMsg(message) {
    if (message.type == 1) {
        appendMsg(message.username + " : " + message.content + " at " + message.createdAt);
    } else {
        console.log(message);
    }
}

/* 显示消息 */
function appendMsg(message) {
    $("#contents").append("<tr><td>" + message + "</td></tr>");
}

/* 清楚消息 */
function clearMsg(){
    $("#contents").html("");
}

/* 发送消息 */
function sendMessage() {
    stompClient.send(
        "/app/chat",
        {},
        JSON.stringify(
            {
                'roomId': $("#room").val(),
                'content': $("#mysend").val()
            }
        )
    );
}

function getMessageRecord(roomId,pageNum,pageSize) {
    stompClient.send(
        "/app/getRecord",
        {},
        JSON.stringify(
            {
                'roomId': roomId,
                'pageNum':pageNum,
                'pageSize': pageSize
            }
        )
    );
}

function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
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