/**
 * 功能说明：套餐包--组件关系标识
 *
 * 修改说明：新增
 * 修改时间：2015-8.24
 * 修  改  人：YAOJIAN
 */
package org.frameworkset.soa.list;

import java.io.Serializable;

public class ComponentRelModel extends BaseModel implements Serializable {
	private static final long serialVersionUID = 2015082511301231L;
	
	/**
	 * 包标识
	 */
	private String packageId;
	
	/**
	 * 组件标识
	 */
	private String componentId;
	
	/**
	 * 组件名称
	 */
	private String name;

	/**
	 * '必选标记：0-必选，1-可选，2-默认选中'
	 */
	private String forceTag;
	
	/**
	 * 生效日期
	 */
	private String startDate;
	
	/**
	 * 失效日期
	 */
	private String endDate;

	/**
	 * 修改人
	 */
	private String staffId;

	/**
	 * 修改部门
	 */
	private String departId;

	/**
	 * 修改日期
	 */
	private String updateDate;
	
	/**
	 * 关系状态：0-正常在用，1-已废弃
	 */
	private String state;;
	
	/**
	 * 排序
	 */
	private String itemIndex;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getForceTag() {
		return forceTag;
	}

	public void setForceTag(String forceTag) {
		this.forceTag = forceTag;
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

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(String itemIndex) {
		this.itemIndex = itemIndex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
