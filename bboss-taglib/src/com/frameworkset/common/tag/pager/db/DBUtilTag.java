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

package com.frameworkset.common.tag.pager.db;

import java.sql.SQLException;

import javax.servlet.jsp.JspException;

/**
 * <p>Title: DBUtilTag.java</p> 
 * <p>Description: 执行数据库操作标签，支持预编译插入，删除，修改操作</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-11 下午01:38:53
 * @author biaoping.yin
 * @version 1.0
 */
public class DBUtilTag extends SQLParamsContextTag
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4455080970563882856L;
	private String statement ;
    private String dbname;

    private String action;
    private String result = "dbutil_result";
    
    
    public int doEndTag() throws JspException
    {  
        if(sqlExecutor != null)
        {
            try
            {
                Object ret =sqlExecutor.execute();
                if(ret != null && result!=null && !result.equals(""))
                    pageContext.setAttribute(result, ret);
            }
            catch (SQLException e)
            {
                throw new JspException(e);
            }
        }
        statement = null;
        dbname= null;
        action= null;
        result = "dbutil_result";
        return super.doEndTag();
    }

    
    public int doStartTag() throws JspException
    {
        super.doStartTag();
        sqlExecutor.setStatement(statement);
        sqlExecutor.setDbname(dbname);
        sqlExecutor.setAction(action);
        return EVAL_BODY_INCLUDE;
    }    


    
    public String getStatement()
    {
        return statement;
    }


    public void setStatement(String statement)
    {
        this.statement = statement;
    }


    public String getDbname()
    {
        return dbname;
    }


    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }


    


    public String getAction()
    {
        return action;
    }


    public void setAction(String action)
    {
        this.action = action;
    }


    public String getResult()
    {
        return result;
    }


    public void setResult(String result)
    {
        this.result = result;
    }


    
    






   
    
    

}
