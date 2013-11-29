
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
 *         &lt;element name="OFuncArea" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="OPstngDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="OTxtlg" type="{urn:sap-com:document:sap:rfc:functions}char60"/>
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
    "oFuncArea",
    "oPstngDate",
    "oTxtlg"
})
@XmlRootElement(name = "ZfmFiFykzV1Response")
public class ZfmFiFykzV1Response {

    @XmlElement(name = "Fi00O06", required = true)
    protected TableOfZstFykz fi00O06;
    @XmlElement(name = "OFuncArea", required = true)
    protected String oFuncArea;
    @XmlElement(name = "OPstngDate", required = true)
    protected String oPstngDate;
    @XmlElement(name = "OTxtlg", required = true)
    protected String oTxtlg;

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
     * 获取oFuncArea属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOFuncArea() {
        return oFuncArea;
    }

    /**
     * 设置oFuncArea属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOFuncArea(String value) {
        this.oFuncArea = value;
    }

    /**
     * 获取oPstngDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPstngDate() {
        return oPstngDate;
    }

    /**
     * 设置oPstngDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPstngDate(String value) {
        this.oPstngDate = value;
    }

    /**
     * 获取oTxtlg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOTxtlg() {
        return oTxtlg;
    }

    /**
     * 设置oTxtlg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOTxtlg(String value) {
        this.oTxtlg = value;
    }

}
