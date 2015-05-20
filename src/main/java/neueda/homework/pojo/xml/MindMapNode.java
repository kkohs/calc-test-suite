package neueda.homework.pojo.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Mind map value node.
 *
 * @author Kristaps Kohs
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MindMapNode {
    @XmlAttribute(name = "TEXT")
    private String text;
    @XmlElement(name = "node")
    private List<MindMapNode> nodes = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MindMapNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<MindMapNode> nodes) {
        this.nodes = nodes;
    }
}
