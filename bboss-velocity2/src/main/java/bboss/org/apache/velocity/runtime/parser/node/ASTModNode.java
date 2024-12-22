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
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.parser.Parser;

/**
 * Handles modulus division<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ASTModNode extends ASTMathNode
{
    /**
     * @param id
     */
    public ASTModNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTModNode(Parser p, int id)
    {
        super(p, id);
    }

    @Override
    public Number perform(Number left, Number right, InternalContextAdapter context)
    {
        /*
         * check for divide / modulo by 0
         */
        if (MathUtils.isZero(right))
        {
            String msg = "Right side of modulus operation is zero. Must be non-zero. "
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
        return MathUtils.modulo(left, right);
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

    @Override
    public String getLiteralOperator()
    {
        return "%";
    }

}
