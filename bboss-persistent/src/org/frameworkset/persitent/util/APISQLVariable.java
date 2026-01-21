package org.frameworkset.persitent.util;
/**
 * Copyright 2020 bboss
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

import com.frameworkset.persistent.SQLTypeMap;
import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>Description: 扩展解析提取sql类型</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/3/2 9:15
 * @author biaoping.yin
 * @version 1.0
 */
public class APISQLVariable extends PersistentSQLVariable {
    /**
     * 是否是查询列标识
     */
    protected boolean column;
    /**
     * 列别名
     */
    protected String alias;
    
    protected void parserTypeAndDefaultObjectValue(String t) {
        if (t.startsWith("column=")) {
            String q = t.substring("column=".length()).trim();
            if(q.equals("true"))
                column = true;

        }
        else if(t.startsWith("alias=")){
            String q = t.substring("alias=".length());
            alias = q;
        }
        super.parserTypeAndDefaultObjectValue(t); 
    }

 

    public boolean isColumn() {
        return column;
    }

    public String getAlias() {
        return alias;
    }
}
