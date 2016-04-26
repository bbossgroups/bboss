package org.frameworkset.soa.list;

import java.io.Serializable;

/**
 * <em>集团套餐和成员套餐关系</em>
 * @author cl
 * @Date 2015/8/25
 * @version 1.0
 */
public class PdPackageRelModel extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String package_id_a;//集团套餐ID
	private String package_id_b;//成员套餐ID
	private String rel_type;//关系类型
	private String role_code;//角色编码
	private String state;//状态
	private String start_date;//开始日期
	private String end_date;//结束日期
	private String update_staff_id;//更新工号
	private String update_depart_id;//更新部门
	private String update_date;//更新日期
	private String group_user_type;//集团用户类型
	private String package_a_name;//
	private String package_b_name;
	private String depart_name;
	
	public String getPackage_id_a() {
		return package_id_a;
	}
	public void setPackage_id_a(String package_id_a) {
		this.package_id_a = package_id_a;
	}
	public String getPackage_id_b() {
		return package_id_b;
	}
	public void setPackage_id_b(String package_id_b) {
		this.package_id_b = package_id_b;
	}
	public String getRel_type() {
		return rel_type;
	}
	public void setRel_type(String rel_type) {
		this.rel_type = rel_type;
	}
	public String getRole_code() {
		return role_code;
	}
	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
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
	public String getGroup_user_type() {
		return group_user_type;
	}
	public void setGroup_user_type(String group_user_type) {
		this.group_user_type = group_user_type;
	}
	public String getPackage_a_name() {
		return package_a_name;
	}
	public void setPackage_a_name(String package_a_name) {
		this.package_a_name = package_a_name;
	}
	public String getPackage_b_name() {
		return package_b_name;
	}
	public void setPackage_b_name(String package_b_name) {
		this.package_b_name = package_b_name;
	}
	public String getDepart_name() {
		return depart_name;
	}
	public void setDepart_name(String depart_name) {
		this.depart_name = depart_name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
		
}
