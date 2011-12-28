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

package org.frameworkset.spi.assemble.callback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;


/**
 * <p>Title: ClasspathAssembleCallback.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-4 ÉÏÎç10:52:29
 * @author biaoping.yin
 * @version 1.0
 */
public class ClasspathAssembleCallback implements AssembleCallback {

	private String docbaseType = AssembleCallback.classpathprex;

	public String getDocbasePath(String path) {
		
		return path == null ?"":path;
	}

	public String getDocbaseType() {
		// TODO Auto-generated method stub
		return docbaseType;
	}

	public String getRootPath() {
		// TODO Auto-generated method stub
		return "";
	}
	
	private static File computeApplicationDir(URL location, File defaultDir)
    {
        if (location == null)
        {
            System.out.println("Warning: Cannot locate the program directory. Assuming default.");
            return defaultDir;
        }
        if (!"file".equalsIgnoreCase(location.getProtocol()))
        {
            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(".");
        }
        String file = location.getFile();
        if (!file.endsWith(".jar") && !file.endsWith(".zip"))
        {
            try
            {
                return (new File(URLDecoder.decode(location.getFile(), "UTF-8"))).getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {

            }

            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(location.getFile());
        }
        else
        {
            
            try
            {
                
                return new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Warning: Unrecognized location type. Assuming default.");
        return new File(location.getFile());
    }
	
	private static File computeApplicationClassesDir(URL location, File defaultDir)
    {
        if (location == null)
        {
            System.out.println("Warning: Cannot locate the program directory. Assuming default.");
            return defaultDir;
        }
        if (!"file".equalsIgnoreCase(location.getProtocol()))
        {
            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(".");
        }
        String file = location.getFile();
        if (!file.endsWith(".jar") && !file.endsWith(".zip"))
        {
            try
            {
                return (new File(URLDecoder.decode(location.getFile(), "UTF-8"))).getAbsoluteFile();
            }
            catch (UnsupportedEncodingException e)
            {

            }

            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(location.getFile());
        }
        else
        {
            
            try
            {
                
                return new File(new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile().getAbsolutePath(),"classes");
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Warning: Unrecognized location type. Assuming default.");
        return new File(location.getFile());
    }

}
