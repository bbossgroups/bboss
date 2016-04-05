package org.frameworkset.web.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by shanl on 14-3-2.
 */
@ServerEndpoint(value = "/Websocket1Action")
public class Websocket1Action extends Endpoint {
    private Session session;
    //private static final Logger sysLogger = Logger.getLogger("sysLog");
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("onClose");
    }

    @Override
    public void onError(Session session, java.lang.Throwable throwable) {
        System.out.println("onError");
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        //sysLogger.info("*** WebSocket closed from sessionId " + this.session.getId());
        RemoteEndpoint.Basic remote =  session.getBasicRemote();

        System.out.println("pathParams:"+session.getPathParameters());
        System.out.println("requestParams"+session.getRequestParameterMap());
        session.addMessageHandler(new MyMessageHandle(remote));
        /***
        try{
            System.out.println("onOpen");
            //System.out.println(session.getQueryString());
            System.out.println(session.getRequestParameterMap());
            session.getBasicRemote().getSendWriter().write("success");
            //session.getBasicRemote().sendText("success");
        }catch(Exception ex){
            ex.printStackTrace();
        }
         ***/
    }

    private class MyMessageHandle implements MessageHandler.Whole<String> {
        RemoteEndpoint.Basic remote =  null;
        public MyMessageHandle(RemoteEndpoint.Basic remote){
            this.remote = remote;
        }

        @Override
        public void onMessage(String s) {
            try {
                remote.sendText("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
