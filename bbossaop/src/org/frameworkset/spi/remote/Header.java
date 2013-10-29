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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * <p>
 * Title: Header.java
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
 * @Date 2009-10-8 下午10:19:44
 * @author biaoping.yin
 * @version 1.0
 */
public class Header implements Externalizable
{

	public static final int		HDR_OVERHEAD	= 100;	// estimated size of a
														// header (used to
														// estimate the size of
														// the entire msg)

	public static final byte	REQ				= 0;

	public static final byte	RSP				= 1;
	
	public static final byte   COMMON             = 2;

	/** Type of header: request or reply */
	private byte					type			= REQ;
	
	
    /**
     * The id of this request to distinguish among other requests from
     * the same <tt>RequestCorrelator</tt> */
	private long id=0;

    /** msg is synchronous if true */
	private boolean rsp_expected=true;

    /** The unique name of the associated <tt>RequestCorrelator</tt> */
	private String corrName=null;
	private Object value;

//	public List<RPCAddress>	dest_mbrs;
//
//	public void setValue(Object value)
//    {
//        this.value = value;
//    }

    public Header()
	{

	}
	
	public Header(String name,Object value)
    {
	       this.corrName = name;
	       this.value = value;
	       this.type = COMMON;
    }
	/**
     */
    public String toString() {
        StringBuilder ret=new StringBuilder();
        ret.append("[name=" + corrName + ", type=");
        ret.append(type == REQ ? "REQ" : type == RSP ? "RSP" : "<unknown>");
        ret.append(", id=" + id);
        ret.append(", rsp_expected=" + rsp_expected );
        ret.append(",value=" + value+ ']');
//        if(dest_mbrs != null)
//            ret.append(", dest_mbrs=").append(dest_mbrs);
        return ret.toString();
    }
	
	/**
     * @param type type of header (<tt>REQ</tt>/<tt>RSP</tt>)
     * @param id id of this header relative to ids of other requests
     * originating from the same correlator
     * @param rsp_expected whether it's a sync or async request
     * @param name the name of the <tt>RequestCorrelator</tt> from which
     */
    public Header(byte type, long id, boolean rsp_expected, String name) {
        this.type         = type;
        this.id           = id;
        this.rsp_expected = rsp_expected;
        this.corrName     = name;
    }
    
    public String getName()
    {
        return this.corrName;
    }
    
    

	/**
	 * To be implemented by subclasses. Return the size of this object for the
	 * serialized version of it. I.e. how many bytes this object takes when
	 * flattened into a buffer. This may be different for each instance, or can
	 * be the same. This may also just be an estimation. E.g. FRAG uses it on
	 * Message to determine whether or not to fragment the message.
	 * Fragmentation itself will be accurate, because the entire message will
	 * actually be serialized into a byte buffer, so we can determine the exact
	 * size.
	 */
	public int size()
	{

		return HDR_OVERHEAD;
	}

	// public void writeTo(DataOutputStream out) throws IOException {
	// ;
	// }
	//
	// public void readFrom(DataInputStream in) throws IOException,
	// IllegalAccessException, InstantiationException {
	// ;
	// }
//
//	public String toString()
//	{
//
//		return '[' + getClass().getName() + " Header]";
//	}

	 public void writeExternal(ObjectOutput out) throws IOException {
         out.writeByte(type);
         out.writeLong(id);
         out.writeBoolean(rsp_expected);
         if(corrName != null) {
             out.writeBoolean(true);
             out.writeUTF(corrName);
         }
         else {
             out.writeBoolean(false);
         }
         if(this.value != null)
         {
             out.writeBoolean(true);
             out.writeObject(this.value);
         }
         else
         {
             out.writeBoolean(false);
         }
//         out.writeObject(dest_mbrs);
     }


     public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
         type         = in.readByte();
         id           = in.readLong();
         rsp_expected = in.readBoolean();
         if(in.readBoolean())
             corrName         = in.readUTF();
         if(in.readBoolean())
             this.value = in.readObject();
//         dest_mbrs=(java.util.List<RPCAddress>)in.readObject();
     }
     
     public Object getValue()
     {
         return this.value;
     }

	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the rsp_expected
	 */
	public boolean isRsp_expected() {
		return rsp_expected;
	}

	/**
	 * @param rspExpected the rsp_expected to set
	 */
	public void setRsp_expected(boolean rspExpected) {
		rsp_expected = rspExpected;
	}

	/**
	 * @return the corrName
	 */
	public String getCorrName() {
		return corrName;
	}

	/**
	 * @param corrName the corrName to set
	 */
	public void setCorrName(String corrName) {
		this.corrName = corrName;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
}
