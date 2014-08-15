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
 * 
 * <p>Title: RemoteException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Apr 24, 2009 10:48:49 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class RemoteException extends RuntimeException {
    private RPCMessage message;
    private int errorcode = -1;

    public RemoteException() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public RemoteException(RPCMessage message,int errorcode) {
        this.message = message;
        this.errorcode = errorcode;
        // TODO Auto-generated constructor stub
    }
    
    public RemoteException(RPCMessage message,Throwable cause) {
        super(cause);
        this.message = message;
        
        // TODO Auto-generated constructor stub
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public RemoteException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public RemoteException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public RPCMessage getRPCMessage()
    {
        return message;
    }

    public int getErrorcode()
    {
        return errorcode;
    }

	

	/**
	 * @param message the message to set
	 */
	public void setMessage(RPCMessage message) {
		this.message = message;
	}

	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
    
//    public Throwable getCause()
//    {
//        if(super.getCause() != null)
//            return super.getCause();
//        if(message != null)
//            return new ServerUnreachableException(message.getDest().toString());
//        else
//        {
//            return new ServerUnreachableException(this.errorcode + "");
//        }
//        
//    }

}
