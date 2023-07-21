package org.frameworkset.spi.properties.includefiles;
/**
 * Copyright 2023 bboss
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

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.PropertiesContainer;
import org.frameworkset.spi.assemble.PropertiesUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2023</p>
 * @Date 2023/7/21
 * @author biaoping.yin
 * @version 1.0
 */
public class PropertiesContainerInclude {
    private Logger logger = LoggerFactory.getLogger(PropertiesContainerInclude.class);
    @Test
    public void testPropertiesContainerInclude(){
        PropertiesContainer propertiesContainer = PropertiesUtil.getPropertiesContainer("org/frameworkset/spi/properties/includefiles/applicationincludes.properties");
        logger.info("elasticEnvUser: {}",propertiesContainer.getProperty("elasticEnvUser"));
        logger.info("elasticSysProPassword: {}",propertiesContainer.getProperty("elasticSysProPassword"));

        logger.info("elasticUser: {}",propertiesContainer.getProperty("elasticUser"));
        logger.info("elasticPassword: {}",propertiesContainer.getProperty("elasticPassword"));

        logger.info("elastic: {}",propertiesContainer.getProperty("elastic"));
        logger.info("changeme: {}",propertiesContainer.getProperty("changeme"));
        logger.info("sample: {}",propertiesContainer.getProperty("sample"));//上级配置优先级高于引用文件配置

    }

    @Test
    public void testIocConfigProperies(){
        BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/properties/includefiles/configproperties.xml");

        //获取属性配置文件中的配置参数
        logger.info("elasticEnvUser: {}",context.getExternalProperty("elasticEnvUser"));
        logger.info("elasticSysProPassword: {}",context.getExternalProperty("elasticSysProPassword"));

        logger.info("elasticUser: {}",context.getExternalProperty("elasticUser"));
        logger.info("elasticPassword: {}",context.getExternalProperty("elasticPassword"));

        logger.info("elastic: {}",context.getExternalProperty("elastic"));
        logger.info("changeme: {}",context.getExternalProperty("changeme"));
        //获取ioc容器中的属性值
        logger.info("ttt: {}",context.getProperty("ttt"));
    }
}
