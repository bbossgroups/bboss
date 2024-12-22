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
import bboss.org.apache.velocity.runtime.parser.Token;
import bboss.org.apache.velocity.runtime.parser.node.Node;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Base class for all directives used in Velocity.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author Nathan Bubna
 * @version $Id$
 */
public abstract class Directive implements DirectiveConstants, Cloneable
{
    private int line = 0;
    private int column = 0;
    private boolean provideScope = false;
    private Template template;

    protected Logger log = null;

    /**
     *
     */
    protected RuntimeServices rsvc = null;

    /**
     * Return the name of this directive.
     * @return The name of this directive.
     */
    public abstract String getName();

    /**
     * Get the directive type BLOCK/LINE.
     * @return The directive type BLOCK/LINE.
     */
    public abstract int getType();

    /**
     * Allows the template location to be set.
     * @param line
     * @param column
     */
    public void setLocation( int line, int column )
    {
        this.line = line;
        this.column = column;
    }

    /**
     * Allows the template location to be set.
     * @param line
     * @param column
     * @param template
     */
    public void setLocation(int line, int column, Template template)
    {
        setLocation(line, column);
        this.template = template;
    }

    /**
     * returns the template in which this directive appears
     * @return template
     */
    public Template getTemplate()
    {
        return template;
    }

    /**
     * for log msg purposes
     * @return The current line for log msg purposes.
     */
    public int getLine()
    {
        return line;
    }

    /**
     * for log msg purposes
     * @return The current column for log msg purposes.
     */
    public int getColumn()
    {
        return column;
    }

    /**
     * @return The template file name this directive was defined in, or null if not
     * defined in a file.
     */
    public String getTemplateName()
    {
      return template.getName();
    }

    /**
     * @return the name to be used when a scope control is provided for this
     * directive.
     */
    public String getScopeName()
    {
        return getName();
    }

    /**
     * @return true if there will be a scope control injected into the context
     * when rendering this directive.
     */
    public boolean isScopeProvided()
    {
        return provideScope;
    }

    /**
     * How this directive is to be initialized.
     * @param rs
     * @param context
     * @param node
     * @throws TemplateInitException
     */
    public void init( RuntimeServices rs, InternalContextAdapter context,
                      Node node)
        throws TemplateInitException
    {
        rsvc = rs;
        log = rsvc.getLog("directive." + getName());

        provideScope = rsvc.isScopeControlEnabled(getScopeName());
    }

    /**
     * The Parser calls this method during template parsing to check the arguments
     * types.  Be aware that this method is called pre init, so not all data
     * is available in this method.  The default implementation does not peform any
     * checking.  We do this so that Custom directives do not trigger any parse
     * errors in IDEs.
     * @param argtypes type, Array of argument types of each argument to the directive
     * for example StandardParserTreeConstants.JJTWORD
     * @param t token of directive
     * @param templateName the name of the template this directive is referenced in.
     * @throws ParseException
     */
    public void checkArgs(ArrayList<Integer> argtypes,  Token t, String templateName)
        throws ParseException
    {
    }

    /**
     * How this directive is to be rendered
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     * @throws ResourceNotFoundException
     * @throws ParseErrorException
     * @throws MethodInvocationException
     */
    public abstract boolean render( InternalContextAdapter context,
                                    Writer writer, Node node )
           throws IOException, ResourceNotFoundException, ParseErrorException,
                MethodInvocationException;


    /**
     * This creates and places the scope control for this directive
     * into the context (if scope provision is turned on).
     * @param context
     */
    protected void preRender(InternalContextAdapter context)
    {
        if (isScopeProvided())
        {
            String name = getScopeName();
            Object previous = context.get(name);
            context.put(name, makeScope(previous));
        }
    }

    /**
     * @param prev
     * @return scope
     */
    protected Scope makeScope(Object prev)
    {
        return new Scope(this, prev);
    }

    /**
     * This cleans up any scope control for this directive after rendering,
     * assuming the scope control was turned on.
     * @param context
     */
    protected void postRender(InternalContextAdapter context)
    {
        if (isScopeProvided())
        {
            String name = getScopeName();
            Object obj = context.get(name);

            try
            {
                Scope scope = (Scope)obj;
                if (scope.getParent() != null)
                {
                    context.put(name, scope.getParent());
                }
                else if (scope.getReplaced() != null)
                {
                    context.put(name, scope.getReplaced());
                }
                else
                {
                    context.remove(name);
                }
            }
            catch (ClassCastException cce)
            {
                // the user can override the scope with a #set,
                // since that means they don't care about a replaced value
                // and obviously aren't too keen on their scope control,
                // and especially since #set is meant to be handled globally,
                // we'll assume they know what they're doing and not worry
                // about replacing anything superseded by this directive's scope
            }
        }
    }

}
