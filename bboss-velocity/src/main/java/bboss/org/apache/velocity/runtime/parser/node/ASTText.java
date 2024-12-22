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
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.runtime.parser.Token;

import java.io.IOException;
import java.io.Writer;

/**
 *
 */
public class ASTText extends SimpleNode
{
    private String ctext;

    /**
     * @param id
     */
    public ASTText(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTText(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * text getter
     * @return ctext
     */
    public String getCtext()
    {
        return ctext;
    }

    /**
     * text setter
     * @param ctext
     */
    public void setCtext(String ctext)
    {
        this.ctext = ctext;
    }

    /**
     * text getter
     * @return literal representation
     */
    @Override
    public String literal()
    {
        return ctext;
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
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#init(bboss.org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data)
    throws TemplateInitException
    {
        StringBuilder builder = new StringBuilder();
        Token t = getFirstToken();
        for (; t != getLastToken(); t = t.next)
        {
            builder.append(NodeUtils.tokenLiteral(parser, t));
        }
        builder.append(NodeUtils.tokenLiteral(parser, t));
        ctext = builder.toString();

        cleanupParserAndTokens();

        return data;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(bboss.org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException
    {
        writer.write(ctext);
        return true;
    }
}
