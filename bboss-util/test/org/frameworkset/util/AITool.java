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

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.spi.ai.model.FunctionToolDefine;
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
        functionToolDefine.putFuntionName2ndDescription("get_weather","根据用户提供的城市信息，查询对应城市的天气预报")                            
//                            .putParametersType("object")
                            .putRequiredParameters("location")
                            .addParameter("location","string","城市或者地州, 例如：上海市")
        ;
        fuctionToolDescription.add(functionToolDefine);
        logger.info("工具描述：{}", SimpleStringUtil.object2jsonPretty(fuctionToolDescription));
    }

}
