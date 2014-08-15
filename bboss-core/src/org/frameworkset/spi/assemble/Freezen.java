package org.frameworkset.spi.assemble;

public abstract class Freezen
{
    private boolean isfreeze = false;
    
    public void freeze()
    {
        this.isfreeze = true;
    }
    private boolean isFreeze()
    {
        
        return this.isfreeze;
    }
    
    protected void modify() 
    {
        if(this.isFreeze())
            throw new CannotModifyException();
    }
}
