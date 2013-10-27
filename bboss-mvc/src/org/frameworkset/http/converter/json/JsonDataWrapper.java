/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.http.converter.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: JsonDataWrapper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-12-10
 * @author biaoping.yin
 * @version 1.0
 */
public class JsonDataWrapper <T> implements Serializable {   
    private static final long serialVersionUID = -538629307783721872L;   
  
    public JsonDataWrapper(int total, int page, List<T> rows){   
        this.total = total;   
        this.page = page;   
        this.rows = rows;   
    }   
    private int total;   
    private int page;   
    private List<T> rows;   
       
    public int getTotal() {   
        return total;   
    }   
    public void setTotal(int total) {   
        this.total = total;   
    }   
    public int getPage() {   
        return page;   
    }   
    public void setPage(int page) {   
        this.page = page;   
    }   
    public List<T> getRows() {   
        return rows;   
    }   
    public void setRows(List<T> rows) {   
        this.rows = rows;   
    }   
    
    /**
	 * 对列表进行分页操作,数据源是一个列表
	 * @param datas 列表数据
	 * @param offset 获取数据的起始位置
	 * @param pageItems 获取数据的条数
	 * @return ListInfo 对分页数据和总记录条数的封装类
	 */

	public static JsonDataWrapper pagerList(List datas,int page,int pageItems)
	{
     
		if(datas == null)
			return null;
		List list = new ArrayList();
		long offset = (page - 1) * pageItems;
		offset = Math.max(0, offset);
		if(offset >= datas.size())
		{
		    int temp = datas.size() % pageItems;
		    offset = datas.size() - temp;
		}
		for(long i = offset; i < datas.size() && i < offset + pageItems; i ++)
		{
		    list.add(datas.get((int)i));
		}
		JsonDataWrapper listInfo = new JsonDataWrapper(datas.size(),page,list);
		
		return listInfo;
	}
}
