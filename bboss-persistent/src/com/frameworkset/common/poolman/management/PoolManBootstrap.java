/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" bboss persistent,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.poolman.management;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.orm.adapter.DBFactory;


/**
 * If SQLManager is invoked (which happens when DataSources, Drivers, etc. are requested)
 * and not yet instantiated, it will make a call to this class to bootstrap the config
 * and pool deployment process. This class creates a PoolManConfiguration object, which
 * contains data from the poolman.xml file, and then passes that config object to
 * the appropriate deployer depending on whether the config shows that JMX will be used
 * or whether simple local metadata will be used to deploy and manage pools.
 */

//public class PoolManBootstrap extends NotificationBroadcasterSupport implements PoolManBootstrapMBean,javax.management.MBeanRegistration{
public class PoolManBootstrap implements java.io.Serializable{
    private static Logger log = Logger.getLogger(PoolManBootstrap.class);
    private String configFile = PoolManConstants.XML_CONFIG_FILE;
    private static Map<String,Boolean> startedFile = new HashMap<String,Boolean>();
    public static void main(String[] args) throws Exception {
        if ((args == null) || (args.length == 0)) {
            PoolManBootstrap inc = new PoolManBootstrap();
            inc.start();
        }
        else if (args.length == 1) {
            new PoolManBootstrap(args[0]).start();
        }
        else {
            System.out.println("SYNTAX: java -Djava.security.policy=" + "[your policy file] com.frameworkset.common.poolman.management." + "PoolManBootstrap [optional: config file name]");
            System.exit(1);
        }
    }

    public PoolManBootstrap()
 {
    log.debug("construct poolmanbootstrap");
        //this(PoolManConstants.XML_CONFIG_FILE);
}


    /**
     *
     * @param configFile String
     * @param startFromAppServer boolean
     * @throws Exception
     */
    public PoolManBootstrap(String configFile) {

        this.configFile = configFile;
    }


//
//    public void postDeregister() {
//    }
//
//    public void postRegister(Boolean registedSuccess) {
//    }
//
//    public void preDeregister() throws Exception {
//    }
//
//    public ObjectName preRegister(MBeanServer mBeanServer,
//                                  ObjectName objectName) throws Exception {
//        this.mBeanServer = mBeanServer;
//        return null;
//    }

    public void reStart(String configFile) throws Exception{
    }

    public void start() {
        try
        {
            start(configFile);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }
    }
   
    public static void startFromTemplte(Map<String,String> values) {
    	log.debug("PoolManBootstrap(configFile): " + PoolManConstants.XML_CONFIG_FILE_TEMPLATE);
        PoolManConfiguration config = new PoolManConfiguration(PoolManConstants.XML_CONFIG_FILE_TEMPLATE,null);
        try {
            config.loadConfiguration(values);
        } catch (Exception ex) {
            log.error("Start(configFile) loadConfiguration error: " + ex.getMessage());
            //throw ex;
        }

        PoolManDeployer deployer;

        if (config.isUsingJMX()) {
            deployer = new JMXPoolDeployer((MBeanServer)null);
            try {
                deployer.deployConfiguration(config);
            } catch (Exception ex1) {
                log.error("Start(configFile) deployConfiguration error: " + ex1.getMessage());
                //throw ex1;
            }
        }

        else {
        	DBFactory.addDBAdaptors(config.getAdaptors());
            deployer = new LocalPoolDeployer();
            try {
//                deployer.deployConfiguration(config, values);
            	deployer.deployConfiguration(config, (Map<String,String>)null);
            } catch (Exception ex2) {
                log.error("LocalPoolDeployer deployConfiguration error: " + ex2.getMessage(),ex2);
                //throw ex2;
            }
        }
        //初始化主键生成机制
        if(deployer != null)
        {
            try
            {
                ((BaseTableManager)deployer).initTableInfo(values.get("dbname"));
            }
            catch(Exception e)
            {
                log.error("InitTableInfo: " + e.getMessage(),e);
                log.debug("Initial tableinfo failed!");
                //throw e;
            }
        }

       
        com.frameworkset.common.poolman.sql.PoolMan.STARTED = true;
    }
    
    public void start(String configFile) {
        log.debug("PoolManBootstrap(configFile): " + configFile);
        PoolManConfiguration config = new PoolManConfiguration(configFile,null);
        try {
            config.loadConfiguration(null);
        } catch (Exception ex) {
            log.error("Start(configFile) loadConfiguration error: " + ex.getMessage(),ex);
            //throw ex;
        }

        PoolManDeployer deployer;

        if (config.isUsingJMX()) {
            deployer = new JMXPoolDeployer((MBeanServer)null);
            try {
                deployer.deployConfiguration(config);
            } catch (Exception ex1) {
                log.error("Start(configFile) deployConfiguration error: " + ex1.getMessage(),ex1);
                //throw ex1;
            }
        }

        else {
        	DBFactory.addDBAdaptors(config.getAdaptors());
            deployer = new LocalPoolDeployer();
            try {
                deployer.deployConfiguration(config);
            } catch (Exception ex2) {
                log.error("LocalPoolDeployer deployConfiguration error: " + ex2.getMessage(),ex2);
                //throw ex2;
            }
        }
        //初始化主键生成机制
        if(deployer != null)
        {
            try
            {
                ((BaseTableManager)deployer).initTableInfo();
            }
            catch(Exception e)
            {
                log.error("InitTableInfo: " + e.getMessage(),e);
                log.debug("Initial tableinfo failed!");
                //throw e;
            }
        }

      
        // SET STATUS
        com.frameworkset.common.poolman.sql.PoolMan.STARTED = true;


    }
    
    /**
     * 直接从配置文件启动数据库连接池，指定每个库的dbnamespace，用来避免数据库名称的冲突
     * 一旦指定的了dbnamespace，那么原来configfile中指定的dbname就回自动在前面追加dbnamespace.前缀
     * 例如：
     * 	
     * @param configFile
     * @param dbnamespace
     */
    public static void startDBSFromConf(String configFile,String dbnamespace,String[] startdbnames) {
        log.debug("PoolManBootstrap(configFile): " + configFile);
        PoolManConfiguration config = new PoolManConfiguration(configFile,dbnamespace,startdbnames);
        try {
            config.loadConfiguration(null);
        } catch (Exception ex) {
            log.error("Start(configFile) loadConfiguration error: " + ex.getMessage(),ex);
            //throw ex;
        }

        PoolManDeployer deployer;

        if (config.isUsingJMX()) {
            deployer = new JMXPoolDeployer((MBeanServer)null);
            try {
                deployer.deployConfiguration(config);
            } catch (Exception ex1) {
                log.error("Start(configFile) deployConfiguration error: " + ex1.getMessage(),ex1);
                //throw ex1;
            }
        }

        else {
        	DBFactory.addDBAdaptors(config.getAdaptors());
            deployer = new LocalPoolDeployer();
            try {
                deployer.deployConfiguration(config);
            } catch (Exception ex2) {
                log.error("LocalPoolDeployer deployConfiguration error: " + ex2.getMessage(),ex2);
                //throw ex2;
            }
        }
        //初始化主键生成机制
        if(deployer != null)
        {
            try
            {
                ((BaseTableManager)deployer).initTableInfo();
            }
            catch(Exception e)
            {
                log.error("InitTableInfo: " + e.getMessage(),e);
                log.debug("Initial tableinfo failed!");
                //throw e;
            }
        }

      
        // SET STATUS
        com.frameworkset.common.poolman.sql.PoolMan.STARTED = true;


    }
    
    /**
     * 读取配置文件启动特定的数据库
     * @param configFile
     * @param dbname
     * @throws Exception 
     */
    public void startDB(String dbname) throws Exception {
        log.debug("PoolManBootstrap(configFile): " + configFile);
        if(dbname == null || dbname.equals(""))
        	dbname = SQLUtil.getSQLManager().getDefaultDBName();
        PoolManConfiguration config = new PoolManConfiguration(configFile,dbname);
        try {
            config.loadConfiguration(null);
        } catch (Exception ex) {
//            log.error("Start(configFile) loadConfiguration error: " + ex.getMessage());
            throw ex;
        }

        PoolManDeployer deployer = new LocalPoolDeployer();
            try {
                deployer.deployConfiguration(config,dbname);
            } catch (Exception ex2) {
//                log.error("LocalPoolDeployer deployConfiguration error: " + ex2.getMessage(),ex2);
                throw ex2;
            }
       
        //初始化主键生成机制
        if(deployer != null)
        {
            try
            {
                ((BaseTableManager)deployer).initTableInfo(dbname);
            }
            catch(Exception e)
            {
                log.error("InitTableInfo: " + e.getMessage(),e);
                log.debug("Initial tableinfo failed!");
                throw e;
            }
        }

        


    }

    public void stop() {

    }

}



