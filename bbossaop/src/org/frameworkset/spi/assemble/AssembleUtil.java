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

import java.util.List;

import org.frameworkset.spi.assemble.callback.AssembleCallback;
import org.frameworkset.spi.assemble.callback.AssembleCallbackException;
import org.frameworkset.spi.assemble.callback.AssembleCallbackResolver;
import org.frameworkset.spi.assemble.callback.ClasspathAssembleCallback;
import org.frameworkset.spi.assemble.callback.DefaultAssembleCallbackResolver;

/**
 * <p>Title: AssembleUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-4 下午03:56:12
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AssembleUtil {
	private static AssembleCallbackResolver assembleCallbackResolver = new DefaultAssembleCallbackResolver();
	public final static AssembleCallback defaultAssembleCallback = new ClasspathAssembleCallback();
	public static AssembleCallbackResolver getAssembleCallbackResolver()
	{
		if(assembleCallbackResolver != null)
			return assembleCallbackResolver;
		
		synchronized(AssembleCallbackResolver.class)
		{
			if(assembleCallbackResolver != null)
				return assembleCallbackResolver;
			
			DefaultAssembleCallbackResolver temp = new DefaultAssembleCallbackResolver();
			ClasspathAssembleCallback assembleCallback = new ClasspathAssembleCallback();
			
			temp.registAssembleCallback(assembleCallback);
			
			assembleCallbackResolver = temp;
			return assembleCallbackResolver;
		}
	}
	
	public static void registAssembleCallback(AssembleCallback assembleCallback)
	{
		getAssembleCallbackResolver().registAssembleCallback(assembleCallback);
	}
	
	public static List<ManagerImport> getManagerImports(ManagerImport docbaseImport) throws AssembleCallbackException
	{
		return getAssembleCallbackResolver().getManagerImports(docbaseImport);
	}
	
	public static List<ManagerImport> getManagerImports(String docbaseType,String docbase,String contextFile) throws AssembleCallbackException
	{
		return getAssembleCallbackResolver().getManagerImports( docbaseType, docbase, contextFile);
	}
	
	
	
}
