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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * @author biaoping.yin
 * @Date 2026/2/10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameters {
    private String type = "object";
    private Map<String,Property> properties;
    private List<String> required;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }
    
    public Parameters addRequired(String name) {
        if (required == null)
            required = new java.util.ArrayList<String>();
        required.add(name);
        return this;
    }

    public void addParameter(String name, String type, String desc) {
        if (properties == null)
            properties = new java.util.LinkedHashMap<String, Property>();
        properties.put(name, new Property(type, desc));
    }

    /**
     * 添加复合参数条件
     * @param name
     * @param key
     * @param type
     * @param desc
     */
    public void addSubParameter(String name,String key, String type, String desc) {
        if (properties == null)
            properties = new java.util.LinkedHashMap<String, Property>();
//        Map<String,Property> map = properties.get( name);
        Property property = properties.get(name);
        if(property == null){
            property = new Property("object",null);
            properties.put(name,property);
        }
        property.addParameter(key,type,desc);
        
    }

    /**
     * 添加复合参数条件
     * @param name 
     */
    public void addParameter(String name,Property property) {
        if (properties == null)
            properties = new java.util.LinkedHashMap<String, Property>();
        
       
        properties.put(name,property);
         

    }
}
