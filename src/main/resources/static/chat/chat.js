//var sessionId = null;
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
        console.log('Connected: ' + socket.sessionId);
        recordSubscription = stompClient.subscribe(
            '/user/' + sessionId + '/self',
            function (response) {
                var recordList = JSON.parse(response.body).list;
                for (var i=0;i<recordList.length;i++){
                    var record = recordList[i];
                    console.log(record);
                    prependMsg(record.username + " : " + record.content + " at " + record.createdAt);
                }
            }
        );
        stompClient.send("/app/init");
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
                'toId': $("#room").val()
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
                'toId': $("#room").val()
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
    console.log(message);
    if (message.type == 1) {
        appendMsg(message.username + " : " + message.content + " at " + message.createdAt);
    }
}

/* 往顶部添加消息 */
function prependMsg(message) {
    $("#contents").prepend("<tr><td>" + message + "</td></tr>");
}


/* 往底部消息 */
function appendMsg(message) {
    $("#contents").append("<tr><td>" + message + "</td></tr>");
}

/* 清空消息 */
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
                'toId': $("#room").val(),
                'content': $("#mysend").val(),
                'type':1
            }
        )
    );
}

/* 查看消息记录 */
function getMessageRecord(toId,pageNum,pageSize) {
    stompClient.send(
        "/app/getRecordByToIdAndPage",
        {},
        JSON.stringify(
            {
                'toId': toId,
                'pageNum':pageNum,
                'pageSize': pageSize,
                'type':1
            }
        )
    );
}

/* 查看消息记录 */
// datetime格式 : yyyy-MM-dd'T'HH:mm:ss
function getMessageRecord2(toId,datetime) {
    stompClient.send(
        "/app/getRecordByToIdAndLastTime",
        {},
        JSON.stringify(
            {
                'toId': toId,
                'time': datetime,
                'type':1
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