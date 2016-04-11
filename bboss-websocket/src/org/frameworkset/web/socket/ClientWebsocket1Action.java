package org.frameworkset.web.socket;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class ClientWebsocket1Action {
	@OnOpen
	public void onOpen(Session session)
	{
		System.out.println("connect to endpoint"+session.getBasicRemote());
		try {
			session.getBasicRemote().sendText("Hello!I'm 多多！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(String message)
	{
		System.out.println("Client receive message:"+message);
	}

}
