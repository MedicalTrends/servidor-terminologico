package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "definicionRelacionAtributo")
public class RelationshipAttributeDefinitionResponse implements Serializable {

    @XmlElement(name="name")
    private String name;
    @XmlElement(name="multiplicidad")
    private MultiplicityResponse multiplicity;
    @XmlElement(name="definicionObjetivo")
    private TargetDefinitionResponse targetDefinition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultiplicityResponse getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(MultiplicityResponse multiplicity) {
        this.multiplicity = multiplicity;
    }

    public TargetDefinitionResponse getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinitionResponse targetDefinition) {
        this.targetDefinition = targetDefinition;
    }
}
