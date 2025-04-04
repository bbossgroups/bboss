package bboss.org.apache.velocity.runtime.resource.loader;

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
import bboss.org.apache.velocity.runtime.RuntimeServices;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A small wrapper around a Jar
 *
 * @author <a href="mailto:daveb@miceda-data.com">Dave Bryson</a>
 * @version $Id$
 */
public class JarHolder
{
    private String urlpath = null;
    private JarFile theJar = null;
    private JarURLConnection conn = null;

    private Logger log = null;

    /**
     * @param rs
     * @param urlpath
     * @param log
     */
    public JarHolder( RuntimeServices rs, String urlpath, Logger log )
    {
        this.log = log;

        this.urlpath=urlpath;
        init();

        log.debug("JarHolder: initialized JAR: {}", urlpath);
    }

    /**
     *
     */
    public void init()
    {
        try
        {
            log.debug("JarHolder: attempting to connect to {}", urlpath);

            URL url = new URL( urlpath );
            conn = (JarURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();
            theJar = conn.getJarFile();
        }
        catch (IOException ioe)
        {
            String msg = "JarHolder: error establishing connection to JAR at \""
                         + urlpath + "\"";
            log.error(msg, ioe);
            throw new VelocityException(msg, ioe);
        }
    }

    /**
     *
     */
    public void close()
    {
        try
        {
            theJar.close();
        }
        catch ( Exception e )
        {
            String msg = "JarHolder: error closing the JAR file";
            log.error(msg, e);
            throw new VelocityException(msg, e);
        }
        theJar = null;
        conn = null;

        log.trace("JarHolder: JAR file closed");
    }

    /**
     * @param theentry
     * @return The requested resource.
     * @throws ResourceNotFoundException
     */
    public InputStream getResource( String theentry )
     throws ResourceNotFoundException {
        InputStream data = null;

        try
        {
            JarEntry entry = theJar.getJarEntry( theentry );

            if ( entry != null )
            {
                data =  theJar.getInputStream( entry );
            }
        }
        catch(Exception fnfe)
        {
            log.error("JarHolder: getResource() error", fnfe);
            throw new ResourceNotFoundException(fnfe);
        }

        return data;
    }

    /**
     * @return The entries of the jar as a hashtable.
     */
    public Map<String, String> getEntries()
    {
        Map<String, String> allEntries = new HashMap<>(559);

        Enumeration<JarEntry> all  = theJar.entries();
        while ( all.hasMoreElements() )
        {
            JarEntry je = all.nextElement();

            // We don't map plain directory entries
            if ( !je.isDirectory() )
            {
                allEntries.put( je.getName(), this.urlpath );
            }
        }
        return allEntries;
    }

    /**
     * @return The URL path of this jar holder.
     */
    public String getUrlPath()
    {
        return urlpath;
    }
}
