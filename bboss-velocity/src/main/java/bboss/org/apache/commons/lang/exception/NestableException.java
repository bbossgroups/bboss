/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bboss.org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * The base class of all exceptions which can contain other exceptions.
 *
 * It is intended to ease the debugging by carrying on the information
 * about the exception which was caught and provoked throwing the
 * current exception. Catching and rethrowing may occur multiple
 * times, and provided that all exceptions except the first one
 * are descendants of <code>NestedException</code>, when the
 * exception is finally printed out using any of the <code>
 * printStackTrace()</code> methods, the stack trace will contain
 * the information about all exceptions thrown and caught on
 * the way.
 * <p> Running the following program
 * <p><blockquote><pre>
 *  1 import bboss.org.apache.commons.lang.exception.NestableException;
 *  2
 *  3 public class Test {
 *  4     public static void main( String[] args ) {
 *  5         try {
 *  6             a();
 *  7         } catch(Exception e) {
 *  8             e.printStackTrace();
 *  9         }
 * 10      }
 * 11
 * 12      public static void a() throws Exception {
 * 13          try {
 * 14              b();
 * 15          } catch(Exception e) {
 * 16              throw new NestableException("foo", e);
 * 17          }
 * 18      }
 * 19
 * 20      public static void b() throws Exception {
 * 21          try {
 * 22              c();
 * 23          } catch(Exception e) {
 * 24              throw new NestableException("bar", e);
 * 25          }
 * 26      }
 * 27
 * 28      public static void c() throws Exception {
 * 29          throw new Exception("baz");
 * 30      }
 * 31 }
 * </pre></blockquote>
 * <p>Yields the following stack trace:
 * <p><blockquote><pre>
 * bboss.org.apache.commons.lang.exception.NestableException: foo
 *         at Test.a(Test.java:16)
 *         at Test.main(Test.java:6)
 * Caused by: bboss.org.apache.commons.lang.exception.NestableException: bar
 *         at Test.b(Test.java:24)
 *         at Test.a(Test.java:14)
 *         ... 1 more
 * Caused by: java.lang.Exception: baz
 *         at Test.c(Test.java:29)
 *         at Test.b(Test.java:22)
 *         ... 2 more
 * </pre></blockquote><br>
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author Daniel L. Rall
 * @author <a href="mailto:knielsen@apache.org">Kasper Nielsen</a>
 * @author <a href="mailto:steven@caswell.name">Steven Caswell</a>
 * @since 1.0
 * @version $Id: NestableException.java 512889 2007-02-28 18:18:20Z dlr $
 */
public class NestableException extends Exception implements Nestable {
    
    /**
     * Required for serialization support.
     * 
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * The helper instance which contains much of the code which we
     * delegate to.
     */
    protected NestableDelegate delegate = new NestableDelegate(this);

    /**
     * Holds the reference to the exception or error that caused
     * this exception to be thrown.
     */
    private Throwable cause = null;

    /**
     * Constructs a new <code>NestableException</code> without specified
     * detail message.
     */
    public NestableException() {
        super();
    }

    /**
     * Constructs a new <code>NestableException</code> with specified
     * detail message.
     *
     * @param msg The error message.
     */
    public NestableException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>NestableException</code> with specified
     * nested <code>Throwable</code>.
     *
     * @param cause the exception or error that caused this exception to be
     * thrown
     */
    public NestableException(Throwable cause) {
        super();
        this.cause = cause;
    }

    /**
     * Constructs a new <code>NestableException</code> with specified
     * detail message and nested <code>Throwable</code>.
     *
     * @param msg    the error message
     * @param cause  the exception or error that caused this exception to be
     * thrown
     */
    public NestableException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    /**
     * {@inheritDoc}
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Returns the detail message string of this throwable. If it was
     * created with a null message, returns the following:
     * (cause==null ? null : cause.toString()).
     *
     * @return String message string of the throwable
     */
    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else if (cause != null) {
            return cause.toString();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage(int index) {
        if (index == 0) {
            return super.getMessage();
        }
        return delegate.getMessage(index);
    }

    /**
     * {@inheritDoc}
     */
    public String[] getMessages() {
        return delegate.getMessages();
    }

    /**
     * {@inheritDoc}
     */
    public Throwable getThrowable(int index) {
        return delegate.getThrowable(index);
    }

    /**
     * {@inheritDoc}
     */
    public int getThrowableCount() {
        return delegate.getThrowableCount();
    }

    /**
     * {@inheritDoc}
     */
    public Throwable[] getThrowables() {
        return delegate.getThrowables();
    }

    /**
     * {@inheritDoc}
     */
    public int indexOfThrowable(Class type) {
        return delegate.indexOfThrowable(type, 0);
    }

    /**
     * {@inheritDoc}
     */
    public int indexOfThrowable(Class type, int fromIndex) {
        return delegate.indexOfThrowable(type, fromIndex);
    }

    /**
     * {@inheritDoc}
     */
    public void printStackTrace() {
        delegate.printStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    public void printStackTrace(PrintStream out) {
        delegate.printStackTrace(out);
    }

    /**
     * {@inheritDoc}
     */
    public void printStackTrace(PrintWriter out) {
        delegate.printStackTrace(out);
    }

    /**
     * {@inheritDoc}
     */
    public final void printPartialStackTrace(PrintWriter out) {
        super.printStackTrace(out);
    }

}
