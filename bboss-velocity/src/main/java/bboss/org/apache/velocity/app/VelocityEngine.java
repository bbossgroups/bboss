package bboss.org.apache.velocity.app;

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

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.context.Context;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.RuntimeInstance;
import bboss.org.apache.velocity.runtime.resource.Resource;
import org.slf4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * <p>
 * This class provides a separate new-able instance of the
 * Velocity template engine.  The alternative model for use
 * is using the Velocity class which employs the singleton
 * model.
 * </p>
 * <p>Velocity will call
 * the parameter-less init() at the first use of this class
 * if the init() wasn't explicitly called.  While this will
 * ensure that Velocity functions, it probably won't
 * function in the way you intend, so it is strongly recommended that
 * you call an init() method yourself if you use the default constructor.
 * </p>
 *
 * @version $Id$
 */
public class VelocityEngine implements RuntimeConstants
{
    private RuntimeInstance ri = new RuntimeInstance();

    /**
     *  Init-less CTOR
     */
    public VelocityEngine()
    {
        // do nothing
    }

    /**
     * Construct a VelocityEngine with the initial properties defined in the file
     * propsFilename
     * @param propsFilename properties filename
     */
    public VelocityEngine(String propsFilename)
    {
        ri.setProperties(propsFilename);
    }

    /**
     * Construct a VelocityEngine instance with the specified initial properties.
     * @param p properties
     */
    public VelocityEngine(Properties p)
    {
        ri.setProperties(p);
    }

    /**
     *  initialize the Velocity runtime engine, using the default
     *  properties of the Velocity distribution
     */
    public void init()
    {
        ri.init();
    }

    /**
     * Resets the instance, so Velocity can be re-initialized again.
     *
     * @since 2.0.0
     */
    public void reset()
    {
        ri.reset();
    }

    /**
     *  initialize the Velocity runtime engine, using default properties
     *  plus the properties in the properties file passed in as the arg
     *
     *  @param propsFilename file containing properties to use to initialize
     *         the Velocity runtime
     */
    public void init(String propsFilename)
    {
        ri.init(propsFilename);
    }

    /**
     *  initialize the Velocity runtime engine, using default properties
     *  plus the properties in the passed in java.util.Properties object
     *
     *  @param p  Properties object containing initialization properties
     */
    public void init(Properties p)
    {
        ri.init(p);
    }

    /**
     * Set a Velocity Runtime property.
     *
     * @param  key
     * @param  value
     */
    public void setProperty(String key, Object value)
    {
        ri.setProperty(key, value);
    }

    /**
     * Add a Velocity Runtime property.
     *
     * @param  key
     * @param  value
     */
    public void addProperty(String key, Object value)
    {
        ri.addProperty(key, value);
    }

    /**
     * Clear a Velocity Runtime property.
     *
     * @param key of property to clear
     */
    public void clearProperty(String key)
    {
        ri.clearProperty(key);
    }

    /**
     * Set an entire configuration at once from a Properties configuration
     *
     * @param  configuration
     * @since 2.0
     */
    public void setProperties(Properties configuration)
    {
        ri.setProperties( configuration );
    }

    /**
     * Set an entire configuration at once from a named properties file
     *
     * @param  propsFilename properties filename
     * @since 2.1
     */
    public void setProperties(String propsFilename)
    {
        ri.setProperties(propsFilename);
    }

    /**
     *  Get a Velocity Runtime property.
     *
     *  @param key property to retrieve
     *  @return property value or null if the property
     *        not currently set
     */
    public Object getProperty( String key )
    {
        return ri.getProperty( key );
    }

    /**
     *  renders the input string using the context into the output writer.
     *  To be used when a template is dynamically constructed, or want to use
     *  Velocity as a token replacer.
     *
     *  @param context context to use in rendering input string
     *  @param out  Writer in which to render the output
     *  @param logTag  string to be used as the template name for log
     *                 messages in case of error
     *  @param instring input string containing the VTL to be rendered
     *
     *  @return true if successful, false otherwise.  If false, see
     *             Velocity runtime log
     * @throws ParseErrorException The template could not be parsed.
     * @throws MethodInvocationException A method on a context object could not be invoked.
     * @throws ResourceNotFoundException A referenced resource could not be loaded.
     */
    public  boolean evaluate( Context context,  Writer out,
                                     String logTag, String instring )
        throws ParseErrorException, MethodInvocationException,
            ResourceNotFoundException
    {
        return ri.evaluate(context, out, logTag, instring);
    }

    /**
     *  Renders the input reader using the context into the output writer.
     *  To be used when a template is dynamically constructed, or want to
     *  use Velocity as a token replacer.
     *
     *  @param context context to use in rendering input string
     *  @param writer  Writer in which to render the output
     *  @param logTag  string to be used as the template name for log messages
     *                 in case of error
     *  @param reader Reader containing the VTL to be rendered
     *
     *  @return true if successful, false otherwise.  If false, see
     *               Velocity runtime log
     * @throws ParseErrorException The template could not be parsed.
     * @throws MethodInvocationException A method on a context object could not be invoked.
     * @throws ResourceNotFoundException A referenced resource could not be loaded.
     *  @since Velocity v1.1
     */
    public boolean evaluate(Context context, Writer writer,
                                    String logTag, Reader reader)
        throws ParseErrorException, MethodInvocationException,
            ResourceNotFoundException
    {
        return ri.evaluate(context, writer, logTag, reader);
    }


    /**
     * Invokes a currently registered Velocimacro with the params provided
     * and places the rendered stream into the writer.
     * <br>
     * Note : currently only accepts args to the VM if they are in the context.
     *
     * @param vmName name of Velocimacro to call
     * @param logTag string to be used for template name in case of error. if null,
     *               the vmName will be used
     * @param params keys for args used to invoke Velocimacro, in java format
     *               rather than VTL (eg  "foo" or "bar" rather than "$foo" or "$bar")
     * @param context Context object containing data/objects used for rendering.
     * @param writer  Writer for output stream
     * @return true if Velocimacro exists and successfully invoked, false otherwise.
     */
    public boolean invokeVelocimacro( String vmName, String logTag,
                                              String params[], Context context,
                                              Writer writer )
    {
        return ri.invokeVelocimacro(vmName, logTag, params, context, writer);
    }

    /**
     *  merges a template and puts the rendered stream into the writer
     *
     *  @param templateName name of template to be used in merge
     *  @param encoding encoding used in template
     *  @param context  filled context to be used in merge
     *  @param  writer  writer to write template into
     *
     *  @return true if successful, false otherwise.  Errors
     *           logged to velocity log
     * @throws ResourceNotFoundException
     * @throws ParseErrorException
     * @throws MethodInvocationException
     *  @since Velocity v1.1
     */
    public boolean mergeTemplate( String templateName, String encoding,
                                      Context context, Writer writer )
        throws ResourceNotFoundException, ParseErrorException, MethodInvocationException
    {
        Template template = ri.getTemplate(templateName, encoding);

        if ( template == null )
        {
            String msg = "VelocityEngine.mergeTemplate() was unable to load template '"
                           + templateName + "'";
            getLog().error(msg);
            throw new ResourceNotFoundException(msg);
        }
        else
        {
            template.merge(context, writer);
            return true;
         }
    }

    /**
     *  Returns a <code>Template</code> from the Velocity
     *  resource management system.
     *
     * @param name The file name of the desired template.
     * @return     The template.
     * @throws ResourceNotFoundException if template not found
     *          from any available source.
     * @throws ParseErrorException if template cannot be parsed due
     *          to syntax (or other) error.
     */
    public Template getTemplate(String name)
        throws ResourceNotFoundException, ParseErrorException
    {
        return ri.getTemplate( name );
    }

    /**
     *  Returns a <code>Template</code> from the Velocity
     *  resource management system.
     *
     * @param name The file name of the desired template.
     * @param encoding The character encoding to use for the template.
     * @return     The template.
     * @throws ResourceNotFoundException if template not found
     *          from any available source.
     * @throws ParseErrorException if template cannot be parsed due
     *          to syntax (or other) error.
     *  @since Velocity v1.1
     */
    public Template getTemplate(String name, String encoding)
        throws ResourceNotFoundException, ParseErrorException
    {
        return ri.getTemplate( name, encoding );
    }

    /**
     *   Determines if a resource is accessible via the currently
     *   configured resource loaders.
     *   <br><br>
     *   Note that the current implementation will <b>not</b>
     *   change the state of the system in any real way - so this
     *   cannot be used to pre-load the resource cache, as the
     *   previous implementation did as a side-effect.
     *   <br><br>
     *   The previous implementation exhibited extreme laziness and
     *   sloth, and the author has been flogged.
     *
     *   @param resourceName  name of the resource to search for
     *   @return true if found, false otherwise
     *   @since 1.5
     */
    public boolean resourceExists(String resourceName)
    {
        return (ri.getLoaderNameForResource(resourceName) != null);
    }

    /**
     * Returns the current slf4j logger. Its namespace defaults to <code>org.apache.velocity</code>
     * if it hasn't been configured using the property <code>runtime.log.name</code> or the property
     * <code>runtime.log.instance</code>.
     * @return A Logger object.
     * @since 1.5
     */
    public Logger getLog()
    {
        return ri.getLog();
    }

    /**
     *  <p>
     *  Sets an application attribute (which can be any Object) that will be
     *  accessible from any component of the system that gets a
     *  RuntimeServices. This allows communication between the application
     *  environment and custom pluggable components of the Velocity engine,
     *  such as ResourceLoaders and Loggers.
     *  </p>
     *
     *  <p>
     *  Note that there is no enforcement or rules for the key
     *  used - it is up to the application developer.  However, to
     *  help make the intermixing of components possible, using
     *  the target Class name (e.g. com.foo.bar ) as the key
     *  might help avoid collision.
     *  </p>
     *
     *  @param key object 'name' under which the object is stored
     *  @param value object to store under this key
     */
     public void setApplicationAttribute( Object key, Object value )
     {
        ri.setApplicationAttribute(key, value);
     }

     /**
      *  <p>
      *  Return an application attribute (which can be any Object)
      *  that was set by the application in order to be accessible from
      *  any component of the system that gets a RuntimeServices.
      *  This allows communication between the application
      *  environment and custom pluggable components of the
      *  Velocity engine, such as ResourceLoaders and Loggers.
      *  </p>
      *
      *  @param key object 'name' under which the object is stored
      *  @return value object to store under this key
      * @since 1.5
      */
     public Object getApplicationAttribute( Object key )
     {
        return ri.getApplicationAttribute(key);
     }

     /**
      * Remove a directive.
      * @param name name of the directive.
      */
     public void removeDirective(String name)
     {
        ri.removeDirective(name);
     }

     /**
      * Instantiates and loads the directive with some basic checks.
      *
      * @param directiveClass classname of directive to load
      */
     public void loadDirective(String directiveClass)
     {
        ri.loadDirective(directiveClass);
     }

    public void initTemplate(Resource template)
    {
        ri.initTemplate(template);
    }

    public void initTemplate(Resource template, String encoding)
    {
        ri.initTemplate(template,  encoding);
    }
}
