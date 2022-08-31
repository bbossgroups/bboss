package org.frameworkset.util;
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

import com.frameworkset.orm.annotation.ESIndexWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: Elasticsearch 注解信息封装类</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/8/26 11:10
 * @author biaoping.yin
 * @version 1.0
 */
public class ESPropertyDescripts {


	private volatile transient List<ClassUtil.PropertieDescription> esAnnonationProperties = new ArrayList<ClassUtil.PropertieDescription>(10);
	private ESIndexWrapper esIndexWrapper;
	/**
	 * es父id属性标识
	 */
	private volatile transient ClassUtil.PropertieDescription esParentProperty;

	/**
	 * esid属性
	 */
	private volatile transient ClassUtil.PropertieDescription esIdProperty;

	/**
	 @ESVersion
	 protected int version;
	 @ESVersionType
	 protected String versionType;
	 @ESRetryOnConflict
	 protected int retryOnConflict;
	 @ESRouting
	 protected String routing;
	 @ESDocAsUpsert
	 protected boolean docAsUpsert;
	 @ESSource
	 protected boolean returnSource;
	 */
	/**
	 * es父id属性标识
	 */
	private volatile transient ClassUtil.PropertieDescription esVersionProperty;

	private volatile transient ClassUtil.PropertieDescription esMetaVersionProperty;

	private volatile transient ClassUtil.PropertieDescription esMetaIdProperty;
	private volatile transient ClassUtil.PropertieDescription esMetaParentIdProperty;

	/**
	 * esid属性
	 */
	private volatile transient ClassUtil.PropertieDescription esVersionTypeProperty;
	/**
	 * es父id属性标识
	 */
	private volatile transient ClassUtil.PropertieDescription esRetryOnConflictProperty;

	/**
	 * esid属性
	 */
	private volatile transient ClassUtil.PropertieDescription esRoutingProperty;
	/**
	 * es父id属性标识
	 */
	private volatile transient ClassUtil.PropertieDescription esDocAsUpsertProperty;

	/**
	 * esid属性
	 */
	private volatile transient ClassUtil.PropertieDescription esReturnSourceProperty;


	/**
	 * es meta fields属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaFieldsProperty;

	/**
	 * es meta matched_queries属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMatchedQueriesProperty;

	/**
	 * es meta Found属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaFoundProperty;

	/**
	 * es meta Highlight属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaHighlightProperty;

	/**
	 * es meta Index属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaIndexProperty;

	/**
	 * es meta InnerHits属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaInnerHitsProperty;

	/**
	 * es meta Nested属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaNestedProperty;

	/**
	 * es meta node属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaNodeProperty;

	/**
	 * es meta Shard属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaShardProperty;

	/**
	 * es meta Score属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaScoreProperty;

	/**
	 * es meta Sort属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaSortProperty;

	/**
	 * es meta Type属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaTypeProperty;

	/**
	 * es meta explanation属性
	 */
	private volatile transient ClassUtil.PropertieDescription esMetaExplanationProperty;

	private volatile transient ClassUtil.PropertieDescription esMetaSeqNoProperty;

	private volatile transient ClassUtil.PropertieDescription esMetaPrimaryTermProperty;
	private boolean containReadSetProperty;

	public ESIndexWrapper getEsIndexWrapper() {
		return esIndexWrapper;
	}

	public void setEsIndexWrapper(ESIndexWrapper esIndexWrapper) {
		this.esIndexWrapper = esIndexWrapper;
	}

	public ClassUtil.PropertieDescription getEsParentProperty() {
		return esParentProperty;
	}

	public void setEsParentProperty(ClassUtil.PropertieDescription esParentProperty) {
		this.esParentProperty = esParentProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esParentProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esParentProperty);
	}

	public ClassUtil.PropertieDescription getEsIdProperty() {
		return esIdProperty;
	}

	public void setEsIdProperty(ClassUtil.PropertieDescription esIdProperty) {
		this.esIdProperty = esIdProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esIdProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esIdProperty);
	}

	public ClassUtil.PropertieDescription getEsVersionProperty() {
		return esVersionProperty;
	}

	public void setEsVersionProperty(ClassUtil.PropertieDescription esVersionProperty) {
		this.esVersionProperty = esVersionProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esVersionProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esVersionProperty);
	}

	public ClassUtil.PropertieDescription getEsVersionTypeProperty() {
		return esVersionTypeProperty;
	}

	public void setEsVersionTypeProperty(ClassUtil.PropertieDescription esVersionTypeProperty) {
		this.esVersionTypeProperty = esVersionTypeProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esVersionTypeProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esVersionTypeProperty);
	}

	public ClassUtil.PropertieDescription getEsRetryOnConflictProperty() {
		return esRetryOnConflictProperty;
	}

	public void setEsRetryOnConflictProperty(ClassUtil.PropertieDescription esRetryOnConflictProperty) {
		this.esRetryOnConflictProperty = esRetryOnConflictProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esRetryOnConflictProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esRetryOnConflictProperty);
	}

	public ClassUtil.PropertieDescription getEsRoutingProperty() {
		return esRoutingProperty;
	}

	public void setEsRoutingProperty(ClassUtil.PropertieDescription esRoutingProperty) {
		this.esRoutingProperty = esRoutingProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esRoutingProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esRoutingProperty);
	}

	public ClassUtil.PropertieDescription getEsDocAsUpsertProperty() {
		return esDocAsUpsertProperty;
	}

	public void setEsDocAsUpsertProperty(ClassUtil.PropertieDescription esDocAsUpsertProperty) {
		this.esDocAsUpsertProperty = esDocAsUpsertProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esDocAsUpsertProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esDocAsUpsertProperty);
	}

	public ClassUtil.PropertieDescription getEsReturnSourceProperty() {
		return esReturnSourceProperty;
	}

	public void setEsReturnSourceProperty(ClassUtil.PropertieDescription esReturnSourceProperty) {
		this.esReturnSourceProperty = esReturnSourceProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esReturnSourceProperty.isESReadSet();
		this.esAnnonationProperties.add(this.esReturnSourceProperty);
	}
	public List<ClassUtil.PropertieDescription> getEsAnnonationProperties() {
		return esAnnonationProperties;
	}

	public ClassUtil.PropertieDescription getEsMetaFieldsProperty() {
		return esMetaFieldsProperty;
	}

	public void setEsMetaFieldsProperty(ClassUtil.PropertieDescription esMetaFieldsProperty) {
		this.esMetaFieldsProperty = esMetaFieldsProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaFieldsProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaFieldsProperty);
	}

	public void setEsMatchedQueriesProperty(ClassUtil.PropertieDescription esMatchedQueriesProperty) {
		this.esMatchedQueriesProperty = esMatchedQueriesProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMatchedQueriesProperty.isESReadSet();
		this.esAnnonationProperties.add(esMatchedQueriesProperty);
	}

	public ClassUtil.PropertieDescription getEsMatchedQueriesProperty() {
		return esMatchedQueriesProperty;
	}

	public ClassUtil.PropertieDescription getEsMetaFoundProperty() {
		return esMetaFoundProperty;
	}

	public void setEsMetaFoundProperty(ClassUtil.PropertieDescription esMetaFoundProperty) {
		this.esMetaFoundProperty = esMetaFoundProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaFoundProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaFoundProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaHighlightProperty() {
		return esMetaHighlightProperty;
	}

	public void setEsMetaHighlightProperty(ClassUtil.PropertieDescription esMetaHighlightProperty) {
		this.esMetaHighlightProperty = esMetaHighlightProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaHighlightProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaHighlightProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaIndexProperty() {
		return esMetaIndexProperty;
	}

	public void setEsMetaIndexProperty(ClassUtil.PropertieDescription esMetaIndexProperty) {
		this.esMetaIndexProperty = esMetaIndexProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaIndexProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaIndexProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaInnerHitsProperty() {
		return esMetaInnerHitsProperty;
	}

	public void setEsMetaInnerHitsProperty(ClassUtil.PropertieDescription esMetaInnerHitsProperty) {
		this.esMetaInnerHitsProperty = esMetaInnerHitsProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaInnerHitsProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaInnerHitsProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaNestedProperty() {
		return esMetaNestedProperty;
	}

	public void setEsMetaNestedProperty(ClassUtil.PropertieDescription esMetaNestedProperty) {
		this.esMetaNestedProperty = esMetaNestedProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaNestedProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaNestedProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaNodeProperty() {
		return esMetaNodeProperty;
	}

	public void setEsMetaNodeProperty(ClassUtil.PropertieDescription esMetaNodeProperty) {
		this.esMetaNodeProperty = esMetaNodeProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaNodeProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaNodeProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaShardProperty() {
		return esMetaShardProperty;
	}

	public void setEsMetaShardProperty(ClassUtil.PropertieDescription esMetaShardProperty) {
		this.esMetaShardProperty = esMetaShardProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaShardProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaShardProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaScoreProperty() {
		return esMetaScoreProperty;
	}

	public void setEsMetaScoreProperty(ClassUtil.PropertieDescription esMetaScoreProperty) {
		this.esMetaScoreProperty = esMetaScoreProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaScoreProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaScoreProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaSortProperty() {
		return esMetaSortProperty;
	}

	public void setEsMetaSortProperty(ClassUtil.PropertieDescription esMetaSortProperty) {
		this.esMetaSortProperty = esMetaSortProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaSortProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaSortProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaTypeProperty() {
		return esMetaTypeProperty;
	}

	public void setEsMetaTypeProperty(ClassUtil.PropertieDescription esMetaTypeProperty) {
		this.esMetaTypeProperty = esMetaTypeProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaTypeProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaTypeProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaExplanationProperty() {
		return esMetaExplanationProperty;
	}

	public void setEsMetaExplanationProperty(ClassUtil.PropertieDescription esMetaExplanationProperty) {
		this.esMetaExplanationProperty = esMetaExplanationProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaExplanationProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaExplanationProperty);
	}

	public boolean isContainReadSetProperty() {
		return containReadSetProperty;
	}


	public ClassUtil.PropertieDescription getEsMetaVersionProperty() {
		return esMetaVersionProperty;
	}

	public void setEsMetaVersionProperty(ClassUtil.PropertieDescription esMetaVersionProperty) {
		this.esMetaVersionProperty = esMetaVersionProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaVersionProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaVersionProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaIdProperty() {
		return esMetaIdProperty;
	}

	public void setEsMetaIdProperty(ClassUtil.PropertieDescription esMetaIdProperty) {
		this.esMetaIdProperty = esMetaIdProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaIdProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaIdProperty);
	}


	public ClassUtil.PropertieDescription getEsMetaParentIdProperty() {
		return esMetaParentIdProperty;
	}

	public void setEsMetaParentIdProperty(ClassUtil.PropertieDescription esMetaParentIdProperty) {
		this.esMetaParentIdProperty = esMetaParentIdProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaParentIdProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaParentIdProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaSeqNoProperty() {
		return esMetaSeqNoProperty;
	}

	public void setEsMetaSeqNoProperty(ClassUtil.PropertieDescription esMetaSeqNoProperty) {
		this.esMetaSeqNoProperty = esMetaSeqNoProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaSeqNoProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaSeqNoProperty);
	}

	public ClassUtil.PropertieDescription getEsMetaPrimaryTermProperty() {
		return esMetaPrimaryTermProperty;
	}

	public void setEsMetaPrimaryTermProperty(ClassUtil.PropertieDescription esMetaPrimaryTermProperty) {
		this.esMetaPrimaryTermProperty = esMetaPrimaryTermProperty;
		if(!containReadSetProperty)
			containReadSetProperty = esMetaPrimaryTermProperty.isESReadSet();
		this.esAnnonationProperties.add(esMetaPrimaryTermProperty);
	}
}
