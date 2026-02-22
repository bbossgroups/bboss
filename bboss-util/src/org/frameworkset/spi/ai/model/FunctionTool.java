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

import java.util.List;
import java.util.Map;

/**
 * @author biaoping.yin
 * @Date 2026/2/9
 */
public class FunctionTool {
    private int index;
    private String id;
    private String type;
    private String functionName;
    private Map arguments;
    
    private List<Map> toolcalls;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Map getArguments() {
        return arguments;
    }

    public void setArguments(Map arguments) {
        this.arguments = arguments;
    }

    public List<Map> getToolcalls() {
        return toolcalls;
    }

    public void setToolcalls(List<Map> toolcalls) {
        this.toolcalls = toolcalls;
    }
}
