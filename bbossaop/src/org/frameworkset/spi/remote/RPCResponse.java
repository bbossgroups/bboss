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



/**
 * <p>Title: RPCResponse.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-7 下午05:35:47
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCResponse implements java.io.Serializable
{
	/* flag that represents whether the response was received */
    boolean received=false;

    /* flag that represents whether the response was suspected */
    boolean suspected=false;

    /* The sender of this response */
    RPCAddress sender=null;

    /* the value from the response */
    Object retval=null;
    

    public RPCResponse(RPCAddress sender) {
        this.sender=sender;
    }

    public RPCResponse(RPCAddress sender, boolean suspected) {
        this.sender=sender;
        this.suspected=suspected;
    }

    public RPCResponse(RPCAddress sender, Object retval) {
        this.sender=sender;
        this.retval=retval;
        received=true;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof RPCResponse))
            return false;
        RPCResponse other=(RPCResponse)obj;
        if(sender != null)
            return sender.equals(other.sender);
        return other.sender == null;
    }

    public Object getValue() {
        return retval;
    }

    public void setValue(Object val) {
        this.retval=val;
    }

    public RPCAddress getSender() {
        return sender;
    }

    public boolean wasReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received=received;
        if(received)
            suspected=false;
    }

    public boolean wasSuspected() {
        return suspected;
    }

    public void setSuspected(boolean suspected) {
        this.suspected=suspected;
        if(suspected)
            received=false;
    }

    public String toString() {
        return new StringBuilder("sender=").append(sender).append(", retval=").append(retval).append(", received=").
                append(received).append(", suspected=").append(suspected).toString();
    }

}
