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

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.ParseException;
import bboss.org.apache.velocity.runtime.parser.node.Node;
import bboss.org.apache.velocity.runtime.parser.node.SimpleNode;
import bboss.org.apache.velocity.runtime.parser.node.StandardParserTreeConstants;
import bboss.org.apache.velocity.util.introspection.Info;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

/**
 * Evaluates the directive argument as a VTL string, using the existing
 * context.
 *
 * @author <a href="mailto:wglass@apache.org">Will Glass-Husain</a>
 * @version $Id$
 * @since 1.6
 */
public class Evaluate extends Directive
{

    /**
     * Return name of this directive.
     * @return The name of this directive.
     */
    @Override
    public String getName()
    {
        return "evaluate";
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
     * Initialize and check arguments.
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
         * Check that there is exactly one argument and it is a string or reference.
         */

        int argCount = node.jjtGetNumChildren();
        if (argCount == 0)
        {
            throw new TemplateInitException(
                    "#" + getName() + "() requires exactly one argument",
                    null,
                    rsvc.getLogContext().getStackTrace(),
                    context.getCurrentTemplateName(),
                    node.getColumn(),
                    node.getLine());
        }
        if (argCount > 1)
        {
            /*
             * use line/col of second argument
             */

            throw new TemplateInitException(
                    "#" + getName() + "() requires exactly one argument",
                    null,
                    rsvc.getLogContext().getStackTrace(),
                    context.getCurrentTemplateName(),
                    node.jjtGetChild(1).getColumn(),
                    node.jjtGetChild(1).getLine());
        }

        Node childNode = node.jjtGetChild(0);
        if ( childNode.getType() !=  StandardParserTreeConstants.JJTSTRINGLITERAL &&
             childNode.getType() !=  StandardParserTreeConstants.JJTREFERENCE )
        {
           throw new TemplateInitException(
                   "#" + getName() + "()  argument must be a string literal or reference",
                   null,
                   rsvc.getLogContext().getStackTrace(),
                   context.getCurrentTemplateName(),
                   childNode.getColumn(),
                   childNode.getLine());
        }
    }

    /**
     * Evaluate the argument, convert to a String, and evaluate again
     * (with the same context).
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     * @throws ResourceNotFoundException
     * @throws ParseErrorException
     * @throws MethodInvocationException
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer,
                          Node node) throws IOException, ResourceNotFoundException,
            ParseErrorException, MethodInvocationException
    {

        /*
         * Evaluate the string with the current context.  We know there is
         * exactly one argument and it is a string or reference.
         */

        Object value = node.jjtGetChild(0).value( context );
        String sourceText;
        if ( value != null )
        {
            sourceText = value.toString();
        }
        else
        {
            sourceText = "";
        }

        /*
         * The new string needs to be parsed since the text has been dynamically generated.
         */
        String templateName = context.getCurrentTemplateName();
        Template template = (Template)context.getCurrentResource();
        if (template == null)
        {
            template = new Template();
            template.setName(templateName);
        }
        SimpleNode nodeTree = null;

        try
        {
            nodeTree = rsvc.parse(new StringReader(sourceText), template);
        }
        catch (ParseException | TemplateInitException pex)
        {
            // use the line/column from the template
            Info info = new Info( templateName, node.getLine(), node.getColumn() );
            throw  new ParseErrorException( pex.getMessage(), info, rsvc.getLogContext().getStackTrace() );
        }

        /*
         * now we want to init and render.  Chain the context
         * to prevent any changes to the current context.
         */

        if (nodeTree != null)
        {
            context.pushCurrentTemplateName(templateName);

            try
            {
                try
                {
                    nodeTree.init(context, rsvc);
                }
                catch (TemplateInitException pex)
                {
                    Info info = new Info( templateName, node.getLine(), node.getColumn() );
                    throw  new ParseErrorException( pex.getMessage(), info, rsvc.getLogContext().getStackTrace() );
                }

                try
                {
                    preRender(context);

                    /*
                     *  now render, and let any exceptions fly
                     */
                    nodeTree.render(context, writer);
                }
                catch (StopCommand stop)
                {
                    if (!stop.isFor(this))
                    {
                        throw stop;
                    }
                }
                catch (ParseErrorException pex)
                {
                    // convert any parsing errors to the correct line/col
                    Info info = new Info( templateName, node.getLine(), node.getColumn() );
                    throw  new ParseErrorException( pex.getMessage(), info, rsvc.getLogContext().getStackTrace() );
                }
            }
            finally
            {
                context.popCurrentTemplateName();
                postRender(context);
            }
            return true;
        }

        return false;
    }

}
