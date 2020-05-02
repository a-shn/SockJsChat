<html>
<head>
    <title>Chat</title>
</head>
<body>
rooms_: </br>
<#list rooms as room>
    <a href="/chat?room=${room.roomId}">${room.name}</a></br>
</#list>
</body>
</html>