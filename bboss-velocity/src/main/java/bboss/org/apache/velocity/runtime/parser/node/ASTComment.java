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
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.runtime.parser.Token;

import java.io.IOException;
import java.io.Writer;

/**
 *  Represents all comments...
 *
 *  @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 *  @version $Id$
 */
public class ASTComment extends SimpleNode
{
    private static final char[] ZILCH = "".toCharArray();

    private char[] carr;

    /**
     * @param id
     */
    public ASTComment(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTComment(Parser p, int id)
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
     *  We need to make sure we catch any of the dreaded MORE tokens.
     * @param context
     * @param data
     * @return The data object.
     */
    @Override
    public Object init(InternalContextAdapter context, Object data)
    {
        Token t = getFirstToken();

        int loc1 = t.image.indexOf(parser.lineComment());
        int loc2 = t.image.indexOf(parser.blockComment());

        if (loc1 == -1 && loc2 == -1)
        {
            carr = ZILCH;
        }
        else
        {
            carr = t.image.substring(0, (loc1 == -1) ? loc2 : loc1).toCharArray();
        }

        cleanupParserAndTokens();

        return data;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(bboss.org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException
    {
        writer.write(carr);
        return true;
    }

}
