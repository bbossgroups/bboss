package com.frameworkset;

import java.io.Serializable;
import java.sql.Timestamp;


public class MetadataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1380385031400115783L;
	
	private Timestamp create_time; //创建时间
	private String creator; //创建者
	private String data_type_code; //数据类型编码
	private String metadata_set_code; //共享项集编码
	private String metadata_set_id; //共享项集ID
	private String metadata_set_name; //名称
	private String modifier; //最后修改者
	private Timestamp modify_time; //最后修改时间
	private String remark; //描述
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp createTime) {
		create_time = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getData_type_code() {
		return data_type_code;
	}
	public void setData_type_code(String dataTypeCode) {
		data_type_code = dataTypeCode;
	}
	public String getMetadata_set_code() {
		return metadata_set_code;
	}
	public void setMetadata_set_code(String metadataSetCode) {
		metadata_set_code = metadataSetCode;
	}
	public String getMetadata_set_id() {
		return metadata_set_id;
	}
	public void setMetadata_set_id(String metadataSetId) {
		metadata_set_id = metadataSetId;
	}
	public String getMetadata_set_name() {
		return metadata_set_name;
	}
	public void setMetadata_set_name(String metadataSetName) {
		metadata_set_name = metadataSetName;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Timestamp getModify_time() {
		return modify_time;
	}
	public void setModify_time(Timestamp modifyTime) {
		modify_time = modifyTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "MetadataSet [create_time=" + create_time + ", creator="
				+ creator + ", data_type_code=" + data_type_code
				+ ", metadata_set_code=" + metadata_set_code
				+ ", metadata_set_id=" + metadata_set_id
				+ ", metadata_set_name=" + metadata_set_name + ", modifier="
				+ modifier + ", modify_time=" + modify_time + ", remark="
				+ remark + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result
				+ ((data_type_code == null) ? 0 : data_type_code.hashCode());
		result = prime
				* result
				+ ((metadata_set_code == null) ? 0 : metadata_set_code
						.hashCode());
		result = prime * result
				+ ((metadata_set_id == null) ? 0 : metadata_set_id.hashCode());
		result = prime
				* result
				+ ((metadata_set_name == null) ? 0 : metadata_set_name
						.hashCode());
		result = prime * result
				+ ((modifier == null) ? 0 : modifier.hashCode());
		result = prime * result
				+ ((modify_time == null) ? 0 : modify_time.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetadataSet other = (MetadataSet) obj;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (data_type_code == null) {
			if (other.data_type_code != null)
				return false;
		} else if (!data_type_code.equals(other.data_type_code))
			return false;
		if (metadata_set_code == null) {
			if (other.metadata_set_code != null)
				return false;
		} else if (!metadata_set_code.equals(other.metadata_set_code))
			return false;
		if (metadata_set_id == null) {
			if (other.metadata_set_id != null)
				return false;
		} else if (!metadata_set_id.equals(other.metadata_set_id))
			return false;
		if (metadata_set_name == null) {
			if (other.metadata_set_name != null)
				return false;
		} else if (!metadata_set_name.equals(other.metadata_set_name))
			return false;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		if (modify_time == null) {
			if (other.modify_time != null)
				return false;
		} else if (!modify_time.equals(other.modify_time))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		return true;
	}
	
	

}
