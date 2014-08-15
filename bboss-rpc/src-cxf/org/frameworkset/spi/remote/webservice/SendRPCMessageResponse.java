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
package org.frameworkset.spi.remote.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

   
/**
 * 
 * <p>Title: SendRPCMessageResponse.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-11-4 下午05:07:23
 * @author biaoping.yin     
 * @version 1.0  
 */
@XmlRootElement(name = "sendRPCMessageResponse", namespace = "http://webservice.remote.spi.frameworkset.org/")
@XmlAccessorType(XmlAccessType.FIELD)     
@XmlType(name = "sendRPCMessageResponse", namespace = "http://webservice.remote.spi.frameworkset.org/")
public class SendRPCMessageResponse {  

    @XmlElement(name = "return")
    private org.frameworkset.spi.remote.RPCMessage _return;

    public org.frameworkset.spi.remote.RPCMessage getReturn() {
        return this._return;
    }

    public void setReturn(org.frameworkset.spi.remote.RPCMessage new_return)  {
        this._return = new_return;
    }

}

