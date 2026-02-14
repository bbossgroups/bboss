package org.frameworkset.spi.ai.model;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * [
 *                             {
 *                                 "type": "function",
 *                                 "function": {
 *                                     "name": "get_weather",
 *                                     "description": "根据用户提供的城市信息，查询对应城市的天气预报",
 *                                     "parameters": {
 *                                         "type": "object",
 *                                         "properties": {
 *                                             "location": {
 *                                                 "type": "string",
 *                                                 "description": "城市或者地州, 例如：上海市"
 *                                             }
 *                                         },
 *                                         "required": ["location"]
 *                                     }
 *                                 }
 *                             }
 *                         ]
 *                         """;
 * @author biaoping.yin
 * @Date 2026/2/9
 */
public class FunctionToolDefine {
    private String type = "function";
    private Function function;
    @JsonIgnore
    private FunctionCall functionCall;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
    
    public FunctionToolDefine funtionName(String functionName){
        if(function == null){
            function = new Function();
        }
    	this.function.setName(functionName);
    	return this;
    }

    public FunctionToolDefine funtionName2ndDescription(String functionName, String desc) {
        if(function == null){
            function = new Function();
        }
        this.function.setName(functionName);
        this.function.setDescription(desc);
        return this;
    }

    public FunctionToolDefine parametersType(String type) {
        if (function == null) {
            function = new Function();
        }
        function.parametersType(type);
        return this;
    }
    
    public FunctionToolDefine requiredParameters(String ... requireds) {
        if (function == null) {
            function = new Function();
        }
        function.parametersRequired(requireds);
        return this;
    }

    public FunctionToolDefine addRequiredParameter(String required) {
        if (function == null) {
            function = new Function();
        }
        function.addRequiredParameter(required);
        return this;
    }

    public FunctionToolDefine addParameter(String name, String type, String desc) {
        if (function == null) {
            function = new Function();
        }
        function.addParameter(  name,   type,   desc);
        return this;
    }

    public FunctionToolDefine addSubParameter(String name,String key, String type, String desc) {
        if (function == null) {
            function = new Function();
        }
        function.addSubParameter(name,key,type,desc);
        return this;
    }

    /**
     * 添加复合参数条件
     * @param name
     */
    public FunctionToolDefine addParameter(String name,Property property) {
        if (function == null) {
            function = new Function();
        }
        function.addParameter(name,property);
        return this;
    }

    public FunctionToolDefine setFunctionCall(FunctionCall functionCall) {
        this.functionCall = functionCall;
        return this;
    }

    public FunctionCall getFunctionCall() {
        return functionCall;
    }
    public Parameters parameters(){
        return function.getParameters();
    }
}
