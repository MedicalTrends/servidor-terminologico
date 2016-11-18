package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "objetivo")
public class TargetResponse implements Serializable {

    @XmlElement(name="tipoObjetivo")
    private TargetTypeResponse targetTypeResponse;
    @XmlElement(name="activo")
    private Boolean isActive;
    @XmlElement(name="idModulo")
    private Long moduleId;
    @XmlElement(name="estadoDefinicionId")
    private Long definitionStatusId;
    @XmlElement(name="valido")
    private Boolean isValid;
    @XmlElement(name="tiempoEfectivo")
    private Date effectiveTime;
    @XmlElement(name="valor")
    private String value;
    @XmlElement(name="conceptoSMTK")
    private ConceptResponse concept;
    @XmlElement(name="relacion")
    private RelationshipResponse relationship;
    @XmlElement(name="tablaAuxiliar")
    private HelperTableResponse helperTableResponse;
    @XmlElement(name="campos")
    private Map<String,String> fields;

    public TargetTypeResponse getTargetTypeResponse() {
        return targetTypeResponse;
    }

    public void setTargetTypeResponse(TargetTypeResponse targetTypeResponse) {
        this.targetTypeResponse = targetTypeResponse;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getDefinitionStatusId() {
        return definitionStatusId;
    }

    public void setDefinitionStatusId(Long definitionStatusId) {
        this.definitionStatusId = definitionStatusId;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ConceptResponse getConcept() {
        return concept;
    }

    public void setConcept(ConceptResponse concept) {
        this.concept = concept;
    }

    public RelationshipResponse getRelationship() {
        return relationship;
    }

    public void setRelationship(RelationshipResponse relationship) {
        this.relationship = relationship;
    }

    public HelperTableResponse getHelperTableResponse() {
        return helperTableResponse;
    }

    public void setHelperTableResponse(HelperTableResponse helperTableResponse) {
        this.helperTableResponse = helperTableResponse;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
