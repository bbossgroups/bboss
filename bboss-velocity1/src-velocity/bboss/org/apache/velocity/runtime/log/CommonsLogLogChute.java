package bboss.org.apache.velocity.runtime.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bboss.org.apache.velocity.runtime.RuntimeServices;

/**
 * Redirects Velocity's LogChute messages to commons-logging.
 *
 * <p>To use, first set up commons-logging, then tell Velocity to use
 * this class for logging by adding the following to your velocity.properties:
 *
 * <code>
 * runtime.log.logsystem.class = bboss.org.apache.velocity.runtime.log.CommonsLogLogChute
 * </code>
 * </p>
 *
 * <p>You may also set this property to specify what log/name Velocity's
 * messages should be logged to (example below is default).
 * <code>
 * runtime.log.logsystem.commons.logging.name = bboss.org.apache.velocity
 * </code>
 * </p>
 * 
 * @author Nathan Bubna
 * @since 1.6
 * @version $Id: CommonsLogLogChute.java 71982 2004-02-18 20:11:07Z nbubna $
 */
public class CommonsLogLogChute implements LogChute
{

    /** Property key for specifying the name for the log instance */
    public static final String LOGCHUTE_COMMONS_LOG_NAME =
        "runtime.log.logsystem.commons.logging.name";

    /** Default name for the commons-logging instance */
    public static final String DEFAULT_LOG_NAME = "bboss.org.apache.velocity";

    
    /** the commons-logging Log instance */
    protected Logger log;


    /********** LogChute methods *************/

    public void init(RuntimeServices rs) throws Exception
    {
        String name = 
            (String)rs.getProperty(LOGCHUTE_COMMONS_LOG_NAME);
        
        if (name == null)
        {
            name = DEFAULT_LOG_NAME;
        }
        log = LoggerFactory.getLogger(name);
        log(LogChute.DEBUG_ID, "CommonsLogLogChute name is '" + name + "'");
    }

    /**
     * Send a log message from Velocity.
     */
    public void log(int level, String message)
    {
        switch (level) 
        {
            case LogChute.WARN_ID:
                log.warn(message);
                break;
            case LogChute.INFO_ID:
                log.info(message);
                break;
            case LogChute.TRACE_ID:
                log.trace(message);
                break;
            case LogChute.ERROR_ID:
                log.error(message);
                break;
            case LogChute.DEBUG_ID:
            default:
                log.debug(message);
                break;
        }
    }

    /**
     * Send a log message from Velocity with an error.
     */
    public void log(int level, String message, Throwable t)
    {
        switch (level) 
        {
            case LogChute.WARN_ID:
                log.warn(message, t);
                break;
            case LogChute.INFO_ID:
                log.info(message, t);
                break;
            case LogChute.TRACE_ID:
                log.trace(message, t);
                break;
            case LogChute.ERROR_ID:
                log.error(message, t);
                break;
            case LogChute.DEBUG_ID:
            default:
                log.debug(message, t);
                break;
        }
    }

    /**
     * Checks whether the specified log level is enabled.
     */
    public boolean isLevelEnabled(int level)
    {
        switch (level)
        {
            case LogChute.DEBUG_ID:
                return log.isDebugEnabled();
            case LogChute.INFO_ID:
                return log.isInfoEnabled();
            case LogChute.TRACE_ID:
                return log.isTraceEnabled();
            case LogChute.WARN_ID:
                return log.isWarnEnabled();
            case LogChute.ERROR_ID:
                return log.isErrorEnabled();
            default:
                return true;
        }
    }
}
