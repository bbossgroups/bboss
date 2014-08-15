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

package org.frameworkset.spi.assemble;

import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.util.EditorInf;

/**
 * <p>Title: Editor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-12-7 下午12:51:42
 * @author biaoping.yin
 * @version 1.0
 */
public class Editor
{
    private String editor;
    public String getEditor()
    {
        return editor;
    }
    public void setEditor(String editor)
    {
        this.editor = editor;
    }
    private EditorInf instance;
    private Object lock = new Object();
    public EditorInf getEditorInf(BaseApplicationContext context) throws Exception
    {
        if(instance != null)
            return this.instance;
        synchronized(lock)
        {
            if(instance != null)
                return this.instance;
            instance = (EditorInf) context.createBean(this.getEditor());
            return instance;
        }
    }
    
    
}
