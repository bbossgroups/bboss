/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.orm.doclet;

/**
 * <p>Title: </p>
 *
 * <p>Description: 自定义的标记名称和属性常量</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface TagConst {
    public static final String TAG_DATABASE = "database";
    public static final String TAG_DATABASE_NAME = "name";
    public static final String TAG_DATABASE_TYPE = "type";
    public static final String TAG_DATABASE_TABLE = "table";

    public static final String TAG_COLUMN = "table.column";
    public static final String TAG_COLUMN_NAME = "name";
    public static final String TAG_COLUMN_REQUIRED = "required";
    public static final String TAG_COLUMN_PRIMARYKEY = "primaryKey";
    public static final String TAG_COLUMN_TYPE = "type";
    public static final String TAG_COLUMN_DESCRIPTION = "description";

    public static final String TAG_TABLE = "table";
    public static final String TAG_TABLE_NAME = "name";
    public static final String TAG_TABLE_SCHEMA = "schema";


}
