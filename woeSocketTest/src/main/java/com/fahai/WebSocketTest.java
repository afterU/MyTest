package com.fahai;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by z on 2017/7/28.
 * @ServerEndpoint  注解是一个类层次的注解，它的主要功能将目前的类定义成一个websocket服务器端
 * 注解的值将被用于监听用户连接的终端访问url地址，客户端可以通过这个url来连接到websocket服务器端
 */
@ServerEndpoint("/websocket")
public class WebSocketTest {

    //静态变量，用来记录当前在线连接数，应该把他设计成线程安全的
    private static int onlineCount = 0;

    //concurrent包的线程安全set，用来存放每个客户端对应的MyWebSocket对象，若要实现服务端与单一客户端通信的话，可以使用map来存放，其中key课可以为用户标识
    private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

    //与摸个客户端的链接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 链接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);//加入到set中
        addOnlineCount();//在线数加1
        System.out.println("有新链接加入！当前在线人数为"+getOnlineCount());

    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);//从set中删除
        subOnlineCount();//在线数减1
        System.out.println("有一链接关闭！当前在线人数为："+getOnlineCount());
    }
    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println("来自客户端的消息："+message);
        //群发消息
        for (WebSocketTest item : webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        WebSocketTest.onlineCount++;
    }
    public static synchronized void subOnlineCount(){
        WebSocketTest.onlineCount--;
    }
}
