package org.frameworkset.soa.list;

import java.io.Serializable;

/***
 * 
 * 
 * 项目名称：CrmPlatform
 * 类名称：PackageGroupRefModel
 * 类描述：套餐包发布与群组的关系
 * 创建人：suhaibin
 * 创建时间：2015年8月24日 下午8:59:26
 * 修改人：
 * 修改时间：2015年8月24日 下午8:59:26
 * 修改备注：
 * @version 
 *
 */
public class PackageGroupRefModel  extends BaseModel implements Serializable
{
    private static final long serialVersionUID = -19999999945367789L;
    private String packageId;//套餐包ID
    private String groupType;//群组类型
    private String groupCode;//群组编码
    private String state;//关系状态
    private String startDate;//开始日期
    private String endDate;//结束日期
    private String updateStaffId;//修改人
    private String updateDepartId;//修改部门
    private String updateDate;//修改日期
    
    private String groupName;//群组名称
    
    public String getPackageId()
    {
        return packageId;
    }
    public void setPackageId(String packageId)
    {
        this.packageId = packageId;
    }
    public String getGroupType()
    {
        return groupType;
    }
    public void setGroupType(String groupType)
    {
        this.groupType = groupType;
    }
    public String getGroupCode()
    {
        return groupCode;
    }
    public void setGroupCode(String groupCode)
    {
        this.groupCode = groupCode;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }
    public String getStartDate()
    {
        return startDate;
    }
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }
    public String getEndDate()
    {
        return endDate;
    }
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    public String getUpdateStaffId()
    {
        return updateStaffId;
    }
    public void setUpdateStaffId(String updateStaffId)
    {
        this.updateStaffId = updateStaffId;
    }
    public String getUpdateDepartId()
    {
        return updateDepartId;
    }
    public void setUpdateDepartId(String updateDepartId)
    {
        this.updateDepartId = updateDepartId;
    }
    public String getUpdateDate()
    {
        return updateDate;
    }
    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }
    public String getGroupName()
    {
        return groupName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    
    
}
