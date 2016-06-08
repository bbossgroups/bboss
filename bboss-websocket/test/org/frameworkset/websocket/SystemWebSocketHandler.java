package org.frameworkset.websocket;

import org.frameworkset.web.socket.inf.CloseStatus;
import org.frameworkset.web.socket.inf.TextMessage;
import org.frameworkset.web.socket.inf.WebSocketHandler;
import org.frameworkset.web.socket.inf.WebSocketMessage;
import org.frameworkset.web.socket.inf.WebSocketSession;

/**
 *
 * @author dayu
 */
public class SystemWebSocketHandler implements WebSocketHandler {
	private boolean supportsPartialMessages;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......");
        session.sendMessage(new TextMessage("Server:connected OK!"));
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        TextMessage returnMessage = new TextMessage(wsm.getPayload()
				+ " received at server");
		wss.sendMessage(returnMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
            wss.close();
        }
       System.out.println("websocket connection closed......");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println("websocket connection closed......");
    }

    @Override
    public boolean supportsPartialMessages() {
        return supportsPartialMessages;
    }
    
}
