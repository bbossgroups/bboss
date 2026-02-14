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

import java.util.Map;

/**
 *  "location": {
 *  *                                                 "type": "string",
 *  *                                                 "description": "城市或者地州, 例如：上海市"
 *  *                                             }
 * @author biaoping.yin
 * @Date 2026/2/10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {
    private String type = "object";
    private String description;
    private Map<String,Property> properties;

    public Property() {
    }
    
    public Property(String type, String desc) {
        this.type = type;
        this.description = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property addParameter(String name, String type, String desc) {
        if (properties == null)
            properties = new java.util.LinkedHashMap<>();
        properties.put(name, new Property(type, desc));
        return this;
    }

    public Property addParameter(String name, Property property) {
        if (properties == null)
            properties = new java.util.LinkedHashMap<>();
        properties.put(name, property);
        return this;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }
}
