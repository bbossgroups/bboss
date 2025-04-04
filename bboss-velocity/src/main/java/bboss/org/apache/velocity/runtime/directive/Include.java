package bboss.org.apache.velocity.runtime.directive;

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

import bboss.org.apache.velocity.app.event.EventHandlerUtil;
import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.node.Node;
import bboss.org.apache.velocity.runtime.parser.node.StandardParserTreeConstants;
import bboss.org.apache.velocity.runtime.resource.Resource;
import bboss.org.apache.velocity.util.StringUtils;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>Pluggable directive that handles the #include() statement in VTL.
 * This #include() can take multiple arguments of either
 * StringLiteral or Reference.</p>
 *
 * <p>Notes:</p>
 * <ol>
 * <li>For security reasons, the included source material can only come
 *    from somewhere within the template root tree.  If you want to include
 *    content from elsewhere on your disk, add extra template roots, or use
 *    a link from somwehere under template root to that content.</li>
 *
 *  <li>By default, there is no output to the render stream in the event of
 *    a problem.  You can override this behavior with two property values :
 *       include.output.errormsg.start
 *       include.output.errormsg.end
 *     If both are defined in velocity.properties, they will be used to
 *     in the render output to bracket the arg string that caused the
 *     problem.
 *     Ex. : if you are working in html then
 *       include.output.errormsg.start=&lt;!-- #include error :
 *       include.output.errormsg.end= --&gt;
 *     might be an excellent way to start...</li>
 *
 *  <li>As noted above, #include() can take multiple arguments.
 *    Ex : #include('foo.vm' 'bar.vm' $foo)
 *    will include all three if valid to output without any
 *    special separator.</li>
 *  </ol>
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:kav@kav.dk">Kasper Nielsen</a>
 * @version $Id$
 */
public class Include extends InputBase
{
    private String outputMsgStart = "";
    private String outputMsgEnd = "";

    /**
     * Return name of this directive.
     * @return The name of this directive.
     */
    @Override
    public String getName()
    {
        return "include";
    }

    /**
     * Return type of this directive.
     * @return The type of this directive.
     */
    @Override
    public int getType()
    {
        return LINE;
    }

    /**
     * Since there is no processing of content,
     * there is never a need for an internal scope.
     */
    @Override
    public boolean isScopeProvided()
    {
        return false;
    }

    /**
     *  simple init - init the tree and get the elementKey from
     *  the AST
     * @param rs
     * @param context
     * @param node
     * @throws TemplateInitException
     */
    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context,
                     Node node)
        throws TemplateInitException
    {
        super.init( rs, context, node );

        /*
         *  get the msg, and add the space so we don't have to
         *  do it each time
         */
        outputMsgStart = rsvc.getString(RuntimeConstants.ERRORMSG_START);
        outputMsgStart = outputMsgStart + " ";

        outputMsgEnd = rsvc.getString(RuntimeConstants.ERRORMSG_END );
        outputMsgEnd = " " + outputMsgEnd;
    }

    /**
     *  iterates through the argument list and renders every
     *  argument that is appropriate.  Any non appropriate
     *  arguments are logged, but render() continues.
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     * @throws MethodInvocationException
     * @throws ResourceNotFoundException
     */
    @Override
    public boolean render(InternalContextAdapter context,
                          Writer writer, Node node)
        throws IOException, MethodInvocationException,
               ResourceNotFoundException
    {
        /*
         *  get our arguments and check them
         */

        int argCount = node.jjtGetNumChildren();

        for( int i = 0; i < argCount; i++)
        {
            /*
             *  we only handle StringLiterals and References right now
             */

            Node n = node.jjtGetChild(i);

            if ( n.getType() ==  StandardParserTreeConstants.JJTSTRINGLITERAL ||
                 n.getType() ==  StandardParserTreeConstants.JJTREFERENCE )
            {
                if (!renderOutput( n, context, writer ))
                    outputErrorToStream( writer, "error with arg " + i
                        + " please see log.");
            }
            else
            {
                String msg = "invalid #include() argument '"
                  + n.toString() + "' at " + StringUtils.formatFileString(this);
                log.error(msg);
                outputErrorToStream( writer, "error with arg " + i
                    + " please see log.");
                throw new VelocityException(msg, null, rsvc.getLogContext().getStackTrace());
            }
        }

        return true;
    }

    /**
     *  does the actual rendering of the included file
     *
     *  @param node AST argument of type StringLiteral or Reference
     *  @param context valid context so we can render References
     *  @param writer output Writer
     *  @return boolean success or failure.  failures are logged
     *  @exception IOException
     *  @exception MethodInvocationException
     *  @exception ResourceNotFoundException
     */
    private boolean renderOutput( Node node, InternalContextAdapter context,
                                  Writer writer )
        throws IOException, MethodInvocationException,
               ResourceNotFoundException
    {
        if ( node == null )
        {
            log.error("#include() null argument");
            return false;
        }

        /*
         *  does it have a value?  If you have a null reference, then no.
         */
        Object value = node.value( context );
        if ( value == null)
        {
            log.error("#include() null argument");
            return false;
        }

        /*
         *  get the path
         */
        String sourcearg = value.toString();

        /*
         *  check to see if the argument will be changed by the event handler
         */

        String arg = EventHandlerUtil.includeEvent( rsvc, context, sourcearg, context.getCurrentTemplateName(), getName() );

        /*
         *   a null return value from the event cartridge indicates we should not
         *   input a resource.
         */
        boolean blockinput = false;
        if (arg == null)
            blockinput = true;

        Resource resource = null;

        try
        {
            if (!blockinput)
                resource = getResource(arg, getInputEncoding(context));
        }
        catch ( ResourceNotFoundException rnfe )
        {
            /*
             * the arg wasn't found.  Note it and throw
             */
            log.error("#" + getName() + "(): cannot find resource '{}', called at {}",
                      arg, StringUtils.formatFileString(this));
            throw rnfe;
        }

        /*
         * pass through application level runtime exceptions
         */
        catch( RuntimeException e )
        {
            log.error("#" + getName() + "(): arg = '{}', called at {}",
                      arg, StringUtils.formatFileString(this));
            throw e;
        }
        catch (Exception e)
        {
            String msg = "#" + getName() + "(): arg = '" + arg +
                        "', called at " + StringUtils.formatFileString(this);
            log.error(msg, e);
            throw new VelocityException(msg, e, rsvc.getLogContext().getStackTrace());
        }


        /*
         *    note - a blocked input is still a successful operation as this is
         *    expected behavior.
         */

        if ( blockinput )
            return true;

        else if ( resource == null )
            return false;

        writer.write((String)resource.getData());
        return true;
    }

    /**
     *  Puts a message to the render output stream if ERRORMSG_START / END
     *  are valid property strings.  Mainly used for end-user template
     *  debugging.
     *  @param writer
     *  @param msg
     *  @throws IOException
     *  @deprecated if/how errors are displayed is not the concern of the engine, which should throw in all cases
     */
    private void outputErrorToStream( Writer writer, String msg )
        throws IOException
    {
        if ( outputMsgStart != null  && outputMsgEnd != null)
        {
            writer.write(outputMsgStart);
            writer.write(msg);
            writer.write(outputMsgEnd);
        }
    }

    /**
     * Find the resource to include
     * @param path resource path
     * @param encoding resource encoding
     * @return found resource
     * @throws ResourceNotFoundException if resource was not found
     */
    protected Resource getResource(String path, String encoding) throws ResourceNotFoundException
    {
        return rsvc.getContent(path, encoding);
    }
}
