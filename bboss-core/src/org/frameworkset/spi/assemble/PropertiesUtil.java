package org.frameworkset.spi.assemble;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.plugin.PropertiesFilePlugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: 获取默认属性配置容器</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/8/1 21:15
 * @author biaoping.yin
 * @version 1.0
 */
public class PropertiesUtil {
    private static Map<String, PropertiesContainer> propertiesContainerMap = new ConcurrentHashMap<>();

    private static Map<String, PropertiesContainer> propertiesApolloContainerMap = new ConcurrentHashMap<>();

    private static Map<String, PropertiesContainer> propertiesNacosContainerMap = new ConcurrentHashMap<>();
    private static Map<String, PropertiesContainer> propertiesPluginContainerMap = new ConcurrentHashMap<>();
    public static PropertiesContainer getPropertiesContainer() {

        return getPropertiesContainer("application.properties");
    }

    public static PropertiesContainer getPropertiesContainer(String propertiesFile) {
        PropertiesContainer propertiesContainer = propertiesContainerMap.get(propertiesFile);
        if (propertiesContainer != null) {
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesContainerMap.get(propertiesFile);
            if (propertiesContainer != null) {
                return propertiesContainer;
            }

            propertiesContainer = new PropertiesContainer();
            propertiesContainer.addConfigPropertiesFile(propertiesFile);
            propertiesContainerMap.put(propertiesFile, propertiesContainer);
        }
        return propertiesContainer;
    }

    public static PropertiesContainer getPropertiesContainer(String propertiesFile, LinkConfigFile linkfile) {
        PropertiesContainer propertiesContainer = propertiesContainerMap.get(propertiesFile);
        if (propertiesContainer != null) {
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesContainerMap.get(propertiesFile);
            if (propertiesContainer != null) {
                return propertiesContainer;
            }

            propertiesContainer = new PropertiesContainer();
            propertiesContainer.addConfigPropertiesFile(propertiesFile, linkfile);
            propertiesContainerMap.put(propertiesFile, propertiesContainer);
        }
        return propertiesContainer;
    }


    public static PropertiesContainer getPropertiesContainerFromPlugin(String configPropertiesPlugin, LinkConfigFile linkfile, BaseApplicationContext applicationContext, Map<String, String> extendsAttributes){
        PropertiesContainer propertiesContainer = propertiesPluginContainerMap.get(configPropertiesPlugin);
        if (propertiesContainer != null) {
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesPluginContainerMap.get(configPropertiesPlugin);
            if (propertiesContainer != null) {
                return propertiesContainer;
            }
            try {
                Class clazz = Class.forName(configPropertiesPlugin.trim());
                PropertiesFilePlugin propertiesFilePlugin = (PropertiesFilePlugin) clazz.newInstance();
                int initType = propertiesFilePlugin.getInitType(applicationContext, extendsAttributes, null);
                if (initType != 1) {//采用文件模式
                    String configPropertiesFile = propertiesFilePlugin.getFiles(applicationContext, extendsAttributes, null);
                    if (SimpleStringUtil.isNotEmpty(configPropertiesFile)) {
                        propertiesContainer = getPropertiesContainer(configPropertiesFile, linkfile);
                        propertiesPluginContainerMap.put(configPropertiesPlugin, propertiesContainer);
                        return propertiesContainer;
                    } else {
                        return getPropertiesContainerFromPlugin(propertiesFilePlugin, configPropertiesPlugin, linkfile, applicationContext, extendsAttributes);
                    }
                } else {
                    return getPropertiesContainerFromPlugin(propertiesFilePlugin, configPropertiesPlugin, linkfile, applicationContext, extendsAttributes);
                }
            } catch (Exception e) {
                throw new AssembleException("configPropertiesPlugin:" + configPropertiesPlugin, e);
            }
        }

    }

    public static PropertiesContainer getPropertiesContainerFromPlugin(PropertiesFilePlugin propertiesFilePlugin, String configPropertiesPlugin,LinkConfigFile linkfile, BaseApplicationContext applicationContext, Map<String, String> extendsAttributes){


            PropertiesContainer propertiesContainer = propertiesPluginContainerMap.get(configPropertiesPlugin);
            if (propertiesContainer != null) {
                return propertiesContainer;
            }
            synchronized (PropertiesUtil.class) {
                propertiesContainer = propertiesPluginContainerMap.get(configPropertiesPlugin);
                if (propertiesContainer != null) {
                    return propertiesContainer;
                }

                propertiesContainer = new PropertiesContainer();
                propertiesContainer.addConfigPropertiesFromPlugin(propertiesFilePlugin, linkfile, applicationContext, extendsAttributes);
                propertiesPluginContainerMap.put(configPropertiesPlugin, propertiesContainer);
            }
            return propertiesContainer;


    }
    public static PropertiesContainer getPropertiesContainerFromApollo(String propertiesFile, LinkConfigFile linkfile, BaseApplicationContext applicationContext, Map<String,String> extendsAttributes){
        PropertiesContainer propertiesContainer = propertiesApolloContainerMap.get(propertiesFile);
        if(propertiesContainer != null){
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesApolloContainerMap.get(propertiesFile);
            if(propertiesContainer != null){
                return propertiesContainer;
            }

            propertiesContainer = new PropertiesContainer();
            propertiesContainer.addConfigPropertiesFromApollo(linkfile,applicationContext,extendsAttributes);
            propertiesApolloContainerMap.put(propertiesFile,propertiesContainer);
        }
        return propertiesContainer;
    }

    public static PropertiesContainer getPropertiesContainerFromApollo(String propertiesFile){
        PropertiesContainer propertiesContainer = propertiesApolloContainerMap.get(propertiesFile);
        if(propertiesContainer != null){
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesApolloContainerMap.get(propertiesFile);
            if(propertiesContainer != null){
                return propertiesContainer;
            }

            propertiesContainer = new PropertiesContainer();
            propertiesContainer.addConfigPropertiesFromApollo(propertiesFile);
            propertiesApolloContainerMap.put(propertiesFile,propertiesContainer);
        }
        return propertiesContainer;
    }

    private static String buildNacosKey(String propertiesFile,  String  serverAddr,
                                        String dataId,  String group){
        StringBuilder builder = new StringBuilder();
        builder.append("nacos:").append(propertiesFile).append("^").append(serverAddr)
                .append("^").append(dataId)
                .append("^").append(group);
        return builder.toString();
    }
    public static PropertiesContainer getPropertiesContainerFromNacos(String propertiesFile,  String  serverAddr,
                                                                      String dataId,  String group,LinkConfigFile linkfile, BaseApplicationContext applicationContext, Map<String,String> extendsAttributes){
        String key = buildNacosKey(  propertiesFile,     serverAddr,
                  dataId,    group);
        PropertiesContainer propertiesContainer = propertiesNacosContainerMap.get(key);
        if(propertiesContainer != null){
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesNacosContainerMap.get(key);
            if(propertiesContainer != null){
                return propertiesContainer;
            }

            propertiesContainer = new PropertiesContainer();
            propertiesContainer.addConfigPropertiesFromNacos(linkfile,applicationContext,extendsAttributes);
            propertiesNacosContainerMap.put(key,propertiesContainer);
        }
        return propertiesContainer;
    }

    public static PropertiesContainer getPropertiesContainerFromNacos(String propertiesFile,  String  serverAddr,
                                                                      String dataId,  String group, long timeOut){
        String key = buildNacosKey(  propertiesFile,     serverAddr,
                dataId,    group);
        PropertiesContainer propertiesContainer = propertiesNacosContainerMap.get(key);
        if(propertiesContainer != null){
            return propertiesContainer;
        }
        synchronized (PropertiesUtil.class) {
            propertiesContainer = propertiesNacosContainerMap.get(key);
            if(propertiesContainer != null){
                return propertiesContainer;
            }

            propertiesContainer = new PropertiesContainer();
            propertiesContainer.addConfigPropertiesFromNacos(  propertiesFile,     serverAddr,
                      dataId,    group,   timeOut);
            propertiesNacosContainerMap.put(key,propertiesContainer);
        }
        return propertiesContainer;
    }

}
