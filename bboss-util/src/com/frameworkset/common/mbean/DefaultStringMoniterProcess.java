package com.frameworkset.common.mbean;

import javax.management.Notification;
import javax.management.monitor.MonitorNotification;

/**
 * <p>Title: DefaultStringMoniterProcess</p>
 *
 * <p>Description: 监听管理的字符串变量值，当字符串的值发生变化后做相应的处理</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultStringMoniterProcess extends StringMoniterProcess implements
    StringMoniterProcessMBean {

    public DefaultStringMoniterProcess() {
        super();
    }

    public void sendNotification(Notification not) {
        if (not.getType().equals(
            MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED)) {
            try {
                server.invoke(handler, executeMethod, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.sendNotification(not);
    }


    public static void main(String[] args) {
        DefaultStringMoniterProcess defaultstringmoniterprocess = new
            DefaultStringMoniterProcess();
    }
}
