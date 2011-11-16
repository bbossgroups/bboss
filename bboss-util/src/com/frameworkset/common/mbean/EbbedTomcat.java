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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.loading.MLet;

import org.apache.commons.modeler.Registry;



public class EbbedTomcat {
    public static final String[] mbeanDescriptorXml = {
            "org/apache/catalina/mbeans/mbeans-descriptors.xml",
            "org/apache/catalina/loader/mbeans-descriptors.xml"
    };

    // components of Tomcat are listed below
    public static String DEFAULT_DOMAIN = "Catalina";
    public static String TOMCAT_HOME = "D:\\BSPF\\1_Develop\\3_GrpDevelop\\2_Integration\\1_SrcCode\\bsplatform\\tomcat";
    //--- server
    public static String server_name = DEFAULT_DOMAIN + ":type=Server";
    public static String server_code = "org.apache.catalina.core.StandardServer";
    public static String server_attr[] = {"port", "9005"};

    //--- service
    public static String service_name = DEFAULT_DOMAIN + ":type=Service";
    public static String service_code = "org.apache.catalina.core.StandardService";
    public static String service_attr[] = {"name", "Tomcat-Standalone"};
    //--- engine
    public static String engine_name = DEFAULT_DOMAIN + ":type=Engine";
    public static String engine_code = "org.apache.catalina.core.StandardEngine";
    public static String engine_attr[] = {
            "name" , "Tomcat-Standalone",
            "baseDir", TOMCAT_HOME,
            "defaultHost" , "localhost"
    };
    //--- realm
    public static String realm_name = DEFAULT_DOMAIN + ":type=Realm";
    public static String realm_code = "org.apache.catalina.realm.MemoryRealm";
    public static String realm_attr[] = { "pathname", TOMCAT_HOME + "/conf/tomcat-users.xml"};
    //--- channel between Apache httpd is ignored here

    //--- connector
    public static String connector_name = DEFAULT_DOMAIN + ":type=Connector,port=9080";
    public static String connector_code = "org.apache.coyote.tomcat5.CoyoteConnector";
    public static String connector_attr[] = {"port", "9080"};
    //--- host
    public static String host_name = DEFAULT_DOMAIN + ":type=Host,host=localhost";
    public static String host_code = "org.apache.catalina.core.StandardHost";
    public static String host_attr[] = {"name", "localhost"};

    //--- WebModule
    public static String wm_code = "org.apache.catalina.core.StandardContext";	//common for each webapp
    public static String WEBAPPS_BASE = TOMCAT_HOME + "\\webapps";

    //----- ROOT
    public static String wm_r00t_name = DEFAULT_DOMAIN +
        ":j2eeType=WebModule,name=//localhost/,J2EEApplication=none,J2EEServer=none";
    public static String wm_r00t_attr[] = {
            "docBase", WEBAPPS_BASE + "/ROOT",
            "privileged" , "true",
            "engineName", DEFAULT_DOMAIN
    };
    //----- manager
    //------- I don't authenticate the webapp here,so I don't include realm defination for it
    //------- manager webapp can NOT be used yet...
    public static String wm_mgr_name = DEFAULT_DOMAIN +
        ":j2eeType=WebModule,name=//localhost/manager,J2EEApplication=none,J2EEServer=none";
    public static String wm_mgr_attr[] = {
            "docBase" , WEBAPPS_BASE + "/manager",
            "engineName", DEFAULT_DOMAIN,
            "privileged" , "true"
    };

    Registry registry = null;
    MBeanServer server = null;
    List onames = new ArrayList();

    public static void main(String[] args) {
        EbbedTomcat tomcat = new EbbedTomcat();
        tomcat.start();
    }

    public void start(){
        createMBeanRegistry();
        getMBeanServer();
        createMBeans();
        setMBeansAttr();
        startChaseCat();
    }

    private void startChaseCat(){
        try{
        registry.invoke(onames, "init", false);
        registry.invoke(onames, "start", false);
        }catch(Exception ex){
            System.err.println("Damn, I lose at last step!!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void setMBeanAttribute(String objectName, String[] attrs) {
        // initial MBean attribute can be set here according to
        // the MBean descriptor
        try {
            ObjectName oname=new ObjectName(objectName);
            for(int i=0; i< attrs.length; i+=2){
                String type = registry.getType(oname, attrs[i]);
                Object objValue=registry.convertValue(type, attrs[i+1]);
                server.setAttribute(oname, new Attribute(attrs[i],objValue));
            }
            System.out.println("Set MBean " + objectName + " attributes ok");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setMBeansAttr(){
        setMBeanAttribute(server_name, server_attr);
        setMBeanAttribute(service_name, service_attr);
        setMBeanAttribute(engine_name, engine_attr);
        setMBeanAttribute(realm_name, realm_attr);
        setMBeanAttribute(connector_name, connector_attr);
        setMBeanAttribute(host_name, host_attr);
        setMBeanAttribute(wm_r00t_name, wm_r00t_attr);
        setMBeanAttribute(wm_mgr_name, wm_mgr_attr);
    }
    // iterate all Tomcat MBeans and bind and configure
    private void createMBeans(){
        try{
            bindJmx(server_name, server_code, null, null);
            bindJmx(service_name, service_code, null, null);
            bindJmx(engine_name, engine_code, null, null);
            bindJmx(realm_name, realm_code, null, null);
            bindJmx(connector_name, connector_code, null, null);
            bindJmx(host_name, host_code, null, null);
            bindJmx(wm_r00t_name, wm_code, null, null);
            bindJmx(wm_mgr_name, wm_code, null ,null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // load MBeans descriptor into registry
    private void createMBeanRegistry(){
        try{
            URL url = null;
            //registry = Registry.getRegistry(null, null);
            for(int i=0; i< mbeanDescriptorXml.length; i++){
                url = this.getClass().getClassLoader().getResource(mbeanDescriptorXml[i]);
                Registry.getRegistry(null, null).loadDescriptors(url);
            }
            registry = Registry.getRegistry(null, null);
            System.out.println("Init registry ok");
        }catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    // steal from org.apache.commons.modeler.ant.MLETTask
    private void bindJmx(String objectName, String code, String arg0,
            List args) throws Exception {

        ObjectName oname = new ObjectName(objectName);
        onames.add(oname);

        Arg codeArg = new Arg();
        codeArg.setType("java.lang.String");
        codeArg.setValue(code);
        if (args == null)
            args = new ArrayList();
        args.add(0, codeArg);
        code = "org.apache.commons.modeler.BaseModelMBean";

        Object argsA[]=new Object[ args.size()];
        String sigA[]=new String[args.size()];
        for( int i=0; i<args.size(); i++ ) {
            Arg arg=(Arg)args.get(i);
            if( arg.type==null )
                arg.type="java.lang.String";
            sigA[i]=arg.getType();
            argsA[i]=arg.getValue();
            // XXX Deal with not string types - IntrospectionUtils
        }

        if( args.size()==0 ) {
            server.createMBean(code, oname);
        } else {
            server.createMBean(code, oname, argsA, sigA );
        }

        System.out.println("Bind " + objectName + " ok");
    }

    private void getMBeanServer() {
        server = registry.getMBeanServer();

        try {
            // Register a loader that will be find ant classes.
            ObjectName defaultLoader = new ObjectName("I", "love", "imps");
            MLet mlet = new MLet(new URL[0], this.getClass().getClassLoader());
            server.registerMBean(mlet, defaultLoader);
        } catch (JMException ex) {
            ex.printStackTrace();
        }
    }

    class Arg {
        String type;
        String value;

        public void setType( String type) {
            this.type=type;
        }
        public void setValue( String value ) {
            this.value=value;
        }
        public void addText( String text ) {
            this.value=text;
        }

        public String getValue() {
            return value;
        }

        public String getType() {
            return type;
        }
    }
}
