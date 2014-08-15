package org.frameworkset.spi.remote;

import java.util.Map;

/**
 * 
 * <p>
 * Title: CallbackContext.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class CallbackContext implements java.io.Serializable {
    /**
     * 确定服务器回调的地址，由调用客户端确定
     */
    private Target callbackTarget;
    /**
     * 回调方法的参数，由服务器处理完事件后设置
     */
    private Map callbackParameters;

    public Target getCallbackTarget() {
        return callbackTarget;
    }

    public Map getCallbackParameters() {
        return callbackParameters;
    }

    public CallbackContext(Map callbackParameters, Target callbackTarget) {
        super();
        this.callbackParameters = callbackParameters;
        this.callbackTarget = callbackTarget;
    }

}
