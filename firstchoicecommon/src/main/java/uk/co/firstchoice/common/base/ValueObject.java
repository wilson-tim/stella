package uk.co.firstchoice.common.base;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

import uk.co.firstchoice.common.base.util.Describable;
import uk.co.firstchoice.common.base.xml.util.DescribableAsXML;

/**
 * Generic functionality for objects used to transfer data between objects. All
 * VOs or value objects extending this class will have pre-built implementations
 * of equals(), and hashcode(). In addition, these extended classes will have
 * meaningful implementations of Describable and DescribableAsXML.
 * 
 * <p>
 * This class is named after the "Data Transfer Object" pattern. Some references
 * use the term "Value Object" instead. They mean the same thing.
 * 
 * <p>
 * To keep VOs usable as distributed objects, it's recommended that they don't
 * contain fields which aren't Serializable.
 * 
 * <p>
 * The implementations of equals() and hashcode() make extensions of this object
 * automatically usable in HashMaps and Hashtables.
 * 
 * <p>
 * An example extension follows:
 * 
 * <blockquote>
 * 
 * <pre>
 * public class CustomerVO extends ValueObject {
 * 
 *     public CustomerVO() {
 *     }
 * 
 *     public String getCustomerId() {
 *         return _customerId;
 *     }
 * 
 *     public void setCustomerId(String id) {
 *         if (id == null)
 *             throw new IllegalArgumentException(&quot;Null customer Id not allowed.&quot;);
 *         if (id.equals(&quot;&quot;))
 *             throw new IllegalArgumentException(&quot;Blank customer Id not allowed.&quot;);
 *         _customerId = id;
 *     }
 * 
 *     //  All other accessors and mutators omitted.
 * 
 *     private String _customerId = null;
 * 
 *     private String _firstName = null;
 * 
 *     private String _lastName = null;
 * 
 *     private String _address = null;
 * 
 *     private String _city = null;
 * 
 *     private String _state = null;
 * 
 *     private String _zipCode = null;
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 *  
 */
public abstract class ValueObject implements Comparable, Serializable,
        Describable, DescribableAsXML {

    private String serialVersionUID = null;

    /**
     * @return Returns the serialVersionUID.
     */
    public String getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param serialVersionUID
     *            The serialVersionUID to set.
     * @return Returns the serialVersionUID.
     */
    public void setSerialVersionUID(final String serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    protected ValueObject() {
        this.init();
    }

    protected void init() {
        _classField = this.getClass().getDeclaredFields();
        for (int i = 0; _classField != null && i < _classField.length; i++) {
            _classField[i].setAccessible(true);
        }
        _startBufferSize = (_classField.length + 1) * 128;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        this.init();
    }

    public int hashCode() {
        return this.getConcantonatedObjectValue().hashCode();
    }

    public boolean equals(Object obj) {
        boolean answer = false;

        if (this == obj)
            answer = true;
        if (obj != null
                && obj.getClass().getName().equals(this.getClass().getName())) {
            ValueObject dto = (ValueObject) obj;
            if (this.equalsVO(dto, false) == null)
                answer = true;
        }
        return answer;
    }

    /**
     * Provides a textual reason that two ValueObjects are not equal.
     * 
     * @param obj
     * @return text
     */
    public String getReasonForNotEquals(Object obj) {
        String answer = null;

        if (this == obj)
            answer = null;
        if (obj != null
                && obj.getClass().getName().equals(this.getClass().getName())) {
            ValueObject dto = (ValueObject) obj;
            answer = this.equalsVO(dto, true);
        } else
            answer = "The objects have different class definitions.";

        return answer;
    }

    public String toString() {
        return this.describe();
    }

    public String describe() {
        StringBuffer buffer = new StringBuffer(_startBufferSize);
        Object tempObj = null;

        buffer.append(this.getClass().getName());
        buffer.append("==>");

        if (_classField != null) {
            for (int i = 0; i < _classField.length; i++) {
                buffer.append("; ");
                buffer.append(_classField[i].getName());
                buffer.append("=");
                try {
                    tempObj = _classField[i].get(this);
                } catch (IllegalAccessException a) {
                    buffer.append("not accessable");
                }

                if (tempObj == null)
                    buffer.append("null");
                else {
                    if (tempObj.getClass().isArray())
                        buffer.append(ValueObject
                                .getArrayDescription((Object[]) tempObj));
                    else
                        buffer.append(tempObj);
                }
            }
        }

        buffer.append("<==");

        return buffer.toString();
    }

    public String encodeAsXML() {
        return this.describeAsXMLDocument();
    }

    public String describeAsXMLDocument() {
        StringBuffer buffer = new StringBuffer(_startBufferSize * 2);

        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE ");
        buffer.append(this.getXmlNameForClass());
        buffer.append(">");

        buffer.append(this.describeAsXMLElement());

        return buffer.toString();
    }

    public String describeAsXMLElement() {
        return this.describeAsXMLElement(null);
    }

    public String describeAsXMLElement(String fieldName) {
        StringBuffer buffer = new StringBuffer(_startBufferSize * 2);
        Object tempObj = null;
        Object[] tempArray = null;
        ArrayList segmentList = new ArrayList();

        buffer.append("<Object type=\"");
        buffer.append(this.getClass().getName());
        buffer.append("\" ");
        if (fieldName != null && !fieldName.equals("")) {
            buffer.append(" name=\"");
            buffer.append(fieldName);
            buffer.append("\"");
        }
        buffer.append(">");

        if (_classField != null) {
            for (int i = 0; i < _classField.length; i++) {
                try {
                    tempObj = _classField[i].get(this);
                    if (tempObj != null && tempObj.getClass().isArray()) {
                        tempArray = (Object[]) tempObj;
                        segmentList.add(ValueObject.getArrayAsXMLSegment(
                                tempArray, _classField[i], ""));
                    } else {
                        buffer.append("<Field name=\"");
                        buffer.append(_classField[i].getName());
                        buffer.append("\" type=\"");
                        buffer.append(_classField[i].getType().getName());
                        buffer.append("\"");
                        if (tempObj != null) {
                            if (tempObj instanceof DescribableAsXML) {
                                DescribableAsXML foo = (DescribableAsXML) tempObj;
                                segmentList.add(foo
                                        .describeAsXMLElement(_classField[i]
                                                .getName()));
                            } else {
                                buffer.append(" value=\"");
                                buffer.append(tempObj);
                                buffer.append("\"");
                            }
                        } else
                            buffer.append("null");
                        buffer.append("/>");
                    }
                } catch (IllegalAccessException ia) {
                    ia.printStackTrace();
                }
            }

            for (int i = 0; segmentList != null && i < segmentList.size(); i++) {
                buffer.append(segmentList.get(i));
            }
        }

        buffer.append("</Object>");

        return buffer.toString();
    }

    public int compareTo(Object obj) {
        int compareResult = 0;
        Object tempObj = null;
        Object tempObjCompareTarget = null;
        Comparable c1, c2;

        if (obj == null)
            throw new IllegalArgumentException(
                    "Comparisons to null objects not defined.");
        if (!obj.getClass().getName().equals(this.getClass().getName()))
            throw new IllegalArgumentException(
                    "Comparing different class types not allowed.");

        if (_classField != null) {
            for (int i = 0; compareResult == 0 && i < _classField.length; i++) {
                try {
                    tempObj = _classField[i].get(this);
                    tempObjCompareTarget = _classField[i].get(obj);

                    if (tempObj instanceof Comparable)
                        c1 = (Comparable) tempObj;
                    else
                        throw new IllegalArgumentException(
                                "Comparing classes with incomparable fields not allowed.  "
                                        + _classField[i].getType().getName()
                                        + " fields are not Comparable.");

                    if (tempObjCompareTarget instanceof Comparable)
                        c2 = (Comparable) tempObjCompareTarget;
                    else
                        throw new IllegalArgumentException(
                                "Comparing classes with incomparable fields not allowed."
                                        + _classField[i].getType().getName()
                                        + " fields are not Comparable.");

                    compareResult = c1.compareTo(c2);
                } catch (IllegalAccessException a) {
                    throw new IllegalArgumentException(
                            "Comparing classes with inaccessable fields not allowed.");
                }
            }
        }

        return compareResult;
    }

    private String getXmlNameForClass() {
        if (_xmlNameForClass == null)
            _xmlNameForClass = this.getClass().getName().replace('.', '-');
        return _xmlNameForClass;
    }

    private String getConcantonatedObjectValue() {
        StringBuffer buffer = new StringBuffer(_startBufferSize);
        Object tempObj = null;
        Object[] tempArray = null;

        buffer.append(this.getClass().getName());

        if (_classField != null) {
            for (int i = 0; i < _classField.length; i++) {
                try {
                    tempObj = _classField[i].get(this);
                } catch (IllegalAccessException a) {
                    tempObj = null;
                }

                if (tempObj == null)
                    buffer.append("null");
                else {
                    if (tempObj.getClass().isArray()) {
                        tempArray = (Object[]) tempObj;
                        ValueObject.getArrayBuffer(buffer, tempArray);
                    } else
                        buffer.append(tempObj);
                }
            }
        }

        return buffer.toString();
    }

    private String equalsVO(ValueObject obj, boolean trackReason) {
        boolean answer = true;
        StringBuffer unequalReason = new StringBuffer();
        Object tempThisObj = null;
        Object tempObj = null;

        if (this._classField == null && obj._classField == null) {
            answer = true;
        } else {
            for (int i = 0; i < _classField.length && answer; i++) {
                try {
                    tempThisObj = this._classField[i].get(this);
                } catch (IllegalAccessException a) {
                    tempThisObj = null;
                }
                try {
                    tempObj = obj._classField[i].get(obj);
                } catch (IllegalAccessException a) {
                    tempObj = null;
                }

                if (tempThisObj == null && tempObj != null) {
                    answer = false;
                    if (trackReason) {
                        unequalReason.append("Values for field ");
                        unequalReason.append(_classField[i].getName());
                        unequalReason.append(" are null vs '");
                        unequalReason.append(tempObj);
                        unequalReason.append("'");
                    }
                } else if (tempThisObj != null && tempObj == null) {
                    answer = false;
                    if (trackReason) {
                        unequalReason.append("Values for field ");
                        unequalReason.append(_classField[i].getName());
                        unequalReason.append(" are '");
                        unequalReason.append(tempThisObj);
                        unequalReason.append("' vs null");
                    }
                } else if (tempThisObj == null && tempObj == null) {
                    answer = true;
                } else if (tempThisObj.getClass().isArray()
                        && tempObj.getClass().isArray()) {
                    answer = this.equalsArray((Object[]) tempThisObj,
                            (Object[]) tempObj);
                    if (trackReason) {
                        unequalReason.append("Values for array ");
                        unequalReason.append(_classField[i].getName());
                        unequalReason.append(" are not equal");
                    }
                } else if (!tempThisObj.equals(tempObj)) {
                    answer = false;
                    if (trackReason) {
                        unequalReason.append("Values for field ");
                        unequalReason.append(_classField[i].getName());
                        unequalReason.append(" are '");
                        unequalReason.append(tempThisObj);
                        unequalReason.append("' vs '");
                        unequalReason.append(tempObj);
                        unequalReason.append("'");
                    }
                }
            }
        }

        if (answer)
            return null;
        return unequalReason.toString();

    }

    private boolean equalsArray(Object[] obj1, Object[] obj2) {
        boolean answer = true;

        if (obj1.length == obj2.length) {
            for (int i = 0; i < obj1.length && answer; i++) {
                if (obj1[i].getClass().isArray()
                        && obj2[i].getClass().isArray()) {
                    answer = this.equalsArray(obj1, obj2);
                } else if (!obj1[i].equals(obj2[i]))
                    answer = false;
            }
        } else
            answer = false;

        return answer;
    }

    private static void getArrayBuffer(StringBuffer appendBuffer,
            Object[] arrayToBuf) {
        for (int j = 0; j < arrayToBuf.length; j++) {
            if (arrayToBuf[j] == null)
                appendBuffer.append("null");
            else if (arrayToBuf[j].getClass().isArray()) {
                ValueObject.getArrayBuffer(appendBuffer,
                        (Object[]) arrayToBuf[j]);
            } else {
                appendBuffer.append(arrayToBuf[j]);
            }
        }
    }

    private static String getArrayDescription(Object[] arrayToBuf) {
        StringBuffer buffer = new StringBuffer(1024);
        for (int j = 0; j < arrayToBuf.length; j++) {
            if (buffer.length() > 0)
                buffer.append(",");
            if (arrayToBuf[j] == null)
                buffer.append("null");
            else if (arrayToBuf[j].getClass().isArray()) {
                buffer.append(ValueObject
                        .getArrayDescription((Object[]) arrayToBuf[j]));
            } else {
                buffer.append(arrayToBuf[j]);
            }
        }

        return buffer.toString();
    }

    private static String getArrayAsXMLSegment(Object[] tempArray, Field field,
            String nameSuffix) {
        StringBuffer buffer = new StringBuffer(1024);
        ArrayList segmentList = new ArrayList();

        buffer.append("<Field name=\"");
        buffer.append(field.getName());
        if (nameSuffix != null)
            buffer.append(nameSuffix);
        buffer.append("\" type=\"");
        buffer.append(field.getType().getName());
        buffer.append("\" array=\"Y\" >");

        for (int j = 0; j < tempArray.length; j++) {
            if (tempArray[j] != null && tempArray[j].getClass().isArray()) {
                StringBuffer suffix = new StringBuffer();
                suffix.append(nameSuffix);
                suffix.append("[");
                suffix.append(j);
                suffix.append("]");
                buffer.append(ValueObject.getArrayAsXMLSegment(
                        (Object[]) tempArray[j], field, suffix.toString()));
            } else if (tempArray[j] != null
                    && tempArray[j] instanceof ValueObject) {
                ValueObject dto = (ValueObject) tempArray[j];
                segmentList.add(dto.describeAsXMLElement());
            } else {
                buffer.append("<Field name=\"");
                buffer.append(field.getName());
                buffer.append("[");
                buffer.append(j);
                buffer.append("]\" value=\"");

                if (tempArray[j] == null)
                    buffer.append("null");
                else
                    buffer.append(tempArray[j]);
                buffer.append("\" />");
            }
        }

        for (int i = 0; i < segmentList.size(); i++) {
            buffer.append(segmentList.get(i));
        }
        buffer.append("</Field>");

        return buffer.toString();
    }

    private transient Field[] _classField = null;

    private String _xmlNameForClass = null;

    private int _startBufferSize = 1024;

}