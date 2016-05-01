package org.frameworkset.soa.list;

import java.io.Serializable;

/***
 * 
 * 
 * 项目名称：CrmPlatform
 * 类名称：PackageProviderRefModel
 * 类描述：套餐包发布与运营商的关系
 * 创建人：suhaibin
 * 创建时间：2015年8月24日 下午8:44:53
 * 修改人：
 * 修改时间：2015年8月24日 下午8:44:53
 * 修改备注：
 * @version 
 *
 */
public class PackageProviderRefModel extends BaseModel implements Serializable
{
    private static final long serialVersionUID = -1999999994537888L;
    private String packageId;//套餐包ID
    private String providerCode;//运营商编码   CUC  联通   CTC  电信  CMC 移动
    private String state;//关系状态
    private String startDate;//开始日期
    private String endDate;//结束日期
    private String updateStaffId;//修改人
    private String updateDepartId;//修改部门
    private String updateDate;//修改日期
    
    public String getPackageId()
    {
        return packageId;
    }
    public void setPackageId(String packageId)
    {
        this.packageId = packageId;
    }
    public String getProviderCode()
    {
        return providerCode;
    }
    public void setProviderCode(String providerCode)
    {
        this.providerCode = providerCode;
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
    
    
}
