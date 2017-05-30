package cl.minsal.semantikos.ws.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarBioequivalente", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarBioequivalente", namespace = "http://service.ws.semantikos.minsal.cl/")
public class BioequivalentSearchResponse implements Serializable {

    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionIDPreferida")
    private String descriptionId;
    @XmlElement(name="descriptionPreferida")
    private String description;
    @XmlElement(name="nombreCategoria")
    private String category;

    @XmlElement(name="bioequivalentes")
    private List<BioequivalentResponse> bioequivalentsResponse = new ArrayList<>();

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

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

    public List<BioequivalentResponse> getBioequivalentsResponse() {
        return bioequivalentsResponse;
    }

    public void setBioequivalentsResponse(List<BioequivalentResponse> bioequivalentsResponse) {
        this.bioequivalentsResponse = bioequivalentsResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
