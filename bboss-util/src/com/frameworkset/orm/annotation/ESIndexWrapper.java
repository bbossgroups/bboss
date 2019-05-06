package com.frameworkset.orm.annotation;
/**
 * Copyright 2008 biaoping.yin
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

import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.annotations.DateFormateMeta;
import org.frameworkset.util.tokenizer.TextGrammarParser;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/5/5 23:10
 * @author biaoping.yin
 * @version 1.0
 */
public class ESIndexWrapper {
	public static class NameInfo{
		/**
		 * 如果是固定的索引名称，则对应的索引名称为name字段对应的值，
		 * 否则索引名称动态从tokens中计算得到一个动态的索引名称
		 */
		private String name;

		private List<NameGrammarToken> tokens;
		public void buildName(StringBuilder builder,ClassUtil.ClassInfo classInfo, Object bean){
			if(tokens == null || tokens.size() == 0){
				builder.append(name);
			}
			else{
				NameGrammarToken nameGrammarToken = null;
				for(int i = 0; i < tokens.size(); i ++){
					nameGrammarToken = tokens.get(i);
					if(!nameGrammarToken.varibletoken()) {
						builder.append(nameGrammarToken.getText());
					}
					else{
						Object va = classInfo.getPropertyValue(bean,nameGrammarToken.getFieldName());
						if(va == null)
							throw new NameParserException(new StringBuilder()
									.append(this.toString())
									.append(",property[")
									.append(nameGrammarToken.getFieldName()).append("] is null.").toString());
						if(nameGrammarToken.dateformat != null){
							DateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(nameGrammarToken.dateformat);
							if(va instanceof Date) {
								builder.append(dateFormat.format((Date) va));
							}
							else if(va instanceof Long ){
								builder.append(dateFormat.format(new Date((Long)  va)));

							}
							else{
								builder.append(va);
							}
						}
						else{
							builder.append(va);
						}
					}
				}
			}
//			return null;

		}
		public String buildName(ClassUtil.ClassInfo classInfo, Object bean){
			StringBuilder builder = new StringBuilder();
			buildName(builder,classInfo,bean);
			return builder.toString();
		}

	}
	public static class NameGrammarToken extends TextGrammarParser.GrammarToken{
		protected String fieldName;
		protected DateFormateMeta dateFormateMeta;
		protected String dateformat;
		@Override
		public void after() {
			try {
				if (this.varibletoken()) {
					int idx = text.indexOf(",");
					if (idx > 0) {
						fieldName = text.substring(0, idx);
						dateformat = text.substring(idx + 1);
						dateFormateMeta = DateFormateMeta.buildDateFormateMeta(dateformat);
					} else {
						fieldName = text;
					}
				}
			}
			catch (Exception e){
				throw new NameParserException(this.toString(),e);
			}
		}
		public String getFieldName(){
			return fieldName;
		}
		public DateFormateMeta getDateFormateMeta(){
			return this.dateFormateMeta;
		}

	}
	private static TextGrammarParser.GrammarTokenBuilder<NameGrammarToken> nameGrammarTokenBuilder = new NameGrammarTokenBuilder();
	public static class NameGrammarTokenBuilder implements TextGrammarParser.GrammarTokenBuilder<NameGrammarToken> {
		@Override
		public NameGrammarToken buildGrammarToken() {
			return new NameGrammarToken();
		}
	}

	private NameInfo nameInfo;
	private String type;
	private ESIndex esIndex;
	public ESIndexWrapper(ESIndex esIndex){
		this.esIndex = esIndex;
		this.type = esIndex.type();


	}
	private void initNameInfo(){
		nameInfo = new NameInfo();
		String name = esIndex.name();
		List<NameGrammarToken> tokens = TextGrammarParser.parser(name, '{', '}',nameGrammarTokenBuilder);
		boolean varibled = false;
		for(int i = 0; tokens != null && i < tokens.size(); i ++){
			TextGrammarParser.GrammarToken token = tokens.get(i);
			if(token.varibletoken()){
				varibled = true;
				break;
			}
		}
		if(varibled){
			nameInfo.tokens = tokens;
		}
		else{
			nameInfo.name = name;
		}
	}
	public void buildIndexName(StringBuilder builder,ClassUtil.ClassInfo classInfo,Object bean){
		  nameInfo.buildName(builder,classInfo,bean);
	}

	public String buildIndexName(ClassUtil.ClassInfo classInfo,Object bean){
		return nameInfo.buildName(classInfo,bean);
	}
}
