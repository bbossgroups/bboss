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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.spi.remote.annotations.GuardedBy;

/**
 * <p>
 * Title: RPCRequest.java
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
 * @Date 2009-10-7 下午05:35:32
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCRequest implements java.io.Serializable, ResponseCollector
{
    private static final Log log = LogFactory.getLog(RPCRequest.class);

    /** return only first response */
    public static final int GET_FIRST = 1;

    /** return all responses */
    public static final int GET_ALL = 2;

    /** return majority (of all non-faulty members) */
    public static final int GET_MAJORITY = 3;

    /** return majority (of all members, may block) */
    public static final int GET_ABS_MAJORITY = 4;

    /** return n responses (may block) */
    public static final int GET_N = 5;

    /** return no response (async call) */
    public static final int GET_NONE = 6;

    private long id;

    /** to generate unique request IDs (see getRequestId()) */
    private static long last_req_id = 1;

    protected final int rsp_mode;

    protected final int expected_mbrs;

    private RPCAddress caller;

    /** Map<Address, Rsp>. Maps requests and responses */
    @GuardedBy("lock")
    private final Map<RPCAddress, RPCResponse> requests = new HashMap<RPCAddress, RPCResponse>();

    /** bounded queue of suspected members */
    @GuardedBy("lock")
    private final List<RPCAddress> suspects = new ArrayList<RPCAddress>();

    RPCMessage msg;

    RPCIOHandler corr;

    long timeout;

    protected volatile boolean done = false;

    /** list of members, changed by viewChange() */
    @GuardedBy("lock")
    private final List<RPCAddress> members = new ArrayList<RPCAddress>();

    public RPCRequest(RPCMessage msg, RPCIOHandler corr, List<RPCAddress> mbrs, int rsp_mode, long timeout,
            int expected_mbrs)
    {

        this.msg = msg;
        this.rsp_mode = rsp_mode;
        this.timeout = timeout;
        this.id = getRequestId();
        this.corr = corr;
        this.expected_mbrs = expected_mbrs;
        if (mbrs != null)
        {
            for (RPCAddress mbr : mbrs)
            {
                requests.put(mbr, new RPCResponse(mbr));
            }
            this.members.clear();
            this.members.addAll(mbrs);
        }

    }

    // public RPCRequest()
    // {
    // this.id = getRequestId();
    // }
    /** Generates a new unique request ID */
    private static synchronized long getRequestId()
    {

        long result = System.currentTimeMillis();
        if (result <= last_req_id)
        {
            result = last_req_id + 1;
        }
        last_req_id = result;
        return result;
    }

    public long getId()
    {

        // TODO Auto-generated method stub
        return id;
    }

    public void setResponseFilter(ResponseFilter filter)
    {
        rsp_filter = filter;
    }

    protected ResponseFilter rsp_filter = null;

    public void receiveResponse(Object response_value, RPCAddress sender)
    {

        lock.lock();
        try
        {
            if (done)
            {
                // if(log.isTraceEnabled())
                // log.trace("command is done; cannot add response !");
            }
//            else if (suspects.contains(sender))
//            {
//                // if(log.isWarnEnabled())
//                // log.warn("received response from suspected member " + sender
//                // + "; discarding");
//            }
            else
            {
                // try
                // {
                // ClinentTransport transport = this.addresss.remove(sender);
                // if(transport != null)
                // transport.disconnect();
                // }
                // catch (Exception e)
                // {
                // log.error("transport.disconnect()", e);
                // }
                try
                {
                    RPCResponse rsp = requests.get(sender);
                    if (rsp != null)
                    {
                        if (!rsp.wasReceived())
                        {
                            boolean responseReceived = (rsp_filter == null)
                                    || rsp_filter.isAcceptable(response_value, sender);
                            rsp.setValue(response_value);
                            rsp.setReceived(responseReceived);
                            if (log.isTraceEnabled())
                                log.trace(new StringBuilder("received response for request ").append(id)
                                        .append(", sender=").append(sender).append(", val=").append(response_value));
                        }
                        done = rsp_filter != null && !rsp_filter.needMoreResponses();
    
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        finally
        {
            completed.signalAll(); // wakes up execute()
            lock.unlock();
        }

    }

    /** keep suspects vector bounded */
    private static final int MAX_SUSPECTS = 40;

//    /**
//     * Adds a member to the 'suspects' list. Removes oldest elements from
//     * 'suspects' list to keep the list bounded ('max_suspects' number of
//     * elements), Requires lock to be held
//     */
//    @GuardedBy("lock")
//    private void addSuspect(RPCAddress suspected_mbr)
//    {
//        if (!suspects.contains(suspected_mbr))
//        {
//            suspects.add(suspected_mbr);
//            while (suspects.size() >= MAX_SUSPECTS && !suspects.isEmpty())
//                suspects.remove(0); // keeps queue bounded
//        }
//    }

//    public void suspect(RPCAddress suspected_member)
//    {
//
//        if (suspected_member == null)
//            return;
//
//        lock.lock();
//        try
//        {
//            addSuspect(suspected_member);
//            RPCResponse rsp = requests.get(suspected_member);
//            if (rsp != null)
//            {
//                rsp.setSuspected(true);
//                rsp.setValue(null);
//                completed.signalAll();
//            }
//        }
//        finally
//        {
//            lock.unlock();
//        }
//
//    }

//    public void viewChange(View new_view)
//    {
//
//        // TODO Auto-generated method stub
//
//    }

    private final Lock lock = new ReentrantLock();

    /** Is set as soon as the request has received all required responses */
    private final Condition completed = lock.newCondition();

    public RPCResponseList getResults()
    {

        lock.lock();
        try
        {
//            List<RPCResponse> rsps = requests.values();

            return new RPCResponseList(requests);
        }
        finally
        {
            // this.closeShortConnection();
            lock.unlock();
        }
    }
    
    

    public void setCaller(RPCAddress local_addr)
    {
        this.caller = local_addr;
        // TODO Auto-generated method stub

    }

    public boolean execute() throws Exception
    {
        this.corr.sendRequest(this.id, members, this.msg, this);
        lock.lock();
        try
        {
            done = false;
            boolean retval = collectResponses(timeout);
            // if(retval == false && log.isTraceEnabled())
            // log.trace("call did not execute correctly, request is " +
            // this.toString());
            return retval;
        }
        finally
        {
            done = true;
            lock.unlock();
        }
    }

    // /**
    // * 关闭短连接
    // */
    // private void closeShortConnection()
    // {
    // Collection<ClinentTransport> ss = this.addresss.values();
    // if(ss != null)
    // {
    // for(Iterator<ClinentTransport> ts = ss.iterator();ts.hasNext();)
    // {
    // ClinentTransport t = ts.next();
    // t.disconnect();
    // }
    // }
    //		
    // }

    @GuardedBy("lock")
    private boolean collectResponses(long timeout) throws Exception
    {
        if (timeout <= 0)
        {
            while (true)
            { /* Wait for responses: */
                // adjustMembership(); // may not be necessary, just to make
                // sure...
                if (responsesComplete())
                {
                    if (corr != null)
                    {
                        corr.done(id);
                    }
                    // if(log.isTraceEnabled() && rsp_mode != GET_NONE) {
                    // log.trace("received all responses: " + toString());
                    // }
                    return true;
                }
                try
                {
                    completed.await();
                    // System.out.println("");
                }
                catch (Exception e)
                {
                }
            }
        }
        else
        {
            long start_time = System.currentTimeMillis();
            long timeout_time = start_time + timeout;
            while (timeout > 0)
            { /* Wait for responses: */
                if (responsesComplete())
                {
                    if (corr != null)
                        corr.done(id);
                    // if(log.isTraceEnabled() && rsp_mode != GET_NONE) {
                    // log.trace("received all responses: " + toString());
                    // }
                    return true;
                }
                timeout = timeout_time - System.currentTimeMillis();
                if (timeout > 0)
                {
                    try
                    {
                        completed.await(timeout, TimeUnit.MILLISECONDS);
                        // System.out.println("");
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            if (corr != null)
            {
                corr.done(id);
            }
            // if(log.isTraceEnabled())
            // log.trace("timed out waiting for responses");

            return false;
        }
    }

    @GuardedBy("lock")
    private boolean responsesComplete()
    {
        int num_received = 0, num_not_received = 0, num_suspected = 0;
        final int num_total = requests.size();

        if (done)
            return true;

        for (RPCResponse rsp : requests.values())
        {
            if (rsp.wasReceived())
            {
                num_received++;
            }
            else
            {
                if (rsp.wasSuspected())
                {
                    num_suspected++;
                }
                else
                {
                    num_not_received++;
                }
            }
        }

        switch (rsp_mode)
        {
        case GET_FIRST:
            if (num_received > 0)
                return true;
            if (num_suspected >= num_total)
                // e.g. 2 members, and both suspected
                return true;
            break;
        case GET_ALL:
            return num_received + num_suspected >= num_total;
        case GET_MAJORITY:
            int majority = determineMajority(num_total);
            if (num_received + num_suspected >= majority)
                return true;
            break;
        case GET_ABS_MAJORITY:
            majority = determineMajority(num_total);
            if (num_received >= majority)
                return true;
            break;
        case GET_N:
            if (expected_mbrs >= num_total)
            {
                return responsesComplete();
            }
            return num_received >= expected_mbrs || num_received + num_not_received < expected_mbrs
                    && num_received + num_suspected >= expected_mbrs;
        case GET_NONE:
            return true;
        default:
            // if(log.isErrorEnabled()) log.error("rsp_mode " + rsp_mode +
            // " unknown !");
            break;
        }
        return false;
    }

    private static int determineMajority(int i)
    {
        return i < 2 ? i : (i / 2) + 1;
    }

    // private transient Map<RPCAddress,ClinentTransport> addresss= new
    // HashMap<RPCAddress,ClinentTransport>();
    // public void registConnection(ClinentTransport transport)
    // {
    // addresss.put(transport.getRpcaddress(), transport);
    // // TODO Auto-generated method stub
    //		
    // }

    public String toString()
    {
        return msg.toString();
    }
}
