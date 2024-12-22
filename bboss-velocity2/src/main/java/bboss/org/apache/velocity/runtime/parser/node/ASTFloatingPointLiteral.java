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
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.parser.Parser;

import java.math.BigDecimal;


/**
 * Handles floating point numbers.  The value will be either a Double
 * or a BigDecimal.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @since 1.5
 */
public class ASTFloatingPointLiteral extends SimpleNode
{

    // This may be of type Double or BigDecimal
    private Number value = null;

    /**
     * @param id
     */
    public ASTFloatingPointLiteral(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTFloatingPointLiteral(Parser p, int id)
    {
        super(p, id);
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
     *  Initialization method - doesn't do much but do the object
     *  creation.  We only need to do it once.
     * @param context
     * @param data
     * @return The data object.
     * @throws TemplateInitException
     */
    @Override
    public Object init(InternalContextAdapter context, Object data)
        throws TemplateInitException
    {
        /*
         *  init the tree correctly
         */

        super.init( context, data );

        /*
         * Determine the size of the item and make it a Double or BigDecimal as appropriate.
         */
         String str = getFirstToken().image;
         try
         {
             value = Double.valueOf( str );

         } catch ( NumberFormatException E1 )
         {

            // if there's still an Exception it will propqgate out
            value = new BigDecimal( str );

        }
        cleanupParserAndTokens();

        return data;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#value(org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public Object value(InternalContextAdapter context)
    {
        return value;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#evaluate(org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public boolean evaluate(InternalContextAdapter context)
    {
        return !rsvc.getBoolean(RuntimeConstants.CHECK_EMPTY_OBJECTS, true) || !MathUtils.isZero(value);
    }

}
