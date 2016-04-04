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
package com.frameworkset.orm.engine.transform;

import java.util.List;

import org.junit.Test;

import com.frameworkset.orm.engine.model.Database;

/**
 * <p>Title: TestXmlToData.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-1-26 下午05:12:00
 * @author biaoping.yin
 * @version 1.0
 */
public class TestXmlToData
{
    @Test
    public void test() throws Exception
    {
        XmlToData xmltoData = new XmlToData(new Database("derby"),"resources/database_3_2.dtd");
        List data = xmltoData.parseFile("src/com/frameworkset/orm/engine/model/domaintest-schema.xml");
        System.out.println(data);
        
    }
}
