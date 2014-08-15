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
 * <p>Title: ResponseFilter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-10 下午10:58:00
 * @author biaoping.yin
 * @version 1.0
 */
public interface ResponseFilter
{
	/**
     * Determines whether a response from a given sender should be added to the response list of the request
     * @param response The response (usually a serializable value)
     * @param sender The sender of response
     * @return True if we should add the response to the response list ({@link bboss.org.jgroups.util.RspList}) of a request,
     * otherwise false. In the latter case, we don't add the response to the response list.
     */
    boolean isAcceptable(Object response, RPCAddress sender);

    /**
     * Right after calling {@link #isAcceptable(Object, bboss.org.jgroups.Address)}, this method is called to see whether
     * we are done with the request and can unblock the caller
     * @return False if the request is done, otherwise true
     */
    boolean needMoreResponses();
}
