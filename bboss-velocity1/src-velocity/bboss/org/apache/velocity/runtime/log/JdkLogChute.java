package bboss.org.apache.velocity.runtime.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

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


import bboss.org.apache.velocity.runtime.RuntimeServices;

/**
 * Implementation of a simple java.util.logging LogChute.
 *
 * @author <a href="mailto:nbubna@apache.org>Nathan Bubna</a>
 * @version $Id: JdkLogChute.java 703541 2008-10-10 18:09:42Z nbubna $
 * @since 1.5
 */
public class JdkLogChute implements LogChute
{
    /** Property key for specifying the name for the logger instance */
    public static final String RUNTIME_LOG_JDK_LOGGER =
        "runtime.log.logsystem.jdk.logger";

    public static final String RUNTIME_LOG_JDK_LOGGER_LEVEL =
        "runtime.log.logsystem.jdk.logger.level";

    /** Default name for the JDK logger instance */
    public static final String DEFAULT_LOG_NAME = "bboss.org.apache.velocity";

    /**
     *
     */
    protected Logger logger = null;

    /**
     * @see bboss.org.apache.velocity.runtime.log.LogChute#init(bboss.org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs)
    {
        String name = (String)rs.getProperty(RUNTIME_LOG_JDK_LOGGER);
        if (name == null)
        {
            name = DEFAULT_LOG_NAME;
        }
        logger = LoggerFactory.getLogger(name);

//        /* get and set specified level for this logger, */
//        String lvl = rs.getString(RUNTIME_LOG_JDK_LOGGER_LEVEL);
//        if (lvl != null)
//        {
//            Level level = Level.parse(lvl);
//            logger.setLevel(level);
//            log(LogChute.DEBUG_ID, "JdkLogChute will use logger '"
//                +name+'\''+" at level '"+level+'\'');
//        }

    }

//    /**
//     * Returns the java.util.logging.Level that matches
//     * to the specified LogChute level.
//     * @param level
//     * @return The current log level of the JDK Logger.
//     */
//    protected Level getJdkLevel(int level)
//    {
//        switch (level)
//        {
//            case LogChute.WARN_ID:
//                return Level.WARNING;
//            case LogChute.INFO_ID:
//                return Level.INFO;
//            case LogChute.DEBUG_ID:
//                return Level.FINE;
//            case LogChute.TRACE_ID:
//                return Level.FINEST;
//            case LogChute.ERROR_ID:
//                return Level.SEVERE;
//            default:
//                return Level.FINER;
//        }
//    }

    /**
     * Logs messages
     *
     * @param level severity level
     * @param message complete error message
     */
    public void log(int level, String message)
    {
        log(level, message, null);
    }

    /**
     * Send a log message from Velocity along with an exception or error
     * @param level
     * @param message
     * @param t
     */
    public void log(int level, String message, Throwable t)
    {

        switch (level)
        {
            case LogChute.WARN_ID:
            	 if (t == null)
                 {
                     logger.warn( message);
                 }
                 else
                 {
                     logger.warn( message, t);
                 }
                break;
            case LogChute.INFO_ID:
            	if (t == null)
                {
                    logger.info( message);
                }
                else
                {
                    logger.info( message, t);
                }
               break;
            case LogChute.DEBUG_ID:
            	if (t == null)
                {
                    logger.debug( message);
                }
                else
                {
                    logger.debug( message, t);
                }
               break;
            case LogChute.TRACE_ID:
            	if (t == null)
                {
                    logger.trace( message);
                }
                else
                {
                    logger.trace( message, t);
                }
               break;
            case LogChute.ERROR_ID:
            	if (t == null)
                {
                    logger.error( message);
                }
                else
                {
                    logger.error( message, t);
                }
               break;
            default:
            	if (t == null)
                {
                    logger.debug( message);
                }
                else
                {
                    logger.debug( message, t);
                }
        }
    }

    /**
     * @see bboss.org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
    	switch (level)
        {
            case LogChute.WARN_ID:
            	 return logger.isWarnEnabled();
            case LogChute.INFO_ID:
            	 return logger.isInfoEnabled();
            case LogChute.DEBUG_ID:
            	return logger.isDebugEnabled();
            case LogChute.TRACE_ID:
            	return logger.isTraceEnabled();
            case LogChute.ERROR_ID:
            	return logger.isErrorEnabled();
            default:
            	return logger.isDebugEnabled();
        }
    }

}
