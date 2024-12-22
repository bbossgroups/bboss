package bboss.org.apache.velocity.runtime.parser.node;

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
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.parser.Parser;

import java.io.IOException;
import java.io.Writer;

/**
 * This class is responsible for handling the ElseIf VTL control statement.
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
*/
public class ASTElseIfStatement extends SimpleNode
{
    /**
     * @param id
     */
    public ASTElseIfStatement(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTElseIfStatement(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * @throws TemplateInitException
     * @see bboss.org.apache.velocity.runtime.parser.node.Node#init(org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
        Object obj = super.init(context, data);
         cleanupParserAndTokens(); // drop reference to Parser and all JavaCC Tokens
        return obj;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(org.apache.velocity.runtime.parser.node.StandardParserVisitor, java.lang.Object)
     */
    @Override
    public Object jjtAccept(StandardParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * An ASTElseStatement is true if the expression
     * it contains evaluates to true. Expressions know
     * how to evaluate themselves, so we do that
     * here and return the value back to ASTIfStatement
     * where this node was originally asked to evaluate
     * itself.
     * @param context
     * @return True if all children are true.
     * @throws MethodInvocationException
     */
    @Override
    public boolean evaluate (InternalContextAdapter context)
        throws MethodInvocationException
    {
        return jjtGetChild(0).evaluate(context);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException,MethodInvocationException,
        	ResourceNotFoundException, ParseErrorException
    {
        return jjtGetChild(1).render( context, writer );
    }
}
