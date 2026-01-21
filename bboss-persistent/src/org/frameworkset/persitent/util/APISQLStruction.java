package org.frameworkset.persitent.util;
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

import com.frameworkset.util.VariableHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author biaoping.yin
 * @Date 2026/1/21
 */
public class APISQLStruction extends VariableHandler.SQLStruction {
    /**
     * 查询字段清单
     */
    protected List<APISQLVariable> columns;
    private List<APISQLVariable> parameters;
    
    public APISQLStruction()
    {
        super();
    }
    public APISQLStruction(String sql)
    {
        super(sql);
    }

    @Override
    public String buildSQL()
    {
        if(sql == null)
        {
            if(this.variables != null && variables.size() > 0)
            {

               
                StringBuilder sql_ = new StringBuilder();
                int tsize = this.tokens.size();
                int vsize = this.variables.size();
                if(tsize == vsize)
                {
                    for(int i = 0; i < vsize; i ++)
                    {
                        sql_.append(tokens.get(i));
                        APISQLVariable variable = (APISQLVariable)this.variables.get(i);
                        if(!variable.isColumn()) {//还原变量
                            variable.toSQLString(sql_);

                        }
                        else{
                            //还原查询列表
                            sql_.append(variable.getVariableName());
                            if(variable.getAlias() != null){
                                sql_.append(" as ").append(variable.getAlias());
                            }
                        }
                    }
                }
                else //tsize = vsize + 1;
                {
                    for(int i = 0; i < vsize; i ++)
                    {
                        sql_.append(tokens.get(i));
                        APISQLVariable variable = (APISQLVariable)this.variables.get(i);
                        if(!variable.isColumn()) {//还原变量
                            variable.toSQLString(sql_);

                        }
                        else{
                            //还原查询列表
                            sql_.append(variable.getVariableName());
                            if(variable.getAlias() != null){
                                sql_.append(" as ").append(variable.getAlias());
                            }
                        }
                    }
                    sql_.append(tokens.get(vsize));
                }
                this.sql = sql_.toString();

            }

        }
        return sql;
    }


    /**
     * 获取查询字段清单
     * @return
     */
    public List<APISQLVariable> getColumns() {
        return columns;
    }
    
    public List<APISQLVariable> getParameters() {
        return parameters;
    }

    @Override
    protected void after() {
        //将variables中的isColumn为true的变量放置到columns中
        if(variables != null) {
            for (int i = 0; i < this.variables.size(); i++) {
                APISQLVariable variable = (APISQLVariable) this.variables.get(i);
                if (variable.isColumn()) {
                    if (this.columns == null) {
                        this.columns = new ArrayList<>();
                    }
                    this.columns.add(variable);
                }
                else {
                    if (this.parameters == null) {
                        this.parameters = new ArrayList<>();
                    }
                    this.parameters.add(variable);
                }
            }
        }
        super.after();
    }
    
    
}
