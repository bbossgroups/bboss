package org.frameworkset.util;
/**
 * Copyright 2026 bboss
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

import com.frameworkset.util.JsonUtil;
import org.frameworkset.spi.ai.model.FunctionToolDefine;
import org.frameworkset.spi.ai.model.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author biaoping.yin
 * @Date 2026/2/10
 */
public class AITool {
    private static final Logger logger = LoggerFactory.getLogger(AITool.class);
    public static void main(String[] args) {
        //定义工具描述，可以添加多个工具描述
//        String tools_ = """
//                        [
//                            {
//                                "type": "function",
//                                "function": {
//                                    "name": "get_weather",
//                                    "description": "根据用户提供的城市信息，查询对应城市的天气预报",
//                                    "parameters": {
//                                        "type": "object",
//                                        "properties": {
//                                            "location": {
//                                                "type": "string",
//                                                "description": "城市或者地州, 例如：上海市"
//                                            }
//                                        },
//                                        "required": ["location"]
//                                    }
//                                }
//                            }
//                        ]
//                        """;
//        List<FunctionToolDefine> fuctionToolDescription = SimpleStringUtil.json2ListObject(tools_, FunctionToolDefine.class);

        List<FunctionToolDefine> fuctionToolDescription = new ArrayList<>();
        FunctionToolDefine functionToolDefine = new FunctionToolDefine();
//        functionToolDefine.setType("function");
        functionToolDefine.funtionName2ndDescription("get_weather","根据用户提供的城市信息，查询对应城市的天气预报")                            
//                            .parametersType("object")
                            .requiredParameters("location")
                            .addParameter("location","string","城市或者地州, 例如：上海市")
        ;
        fuctionToolDescription.add(functionToolDefine);

        functionToolDefine = new FunctionToolDefine();
//        functionToolDefine.setType("function");
        functionToolDefine.funtionName2ndDescription("log4jtest","根据用户账号查询登录日志数据")
//                            .parametersType("object")
                .requiredParameters("subuser")
                .addSubParameter("params","subuser","string","登录账号，例如：admin")
        ;
        fuctionToolDescription.add(functionToolDefine);

        functionToolDefine = new FunctionToolDefine();
//        functionToolDefine.setType("function");
        functionToolDefine.funtionName2ndDescription("log4jtestpage","根据用户账号查询登录日志数据,进行分页查询")
//                            .parametersType("object")
                .requiredParameters("pageNum","pageSize")
                .addSubParameter("params","subuser","string","登录账号，例如：admin")
                .addParameter("pageNum","integer","分页页码，从1开始，例如：1")
                .addParameter("pageSize","integer","分页大小，例如：10")
        ;
        fuctionToolDescription.add(functionToolDefine);

        functionToolDefine = new FunctionToolDefine();
//        functionToolDefine.setType("function");
        functionToolDefine.funtionName2ndDescription("innerproperties","根据用户账号查询登录日志数据,进行分页查询")
//                            .parametersType("object")
                .requiredParameters("pageNum","pageSize")
                .addSubParameter("params","subuser","string","登录账号，例如：admin")
                .addParameter("pageNum","integer","分页页码，从1开始，例如：1")
                .addParameter("pageSize","integer","分页大小，例如：10")
                
        ;
        Property property = new Property();
        property.addParameter("name","string","用户姓名");
        property.addParameter("age","integer","用户年龄");
        property.addParameter("sex","string","用户性别");
        property.addParameter("birthday","date","用户生日");
        
        property.addParameter("address","string","用户地址");

        Property property_ = new Property();
        property_.addParameter("weather","string","天气");
        property_.addParameter("temperature","double","温度");
        property.addParameter("weather_info",property_);
        functionToolDefine.addParameter("userandwether",property);
        fuctionToolDescription.add(functionToolDefine);
        logger.info("functionToolDefine：{}", JsonUtil.object2jsonPretty(functionToolDefine));
        logger.info("工具描述：{}", JsonUtil.object2jsonPretty(fuctionToolDescription));
    }

}
