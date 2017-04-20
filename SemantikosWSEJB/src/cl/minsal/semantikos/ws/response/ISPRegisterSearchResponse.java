package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ISPRegisterSearchResponse implements Serializable {

    @XmlElement(name="idConcepto")
    private String conceptId;
    @XmlElement(name="idDescripcionPreferida")
    private String descriptionId;
    @XmlElement(name="descripcionPreferida")
    private String description;
    @XmlElement(name="nombreCategoria")
    private String category;

    @XmlElement(name="ispRegisters")
    private List<ISPRegisterResponse> ispRegistersResponse = new ArrayList<>();

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ISPRegisterResponse> getIspRegistersResponse() {
        return ispRegistersResponse;
    }

    public void setIspRegistersResponse(List<ISPRegisterResponse> ispRegistersResponse) {
        this.ispRegistersResponse = ispRegistersResponse;
    }
}
