package org.frameworkset.soa.list;

import java.io.Serializable;

/**
 * 套餐包地域关系表
 * @author yd
 *
 */
public class PackageReleaseRelModel extends BaseModel implements Serializable {

	  
    /** 
     * @Fields serialVersionUID TODO 
     */  
        
    private static final long serialVersionUID = 1L;
    private String packageId;//套餐包id
	private String areaLevel;//地域等级
	private String areaCode;//地域编码
	private String areaName;//地域名称
	private String state;//状态
	private String startDate;//开始时间
	private String endDate;//结束时间
	private String updateStaffId;//更新人id
	private String updateDepartId;//更新人部门id
	private String updateDate;//更新日期
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
 
 
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getAreaLevel() {
		return areaLevel;
	}
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
