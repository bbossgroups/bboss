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
import bboss.org.apache.velocity.exception.MathException;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.util.DuckType;

/**
 * Helps handle math<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author Nathan Bubna
 * @version $Id: ASTMathNode.java 517553 2007-03-13 06:09:58Z wglass $
 */
public abstract class ASTMathNode extends ASTBinaryOperator
{
    protected boolean strictMode = false;

    public ASTMathNode(int id)
    {
        super(id);
    }

    public ASTMathNode(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
        super.init(context, data);
        strictMode = rsvc.getBoolean(RuntimeConstants.STRICT_MATH, false);
        cleanupParserAndTokens();
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jjtAccept(StandardParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * gets the two args and performs the operation on them
     *
     * @param context
     * @return result or null
     * @throws MethodInvocationException
     */
    @Override
    public Object value(InternalContextAdapter context) throws MethodInvocationException
    {
        Object left = jjtGetChild(0).value(context);
        Object right = jjtGetChild(1).value(context);

        /*
         * should we do anything special here?
         */
        Object special = handleSpecial(left, right, context);
        if (special != null)
        {
            return special;
        }

        // coerce to Number type, if possible
        try
        {
            left = DuckType.asNumber(left);
        }
        catch (NumberFormatException nfe) {}
        try
        {
            right = DuckType.asNumber(right);
        }
        catch (NumberFormatException nfe) {}

        /*
         * if not a Number, not much we can do
         */
        if (!(left instanceof Number) || !(right instanceof Number))
        {
            boolean wrongright = (left instanceof Number);
            boolean wrongtype = wrongright ? right != null : left != null;
            String msg = (wrongright ? "Right" : "Left")
                        + " side of math operation ("
                        + jjtGetChild(wrongright ? 1 : 0).literal() + ") "
                        + (wrongtype ? "is not a Number. " : "has a null value. ")
                        + getLocation(context);
            if (strictMode)
            {
                log.error(msg);
                throw new MathException(msg, rsvc.getLogContext().getStackTrace());
            }
            else
            {
                log.debug(msg);
                return null;
            }
        }

        return perform((Number)left, (Number)right, context);
    }

    /**
     * Extension hook to allow special behavior by subclasses
     * If this method returns a non-null value, that is returned,
     * rather than the result of the math operation.
     * @param left
     * @param right
     * @param context
     * @return special value
     * @see ASTAddNode#handleSpecial
     */
    protected Object handleSpecial(Object left, Object right, InternalContextAdapter context)
    {
        // do nothing, this is an extension hook
        return null;
    }

    /**
     * Performs the math operation represented by this node.
     * @param left
     * @param right
     * @param context
     * @return computed value
     * @see ASTAddNode#perform(Number, Number, InternalContextAdapter)
     */
    public abstract Number perform(Number left, Number right, InternalContextAdapter context);

}
