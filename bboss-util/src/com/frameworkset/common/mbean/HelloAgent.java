package com.frameworkset.common.mbean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanInfo;

import org.apache.log4j.Logger;

//import com.sun.jdmk.comm.HtmlAdaptorServer;

public class HelloAgent extends Thread{
    private MBeanServer server = null;
    ObjectName poolObjectName = null;
    private static Logger log = Logger.getLogger(HelloAgent.class);
    public void run() {
        try
        {
            this.startMBean();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startMBean() throws Exception{
        try {
            log.debug(new Object[0].getClass().getName());
            poolObjectName = new ObjectName("BSPFServer:name=poolstrap");
        } catch (MalformedObjectNameException ex1) {
        }
//        server = ContextLoaderListener.server =
//            MBeanServerFactory.createMBeanServer("BSPFServer"); ;
//            log.debug("find mbserver from MBeanServerFactory:"+MBeanServerFactory.findMBeanServer("BSPFServer").size());

//        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
//
//        ObjectName adapterName = null;
//
//        try {
//            adapterName =
//                new ObjectName("BSPFServer:name=htmladapter,port=9097");
//
//            adapter.setPort(9097);
//            server.registerMBean(adapter, adapterName);
//            adapter.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//                log.debug(System.getProperties());
//
//        ObjectName loaderName = new ObjectName(
//            "BSPFServer:service=JMXPatchedClassLoader");
//        /**
//         * className - The class name of the MBean to be instantiated.
//         * name - The object name of the MBean. May be null.
//         * params - An array containing the parameters of the constructor to be invoked.
//         * signature - An array containing the signature of the constructor to be invoked.
//         */
////        server.createMBean(
////            "com.frameworkset.common.poolman.management.JMXClassLoader",
////            loaderName, new Object[] {this.getClass().getClassLoader()},
////            new String[] {"java.lang.ClassLoader"});
//
//        log.debug("initDatasource in server:" + server);
//        ObjectInstance poolstrap = null;
//        try {
//            poolstrap = server.getObjectInstance(poolObjectName);
//        } catch (InstanceNotFoundException ex) {
//            //ex.printStackTrace();
//            log.debug(
//                "MBeanServer cannot find MBean with ObjectName DefaultDomain:name=poolstrap");
//        }
//        if (poolstrap == null) {
//            ModelMBeanInfoBuilder mbeanInfoBuilder = new ModelMBeanInfoBuilder();
//
//            ModelMBeanInfoBuilder builder = new ModelMBeanInfoBuilder();
////            Descriptor attDesc =
////                builder.buildAttributeDescriptor("MyAttribute",
////                                                 null, "always", "10", null,
////                                                 "getMyAttribute",
////                                                 null, "10");
////            builder.addModelMBeanAttribute("MyAttribute",
////                                           "java.lang.String",
////                                           true, false, false, "", attDesc);
//            //定义PoolManBootstrap start 方法的描述信息
////            Class clazz = com.frameworkset.common.poolman.management.
////                          PoolManBootstrap.class;
//            Class clazz = null;
//            //Descriptor constructDesc = builder.buildOperationDescriptor("PoolmanBootstrap","","constructor",null,null,"com.frameworkset.common.poolman.management.PoolManBootstrap","10");
//            //builder.addModelMBeanConstructor(clazz.getDeclaredConstructor(null),"PoolManBootstrap",constructDesc);
//            builder.addModelMBeanConstructor(clazz.getDeclaredConstructor(null),
//                                             "PoolManBootstrap");
//            //Descriptor constructDesc1 = builder.buildOperationDescriptor("PoolmanBootstrap","","constructor",null,null,"com.frameworkset.common.poolman.management.PoolManBootstrap","10");
//            //builder.addModelMBeanConstructor(clazz.getDeclaredConstructor(new Class[] {String.class}),"PoolManBootstrap",constructDesc);
//            builder.addModelMBeanConstructor(clazz.getDeclaredConstructor(new
//                Class[] {String.class}), "PoolManBootstrap");
////            Descriptor startDesc = builder.buildOperationDescriptor(
////                "start", null, "operation", null, null,
////                "com.frameworkset.common.poolman.management.PoolManBootstrap",
////                "10");
////            builder.addModelMBeanMethod("start", null,
////                                        null, null, "启动poolman",
////                                        "java.lang.String",
////                                        MBeanOperationInfo.ACTION,
////                                        startDesc);
//
//
//            //定义PoolManBootstrap stop 方法的描述信息
//            Descriptor stopParamDesc = builder.buildOperationDescriptor(
//                "stop",
//                null, "operation", null, null,
//                "com.frameworkset.common.poolman.management.PoolManBootstrap",
//                "10");
//            builder.addModelMBeanMethod("stop", null,
//                                        null, null, "",
//                                        "void", MBeanOperationInfo.ACTION,
//                                        stopParamDesc);
//            Descriptor startDesc = builder.buildOperationDescriptor(
//                "start", null, "operation", null, null,
//                "com.frameworkset.common.poolman.management.PoolManBootstrap",
//                "10");
//            builder.addModelMBeanMethod("start", null,
//                                        null, null, "poolman",
//                                        "void",
//                                        MBeanOperationInfo.ACTION,
//                                        startDesc);
//            //定义PoolManBootstrap reStart 方法的描述信息
//            Descriptor reStartParamDesc = builder.buildOperationDescriptor(
//                "reStart",
//                null, "operation", null, null,
//                "com.frameworkset.common.poolman.management.PoolManBootstrap",
//                "10");
//            builder.addModelMBeanMethod("reStart", new String[] {"java.lang.String"},
//                                        new String[] {"configFile"},
//                                        new String[] {
//                                        "the path of config file 'poolman.xml'"},
//                                        "poolman",
//                                        "void", MBeanOperationInfo.ACTION,
//                                        reStartParamDesc);
//            //定义PoolManBootstrap start With Param 方法的描述信息
//            Descriptor startWithParamDesc = builder.buildOperationDescriptor(
//                "start",
//                null, "operation", null, null,
//                "com.frameworkset.common.poolman.management.PoolManBootstrap",
//                "10");
//
//            builder.addModelMBeanMethod("start", new String[] {"java.lang.String"},
//                                        new String[] {"configFile"},
//                                        new String[] {
//                                        "the path of config file poolman.xml"},
//                                        "poolman",
//                                        "void", MBeanOperationInfo.ACTION,
//                                        startWithParamDesc);
//
//
//
//            Descriptor mbeanDesc = builder.buildMBeanDescriptor(
//                "modeledClass",
//                "", "OnUpdate", "10", ".", "ModeledClass",
//                "T", "d:/jmx.log");
//            ModelMBeanInfo info =
//                builder.buildModelMBeanInfo(mbeanDesc);
//            //RmiConnectorClient client = RMIClientFactory.getClient();
//            //ObjectName mName = new ObjectName("JMXBookAgent:name=Modeled");
//
////            server.createMBean(
////                "org.apache.commons.modeler.BaseModelMBean",
////                poolObjectName);
//
//            ObjectInstance poolman = server.createMBean(
//                "javax.management.modelmbean.RequiredModelMBean",
//                //poolObjectName, loaderName);
//                poolObjectName);
//
//            log.debug("poolman bootstrap registed:" + poolman);
//            String[] sig = {"java.lang.Object", "java.lang.String"};
////            Object[] params = {new com.frameworkset.common.poolman.management.
////                              PoolManBootstrap(), "ObjectReference"};
//            
//            Object[] params = {null, "ObjectReference"};
//            server.invoke(poolObjectName, "setManagedResource", params, sig);
//            sig = new String[1];
//            sig[0] = "javax.management.modelmbean.ModelMBeanInfo";
//            params = new Object[1];
//            params[0] = info;
//            server.invoke(poolObjectName, "setModelMBeanInfo", params, sig);
//            MBeanInfo mbeanInfo = server.getMBeanInfo(poolman.getObjectName());
//            log.info("mbeanInfo:" + mbeanInfo.toString());
//            //invoke(String s, Object aobj[], String as[])
//
//            server.invoke(poolObjectName, "start", new Object[0], null);
//        }
    }

    public HelloAgent() {

    }

    public static void main(String args[]) {
        System.out.println("HelloAgent is running");
        HelloAgent agent = new HelloAgent();
        agent.start();
    }
}
