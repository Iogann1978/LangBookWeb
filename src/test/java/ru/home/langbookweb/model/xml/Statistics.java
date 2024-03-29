//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.21 at 05:43:40 PM MSK 
//


package ru.home.langbookweb.model.xml;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="shown" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="answered" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="defectiveMeaningsQuantity" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="suspendedMeaningsQuantity" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="readyMeaningsQuantity" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="activeMeaningsQuantity" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="learnedMeaningsQuantity" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "statistics")
public class Statistics {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "status")
    protected Byte status;
    @XmlAttribute(name = "shown")
    protected Byte shown;
    @XmlAttribute(name = "answered")
    protected Byte answered;
    @XmlAttribute(name = "defectiveMeaningsQuantity")
    protected Byte defectiveMeaningsQuantity;
    @XmlAttribute(name = "suspendedMeaningsQuantity")
    protected Byte suspendedMeaningsQuantity;
    @XmlAttribute(name = "readyMeaningsQuantity")
    protected Byte readyMeaningsQuantity;
    @XmlAttribute(name = "activeMeaningsQuantity")
    protected Byte activeMeaningsQuantity;
    @XmlAttribute(name = "learnedMeaningsQuantity")
    protected Byte learnedMeaningsQuantity;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setStatus(Byte value) {
        this.status = value;
    }

    /**
     * Gets the value of the shown property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getShown() {
        return shown;
    }

    /**
     * Sets the value of the shown property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setShown(Byte value) {
        this.shown = value;
    }

    /**
     * Gets the value of the answered property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getAnswered() {
        return answered;
    }

    /**
     * Sets the value of the answered property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setAnswered(Byte value) {
        this.answered = value;
    }

    /**
     * Gets the value of the defectiveMeaningsQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getDefectiveMeaningsQuantity() {
        return defectiveMeaningsQuantity;
    }

    /**
     * Sets the value of the defectiveMeaningsQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setDefectiveMeaningsQuantity(Byte value) {
        this.defectiveMeaningsQuantity = value;
    }

    /**
     * Gets the value of the suspendedMeaningsQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getSuspendedMeaningsQuantity() {
        return suspendedMeaningsQuantity;
    }

    /**
     * Sets the value of the suspendedMeaningsQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setSuspendedMeaningsQuantity(Byte value) {
        this.suspendedMeaningsQuantity = value;
    }

    /**
     * Gets the value of the readyMeaningsQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getReadyMeaningsQuantity() {
        return readyMeaningsQuantity;
    }

    /**
     * Sets the value of the readyMeaningsQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setReadyMeaningsQuantity(Byte value) {
        this.readyMeaningsQuantity = value;
    }

    /**
     * Gets the value of the activeMeaningsQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getActiveMeaningsQuantity() {
        return activeMeaningsQuantity;
    }

    /**
     * Sets the value of the activeMeaningsQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setActiveMeaningsQuantity(Byte value) {
        this.activeMeaningsQuantity = value;
    }

    /**
     * Gets the value of the learnedMeaningsQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getLearnedMeaningsQuantity() {
        return learnedMeaningsQuantity;
    }

    /**
     * Sets the value of the learnedMeaningsQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setLearnedMeaningsQuantity(Byte value) {
        this.learnedMeaningsQuantity = value;
    }

}
