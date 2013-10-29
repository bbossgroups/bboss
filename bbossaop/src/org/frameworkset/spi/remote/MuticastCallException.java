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
import java.util.List;

/**
 * <p>Title: MuticastCallException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-8 下午04:43:07
 * @author biaoping.yin
 * @version 1.0
 */
public class MuticastCallException extends RuntimeException
{

    private List<Exception> erros = new ArrayList<Exception>();
    public MuticastCallException()
    {
        
    }

    public MuticastCallException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public MuticastCallException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public MuticastCallException(String message, Throwable cause)
    {
        super(message, cause);
        
    }
    
    public void addException(RPCAddress address ,Throwable rsp)
    {
        erros.add(new Exception(address.toString(), rsp));
    }
    
    public void printStackTrace()
    {
        for(Exception e:erros)
        {
            e.printStackTrace();
        }
    }
    
    

}
