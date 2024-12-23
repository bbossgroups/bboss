package bboss.org.apache.velocity.runtime.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bboss.org.apache.velocity.runtime.RuntimeServices;

/**
 * Implementation of a simple log4j system that will either latch onto
 * an existing category, or just do a simple rolling file log.
 *
 * @author <a href="mailto:geirm@apache.org>Geir Magnusson Jr.</a>
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel L. Rall</a>
 * @author <a href="mailto:nbubna@apache.org>Nathan Bubna</a>
 * @version $Id: Log4JLogChute.java 730039 2008-12-30 03:53:19Z byron $
 * @since Velocity 1.5
 * @since 1.5
 */
public class Log4JLogChute implements LogChute
{
    public static final String RUNTIME_LOG_LOG4J_LOGGER =
            "bboss.org.apache.velocity";
    public static final String RUNTIME_LOG_LOG4J_LOGGER_LEVEL =
            "bboss.org.apache.velocity.level";

    private RuntimeServices rsvc = null;
    private boolean hasTrace = false;


    /**
     * <a href="http://jakarta.apache.org/log4j/">Log4J</a> logging API.
     */
    protected Logger logger = null;

    /**
     * @see bboss.org.apache.velocity.runtime.log.LogChute#init(bboss.org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception
    {
        rsvc = rs;

        /* first see if there is a category specified and just use that - it allows
         * the application to make us use an existing logger
         */
        String name = (String)rsvc.getProperty(RUNTIME_LOG_LOG4J_LOGGER);
        if (name != null)
        {
            logger = LoggerFactory.getLogger(name);
            log(DEBUG_ID, "Log4JLogChute using logger '" + name + '\'');
        }
        else
        {
            // create a logger with this class name to avoid conflicts
            logger = LoggerFactory.getLogger(this.getClass().getName());

//            // if we have a file property, then create a separate
//            // rolling file log for velocity messages only
//            String file = rsvc.getString(RuntimeConstants.RUNTIME_LOG);
//            if (file != null && file.length() > 0)
//            {
//                initAppender(file);
//            }
        }

        /* get and set specified level for this logger */
//        String lvl = rsvc.getString(RUNTIME_LOG_LOG4J_LOGGER_LEVEL);
//        if (lvl != null)
//        {
//            Level level = Level.valueOf(lvl);
//            logger.setLevel(level);
//        }
        
//        /* Ok, now let's see if this version of log4j supports the trace level. */
//        try
//        {
//            Field traceLevel = Level.class.getField("TRACE");
//            // we'll never get here in pre 1.2.12 log4j
//            hasTrace = true;
//        }
//        catch (NoSuchFieldException e)
//        {
//            log(DEBUG_ID,
//                "The version of log4j being used does not support the \"trace\" level.");
//        }
    }

//    // This tries to create a file appender for the specified file name.
//    private void initAppender(String file) throws Exception
//    {
//        try
//        {
//            // to add the appender
//            PatternLayout layout = new PatternLayout("%d - %m%n");
//            this.appender = new RollingFileAppender(layout, file, true);
//
//            // if we successfully created the file appender,
//            // configure it and set the logger to use only it
//            appender.setMaxBackupIndex(1);
//            appender.setMaximumFileSize(100000);
//
//            // don't inherit appenders from higher in the logger heirarchy
//            logger.setAdditivity(false);
//            logger.addAppender(appender);
//            log(DEBUG_ID, "Log4JLogChute initialized using file '"+file+'\'');
//        }
//        catch (IOException ioe)
//        {
//            rsvc.getLog().error("Could not create file appender '"+file+'\'', ioe);
//            throw ExceptionUtils.createRuntimeException("Error configuring Log4JLogChute : ", ioe);
//        }
//    }

    /**
     *  logs messages
     *
     *  @param level severity level
     *  @param message complete error message
     */
    public void log(int level, String message)
    {
        switch (level)
        {
            case LogChute.WARN_ID:
                logger.warn(message);
                break;
            case LogChute.INFO_ID:
                logger.info(message);
                break;
            case LogChute.TRACE_ID:
                if (hasTrace)
                {
                    logger.trace(message);
                }
                else
                {
                    logger.debug(message);
                }
                break;
            case LogChute.ERROR_ID:
                logger.error(message);
                break;
            case LogChute.DEBUG_ID:
            default:
                logger.debug(message);
                break;
        }
    }

    /**
     * @see bboss.org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String, java.lang.Throwable)
     */
    public void log(int level, String message, Throwable t)
    {
        switch (level)
        {
            case LogChute.WARN_ID:
                logger.warn(message, t);
                break;
            case LogChute.INFO_ID:
                logger.info(message, t);
                break;
            case LogChute.TRACE_ID:
                if (hasTrace)
                {
                    logger.trace(message, t);
                }
                else
                {
                    logger.debug(message, t);
                }
                break;
            case LogChute.ERROR_ID:
                logger.error(message, t);
                break;
            case LogChute.DEBUG_ID:
            default:
                logger.debug(message, t);
                break;
        }
    }

    /**
     * @see bboss.org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level)
    {
        switch (level)
        {
            case LogChute.DEBUG_ID:
                return logger.isDebugEnabled();
            case LogChute.INFO_ID:
                return logger.isInfoEnabled();
            case LogChute.TRACE_ID:
                if (hasTrace)
                {
                    return logger.isTraceEnabled();
                }
                else
                {
                    return logger.isDebugEnabled();
                }
            case LogChute.WARN_ID:
                return logger.isWarnEnabled();
            case LogChute.ERROR_ID:
                // can't be disabled in log4j
                return logger.isErrorEnabled();
            default:
                return true;
        }
    }

    /**
     * Also do a shutdown if the object is destroy()'d.
     * @throws Throwable
     */
    protected void finalize() throws Throwable
    {
        shutdown();
    }

    /** Close all destinations*/
    public void shutdown()
    {
        
    }

}
