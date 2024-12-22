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

import org.apache.commons.lang3.StringUtils;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.resource.Resource;
import bboss.org.apache.velocity.runtime.resource.util.StringResource;
import bboss.org.apache.velocity.runtime.resource.util.StringResourceRepository;
import bboss.org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl;
import bboss.org.apache.velocity.util.ClassUtils;
import bboss.org.apache.velocity.util.ExtProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Resource loader that works with Strings. Users should manually add
 * resources to the repository that is used by the resource loader instance.
 *
 * Below is an example configuration for this loader.
 * Note that 'repository.class' is not necessary;
 * if not provided, the factory will fall back on using
 * {@link StringResourceRepositoryImpl} as the default.
 * <pre>
 * resource.loaders = string
 * resource.loader.string.description = Velocity StringResource loader
 * resource.loader.string.class = org.apache.velocity.runtime.resource.loader.StringResourceLoader
 * resource.loader.string.repository.name = MyRepositoryName (optional, to avoid using the default repository)
 * resource.loader.string.repository.class = org.apache.velocity.runtime.resource.loader.StringResourceRepositoryImpl
 * </pre>
 * Resources can be added to the repository like this:
 * <pre><code>
 *   StringResourceRepository repo = StringResourceLoader.getRepository();
 *
 *   String myTemplateName = "/some/imaginary/path/hello.vm";
 *   String myTemplate = "Hi, ${username}... this is some template!";
 *   repo.putStringResource(myTemplateName, myTemplate);
 * </code></pre>
 *
 * After this, the templates can be retrieved as usual.
 * <br>
 * <p>If there will be multiple StringResourceLoaders used in an application,
 * you should consider specifying a 'resource.loader.string.repository.name = foo'
 * property in order to keep you string resources in a non-default repository.
 * This can help to avoid conflicts between different frameworks or components
 * that are using StringResourceLoader.
 * You can then retrieve your named repository like this:
 * <pre><code>
 *   StringResourceRepository repo = StringResourceLoader.getRepository("foo");
 * </code></pre>
 * <p>and add string resources to the repo just as in the previous example.
 * </p>
 * <p>If you have concerns about memory leaks or for whatever reason do not wish
 * to have your string repository stored statically as a class member, then you
 * should set 'resource.loader.string.repository.static = false' in your properties.
 * This will tell the resource loader that the string repository should be stored
 * in the Velocity application attributes.  To retrieve the repository, do:</p>
 * <pre><code>
 *   StringResourceRepository repo = velocityEngine.getApplicationAttribute("foo");
 * </code></pre>
 * <p>If you did not specify a name for the repository, then it will be stored under the
 * class name of the repository implementation class (for which the default is
 * 'org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl').
 * Incidentally, this is also true for the default statically stored repository.
 * </p>
 * <p>Whether your repository is stored statically or in Velocity's application
 * attributes, you can also manually create and set it prior to Velocity
 * initialization.  For a static repository, you can do something like this:
 * <pre><code>
 *   StringResourceRepository repo = new MyStringResourceRepository();
 *   repo.magicallyAddSomeStringResources();
 *   StringResourceLoader.setRepository("foo", repo);
 * </code></pre>
 * <p>Or for a non-static repository:</p>
 * <pre><code>
 *   StringResourceRepository repo = new MyStringResourceRepository();
 *   repo.magicallyAddSomeStringResources();
 *   velocityEngine.setApplicationAttribute("foo", repo);
 * </code></pre>
 * <p>Then, assuming the 'resource.loader.string.repository.name' property is
 * set to 'some.name', the StringResourceLoader will use that already created
 * repository, rather than creating a new one.
 * </p>
 *
 * @author <a href="mailto:eelco.hillenius@openedge.nl">Eelco Hillenius</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @author Nathan Bubna
 * @version $Id$
 * @since 1.5
 */
public class StringResourceLoader extends ResourceLoader
{
    /**
     * Key to determine whether the repository should be set as the static one or not.
     * @since 1.6
     */
    public static final String REPOSITORY_STATIC = "repository.static";

    /**
     * By default, repositories are stored statically (shared across the VM).
     * @since 1.6
     */
    public static final boolean REPOSITORY_STATIC_DEFAULT = true;

    /** Key to look up the repository implementation class. */
    public static final String REPOSITORY_CLASS = "repository.class";

    /** The default implementation class. */
    public static final String REPOSITORY_CLASS_DEFAULT =
        StringResourceRepositoryImpl.class.getName();

    /**
     * Key to look up the name for the repository to be used.
     * @since 1.6
     */
    public static final String REPOSITORY_NAME = "repository.name";

    /** The default name for string resource repositories
     * ('org.apache.velocity.runtime.resource.util.StringResourceRepository').
     * @since 1.6
     */
    public static final String REPOSITORY_NAME_DEFAULT =
        StringResourceRepository.class.getName();

    /** Key to look up the repository char encoding. */
    public static final String REPOSITORY_ENCODING = "repository.encoding";

    protected static final Map<String, StringResourceRepository> STATIC_REPOSITORIES =
        Collections.synchronizedMap(new HashMap<>());

    /**
     * Returns a reference to the default static repository.
     * @return default static repository
     */
    public static StringResourceRepository getRepository()
    {
        return getRepository(REPOSITORY_NAME_DEFAULT);
    }

    /**
     * Returns a reference to the repository stored statically under the
     * specified name.
     * @param name
     * @return named repository
     * @since 1.6
     */
    public static StringResourceRepository getRepository(String name)
    {
        return (StringResourceRepository)STATIC_REPOSITORIES.get(name);
    }

    /**
     * Sets the specified {@link StringResourceRepository} in static storage
     * under the specified name.
     * @param name
     * @param repo
     * @since 1.6
     */
    public static void setRepository(String name, StringResourceRepository repo)
    {
        STATIC_REPOSITORIES.put(name, repo);
    }

    /**
     * Removes the {@link StringResourceRepository} stored under the specified
     * name.
     * @param name
     * @return removed repository
     * @since 1.6
     */
    public static StringResourceRepository removeRepository(String name)
    {
        return (StringResourceRepository)STATIC_REPOSITORIES.remove(name);
    }

    /**
     * Removes all statically stored {@link StringResourceRepository}s.
     * @since 1.6
     */
    public static void clearRepositories()
    {
        STATIC_REPOSITORIES.clear();
    }


    /**
     * the repository used internally by this resource loader
     */
    protected StringResourceRepository repository;


    /**
     * @param configuration
     * @see ResourceLoader#init(bboss.org.apache.velocity.util.ExtProperties)
     */
    @Override
    public void init(final ExtProperties configuration)
    {
        log.trace("StringResourceLoader: initialization starting.");

        // get the repository configuration info
        String repoClass = configuration.getString(REPOSITORY_CLASS, REPOSITORY_CLASS_DEFAULT);
        String repoName = configuration.getString(REPOSITORY_NAME, REPOSITORY_NAME_DEFAULT);
        boolean isStatic = configuration.getBoolean(REPOSITORY_STATIC, REPOSITORY_STATIC_DEFAULT);
        String encoding = configuration.getString(REPOSITORY_ENCODING);

        // look for an existing repository of that name and isStatic setting
        if (isStatic)
        {
            this.repository = getRepository(repoName);
            if (repository != null)
            {
                log.debug("Loaded repository '{}' from static repo store", repoName);
            }
        }
        else
        {
            this.repository = (StringResourceRepository)rsvc.getApplicationAttribute(repoName);
            if (repository != null)
            {
                log.debug("Loaded repository '{}' from application attributes", repoName);
            }
        }

        if (this.repository == null)
        {
            // since there's no repository under the repo name, create a new one
            this.repository = createRepository(repoClass, encoding);

            // and store it according to the isStatic setting
            if (isStatic)
            {
                setRepository(repoName, this.repository);
            }
            else
            {
                rsvc.setApplicationAttribute(repoName, this.repository);
            }
        }
        else
        {
            // ok, we already have a repo
            // warn them if they are trying to change the class of the repository
            if (!this.repository.getClass().getName().equals(repoClass))
            {
                log.debug("Cannot change class of string repository '{}' from {} to {}." +
                          " The change will be ignored.",
                          repoName, this.repository.getClass().getName(), repoClass);
            }

            // allow them to change the default encoding of the repo
            if (encoding != null &&
                !this.repository.getEncoding().equals(encoding))
            {
                log.debug("Changing the default encoding of string repository '{}' from {} to {}",
                          repoName, this.repository.getEncoding(), encoding);
                this.repository.setEncoding(encoding);
            }
        }

        log.trace("StringResourceLoader: initialization complete.");
    }

    /**
     * @param className
     * @param encoding
     * @return created repository
     * @since 1.6
     */
    public StringResourceRepository createRepository(final String className,
                                                     final String encoding)
    {
        log.debug("Creating string repository using class {}...", className);

        StringResourceRepository repo;
        try
        {
            repo = (StringResourceRepository) ClassUtils.getNewInstance(className);
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new VelocityException("Could not find '" + className + "'", cnfe);
        }
        catch (IllegalAccessException iae)
        {
            throw new VelocityException("Could not access '" + className + "'", iae);
        }
        catch (InstantiationException ie)
        {
            throw new VelocityException("Could not instantiate '" + className + "'", ie);
        }

        if (encoding != null)
        {
            repo.setEncoding(encoding);
        }
        else
        {
            repo.setEncoding(RuntimeConstants.ENCODING_DEFAULT);
        }

        log.debug("Default repository encoding is {}", repo.getEncoding());
        return repo;
    }

    /**
     * Overrides superclass for better performance.
     * @param name resource name
     * @return whether resource exists
     * @since 1.6
     */
    @Override
    public boolean resourceExists(final String name)
    {
        if (name == null)
        {
            return false;
        }
        return (this.repository.getStringResource(name) != null);
    }

    /**
     * Get a reader so that the Runtime can build a
     * template with it.
     *
     * @param name name of template to get.
     * @param encoding asked encoding
     * @return Reader containing the template.
     * @throws ResourceNotFoundException Ff template not found
     *         in the RepositoryFactory.
     * @since 2.0
     */
    @Override
    public Reader getResourceReader(String name, String encoding)
            throws ResourceNotFoundException
    {
        if (StringUtils.isEmpty(name))
        {
            throw new ResourceNotFoundException("No template name provided");
        }

        StringResource resource = this.repository.getStringResource(name);

        if(resource == null)
        {
            throw new ResourceNotFoundException("Could not locate resource '" + name + "'");
        }

        byte [] byteArray = null;
        InputStream rawStream = null;

        try
        {
            byteArray = resource.getBody().getBytes(resource.getEncoding());
            rawStream = new ByteArrayInputStream(byteArray);
            return new InputStreamReader(rawStream, resource.getEncoding());
        }
        catch(UnsupportedEncodingException ue)
        {
            if (rawStream != null)
            {
                try
                {
                    rawStream.close();
                }
                catch (IOException ioe) {}
            }
            throw new VelocityException("Could not convert String using encoding " + resource.getEncoding(), ue, rsvc.getLogContext().getStackTrace());
        }
    }

    /**
     * @param resource
     * @return whether resource was modified
     * @see ResourceLoader#isSourceModified(bboss.org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public boolean isSourceModified(final Resource resource)
    {
        StringResource original = null;
        boolean result = true;

        original = this.repository.getStringResource(resource.getName());

        if (original != null)
        {
            result =  original.getLastModified() != resource.getLastModified();
        }

        return result;
    }

    /**
     * @param resource
     * @return last modified timestamp
     * @see ResourceLoader#getLastModified(bboss.org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public long getLastModified(final Resource resource)
    {
        StringResource original = null;

        original = this.repository.getStringResource(resource.getName());

        return (original != null)
                ? original.getLastModified()
                : 0;
    }

}

