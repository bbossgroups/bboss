package org.frameworkset.soa.list;

import java.io.Serializable;

/**
 * 套餐包权限关系表
 * 
 * @author yd
 *
 */
public class PackageRightRelModel extends BaseModel implements Serializable {

	  
    /** 
     * @Fields serialVersionUID TODO 
     */  
        
    private static final long serialVersionUID = 1L;
    private String packageId;// 套餐包id
	private String rightCode;// 权限编码
	private String rightName; //权限名称
	private String startDate;// 开始时间
	private String endDate;// 结束时间
	private String state;// 状态
	private String updateStaffId;// 更新人id
	private String updateDepartId;// 更新人部门id
	private String updateDate;// 更新日期
 

	
	public String getRightName() {
		return rightName;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getRightCode() {
		return rightCode;
	}

	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdateStaffId() {
		return updateStaffId;
	}

	public void setUpdateStaffId(String updateStaffId) {
		this.updateStaffId = updateStaffId;
	}

	public String getUpdateDepartId() {
		return updateDepartId;
	}

	public void setUpdateDepartId(String updateDepartId) {
		this.updateDepartId = updateDepartId;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

}
