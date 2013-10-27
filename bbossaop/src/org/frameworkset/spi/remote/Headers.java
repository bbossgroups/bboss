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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;



/**
 * <p>Title: Headers.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-8 下午10:18:50
 * @author biaoping.yin
 * @version 1.0
 */
public class Headers extends HashMap<String,Header>
{
	
	public void writeTo(DataOutputStream out_) throws IOException
	{
		
		java.io.ObjectOutputStream out = null;
		java.io.ByteArrayOutputStream bout = null;
		try {
			bout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bout);
			
			out.writeObject(this);
			byte[] bytes = bout.toByteArray();
			out_.writeInt(bytes.length);
			out_.write(bytes);
			
		} catch (IOException e) {
			throw e;
		}
		finally
		{
			
			try {
				if (out != null)
					out.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if (bout != null)
					bout.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
	}
	/** Used to store strings and headers, e.g: name-1 | header-1 | name-2 | header-2 | null | null | name-3 | header-3 */
//    private Object[] data;
//
//    public void setData(Object[] data)
//    {
//        this.data = data;
//    }
//
//
//    public Object[] getData()
//    {
//        return data;
//    }


//    /** Add space for 3 new elements when resizing */
//    private static final int RESIZE_INCR=6;

    public Headers() {
        super(3);
    }
    public Headers(int initial_capacity) {
//        data=new Object[initial_capacity << 1];
        super(initial_capacity);
    }

    public Headers(Headers hdrs) {
//        data=new Object[hdrs.data.length];
//        System.arraycopy(hdrs.data, 0, this.data, 0, hdrs.data.length);
        super(hdrs);
    }

//    public Object[] getRawData() {
//        return data;
//    }

    /**
     * Returns the header associated with key
     * @param key
     * @return
     */
    public Header getHeader(String key) {
//        for(int i=0; i < data.length; i+=2) {
//            if(data[i] == null)
//                return null;
//            if(data[i].equals(key))
//                return (Header)data[i+1];
//        }
//        return null;
        return this.get(key);
    }

    public Map<String,Header> getHeaders() {
//        Map<String,Header> retval=new HashMap<String,Header>(data.length / 2);
//        for(int i=0; i < data.length; i+=2) {
//            if(data[i] != null)
//                retval.put((String)data[i], (Header)data[i+1]);
//            else
//                break;
//        }
//        return retval;
        return this;
    }

    public String printHeaders() {
//        StringBuilder sb=new StringBuilder();
//        boolean first=true;
//        for(int i=0; i < data.length; i+=2) {
//            if(data[i] != null) {
//                if(first)
//                    first=false;
//                else
//                    sb.append(", ");
//                sb.append(data[i]).append(": ").append(data[i+1]);
//            }
//            else
//                break;
//        }
        
//        return sb.toString();
        return this.toString();
    }


    /** Puts a header given a key into the hashmap. Overwrites potential existing entry. */
    public void putHeader(String key, Header hdr) {
//        _putHeader(key, hdr, 0, true);
        this.put(key, hdr);
    }




    /**
     * Puts a header given a key into the map, only if the key doesn't exist yet
     * @param key
     * @param hdr
     * @return the previous value associated with the specified key, or
     *         <tt>null</tt> if there was no mapping for the key.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with the key,
     *         if the implementation supports null values.)
     */
    public Header putHeaderIfAbsent(String key, Header hdr) {
//        return _putHeader(key, hdr, 0, false);
        Header retval=this.get(key);
        if(retval == null)
        {
            this.putHeader(key, hdr);
            
        }
        return retval;
    }

//    /**
//     *
//     * @param key
//     * @return the header assoaicted with key
//     * @deprecated Use getHeader() instead. The issue with removing a header is described in
//     * http://jira.jboss.com/jira/browse/JGRP-393
//     */
//    public Header removeHeader(String key) {
//        return getHeader(key);
//    }

//    public Headers copy() {
//        return new Headers(this);
//    }

//    public int marshalledSize() {
//        int retval=0;
//        for(int i=0; i < data.length; i+=2) {
//            if(data[i] != null) {
//                retval+=((String)data[i]).length() +2;
//                retval+=(Global.SHORT_SIZE *2); // 2 for magic number, 2 for size (short)
//                retval+=((Header)data[i+1]).size();
//            }
//            else
//                break;
//        }
//        return retval;
//    }

//    public int size() {
//        int retval=0;
//        for(int i=0; i < data.length; i+=2) {
//            if(data[i] != null)
//                retval++;
//            else
//                break;
//        }
//        return retval;
//    }

//    public int capacity() {
//        return data.length / 2;
//    }

//    public String printObjectHeaders() {
//        StringBuilder sb=new StringBuilder();
//        for(int i=0; i < data.length; i+=2) {
//            if(data[i] != null)
//                sb.append(data[i]).append(": ").append(data[i+1]).append('\n');
//            else
//                break;
//        }
//        return sb.toString();
//    }

//    public String toString() {
//        return printHeaders();
//    }


//    /**
//     * Increases the capacity of the array and copies the contents of the old into the new array
//     */
//    private void resize() {
//        int new_size=data.length + RESIZE_INCR;
//        Object[] new_data=new Object[new_size];
//        System.arraycopy(data, 0, new_data, 0, data.length);
//        data=new_data;
//    }


//    private Header _putHeader(String key, Header hdr, int start_index, boolean replace_if_present) {
//        int i=start_index;
//        while(i < data.length) {
//            if(data[i] == null) {
//                data[i]=key;
//                data[i+1]=hdr;
//                return null;
//            }
//            if(data[i].equals(key)) {
//                Header retval=(Header)data[i+1];
//                if(replace_if_present) {
//                    data[i+1]=hdr;
//                }
//                return retval;
//            }
//            i+=2;
//            if(i >= data.length) {
//                resize();
//            }
//        }
//        throw new IllegalStateException("unable to add element " + key + ", index=" + i); // we should never come here
//    }
    
    
}
