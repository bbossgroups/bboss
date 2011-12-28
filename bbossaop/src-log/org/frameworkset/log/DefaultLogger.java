package org.frameworkset.log;






public class DefaultLogger extends Logger
{
    org.apache.log4j.Logger log = null;
    public DefaultLogger(org.apache.log4j.Logger log)
    {
        this.log = log;
    }
    public DefaultLogger()
    {
        
    }
    @Override
    public void logBasic(String log)
    {
        System.out.println(log);

    }

    @Override
    public void logBasic(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

    @Override
    public void logDebug(String log)
    {
        if(this.log != null)
            this.log.debug(log);
        else
            System.out.println(log);

    }

    @Override
    public void logDebug(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

    @Override
    public void logDetailed(String log)
    {
        if(this.log != null)
            this.log.debug(log);
        else
            System.out.println(log);

    }

    @Override
    public void logDetailed(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

    @Override
    public void logError(String log)
    {
        if(this.log != null)
            this.log.debug(log);
        else
            System.out.println(log);

    }

    @Override
    public void logError(String log, Throwable e)
    {
        if(this.log != null)
            this.log.debug(log);
        else
            System.out.println(log);

    }

    @Override
    public void logError(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

    @Override
    public void logError(String subject, String log, Throwable e)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

    @Override
    public void logMinimal(String log)
    {
        if(this.log != null)
            this.log.debug(log);
        else
            System.out.println(log);

    }

    @Override
    public void logMinimal(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

    @Override
    public void logRowlevel(String log)
    {
        if(this.log != null)
            this.log.debug( log);
        else
            System.out.println(log);

    }

    @Override
    public void logRowlevel(String subject, String log)
    {
        if(this.log != null)
            this.log.debug(subject + "--" + log);
        else
            System.out.println(subject + "--" + log);

    }

}
