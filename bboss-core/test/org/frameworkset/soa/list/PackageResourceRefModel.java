package org.frameworkset.soa.list;

import java.io.Serializable;

/***
 * 
 * 
 * 项目名称：CrmPlatform
 * 类名称：PackageResourceRefModel
 * 类描述：套餐包发布与资源的关系
 * 创建人：suhaibin
 * 创建时间：2015年8月24日 下午8:44:53
 * 修改人：
 * 修改时间：2015年8月24日 下午8:44:53
 * 修改备注：
 * @version 
 *
 */
public class PackageResourceRefModel extends BaseModel implements Serializable
{
    private static final long serialVersionUID = -199995566694537888L;
    private String packageId;//套餐包ID
    private String resType;//资源类型：0-号，1-卡
    private String resTypeCode;//资源大类
    private String levelId;//号码等级：保存号码的等级编码
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
    public String getResType()
    {
        return resType;
    }
    public void setResType(String resType)
    {
        this.resType = resType;
    }
    public String getResTypeCode()
    {
        return resTypeCode;
    }
    public void setResTypeCode(String resTypeCode)
    {
        this.resTypeCode = resTypeCode;
    }
    public String getLevelId()
    {
        return levelId;
    }
    public void setLevelId(String levelId)
    {
        this.levelId = levelId;
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
