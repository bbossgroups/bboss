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

import java.io.IOException;
import java.io.Writer;

import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.runtime.parser.Parser;

/**
 * This class is responsible for handling EscapedDirectives
 *  in VTL.
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTEscapedDirective.java 731266 2009-01-04 15:11:20Z byron $
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
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(bboss.org.apache.velocity.runtime.parser.node.ParserVisitor, java.lang.Object)
     */
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(bboss.org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException
    {
        writer.write(getFirstToken().image);
        return true;
    }

}
