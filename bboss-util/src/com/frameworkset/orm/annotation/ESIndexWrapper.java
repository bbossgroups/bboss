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

import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.annotations.DateFormateMeta;
import org.frameworkset.util.tokenizer.TextGrammarParser;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/5/5 23:10
 * @author biaoping.yin
 * @version 1.0
 */
public class ESIndexWrapper {
	public static interface GetVariableValue{
		public Object getValue(String property);
	}

	public NameInfo getNameInfo() {
		return nameInfo;
	}

	public void setNameInfo(NameInfo nameInfo) {
		this.nameInfo = nameInfo;
	}


	public ESIndex getEsIndex() {
		return esIndex;
	}

	public void setEsIndex(ESIndex esIndex) {
		this.esIndex = esIndex;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static class NameInfo{
		/**
		 * 如果是固定的索引名称，则对应的索引名称为name字段对应的值，
		 * 否则索引名称动态从tokens中计算得到一个动态的索引名称
		 */
		protected String name;

		private List<NameGrammarToken> tokens;
		private void buildName(Writer writer,GetVariableValue getVariableValue) throws IOException {
			if(name != null){
				writer.write(name);
				return;
			}
			if(tokens == null || tokens.size() == 0){
				return;
			}
			NameGrammarToken nameGrammarToken = null;
			for(int i = 0; i < tokens.size(); i ++){
				nameGrammarToken = tokens.get(i);
				if(!nameGrammarToken.varibletoken()) {
					writer.write(nameGrammarToken.getText());
				}
				else{
					if(nameGrammarToken.getFieldName() != null) {

//						Object va = classInfo.getPropertyValue(bean, nameGrammarToken.getFieldName());
						Object va = getVariableValue.getValue(nameGrammarToken.getFieldName());
						if (va == null)
							throw new NameParserException(new StringBuilder()
									.append(this.toString())
									.append(",property[")
									.append(nameGrammarToken.getFieldName()).append("] is null.").toString());
						if (nameGrammarToken.dateformat != null) {
							DateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(nameGrammarToken.dateformat);
							if (va instanceof Date) {
								writer.write(dateFormat.format((Date) va));
							} else if (va instanceof Long) {
								writer.write(dateFormat.format(new Date((Long) va)));

							} else {
								writer.write(String.valueOf(va));
							}
						} else {
							writer.write(String.valueOf(va));
						}
					}
					else{ //取当前时间作为索引名称
						DateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(nameGrammarToken.dateformat);
						Date date = new Date();
						writer.write(dateFormat.format(date));

					}
				}
			}
		}


		private void buildName(StringBuilder builder,GetVariableValue getVariableValue){
			if(name != null){
				builder.append(name);
				return;
			}
			if(tokens == null || tokens.size() == 0){
				return;
			}
			NameGrammarToken nameGrammarToken = null;
			for(int i = 0; i < tokens.size(); i ++){
				nameGrammarToken = tokens.get(i);
				if(!nameGrammarToken.varibletoken()) {
					builder.append(nameGrammarToken.getText());
				}
				else{
					if(nameGrammarToken.getFieldName() != null) {

//						Object va = classInfo.getPropertyValue(bean, nameGrammarToken.getFieldName());
						Object va = getVariableValue.getValue(nameGrammarToken.getFieldName());
						if (va == null)
							throw new NameParserException(new StringBuilder()
									.append(this.toString())
									.append(",property[")
									.append(nameGrammarToken.getFieldName()).append("] is null.").toString());
						if (nameGrammarToken.dateformat != null) {
							DateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(nameGrammarToken.dateformat);
							if (va instanceof Date) {
								builder.append(dateFormat.format((Date) va));
							} else if (va instanceof Long) {
								builder.append(dateFormat.format(new Date((Long) va)));

							} else {
								builder.append(va);
							}
						} else {
							builder.append(va);
						}
					}
					else{ //取当前时间作为索引名称
						DateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(nameGrammarToken.dateformat);
						Date date = new Date();
						builder.append(dateFormat.format(date));

					}
				}
			}
		}

		/**
		 * ClassUtil.ClassInfo classInfo, Object bean

		 * @return
		 */
		public String buildName(GetVariableValue getVariableValue){
			if(name == null || name.equals("")){
				if(tokens == null  || tokens.size() == 0 )
					return null;
			}
			else{
				return name;
			}
			StringBuilder builder = new StringBuilder();
			buildName(builder,  getVariableValue);
			return builder.toString();
		}

	}

	public static class TypeInfo{
		/**
		 * 如果是固定的索引名称，则对应的索引名称为name字段对应的值，
		 * 否则索引名称动态从tokens中计算得到一个动态的索引名称
		 */
		private String type;
		private List<NameGrammarToken> tokens;
		/**
		 * ClassUtil.ClassInfo classInfo, Object bean
		 * @param writer
		 * @param getVariableValue
		 */
		private void buildType(Writer writer,GetVariableValue getVariableValue) throws IOException {
			if(type != null){
				writer.write(type);
				return;
			}
			if(tokens == null || tokens.size() == 0){
				return;
			}
			NameGrammarToken nameGrammarToken = null;
			for(int i = 0; i < tokens.size(); i ++){
				nameGrammarToken = tokens.get(i);
				if(!nameGrammarToken.varibletoken()) {
					writer.write(nameGrammarToken.getText());
				}
				else{
//					Object va = classInfo.getPropertyValue(bean,nameGrammarToken.getFieldName());
					Object va = getVariableValue.getValue(nameGrammarToken.getFieldName());
					if(va == null)
						throw new NameParserException(new StringBuilder()
								.append(this.toString())
								.append(",property[")
								.append(nameGrammarToken.getFieldName()).append("] is null.").toString());
					writer.write(String.valueOf(va));
				}
			}


		}

		/**
		 * ClassUtil.ClassInfo classInfo, Object bean
		 * @param builder
		 * @param getVariableValue
		 */
		private void buildType(StringBuilder builder,GetVariableValue getVariableValue){
			if(type != null){
				builder.append(type);
				return;
			}
			if(tokens == null || tokens.size() == 0){
				return;
			}
			NameGrammarToken nameGrammarToken = null;
			for(int i = 0; i < tokens.size(); i ++){
				nameGrammarToken = tokens.get(i);
				if(!nameGrammarToken.varibletoken()) {
					builder.append(nameGrammarToken.getText());
				}
				else{
//					Object va = classInfo.getPropertyValue(bean,nameGrammarToken.getFieldName());
					Object va = getVariableValue.getValue(nameGrammarToken.getFieldName());
					if(va == null)
						throw new NameParserException(new StringBuilder()
								.append(this.toString())
								.append(",property[")
								.append(nameGrammarToken.getFieldName()).append("] is null.").toString());
					builder.append(va);
				}
			}


		}

		/**
		 * ClassUtil.ClassInfo classInfo, Object bean
		 * @param getVariableValue
		 * @return
		 */
		public String buildType(GetVariableValue getVariableValue){
			if(type == null || type.equals("")){
				if(tokens == null  || tokens.size() == 0 )
					return null;
			}
			else{
				return type;
			}
			StringBuilder builder = new StringBuilder();
			buildType(builder,getVariableValue);
			return builder.toString();
		}

	}
	private static Map<String,String> parserVarinfo(String var){
//		int idx = var.indexOf(",");
		int idx = var.indexOf(",");
		Map<String,String> varinfo = new HashMap();
		if(idx > 0) {
			String[] arr = var.split(",");

			for (int i = 0; i < arr.length; i++) {
				String e = arr[i];
				String[] attr = e.split("=");
				if (attr.length == 1)
					throw new NameParserException(new StringBuilder()
							.append("varible syntax error:")
							.append(var)
							.append(",")
							.append(e)
							.append(" must be a express like xxx=vvvv").toString());
				varinfo.put(attr[0], attr[1]);
			}

		}
		else{
			varinfo.put("field",var);
		}
		return varinfo;

	}
	public static class NameGrammarToken extends TextGrammarParser.GrammarToken{
		protected String fieldName;
		protected DateFormateMeta dateFormateMeta;
		protected String dateformat;
		@Override
		public void after() {
			try {
				if (this.varibletoken()) {

					Map<String, String> varinfo = parserVarinfo(text);
					fieldName = varinfo.get("field");
					dateformat = varinfo.get("dateformat");
					if (dateformat != null) {
						dateFormateMeta = DateFormateMeta.buildDateFormateMeta(dateformat);
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
	private TypeInfo typeInfo;
	private String index;
	private String type;
	private ESIndex esIndex;
	public ESIndexWrapper(ESIndex esIndex){
		this.esIndex = esIndex;
		initInfo(esIndex.name(),esIndex.type());


	}
	public ESIndexWrapper(String indexPattern,String typePattern){

		initInfo(indexPattern,typePattern);


	}
	private void initInfo(String name,String type){
		this.index = name;
		this.type = type;
		nameInfo = new NameInfo();
//		String name = esIndex.name();
		List<NameGrammarToken> tokens = TextGrammarParser.parser(index, '{', '}',nameGrammarTokenBuilder);
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
			nameInfo.name = index;
		}
		if (type != null) {
			typeInfo = new TypeInfo();
//			String type = esIndex.type();
			tokens = TextGrammarParser.parser(type, '{', '}', nameGrammarTokenBuilder);
			varibled = false;
			for (int i = 0; tokens != null && i < tokens.size(); i++) {
				TextGrammarParser.GrammarToken token = tokens.get(i);
				if (token.varibletoken()) {
					varibled = true;
					break;
				}
			}
			if (varibled) {
				typeInfo.tokens = tokens;
			} else {
				typeInfo.type = type;
			}
		}
	}
	public void buildIndexName(Writer writer, GetVariableValue getVariableValue) throws IOException {
		nameInfo.buildName(writer,  getVariableValue);
	}
	public void buildIndexName(StringBuilder builder,GetVariableValue getVariableValue){
		  nameInfo.buildName(builder,  getVariableValue);
	}

	public String buildIndexName(GetVariableValue getVariableValue){
		return nameInfo.buildName(  getVariableValue);
	}
	public void buildIndexType(Writer writer,GetVariableValue getVariableValue) throws IOException {
		typeInfo.buildType(  writer,  getVariableValue);
	}
	public void buildIndexType(StringBuilder builder,GetVariableValue getVariableValue){
		typeInfo.buildType(builder,  getVariableValue);
	}

	public String buildIndexType(GetVariableValue getVariableValue){
		return typeInfo.buildType(  getVariableValue);
	}

	public TypeInfo getTypeInfo(){
		return typeInfo;
	}
}
