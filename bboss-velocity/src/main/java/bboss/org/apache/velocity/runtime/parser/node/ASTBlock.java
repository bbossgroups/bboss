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
import bboss.org.apache.velocity.runtime.RuntimeConstants.SpaceGobbling;
import bboss.org.apache.velocity.runtime.parser.Parser;

import java.io.IOException;
import java.io.Writer;


/**
 *
 */
public class ASTBlock extends SimpleNode
{
    private String prefix = "";
    private String postfix = "";

    // used during parsing
    public boolean endsWithNewline = false;

    /*
     * '#' and '$' prefix characters eaten by javacc MORE mode, prefixing the '#' ending the block
     */
    private String morePostfix = "";

    /**
     * @param id
     */
    public ASTBlock(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTBlock(Parser p, int id)
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
     * @throws TemplateInitException
     * @see bboss.org.apache.velocity.runtime.parser.node.Node#init(bboss.org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
        Object obj = super.init(context, data);
        cleanupParserAndTokens(); // drop reference to Parser and all JavaCC Tokens
        return obj;
    }

    /**
     * set indentation prefix
     * @param prefix
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * get indentation prefix
     * @return indentation prefix
     */
    public String getPrefix()
    {
        return prefix;
    }

    public void setMorePostfix(String morePosffix)
    {
        this.morePostfix = morePosffix;
    }

    /**
     * set indentation postfix
     * @param postfix
     */
    public void setPostfix(String postfix)
    {
        this.postfix = postfix;
    }

    /**
     * get indentation postfix
     * @return indentation prefix
     */
    public String getPostfix()
    {
        return postfix;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(bboss.org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException, MethodInvocationException,
        	ResourceNotFoundException, ParseErrorException
    {
        SpaceGobbling spaceGobbling = rsvc.getSpaceGobbling();

        if (spaceGobbling == SpaceGobbling.NONE)
        {
            writer.write(prefix);
        }

        int i, k = jjtGetNumChildren();

        for (i = 0; i < k; i++)
            jjtGetChild(i).render(context, writer);

        if (morePostfix.length() > 0 || spaceGobbling.compareTo(SpaceGobbling.LINES) < 0)
        {
            writer.write(postfix);
        }

        writer.write(morePostfix);

        return true;
    }
}
