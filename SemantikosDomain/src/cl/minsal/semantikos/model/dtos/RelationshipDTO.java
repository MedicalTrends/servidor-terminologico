package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Una Relación es una asociación con significado entre 2 cosas.</p>
 * <p>
 * En el Modelo General de Terminologías los Conceptos pueden estar relacionados entre sí o con otras entidades,
 * entonces, una Relación es una asociación entre un Concepto Origen y un Objeto Destino.</p>
 * <p>Cada Relación agrega información sobre el Concepto Origen.</p>
 * Hay 2 Tipos de Relaciones:
 * •	Definitorias
 * •	De Atributos
 *
 * @author Andrés Farías
 */
public class RelationshipDTO implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RelationshipDTO.class);

    private long id;

    /** ID de negocio (Solo utilizado en relaciones SCT) */
    private String idRelationship;

    private long idSourceConcept;

    /** La definición de esta relación */
    private long idRelationshipDefinition;

    /** El elemento destino de esta relación */
    private TargetDTO targetDTO;

    /** La relación es Vigente (valida) hasta la fecha... */
    private Timestamp validityUntil;

    /** Fecha en que fue creada la relación */
    private Timestamp creationDate;

    private List<RelationshipAttributeDTO> relationshipAttributeDTOs = new ArrayList<>();

    public RelationshipDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdSourceConcept() {
        return idSourceConcept;
    }

    public void setIdSourceConcept(long idSourceConcept) {
        this.idSourceConcept = idSourceConcept;
    }

    public long getIdRelationshipDefinition() {
        return idRelationshipDefinition;
    }

    public void setIdRelationshipDefinition(long idRelationshipDefinition) {
        this.idRelationshipDefinition = idRelationshipDefinition;
    }

    public TargetDTO getTargetDTO() {
        return targetDTO;
    }

    public void setTargetDTO(TargetDTO targetDTO) {
        this.targetDTO = targetDTO;
    }

    public String getIdRelationship() {
        return idRelationship;
    }

    public void setIdRelationship(String idRelationship) {
        this.idRelationship = idRelationship;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public List<RelationshipAttributeDTO> getRelationshipAttributeDTOs() {
        return relationshipAttributeDTOs;
    }

    public void setRelationshipAttributeDTOs(List<RelationshipAttributeDTO> relationshipAttributeDTOs) {
        if(relationshipAttributeDTOs != null) {
            this.relationshipAttributeDTOs = relationshipAttributeDTOs;
        }
    }
}
