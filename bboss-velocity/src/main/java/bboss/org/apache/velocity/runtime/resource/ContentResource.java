package bboss.org.apache.velocity.runtime.resource;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.util.StringBuilderWriter;

import java.io.BufferedReader;
import java.io.Writer;

/**
 * This class represent a general text resource that may have been
 * retrieved from any number of possible sources.
 *
 * Also of interest is Velocity's {@link bboss.org.apache.velocity.Template}
 * <code>Resource</code>.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ContentResource extends Resource
{
    /** Default empty constructor */
    public ContentResource()
    {
        super();

        setType(ResourceManager.RESOURCE_CONTENT);
    }

    /**
     * Pull in static content and store it.
     * @return True if everything went ok.
     *
     * @exception ResourceNotFoundException Resource could not be
     * found.
     */
    @Override
    public boolean process()
        throws ResourceNotFoundException
    {
        BufferedReader reader = null;

        try
        {
            Writer sw = new StringBuilderWriter();

            reader = new BufferedReader(resourceLoader.getResourceReader(name, encoding));

            char buf[] = new char[1024];
            int len = 0;

            while ( ( len = reader.read( buf, 0, 1024 )) != -1)
                sw.write( buf, 0, len );

            setData(sw.toString());

            return true;
        }
        catch ( ResourceNotFoundException e )
        {
            // Tell the ContentManager to continue to look through any
            // remaining configured ResourceLoaders.
            throw e;
        }
        catch ( Exception e )
        {
            String msg = "Cannot process content resource";
            log.error(msg, e);
            throw new VelocityException(msg, e, rsvc.getLogContext().getStackTrace());
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }
}
