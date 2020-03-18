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

import org.frameworkset.util.annotations.DateFormateMeta;
import org.frameworkset.util.tokenizer.TextGrammarParser;

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
	public boolean isUseBatchContextIndexName() {
		return useBatchContextIndexName;
	}

	public void setUseBatchContextIndexName(boolean useBatchContextIndexName) {
		this.useBatchContextIndexName = useBatchContextIndexName;
	}

	public boolean isUseBatchContextIndexType() {
		return useBatchContextIndexType;
	}

	public void setUseBatchContextIndexType(boolean useBatchContextIndexType) {
		this.useBatchContextIndexType = useBatchContextIndexType;
	}

	public static interface GetVariableValue{
		public Object getValue(String property);
		public BatchContext getBatchContext();
		public void setBatchContext(BatchContext batchContext);
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
	public String getName(){
		return this.getNameInfo().getName();
	}

	public List<NameGrammarToken>  getNameTokens(){
		return this.getNameInfo().getTokens();
	}

	public boolean isOnlyCurrentDateTimestamp(){
		return this.getNameInfo().isOnlyCurrentDateTimestamp();
	}

	public static class NameInfo{
		/**
		 * 如果是固定的索引名称，则对应的索引名称为name字段对应的值，
		 * 否则索引名称动态从tokens中计算得到一个动态的索引名称
		 */
		protected String name;

		public String getName() {
			return name;
		}

		public boolean isOnlyCurrentDateTimestamp() {
			return onlyCurrentDateTimestamp;
		}

		protected boolean onlyCurrentDateTimestamp;

		private List<NameGrammarToken> tokens;
		public List<NameGrammarToken> getTokens(){
			return tokens;
		}
		public String toString(){
			StringBuilder builder = new StringBuilder();
			builder.append("name:").append(name).append(",onlyCurrentDateTimestamp:").append(onlyCurrentDateTimestamp);
			if(tokens != null )
				builder.append(",tokens:").append(tokens.toString());
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


		public String getType() {
			return type;
		}



		public List<NameGrammarToken> getTokens() {
			return tokens;
		}
		public String toString(){
			StringBuilder builder = new StringBuilder();
			builder.append("type:").append(type);
			if(tokens != null )
				builder.append(",tokens:").append(tokens.toString());
			return builder.toString();
		}



	}
	private static Map<String,String> parserVarinfo(String var){
//		int idx = var.indexOf(",");
		int idx = var.indexOf("=");
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
			idx = var.indexOf(",");
			if(idx > 0){
				String[] datas = var.split(",");
				varinfo.put("field", datas[0].trim());
				varinfo.put("dateformat", datas[1].trim());
			}
			else {
				varinfo.put("field", var);
			}
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
		public String getDateformat(){
			return this.dateformat;
		}
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("fieldName=").append(fieldName).append("|");
			builder.append("dateformat=").append(dateformat).append("|");
			builder.append(super.toString());
			return builder.toString();

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
	private boolean useBatchContextIndexName;
	private boolean useBatchContextIndexType;
	public ESIndexWrapper(ESIndex esIndex){
		this.esIndex = esIndex;
		this.useBatchContextIndexName = esIndex.useBatchContextIndexName();
		this.useBatchContextIndexType = esIndex.useBatchContextIndexType();
		initInfo(esIndex.name(),esIndex.type());


	}
	public ESIndexWrapper(String indexPattern,String typePattern){

		initInfo(indexPattern,typePattern);


	}
	private void initInfo(String name,String type){
		this.index = name;
		this.type = type;
		if(type != null && type.equals(""))
			type = null;
		nameInfo = new NameInfo();
//		String name = esIndex.name();
		List<NameGrammarToken> tokens = TextGrammarParser.parser(index, '{', '}',nameGrammarTokenBuilder);
		boolean varibled = false;
		int varcount = 0;
		NameGrammarToken one = null;
		for(int i = 0; tokens != null && i < tokens.size(); i ++){
			NameGrammarToken token = tokens.get(i);
			if(token.varibletoken()){
				varibled = true;
				varcount ++;
				if(one == null){
					one = token;
				}


			}
		}
		if(varibled){
			nameInfo.tokens = tokens;
			if(varcount == 1){
				if(one.getFieldName() == null ){//only current date timestamp
					nameInfo.onlyCurrentDateTimestamp = true;
				}
			}
		}
		else{
			nameInfo.name = index;
		}
		if (type != null) {
			varcount = 0;
			one = null;
			typeInfo = new TypeInfo();
//			String type = esIndex.type();
			tokens = TextGrammarParser.parser(type, '{', '}', nameGrammarTokenBuilder);
			varibled = false;
			for (int i = 0; tokens != null && i < tokens.size(); i++) {
				NameGrammarToken token = tokens.get(i);
				if (token.varibletoken()) {
					varibled = true;
					varcount ++;
					if(one == null){
						one = token;
					}
				}
			}
			if (varibled) {
				typeInfo.tokens = tokens;
			} else {
				typeInfo.type = type;
			}
		}
	}


	public TypeInfo getTypeInfo(){
		return typeInfo;
	}
}
