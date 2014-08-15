/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.spi.remote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.spi.RemoteCallContext;
import org.frameworkset.spi.remote.BaseRPCIOHandler.Marshaller2;
import org.frameworkset.spi.security.SecurityContext;

/**
 * <p>
 * Title: RPCClient.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-10-7 下午05:28:38
 * @author biaoping.yin
 * @version 1.0
 */
public final class RPCClient
{

	
	private static RPCClient instance = null;
	
	
	
	public static RPCClient getInstance()
	{
		if(instance != null)
			return instance;
		synchronized(RPCClient.class)
		{
			if(instance != null)
				return instance;
			
			instance = new RPCClient();
			
		}
		return instance;
	}
	
	
	  
	
	
	

	private RPCClient()
	{
//		DefaultRemoteHandler remoteHandler = new DefaultRemoteHandler();
//		corr = new RPCServerIoHandler("RPCServerIoHandler",new RPCRequestHandler(remoteHandler));
//		corr = Util.getRPCServerIoHandler();
	}
	
	
	protected final Logger			log				= Logger.getLogger(getClass());

	

//	private RPCIOHandler	corr			= null;

	protected Marshaller2		req_marshaller	= null;

	@SuppressWarnings("unchecked")
	public Object callRemoteMethod(String host, int port, String method_name,
			Object[] args, Class[] types, int mode, long timeout,String protocol,RemoteCallContext callContext)
			throws Throwable
	{

		return callRemoteMethod(new RPCAddress(host, port,null,protocol), method_name, args,
								types, mode, timeout,protocol,callContext);
	}


	@SuppressWarnings("unchecked")
	public Object callRemoteMethod(RPCAddress dest, String method_name,
			Object[] args, Class[] types, int mode, long timeout,String protocol,RemoteCallContext callContext)
			throws Throwable
	{

		RPCMethodCall method_call = new RPCMethodCall(method_name, args, types,callContext == null? null:callContext.getSecutiryContext());
		byte[] buf = null;
		RPCMessage msg = null;
		Object retval = null;
		int expected_mbrs = 1;
		List<RPCAddress> mbrs = new ArrayList<RPCAddress>();
		if (dest == null)
		{
//			if (log.isErrorEnabled())
				log
						.error("the message's destination is null, cannot send message");
			return null;
		}
		mbrs.add(dest);
		
		if(!BaseRPCIOHandler.useOOB)
		{
			buf = Util.objectToByteBuffer(method_call);
			
			msg = new RPCMessage(null,dest);		
			msg.setEncrypt(SecurityContext.getSecurityManager().enableEncrypt());
			
			msg.setBuffer( buf);
		}
		else
		{
			msg = new RPCMessage(null,dest);		
//			msg.setEncrypt(SecurityContext.getSecurityManager().enableEncrypt());
			msg.setResultSerial(RPCMessage.OOB);
			msg.setData( method_call);
		}
		
		if(callContext != null && callContext.getHeaders() != null && callContext.getHeaders().size() > 0)
		    msg.setHeaders(callContext.getHeaders());		
//		msg.setEncrypt(SecurityContext.getSecurityManager().enableEncrypt());
		RPCResponseList rsp_list = sendMessage(mbrs, msg, mode, timeout,
												expected_mbrs,protocol);
		
		if (rsp_list.isEmpty())
		{		
			return null;
		}
		if (rsp_list.size() > 1)
		{
//			if (log.isWarnEnabled())
				log
						.warn("response list contains more that 1 response; returning first response !");
		}
		RPCResponse rsp = (RPCResponse) rsp_list.elementAt(0);
		if (rsp.wasSuspected())
		{
			throw new SuspectedException(dest);
		}
		if (!rsp.wasReceived())
		{
			throw new TimeoutException("timeout sending message to " + dest);
		}
		retval = rsp.getValue();
		handleException(retval);
//		if (retval instanceof RemoteException)
//			throw (Throwable) retval;
//		else if (retval instanceof Throwable)
//		{
////			throw (Throwable)retval;
//			throw new RemoteException((RPCMessage)null,(Throwable)retval);
//		}
		return retval;

	}
	
	public static void handleException(Object retval) throws Throwable 
	{
		if(retval == null)
			return ;
		if (retval instanceof RemoteException)			
			throw (Throwable) retval;
		else if(retval instanceof java.lang.reflect.InvocationTargetException)
		{
			throw ((java.lang.reflect.InvocationTargetException)retval).getTargetException();
		}
		else if (retval instanceof Throwable)
		{
			throw (Throwable)retval;
			
//			throw new RemoteException((RPCMessage)null,(Throwable)retval);
		}
		
	}
	
	

	public RPCResponseList callRemoteMethods(List<RPCAddress> dests,
			RPCMethodCall method_call, int resultMode, long timeout,
			boolean use_anycasting,  ResponseFilter filter,String protocol,Headers headers)
	{

		if (dests != null && dests.isEmpty())
		{		
			log.info(new StringBuilder("destination list of ")
						.append(method_call.getMethodName())
						.append("() is empty: no need to send message"));
			return new RPCResponseList();
		}
		byte[] buf = null;
		RPCMessage msg = null;
		int expected_mbrs = 1;
//		if (log.isTraceEnabled())
		log.info(new StringBuilder("dests=").append(dests)
					.append(", method_call=").append(method_call)
					.append(", resultMode=").append(resultMode).append(", timeout=")
					.append(timeout));
		if(!BaseRPCIOHandler.useOOB)
		{
			try
			{
				buf =  Util.objectToByteBuffer(method_call);
			}
			catch (Exception e)
			{			
				throw new RuntimeException("failure to marshal argument(s)", e);
			}
	
			msg = new RPCMessage();
			try {
				msg.setEncrypt(SecurityContext.getSecurityManager().enableEncrypt());
				msg.setBuffer(buf);
			} catch (Exception e) {
				throw new RuntimeException("failure to setBuffer ", e);
			}
		}
		else
		{
			msg = new RPCMessage();
			try {
//				msg.setEncrypt(SecurityContext.getSecurityManager().enableEncrypt());
				msg.setData(method_call);
//				msg.setBuffer(buf);
				msg.setResultSerial(msg.OOB);
			} catch (Exception e) {
				throw new RuntimeException("failure to setBuffer ", e);
			}
		}
		
		if(headers != null && headers.size() > 0)
		    msg.setHeaders(headers);
		// if(oob)
		// msg.setFlag(Message.OOB);
		RPCResponseList rsp_list = sendMessage(dests, msg, resultMode, timeout,
												expected_mbrs,protocol);
		// if(log.isTraceEnabled()) log.trace("retval: " + retval);
		if (rsp_list.isEmpty())
		{
			// if(log.isWarnEnabled())
			// log.warn(" response list is empty");
			return null;
		}		
//		prehandleResponses(rsp_list);		
//		if (log.isTraceEnabled())
//		log.info("responses: " + rsp_list);
		return rsp_list;
	}
	
	private void prehandleResponses(RPCResponseList rsp_list) throws MuticastCallException
	{
	    Set<Entry<RPCAddress,RPCResponse>> entries = rsp_list.entrySet();
	    MuticastCallException e = null;
	    Iterator<Entry<RPCAddress,RPCResponse>> its = entries.iterator();
	    while(its.hasNext())
	    {
	        Entry<RPCAddress,RPCResponse> entry = its.next();
	        RPCResponse rsp = entry.getValue();
	        Object value = rsp.getValue();
	        if(value != null && value instanceof Throwable)
	        {
	            if(e == null)
	                e = new MuticastCallException();
	            e.addException(entry.getKey(), new RemoteException((String)null,(Throwable)value));
	        }
	            
	    }
	    if(e != null)
	        throw e;
	}

	/**
	 * Sends a message to a single member (destination = msg.dest) and returns
	 * the response. The message's destination must be non-zero !
	 */
	public RPCResponseList sendMessage(List<RPCAddress> mbrs, RPCMessage msg,
			int resultMode, long timeout, int expected_mbrs,String protocol) 
	{

		
		RPCResponseList rsp_list = null;
		
		
		RPCRequest _req = null;

		if (mbrs == null)
		{
//			if (log.isErrorEnabled())
				log
						.error("the message's destination is null, cannot send message");
			return null;
		}

		// mbrs.add(dest); // dummy membership (of destination address)

		_req = new RPCRequest(msg, Util.getRPCIOHandler(protocol), mbrs, resultMode, timeout, expected_mbrs);
//		_req.setCaller(local_addr);
		try
		{
			_req.execute();
		}
		catch (Exception t)
		{
			throw new RuntimeException("调用远程服务失败，请确认服务器已经启动或者检查网络是否联通： " + _req, t);
		}

		if (resultMode == RPCRequest.GET_NONE)
		{
			return null;
		}

		rsp_list = _req.getResults();
		return rsp_list;

	}

	@SuppressWarnings("unchecked")
	public RPCResponseList callRemoteMethod(List<RPCAddress> dests, String method_name,
			Object[] params, Class[] rpTypes, int resultMode, long timeout,boolean use_anycasting,  ResponseFilter filter,String protocol,RemoteCallContext callContext)
	{
		RPCMethodCall method_call = new RPCMethodCall(method_name, params, rpTypes,callContext == null?null:callContext.getSecutiryContext());
		return callRemoteMethods(dests,
		             			method_call, resultMode, timeout,
		            			use_anycasting,  filter,protocol,callContext == null?null:callContext.getHeaders());
		
	}

	// public void sendRequest(RPCRequest request) {
	// System.out.println(session);
	// if (session == null) {
	// //noinspection ThrowableInstanceNeverThrown
	// // imageListener.onException(new Throwable("not connected"));
	// } else {
	// session.write(request);
	// }
	// }
	//
	// public void messageReceived(IoSession session, Object message) throws
	// Exception {
	// RPCResponse response = (RPCResponse) message;
	// System.out.println("response = " +response);
	// // imageListener.onImages(response.getImage1(), response.getImage2());
	// }
	//
	// public void exceptionCaught(IoSession session, Throwable cause) throws
	// Exception {
	// cause.printStackTrace();
	// // imageListener.onException(cause);
	// }

	// private static class MessageInspector implements ResponseInspector {
	// /**
	// * Default constructor
	// */
	// public MessageInspector() {
	// super();
	// }
	//        
	// public Object getRequestId(Object message) {
	// if (!(message instanceof Message)) {
	// return null;
	// }
	//
	// return ((Message) message).getId();
	// }
	//
	// public ResponseType getResponseType(Object message) {
	// if (!(message instanceof Message)) {
	// return null;
	// }
	//
	// return ((Message) message).getType();
	// }
	// }
	//    
	// static class Message {
	// private final int id;
	//
	// private final ResponseType type;
	//
	// Message(int id, ResponseType type) {
	// this.id = id;
	// this.type = type;
	// }
	//
	// public int getId() {
	// return id;
	// }
	//
	// public ResponseType getType() {
	// return type;
	// }
	// }

	// private static class WriteRequestMatcher extends AbstractMatcher {
	// private WriteRequest lastWriteRequest;
	//
	// /**
	// * Default constructor
	// */
	// public WriteRequestMatcher() {
	// super();
	// }
	//        
	// public WriteRequest getLastWriteRequest() {
	// return lastWriteRequest;
	// }
	//
	// @Override
	// protected boolean argumentMatches(Object expected, Object actual) {
	// if (actual instanceof WriteRequest
	// && expected instanceof WriteRequest) {
	// boolean answer = ((WriteRequest) expected).getMessage().equals(
	// ((WriteRequest) actual).getMessage());
	// lastWriteRequest = (WriteRequest) actual;
	// return answer;
	// }
	// return super.argumentMatches(expected, actual);
	// }
	// }
	//
	// static class ExceptionMatcher extends AbstractMatcher {
	// @Override
	// protected boolean argumentMatches(Object expected, Object actual) {
	// if (actual instanceof RequestTimeoutException
	// && expected instanceof RequestTimeoutException) {
	// return ((RequestTimeoutException) expected)
	// .getRequest()
	// .equals(((RequestTimeoutException) actual).getRequest());
	// }
	// return super.argumentMatches(expected, actual);
	// }
	// }
	
	

}
