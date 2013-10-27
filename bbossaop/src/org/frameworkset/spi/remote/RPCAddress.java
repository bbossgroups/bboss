package org.frameworkset.spi.remote;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.serviceidentity.TargetImpl;

/**
 * 
 * <p>
 * Title: RPCAddress.java
 * </p>
 * <p>
 * Description: 提供远程服务的地址信息，包括ip和端口
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-8-31 上午09:43:25
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCAddress implements Externalizable, Comparable<RPCAddress>, Cloneable
{
    private String ip;
    
    /**
     * webservice协议地址上下文
     */
    private String webserviceurl = null;   
    
    /**
     * jms服务器地址标识
     */
    private String server_uuid = null;
    

    
	public String getServer_uuid()
	{
	
		return server_uuid;
	}

	private transient InetAddress ipAddress;
    
    /**
     * 对应的webservice地址上下文
     */
    private String contextpath;

    private boolean security = false;
	
	public boolean isSecurity()
	{	
		return security;
	}


	
	public void setSecurity(boolean security)
	{	
		this.security = security;
	}


	public String getContextpath()
	{
	
		return contextpath;
	}

	
	public void setContextpath(String contextpath)
	{
		if(contextpath != null)
		{
			if(protocol != null && this.protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
			{
				if(!contextpath.trim().equals("") && !contextpath.endsWith("/"))
					this.contextpath = contextpath + "/";
				else
				{
					this.contextpath = contextpath;
				}
			}
			else
			{
				this.contextpath = contextpath;
			}
		}
	}

	/**
     * 远程地址对应的通讯协议：mina，jgroup，jms，webservice，rmi等等
     */
    private String protocol;

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }



    public String getProtocol()
    {
        return protocol;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    private int port;

    private Object origineAddress = null;

    public Object getOrigineAddress()
    {
        return origineAddress;
    }

    public RPCAddress(String ip, int port)
    {
        this(ip, port, null, null);
    }
    
    public RPCAddress(String server_uuid)
    {
        this.server_uuid = server_uuid;
        this.protocol = Target.BROADCAST_TYPE_JMS;
    }
    
    
    public RPCAddress(String server_uuid,String protocol)
    {
        this.server_uuid = server_uuid;
        this.protocol = protocol;
        
        
    }
    
    public RPCAddress(String server_uuid,Object origineAddress,String protocol)
    {
        this.server_uuid = server_uuid;
        this.protocol = protocol;
        this.origineAddress = origineAddress;
        
        
    }

    public RPCAddress(String ip, int port, Object origineAddress)
    {
        this(ip, port, origineAddress, null);
        // this.ip = ip;
        // this.port = port;
        // this.origineAddress = origineAddress;
    }
    
    public RPCAddress(String protocol,String ip, int port, String contextpath)
    {
        this(ip, port, null, protocol);
        // this.ip = ip;
        // this.port = port;
        // this.origineAddress = origineAddress;
        this.setContextpath(contextpath);
    }

    public RPCAddress(String ip, int port, Object origineAddress, String protocol)
    {
        this.ip = ip;
        this.port = port;
        this.origineAddress = origineAddress;
        this.protocol = protocol == null || protocol.trim().equals("") ? Util.default_protocol : protocol;
    }
    
    public RPCAddress(String ip, int port,  String protocol)
    {
        this.ip = ip;
        this.port = port;
      
        this.protocol = protocol == null || protocol.trim().equals("") ? Util.default_protocol : protocol;
    }

    public RPCAddress(InetAddress ipAddress, int port)
    {
        // this.ipAddress = ipAddress;
        // this.port = port;
        // this.ip = ipAddress.getHostAddress();
        this(ipAddress, port, null, null);
    }

    public RPCAddress(InetAddress ipAddress, int port, Object origineAddress, String protocol)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.ip = ipAddress.getHostAddress();
        this.origineAddress = origineAddress;
        this.protocol = protocol == null || protocol.trim().equals("") ? Util.default_protocol : protocol;
    }

    public RPCAddress(InetSocketAddress inet)
    {
        this(inet.getAddress(), inet.getPort(), null, null);        
    }

    public RPCAddress()
    {

        
    }

   


	public String getHostName()
    {
        return ip;
    }

    public String getCanonicalHostName()
    {
        return ipAddress != null ? ipAddress.getCanonicalHostName() : null;
    }

    /**
     * Establishes an order between 2 addresses. Assumes other contains non-null
     * IpAddress. Excludes channel_name from comparison.
     * 
     * @return 0 for equality, value less than 0 if smaller, greater than 0 if
     *         greater.
     * @deprecated Use {@link #compareTo(bboss.org.jgroups.Address)} instead
     */
    public final int compare(RPCAddress other)
    {
        return compareTo(other);
    }

    /**
     * implements the java.lang.Comparable interface
     * 
     * @see java.lang.Comparable
     * @param o
     *            - the Object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @exception java.lang.ClassCastException
     *                - if the specified object's type prevents it from being
     *                compared to this Object.
     */
    public final int compareTo(RPCAddress o)
    {

        if (this == o)
            return 0;
        if (!(o instanceof RPCAddress))
            throw new ClassCastException("comparison between different classes: the other object is "
                    + (o != null ? o.getClass() : o));
        RPCAddress other = (RPCAddress) o;
        if(this.protocol .equals(Target.BROADCAST_TYPE_JMS) || this.protocol .equals(Target.BROADCAST_TYPE_JRGOUP ))
        {
        	return this.server_uuid.compareTo(other.server_uuid);
        }
        else  if(this.protocol .equals(Target.BROADCAST_TYPE_REST))
        {
        	return this.server_uuid.compareTo(other.server_uuid);
        }
        
        if (ip != null && other.ip != null)
        {
        	if(this.protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
        	{
        		if((this.isSecurity() && !other.isSecurity()) || (!this.isSecurity() && other.isSecurity()))
        			return -1;
        		if (!ip.equals(other.ip))
                {                    
                        return 1;
                     

                }
        		if(port != other.port)
        			return 1;
        		return this.contextpath.equals(other.contextpath)?0:1;
        		
        	}
            if (ip.equals(other.ip))
            {
                if (port == other.port)
                    return 0;

            }
            
            return 1;
//            return port < other.port ? -1 : (port > other.port ? 1 : 0);

        }
        

        return 1;

    }

    public final boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof RPCAddress))
            return false;

        return compareTo((RPCAddress) obj) == 0;
    }

    public final int hashCode()
    {
    	if(this.protocol.equals(Target.BROADCAST_TYPE_JMS) || this.protocol.equals(Target.BROADCAST_TYPE_REST))
    		return this.server_uuid.hashCode();
    	if(this.protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
    		return this.getWebServiceURL().hashCode();
    	
    		
        return ip != null ? ip.hashCode() + port : port;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(this.protocol.equalsIgnoreCase(Target.BROADCAST_TYPE_JMS) || this.protocol.equalsIgnoreCase(Target.BROADCAST_TYPE_JRGOUP))
        	return protocol + "::"+this.server_uuid;
        if(this.protocol.equalsIgnoreCase(Target.BROADCAST_TYPE_REST))
        	return "rest::"+this.server_uuid;
        
        if (this.origineAddress != null)
            return this.origineAddress.toString();
        if(this.protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
        	return this.getWebServiceURL();
        sb.append(this.protocol).append(":");
        if (ip == null)
            sb.append("<null>");
        else
        {
            sb.append(ip);
//            if (this.ipAddress != null)
//            {
//
//                String host_name = null;
//                boolean resolve_dns = true;
//                if (resolve_dns)
//                {
//                    host_name = ipAddress.getHostName();
//                    // appendShortName(host_name, sb);
//                }
//                else
//                {
//                    host_name = ipAddress.getHostAddress();
//                }
//                sb.append(host_name);
//            }
        }
        sb.append(":").append(port);

        return sb.toString();
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
    	
    		
        if (ip != null)
        {
            // byte[] address=ip_addr.getAddress();
            byte[] address = ip.getBytes();
            out.writeByte(address.length); // 1 byte
            out.write(address, 0, address.length);
        }
        else
        {
            out.writeByte(0);
        }
        out.writeShort(port);
        if (this.origineAddress != null)
        {
            out.writeByte(1);
            out.writeObject(this.origineAddress);
        }
        else
        {
            out.writeByte(0);
        }
        if (this.protocol != null)
        {
            byte[] address = protocol.getBytes();
            out.writeByte(address.length);
            out.write(address, 0, address.length);
        }
        else
        {
            out.writeByte(-1);
        }
        
        if(this.contextpath != null)
        {
        	
        	 byte[] t = contextpath.getBytes();
             out.writeByte(t.length);
             out.write(t, 0, t.length);
        	
        }
        else
        {
        	out.writeByte(-1);
        }
        
        if(this.security)
        {
        	
        	
             out.writeByte(1);
             out.writeBoolean(security);
        	
        }
        
        else
        {
        	out.writeByte(-1);
        }
        if(this.server_uuid != null)
        {
        	 byte[] t = server_uuid.getBytes();
             out.writeByte(t.length);
             out.write(t, 0, t.length);
        }
        else
        {
        	out.writeByte(-1);
        }
        // if(additional_data != null) {
        // out.writeBoolean(true);
        // out.writeShort(additional_data.length);
        // out.write(additional_data, 0, additional_data.length);
        // }
        // else
        // out.writeBoolean(false);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int len = in.readByte();
        if (len > 0)
        {
            // read the four bytes
            byte[] a = new byte[len];
            // in theory readFully(byte[]) should be faster
            // than read(byte[]) since latter reads
            // 4 bytes one at a time
            in.readFully(a);
            // look up an instance in the cache
            this.ip = new String(a);
        }
        // then read the port
        port = in.readUnsignedShort();
        int o = in.readByte();
        if (o == 1)
            this.origineAddress = in.readObject();
        int plen = in.readByte();
        if (plen >= 0)
        {
            byte[] a = new byte[plen];
            in.readFully(a);
            this.protocol = new String(a);
        }
        int clen = in.readByte();
        if(clen > 0)
        {
        	byte[] a = new byte[plen];
            in.readFully(a);
            this.contextpath = new String(a);
//        	this.contextpath = new 
        }
        int sec = in.readByte();
        if(sec > 0)
        	this.security = in.readBoolean();
        
        sec = in.readByte();
        if(sec > 0)
        {
            byte[] a = new byte[sec];
            in.readFully(a);
            this.server_uuid = new String(a);
        }
        

    }

    public Object clone() throws CloneNotSupportedException
    {
        RPCAddress ret = new RPCAddress(ip, port, this.origineAddress, this.protocol);
        ret.ipAddress = this.ipAddress;
        // ret.protocol = this.protocol;

        // if(additional_data != null) {
        // ret.additional_data=new byte[additional_data.length];
        // System.arraycopy(additional_data, 0, ret.additional_data, 0,
        // additional_data.length);
        // }
        return ret;
    }
    
    public String getWebServiceURL()
    {
    	if(this.webserviceurl != null)
    		return this.webserviceurl;
    	if(this.protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
    	{
    		synchronized(this)
    		{
    			if(this.webserviceurl != null)
    	    		return this.webserviceurl;
    			return webserviceurl = TargetImpl.buildWebserviceURL(this);
    		}
    	}
    	return null;
    }
    private Map<String,String> urls = new HashMap<String,String>();
    public String getWebServiceURL(String serverport)
    {
    	String webserviceurl  = urls.get(serverport);
    	if(webserviceurl != null)
    		return webserviceurl;
    	if(this.protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
    	{
    		synchronized(this)
    		{
    			webserviceurl  = urls.get(serverport);
    	    	if(webserviceurl != null)
    	    		return webserviceurl;
    			webserviceurl = TargetImpl.buildWebserviceURL(this,serverport);
    			urls.put(serverport, webserviceurl);
    		}
    	}
    	return webserviceurl;
    }
}
