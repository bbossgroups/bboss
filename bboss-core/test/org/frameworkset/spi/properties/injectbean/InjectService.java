package org.frameworkset.spi.properties.injectbean;

public class InjectService implements InjectServiceInf
{
    private int test_int;
    /* (non-Javadoc)
     * @see org.frameworkset.spi.properties.injectbean.InjectServiceInf#getTest_int()
     */
    public int getTest_int()
    {
        return test_int;
    }

    public void setTest_int(int test_int)
    {
        this.test_int = test_int;
    }

    /* (non-Javadoc)
     * @see org.frameworkset.spi.properties.injectbean.InjectServiceInf#getTest_inject()
     */
    public Inject getTest_inject()
    {
        return test_inject;
    }

    public void setTest_inject(Inject test_inject)
    {
        this.test_inject = test_inject;
    }

    /* (non-Javadoc)
     * @see org.frameworkset.spi.properties.injectbean.InjectServiceInf#getRefattr()
     */
    public String getRefattr()
    {
        return refattr;
    }

    public void setRefattr(String refattr)
    {
        this.refattr = refattr;
    }

    /* (non-Javadoc)
     * @see org.frameworkset.spi.properties.injectbean.InjectServiceInf#getRefservice()
     */
    public ServiceInf getRefservice()
    {
        return refservice;
    }

    public void setRefservice(ServiceInf refservice)
    {
        this.refservice = refservice;
    }

    /* (non-Javadoc)
     * @see org.frameworkset.spi.properties.injectbean.InjectServiceInf#getRefservice_direct()
     */
    public ServiceInf getRefservice_direct()
    {
        return refservice_direct;
    }

    public void setRefservice_direct(ServiceInf refservice_direct)
    {
        this.refservice_direct = refservice_direct;
    }

    private Inject test_inject;
    private String refattr;
    private ServiceInf refservice;
    private ServiceInf refservice_direct;
    public InjectService(int test_int, 
            Inject test_inject, 
            String refattr, 
            ServiceInf refservice,
            ServiceInf refservice_direct)
    {
        super();
        this.test_int = test_int;
        this.test_inject = test_inject;
        this.refattr = refattr;
        this.refservice = refservice;
        this.refservice_direct = refservice_direct;
    }
    public InjectService()
    {
        
    }

    @Override
    public String toString()
    {
       StringBuffer ret = new StringBuffer();
       ret.append("test_int=").append(test_int)
           .append(",test_inject=").append(test_inject)
           .append(",refattr=").append(refattr)
           .append(",refservice=").append(refservice)
           .append(",refservice_direct=").append(refservice_direct);
       return ret.toString();
    }  
    
    
    
}
