/**
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
package org.frameworkset.util.annotations.wraper;

import org.frameworkset.util.annotations.AnnotationUtils;

import com.frameworkset.orm.annotation.Column;
import com.frameworkset.util.ColumnEditorInf;
import com.frameworkset.util.EditorInf;

/**
 * <p>ColumnWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class ColumnWraper {
	private String dataformat;
	private String name;
	private String type;
	private String charset;
	private String editor;
	/**
	 * 改进持久or mapping机制，Column注解添加ignorebind和ignoreCUDbind，editorparams
	 */
	private String editorparams;
	private ColumnEditorInf _editor;
	private boolean inited;
	private boolean ignoreCUDbind = false;
	private boolean ignorebind = false;
	public ColumnWraper(Column column) {
		dataformat =  AnnotationUtils.converDefaultValue(column.dataformat());
		editorparams =  AnnotationUtils.converDefaultValue(column.editorparams());
		
		name = column.name();
		type =  AnnotationUtils.converDefaultValue(column.type());
		charset =  AnnotationUtils.converDefaultValue(column.charset());
		this.editor = AnnotationUtils.converDefaultValue(column.editor());
		this.ignoreCUDbind = column.ignoreCUDbind();
		this.ignorebind = column.ignorebind();
	}
	public String dataformat(){
		return dataformat;
	}
	public String name() {
		return name;
	}
	public String type() {
		return type;
	}
	public String charset() {
		return charset;
	}
	public ColumnEditorInf editor()
	{
		if(inited)
			return this._editor;
		synchronized(this)
		{
			if(inited)
				return this._editor;
			if(editor != null && !editor.trim().equals(""))
			{
				try
				{
					Class<?> clazz = Class.forName(this.editor.trim());
					_editor = (ColumnEditorInf)clazz.newInstance();
				}
				catch(Exception e)
				{
					
				}
				
			}
			inited = true;
		}
		return this._editor;
		
	}
	public String editorparams() {
		return editorparams;
	}
	 
	public boolean ignoreCUDbind() {
		return ignoreCUDbind;
	}
	
	public boolean ignorebind() {
		return ignorebind;
	}

}
