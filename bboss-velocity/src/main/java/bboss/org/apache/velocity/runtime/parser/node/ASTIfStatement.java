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
import bboss.org.apache.velocity.runtime.parser.Token;

import java.io.IOException;
import java.io.Writer;


/**
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ASTIfStatement extends SimpleNode
{
    private String prefix = "";
    private String postfix = "";

    /*
     * '#' and '$' prefix characters eaten by javacc MORE mode
     */
    private String morePrefix = "";

    /**
     * @param id
     */
    public ASTIfStatement(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTIfStatement(Parser p, int id)
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

        /*
         * handle '$' and '#' chars prefix
         */
        Token t = getFirstToken();
        int pos = -1;
        while (t != null && (pos = t.image.lastIndexOf(rsvc.getParserConfiguration().getHashChar())) == -1)
        {
            t = t.next;
        }
        if (t != null && pos > 0)
        {
            morePrefix = t.image.substring(0, pos);
        }

        /* handle structured space gobbling */
        if (rsvc.getSpaceGobbling() == SpaceGobbling.STRUCTURED && postfix.length() > 0)
        {
            NodeUtils.fixIndentation(this, prefix);
        }

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
     * @return prefix
     */
    public String getPrefix()
    {
        return prefix;
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
     * @return postfix
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
        throws IOException,MethodInvocationException,
        	ResourceNotFoundException, ParseErrorException
    {
        SpaceGobbling spaceGobbling = rsvc.getSpaceGobbling();

        if (morePrefix.length() > 0 || spaceGobbling.compareTo(SpaceGobbling.LINES) < 0)
        {
            writer.write(prefix);
        }

        writer.write(morePrefix);

        /*
         * Check if the #if(expression) construct evaluates to true:
         */
        if (jjtGetChild(0).evaluate(context))
        {
            jjtGetChild(1).render(context, writer);
        }
        else
        {
            int totalNodes = jjtGetNumChildren();

            /*
             * Now check the remaining nodes left in the
             * if construct. The nodes are either elseif
             *  nodes or else nodes. Each of these node
             * types knows how to evaluate themselves. If
             * a node evaluates to true then the node will
             * render itself and this method will return
             * as there is nothing left to do.
             */
            for (int i = 2; i < totalNodes; i++)
            {
                if (jjtGetChild(i).evaluate(context))
                {
                    jjtGetChild(i).render(context, writer);
                    break;
                }
            }
        }

        if (morePrefix.length() > 0 || spaceGobbling == SpaceGobbling.NONE)
        {
            writer.write(postfix);
        }

        /*
         * This is reached without rendering anything (other than potential suffix/prefix) when an ASTIfStatement
         * consists of an if/elseif sequence where none of the nodes evaluate to true.
         */

        return true;
    }

    /**
     * @param context
     * @param visitor
     */
    public void process( InternalContextAdapter context, StandardParserVisitor visitor)
    {
    }
}
