package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaObtenerCrossmapsDirectos", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaObtenerCrossmapsDirectos", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMembersResponse extends Response implements Serializable{

    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionIDPreferida")
    private String descriptionId;
    @XmlElement(name="descriptionPreferida")
    private String description;
    @XmlElement(name="nombreCategoria")
    private String category;

    @XmlElement(name="nombreCortoCrossmapSet")
    private String abbreviatedName;

    @XmlElement(name="nombreCrossmapSet")
    private String name;

    @XmlElement(name="version")
    private Integer version;

    /** La lista de crossmaps indirectos (response) */
    @XmlElementWrapper(name = "crossmapSetMembers")
    @XmlElement(name = "crossmapSetMember")
    private List<CrossmapSetMemberResponse> crossmapSetMemberResponses;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public CrossmapSetMembersResponse() {
        this.crossmapSetMemberResponses = new ArrayList<>();
    }

    /**
     * Este constructor es responsable de poblar la lista de crossmapsSetMembers "response" a partir del  objeto de
     * negocio.
     *
     * @param crossmapSetMembers La lista de crossmapSetMembers de negocio.
     */
    public CrossmapSetMembersResponse(List<CrossmapSetMember> crossmapSetMembers) {
        this();

        if (crossmapSetMembers == null || crossmapSetMembers.isEmpty()){
            return;
        }

        for (CrossmapSetMember crossmapSetMember : crossmapSetMembers) {
            this.crossmapSetMemberResponses.add(new CrossmapSetMemberResponse(crossmapSetMember));
        }

        this.quantity = crossmapSetMembers.size();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<CrossmapSetMemberResponse> getCrossmapSetMemberResponses() {
        return crossmapSetMemberResponses;
    }

    public void setCrossmapSetMemberResponses(List<CrossmapSetMemberResponse> crossmapSetMemberResponses) {
        this.crossmapSetMemberResponses = crossmapSetMemberResponses;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
