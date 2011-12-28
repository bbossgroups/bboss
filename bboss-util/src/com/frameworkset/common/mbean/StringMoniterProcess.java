package com.frameworkset.common.mbean;

import javax.management.ObjectName;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class StringMoniterProcess extends javax.management.monitor.StringMonitor implements StringMoniterProcessMBean{
    protected ObjectName handler;
    protected String executeMethod;
    public StringMoniterProcess() {
        super();
    }

    public static void main(String[] args) {
        StringMoniterProcess stringmoniterprocess = new StringMoniterProcess();
    }

    public void setExecutableMethodOnDiffer(ObjectName handler,
                                            String executeMethod) {
        this.handler = handler;
        this.executeMethod = executeMethod;
    }
}
