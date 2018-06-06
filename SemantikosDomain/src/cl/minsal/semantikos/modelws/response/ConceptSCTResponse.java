package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alfonso Cornejo on 2016-10-11.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conceptoSCT", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "ConceptoSCT", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptSCTResponse implements Serializable {

    @XmlElement(name = "conceptID")
    private long conceptId;

    @XmlElementWrapper(name = "descripciones")
    @XmlElement(name = "descripcion")
    private List<DescriptionSCTResponse> descriptions;

    @XmlElementWrapper(name = "relaciones")
    @XmlElement(name = "relacion")
    private List<RelationshipSCTResponse> relationships;

    @XmlElementWrapper(name = "mapeos")
    @XmlElement(name = "mapeo")
    private List<IndirectCrossMapResponse> indirectCrossMaps;


    public ConceptSCTResponse() {
        this.relationships = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.indirectCrossMaps = new ArrayList<>();
    }

    public ConceptSCTResponse(ConceptSCT conceptSCT, List<IndirectCrossmap> indirectCrossmaps) {
        this();

        this.conceptId = conceptSCT.getId();

        /* Se cargan las otras propiedades del concepto */
        loadDescriptions(conceptSCT);

        loadRelationships(conceptSCT);

        loadIndirectCrossmaps(indirectCrossmaps);

    }

    /**
     * Este método es responsable de cargar las descripciones de un concepto (<code>toConceptResponse</code>) a otro
     * (<code>sourceConcept</code>).
     *
     * @param sourceConcept El concepto desde el cual se cargan las descripciones
     */
    private void loadDescriptions(@NotNull ConceptSCT sourceConcept) {
        for (DescriptionSCT description : sourceConcept.getDescriptions()) {
            this.descriptions.add(new DescriptionSCTResponse(description));
        }
    }

    /**
     * Este método es responsable de caregar en este concepto los atributos de un concepto fuente.
     *
     * @param sourceConcept El concepto desde el cual se cargan los atrubos.
     */
    private void loadRelationships(@NotNull ConceptSCT sourceConcept) {
        for (RelationshipSCT relationship : sourceConcept.getRelationships()) {
            this.relationships.add(new RelationshipSCTResponse(relationship));
        }
    }

    /**
     * Este método es responsable de caregar en este concepto los atributos de un concepto fuente.
     *
     * @param indirectCrossmaps El concepto desde el cual se cargan los atrubos.
     */
    private void loadIndirectCrossmaps(@NotNull List<IndirectCrossmap> indirectCrossmaps) {
        for (IndirectCrossmap indirectCrossmap : indirectCrossmaps) {
            this.indirectCrossMaps.add(new IndirectCrossMapResponse(indirectCrossmap));
        }
    }

    public long getConceptId() {
        return conceptId;
    }

    public void setConceptId(long conceptId) {
        this.conceptId = conceptId;
    }

    public void setDescriptions(List<DescriptionSCTResponse> descriptions) {
        this.descriptions = descriptions;
    }

    public List<RelationshipSCTResponse> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipSCTResponse> relationships) {
        this.relationships = relationships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptSCTResponse)) return false;

        ConceptSCTResponse that = (ConceptSCTResponse) o;

        return getConceptId() == that.getConceptId();
    }

    @Override
    public int hashCode() {
        int result = (int) (getConceptId() ^ (getConceptId() >>> 32));
        return result;
    }

}
