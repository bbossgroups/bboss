package org.frameworkset.soa.list;

import java.io.Serializable;

/***
 * 
 * 
 * 项目名称：CrmPlatform
 * 类名称：PubGroupDefModel
 * 类描述：群组定义实体类
 * 创建人：suhaibin
 * 创建时间：2015年8月29日 上午11:14:09
 * 修改人：
 * 修改时间：2015年8月29日 上午11:14:09
 * 修改备注：
 * @version 
 *
 */
public class PubGroupDefModel   implements Serializable
{
    private static final long serialVersionUID = -1999656745367789L;
    
    private String groupCode;//群组编码
    private String groupName;//群组名称
    private String groupType;//群组类型
    private String groupDesc;//群组描述
    private String state;//关系状态
    private String updateStaffId;//修改人
    private String updateDepartId;//修改部门
    private String updateDate;//修改日期
    
    public String getGroupCode()
    {
        return groupCode;
    }
    public void setGroupCode(String groupCode)
    {
        this.groupCode = groupCode;
    }
    public String getGroupName()
    {
        return groupName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    public String getGroupType()
    {
        return groupType;
    }
    public void setGroupType(String groupType)
    {
        this.groupType = groupType;
    }
    public String getGroupDesc()
    {
        return groupDesc;
    }
    public void setGroupDesc(String groupDesc)
    {
        this.groupDesc = groupDesc;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
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
