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



import java.util.List;

/**
 * <p>Title: RPCIOHandle.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-30 下午03:24:49
 * @author biaoping.yin
 * @version 1.0
 */
public interface RPCIOHandler
{
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
            throws Exception;
    
    public void done(long id);

    /**
     * @param ret
     */
    public RPCMessage messageReceived(RPCMessage ret)  throws Exception;
    
    /**
     * 获取本地地址
     * @return
     */
    public RPCAddress getLocalAddress();
}
