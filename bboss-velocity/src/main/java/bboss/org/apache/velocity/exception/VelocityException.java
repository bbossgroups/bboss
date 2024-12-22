package bboss.org.apache.velocity.exception;

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

import bboss.org.apache.velocity.runtime.parser.LogContext;

/**
*  Base class for Velocity runtime exceptions thrown to the
 * application layer.
 *
 * @author <a href="mailto:kdowney@amberarcher.com">Kyle F. Downey</a>
 * @version $Id$
 */
public class VelocityException extends RuntimeException
{
    /**
     * Version Id for serializable
     */
    private static final long serialVersionUID = 1251243065134956045L;

    /**
     * LogContext VTL location tracking context
     */
    private LogContext logContext = null;

    /**
     * VTL vtlStackTrace, populated at construction when runtime.log.track_location is true
     */
    private String vtlStackTrace[] = null;

    /**
     * @param exceptionMessage The message to register.
     */
    public VelocityException(final String exceptionMessage)
    {
        super(exceptionMessage);
    }

    /**
     * @param exceptionMessage The message to register.
     * @param wrapped A throwable object that caused the Exception.
     * @since 1.5
     */
    public VelocityException(final String exceptionMessage, final Throwable wrapped)
    {
        super(exceptionMessage, wrapped);
    }

    /**
     * @param exceptionMessage The message to register.
     * @param wrapped A throwable object that caused the Exception.
     * @param vtlStackTrace VTL stacktrace
     * @since 2.2
     */
    public VelocityException(final String exceptionMessage, final Throwable wrapped, final String[] vtlStackTrace)
    {
        super(exceptionMessage, wrapped);
        this.vtlStackTrace = vtlStackTrace;
    }

    /**
     * @param wrapped A throwable object that caused the Exception.
     * @since 1.5
     */
    public VelocityException(final Throwable wrapped)
    {
        super(wrapped);
    }

    /**
     * @param wrapped A throwable object that caused the Exception.
     * @param vtlStackTrace VTL stacktrace
     * @since 2.2
     */
    public VelocityException(final Throwable wrapped, final String[] vtlStackTrace)
    {
        super(wrapped);
        this.vtlStackTrace = vtlStackTrace;
    }

    /**
     *  returns the wrapped Throwable that caused this
     *  MethodInvocationException to be thrown
     *
     *  @return Throwable thrown by method invocation
     *  @since 1.5
     *  @deprecated Use {@link java.lang.RuntimeException#getCause()}
     */
    public Throwable getWrappedThrowable()
    {
        return getCause();
    }

    public String[] getVtlStackTrace()
    {
        return vtlStackTrace;
    }
}
