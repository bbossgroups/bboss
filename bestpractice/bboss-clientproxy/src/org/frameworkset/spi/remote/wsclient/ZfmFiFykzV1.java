
package org.frameworkset.spi.remote.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Fi00O06" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfZstFykz"/>
 *         &lt;element name="IFuncArea" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="IPstngDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="ITxtlg" type="{urn:sap-com:document:sap:rfc:functions}char60"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fi00O06",
    "iFuncArea",
    "iPstngDate",
    "iTxtlg"
})
@XmlRootElement(name = "ZfmFiFykzV1")
public class ZfmFiFykzV1 {

    @XmlElement(name = "Fi00O06", required = true)
    protected TableOfZstFykz fi00O06;
    @XmlElement(name = "IFuncArea", required = true)
    protected String iFuncArea;
    @XmlElement(name = "IPstngDate", required = true)
    protected String iPstngDate;
    @XmlElement(name = "ITxtlg", required = true)
    protected String iTxtlg;

    /**
     * 获取fi00O06属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfZstFykz }
     *     
     */
    public TableOfZstFykz getFi00O06() {
        return fi00O06;
    }

    /**
     * 设置fi00O06属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfZstFykz }
     *     
     */
    public void setFi00O06(TableOfZstFykz value) {
        this.fi00O06 = value;
    }

    /**
     * 获取iFuncArea属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIFuncArea() {
        return iFuncArea;
    }

    /**
     * 设置iFuncArea属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIFuncArea(String value) {
        this.iFuncArea = value;
    }

    /**
     * 获取iPstngDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPstngDate() {
        return iPstngDate;
    }

    /**
     * 设置iPstngDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPstngDate(String value) {
        this.iPstngDate = value;
    }

    /**
     * 获取iTxtlg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITxtlg() {
        return iTxtlg;
    }

    /**
     * 设置iTxtlg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITxtlg(String value) {
        this.iTxtlg = value;
    }

}
