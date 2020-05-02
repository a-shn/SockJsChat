var sock = new SockJS('http://localhost:8080/init');
sock.onopen = function () {
    console.log('open');
};

sock.onmessage = function (response) {
    var json_mes = JSON.parse(response.data)
    $('#messages').first().after('<li>\'' + json_mes['text'] + '\' - said ' + json_mes["from"] + '</li>');
};

sock.onclose = function () {
    console.log('close');
};

function sendMessage(text) {
    sock.send(text)
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
        }
    })
}