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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.frameworkset.spi.security.SecurityContext;
import org.frameworkset.thread.ThreadPoolExecutor;
import org.frameworkset.thread.ThreadPoolManagerFactory;

import bboss.org.jgroups.util.Buffer;

/**
 * <p>
 * Title: BaseIOHandler.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2009-11-9 下午12:52:30
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseRPCIOHandler implements RPCIOHandler
{
    private ThreadPoolExecutor reqest_threadpool ;
    /**
     * 用来标识是否采用对象结果类型
     */
    public static final boolean useOOB = true;

//    public static ThreadPoolExecutor response_threadpool = ThreadPoolManagerFactory
//            .getThreadPoolExecutor("RPCIOHandler.response.Threadpool");

    
    public ThreadPoolExecutor getReqestThreadpool()
    {
        if(reqest_threadpool != null)
        {
            return reqest_threadpool;
        }
        synchronized(this)
        {
            if(reqest_threadpool != null)
            {
                return reqest_threadpool;
            }
            reqest_threadpool = ThreadPoolManagerFactory
            .getThreadPoolExecutor("RPCIOHandler.request.Threadpool");            
        }
        return reqest_threadpool;
    }
    protected String name;

    public String getName()
    {

        return name;
    }

    protected static final Logger log = Logger.getLogger(BaseRPCIOHandler.class);

    /**
     * The table of pending requests (keys=Long (request IDs), values=
     * <tt>RequestEntry</tt>)
     */
    protected final ConcurrentMap<Long, ResponseCollector> requests = new ConcurrentHashMap<Long, ResponseCollector>();

    /**
     * The handler for the incoming requests. It is called from inside the
     * dispatcher thread
     */
    protected RequestHandler request_handler = null;

//    protected boolean asyn_request = false;

    protected String localaddress = null;

    /**
     * 客服端标识id，作为服务器返回给本客服端的响应消息的特殊标识
     */
    protected RPCAddress src_address;

    public RPCAddress getLocalAddress()
    {
        return this.src_address;
    }

    // protected String webservice_server_type = "cxf";

    public BaseRPCIOHandler(String name, RequestHandler handler,String localaddress)
    {
        this.name = name;
        this.request_handler = handler;
//        this.asyn_request = asyn_request;
        this.localaddress = localaddress;
        // this.webservice_server_type = webservice_server_type;
        // this.local_addr = local_addr;
    }

    /**
     * Add an association of:<br>
     * ID -> <tt>RspCollector</tt>
     */
    protected void addEntry(long id, ResponseCollector coll)
    {
        requests.putIfAbsent(id, coll);
    }

    /**
     * Remove the request entry associated with the given ID
     * 
     * @param id
     *            the id of the <tt>RequestEntry</tt> to remove
     */
    private void removeEntry(long id)
    {
        Long id_obj = new Long(id);

        // changed by bela Feb 28 2003 (bug fix for 690606)
        // changed back to use synchronization by bela June 27 2003 (bug fix for
        // #761804),
        // we can do this because we now copy for iteration (viewChange() and
        // suspect())
        requests.remove(id_obj);
    }

    public void done(long id)
    {

        removeEntry(id);

    }

    protected abstract BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg, RPCAddress address);

    /**
     * Send a request to a group. If no response collector is given, no
     * responses are expected (making the call asynchronous).
     * 
     * @param id
     *            The request ID. Must be unique for this JVM (e.g. current time
     *            in millisecs)
     * @param dest_mbrs
     *            The list of members who should receive the call. Usually a
     *            group RPC is sent via multicast, but a receiver drops the
     *            request if its own address is not in this list. Will not be
     *            used if it is null.
     * @param msg
     *            The request to be sent. The body of the message carries the
     *            request data
     * 
     * @param coll
     *            A response collector (usually the object that invokes this
     *            method). Its methods <code>receiveResponse()</code> and
     *            <code>suspect()</code> will be invoked when a message has been
     *            received or a member is suspected, respectively.
     */
    public void sendRequest(long id, List<RPCAddress> dest_mbrs, RPCMessage msg, ResponseCollector coll)
            throws Exception
    {
        Header hdr;

        // i. Create the request correlator header and add it to the
        // msg
        // ii. If a reply is expected (sync call / 'coll != null'), add a
        // coresponding entry in the pending requests table
        // iii. If deadlock detection is enabled, set/update the call stack
        // iv. Pass the msg down to the protocol layer below
        hdr = new Header(Header.REQ, id, (coll != null), getName());
        // hdr.dest_mbrs = dest_mbrs;

        if (coll != null)
        {
            addEntry(hdr.getId(), coll);
        }
        msg.putHeader(getName(), hdr);

        if (dest_mbrs.size() > 1)
        {
            for (Iterator<RPCAddress> it = dest_mbrs.iterator(); it.hasNext();)
            {
                final RPCAddress mbr = it.next();
                final RPCMessage copy = msg.copy(true);
                final RemoteException e = new RemoteException();
                this.getReqestThreadpool().execute(new java.lang.Runnable()
                {
                    public void run()
                    {
                        copy.setDest(mbr);
                        try
                        {
                            BaseFutureCall future = buildBaseFutureCall(copy, mbr);
                            // if (asyn_request)
                            // {
                            // FutureTask<RPCMessage> fr = new
                            // FutureTask<RPCMessage>(future);
                            // new Thread(fr).start();
                            //                            
                            // RPCMessage ret = fr.get();
                            // //同步调用系统（webservice，rmi，ejb等）一般会直接返回调用结果，异步（mina，jms）系统将返回空值，
                            // //因此异步调用协议（mina，jms）情况下无需直接调用handler.messageReceived(ret);进行消息处理
                            //                                
                            // if(ret !=
                            // null)//mina协议处理时返回null值,webservice返回其调用结果
                            // messageReceived(ret);
                            // }
                            // else
                            {
                                RPCMessage ret = future.call();
                                // 同步调用系统（webservice，rmi，ejb等）一般会直接返回调用结果，异步（mina，jms）系统将返回空值，
                                // 因此异步调用协议（mina，jms）情况下无需直接调用handler.messageReceived(ret);进行消息处理

                                if (ret != null)// mina协议处理时返回null值,webservice返回其调用结果
                                    messageReceived(ret);
                            }
                        }
                        catch (RemoteException e_)
                        {
                            try
                            {
                            	e.setErrorcode(e_.getErrorcode());
                            	e.setMessage(e_.getRPCMessage());
                            	e.initCause(e_.getCause());
                                exceptionReceived(e);
                            }
                            catch (Exception e1)
                            {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                           
                        }
                        catch (RuntimeException e_)
                        {
                            try
                            {
                            	
                            	e.setMessage(copy);
                            	e.initCause(e_);
                                exceptionReceived(e);
                            }
                            catch (Exception e1)
                            {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                        catch (Exception e_)
                        {
                            try
                            {
                            	e.setMessage(copy);
                            	e.initCause(e_);
                                exceptionReceived(e);
                            }
                            catch (Exception e1)
                            {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        else
        {
            final RPCAddress mbr = dest_mbrs.get(0);
            final RPCMessage copy = msg;
//            System.out.println(mbr);
            final RemoteException e = new RemoteException();
//e.printStackTrace();
            this.getReqestThreadpool().execute(new java.lang.Runnable()
            {
                public void run()
                {

                    copy.setDest(mbr);
                    try
                    {
                        BaseFutureCall future = buildBaseFutureCall(copy, mbr);
                        // if (asyn_request)
                        // {
                        // FutureTask<RPCMessage> fr = new
                        // FutureTask<RPCMessage>(future);
                        // new Thread(fr).start();
                        // RPCMessage ret = fr.get();
                        // //同步调用系统（webservice，rmi，ejb等）一般会直接返回调用结果，异步（mina，jms）系统将返回空值，
                        // //因此异步调用协议（mina，jms）情况下无需直接调用handler.messageReceived(ret);进行消息处理
                        //
                        // if (ret != null)//mina协议处理时返回null值,webservice返回其调用结果
                        // messageReceived(ret);
                        // }
                        // else
                        {
                            RPCMessage ret = future.call();
                            // 同步调用系统（webservice，rmi，ejb等）一般会直接返回调用结果，异步（mina，jms）系统将返回空值，
                            // 因此异步调用协议（mina，jms）情况下无需直接调用handler.messageReceived(ret);进行消息处理

                            if (ret != null)// mina协议处理时返回null值,webservice返回其调用结果
                                messageReceived(ret);
                        }
                    }
                    catch (RemoteException e_)
                    {
                        try
                        {
                        	e.setMessage(e_.getRPCMessage());
                        	e.setErrorcode(e_.getErrorcode());
                        	e.initCause(e_.getCause());
                            exceptionReceived(e);
                        }
                        catch (Exception e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                       
                    }
                    catch (RuntimeException e_)
                    {
                        try
                        {
                        	e.setMessage(copy);
                        	e.initCause(e_);
                            exceptionReceived(e);
                        }
                        catch (Exception e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    catch (Exception e_)
                    {
                        try
                        {
                        	e.setMessage(copy);
                        	e.initCause(e_);
                            exceptionReceived(e);
                        }
                        catch (Exception e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            });
            // }
            // else
            // {
            // future.call();
            // }
            // FutureTask<RPCMessage> fr=new FutureTask<RPCMessage>(new
            // FutureCall(copy,mbr,this));
            // new Thread(fr).start();
            // ClinentTransport transport =
            // ClinentTransport.getClinentTransport(mbr, this);
            // if (!MinaRPCServer.defaultlongconnection)
            // coll.registConnection(transport);
            // transport.write(copy);
            /**
             * webservice call
             */

        }

    }

    protected void assertMessage(Object message) throws IllegalMessage
    {
        if (message instanceof RPCMessage || message instanceof String)
        {

        }
        else throw new IllegalMessage(message.toString());
    }

    /**
     * 服务器端接收到请求后，对请求进行处理，将处理的结果返回给客服端
     * 
     * @param req
     * @param hdr
     * @return
     */
    protected RPCMessage handleRequest(RPCMessage req, Header hdr)
    {
        Object retval;
        Object rsp_buf; // either byte[] or Buffer
        Header rsp_hdr;
        RPCMessage rsp = null;

        // i. Get the request correlator header from the msg and pass it to
        // the registered handler
        //
        // ii. If a reply is expected, pack the return value from the request
        // handler to a reply msg and send it back. The reply msg has the same
        // ID as the request and the name of the sender request correlator

        // if(log.isTraceEnabled()) {
        // log.trace(new StringBuilder("calling (").append((request_handler !=
        // null? request_handler.getClass().getName() : "null")).
        // append(") with request ").append(hdr.id));
        // }

        try
        {

            retval = request_handler.handle(req);
        }
        catch (Throwable t)
        {
            // if(log.isErrorEnabled()) log.error("error invoking method", t);
            retval = t;
        }

        if (!hdr.isRsp_expected()) // asynchronous call, we don't need to send a
            // response; terminate call here
            return null;
        //
        // if(transport == null) {
        // if(log.isErrorEnabled())
        // log.error("failure sending response; no transport available");
        // return;
        // }
        if(!useOOB)
        {
	        // changed (bela Feb 20 2004): catch exception and return exception
	        try
	        { // retval could be an exception, or a real value
	            rsp_buf = Util.objectToByteBuffer(retval);
	        }
	        catch (Throwable t)
	        {
	            try
	            { // this call should succeed (all exceptions are serializable)
	                rsp_buf = Util.objectToByteBuffer(t);
	            }
	            catch (Throwable tt)
	            {
	                // if(log.isErrorEnabled())
	                // log.error("failed sending rsp: return value (" + retval +
	                // ") is not serializable");
	                return null;
	            }
	        }
	
	        rsp = req.makeReply();
	        boolean encrypt = SecurityContext.getSecurityManager().enableEncrypt();
	        rsp.setEncrypt(encrypt);
	        // rsp.setFlag(Message.OOB);
	        if (rsp_buf instanceof Buffer)
	        {
	//            rsp.setBuffer(((Buffer) rsp_buf).getBuf());
	//        	byte[] temp = null;
	//			try {
	//				temp = RequestCorrelator.encrypt((Buffer) rsp_buf);
	//			} catch (Exception e) {
	//				rsp.setEncrypt(false);
	//				temp = ((Buffer) rsp_buf).getBuf();
	//			}
				try {
					rsp.setBuffer(((Buffer) rsp_buf).getBuf());
				} catch (Exception e) {
					rsp.setEncrypt(false);
				}
	        }
	        else if (rsp_buf instanceof byte[])
				try {
					rsp.setBuffer((byte[]) rsp_buf);
				} catch (Exception e) {
					rsp.setEncrypt(false);
				}
        }
        else 
        {
        	  rsp = req.makeReply();
        	rsp.setResultSerial(RPCMessage.OOB);
        	rsp.setData(retval);
        }
        rsp_hdr = new Header(Header.RSP, hdr.getId(), false, name);
        rsp.putHeader(name, rsp_hdr);
        return rsp;

    }

    protected Marshaller2 marshaller = null;

    public class ReceiveFuture implements Callable<RPCMessage>
    {
        RPCMessage message_;

        Header hdr;

        public ReceiveFuture(RPCMessage message_, Header hdr)
        {
            this.message_ = message_;
            this.hdr = hdr;
        }

        public RPCMessage call() throws Exception
        {
            return handleRequest(message_, hdr);
        }

    }

    /**
     * 客服端接收到服务器的响应后，对接收到得结果集进行处理,同时也对请求进行转发
     * 
     * @param message_
     * @throws Exception
     */
    public RPCMessage messageReceived(RPCMessage message_) throws Exception
    {

        Header hdr = message_.getHeader(name);

        switch (hdr.getType())
        {
            case Header.REQ:
                if (request_handler == null)
                {
//                    if (log.isWarnEnabled())
                    {
                        log.warn("there is no request handler installed to deliver request !");
                    }
                    return null;
                }
//                ReceiveFuture future = new ReceiveFuture(message_, hdr);
//                FutureTask<RPCMessage> fr = new FutureTask<RPCMessage>(future);
//                response_threadpool.execute(fr);
                 return this.handleRequest(message_, hdr);
//                return fr.get();

            case Header.RSP:
                message_.getHeader(name);
                ResponseCollector coll = requests.get(Long.valueOf(hdr.getId()));
                if (coll != null)
                {
                    RPCAddress sender = message_.getSrc_addr();
                    Object retval = null;
                    if(message_.getResultSerial() != RPCMessage.OOB)
                    {
                    	
	                    byte[] buf = message_.getBuffer();
	                    int offset = message_.getOffset(), length = message_.getLength();
	                    try
	                    {
	                        retval = marshaller != null ? marshaller.objectFromByteBuffer(buf, offset, length) : Util
	                                .objectFromByteBuffer(buf, offset, length);
	                    }
	                    catch (Exception e)
	                    {
	                        log.error("failed unmarshalling buffer into return value", e);
	                        retval = e;
	                    }
                    }
                    else
                    {
                    	retval = message_.getData();
                    }
                    
                    coll.receiveResponse(retval, sender);
                }
                return null;

            default:
                message_.getHeader(name);
//                if (log.isErrorEnabled())
                    log.error("header's type is neither REQ nor RSP !");
                return null;
        }

    }
    
    
    /**
     * 客服端接收到服务器的响应后，对接收到得结果集进行处理,同时也对请求进行转发
     * 
     * @param message_
     * @throws Exception
     */
    public void exceptionReceived(RemoteException message_) throws Exception
    {

        Header hdr = message_.getRPCMessage().getHeader(name);
        ResponseCollector coll = requests.get(Long.valueOf(hdr.getId()));
        if (coll != null)
        {
            RPCAddress sender = message_.getRPCMessage().getDest();
            Throwable cause = message_.getCause();
            if(cause == null)
            {
                cause =   new ServerUnreachableException(sender.toString());
                cause.initCause(message_);
            }
            coll.receiveResponse(message_, sender);
        }
        

            
        

    }

    public interface Marshaller
    {
        byte[] objectToByteBuffer(Object obj) throws Exception;

        Object objectFromByteBuffer(byte[] buf) throws Exception;
    }

    public interface Marshaller2 extends Marshaller
    {
        /**
         * Marshals the object into a byte[] buffer and returns a Buffer with a
         * ref to the underlying byte[] buffer, offset and length.<br/>
         * <em>
         * Note that the underlying byte[] buffer must not be changed as this would change the buffer of a message which
         * potentially can get retransmitted, and such a retransmission would then carry a ref to a changed byte[] buffer !
             * </em>
         * 
         * @param obj
         * @return
         * @throws Exception
         */
        Buffer objectToBuffer(Object obj) throws Exception;

        Object objectFromByteBuffer(byte[] buf, int offset, int length) throws Exception;
    }

    /**
     * Used to provide a Marshaller2 interface to a Marshaller. This class is
     * for internal use only, and will be removed in 3.0 when Marshaller and
     * Marshaller2 get merged. Do not use, but provide an implementation of
     * Marshaller directly, e.g. in setRequestMarshaller().
     */
    public static class MarshallerAdapter implements Marshaller2
    {
        private final Marshaller marshaller;

        public MarshallerAdapter(Marshaller marshaller)
        {
            this.marshaller = marshaller;
        }

        public byte[] objectToByteBuffer(Object obj) throws Exception
        {
            return marshaller.objectToByteBuffer(obj);
        }

        public Object objectFromByteBuffer(byte[] buf) throws Exception
        {
            return buf == null ? null : marshaller.objectFromByteBuffer(buf);
        }

        public Buffer objectToBuffer(Object obj) throws Exception
        {
            byte[] buf = marshaller.objectToByteBuffer(obj);
            return new Buffer(buf, 0, buf.length);
        }

        public Object objectFromByteBuffer(byte[] buf, int offset, int length) throws Exception
        {
            if (buf == null || (offset == 0 && length == buf.length))
                return marshaller.objectFromByteBuffer(buf);
            byte[] tmp = new byte[length];
            System.arraycopy(buf, offset, tmp, 0, length);
            return marshaller.objectFromByteBuffer(tmp);
        }

    }

}
