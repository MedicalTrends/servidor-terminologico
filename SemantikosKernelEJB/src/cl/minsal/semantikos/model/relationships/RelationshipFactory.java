package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.HelperTableManager;
import cl.minsal.semantikos.kernel.daos.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías
 */
@Singleton
public class RelationshipFactory {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipFactory.class);

    @EJB
    private ConceptManager conceptDAO;

    @EJB
    private ConceptSCTDAO conceptSCTDAO;

    @EJB
    private BasicTypeDAO basicTypeDAO;

    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDefinitionDAO;

    @EJB
    private RelationshipDefinitionDAO relDefDAO;

    @EJB
    private TargetDAO targetDAO;

    @EJB
    private HelperTableDAO helperTableDAO;

    @EJB
    private CrossmapsDAO crossmapDAO;

    @EJB
    private HelperTableManager helperTableManager;

    @EJB
    private RelationshipAttributeDAO relationshipAttributeDAO;


    public Relationship createFromJSON(String jsonExpression) throws EJBException {

        /* Transformar el JSON a DTO primero */
        RelationshipDTO relationshipDTO = parseRelationshipFromJSON(jsonExpression);

        long id = relationshipDTO.id;
        ConceptSMTK sourceConcept = conceptDAO.getConceptByID(relationshipDTO.getIdSourceConcept());
        Target target = targetDAO.getTargetByID(relationshipDTO.idTarget);
        RelationshipDefinition relationshipDefinition = relDefDAO.getRelationshipDefinitionByID(relationshipDTO.idRelationshipDefinition);

        return new Relationship(id, sourceConcept, target, relationshipDefinition, relationshipDTO.validityUntil,new ArrayList<RelationshipAttribute>());
    }

    /**
     * Este método es responsable de parsear UN RelationshipDTO desde una expresión JSON.
     *
     * @param jsonExpression La expresión que contiene un RelationshipDTO.
     *
     * @return Un objeto fresco DTO de Relationship.
     */
    private RelationshipDTO parseRelationshipFromJSON(String jsonExpression) {
        ObjectMapper mapper = new ObjectMapper();
        RelationshipDTO relationshipDTO;
        try {
            relationshipDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), RelationshipDTO.class);
        } catch (IOException e) {
            String errorMsg = "Error al transformar de JSON a RelationshipDTO.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
        return relationshipDTO;
    }

    /**
     * Este método es responsable de parsear una expresión JSON a una lista de Relaciones.
     *
     * @param jsonExpression La expresión JSON del tipo:
     *
     * @return Una lista de relaciones creadas a partir de la expresión.
     */
    public List<Relationship> createRelationshipsFromJSON(String jsonExpression) {

        /* Si JSON es nulo, se retorna una lista vacía */
        if (jsonExpression == null){
            return Collections.emptyList();
        }

        /* Se parsea la expresión JSON */
        ObjectMapper mapper = new ObjectMapper();
        RelationshipDTO[] relationshipDTOs;
        try {
            relationshipDTOs = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), RelationshipDTO[].class);
        } catch (IOException e) {
            String errorMsg = "Error when parsing a JSON a Relationships";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        /* Y se recuperan las entidades relacionadas */
        List<Relationship> relationships = new ArrayList<>();
        for (RelationshipDTO relationshipDTO : relationshipDTOs) {
            relationships.add(createRelationshipFromDTO(relationshipDTO));
        }

        /* Se retorna como una lista */
        return relationships;
    }

    /**
     * Este método es responsable de crear la entidad Relación completa a partir del DTO.
     * <p>Se crea la relación a partir de esos datos: concepto origen, definición de relación y target.</p>
     *
     * @param relationshipDTO El DTO a partir del cual se crea la relación.
     *
     * @return Una relación fresca creada a partir del DTO.
     */
    private Relationship createRelationshipFromDTO(RelationshipDTO relationshipDTO) {

        /* Concepto origen */
        ConceptSMTK sourceConceptSMTK = conceptDAO.getConceptByID(relationshipDTO.idSourceConcept);

        /* Definición de la relación */
        long idRelationshipDefinition = relationshipDTO.idRelationshipDefinition;
        RelationshipDefinition relationshipDefinition = relDefDAO.getRelationshipDefinitionByID(idRelationshipDefinition);

        /* El target que puede ser básico, smtk, tablas, crossmaps o snomed-ct */
        Target target;
        long idTarget = relationshipDTO.idTarget;

        /* El target puede ser Tipo Básico */
        if (relationshipDefinition.getTargetDefinition().isBasicType()) {
            target = basicTypeDAO.getBasicTypeValueByID(idTarget);
        }
        //TODO arreglar el obtener target

        /* El target puede ser a un registro de una tabla auxiliar */
        else if (relationshipDefinition.getTargetDefinition().isHelperTable()) {
            //target = helperTableManager.getRecord(idTarget);
            target = targetDAO.getTargetByID(idTarget);
            /**
             * Se setea el id desde el fields para ser utilizado por el custom converter
             */
            if ( target != null ) {
                HelperTableRecord helperTableRecord = (HelperTableRecord) target;
                Map<String, String> fields = helperTableRecord.getFields();
                if ( fields != null && fields.get("id") != null ) {
                    helperTableRecord.setId(new Long(fields.get("id")));
                }
                target = helperTableRecord;
            }
        }

        /* El target puede ser un concepto SMTK */
        else if (relationshipDefinition.getTargetDefinition().isSMTKType()) {
            //target = conceptDAO.getConceptByID(idTarget);
            target = targetDAO.getTargetByID(idTarget);
        }

        /* El target puede ser un concepto Snomed CT */
        else if (relationshipDefinition.getTargetDefinition().isSnomedCTType()) {
            //target = conceptSCTDAO.getConceptCSTByID(idTarget);
            target = targetDAO.getTargetByID(idTarget);
        }

        /* Y sino, puede ser crossmap */
        else if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
            target = crossmapDAO.getDirectCrossmapById(idTarget);
        }

        /* Sino, hay un nuevo tipo de target que no está siendo gestionado */
        else {
            String msg = "Un tipo no manejado de Target se ha recibido.";
            logger.error(msg);
            throw new EJBException(msg);
        }

        List<RelationshipAttribute> relationshipAttributes= relationshipAttributeDAO.getRelationshipAttribute(relationshipDTO.getId());

        // Se agregan las definiciones de atributo
        relationshipDefinition.setRelationshipAttributeDefinitions(relationshipDefinitionDAO.getRelationshipAttributeDefinitionsByRelationshipDefinition(relationshipDefinition));

        Relationship relationship= new Relationship(relationshipDTO.getId(), sourceConceptSMTK, target, relationshipDefinition, relationshipDTO.validityUntil,new ArrayList<RelationshipAttribute>());
        relationship.setRelationshipAttributes(relationshipAttributes);
        return relationship;
    }

}

class RelationshipDTO {

    protected long id;
    protected long idSourceConcept;
    protected long idTarget;
    protected long idRelationshipDefinition;
    protected Timestamp validityUntil;

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

    public long getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(long idTarget) {
        this.idTarget = idTarget;
    }

    public long getIdRelationshipDefinition() {
        return idRelationshipDefinition;
    }

    public void setIdRelationshipDefinition(long idRelationshipDefinition) {
        this.idRelationshipDefinition = idRelationshipDefinition;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }


}
