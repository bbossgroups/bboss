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
package org.frameworkset.spi;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company:bboss workspace groups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class SPIException extends RuntimeException implements Serializable {
	private SPIException[] es;
    public static void main(String[] args) {

    }

    public SPIException() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public SPIException(SPIException[] es)
    {
    	this.es = es;
    }

    public SPIException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SPIException(String message)
    {
        super(message);
    }

    public SPIException(Throwable message)
    {
        super(message);
    }

	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		if(this.es == null || this.es.length == 0)
			super.printStackTrace();
		else
		{
			for(SPIException e:es)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void printStackTrace(PrintStream s) {
		if(this.es == null || this.es.length == 0)
			super.printStackTrace(s);
		else
		{
			for(SPIException e:es)
			{
				e.printStackTrace(s);
			}
		}
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		if(this.es == null || this.es.length == 0)
			super.printStackTrace(s);
		else
		{
			for(SPIException e:es)
			{
				e.printStackTrace(s);
			}
		}
	}


}
