package com.frameworkset.common.mbean;

import javax.management.ObjectName;

/**
 * <p>Title: </p>
 *
 * <p>Description: By using JMX monitors to observe managed String attribute, you can design
 * MBeans to reroute messaging, reconfigure services, or start new processes</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface StringMoniterProcessMBean extends javax.management.monitor.StringMonitorMBean
{
    /**
     * @param name ObjectName 监听器监听到特定的消息时，根据消息类型可以做相应的处理
     * @param methodName String
     */
    public void setExecutableMethodOnDiffer(ObjectName handler, String executeMethod);
}
