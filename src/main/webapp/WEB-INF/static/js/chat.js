var sock = new SockJS('http://localhost:8080/init');
sock.onopen = function () {
    console.log('open');
};

sock.onmessage = function (e) {
    console.log('message', e.data);
    sock.close();
};

sock.onclose = function () {
    console.log('close');
};

function sendMessage(text) {
    sock.send(text)
}

function receiveMessage() {
    $.ajax({
        url: "/getNewMessage",
        method: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
            $('#messages').first().after('<li>\'' + response[0]['text'] + '\' - said ' + response[0]["from"] + '</li>');
            receiveMessage();
        }
    })
}

function initiateMessages() {
    $.ajax({
        url: "/getMessages",
        method: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
            for (let i = response.length - 1; i >= 0; i--) {
                $('#messages').first().after('<li>\'' + response[i]['text'] + '\' - said ' + response[i]["from"] + '</li>');
            }
            receiveMessage();
        }
    })
}