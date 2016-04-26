/**
 * 功能说明：转换分类
 * 修改说明：新增
 * 修改时间：2015-8.24
 * 修  改  人：李阳
 */
package org.frameworkset.soa.list;
import java.io.Serializable;
public class  PackageTransRelModel implements Serializable{
	  
    /** 
     * @Fields serialVersionUID TODO 
     */  
        
    private static final long serialVersionUID = 1L;
    private String package_id_a;//转化套餐ID
	private String package_id_b;//被转化套餐ID
	private String package_name;//被转化套餐名称
	private String both_tag;// 是否双向互转
	private String start_date; // 起始日期
	private String end_date;//  结束日期
	private String update_staff_id;// 修改人
	private String  update_depart_id;// 修改人部门
	private String  update_date;//修改日期
	private String trans_tag;// 是否允许变更
	private String  state;//  状态
	private String  operation_type;//新增操作类型
	
	
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
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
	public String getBoth_tag() {
		return both_tag;
	}
	public void setBoth_tag(String both_tag) {
		this.both_tag = both_tag;
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

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}
	public String getTrans_tag() {
		return trans_tag;
	}
	public void setTrans_tag(String trans_tag) {
		this.trans_tag = trans_tag;
	}
	
	
}
