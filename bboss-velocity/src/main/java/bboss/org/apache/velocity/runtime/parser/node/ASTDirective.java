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
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.runtime.RuntimeConstants.SpaceGobbling;
import bboss.org.apache.velocity.runtime.directive.BlockMacro;
import bboss.org.apache.velocity.runtime.directive.Directive;
import bboss.org.apache.velocity.runtime.directive.RuntimeMacro;
import bboss.org.apache.velocity.runtime.parser.ParseException;
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.runtime.parser.StandardParserConstants;
import bboss.org.apache.velocity.runtime.parser.Token;
import bboss.org.apache.velocity.util.introspection.Info;

import java.io.IOException;
import java.io.Writer;

/**
 * This class is responsible for handling the pluggable
 * directives in VTL.
 *
 * For example :  #foreach()
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:kav@kav.dk">Kasper Nielsen</a>
 * @version $Id$
 */
public class ASTDirective extends SimpleNode
{
    private Directive directive = null;
    private String directiveName = "";
    private boolean isDirective;
    private boolean isInitialized;

    private String prefix = "";
    private String postfix = "";

    /*
     * '#' and '$' prefix characters eaten by javacc MORE mode
     */
    private String morePrefix = "";

    /**
     * @param id
     */
    public ASTDirective(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTDirective(Parser p, int id)
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
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#init(bboss.org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public synchronized Object init(InternalContextAdapter context, Object data)
    throws TemplateInitException
    {
        Token t;

        /* method is synchronized to avoid concurrent directive initialization **/

        if (!isInitialized)
        {
            super.init( context, data );

            /*
             * handle '$' and '#' chars prefix
             */
            t = getFirstToken();
            int pos = -1;
            while (t != null && (pos = t.image.lastIndexOf(rsvc.getParserConfiguration().getHashChar())) == -1)
            {
                t = t.next;
            }
            if (t != null && pos > 0)
            {
                morePrefix = t.image.substring(0, pos);
            }

            /*
             *  only do things that are not context dependent
             */

            if (parser.isDirective( directiveName ))
            {
                isDirective = true;

                try
                {
                    directive = parser.getDirective( directiveName )
                        .getClass().newInstance();
                }
                catch (InstantiationException | IllegalAccessException e)
                {
                    throw new VelocityException(
                            "Couldn't initialize directive of class " +
                            parser.getDirective(directiveName).getClass().getName(),
                            e, rsvc.getLogContext().getStackTrace());
                }

                t = getFirstToken();
                if (t.kind == StandardParserConstants.WHITESPACE) t = t.next;
                directive.setLocation(t.beginLine, t.beginColumn, getTemplate());
                directive.init(rsvc, context, this);
            }
            else if( directiveName.startsWith(String.valueOf(rsvc.getParserConfiguration().getAtChar())) )
            {
                if( this.jjtGetNumChildren() > 0 )
                {
                    // block macro call (normal macro call but has AST body)
                    directiveName = directiveName.substring(1);

                    directive = new BlockMacro();
                    directive.setLocation(getLine(), getColumn(), getTemplate());

                    try
                    {
                        ((BlockMacro)directive).init( rsvc, directiveName, context, this );
                    }
                    catch (TemplateInitException die)
                    {
                        throw new TemplateInitException(die.getMessage(),
                            (ParseException) die.getCause(),
                            rsvc.getLogContext().getStackTrace(),
                            die.getTemplateName(),
                            die.getColumnNumber() + getColumn(),
                            die.getLineNumber() + getLine());
                    }
                    isDirective = true;
                }
                else
                {
                    // this is a fake block macro call without a body. e.g. #@foo
                    // just render as it is
                    isDirective = false;
                }
            }
            else
            {
                /*
                  Create a new RuntimeMacro
                 */
                directive = new RuntimeMacro();
                directive.setLocation(getLine(), getColumn(), getTemplate());

                /*
                  Initialize it
                 */
                try
                {
                    ((RuntimeMacro)directive).init( rsvc, directiveName, context, this );
                }

                /*
                  correct the line/column number if an exception is caught
                 */
                catch (TemplateInitException die)
                {
                    throw new TemplateInitException(die.getMessage(),
                            (ParseException) die.getCause(),
                            rsvc.getLogContext().getStackTrace(),
                            die.getTemplateName(),
                            die.getColumnNumber() + getColumn(),
                            die.getLineNumber() + getLine());
                }
                isDirective = true;

            }

            isInitialized = true;

            saveTokenImages();
            cleanupParserAndTokens();
        }

        if (morePrefix.length() == 0 && rsvc.getSpaceGobbling() == SpaceGobbling.STRUCTURED && isInitialized && isDirective && directive.getType() == Directive.BLOCK)
        {
            NodeUtils.fixIndentation(this, prefix);
        }

        return data;
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
     * more prefix getter
     * @return more prefix
     */
    public String getMorePrefix()
    {
        return morePrefix;
    }

    public int getDirectiveType()
    {
        return directive.getType();
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#render(bboss.org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException,MethodInvocationException, ResourceNotFoundException, ParseErrorException
    {
        SpaceGobbling spaceGobbling = rsvc.getSpaceGobbling();
        /*
         *  normal processing
         */

        if (isDirective)
        {
            if (morePrefix.length() > 0 || spaceGobbling.compareTo(SpaceGobbling.LINES) < 0)
            {
                writer.write(prefix);
            }

            writer.write(morePrefix);

            try
            {
                rsvc.getLogContext().pushLogContext(this, new Info(getTemplateName(), getLine(), getColumn()));
                directive.render(context, writer, this);
            }
            finally
            {
                rsvc.getLogContext().popLogContext();
            }

            if (morePrefix.length() > 0 || spaceGobbling == SpaceGobbling.NONE)
            {
                writer.write(postfix);
            }
        }
        else
        {
            writer.write(prefix);
            writer.write(morePrefix);
            writer.write(rsvc.getParserConfiguration().getHashChar());
            writer.write(directiveName);
            writer.write(postfix);
        }

        return true;
    }

    /**
     *   Sets the directive name.  Used by the parser.  This keeps us from having to
     *   dig it out of the token stream and gives the parse the change to override.
     * @param str
     */
    public void setDirectiveName( String str )
    {
        directiveName = str;
    }

    /**
     *  Gets the name of this directive.
     *  @return The name of this directive.
     */
    public String getDirectiveName()
    {
        return directiveName;
    }

    @Override
    public String toString()
    {
    	return "ASTDirective [" + super.toString() + ", directiveName="
    	        + directiveName + "]";
    }

    /**
     * Returns the string "#<i>directive_name</i>(...)". Arguments literals are not rendered. This method is only
     * used for displaying the VTL stacktrace when a rendering error is encountered when runtime.log.track_location is true.
     * @return
     */
    @Override
    public String literal()
    {
        if (literal != null)
        {
            return literal;
        }
        StringBuilder builder = new StringBuilder();
        builder.append('#').append(getDirectiveName()).append("(...)");

        return literal = builder.toString();
    }

}
