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
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.util.DuckType;
import bboss.org.apache.velocity.util.StringUtils;

/**
 * Numeric comparison support<br><br>
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author Nathan Bubna
 */
public abstract class ASTComparisonNode extends ASTBinaryOperator
{
    /**
     * @param id
     */
    public ASTComparisonNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTComparisonNode(Parser p, int id)
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
    public boolean evaluate(InternalContextAdapter context) throws MethodInvocationException
    {
        Object left = jjtGetChild(0).value(context);
        Object right = jjtGetChild(1).value(context);

        if (left == null || right == null)
        {
            return compareNull(left, right);
        }
        Boolean result = null;
        if (!(left instanceof String) || !(right instanceof String)) {
            result = compareNumbers(left, right);
        }
        if (result == null)
        {
            result = compareNonNumber(left, right);
        }
        return result;
    }

    /**
     * Always false by default, != and == subclasses must override this.
     * @param left
     * @param right
     * @return comparison result
     */
    public boolean compareNull(Object left, Object right)
    {
        // if either side is null, log and bail
        String msg = (left == null ? "Left" : "Right")
                       + " side ("
                       + jjtGetChild( (left == null? 0 : 1) ).literal()
                       + ") of comparison operation has null value at "
                       + StringUtils.formatFileString(this);
        if (rsvc.getBoolean(RuntimeConstants.RUNTIME_REFERENCES_STRICT, false))
        {
            throw new VelocityException(msg, null, rsvc.getLogContext().getStackTrace());
        }
        log.error(msg);
        return false;
    }

    /**
     * compare numbers
     * @param left
     * @param right
     * @return comparison result
     */
    public Boolean compareNumbers(Object left, Object right)
    {
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

        // only compare Numbers
        if (left instanceof Number && right instanceof Number)
        {
            return numberTest(MathUtils.compare((Number)left, (Number)right));
        }
        return null;
    }

    /**
     * get the string representing the mathematical operator
     * @return operator string
     */
    @Override
    public abstract String getLiteralOperator();

    /**
     * performs the actual comparison
     * @param compareResult
     * @return comparison result
     */
    public abstract boolean numberTest(int compareResult);

    public boolean compareNonNumber(Object left, Object right)
    {
        // by default, log and bail
        String msg = (right instanceof Number ? "Left" : "Right")
                       + " side of comparison operation is not a number at "
                       + StringUtils.formatFileString(this);
        if (rsvc.getBoolean(RuntimeConstants.RUNTIME_REFERENCES_STRICT, false))
        {
            throw new VelocityException(msg, null, rsvc.getLogContext().getStackTrace());
        }
        log.error(msg);
        return false;
    }

    private String getLiteral(boolean left)
    {
        return jjtGetChild(left ? 0 : 1).literal();
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#value(bboss.org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public Object value(InternalContextAdapter context) throws MethodInvocationException
    {
        return evaluate(context);
    }
}
