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
import bboss.org.apache.velocity.runtime.parser.Parser;

/**
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ASTAndNode extends ASTLogicalOperator
{
    /**
     * @param id
     */
    public ASTAndNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTAndNode(Parser p, int id)
    {
        super(p, id);
    }

    @Override
    public String getLiteralOperator()
    {
        return "&&";
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
     *  Returns the value of the expression.
     *  Since the value of the expression is simply the boolean
     *  result of evaluate(), lets return that.
     * @param context
     * @return The value of the expression.
     * @throws MethodInvocationException
     */
    @Override
    public Object value(InternalContextAdapter context)
        throws MethodInvocationException
    {
        return evaluate(context) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * logical and :
     * <pre>
     *   null &amp;&amp; right = false
     *   left &amp;&amp; null = false
     *   null &amp;&amp; null = false
     * </pre>
     * @param context
     * @return True if both sides are true.
     * @throws MethodInvocationException
     */
    @Override
    public boolean evaluate(InternalContextAdapter context)
        throws MethodInvocationException
    {
        Node left = jjtGetChild(0);
        Node right = jjtGetChild(1);

        /*
         * null == false
         */
        if (left == null || right == null)
        {
            return false;
        }

        /*
         *  short circuit the test.  Don't eval the RHS if the LHS is false
         */

        if( left.evaluate( context ) )
        {
            if ( right.evaluate( context ) )
            {
                return true;
            }
        }

        return false;
    }
}
