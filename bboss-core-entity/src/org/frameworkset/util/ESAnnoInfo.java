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

/**
 * <p>Description: Elasticsearch 注解信息封装类</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/8/26 11:10
 * @author biaoping.yin
 * @version 1.0
 */
public class ESAnnoInfo {
	/**
	 * es控制参数
	 */
//	private boolean persistentESId;
//	private boolean esIdReadSet;
//	private boolean persistentESParentId;
//	private boolean esParentIdReadSet;
//	private boolean persistentESVersion;
//	private boolean esVersionReadSet;
//	private boolean persistentESVersionType;
//	private boolean persistentESRetryOnConflict;
//	private boolean persistentESRouting;
//	private boolean esRoutingReadSet;
//	private boolean persistentESDocAsUpsert;
//	private boolean persistentESSource;
	private boolean persistent;
	private boolean readSet;
//	/**
//	 * es检索元数据参数
//	 */
//
//
//	public boolean isPersistentESId() {
//		return persistentESId;
//	}
//
//	public void setPersistentESId(boolean persistentESId) {
//		this.persistentESId = persistentESId;
//	}
//
//	public boolean isEsIdReadSet() {
//		return esIdReadSet;
//	}
//
//	public void setEsIdReadSet(boolean esIdReadSet) {
//		this.esIdReadSet = esIdReadSet;
//	}
//
//	public boolean isPersistentESParentId() {
//		return persistentESParentId;
//	}
//
//	public void setPersistentESParentId(boolean persistentESParentId) {
//		this.persistentESParentId = persistentESParentId;
//	}
//
//	public boolean isEsParentIdReadSet() {
//		return esParentIdReadSet;
//	}
//
//	public void setEsParentIdReadSet(boolean esParentIdReadSet) {
//		this.esParentIdReadSet = esParentIdReadSet;
//	}
//
//	public boolean isPersistentESVersion() {
//		return persistentESVersion;
//	}
//
//	public void setPersistentESVersion(boolean persistentESVersion) {
//		this.persistentESVersion = persistentESVersion;
//	}
//
//	public boolean isPersistentESVersionType() {
//		return persistentESVersionType;
//	}
//
//	public void setPersistentESVersionType(boolean persistentESVersionType) {
//		this.persistentESVersionType = persistentESVersionType;
//	}
//
//	public boolean isPersistentESRetryOnConflict() {
//		return persistentESRetryOnConflict;
//	}
//
//	public void setPersistentESRetryOnConflict(boolean persistentESRetryOnConflict) {
//		this.persistentESRetryOnConflict = persistentESRetryOnConflict;
//	}
//
//	public boolean isPersistentESRouting() {
//		return persistentESRouting;
//	}
//
//	public void setPersistentESRouting(boolean persistentESRouting) {
//		this.persistentESRouting = persistentESRouting;
//	}
//
//	public boolean isPersistentESDocAsUpsert() {
//		return persistentESDocAsUpsert;
//	}
//
//	public void setPersistentESDocAsUpsert(boolean persistentESDocAsUpsert) {
//		this.persistentESDocAsUpsert = persistentESDocAsUpsert;
//	}
//
//	public boolean isPersistentESSource() {
//		return persistentESSource;
//	}
//
//	public void setPersistentESSource(boolean persistentESSource) {
//		this.persistentESSource = persistentESSource;
//	}
//
//	public boolean isEsVersionReadSet() {
//		return esVersionReadSet;
//	}
//
//	public void setEsVersionReadSet(boolean esVersionReadSet) {
//		this.esVersionReadSet = esVersionReadSet;
//	}
//
//	public boolean isEsRoutingReadSet() {
//		return esRoutingReadSet;
//	}
//
//	public void setEsRoutingReadSet(boolean esRoutingReadSet) {
//		this.esRoutingReadSet = esRoutingReadSet;
//	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public boolean isReadSet() {
		return readSet;
	}

	public void setReadSet(boolean readSet) {
		this.readSet = readSet;
	}
}
