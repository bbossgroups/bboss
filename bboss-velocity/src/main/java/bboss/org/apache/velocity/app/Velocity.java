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
import bboss.org.apache.velocity.runtime.RuntimeSingleton;
import bboss.org.apache.velocity.runtime.resource.Resource;
import org.slf4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * This class provides  services to the application
 * developer, such as:
 * <ul>
 * <li> Simple Velocity Runtime engine initialization methods.
 * <li> Functions to apply the template engine to streams and strings
 *      to allow embedding and dynamic template generation.
 * <li> Methods to access Velocimacros directly.
 * </ul>
 *
 * <br><br>
 * While the most common way to use Velocity is via templates, as
 * Velocity is a general-purpose template engine, there are other
 * uses that Velocity is well suited for, such as processing dynamically
 * created templates, or processing content streams.
 *
 * <br><br>
 * The methods herein were developed to allow easy access to the Velocity
 * facilities without direct spelunking of the internals.  If there is
 * something you feel is necessary to add here, please, send a patch.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:Christoph.Reck@dlr.de">Christoph Reck</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id$
 */
public class Velocity implements RuntimeConstants
{
    /**
     *  initialize the Velocity runtime engine, using the default
     *  properties of the Velocity distribution
     */
    public static void init()
    {
        RuntimeSingleton.init();
    }

    public static void initTemplate(Resource template)
    {
        RuntimeSingleton.initTemplate(template);
    }

    public static void initTemplate(Resource template,String encoding)
    {
        RuntimeSingleton.initTemplate(template, encoding);
    }

    /**
     * Resets the instance, so Velocity can be re-initialized again.
     *
     * @since 2.0.0
     */
    public static void reset()
    {
        RuntimeSingleton.reset();
    }

    /**
     *  initialize the Velocity runtime engine, using default properties
     *  plus the properties in the properties file passed in as the arg
     *
     *  @param propsFilename file containing properties to use to initialize
     *         the Velocity runtime
     */
    public static void init( String propsFilename )
    {
        RuntimeSingleton.init(propsFilename);
    }

    /**
     *  initialize the Velocity runtime engine, using default properties
     *  plus the properties in the passed in java.util.Properties object
     *
     *  @param p  Properties object containing initialization properties
     */
    public static void init( Properties p )
    {
        RuntimeSingleton.init( p );
    }

    /**
     * Set a Velocity Runtime property.
     *
     * @param key The property key.
     * @param value The property value.
     */
    public static void setProperty(String key, Object value)
    {
        RuntimeSingleton.setProperty(key, value);
    }

    /**
     * Add a Velocity Runtime property.
     *
     * @param key The property key.
     * @param value The property value.
     */
    public static void addProperty(String key, Object value)
    {
        RuntimeSingleton.addProperty(key, value);
    }

    /**
     * Clear a Velocity Runtime property.
     *
     * @param key of property to clear
     */
    public static void clearProperty(String key)
    {
        RuntimeSingleton.clearProperty(key);
    }

    /**
     * Set an entire configuration at once.
     *
     * @param configuration A configuration object.
     * @since 2.0
     *
     */
    public static void setProperties( Properties configuration)
    {
        RuntimeSingleton.setProperties(configuration);
    }

    /**
     *  Get a Velocity Runtime property.
     *
     *  @param key property to retrieve
     *  @return property value or null if the property
     *        not currently set
     */
    public static Object getProperty( String key )
    {
        return RuntimeSingleton.getProperty( key );
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
    public static  boolean evaluate( Context context,  Writer out,
                                     String logTag, String instring )
        throws ParseErrorException, MethodInvocationException,
            ResourceNotFoundException
    {
        return RuntimeSingleton.getRuntimeServices()
            .evaluate(context, out, logTag, instring);
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
    public static boolean evaluate( Context context, Writer writer,
                                    String logTag, Reader reader )
        throws ParseErrorException, MethodInvocationException,
            ResourceNotFoundException
    {
        return RuntimeSingleton.getRuntimeServices().evaluate(context, writer,
                                                              logTag, reader);
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
    public static  boolean invokeVelocimacro( String vmName, String logTag,
                                              String params[], Context context,
                                              Writer writer )
    {
        return RuntimeSingleton.getRuntimeServices()
            .invokeVelocimacro(vmName, logTag, params, context, writer);
    }

    /**
     *  Merges a template and puts the rendered stream into the writer
     *
     *  @param templateName name of template to be used in merge
     *  @param encoding encoding used in template
     *  @param context  filled context to be used in merge
     *  @param  writer  writer to write template into
     *
     *  @return true if successful, false otherwise.  Errors
     *           logged to velocity log
     *
     * @throws ParseErrorException The template could not be parsed.
     * @throws MethodInvocationException A method on a context object could not be invoked.
     * @throws ResourceNotFoundException A referenced resource could not be loaded.
     *
     * @since Velocity v1.1
     */
    public static boolean mergeTemplate( String templateName, String encoding,
                                      Context context, Writer writer )
        throws ResourceNotFoundException, ParseErrorException, MethodInvocationException
    {
        Template template = RuntimeSingleton.getTemplate(templateName, encoding);

        if ( template == null )
        {
            String msg = "Velocity.mergeTemplate() was unable to load template '"
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
    public static Template getTemplate(String name)
        throws ResourceNotFoundException, ParseErrorException
    {
        return RuntimeSingleton.getTemplate( name );
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
     *
     *  @since Velocity v1.1
     */
    public static Template getTemplate(String name, String encoding)
        throws ResourceNotFoundException, ParseErrorException
    {
        return RuntimeSingleton.getTemplate( name, encoding );
    }

    /**
     * <p>Determines whether a resource is accessible via the
     * currently configured resource loaders.  {@link
     * bboss.org.apache.velocity.runtime.resource.Resource} is the generic
     * description of templates, static content, etc.</p>
     *
     * <p>Note that the current implementation will <b>not</b> change
     * the state of the system in any real way - so this cannot be
     * used to pre-load the resource cache, as the previous
     * implementation did as a side-effect.</p>
     *
     * @param resourceName The name of the resource to search for.
     * @return Whether the resource was located.
     */
    public static boolean resourceExists(String resourceName)
    {
        return (RuntimeSingleton.getLoaderNameForResource(resourceName) != null);
    }

    /**
     * Returns the current logger.
     *
     * @return A convenience Logger instance.
     * @since 1.5
     */
    public static Logger getLog()
    {
        return RuntimeSingleton.getLog();
    }

    /**
     *  <p>
     *  Set the an ApplicationAttribue, which is an Object
     *  set by the application which is accessible from
     *  any component of the system that gets a RuntimeServices.
     *  This allows communication between the application
     *  environment and custom pluggable components of the
     *  Velocity engine, such as loaders and loggers.
     *  </p>
     *
     *  <p>
     *  Note that there is no enforcement or rules for the key
     *  used - it is up to the application developer.  However, to
     *  help make the intermixing of components possible, using
     *  the target Class name (e.g.  com.foo.bar ) as the key
     *   might help avoid collision.
     *  </p>
     *
     *  @param key object 'name' under which the object is stored
     *  @param value object to store under this key
     */
     public static void setApplicationAttribute( Object key, Object value )
     {
        RuntimeSingleton.getRuntimeServices().setApplicationAttribute( key, value);
     }


    /**
     * Remove a directive.
     *
     * @param name name of the directive.
     */
    public void removeDirective(String name)
    {
        RuntimeSingleton.removeDirective(name);
    }

    /**
     * Instantiates and loads the directive with some basic checks.
     *
     * @param directiveClass classname of directive to load
     */
    public void loadDirective(String directiveClass)
    {
        RuntimeSingleton.loadDirective(directiveClass);
    }
}
