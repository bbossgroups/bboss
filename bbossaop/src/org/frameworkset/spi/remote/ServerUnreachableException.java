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
 * <p>Title: ServerUnreachableException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-28 下午02:20:52
 * @author biaoping.yin
 * @version 1.0
 */
public class ServerUnreachableException extends RemoteException
{

    public ServerUnreachableException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ServerUnreachableException(RPCMessage message, int errorcode)
    {
        super(message, errorcode);
        // TODO Auto-generated constructor stub
    }

    public ServerUnreachableException(RPCMessage message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ServerUnreachableException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ServerUnreachableException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }
    
    public Throwable getCause()
    {
        return null;
    }

    public ServerUnreachableException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
