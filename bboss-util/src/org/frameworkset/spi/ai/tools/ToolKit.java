package org.frameworkset.spi.ai.tools;
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
import org.frameworkset.spi.ai.model.FunctionCall;
import org.frameworkset.spi.ai.model.FunctionToolDefine;

import java.util.*;

/**
 * @author biaoping.yin
 * @Date 2026/2/10
 */
public class ToolKit {
    private List<FunctionToolDefine> functionTools  ;
    private Map<String, FunctionCall> functionCalls ;

    private Map<String, FunctionToolDefine> functionToolDefineMap ;
    public ToolKit() { 
    }

    public ToolKit setFunctionCalls(Map<String, FunctionCall> functionCalls) {
        this.functionCalls = functionCalls;
        return this;
    }
    public ToolKit setFunctionTools(List<FunctionToolDefine> functionTools) {
        this.functionTools = functionTools;
        if(this.functionTools != null){
            functionToolDefineMap = new HashMap<>();
            for(FunctionToolDefine functionToolDefine:functionTools){
                functionToolDefineMap.put(functionToolDefine.getFunction().getName(), functionToolDefine);
            }
        }
        return this;
    }

    public ToolKit setFunctionTools(String functionTools) {
        if(functionTools != null){
            this.functionTools = SimpleStringUtil.json2ListObject(functionTools, FunctionToolDefine.class);
        }

        return this;
    }
    
    public ToolKit registerFunctionCall(String functionName,FunctionCall functionCall){
        if(functionCalls == null)
            functionCalls = new LinkedHashMap<>();
        functionCalls.put(functionName, functionCall);
        return this;
    }
    public ToolKit registerFunctionTool(FunctionToolDefine functionTool){
        if(functionTools == null)
            functionTools = new ArrayList<>();
        functionTools.add(functionTool);
        return this;
    }
    public void buildTools(){ 
        if(this.functionCalls != null && functionCalls.size() > 0){
            Iterator<Map.Entry<String, FunctionCall>> iterator = functionCalls.entrySet().iterator();
            while(iterator.hasNext()){ 
                Map.Entry<String, FunctionCall> entry = iterator.next();
                FunctionCall functionCall = entry.getValue();
                String functionName = entry.getKey();
                FunctionToolDefine functionToolDefine = this.functionToolDefineMap.get(functionName);
                if(functionToolDefine != null){
                    functionToolDefine.setFunctionCall(functionCall);
                }
                else{
                    throw new ToolKitDefineException("未定义的函数执行处理器："+functionName);
                }
            }
                
        }
    }
}
