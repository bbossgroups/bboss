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

import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * BlockMacro directive is used to invoke Velocity macros with normal parameters and a macro body.
 * <p>
 * The macro can then refer to the passed body AST. This directive can be used as a
 * "decorator". Body AST can contain any valid Velocity syntax.
 *
 * An example:
 * <pre>
 * #set($foobar = "yeah!")
 *
 * #macro(strong $txt)
 * &lt;strong&gt;$bodyContent&lt;/strong&gt; $txt
 * #end
 *
 * #@strong($foobar)
 * &lt;u&gt;This text is underlined and bold&lt;/u&gt;
 * #end
 * </pre>
 * Will print:
 * <pre>
 * &lt;strong&gt;&lt;u&gt;This text is underlined and bold&lt;u&gt;&lt;/strong&gt; yeah!
 * </pre>
 *
 * bodyContent reference name is configurable (see velocity.properties).
 *
 * @author <a href="mailto:wyla@removethis.sci.fi">Jarkko Viinamaki</a>
 * @since 1.7
 * @version $Id$
 */
public class BlockMacro extends Block
{
    private String name;
    private RuntimeMacro macro;

    @Override
    public String getName()
    {
        return key;
    }

    /**
     * Override to use the macro name, since it is within an
     * #@myMacro() ... #end block that the scope in question
     * would be used.
     */
    @Override
    public String getScopeName()
    {
        return name;
    }

    /**
     * Initializes the directive.
     *
     * @param rs
     * @param macroName
     * @param context
     * @param node
     * @throws TemplateInitException
     */
    public void init(RuntimeServices rs, String macroName, InternalContextAdapter context, Node node)
        throws TemplateInitException
    {
        this.name = macroName;

        super.init(rs, context, node);

        // get name of the reference that refers to AST block passed to block macro call
        key = rsvc.getString(RuntimeConstants.VM_BODY_REFERENCE, "bodyContent");

        // use the macro max depth for bodyContent max depth as well
        maxDepth = rsvc.getInt(RuntimeConstants.VM_MAX_DEPTH);

        macro = new RuntimeMacro();
        macro.setLocation(getLine(), getColumn(), getTemplate());
        macro.init(rsvc, name, context, node);
    }

    /**
     * Renders content using the selected macro and the passed AST body.
     *
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
        throws IOException
    {
        return macro.render(context, writer, node, new Reference(context, this));
    }

}
