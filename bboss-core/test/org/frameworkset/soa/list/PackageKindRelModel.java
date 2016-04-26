/**
 * 功能说明：分类关系
 * 
 * 修改说明：新增
 * 修改时间：2015-8.24
 * 修  改  人：李阳
 */
package org.frameworkset.soa.list;

import java.io.Serializable;

public class PackageKindRelModel implements Serializable {
	  
    /** 
     * @Fields serialVersionUID TODO 
     */  
        
    private static final long serialVersionUID = 1L;
    private String package_kind_code;// 套餐分类名称
	private String package_kind_name;//套餐分类名称
	private String package_id;// 套餐id
	private String start_date;// 开始时间
	private String end_date;// 结束时间
	private String state;// 状态
	private String update_staff_id;// 修改人工号
	private String update_depart_id;// 修改人部门
	private String update_date;// 修改时间
	private String operation_type;// 操作类型

	
	public String getPackage_kind_name() {
		return package_kind_name;
	}

	public void setPackage_kind_name(String package_kind_name) {
		this.package_kind_name = package_kind_name;
	}

	public String getPackage_kind_code() {
		return package_kind_code;
	}

	public void setPackage_kind_code(String package_kind_code) {
		this.package_kind_code = package_kind_code;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}

}
