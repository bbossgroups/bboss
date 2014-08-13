package org.frameworkset.spi.properties.injectbean;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

public class TestBean
{
	static BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/injectbean/injectbean.xml");
    public static void Ptest()
    {
        InjectServiceInf is = (InjectServiceInf)context.getBeanObject("inject.p.injectbean");
        System.out.println("is:" +is);
    }
    
    public static void Ftest()
    {
        InjectServiceInf is = (InjectServiceInf)context.getBeanObject("inject.f.injectbean");
        System.out.println("is:" +is);
    }
    
    public static void Ctest()
    {
        InjectServiceInf is = (InjectServiceInf)context.getBeanObject("inject.c.injectbean");
        System.out.println("is:" +is);
    }
    @Test
    public void testEditor0()
    {
        EditorService editorService = (EditorService)context.getBeanObject("inject.editor.injectbean.0");
        org.junit.Assert.assertEquals("男", editorService.getSex());
        System.out.println("sex:" + editorService.getSex());
    }
    @Test
    public void testEditor1()
    {
        EditorService editorService = (EditorService)context.getBeanObject("inject.editor.injectbean.1");
        org.junit.Assert.assertEquals("女", editorService.getSex());
    }
    
    @Test
    public void testEditor2()
    {
        EditorService editorService = (EditorService)context.getBeanObject("inject.editor.injectbean.2");
        System.out.println("sex:" + editorService.getSex());
        org.junit.Assert.assertEquals("未知", editorService.getSex());
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
