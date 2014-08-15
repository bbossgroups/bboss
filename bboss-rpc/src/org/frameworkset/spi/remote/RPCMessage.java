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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.spi.security.SecurityContext;

/**
 * <p>
 * Title: RPCMessage.java
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
 * @Date 2009-10-8 下午10:15:31
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCMessage implements Serializable
{
    protected RPCAddress src_addr = null;

    /** All headers are placed here */
    protected Headers headers;
    
    protected Object data;
    
    protected boolean encrypt = false;
    
    protected int resultSerial ;
    
    /**
     * 是否解密标记
     * 该标记只能被getBuffer置为true，而且只能被setbuffer置为false
     * 一旦getBuffer方法被执行，则加密消息被解密
     */
    protected boolean flag = false;

    /** The payload */
    private byte[] buf = null;

    /** The index into the payload (usually 0) */
    protected int offset = 0;

    /**
     * The number of bytes in the buffer (usually buf.length is buf not equal to
     * null).
     */
    protected int length = 0;

    protected RPCAddress dest;

    public RPCAddress getSrc_addr()
    {

        return src_addr;
    }

    public void setSrc_addr(RPCAddress src_addr)
    {

        this.src_addr = src_addr;
    }

    public void setDest(RPCAddress dest)
    {

        this.dest = dest;
    }

    public RPCAddress getDest()
    {

        return dest;
    }

    public static Headers createHeaders(int size)
    {
        return size > 0 ? new Headers(size) : new Headers(3);
    }

    private static Headers createHeaders(Headers m)
    {
        return new Headers(m);
    }

    public RPCMessage(RPCAddress src_addr, RPCAddress dest)
    {
        // RPCAddress temp = this.dest;
        this.dest = dest;
        this.src_addr = src_addr;
        headers = createHeaders(3);
    }

    public Header getHeader(String key)
    {
        return headers.getHeader(key);
    }

    public RPCMessage makeReply()
    {
        return new RPCMessage(this.dest, this.src_addr);
    }

//    public final void setBuffer(Buffer buf)
//    {
//        if (buf != null)
//        {
//            this.buf = buf.getBuf();
//            this.offset = buf.getOffset();
//            this.length = buf.getLength();
//        }
//    }

    /** Returns the offset into the buffer at which the data starts */
    public int getOffset()
    {
        return offset;
    }

    public Headers getHeaders()
    {
        return headers;
    }

    public void setHeaders(Headers headers)
    {
    	if(this.headers != null )
    	{
    		if(headers != null)
    			this.headers .putAll( headers);
    	}
    	else
    	{
    		this.headers = createHeaders(4);
    		if(headers != null)
    		{
	    		
	    		headers.putAll(headers);
    		}
    	}
    	
    }

    /** Returns the number of bytes in the buffer */
    public int getLength()
    {
        return length;
    }

    final public void setBuffer(byte[] b) throws Exception 
    {
        buf = b;
        if (buf != null)
        {
            offset = 0;
            length = buf.length;
          //消息加密
//            this.encrypt = SecurityContext.getSecurityManager().enableEncrypt();
            if(encrypt)
            	buf = SecurityContext.getSecurityManager().encode(buf);
            
            flag = false;
        }
        else
        {
            offset = length = 0;
        }
    }

    /**
     * Puts a header given a key into the hashmap. Overwrites potential existing
     * entry.
     */
    public void putHeader(String key, Header hdr)
    {
        headers.putHeader(key, hdr);
    }

    public RPCMessage copy()
    {
        return copy(true);
    }

    public RPCMessage(boolean create_headers)
    {
        if (create_headers)
            headers = createHeaders(3);
    }

    public RPCMessage()
    {

        headers = createHeaders(3);
    }

    /**
     * Set the internal buffer to point to a subset of a given buffer
     * 
     * @param b
     *            The reference to a given buffer. If null, we'll reset the
     *            buffer to null
     * @param offset
     *            The initial position
     * @param length
     *            The number of bytes
     */
    final public void setBuffer(byte[] b, int offset, int length)
    {
        buf = b;
        if (buf != null)
        {
            if (offset < 0 || offset > buf.length)
                throw new ArrayIndexOutOfBoundsException(offset);
            if ((offset + length) > buf.length)
                throw new ArrayIndexOutOfBoundsException((offset + length));
            this.offset = offset;
            this.length = length;          
        }
        else
        {
            this.offset = this.length = 0;
        }
    }

    /**
     * Create a copy of the message. If offset and length are used (to refer to
     * another buffer), the copy will contain only the subset offset and length
     * point to, copying the subset into the new copy.
     * 
     * @param copy_buffer
     * @return Message with specified data
     */
    public RPCMessage copy(boolean copy_buffer)
    {
        RPCMessage retval = new RPCMessage(false);
        retval.dest = dest;
        retval.src_addr = src_addr;
     
        retval.encrypt = this.encrypt;

        if (copy_buffer )
        {
        	if(buf != null)
        		retval.setBuffer(buf, offset, length);
            retval.data = this.data;
            retval.resultSerial = this.resultSerial;
        }
        retval.flag = flag;
        retval.headers = createHeaders(headers);
        return retval;
    }

    protected Object clone() throws CloneNotSupportedException
    {
        return copy();
    }
    
    /**
     * Returns a copy of the buffer if offset and length are used, otherwise a
     * reference.
     * 
     * @return byte array with a copy of the buffer.
     * @throws Exception 
     */
    final public byte[] getBuffer() throws Exception
    {
        if (buf == null)
            return null;
        if(this.encrypt && !flag)
        {
            buf = SecurityContext.getSecurityManager().decode(buf);
            flag = true;
        }
        if (offset == 0 && length == buf.length)
            return buf;
        else
        {
            byte[] retval = new byte[length];
            System.arraycopy(buf, offset, retval, 0, length);
            return retval;
        }
    }

    public String toString()
    {
        return "src_addr=" + src_addr + ",headers=" + headers;
    }

    protected static final Log log = LogFactory.getLog(RPCMessage.class);

	public static final int OOB = 2;
    
    public void setEncrypt(boolean encrypt)
    {
        this.encrypt = encrypt;
    }
    public boolean encrypt()
    {
        return this.encrypt;
    }
    
    public String getParameter(String name)
    {
    	Header header = this.getHeader(name);
    	if(header != null)
    		return String.valueOf(header.getValue());
    	return null;
    }
    
    public String getParameter(String name,String defaultValue)
    {
    	Header header = this.getHeader(name);
    	if(header != null)
    		return String.valueOf(header.getValue());
    	return defaultValue;
    }

	

	public int getResultSerial() {
		return resultSerial;
	}

	public void setResultSerial(int resultSerial) {
		this.resultSerial = resultSerial;
	}

	public void setData(Object data) {
		this.data = data;
		
	}

	public Object getData() {
		return data;
	}

}
