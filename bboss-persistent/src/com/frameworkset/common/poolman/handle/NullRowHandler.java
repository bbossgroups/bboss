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
package com.frameworkset.common.poolman.handle;

import com.frameworkset.common.poolman.Record;

/**
 * <p>Title: NullRowHandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-1-22 下午06:13:30
 * @author biaoping.yin
 * @version 1.0
 * @param <T>
 */
public abstract class NullRowHandler<T> extends BaseRowHandler<T>
{

    /* (non-Javadoc)
     * @see com.frameworkset.common.poolman.handle.BaseRowHandler#handleRow(java.lang.Object, com.frameworkset.common.poolman.Record)
     */
    @Override
    public void handleRow(T rowValue, Record origine) throws Exception
    {
        handleRow(origine);
        
    }
    

    public abstract void handleRow(Record origine) throws Exception;
}
