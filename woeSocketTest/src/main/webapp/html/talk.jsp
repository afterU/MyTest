<%--
  Created by IntelliJ IDEA.
  User: z
  Date: 2017/7/28
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Java后端WebSocket的Tomcat实现</title>
</head>
<body>
Welcome<br/><input id="text" type="text" />
<button onclick="send()">发送消息</button>
<hr/>
<button onclick="closeWebSocket()">关闭WebSocket链接</button>
<hr/>
<div id="message"></div>
</body>
<script>
    var websocket = null;
    //判断当前浏览器是否支持WebScoket
    if('WebSocket' in window){
        websocket  = new WebSocket("ws://localhost:8080/websocket");
    }else{
        alert('当前浏览器 Not support websocket');
    }
    //链接发生错误的回调方法
    /*  websocket.onerror = function () {
     setMessageInnerHTML("WebSocket连接发生错误")
     };*/
    //连接成功建立的回调方法
    websocket.onopen = function () {
        setMessageInnerHTML("websocket连接成功")
    }
    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }
    //链接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("websocket连接关闭")
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接,防止连接还没有断开就关闭窗口，server端会抛出异常
    window.onbeforeumload = function () {
        closeWebSocket();
    }
    //将消息显示到网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML+'<br/>'
    }
    //关闭websocker连接
    function closeWebSocket(){
        websocket.close();
    }
    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
</script>
</html>
