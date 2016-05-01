/**
 * 功能说明：套餐包--基本信息
 *
 * 修改说明：新增
 * 修改时间：2015-8.24
 * 修  改  人：YAOJIAN
 */
package org.frameworkset.soa.list;

import java.io.Serializable;

public class BaseInfoModel extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2015082511301231L;
	

	/**
	 * 判断是否新增标识的package id
	 */
	private String id;
	
	/**
	 * 套餐包ID
	 */
	private Integer packageId;

	/**
	 * 套餐包名称
	 */
	private String name;
	
	/**
	 * 套餐包类型：0-主套餐包，1-合约套餐包，2-上网卡套餐包
	 */
	private String type;
	
	/**
	 * 套餐包描述
	 */
	private String desc;
	
	/**
	 * 网别：20-2G，30-3G，40-4G
	 */
	private String netType;
	
	/**
	 * 品牌
	 */
	private String brand;
	
	/**
	 * 生效日期
	 */
	private String startDate;
	
	/**
	 * 失效日期
	 */
	private String endDate;
	
	/**
	 * 生效方式：0-立即，1-按照偏移值和偏移单位生效
	 */
	private String startTag;
	
	/**
	 * 生效偏移值
	 */
	private Integer startOffset;
	
	/**
	 * 生效偏移单位：0:天 1:自然天 2:月 3:自然月
	 */
	private String startUnit;
	
	/**
	 * 失效方式：0-立即，1-按照偏移值和偏移单位生效
	 */
	private String endTag;
	
	/**
	 * 失效偏移值
	 */
	private Integer endOffset;
	
	/**
	 * 失效偏移单位：0:天 1:自然天 2:月 3:自然月 4:年 5:自然年
	 */
	private String endUnit;
	
	/**
	 * 付费标识：0-后付费，1-预付费，2-准预付费
	 */
	private String prepay;
	
	/**
	 * 创建时间
	 */
	private String createDate;
	
	/**
	 * 接入类产品编码
	 */
	private Integer mainProdId;
	
	/**
	 * 是否自由组合套餐：0-非自由组合套餐，1-自由组合套餐
	 */
	private String compTag;
	
	/**
	 * 是否融合套餐：0—非融合套餐，1—融合套餐
	 */
	private String groupTag;
	
	/**
	 * 是否默认展开：0-默认不展开，1-默认展开
	 */
	private String needExp;
	
	/**
	 * 版本号
	 */
	private String version;
	
	/**
	 * 套餐状态：0-正常在用，1-已废弃
	 */
	private String state;
	
	/**
	 * 修改人
	 */
	private String staffId;
	
	/**
	 * 修改部门
	 */
	private String departId;
	
	/**
	 * 修改时间
	 */
	private String updateDate;
	
	/**
	 * 费用值：以分为单位
	 */
	private double fee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getStartTag() {
		return startTag;
	}

	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	public String getStartUnit() {
		return startUnit;
	}

	public void setStartUnit(String startUnit) {
		this.startUnit = startUnit;
	}

	public String getEndTag() {
		return endTag;
	}

	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}

	public String getEndUnit() {
		return endUnit;
	}

	public void setEndUnit(String endUnit) {
		this.endUnit = endUnit;
	}

	public String getPrepay() {
		return prepay;
	}

	public void setPrepay(String prepay) {
		this.prepay = prepay;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getCompTag() {
		return compTag;
	}

	public void setCompTag(String compTag) {
		this.compTag = compTag;
	}

	public String getGroupTag() {
		return groupTag;
	}

	public void setGroupTag(String groupTag) {
		this.groupTag = groupTag;
	}

	public String getNeedExp() {
		return needExp;
	}

	public void setNeedExp(String needExp) {
		this.needExp = needExp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(Integer startOffset) {
		this.startOffset = startOffset;
	}

	public Integer getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(Integer endOffset) {
		this.endOffset = endOffset;
	}

	public Integer getMainProdId() {
		return mainProdId;
	}

	public void setMainProdId(Integer mainProdId) {
		this.mainProdId = mainProdId;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
