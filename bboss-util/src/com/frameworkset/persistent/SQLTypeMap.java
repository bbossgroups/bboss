package com.frameworkset.persistent;
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

import java.io.File;

/**
 * SQL参数类型枚举
 * @author biaoping.yin
 * @Date 2026/1/15
 */

public enum SQLTypeMap {
    STRING("string", String.class),
    INT("int", Integer.class),
    LONG("long", Long.class),
    DOUBLE("double", Double.class),
    FLOAT("float", Float.class),
    SHORT("short", Short.class),
    DATE("date", java.sql.Date.class),
    TIMESTAMP("timestamp", java.sql.Timestamp.class),
    BIGDECIMAL("bigdecimal", java.math.BigDecimal.class),
    BOOLEAN("boolean", Boolean.class),
    BYTE("byte", Byte.class),
    TIME("time", java.sql.Time.class),
    BYTEARRAY("byte[]", byte[].class),
    BLOBBYTEARRAY("blobbyte[]", byte[].class),
    BLOBFILE("blobfile", File.class),
    BLOB("blob", java.sql.Blob.class),
    CLOBFILE("clobfile", File.class),
    CLOB("clob", java.sql.Clob.class),
    OBJECT("object", Object.class),
    NULL("null", Void.class);
    
    //增加一个静态Map变量，以typeName为key，SQLTypeMap为value
    private static final java.util.Map<String, SQLTypeMap> typeNameMap = new java.util.HashMap<>();
    static {
        for (SQLTypeMap type : values()) {
            typeNameMap.put(type.typeName, type);
        }
    }
    private final String typeName;
    private final Class<?> clazz;

    SQLTypeMap(String typeName, Class<?> clazz) {
        this.typeName = typeName;
        this.clazz = clazz;
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 根据类型名称查找对应的枚举值
     */
    public static SQLTypeMap fromTypeName(String typeName) {
        //从typeNameMap中查找
        SQLTypeMap sqlTypeMap = typeNameMap.get(typeName);
        if(sqlTypeMap == null)
            throw new IllegalArgumentException("Unknown SQL type: " + typeName);
        return sqlTypeMap;
    }

    /**
     * 根据Class查找对应的枚举值
     */
    public static SQLTypeMap fromClass(Class<?> clazz) {
        for (SQLTypeMap type : values()) {
            if (type.clazz.equals(clazz)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No SQL type found for class: " + clazz);
    }
}

