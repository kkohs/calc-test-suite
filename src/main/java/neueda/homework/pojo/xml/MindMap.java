package neueda.homework.pojo.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Kristaps Kohs
 */
@XmlRootElement(name = "map")
@XmlAccessorType(XmlAccessType.FIELD)
public class MindMap {

    @XmlElement(name = "node")
    private List<MindMapNode> nodes;


    public List<MindMapNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<MindMapNode> nodes) {
        this.nodes = nodes;
    }
}
