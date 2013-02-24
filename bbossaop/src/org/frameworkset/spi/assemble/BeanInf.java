package org.frameworkset.spi.assemble;

import java.util.List;

import org.frameworkset.spi.BaseApplicationContext;

public interface BeanInf
{
    Class getBeanClass();
    String getName();
    String getXpath();
    public List<Pro> getReferences();
    public Construction getConstruction();
    public List<Pro> getConstructorParams();
    public String getConfigFile();
    public void setConfigFile(String configFile);
    public String getInitMethod();
    public String getDestroyMethod();
    
    public BaseApplicationContext getApplicationContext();
    
    public String getFactory_class();
    public Class getFactoryClass();
    public String getFactory_method();
    public String getFactory_bean();
    
    public boolean isSinglable();
    
}
