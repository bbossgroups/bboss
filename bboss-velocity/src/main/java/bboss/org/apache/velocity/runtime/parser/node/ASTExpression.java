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
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.parser.Parser;

/**
 *
 */
public class ASTExpression extends SimpleNode
{
    /**
     * @param id
     */
    public ASTExpression(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTExpression(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(bboss.org.apache.velocity.runtime.parser.node.StandardParserVisitor, java.lang.Object)
     */
    @Override
    public Object jjtAccept(StandardParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#evaluate(bboss.org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public boolean evaluate(InternalContextAdapter context)
        throws MethodInvocationException
    {
        return jjtGetChild(0).evaluate(context);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#value(bboss.org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public Object value(InternalContextAdapter context)
        throws MethodInvocationException
    {
        return jjtGetChild(0).value(context);
    }

    /**
     * @throws TemplateInitException
     * @see bboss.org.apache.velocity.runtime.parser.node.Node#init(bboss.org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
    	Object obj = super.init(context, data);
    	saveTokenImages();
    	cleanupParserAndTokens(); // drop reference to Parser and all JavaCC Tokens
    	return obj;
    }

    @Override
    public String literal()
    {
        return jjtGetChild(0).literal();
    }
}
