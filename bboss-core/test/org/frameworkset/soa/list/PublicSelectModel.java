package org.frameworkset.soa.list;

import java.io.Serializable;

/***
 * 
 * 
 * 项目名称：CrmPlatform
 * 类名称：PublicSelectModel
 * 类描述：公共的下拉框实体类
 * 创建人：suhaibin
 * 创建时间：2015年8月29日 下午4:31:35
 * 修改人：
 * 修改时间：2015年8月29日 下午4:31:35
 * 修改备注：
 * @version 
 *
 */
public class PublicSelectModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String  optionValue;
    private String  optionText;
    
    public String getOptionValue()
    {
        return optionValue;
    }
    public void setOptionValue(String optionValue)
    {
        this.optionValue = optionValue;
    }
    public String getOptionText()
    {
        return optionText;
    }
    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }
    
    
}
