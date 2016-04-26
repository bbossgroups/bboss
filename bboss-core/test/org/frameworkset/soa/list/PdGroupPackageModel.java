package org.frameworkset.soa.list;

import java.io.Serializable;

/**
 * <em>组合套餐类型</em>
 * @author cl
 * @Date 2015/8/25
 * @version 1.0
 */
public class PdGroupPackageModel extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String packageId;//套餐包id
	private String relation_type_code;//关系类型编码
	private String min_number;//最小成员数
	private String max_number;//最大成员数
	private String samecust_tag;//同客户标记
	private String sameacc_tag;//同帐户标记
	private String update_staff_id;//修改员工
	private String update_depart_id;//修改部门
	private String update_date;//更新日期
	private String state;//状态
	private String packageName;
	private String relation_type_name;
	private String depart_name;
	//private  String operationType;//0：新增， 1：修改，2：删除
	
	/*public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}*/
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getRelation_type_code() {
		return relation_type_code;
	}
	public void setRelation_type_code(String relation_type_code) {
		this.relation_type_code = relation_type_code;
	}
	public String getMin_number() {
		return min_number;
	}
	public void setMin_number(String min_number) {
		this.min_number = min_number;
	}
	public String getMax_number() {
		return max_number;
	}
	public void setMax_number(String max_number) {
		this.max_number = max_number;
	}
	public String getSamecust_tag() {
		return samecust_tag;
	}
	public void setSamecust_tag(String samecust_tag) {
		this.samecust_tag = samecust_tag;
	}
	public String getSameacc_tag() {
		return sameacc_tag;
	}
	public void setSameacc_tag(String sameacc_tag) {
		this.sameacc_tag = sameacc_tag;
	}
	public String getUpdate_staff_id() {
		return update_staff_id;
	}
	public void setUpdate_staff_id(String update_staff_id) {
		this.update_staff_id = update_staff_id;
	}
	public String getUpdate_depart_id() {
		return update_depart_id;
	}
	public void setUpdate_depart_id(String update_depart_id) {
		this.update_depart_id = update_depart_id;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getRelation_type_name() {
		return relation_type_name;
	}
	public void setRelation_type_name(String relation_type_name) {
		this.relation_type_name = relation_type_name;
	}
	public String getDepart_name() {
		return depart_name;
	}
	public void setDepart_name(String depart_name) {
		this.depart_name = depart_name;
	}
	

}
