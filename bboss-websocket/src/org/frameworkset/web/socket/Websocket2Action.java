package org.frameworkset.web.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by shanl on 14-3-2.
 */
//@ServerEndpoint(value = "/Websocket2")
public class Websocket2Action {
    private Session session;
    //private static final Logger sysLogger = Logger.getLogger("sysLog");

    @OnOpen
    public void open(Session session,EndpointConfig config) {
        this.session = session;

        //sysLogger.info("*** WebSocket opened from sessionId " + session.getId());
    }

    @OnMessage
    public void inMessage(Session session,String message) {
        //sysLogger.info("*** WebSocket Received from sessionId " + this.session.getId() + ": " + message);
        System.out.println("rece:"+message);
        try {
            session.getBasicRemote().sendText("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void end(Session session) {
        //sysLogger.info("*** WebSocket closed from sessionId " + this.session.getId());
    }

}
