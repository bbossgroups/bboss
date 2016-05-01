package org.frameworkset.soa.list;

import java.io.Serializable;

/**
 * 套餐包渠道关系表
 * 
 * @author yd
 *
 */
public class PackageChannelRelModel extends BaseModel implements Serializable {

	  
    /** 
     * @Fields serialVersionUID TODO 
     */  
        
    private static final long serialVersionUID = 1L;
    private String packageId;// 套餐包id
	private String inNetMode;// 渠道编码
	private String inNetName;// 渠道名称
	private String startDate;// 开始时间
	private String endDate;// 结束时间
	private String state;// 状态
	private String updateStaffId;// 更新人id
	private String updateDepartId;// 更新人部门id
	private String updateDate;// 更新日期

	public String getInNetName() {
		return inNetName;
	}

	public void setInNetName(String inNetName) {
		this.inNetName = inNetName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getInNetMode() {
		return inNetMode;
	}

	public void setInNetMode(String inNetMode) {
		this.inNetMode = inNetMode;
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
