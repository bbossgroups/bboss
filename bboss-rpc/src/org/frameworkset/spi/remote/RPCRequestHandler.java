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

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.frameworkset.spi.remote.context.RequestContext;
import org.frameworkset.spi.security.SecurityContext;



/**
 * <p>Title: RPCRequestHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-13 下午10:25:20
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCRequestHandler implements RequestHandler
{
	private Object server_obj;
	private Method callMethod ;
	 protected final Logger log=Logger.getLogger(RPCRequestHandler.class);
	public RPCRequestHandler(Object server_obj)
	{
		this.server_obj = server_obj;
		try {
			this.callMethod = server_obj.getClass().getDeclaredMethod("callMethod", new Class[]{RemoteServiceID.class,
			                     String.class,
			                     Object[].class,
			                     Class[].class});
		} catch (SecurityException e) {
			log.error(e.getMessage(),e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(),e);
		}
	}
	/**
     * Message contains MethodCall. Execute it against *this* object and return result.
     * Use MethodCall.invoke() to do this. Return result.
     */
    public Object handle(RPCMessage req) {
        Object      body=null;
        RPCMethodCall  method_call;

        if(server_obj == null) {
           log.error("no method handler is registered. Discarding request.");
            return null;
        }
        if(req.getResultSerial() != RPCMessage.OOB)
        {
	        if(req == null || req.getLength() == 0) {
	           log.error("message or message buffer is null");
	            return null;
	        }
	
	        try {
	            body=Util.objectFromByteBuffer(req.getBuffer(), req.getOffset(), req.getLength());
	                    
	        }
	        catch(Throwable e) {
	           log.error("exception marshalling object", e);
	            return e;
	        }
        }
        else
        {
        	body = req.getData();
        }

        if(!(body instanceof RPCMethodCall)) {
            log.error("message does not contain a MethodCall object");
            
            // create an exception to represent this and return it
            return  new IllegalArgumentException("message does not contain a MethodCall object") ;
        }

        method_call=(RPCMethodCall)body;
        
        
        try {
            if(log.isTraceEnabled())
                log.trace("[sender=" + req.getSrc_addr() + "], method_call: " + method_call);
//            preMethodCall(method_call);
          //UPDATE EXECUTE AUTHENTICATE AND AHUTHORIATE OPERATION
            SecurityContext securityContext = method_call.getSecurityContext(); 
           
            RequestContext context = new RequestContext(securityContext,true);
            ServiceID id = (ServiceID)method_call.getArgs()[0];
            String method = (String)method_call.getArgs()[1];
            Class[] types = (Class[])method_call.getArgs()[3];
            context.preMethodCall( id, method, types, req.getHeaders());
            return method_call.invoke(server_obj,this.callMethod);
        }
        catch(Throwable x) {
            return x;
        }
        finally
        {
        	RequestContext.destoryRequestContext();
        }
    }
	public Method getCallMethod() {
		return callMethod;
	}
    
//    /**
//     * 执行远程调用的准备功能，做认证和鉴权
//     * @param method_call
//     * @throws Throwable
//     */
//    private void preMethodCall(RPCMethodCall method_call) throws Throwable
//    {
//        SecurityContext securityContext = method_call.getSecurityContext();
//        if(SecurityContext.getSecurityManager().checkUser(securityContext))
//        {
//            initPermissionInfo( method_call);
//            if(SecurityContext.getSecurityManager().checkPermission(securityContext))
//                SecurityContext.setSecurityContext(securityContext);
//            else
//            {
//                throw new SecurityException("权限检测失败,当前用户无法执行服务操作：" + securityContext);
//            }
//        }
//        else
//        {
//            throw new SecurityException("认证失败,请检查用户凭证信息是否正确：" + securityContext);
//        }
//    }
//    
//    private void initPermissionInfo(RPCMethodCall method_call)
//    {
//        SecurityContext securityContext = method_call.getSecurityContext();
//        
//        ServiceID id = (ServiceID)method_call.getArgs()[0];
//        securityContext.setServiceid(id.getService());
//        String method = (String)method_call.getArgs()[1];
//        Class[] types = (Class[])method_call.getArgs()[3];
//        securityContext.setServiceid(id.getService());
//        securityContext.setMethodop(SynchronizedMethod.buildMethodUUID(method,types));
//        
//        
//    }
}
