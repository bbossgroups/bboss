package org.frameworkset.web.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

/**
 * Tomcat7 WebSocket的簡單應用, WebSocket的servlet接口集成自webSocketServlet,实质为Serlet
 * <p>功能描述,该部分必须以中文句号结尾。<p>
 *
 * 创建日期  2013-7-21<br>
 * @author  longgangbai <br>
 * @version $Revision$ $Date$
 * @since   3.0.0
 */
public class WebSocketServlet extends 
        org.apache.catalina.websocket.WebSocketServlet { 
    //
    private Logger logger=Logger.getLogger(WebSocketServlet.class.getSimpleName());
    
    private static final long serialVersionUID = 1L;

    
    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest arg1) {
        logger.info(";request ws servelt");
        
        return  new MessageInbound(){
            @Override
            protected void onClose(int status){
                logger.info(";web socket closed :"+status);
            }
            @Override
            protected void onOpen(WsOutbound outbound){
                logger.info(";web socket onOpen !");
            }
            @Override
            protected void onBinaryMessage(ByteBuffer buff) throws IOException {
                // TODO Auto-generated method stub
                logger.info(";web socket Received : !"+buff.remaining());
            }

            @Override
            protected void onTextMessage(CharBuffer buff) throws IOException {
            	
//                logger.info(";web socket Received : !"+buff.remaining());
            	String msg = buff.toString();
                logger.info("buff.toString();"+msg);
                //getWsOutbound可以返回当前的WsOutbound,通过他向客户端返回数据,这里采用nio的CharBuffer
//                for (int j = 0; j < 50; j++) 
//                {
//                    try {
//                        Thread.sleep(2000);
//                        this.getWsOutbound().writeTextMessage(CharBuffer.wrap("多多"));
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
                this.getWsOutbound().writeTextMessage(CharBuffer.wrap("多多:"+msg));
                
            }
            
        }; 
    } 

} 
