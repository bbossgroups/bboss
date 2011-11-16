package org.frameworkset.spi.properties.injectbean;

import org.frameworkset.spi.BaseSPIManager;
import org.junit.Test;

public class TestBean
{
    public static void Ptest()
    {
        InjectServiceInf is = (InjectServiceInf)BaseSPIManager.getBeanObject("inject.p.injectbean");
        System.out.println("is:" +is);
    }
    
    public static void Ftest()
    {
        InjectServiceInf is = (InjectServiceInf)BaseSPIManager.getBeanObject("inject.f.injectbean");
        System.out.println("is:" +is);
    }
    
    public static void Ctest()
    {
        InjectServiceInf is = (InjectServiceInf)BaseSPIManager.getBeanObject("inject.c.injectbean");
        System.out.println("is:" +is);
    }
    @Test
    public void testEditor0()
    {
        EditorService editorService = (EditorService)BaseSPIManager.getBeanObject("inject.editor.injectbean.0");
        org.junit.Assert.assertEquals("ÄÐ", editorService.getSex());
        System.out.println("sex:" + editorService.getSex());
    }
    @Test
    public void testEditor1()
    {
        EditorService editorService = (EditorService)BaseSPIManager.getBeanObject("inject.editor.injectbean.1");
        org.junit.Assert.assertEquals("Å®", editorService.getSex());
    }
    
    @Test
    public void testEditor2()
    {
        EditorService editorService = (EditorService)BaseSPIManager.getBeanObject("inject.editor.injectbean.2");
        System.out.println("sex:" + editorService.getSex());
        org.junit.Assert.assertEquals("Î´Öª", editorService.getSex());
    }
      
    
    public static void main(String[] args)
    {
        try
        {
            Ptest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            Ftest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            Ctest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }

}
