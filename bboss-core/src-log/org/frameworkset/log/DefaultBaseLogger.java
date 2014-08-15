package org.frameworkset.log;

import org.apache.log4j.Logger;

/**
 * 
 * <p>Title: DefaultBaseLogger.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2009-11-16
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultBaseLogger implements BaseLogger
{
    Logger log = null;
    public DefaultBaseLogger(Logger log)
    {
        this.log = log;
    }
    public DefaultBaseLogger()
    {
        
    }
    public void logBasic(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);
        
    }

}
