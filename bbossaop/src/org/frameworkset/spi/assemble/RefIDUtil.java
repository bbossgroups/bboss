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

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.spi.assemble.RefID.Index;

/**
 * <p>Title: RefIDUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-1-2 下午10:02:32
 * @author biaoping.yin
 * @version 1.0
 */
public class RefIDUtil {
	public static RefID parsedRefID(String refid)
	{
		char[] tokens = refid.toCharArray();
		StringBuffer name = new StringBuffer();
		StringBuffer index = new StringBuffer();		
		RefID ref = null;
		RefID header = null;
		
		List<Index> indexs = null;
		boolean indexstart = false;
		boolean namestart = true;
		int len = tokens.length-1;
		boolean isConstruction = false;
		for(int i = 0; i < tokens.length; i ++)
		{
			if(tokens[i] == '-' && i < len &&  tokens[i+1] == '>')
			{
				i ++;

				
				if(ref == null)
				{
					ref = new RefID();
					ref.setName(name.toString());
					ref.setIndexs(indexs);
//					ref.setInconstruction(isConstruction);
					header = ref;
				}
				else
				{
					RefID ref_old = ref;
					ref = new RefID();
					ref.setParent(ref_old);
					ref_old.setNext(ref);
					ref.setName(name.toString());
//					ref.setInconstruction(isConstruction);
					ref.setIndexs(indexs);
				}
				namestart = true;
				indexstart = false;
				isConstruction = false;
				indexs = null;
				name.setLength(0);
			}
			else if(tokens[i] == '[')
			{
				i ++;
				namestart = false;
				indexstart = true;
				isConstruction = false;
				
				index.append(tokens[i]);
			}
			else if(tokens[i] == ']')
			{
//				i ++;
				if(indexs == null)
					indexs = new ArrayList<Index>(1);
				if(i < len && tokens[i+1] != '[') //如果后续字符还是[开头，则继续计算下一维索引标识，
				{
					
//					index.append(tokens[i]);
					String t = index.toString();					
					try {
						indexs.add(new Index(Integer.parseInt(t),isConstruction));
					} catch (Throwable e) {
						indexs.add(new Index(t,isConstruction));
					}
					namestart = true;
					indexstart = false;
					isConstruction = false;
					index.setLength(0);
					
				}
				else
				{
					i ++;
					String t = index.toString();					
					try {
						indexs.add(new Index(Integer.parseInt(t),isConstruction));
					} catch (Throwable e) {
						indexs.add(new Index(t,isConstruction));
					}
					index.setLength(0);
				}
				
				
			}
			
			else if(tokens[i] == '{')
			{
				i ++;
				namestart = false;
				indexstart = true;
				isConstruction = true;
				index.append(tokens[i]);
			}
			else if(tokens[i] == '}')
			{
//				i ++;
				if(indexs == null)
					indexs = new ArrayList<Index>(1);
				if(i < len && tokens[i+1] != '{') //如果后续字符还是[开头，则继续计算下一维索引标识，
				{
					
					
//					index.append(tokens[i]);
					String t = index.toString();					
					try {
						indexs.add(new Index(Integer.parseInt(t),isConstruction));
					} catch (Throwable e) {
						indexs.add(new Index(t,isConstruction));
					}
					namestart = true;
					indexstart = false;
					isConstruction = false;
					index.setLength(0);
					
				}
				else
				{
					i ++;
					String t = index.toString();					
					try {
						indexs.add(new Index(Integer.parseInt(t),isConstruction));
					} catch (Throwable e) {
						indexs.add(new Index(t,isConstruction));
					}
					index.setLength(0);
				}
				
				
			}
			else
			{
				if(namestart)
					name.append(tokens[i]);
				else
					index.append(tokens[i]);
					
			}
		}
		if(name.length() > 0)
		{
			if(index.length() > 0)
			{
				if(indexs == null)
					indexs = new ArrayList<Index>(1);
				String t = index.toString();					
				try {
					indexs.add(new Index(Integer.parseInt(t),isConstruction));
				} catch (Throwable e) {
					indexs.add(new Index(t,isConstruction));
				}
				index.setLength(0);
			}
			if(ref == null)
			{
				ref = new RefID();
				ref.setName(name.toString());
				ref.setIndexs(indexs);
				header = ref;
			}
			else
			{
				RefID ref_old = ref;
				ref = new RefID();
				ref.setParent(ref_old);
				ref_old.setNext(ref);
				ref.setName(name.toString());
				ref.setIndexs(indexs);
			}
		}
		return header;
	}
}
