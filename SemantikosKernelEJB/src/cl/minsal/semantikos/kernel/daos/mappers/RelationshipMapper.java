package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class RelationshipMapper {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipMapper.class);

    @EJB
    HelperTableDAO helperTableDAO;

    @EJB
    TargetDAO targetDAO;

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    BasicTypeDAO basicTypeDAO;

    @EJB
    RelationshipAttributeDAO relationshipAttributeDAO;


    private Relationship createRelationshipFromResultSet(ResultSet rs, ConceptSMTK conceptSMTK) {

        /* Concepto origen */
        //ConceptSMTK sourceConceptSMTK = conceptDAO.getConceptByID(relationshipDTO.idSourceConcept);

        try {
            /* Definición de la relación y sus atributos */
            RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionById(rs.getLong("id_relationship_definition"));

            long id = rs.getLong("id");
            Timestamp validityUntil = rs.getTimestamp("validity_until");
            long idTarget = rs.getLong("id_target");
            Timestamp creationDate = rs.getTimestamp("creation_date");

            /* El target que puede ser básico, smtk, tablas, crossmaps o snomed-ct */
            Relationship relationship = createRelationshipByTargetType(idTarget, conceptSMTK, relationshipDefinition, id, validityUntil);

            List<RelationshipAttribute> relationshipAttributes = relationshipAttributeDAO.getRelationshipAttribute(relationship);
            //relationshipDefinition.setRelationshipAttributeDefinitions(relationshipDefinitionDAO.getRelationshipAttributeDefinitionsByRelationshipDefinition(relationshipDefinition));

            relationship.setRelationshipAttributes(relationshipAttributes);
            relationship.setValidityUntil(validityUntil);
            relationship.setCreationDate(creationDate);
            return relationship;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Este método es reponsable de crear una instancia del tipo correcto de relación en función del target de un tipo
     * en particular.
     *
     * @param relationshipDefinition La relación que lo define.
     *
     * @return Una relación del tipo correcta que define el Target.
     */
    private Relationship createRelationshipByTargetType(long idTarget, ConceptSMTK conceptSMTK, RelationshipDefinition relationshipDefinition, long id, Timestamp validityUntil) {

        Target target;

        /* El target puede ser Tipo Básico */
        if (relationshipDefinition.getTargetDefinition().isBasicType()) {
            BasicTypeValue basicTypeValueByID = basicTypeDAO.getBasicTypeValueByID(idTarget);
            return new Relationship(id, conceptSMTK, basicTypeValueByID, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser a un registro de una tabla auxiliar */
        if (relationshipDefinition.getTargetDefinition().isHelperTable()) {
            //target = helperTableManager.getRecord(idTarget);
            target = targetDAO.getTargetByID(idTarget);
            /**
             * Se setea el id desde el fields para ser utilizado por el custom converter
             */
            HelperTableRow helperTableRow = (HelperTableRow) target;

            return new Relationship(id, conceptSMTK, helperTableRow, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser un concepto SMTK */
        if (relationshipDefinition.getTargetDefinition().isSMTKType()) {

            ConceptSMTK conceptByID = (ConceptSMTK) targetDAO.getTargetByID(idTarget);
            return new Relationship(id, conceptSMTK, conceptByID, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser un concepto Snomed CT */
        if (relationshipDefinition.getTargetDefinition().isSnomedCTType()) {
            ConceptSCT conceptCSTByID = (ConceptSCT) targetDAO.getTargetByID(idTarget);
            return new SnomedCTRelationship(id, conceptSMTK, conceptCSTByID, relationshipDefinition, new ArrayList<RelationshipAttribute>(), validityUntil);
        }

        /* Y sino, puede ser crossmap */
        if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
            target = targetDAO.getTargetByID(idTarget);
            //CrossmapSetMember crossmapSetMemberById = crossmapDAO.getCrossmapSetMemberById(idTarget);
            return new DirectCrossmap(id, conceptSMTK, (CrossmapSetMember)target, relationshipDefinition, validityUntil);
        }

        /* Sino, hay un nuevo tipo de target que no está siendo gestionado */
        String msg = "Un tipo no manejado de Target se ha recibido.";
        logger.error(msg);
        throw new EJBException(msg);
    }
}
