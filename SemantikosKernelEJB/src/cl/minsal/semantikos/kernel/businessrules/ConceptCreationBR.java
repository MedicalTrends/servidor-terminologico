package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinitionFactory;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

import static cl.minsal.semantikos.model.users.ProfileFactory.MODELER_PROFILE;

/**
 * Este componente es responsable de almacenar las reglas de negocio relacionadas a la persistencia de conceptos.
 *
 * @author Andrés Farías
 */
public class ConceptCreationBR implements BusinessRulesContainer {

    private static final Logger logger = LoggerFactory.getLogger(ConceptCreationBR.class);

    public void apply(@NotNull ConceptSMTK conceptSMTK, User IUser) throws Exception {

        /* Reglas que aplican para todas las categorías */
        br101HasFSN(conceptSMTK);
        br102NonEmptyDescriptions(conceptSMTK);
        brTagSMTK001(conceptSMTK);
        /* Creación de acuerdo al rol */
        br001creationRights(conceptSMTK, IUser);
        br103DefinitionalGrade(conceptSMTK);
    }

    /**
     * BR-TagSMTK-001: Cuando se diseña un nuevo Término se le asigna un Tag por defecto.
     *
     * @param conceptSMTK El concepto sobre el cual se valida que tenga un Tag Semantikos asociado.
     */
    protected void brTagSMTK001(ConceptSMTK conceptSMTK) {

        /* Se obtiene el Tag Semantikos */
        TagSMTK tagSMTK = conceptSMTK.getTagSMTK();

        /* Se valida que no sea nulo y que sea de los oficiales */
        if (tagSMTK == null || !tagSMTK.isValid()) {
            throw new BusinessRuleException("BR-TagSMTK-001", "Todo concepto debe tener un Tag Semántikos (válido).");
        }
    }

    /**
     * Esta regla de negocio valida que un concepto no puede tener descripciones nulas o vacias.
     *
     * @param conceptSMTK El concepto cuyas descripciones de validan.
     */
    private void br102NonEmptyDescriptions(ConceptSMTK conceptSMTK) {

        for (Description description : conceptSMTK.getDescriptions()) {
            String term = description.getTerm();
            if (term == null) {
                throw new BusinessRuleException("BR-TagSMTK-002", "El término de una descripción no puede ser nulo.");
            } else if (term.trim().equals("")) {
                throw new BusinessRuleException("BR-TagSMTK-002", "El término de una descripción no puede ser vacío.");
            }
        }
    }

    /**
     * <b>BR-SMTK-001</b>: Conceptos de ciertas categorías pueden sólo ser creados por usuarios con el perfil
     * Modelador.
     *
     * @param conceptSMTK El concepto a crear ser creado.
     * @param user        El usuario que realiza la acción.
     */
    protected void br001creationRights(ConceptSMTK conceptSMTK, User user) {

        /* Categorías restringidas para usuarios con rol diseñador */
        if (conceptSMTK.getCategory().isRestriction()) {
            if (!user.getProfiles().contains(MODELER_PROFILE)) {
                logger.info("Se intenta violar la regla de negocio BR-SMTK-001 por el usuario " + user);
                throw new BusinessRuleException("BR-SMTK-001", "El usuario " + user + " no tiene privilegios para crear conceptos de la categoría " + conceptSMTK.getCategory());
            }
        }
    }

    /**
     * REGLA DE NEGOCIO por definir. Cada concepto debe tener un FSN
     *
     * @param conceptSMTK El concepto que se valida.
     */
    private void br101HasFSN(ConceptSMTK conceptSMTK) {
        Description descriptionFSN = conceptSMTK.getDescriptionFSN();

        if (descriptionFSN == null) {
            throw new BusinessRuleException("BR-UNK", "Todo concepto debe tener una descripción FSN");
        }
    }

    public void br103DefinitionalGrade(ConceptSMTK conceptSMTK) throws Exception {

        if(conceptSMTK.isFullyDefined()!=null)
            return;

        if(!conceptSMTK.isRelationshipsLoaded()) {
            throw new BusinessRuleException("BR-UNK","Intento de cálculo de grado de definición para concepto sin relaciones cargadas",conceptSMTK);
        }

        /**Se obtiene la definición de relacion SNOMED CT**/
        RelationshipDefinition relationshipDefinition = RelationshipDefinitionFactory.getInstance().findRelationshipDefinitionByName(TargetDefinition.SNOMED_CT);

        /**
         * Si alguna de las relaciones es de tipo snomed es posible determinar el grado de definición
         */
        for (Relationship relationship : conceptSMTK.getRelationships()) {
            if(relationship.getRelationshipDefinition().equals(relationshipDefinition)) {
                ConceptSCT conceptSCT = (ConceptSCT) relationship.getTarget();
                conceptSMTK.setFullyDefined((conceptSCT.isCompletelyDefined()) ? true : false);
                conceptSMTK.setInherited(true);
                return;
            } else {
                conceptSMTK.setInherited(false);
                return;
            }
        }
    }
}
