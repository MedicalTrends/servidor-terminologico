package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.kernel.daos.AuthDAO;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.AuditActionType;
import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.users.User;
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
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías on 8/24/16.
 */
@Singleton
public class ConceptAuditActionFactory {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptAuditActionFactory.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private AuthDAO userDAO;

    @EJB
    private AuditableEntityFactory auditableEntityFactory;

    public ConceptAuditActionFactory() {
    }

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param jsonExpression La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<ConceptAuditAction> createAuditActionsFromJSON(String jsonExpression) {
        ObjectMapper mapper = new ObjectMapper();
        ConceptAuditActionDTO[] auditActionDTOs;
        try {
            auditActionDTOs = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), ConceptAuditActionDTO[].class);
        } catch (IOException e) {
            String errorMsg = "Error al parsear un JSON";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        List<ConceptAuditAction> auditActions = new ArrayList<>();
        for (ConceptAuditActionDTO auditActionDTO : auditActionDTOs) {

            ConceptSMTK concept = conceptDAO.getConceptByID(auditActionDTO.getIdConcept());
            AuditActionType auditActionType = AuditActionType.valueOf(auditActionDTO.getIdActionType());
            User user = userDAO.getUserById((int) auditActionDTO.getIdUser());
            AuditableEntityType auditableEntityType = AuditableEntityType.valueOf(auditActionDTO.getIdAuditEntityType());
            AuditableEntity auditableEntityByID = auditableEntityFactory.findAuditableEntityByID(auditActionDTO.getIdAuditableEntity(), auditableEntityType);


            ConceptAuditAction conceptAuditAction = new ConceptAuditAction(concept, auditActionType, auditActionDTO.getDate(), user, auditableEntityByID);
            auditActions.add(conceptAuditAction);
        }
        return auditActions;
    }
}

class ConceptAuditActionDTO {

    /** El concepto en el que se realizó la acción */
    private long idConcept;

    /** The kind of change happened */
    private long idActionType;

    /** La fecha en que tomo lugar la acción auditable */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="America/Buenos_Aires")
    private Timestamp date;

    /** El usuario que realizó la acción */
    private long idUser;

    /**
     * El ID de la entidad que fue el sujeto mismo de la acción: concepto, relación (atributo o SCT), descripción o
     * categoría
     */
    private long idAuditableEntity;

    /** El ID de la entidad auditada. */
    private long idAuditEntityType;

    public ConceptAuditActionDTO() {
    }

    public long getIdConcept() {
        return idConcept;
    }

    public void setIdConcept(long idConcept) {
        this.idConcept = idConcept;
    }

    public long getIdActionType() {
        return idActionType;
    }

    public void setIdActionType(long idActionType) {
        this.idActionType = idActionType;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdAuditableEntity() {
        return idAuditableEntity;
    }

    public void setIdAuditableEntity(long idAuditableEntity) {
        this.idAuditableEntity = idAuditableEntity;
    }

    public long getIdAuditEntityType() {
        return idAuditEntityType;
    }

    public void setIdAuditEntityType(long idAuditEntityType) {
        this.idAuditEntityType = idAuditEntityType;
    }
}

enum AuditableEntityType {

    CONCEPT(1, "Concepto"),
    RELATIONSHIP(2, "Relacione"),
    DESCRIPTION(3, "Descripción"),
    CATEGORY(4, "Categoría"),
    REFSET(5,"Refset");

    /** El identificador único del tipo de Entidad */
    private long id;

    /* El nombre de la entidad */
    private String entityName;

    AuditableEntityType(long id, String entityName) {
        this.id = id;
        this.entityName = entityName;
    }

    public static AuditableEntityType valueOf(long idAuditEntityType) {
        for (AuditableEntityType auditableEntityType : values()) {
            if (auditableEntityType.id == idAuditEntityType) {
                return auditableEntityType;
            }
        }

        throw new EJBException("No existe un tipo de entidad con ID = " + idAuditEntityType);
    }

    public long getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }
}