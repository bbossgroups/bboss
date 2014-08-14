package org.frameworkset.spi.properties;

import java.util.Map;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.assemble.ProSet;

/**
 * 
 * <p>Title: TestProperties.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-4-17 下午03:31:53
 * @author biaoping.yin
 * @version 1.0
 */

public class TestProperties {
	private static ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/properties/properties.xml");  
    public static void testProperties()
    {
        Object class_ =  context.getObjectProperty("cluster_enable");
        boolean cluster_enable = context.getBooleanProperty("cluster_enable");
        System.out.println("cluster_enable:"+cluster_enable);
        boolean cluster_none = context.getBooleanProperty("cluster_none",true);
        System.out.println("cluster_none:"+cluster_none);
        
        String cluster_str_none = context.getProperty("cluster_str_none","cluster_str_none");
        System.out.println("cluster_str_none:"+cluster_str_none);
        
        int cluster_int_none = context.getIntProperty("cluster_int_none",100);
        System.out.println("cluster_int_none:"+cluster_int_none);
        
        int int_enable = context.getIntProperty("int_enable");
        System.out.println("int_enable:"+int_enable);
        
        String cluster_str = context.getProperty("cluster_str");
        System.out.println("cluster_str:"+cluster_str);
        
        String cluster_exception = context.getProperty("cluster_exception");
//        String[] a = new String[10];
        System.out.println(cluster_exception);
        
    }
    
    public static void testMapProperties()
    {
        ProMap map = context.getMapProperty("connection.params");
        System.out.println(map);
    }
    
    /**
     * 获取扩展属性
     */
    public static void testExtendsProperties()
    {
    	 boolean cluster_enable = context.getBooleanProperty("cluster_enable");
    	 boolean testattrboolean = context.getBooleanExtendAttribute("cluster_enable", "testattrboolean");
    	 String testattrstring = context.getStringExtendAttribute("cluster_enable", "testattrstring");
    	 int testattrint = context.getIntExtendAttribute("cluster_enable", "testattrint");
        
    }
    
    
    
    public static void testWithValueGetMapProperties()
    {
        ProMap map = context.getMapProperty("connection.params");
        Pro pro = context.getProBean("connection.params");
        Map attrs = pro.getExtendsAttributes();
        System.out.println(map);
        System.out.println("alwaysSessionAsync：" + map.getBoolean("alwaysSessionAsync"));
        System.out.println("closeTimeout：" + map.getInt("closeTimeout"));
        System.out.println("useConnectionPool String：" + map.getString("useConnectionPool"));
        System.out.println("useConnectionPool Pro object:：" + map.getPro("useConnectionPool"));
        
        /**
         * 非法获取整数
         */
        try
        {
            System.out.println("useConnectionPool int：" + map.getInt("useConnectionPool"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        /**
         * 非法获取boolean
         */
        try
        {
            System.out.println("useConnectionPool boolean：" + map.getBoolean("useConnectionPool"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public static void testListProperties()
    {
        ProList list = context.getListProperty("aaa.connection.list.params");
        System.out.println(list);
    }
    
    public static void testFreeze()
    {
        ProList list = context.getListProperty("aaa.connection.list.params");
        
        list.add(new Pro());
    }
    
    public static void testSetProperties()
    {
        ProSet set = context.getSetProperty("aaa.connection.set.params");
        System.out.println(set);
    }
    
    public static void main(String[] args)
    {
        try
        {
            testProperties();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            testMapProperties();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            testListProperties();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            testSetProperties();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            testWithValueGetMapProperties();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            testFreeze();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
