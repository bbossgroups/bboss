
package org.frameworkset.spi.remote.wsclient;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ZstFykz complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ZstFykz">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GlAccount" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Txtsh" type="{urn:sap-com:document:sap:rfc:functions}char60"/>
 *         &lt;element name="DebCreLc" type="{urn:sap-com:document:sap:rfc:functions}curr17.2"/>
 *         &lt;element name="LocCurrcy" type="{urn:sap-com:document:sap:rfc:functions}cuky5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZstFykz", propOrder = {
    "glAccount",
    "txtsh",
    "debCreLc",
    "locCurrcy"
})
public class ZstFykz {

    @XmlElement(name = "GlAccount", required = true)
    protected String glAccount;
    @XmlElement(name = "Txtsh", required = true)
    protected String txtsh;
    @XmlElement(name = "DebCreLc", required = true)
    protected BigDecimal debCreLc;
    @XmlElement(name = "LocCurrcy", required = true)
    protected String locCurrcy;

    /**
     * 获取glAccount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlAccount() {
        return glAccount;
    }

    /**
     * 设置glAccount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlAccount(String value) {
        this.glAccount = value;
    }

    /**
     * 获取txtsh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxtsh() {
        return txtsh;
    }

    /**
     * 设置txtsh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxtsh(String value) {
        this.txtsh = value;
    }

    /**
     * 获取debCreLc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebCreLc() {
        return debCreLc;
    }

    /**
     * 设置debCreLc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebCreLc(BigDecimal value) {
        this.debCreLc = value;
    }

    /**
     * 获取locCurrcy属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocCurrcy() {
        return locCurrcy;
    }

    /**
     * 设置locCurrcy属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocCurrcy(String value) {
        this.locCurrcy = value;
    }

}
