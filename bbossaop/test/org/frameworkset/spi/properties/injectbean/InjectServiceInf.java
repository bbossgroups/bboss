package org.frameworkset.spi.properties.injectbean;

public interface InjectServiceInf
{

    public abstract int getTest_int();

    public abstract Inject getTest_inject();

    public abstract String getRefattr();

    public abstract ServiceInf getRefservice();

    public abstract ServiceInf getRefservice_direct();

}