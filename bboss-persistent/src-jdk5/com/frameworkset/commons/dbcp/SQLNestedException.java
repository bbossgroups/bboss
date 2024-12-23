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

package com.frameworkset.commons.dbcp;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A SQLException subclass containing another Throwable
 * 
 * @author Dirk Verbeeck
 * @version $Revision: 479137 $ $Date: 2006-11-25 08:51:48 -0700 (Sat, 25 Nov 2006) $
 */
public class SQLNestedException extends SQLException {

    /* Throwable.getCause detection as found in commons-lang */
    private static final Method THROWABLE_CAUSE_METHOD;
    static {
        Method getCauseMethod;
        try {
            getCauseMethod = Throwable.class.getMethod("getCause", (Class[]) null);
        } catch (Exception e) {
            getCauseMethod = null;
        }
        THROWABLE_CAUSE_METHOD = getCauseMethod;
    }
    
    private static boolean hasThrowableCauseMethod() {
        return THROWABLE_CAUSE_METHOD != null;
    }

    /**
     * Holds the reference to the exception or error that caused
     * this exception to be thrown.
     */
    private Throwable cause = null;

    /**
     * Constructs a new <code>SQLNestedException</code> with specified
     * detail message and nested <code>Throwable</code>.
     *
     * @param msg    the error message
     * @param cause  the exception or error that caused this exception to be
     * thrown
     */
    public SQLNestedException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
        if ((cause != null) && (DriverManager.getLogWriter() != null)) {
            DriverManager.getLogWriter().print("Caused by: ");
            cause.printStackTrace(DriverManager.getLogWriter());
        }
    }
    
    public Throwable getCause() {
        return this.cause;
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if ((cause != null) && !hasThrowableCauseMethod()) {
            s.print("Caused by: ");
            this.cause.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if ((cause != null) && !hasThrowableCauseMethod()) {
            s.print("Caused by: ");
            this.cause.printStackTrace(s);
        }
    }
}
