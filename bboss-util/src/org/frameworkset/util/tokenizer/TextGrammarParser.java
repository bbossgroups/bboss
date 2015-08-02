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
package org.frameworkset.util.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用文本语法解析工具，根据一些指定语法前缀和后缀
 * @author biaoping.yin
 *
 */
public class TextGrammarParser {
	public static class GrammarToken
	{
		public static final int TextPlain = 0;
		public static final int VARIABLE = 1;
		private int position;
		private String text;
		/**
		 * 0 普通text
		 * 1 变量
		 */
		
		private int type;
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("text=").append(text).append("|");
			builder.append("position=").append(position).append("|");
			builder.append("type=").append(type == TextPlain?"普通文本":"结构变量").append("\r\n");
			
			return builder.toString();
			
		}
		
		public boolean texttoken()
		{
			return this.type == TextPlain;
		}
		
		public boolean varibletoken()
		{
			return this.type == VARIABLE;
		}
		
		
	}

	private static boolean tplstart(char value,char[] pre,String text,int textsize,int curpos)
	{
		boolean tplstart = true;
		int k = 0;
		for(int i = 1; i < pre.length; i ++)
		{		
			k = curpos + i;
			if(k>=textsize || text.charAt(k) != pre[i])
			{
				tplstart = false;
				break;
			}		
			
		}
		return tplstart;
	}
	/**
	 * 
	 * 计算模板中引用的include模板文件，并将引用文件的内容合并到当前模板中
	 * include中对应的文件内容引用的地址都需要用<cms:uri link="path"/>来指定，这样才不会有发布相对路径问题	
	 * #inlcude(head.html)
	 * 
	 * @param content
	 * @param tokenpre
	 * @param tokenend
	 * @return
	 */
	public static List<GrammarToken> parser(String content,String tokenpre,char tokenend)
	{
		StringBuilder builder = new StringBuilder();
		StringBuilder tplbuilder = new StringBuilder();
		List<GrammarToken> tokens = new ArrayList<GrammarToken>();
		int size = content.length();
		char[] pre = tokenpre.toCharArray();
		boolean tplstart = false;
		for(int i = 0; i < size; i ++)
		{
			char c = content.charAt(i);
			if(c == pre[0])
			{
//				if(content.charAt(i+1) == 'i' 
//						&& content.charAt(i+2) == 'n' 
//						&& content.charAt(i+3) == 'c' 
//						&& content.charAt(i+4) == 'l'
//					&& content.charAt(i+5) == 'u'
//					&& content.charAt(i+6) == 'd'
//					&& content.charAt(i+7) == 'e'
//					&& content.charAt(i+8) == '(')
				if(tplstart(c, pre,content,size,i))
				{
					if(tplstart)	//如果之前已经开始模板前导
					{
//						builder.append("#include(").append(tplbuilder.toString());
						builder.append(tokenpre).append(tplbuilder.toString());
						tplbuilder.setLength(0);
					}
					else
					{
						tplstart = true;
					}
//					i = i+8;	
					i = i+pre.length - 1;					
				}
				else
				{
					if(tplstart)
					{
						tplbuilder.append(c);
					}
					else
					{
						builder.append(c);
					}
				}
			}
			else if(c == tokenend)
			{
				if(tplstart)
				{
//					tplbuilder.append(c);
					if(tplbuilder.length() > 0)
					{
						if(builder.length() > 0)
						{
							GrammarToken stringtoken = new GrammarToken();
							stringtoken.position = tokens.size() ;
							stringtoken.text = builder.toString();
							stringtoken.type = GrammarToken.TextPlain;//宏文件路径
							builder.setLength(0);
							tokens.add(stringtoken);
						}
						GrammarToken stringtoken = new GrammarToken();
						stringtoken.position = tokens.size() ;
						stringtoken.text = tplbuilder.toString();
						stringtoken.type = GrammarToken.VARIABLE;//宏文件路径
						tplbuilder.setLength(0);
						tokens.add(stringtoken);
					}
					else
					{
//						builder.append("#include()");
						builder.append(tokenpre).append(tokenend);
						
						
					}
					tplstart = false;
					
				}
				else
				{					
					builder.append(c);
				}
				
			}
			else
			{
				if(tplstart)
				{
					tplbuilder.append(c);
				}
				else
				{
					builder.append(c);
				}
			}			
		}
		if(tplbuilder.length() > 0)
		{
//			builder.append("#include(").append(tplbuilder.toString());
			builder.append(tokenpre).append(tplbuilder.toString());
		}
		tplbuilder = null;
		if(builder.length() > 0)
		{
			GrammarToken stringtoken = new GrammarToken();
			stringtoken.position = tokens.size() ;
			stringtoken.text = builder.toString();
			stringtoken.type = 0;//宏文件路径
			builder.setLength(0);
			tokens.add(stringtoken);
		}
		builder = null;
		return tokens;
			
		
	}
	

}
