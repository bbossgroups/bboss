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

import java.io.IOException;
import java.io.Writer;

/**
 * This class is responsible for handling EscapedDirectives
 *  in VTL.
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ASTEscapedDirective extends SimpleNode
{
    /**
     * @param id
     */
    public ASTEscapedDirective(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTEscapedDirective(Parser p, int id)
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
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException
    {
    	writer.write(firstImage);
        return true;
    }

    /**
     * @throws TemplateInitException
     * @see bboss.org.apache.velocity.runtime.parser.node.Node#init(org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
    	Object obj = super.init(context, data);
    	saveTokenImages();
    	cleanupParserAndTokens(); // drop reference to Parser and all JavaCC Tokens
    	return obj;
    }
}
