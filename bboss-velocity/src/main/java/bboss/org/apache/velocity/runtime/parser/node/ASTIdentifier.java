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

import bboss.org.apache.velocity.app.event.EventHandlerUtil;
import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.util.introspection.Info;
import bboss.org.apache.velocity.util.introspection.IntrospectionCacheData;
import bboss.org.apache.velocity.util.introspection.VelPropertyGet;

import java.lang.reflect.InvocationTargetException;

/**
 *  ASTIdentifier.java
 *
 *  Method support for identifiers :  $foo
 *
 *  mainly used by ASTReference
 *
 *  Introspection is now moved to 'just in time' or at render / execution
 *  time. There are many reasons why this has to be done, but the
 *  primary two are   thread safety, to remove any context-derived
 *  information from class member  variables.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class ASTIdentifier extends SimpleNode
{
    private String identifier = "";

    /**
     *  This is really immutable after the init, so keep one for this node
     */
    protected Info uberInfo;

    /**
     * Indicates if we are running in strict reference mode.
     */
    protected boolean strictRef = false;

    /**
     * @param id
     */
    public ASTIdentifier(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTIdentifier(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * Identifier getter
     * @return identifier
     */
    public String getIdentifier()
    {
        return identifier;
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
     *  simple init - don't do anything that is context specific.
     *  just get what we need from the AST, which is static.
     * @param context
     * @param data
     * @return The data object.
     * @throws TemplateInitException
     */
    @Override
    public  Object init(InternalContextAdapter context, Object data)
        throws TemplateInitException
    {
        super.init(context, data);

        identifier = rsvc.useStringInterning() ? getFirstToken().image.intern() : getFirstToken().image;

        uberInfo = new Info(getTemplateName(), getLine(), getColumn());

        strictRef = rsvc.getBoolean(RuntimeConstants.RUNTIME_REFERENCES_STRICT, false);

        saveTokenImages();
        cleanupParserAndTokens();

        return data;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#execute(java.lang.Object, bboss.org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public Object execute(Object o, InternalContextAdapter context)
        throws MethodInvocationException
    {
        try
        {
            rsvc.getLogContext().pushLogContext(this, uberInfo);

            VelPropertyGet vg = null;

            try
            {
                /*
                 *  first, see if we have this information cached.
                 */

                IntrospectionCacheData icd = context.icacheGet(this);
                Class<?> clazz = o instanceof Class<?> ? (Class<?>)o : o.getClass();

                /*
                 * if we have the cache data and the class of the object we are
                 * invoked with is the same as that in the cache, then we must
                 * be all right.  The last 'variable' is the method name, and
                 * that is fixed in the template :)
                 */

                if ( icd != null && (icd.contextData == clazz) )
                {
                    vg = (VelPropertyGet) icd.thingy;
                }
                else
                {
                    /*
                     *  otherwise, do the introspection, and cache it.  Use the
                     *  uberspector
                     */

                    vg = rsvc.getUberspect().getPropertyGet(o, identifier, uberInfo);

                    if (vg != null && vg.isCacheable())
                    {
                        icd = new IntrospectionCacheData();
                        icd.contextData = clazz;
                        icd.thingy = vg;
                        context.icachePut(this,icd);
                    }
                }
            }

            /*
             * pass through application level runtime exceptions
             */
            catch( RuntimeException e )
            {
                throw e;
            }
            catch(Exception e)
            {
                String msg = "ASTIdentifier.execute() : identifier = "+identifier;
                log.error(msg, e);
                throw new VelocityException(msg, e, rsvc.getLogContext().getStackTrace());
            }

            /*
             *  we have no getter... punt...
             */

            if (vg == null)
            {
                if (strictRef)
                {
                    throw new MethodInvocationException("Object '" + o.getClass().getName() +
                        "' does not contain property '" + identifier + "'",
                        null, rsvc.getLogContext().getStackTrace(), identifier,
                        uberInfo.getTemplateName(), uberInfo.getLine(), uberInfo.getColumn());
                }
                else
                {
                    return null;
                }
            }

            /*
             *  now try and execute.  If we get a MIE, throw that
             *  as the app wants to get these.  If not, log and punt.
             */
            try
            {
                return vg.invoke(o);
            }
            catch(InvocationTargetException ite)
            {
                /*
                 *  if we have an event cartridge, see if it wants to veto
                 *  also, let non-Exception Throwables go...
                 */

                Throwable t = ite.getTargetException();
                if (t instanceof Exception)
                {
                    try
                    {
                        return EventHandlerUtil.methodException(rsvc, context, o.getClass(), vg.getMethodName(),
                                (Exception) t, uberInfo);
                    }

                    /*
                     * If the event handler throws an exception, then wrap it
                     * in a MethodInvocationException.
                     */
                    catch( Exception e )
                    {
                        throw new MethodInvocationException(
                          "Invocation of method '" + vg.getMethodName() + "'"
                          + " in  " + o.getClass()
                          + " threw exception "
                          + ite.getTargetException().toString(),
                          ite.getTargetException(), rsvc.getLogContext().getStackTrace(), vg.getMethodName(), getTemplateName(), this.getLine(), this.getColumn());
                    }
                }
                else
                {
                    /*
                     * no event cartridge to override. Just throw
                     */

                    throw  new MethodInvocationException(
                    "Invocation of method '" + vg.getMethodName() + "'"
                    + " in  " + o.getClass()
                    + " threw exception "
                    + ite.getTargetException().toString(),
                    ite.getTargetException(), rsvc.getLogContext().getStackTrace(), vg.getMethodName(), getTemplateName(), this.getLine(), this.getColumn());


                }
            }
            catch(IllegalArgumentException iae)
            {
                return null;
            }
            /*
             * pass through application level runtime exceptions
             */
            catch( RuntimeException e )
            {
                throw e;
            }
            catch(Exception e)
            {
                String msg = "ASTIdentifier() : exception invoking method "
                            + "for identifier '" + identifier + "' in "
                            + o.getClass();
                log.error(msg, e);
                throw new VelocityException(msg, e, rsvc.getLogContext().getStackTrace());
            }
        }
        finally
        {
            rsvc.getLogContext().popLogContext();
        }
    }

    /**
     * Returns the string ".<i>identifier</i>". This method is only used for displaying the VTL stacktrace
     * when a rendering error is encountered when runtime.log.track_location is true.
     * @return
     */
    @Override
    public String literal()
    {
        if (literal != null)
        {
            return literal;
        }
        return literal = '.' + getIdentifier();
    }
}
