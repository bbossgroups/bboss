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

package org.frameworkset.spi.serviceidentity;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.frameworkset.netty.NettyRPCServer;
import org.frameworkset.spi.remote.JGroupHelper;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RemoteException;
import org.frameworkset.spi.remote.RemoteServiceID;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.frameworkset.spi.remote.http.HttpServer;
import org.frameworkset.spi.remote.mina.server.MinaRPCServer;
import org.frameworkset.spi.remote.restful.RestfulServiceManager;
import org.frameworkset.spi.remote.rmi.RMIServer;

import bboss.org.jgroups.Address;

/**
 * <p>Title: ServiceIDImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-11 上午10:37:09
 * @author biaoping.yin
 * @version 1.0
 */
public class ServiceIDImpl extends BaseServiceIDImpl implements RemoteServiceID{
	private static Logger log = Logger.getLogger(ServiceIDImpl.class);
	protected transient Target target;
	protected Class<?> infType;
	/**
	 * serviceID: (ip:port;ip:port)/serviceid
	 */
	protected String urlParams;
	/**
	 * 多个远程调用时，返回的结果集类型 result_object = 0; result_first = 1; result_map = 2;
	 * result_rsplist = 3; result_list = 4;
	 */
	protected transient int resultType = result_rsplist;

	protected transient int resultMode = 2;// GET_ALL
	protected String sourceport;

	protected String sourcename;

	protected String sourceip;
	protected boolean restStyle = false;
	protected transient RemoteServiceID restfulServiceID;
	protected String nextRestNode;
	protected String fistRestNode;
	protected String serviceUUID;
	

	protected transient long timeout = 60 * 1000;
    public static final boolean evaluatelocaladdress = Util.defaultContext.getBooleanProperty("rpc.evaluatelocaladdress", false);
    public Target getRestfulTarget() {
		return ((RemoteServiceID)this.restfulServiceID).getTarget();
	}
    public Target getTarget() {
		return this.target;
	}
	private boolean rpcserviceStarted(Target target)
    {
        if(target == null)
            return false;
        try
        
        {
            if (target.protocol_jgroup())
            {
                boolean started = JGroupHelper.getJGroupHelper().clusterstarted();
                if (!started)
                    throw new RemoteException("远程管理组件没有正常启动。无法调用远程服务[" + serviceID + "]");
                return started;
            }
            else if (target.protocol_mina())
            {
                return MinaRPCServer.getMinaRPCServer().started();
            }

            else if (target.protocol_netty())
            {
                return NettyRPCServer.getNettyRPCServer().started();
            }
            else if (target.protocol_rmi())
            {
                return RMIServer.getRMIServer().started();
            }
            else if (target.protocol_http())
            {
                return HttpServer.getHttpServer().started();
            }
            else if (target.protocol_jms())
            {
                return true;
            }
            else if (target.protocol_webservice())
            {
                return true;
            }
            else if(target.protocol_rest())
            {
            	return true;
            }
            else
            {
                throw new RuntimeException("未支持的地址：" + target);
            }
        }
        catch (RemoteException e)
        {
            throw e;
        }
        catch (RuntimeException e)
        {
            throw e;
        }

        catch (Exception e)
        {
            log.error("判断 rpcservice(" + target + ")是否启动失败：", e);
            return false;
        }

    }
	
	private boolean computeAgain()
	{
	    if(this.nextRestNode.equals(TargetImpl.REST_LOCAL))
        {
            isremote = false;
            return true;
        }
        else
        {
            String[] nodes = TargetImpl.parserRestFulPath(nextRestNode);
            this.fistRestNode = nodes[0];
            this.nextRestNode = nodes[1];
            this.restfulServiceID = RestfulServiceManager.getRestfulServiceManager().convert(this,this.applicationContext);                        
            return this.isLocalAddress(this.getRestfulTarget(), true);
        }
	}

    private boolean isLocalAddress(Target target,boolean fromrest)
    {
    	if(true)
    		return false;
    	if(!evaluatelocaladdress)
    		return false;
        if(target == null)
            return false;
        
        if (target.protocol_jgroup())
        {
            Address address = (Address) JGroupHelper.getJGroupHelper().getLocalAddress();
            if (this.compared(address, target))
            {
                if(fromrest)//restful风格地址处理
                {
                    //继续计算下个地址
//                    if(this.nextRestNode.equals(Target.REST_LOCAL))
//                    {
//                        isremote = false;
//                        return true;
//                    }
//                    else
//                    {
//                        String[] nodes = Target.parserRestFulPath(nextRestNode);
//                        this.fistRestNode = nodes[0];
//                        this.nextRestNode = nodes[1];
//                        this.rest = RestfulServiceManager.getRestfulServiceManager().convert(this);                        
//                        return this.isLocalAddress(this.getRestfulTarget(), true);
//                    }
                    return computeAgain();
                }
                else
                {
                    isremote = false;
                    return true;
                }
                
            }
            return false;
        }
        else if (target.protocol_mina())
        {
//            if (MinaRPCServer.getMinaRPCServer().started())
            
            if (compared(Util.getRPCIOHandler(Target.BROADCAST_TYPE_MINA).getLocalAddress(), target))
            {
                if(fromrest)//restful风格地址处理
                {
                    //继续计算下个地址
//                    if(this.nextRestNode.equals(Target.REST_LOCAL))
//                    {
//                        isremote = false;
//                        return true;
//                    }
//                    else
//                    {
//                        String[] nodes = Target.parserRestFulPath(nextRestNode);
//                        this.fistRestNode = nodes[0];
//                        this.nextRestNode = nodes[1];
//                        this.rest = RestfulServiceManager.getRestfulServiceManager().convert(this);                        
//                        return this.isLocalAddress(this.getRestfulTarget(), true);
//                    }
                    return computeAgain();
                }
                else
                {
                    isremote = false;
                    return true;
                }
            }
            
            return false;
        }
        else if (target.protocol_netty())
        {
//            if (MinaRPCServer.getMinaRPCServer().started())
            
            if (compared(Util.getRPCIOHandler(Target.BROADCAST_TYPE_NETTY).getLocalAddress(), target))
            {
                if(fromrest)//restful风格地址处理
                {
                    //继续计算下个地址
//                    if(this.nextRestNode.equals(Target.REST_LOCAL))
//                    {
//                        isremote = false;
//                        return true;
//                    }
//                    else
//                    {
//                        String[] nodes = Target.parserRestFulPath(nextRestNode);
//                        this.fistRestNode = nodes[0];
//                        this.nextRestNode = nodes[1];
//                        this.rest = RestfulServiceManager.getRestfulServiceManager().convert(this);                        
//                        return this.isLocalAddress(this.getRestfulTarget(), true);
//                    }
                    return computeAgain();
                }
                else
                {
                    isremote = false;
                    return true;
                }
            }
            
            return false;
        }
        else if (target.protocol_jms())
        {
            if (this.compared(Util.getRPCIOHandler(Target.BROADCAST_TYPE_JMS).getLocalAddress(), target))
            {
                if(fromrest)//restful风格地址处理
                {
                    //继续计算下个地址
//                    if(this.nextRestNode.equals(Target.REST_LOCAL))
//                    {
//                        isremote = false;
//                        return true;
//                    }
//                    else
//                    {
//                        String[] nodes = Target.parserRestFulPath(nextRestNode);
//                        this.fistRestNode = nodes[0];
//                        this.nextRestNode = nodes[1];
//                        this.rest = RestfulServiceManager.getRestfulServiceManager().convert(this);                        
//                        return this.isLocalAddress(this.getRestfulTarget(), true);
//                    }
                    return computeAgain();
                }
                else
                {
                    isremote = false;
                    return true;
                }
            }
            
            return false;
//            return this.compared(Util.getRPCIOHandler(Target.BROADCAST_TYPE_JMS).getLocalAddress(), target);
        }
        else if (target.protocol_webservice())
        {
            if (this.compared(Util.getRPCIOHandler(Target.BROADCAST_TYPE_WEBSERVICE).getLocalAddress(), target))
            {
                if(fromrest)//restful风格地址处理
                {
                    //继续计算下个地址
//                    if(this.nextRestNode.equals(Target.REST_LOCAL))
//                    {
//                        isremote = false;
//                        return true;
//                    }
//                    else
//                    {
//                        String[] nodes = Target.parserRestFulPath(nextRestNode);
//                        this.fistRestNode = nodes[0];
//                        this.nextRestNode = nodes[1];
//                        this.rest = RestfulServiceManager.getRestfulServiceManager().convert(this);                        
//                        return this.isLocalAddress(this.getRestfulTarget(), true);
//                    }
                    return computeAgain();
                }
                else
                {
                    isremote = false;
                    return true;
                }
            }
            
            return false;
//            return this.compared(Util.getRPCIOHandler(Target.BROADCAST_TYPE_WEBSERVICE).getLocalAddress(), target);
        }
        else if (target.protocol_ejb())
        {
            return false;
        }
        else if (target.protocol_rmi())
        {
            return false;
        }
        else if (target.protocol_http())
        {
            return false;
        }
        
        else if (target.protocol_rest()) //Fixed local address
        {
            return this.isLocalAddress(this.getRestfulTarget(),true);
        }
        else
        {
            throw new RuntimeException("未支持协议的地址：" + target);
        }
    }

//    public ServiceIDImpl(String serviceID,  int resultMode, long timeout, int resultType, int bean_type,BaseApplicationContext applicationcontext)
//    {
//        this(serviceID, null, resultMode, timeout, resultType, bean_type,applicationcontext);
//    }
    
//    public ServiceIDImpl(String serviceID, String providerID, int resultMode, long timeout, int resultType, int bean_type,BaseApplicationContext applicationcontext)
//    {
////    	super(serviceID, providerID, resultMode, timeout, resultType, bean_type,(BaseApplicationContext)applicationcontext);
//    	super( serviceID,  providerID, applicationcontext,  bean_type);    	
//		this.resultMode = resultMode;
//		this.timeout = timeout;
//		this.resultType = resultType;
//        this.buildService();
//        isremote = this.target != null && !this.target.isSelf();
//        if (this.isremote)
//        {
////            rpcserviceStarted(target);
//
//            // IpAddress address =
//            // (IpAddress)JGroupHelper.getJGroupHelper().getLocalAddress();
//            // if(this.compared(address, target))
//            // {
//            // isremote = false;
//            // return;
//            // }
//            if (isLocalAddress(target,false))
//            {
////                if(serviceID.equals("(jms::yinbiaoping-jms)/rpc.test"))
////                    isremote = true;
//                return;
//            }
//            setLocalAddress();
//        }
//
//    }
    
    
    public ServiceIDImpl(String serviceID, String providerID,String applicationcontext,int containerType, int resultMode, long timeout, int resultType, int bean_type)
    {
//    	super(serviceID, providerID,(String)applicationcontext, containerType, resultMode, timeout, resultType, bean_type);
    	super( serviceID,  providerID, applicationcontext, containerType,  bean_type);
		this.resultMode = resultMode;
		this.timeout = timeout;
		this.resultType = resultType;
        this.buildService();
        isremote = this.target != null && !this.target.isSelf();
        if (this.isremote)
        {
//            rpcserviceStarted(target);

            // IpAddress address =
            // (IpAddress)JGroupHelper.getJGroupHelper().getLocalAddress();
            // if(this.compared(address, target))
            // {
            // isremote = false;
            // return;
            // }
            if (isLocalAddress(target,false))
            {
//                if(serviceID.equals("(jms::yinbiaoping-jms)/rpc.test"))
//                    isremote = true;
                return;
            }
            try {
				setLocalAddress();
			} catch (Exception e) {
				log.info(e.getMessage());
			}
        }

    }

    private void setLocalAddress()
    {
    	if(true)
    		return;
        if(target == null)
            return;
        if (target.protocol_jgroup())
        {
            Address address = (Address) JGroupHelper.getJGroupHelper().getLocalAddress();
//            this.sourceip = address.getIpAddress().getHostAddress();
//            this.sourceport = address.getPort() + "";
//            this.sourcename = address.getIpAddress().getHostName();
        }
        else if (target.protocol_mina() )
        {
            RPCAddress address = MinaRPCServer.getMinaRPCServer().getLocalAddress();
            if (address != null)
            {
                this.sourceip = address.getIp();
                this.sourceport = address.getPort() + "";
                // this.sourcename = address.getIpAddress().getHostName();
                this.sourcename = sourceip;
            }
        }
        else if (target.protocol_netty())
        {
            RPCAddress address = NettyRPCServer.getNettyRPCServer().getLocalAddress();
            if (address != null)
            {
                this.sourceip = address.getIp();
                this.sourceport = address.getPort() + "";
                // this.sourcename = address.getIpAddress().getHostName();
                this.sourcename = sourceip;
            }
        }
        else if (target.protocol_jms())
        {
            // return false;
        }
        
        else if (target.protocol_rmi())
        {
        	RPCAddress address = RMIServer.getRMIServer().getLocalAddress();
            if (address != null)
            {
                this.sourceip = address.getIp();
                this.sourceport = address.getPort() + "";
                // this.sourcename = address.getIpAddress().getHostName();
                this.sourcename = sourceip;
            }
        }
        else if (target.protocol_http())
        {
        	RPCAddress address = HttpServer.getHttpServer().getLocalAddress();
            if (address != null)
            {
                this.sourceip = address.getIp();
                this.sourceport = address.getPort() + "";
                // this.sourcename = address.getIpAddress().getHostName();
                this.sourcename = sourceip;
            }
        }
        else if (target.protocol_webservice())
        {
            // return false;
        }
        else if (target.protocol_rest())
        {
            // return false;
        }
        else
        {
            throw new RuntimeException("未支持协议的地址：" + target);
        }

    }

//    public ServiceIDImpl(String serviceID, int resultMode, int waittime, int resultType, int bean_type,BaseApplicationContext applicationcontext)
//    {
//        this(serviceID, null, resultMode, waittime, resultType, bean_type, applicationcontext);
//
//    }

    public boolean compared(Address address, Target target)
    {

        if (target.getTargets().size() == 1)
        {
        	RPCAddress t = target.getTargets().get(0);
        	Address address_ = (Address)t.getOrigineAddress();
        	
        	if(address_ == null)
        	{
        		String uuid = t.getServer_uuid();
        		return address.toString().equals(uuid);
        	}
        	else
        	{
        		return address_.compareTo(address) == 0;
        	}
        }
        return false;
    }

    public boolean compared(RPCAddress address, Target target)
    {

        if (target.getTargets().size() == 1)
        {
            RPCAddress address_ = (RPCAddress) target.getTargets().get(0);
            return address_.compare(address) == 0;
        }
        return false;
    }
    
   
    private void buildService()
    {
        StringTokenizer tokenizer = new StringTokenizer(this.serviceID, ")", false);
        int size = tokenizer.countTokens();
        if (size == 1)
        {
            this.service = tokenizer.nextToken();
        }
        else if(size == 2)
        {
            String target_ = tokenizer.nextToken();
            target_ = target_.substring(1, target_.length());
            target = new TargetImpl(target_);
            

            {
	            String temp = tokenizer.nextToken().substring(1);	            
	            int paramidx = temp.indexOf("?");
	            if(paramidx > -1)
	            {
	                urlParams = temp.substring(paramidx + 1);
	                this.service = temp.substring(0,paramidx);
	            }
	            else
	                this.service = temp;
            }
            if(target.protocol_rest()){
            	this.setRestStyle(target.protocol_rest());
            	this.fistRestNode = this.target.getFirstNode();
                this.nextRestNode = this.target.getNextNode();
            	this.restfulServiceID = RestfulServiceManager.getRestfulServiceManager().convert(this,this.applicationContext);
            	
            }
        }
       
    }

    public static void main(String[] args)
    {
    	//默认的远程服务调用标识
        String serviceid = "(17:1010;18:1020)/serviceid";
        
        //webservice远程服务调用标识
        serviceid = "(webservice::http://17:1010/webroot/;http://17:1010/webroot/)/serviceid";

//        serviceid = "(all)/serviceid";
//        ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
//        System.out.println(id);

    }
    
    
    
    public String getNextRestfulServiceAddress(){
    	if(getNextRestNode().equals("_local_"))
    		return this.getService();
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("(" ).append(Target.BROADCAST_TYPE_REST ).append( "::" ).append( this.getNextRestNode() ).append( ")/" ).append( this.getService());
    	return buffer.toString();
    }

	public ServiceIDImpl()
    {
    	
    }
	public long getTimeout() {
		return timeout;
	}

	public boolean isRestStyle() {
		return this.restStyle;
	}

	public void setRestStyle(boolean restStyle) {
		this.restStyle = restStyle;
	}

	public String getSourceport() {
		return sourceport;
	}

	public String getSourcename() {
		return sourcename;
	}


	
	public int getResultMode() {
		return resultMode;
	}

	public int getResultType() {
		return resultType;
	}
	public void setSourceip(String sourceip) {
		this.sourceip = sourceip;
	}

	public void setSourceport(String sourceport) {
		this.sourceport = sourceport;
	}

	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	/**
	 * @fixed biaoping.yin 2010-10-11
	 * @return the rest
	 */
	

	public String getFistRestNode() {
		return fistRestNode;
	}

	public void setFistRestNode(String fistRestNode) {
		this.fistRestNode = fistRestNode;
	}

	public String getNextRestNode() {
		return nextRestNode;
	}

	public void setNextRestNode(String nextRestNode) {
		this.nextRestNode = nextRestNode;
	}

	public RemoteServiceID getRestfulServiceID() {
		return this.restfulServiceID;
	}

	
	public String getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
//		if(this.urlParams == null)
//			this.urlParams = urlParams;
//		else
//			this.urlParams = this.urlParams + "&"+urlParams;
//		if(restfulServiceID != null)
//			this.restfulServiceID.setUrlParams(urlParams);
	}
	public String getSourceip() {
		return sourceip;
	}
	public Class<?> getInfType() {
		return infType;
	}
	public void setInfType(Class<?> infType) {
		this.infType = infType;
		if(restfulServiceID != null)
			this.restfulServiceID.setInfType(infType);
	}
//	@Override
//	public void apendUrlParams(RemoteServiceID restid) {
//		if(this.urlParams != null )
//		{
//			if( restid.getUrlParams() != null)
//			{
//				this.urlParams = this.urlParams +"&"+ restid.getUrlParams();
//				restid.setUrlParams(this.urlParams);
//			}
//			else
//			{
//				this.urlParams = restid.getUrlParams();
//			}
//		}
//		else
//			this.urlParams = restid.getUrlParams();
//		
//	}
	
}
