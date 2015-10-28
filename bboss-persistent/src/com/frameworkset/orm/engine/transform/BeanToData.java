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

import java.io.Serializable;
import java.util.List;

import com.frameworkset.orm.engine.model.Column;
import com.frameworkset.orm.engine.model.Table;
import com.frameworkset.util.SimpleStringUtil;

/**
 * <p>Title: BeanData</p>
 *
 * <p>Description:
 *  实现java对象中的属性值转换到该java对象所对应的表的属性
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanToData implements Serializable {
    public static void main(String[] args) {
        BeanToData beantodata = new BeanToData();
    }

    /**
     *
     */
    public class DataRow
    {
        private Table table;
        private List columnValues;

        public DataRow(Table table, List columnValues)
        {
            this.table = table;
            this.columnValues = columnValues;
        }

        public Table getTable()
        {
            return table;
        }

        public List getColumnValues()
        {
            return columnValues;
        }
    }

    /**
     *
     */
    public class ColumnValue
    {
        private Column col;
        private String val;

        public ColumnValue(Column col, String val)
        {
            this.col = col;
            this.val = val;
        }

        public Column getColumn()
        {
            return col;
        }

        public String getValue()
        {
            return val;
        }

        public String getEscapedValue()
        {
            StringBuffer sb = new StringBuffer();
            sb.append("'");
            sb.append(SimpleStringUtil.replace(val, "'", "''"));
            sb.append("'");
            return sb.toString();
        }
    }


}
