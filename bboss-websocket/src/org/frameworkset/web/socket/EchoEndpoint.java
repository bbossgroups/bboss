package org.frameworkset.web.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by shanl on 14-3-3.
 */
@ServerEndpoint(value="/echoendpoint")
public class EchoEndpoint {
    @OnOpen
    public void start(Session session){
        System.out.println("session "+session.getId()+" open.");
    }

    @OnMessage
    public void process(Session session, String message){
        System.out.println("rece:" + message);
        RemoteEndpoint.Basic remote = session.getBasicRemote();
        int c = Integer.valueOf(message);
        for (int i=1; i<=c; i++){
            try {
                remote.sendText("response "+i);
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void end(Session session){
        System.out.println("session "+session.getId()+" close.");
    }

    @OnError
    public void error(Session session, java.lang.Throwable throwable){
        System.err.println("session "+session.getId()+" error:"+throwable);
    }
}
