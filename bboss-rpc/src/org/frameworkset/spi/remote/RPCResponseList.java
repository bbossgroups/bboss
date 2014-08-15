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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * <p>Title: RPCResponseList.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-10 下午10:44:23
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCResponseList implements Map<RPCAddress,RPCResponse> {

    /** Map<Address, Rsp> */
    Map<RPCAddress,RPCResponse> rsps=new HashMap<RPCAddress,RPCResponse>();
    List<RPCResponse> responses = new ArrayList<RPCResponse>();

    public RPCResponseList() {

    }

    /** Adds a list of responses
     * @param responses Collection<Rsp>
     */
    public RPCResponseList(Map<RPCAddress,RPCResponse> rsps) {
        if(rsps != null) {
        	this.rsps = new HashMap<RPCAddress,RPCResponse>();
        	this.rsps.putAll(rsps) ;
        	rsps.clear();
        	rsps = null;
        	this.responses.addAll(this.rsps.values());
//            for(RPCResponse rsp: responses) {
//                rsps.put(rsp.getSender(), rsp);
//            }
//        	this.rsps = rsps;
        }
        
    }
    
    /** Adds a list of responses
     * @param responses Collection<Rsp>
     */
    public RPCResponseList(List<RPCResponse> responses) {
        if(responses != null) {
        	
        	this.responses = responses;
            for(RPCResponse rsp: responses) {
                rsps.put(rsp.getSender(), rsp);
            }
//        	this.rsps = rsps;
        }
        
    }
    
    /** Adds a list of responses
     * @param responses Collection<Rsp>
     */
    public RPCResponseList(List<RPCResponse> responses,Map<RPCAddress,RPCResponse> rsps) {
        if(responses != null) {
        	
        	this.responses = responses;
//            for(RPCResponse rsp: responses) {
//                rsps.put(rsp.getSender(), rsp);
//            }
        	this.rsps = rsps;
        }
        
    }
  
    


    public boolean isEmpty() {
        return rsps.isEmpty();
    }

    public boolean containsKey(Object key) {
        return rsps.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return rsps.containsValue(value);
    }

    /**
     * Returns the Rsp associated with address key
     * @param key Address (key)
     * @return Rsp
     */
    public RPCResponse get(Object key) {
        return rsps.get(key);
    }

    /**
     * Returns the value associated with address key
     * @param key
     * @return Object value
     */
    public Object getValue(Object key) {
    	RPCResponse rsp=get(key);
        return rsp != null? rsp.getValue() : null;
    }
    
    /**
     * Returns the value associated with address key
     * @param key
     * @return Object value
     */
    public Object getValue(int key) {
    	
        return this.responses.get(key).getValue();
    }

    public RPCResponse put(RPCAddress key, RPCResponse value) {
        return rsps.put(key, value);
    }

    public RPCResponse remove(Object key) {
        return rsps.remove(key);
    }

    public void putAll(Map<? extends RPCAddress, ? extends RPCResponse> m) {
        rsps.putAll(m);
    }

    public void clear() {
        rsps.clear();
    }

    public Set<RPCAddress> keySet() {
        return rsps.keySet();
    }

    public Collection<RPCResponse> values() {
        return rsps.values();
    }

    public Set<Map.Entry<RPCAddress,RPCResponse>> entrySet() {
        return rsps.entrySet();
    }

    /**
     * Clears the response list
     * @deprecated Use {@link #clear()} instead
     */
    public void reset() {
        clear();
    }


    public void addRsp(RPCAddress sender, Object retval) {
    	RPCResponse rsp=get(sender);
        if(rsp != null) {
            rsp.sender=sender;
            rsp.retval=retval;
            rsp.received=true;
            rsp.suspected=false;
            return;
        }
        rsps.put(sender, new RPCResponse(sender, retval));
    }


    public void addNotReceived(RPCAddress sender) {
    	RPCResponse rsp=get(sender);
        if(rsp == null)
            rsps.put(sender, new RPCResponse(sender));
    }


    public void addSuspect(RPCAddress sender) {
    	RPCResponse rsp=get(sender);
        if(rsp != null) {
            rsp.sender=sender;
            rsp.retval=null;
            rsp.received=false;
            rsp.suspected=true;
            return;
        }
        rsps.put(sender, new RPCResponse(sender, true));
    }


    public boolean isReceived(RPCAddress sender) {
    	RPCResponse rsp=get(sender);
        return rsp != null && rsp.received;
    }

    public int numSuspectedMembers() {
        int num=0;
        Collection<RPCResponse> values=values();
        for(RPCResponse rsp: values) {
            if(rsp.wasSuspected())
                num++;
        }
        return num;
    }

    public int numReceived() {
        int num=0;
        Collection<RPCResponse> values=values();
        for(RPCResponse rsp: values) {
            if(rsp.wasReceived())
                num++;
        }
        return num;
    }

    /** Returns the first value in the response set. This is random, but we try to return a non-null value first */
    public Object getFirst() {
        Collection<RPCResponse> values=values();
        for(RPCResponse rsp: values) {
            if(rsp.getValue() != null)
                return rsp.getValue();
        }
        return null;
    }


    /**
     * Returns the results from non-suspected members that are not null.
     */
    public List<Object> getResults() {
    	List<Object> ret=new ArrayList<Object>();
        Object val;

        for(RPCResponse rsp: values()) {
            if(rsp.wasReceived() && (val=rsp.getValue()) != null)
                ret.add(val);
        }
        return ret;
    }


    public List<RPCAddress> getSuspectedMembers() {
    	List<RPCAddress> retval=new ArrayList<RPCAddress>();
        for(RPCResponse rsp: values()) {
            if(rsp.wasSuspected())
                retval.add(rsp.getSender());
        }
        return retval;
    }


    public boolean isSuspected(RPCAddress sender) {
    	RPCResponse rsp=get(sender);
        return rsp != null && rsp.suspected;
    }


    public int size() {
    	
        return rsps.size();
    }

    /**
     * Returns the Rsp at index i
     * @param i The index
     * @return a Rsp
     * @throws ArrayIndexOutOfBoundsException
     * @deprecated Use {@link #entrySet()} or {@link #values()} instead
     */
    public Object elementAt(int i) throws ArrayIndexOutOfBoundsException {
        Set<RPCAddress> keys=new TreeSet<RPCAddress>(keySet());
        Object[] keys_array=keys.toArray();
        Object key=keys_array[i];
        return get(key);
    }


    public String toString() {
        StringBuilder ret=new StringBuilder();
        for(RPCResponse rsp: values()) {
            ret.append("[" + rsp + "]\n");
        }
        return ret.toString();
    }


    boolean contains(RPCAddress sender) {
        return containsKey(sender);
    }


}

