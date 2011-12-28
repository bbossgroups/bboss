package org.frameworkset.spi.remote;

public interface CallbackHandler extends java.io.Serializable{
    public void handler(CallbackContext context);

}
