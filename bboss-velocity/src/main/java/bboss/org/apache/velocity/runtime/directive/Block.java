package bboss.org.apache.velocity.runtime.directive;

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
import bboss.org.apache.velocity.runtime.Renderable;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.node.Node;
import bboss.org.apache.velocity.util.StringBuilderWriter;
import bboss.org.apache.velocity.util.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Writer;

/**
 * Directive that puts an unrendered AST block in the context
 * under the specified key, postponing rendering until the
 * reference is used and rendered.
 *
 * @author Andrew Tetlaw
 * @author Nathan Bubna
 * @author <a href="mailto:wyla@removethis.sci.fi">Jarkko Viinamaki</a>
 * @since 1.7
 * @version $Id: Block.java 686842 2008-08-18 18:29:31Z nbubna $
 */
public abstract class Block extends Directive
{
    protected Node block;
    protected Logger log;
    protected int maxDepth;
    protected String key;

    /**
     * Return type of this directive.
     * @return type, DirectiveConstants.BLOCK or DirectiveConstants.LINE
     */
    @Override
    public int getType()
    {
        return BLOCK;
    }

    /**
     *  simple init - get the key
     *  @param rs
     *  @param context
     *  @param node
     */
    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node)
        throws TemplateInitException
    {
        super.init(rs, context, node);

        log = rsvc.getLog();

        /*
         * No checking is done. We just grab the last child node and assume
         * that it's the block!
         */
        block = node.jjtGetChild(node.jjtGetNumChildren() - 1);
    }

    /**
     * renders block directive
     * @param context
     * @param writer
     * @return success status
     */
    public boolean render(InternalContextAdapter context, Writer writer)
    {
        preRender(context);
        try
        {
            return block.render(context, writer);
        }
        catch (IOException e)
        {
            String msg = "Failed to render " + id(context) + " to writer at " +
                StringUtils.formatFileString(this);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        catch (StopCommand stop)
        {
            if (!stop.isFor(this))
            {
                throw stop;
            }
            return true;
        }
        finally
        {
            postRender(context);
        }
    }

    /**
     * Creates a string identifying the source and location of the block
     * definition, and the current template being rendered if that is
     * different.
     * @param context
     * @return id string
     */
    protected String id(InternalContextAdapter context)
    {
        StringBuilder str = new StringBuilder(100)
            .append("block $").append(key);
        if (!context.getCurrentTemplateName().equals(getTemplateName()))
        {
            str.append(" used in ").append(context.getCurrentTemplateName());
        }
        return str.toString();
    }

    /**
     * actual class placed in the context, holds the context
     * being used for the render, as well as the parent (which already holds
     * everything else we need).
     */
    public static class Reference implements Renderable
    {
        private InternalContextAdapter context;
        private Block parent;
        private int depth;

        /**
         * @param context
         * @param parent
         */
        public Reference(InternalContextAdapter context, Block parent)
        {
            this.context = context;
            this.parent = parent;
        }

        /**
         * Render the AST of this block into the writer using the context.
         * @param context
         * @param writer
         * @return  success status
         */
        @Override
        public boolean render(InternalContextAdapter context, Writer writer)
        {
            depth++;
            if (depth > parent.maxDepth)
            {
                /* this is only a debug message, as recursion can
                 * happen in quasi-innocent situations and is relatively
                 * harmless due to how we handle it here.
                 * this is more to help anyone nuts enough to intentionally
                 * use recursive block definitions and having problems
                 * pulling it off properly.
                 */
                parent.log.debug("Max recursion depth reached for {} at {}", parent.id(context), StringUtils.formatFileString(parent));
                depth--;
                return false;
            }
            else
            {
                parent.render(context, writer);
                depth--;
                return true;
            }
        }

        /**
         * Makes #if( $blockRef ) true without rendering, so long as we aren't beyond max depth.
         * @return reference value as boolean
         */
        public boolean getAsBoolean()
        {
            return depth <= parent.maxDepth;
        }

        /**
         * @return rendered string
         */
        public String toString()
        {
            Writer writer = new StringBuilderWriter();
            if (render(context, writer))
            {
                return writer.toString();
            }
            return null;
        }
    }
}
