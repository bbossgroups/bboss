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
package com.frameworkset.orm.adapter;

import java.util.Date;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TestDBAdapter {
    public static void main(String[] args) {
        TestDBAdapter testdbadapter = new TestDBAdapter();
        try {
            //根据日期获取不同数据库的日期转换函数,其中com.frameworkset.orm.adapter.DBFactory.DBOracle表示数据库类型常量
            Date date = new Date();
            System.out.println(com.frameworkset.orm.adapter.DBFactory.create(com.frameworkset.orm.adapter.DBFactory.DBOracle).getDateString(date));
            //根据日期字符串获取不同数据库的日期转换函数，其中com.frameworkset.orm.adapter.DBFactory.DBOracle表示数据库类型常量
            String date_str = "11-04-2006 09:27:31";
            System.out.println(com.frameworkset.orm.adapter.DBFactory.create(com.frameworkset.orm.adapter.DBFactory.DBOracle).getDateString(date_str));
        } catch (InstantiationException ex) {
        }
    }

}
