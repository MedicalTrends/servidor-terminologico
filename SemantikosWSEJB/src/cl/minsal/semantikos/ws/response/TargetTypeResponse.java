package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tipoObjetivo")
@XmlType(name = "TipoObjetivo")
public class TargetTypeResponse implements Serializable {

    @XmlElement(name="tipo")
    private String type;
    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="descripcion")
    private String description;
    @XmlElementWrapper(name = "definicionesObjetivo")
    @XmlElement(name="definicionObjetivo")
    private List<TargetDefinitionResponse> targetDefinitions;
    @XmlElementWrapper(name = "definicionesTipoBasico")
    @XmlElement(name="definicionTipoBasico")
    private List<BasicTypeDefinitionResponse> basicTypeDefinitions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TargetDefinitionResponse> getTargetDefinitions() {
        return targetDefinitions;
    }

    public void setTargetDefinitions(List<TargetDefinitionResponse> targetDefinitions) {
        this.targetDefinitions = targetDefinitions;
    }

    public List<BasicTypeDefinitionResponse> getBasicTypeDefinitions() {
        return basicTypeDefinitions;
    }

    public void setBasicTypeDefinitions(List<BasicTypeDefinitionResponse> basicTypeDefinitions) {
        this.basicTypeDefinitions = basicTypeDefinitions;
    }
}
