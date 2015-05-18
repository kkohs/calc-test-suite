package neueda.homework.pojo.xml;

import javax.xml.bind.Element;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kristaps Kohs
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class UnspecifiedData {
    @XmlAnyElement
    private List<Element>  unspecifiedElements = new ArrayList<>();

    @XmlAnyAttribute
    private Map<String,QName> unpecifiedProperties = new HashMap<>();


    public List<Element> getUnspecifiedElements() {
        return unspecifiedElements;
    }

    public void setUnspecifiedElements(List<Element> unspecifiedElements) {
        this.unspecifiedElements = unspecifiedElements;
    }

    public Map<String, QName> getUnpecifiedProperties() {
        return unpecifiedProperties;
    }

    public void setUnpecifiedProperties(Map<String, QName> unpecifiedProperties) {
        this.unpecifiedProperties = unpecifiedProperties;
    }
}
