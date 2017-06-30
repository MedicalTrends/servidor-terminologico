package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.daos.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private HelperTablesManager helperTableManager;

    @EJB
    private RelationshipAttributeDAO relationshipAttributeDAO;

    public Relationship createFromJSON(String jsonExpression) throws EJBException {

        /* Transformar el JSON a DTO primero */
        RelationshipDTO relationshipDTO = parseRelationshipFromJSON(jsonExpression);

        long id = relationshipDTO.id;
        ConceptSMTK sourceConcept = conceptDAO.getConceptByID(relationshipDTO.getIdSourceConcept());
        Target target = targetDAO.getTargetByID(relationshipDTO.idTarget);
        RelationshipDefinition relationshipDefinition = relDefDAO.getRelationshipDefinitionByID(relationshipDTO.idRelationshipDefinition);

        return new Relationship(id, sourceConcept, target, relationshipDefinition, relationshipDTO.validityUntil, new ArrayList<RelationshipAttribute>());
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
        if (jsonExpression == null) {
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

        /* Definición de la relación y sus atributos */
        long idRelationshipDefinition = relationshipDTO.idRelationshipDefinition;
        RelationshipDefinition relationshipDefinition = relDefDAO.getRelationshipDefinitionByID(idRelationshipDefinition);
        //List<RelationshipAttribute> relationshipAttributes = relationshipAttributeDAO.getRelationshipAttribute(relationshipDTO.getId());
        relationshipDefinition.setRelationshipAttributeDefinitions(relationshipDefinitionDAO.getRelationshipAttributeDefinitionsByRelationshipDefinition(relationshipDefinition));

        /* El target que puede ser básico, smtk, tablas, crossmaps o snomed-ct */
        Relationship relationship = createRelationshipByTargetType(relationshipDTO, sourceConceptSMTK, relationshipDefinition);
        //relationship.setRelationshipAttributes(relationshipAttributes);
        relationship.setValidityUntil(relationshipDTO.getValidityUntil());
        relationship.setCreationDate(relationshipDTO.getCreationDate());
        return relationship;
    }

    /**
     * Este método es reponsable de crear una instancia del tipo correcto de relación en función del target de un tipo
     * en particular.
     *
     * @param relationshipDTO        El DTO del relationship.
     * @param sourceConceptSMTK      El concepto origen de la relación.
     * @param relationshipDefinition La relación que lo define.
     *
     * @return Una relación del tipo correcta que define el Target.
     */
    private Relationship createRelationshipByTargetType(RelationshipDTO relationshipDTO, ConceptSMTK sourceConceptSMTK, RelationshipDefinition relationshipDefinition) {
        Target target;
        long idTarget = relationshipDTO.getIdTarget();

        /* El target puede ser Tipo Básico */
        if (relationshipDefinition.getTargetDefinition().isBasicType()) {
            BasicTypeValue basicTypeValueByID = basicTypeDAO.getBasicTypeValueByID(idTarget);
            return new Relationship(relationshipDTO.getId(), sourceConceptSMTK, basicTypeValueByID, relationshipDefinition, relationshipDTO.validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser a un registro de una tabla auxiliar */
        if (relationshipDefinition.getTargetDefinition().isHelperTable()) {
            //target = helperTableManager.getRecord(idTarget);
            target = targetDAO.getTargetByID(idTarget);
            /**
             * Se setea el id desde el fields para ser utilizado por el custom converter
             */
            HelperTableRow helperTableRow = (HelperTableRow) target;

            return new Relationship(relationshipDTO.getId(), sourceConceptSMTK, helperTableRow, relationshipDefinition, relationshipDTO.validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser un concepto SMTK */
        if (relationshipDefinition.getTargetDefinition().isSMTKType()) {

            ConceptSMTK conceptByID = (ConceptSMTK) targetDAO.getTargetByID(idTarget);
            return new Relationship(relationshipDTO.getId(), sourceConceptSMTK, conceptByID, relationshipDefinition, relationshipDTO.validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser un concepto Snomed CT */
        if (relationshipDefinition.getTargetDefinition().isSnomedCTType()) {
            ConceptSCT conceptCSTByID = (ConceptSCT) targetDAO.getTargetByID(idTarget);
            return new SnomedCTRelationship(relationshipDTO.getId(), sourceConceptSMTK, conceptCSTByID, relationshipDefinition, new ArrayList<RelationshipAttribute>(), relationshipDTO.validityUntil);
        }

        /* Y sino, puede ser crossmap */
        if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
            target = targetDAO.getTargetByID(idTarget);
            //CrossmapSetMember crossmapSetMemberById = crossmapDAO.getCrossmapSetMemberById(idTarget);
            return new DirectCrossmap(relationshipDTO.getId(), sourceConceptSMTK, (CrossmapSetMember)target, relationshipDefinition, relationshipDTO.validityUntil);
        }

        /* Sino, hay un nuevo tipo de target que no está siendo gestionado */
        String msg = "Un tipo no manejado de Target se ha recibido.";
        logger.error(msg);
        throw new EJBException(msg);
    }
}

class RelationshipDTO {

    protected long id;
    protected String idRelationship;
    protected long idSourceConcept;
    protected long idTarget;
    protected long idRelationshipDefinition;
    protected Timestamp validityUntil;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="America/Buenos_Aires")
    protected Timestamp creationDate;

    public RelationshipDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdRelationship() {
        return idRelationship;
    }

    public void setIdRelationship(String idRelationship) {
        this.idRelationship = idRelationship;
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
