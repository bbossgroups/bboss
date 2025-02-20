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
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.resource.Resource;
import bboss.org.apache.velocity.util.ExtProperties;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A loader for templates stored on the file system.  Treats the template
 * as relative to the configured root path.  If the root path is empty
 * treats the template name as an absolute path.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:mailmur@yahoo.com">Aki Nieminen</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id$
 */
public class FileResourceLoader extends ResourceLoader
{
    /**
     * The paths to search for templates.
     */
    private List<String> paths = new ArrayList<>();

    /**
     * Used to map the path that a template was found on
     * so that we can properly check the modification
     * times of the files. This is synchronizedMap
     * instance.
     */
    private Map<String, String> templatePaths = Collections.synchronizedMap(new HashMap<>());

    /**
     * @see ResourceLoader#init(bboss.org.apache.velocity.util.ExtProperties)
     */
    @Override
    public void init(ExtProperties configuration)
    {
        log.trace("FileResourceLoader: initialization starting.");

        paths.addAll( configuration.getVector(RuntimeConstants.RESOURCE_LOADER_PATHS) );

        // trim spaces from all paths
        for (ListIterator<String> it = paths.listIterator(); it.hasNext(); )
        {
            String path = StringUtils.trim(it.next());
            it.set(path);
            log.debug("FileResourceLoader: adding path '{}'", path);
        }
        log.trace("FileResourceLoader: initialization complete.");
    }

    /**
     * Get a Reader so that the Runtime can build a
     * template with it.
     *
     * @param templateName name of template to get
     * @return Reader containing the template
     * @throws ResourceNotFoundException if template not found
     *         in the file template path.
     * @since 2.0
     */
    @Override
    public Reader getResourceReader(String templateName, String encoding)
            throws ResourceNotFoundException
    {
        /*
         * Make sure we have a valid templateName.
         */
        if (org.apache.commons.lang3.StringUtils.isEmpty(templateName))
        {
            /*
             * If we don't get a properly formed templateName then
             * there's not much we can do. So we'll forget about
             * trying to search any more paths for the template.
             */
            throw new ResourceNotFoundException(
                    "Need to specify a file name or file path!");
        }

        for (String path : paths)
        {
            InputStream rawStream = null;
            Reader reader = null;

            try
            {
                rawStream = findTemplate(path, templateName);
                if (rawStream != null)
                {
                    reader = buildReader(rawStream, encoding);
                }
            }
            catch (IOException ioe)
            {
                closeQuiet(rawStream);
                String msg = "Exception while loading Template " + templateName;
                log.error(msg, ioe);
                throw new VelocityException(msg, ioe, rsvc.getLogContext().getStackTrace());
            }
            if (reader != null)
            {
                /*
                 * Store the path that this template came
                 * from so that we can check its modification
                 * time.
                 */
                templatePaths.put(templateName, path);
                return reader;
            }
        }

        /*
         * We have now searched all the paths for
         * templates and we didn't find anything so
         * throw an exception.
         */
        throw new ResourceNotFoundException("FileResourceLoader: cannot find " + templateName);
    }

    /**
     * Overrides superclass for better performance.
     * @since 1.6
     */
    @Override
    public boolean resourceExists(String name)
    {
        if (name == null || name.length() == 0)
        {
            return false;
        }

        for (String path : paths)
        {
            try
            {
                File file = getFile(path, name);
                if (file.canRead())
                {
                    return true;
                }
            }
            catch (Exception ioe)
            {
                log.debug("Exception while checking for template {}", name);
            }
        }
        return false;
    }

    /**
     * Try to find a template given a normalized path.
     *
     * @param path a normalized path
     * @param template name of template to find
     * @return InputStream input stream that will be parsed
     *
     */
    private InputStream findTemplate(final String path, final String template)
        throws IOException
    {
        try
        {
            File file = getFile(path, template);

            if (file.canRead())
            {
                FileInputStream fis = null;
                try
                {
                    fis = new FileInputStream(file.getAbsolutePath());
                    return fis;
                }
                catch (IOException e)
                {
                    closeQuiet(fis);
                    throw e;
                }
            }
            else
            {
                return null;
            }
        }
        catch(FileNotFoundException fnfe)
        {
            /*
             *  log and convert to a general Velocity ResourceNotFoundException
             */
            return null;
        }
    }

    private void closeQuiet(final InputStream is)
    {
        if (is != null)
        {
            try
            {
                is.close();
            }
            catch(IOException ioe)
            {
                // Ignore
            }
        }
    }

    /**
     * How to keep track of all the modified times
     * across the paths.  Note that a file might have
     * appeared in a directory which is earlier in the
     * path; so we should search the path and see if
     * the file we find that way is the same as the one
     * that we have cached.
     * @param resource
     * @return True if the source has been modified.
     */
    @Override
    public boolean isSourceModified(Resource resource)
    {
        /*
         * we assume that the file needs to be reloaded;
         * if we find the original file and it's unchanged,
         * then we'll flip this.
         */
        boolean modified = true;

        String fileName = resource.getName();
        String path = templatePaths.get(fileName);
        File currentFile = null;

        for (int i = 0; currentFile == null && i < paths.size(); i++)
        {
            String testPath = (String) paths.get(i);
            File testFile = getFile(testPath, fileName);
            if (testFile.canRead())
            {
                currentFile = testFile;
            }
        }
        File file = getFile(path, fileName);
        if (currentFile == null || !file.exists())
        {
            /*
             * noop: if the file is missing now (either the cached
             * file is gone, or the file can no longer be found)
             * then we leave modified alone (it's set to true); a
             * reload attempt will be done, which will either use
             * a new template or fail with an appropriate message
             * about how the file couldn't be found.
             */
        }
        else if (currentFile.equals(file) && file.canRead())
        {
            /*
             * if only if currentFile is the same as file and
             * file.lastModified() is the same as
             * resource.getLastModified(), then we should use the
             * cached version.
             */
            modified = (file.lastModified() != resource.getLastModified());
        }

        /*
         * rsvc.debug("isSourceModified for " + fileName + ": " + modified);
         */
        return modified;
    }

    /**
     * @see ResourceLoader#getLastModified(bboss.org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public long getLastModified(Resource resource)
    {
        String path = templatePaths.get(resource.getName());
        File file = getFile(path, resource.getName());

        if (file.canRead())
        {
            return file.lastModified();
        }
        else
        {
            return 0;
        }
    }


    /**
     * Create a File based on either a relative path if given, or absolute path otherwise
     */
    private File getFile(String path, String template)
    {

        File file = null;

        if("".equals(path))
        {
            file = new File( template );
        }
        else
        {
            /*
             *  if a / leads off, then just nip that :)
             */
            if (template.startsWith("/"))
            {
                template = template.substring(1);
            }

            file = new File ( path, template );
        }

        return file;
    }
}
