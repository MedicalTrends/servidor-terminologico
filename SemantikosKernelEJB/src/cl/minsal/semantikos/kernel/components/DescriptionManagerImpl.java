package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.businessrules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías
 */
@Stateless
public class DescriptionManagerImpl implements DescriptionManager {

    private static final Logger logger = LoggerFactory.getLogger(DescriptionManagerImpl.class);

    @EJB
    DescriptionDAO descriptionDAO;

    @EJB
    CategoryManager categoryManager;

    @EJB
    private AuditManager auditManager;

    @EJB
    private ConceptManager conceptManager;

    /* El conjunto de reglas de negocio para validar creación de descripciones */
    private DescriptionCreationBR descriptionCreationBR = new DescriptionCreationBR();

    @Override
    public void createDescription(Description description, boolean editionMode, User user) {

        /* Reglas de negocio previas */
        ConceptSMTK conceptSMTK = description.getConceptSMTK();
        DescriptionCreationBR descriptionCreationBR1 = new DescriptionCreationBR();
        descriptionCreationBR1.validatePreConditions(conceptSMTK, description, categoryManager, editionMode);

        descriptionCreationBR1.applyRules(conceptSMTK, description.getTerm(), description.getDescriptionType(), user, categoryManager);
        if (!description.isPersistent()) {
            descriptionDAO.persist(description, user);
        }

        /* Si el concepto al cual se agrega la descripción está modelado, se registra en el historial */
        if (conceptSMTK.isModeled()) {
            auditManager.recordDescriptionCreation(description, user);
        }
    }

    @Override
    public Description bindDescriptionToConcept(ConceptSMTK concept, String term, DescriptionType descriptionType, User user) {

        /* Se aplican las reglas de negocio para crear la Descripción*/
        descriptionCreationBR.applyRules(concept, term, descriptionType, user, categoryManager);

        /* Se crea la descripción */
        Description description = new Description(concept, term, descriptionType);

        /* Se aplican las reglas de negocio para crear la Descripción y se persiste y asocia al concepto */
        new DescriptionBindingBR().applyRules(concept, description, user);
        descriptionDAO.persist(description, user);
        if (!concept.getDescriptions().contains(description)) {
            concept.addDescription(description);
        }

        /* Se retorna la descripción persistida */
        return description;
    }

    @Override
    public Description bindDescriptionToConcept(ConceptSMTK concept, Description description, boolean editionMode, User user) {

        /*
         * Se aplican las pre-condiciones para asociar la descripción al concepto. En particular hay que validar que
         * no exista el término dentro de la misma categoría
         */
        descriptionCreationBR.validatePreConditions(concept, description, categoryManager, editionMode);

        /* Se aplican las reglas de negocio para crear la Descripción y se persiste y asocia al concepto */
        new DescriptionBindingBR().applyRules(concept, description, user);

        /* Se hace la relación a nivel lógico del modelo */
        if (!concept.getDescriptions().contains(description)) {
            concept.addDescription(description);
            description.setConceptSMTK(concept);
        }

        /* Lo esperable es que la descripción no se encontrara persistida */
        if (!description.isPersistent()) {
            descriptionDAO.persist(description, user);
        }

        /* Registrar en el Historial si es preferida (Historial BR) */
        if (description.getConceptSMTK().isModeled()) {
            auditManager.recordDescriptionCreation(description, user);
        }

        /* Se retorna la descripción persistida */
        return description;
    }

    @Override
    public Description unbindDescriptionToConcept(ConceptSMTK concept, Description description, User user) {

        /* Si la descripción no se encontraba persistida, se persiste primero */
        if (!description.isPersistent()) {
            return description;
        }

        /* Se validan las reglas de negocio para eliminar una descripción */
        new DescriptionUnbindingBR().applyRules(concept, description, user);

        /* Se establece la fecha de vigencia y se actualiza la descripción */
        description.setValidityUntil(new Timestamp(currentTimeMillis()));
        descriptionDAO.update(description);

        /* Se retorna la descripción actualizada */
        return description;
    }

    @Override
    public void updateDescription(@NotNull ConceptSMTK conceptSMTK, @NotNull Description initDescription, @NotNull Description finalDescription, @NotNull User user) {

        logger.info("Se actualizan descripciones. \nOriginal: " + initDescription + "\nFinal: " + finalDescription);

        /* Se aplican las reglas de negocio */
        new DescriptionEditionBR().validatePreConditions(initDescription, finalDescription);

        /* Se actualiza el modelo de negocio primero */
        descriptionDAO.invalidate(initDescription);
        finalDescription.setConceptSMTK(conceptSMTK);
        this.bindDescriptionToConcept(conceptSMTK, finalDescription, true, user);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordFavouriteDescriptionUpdate(conceptSMTK, initDescription, user);
    }


    @Override
    public void deleteDescription(Description description, User user) {

        ConceptSMTK concept = description.getConceptSMTK();

        /* Eliminar una descripción de un modelado consiste en dejarla inválida */
        if (concept.isModeled()) {
            descriptionDAO.invalidate(description);

            /* Se registra en el Historial si el concepto está modelado */
            auditManager.recordDescriptionDeletion(concept, description, user);
        }

        /* Eliminar una descripción de un borrador es eliminarla físicamente BR-DES-005 */
        if (!concept.isModeled()) {
            descriptionDAO.deleteDescription(description);
        }
    }

    @Override
    public void moveDescriptionToConcept(ConceptSMTK sourceConcept, Description description, User user) {

        ConceptSMTK targetConcept = description.getConceptSMTK();

        /* Se aplican las reglas de negocio para el traslado */
        DescriptionTranslationBR descriptionTranslationBR = new DescriptionTranslationBR();
        descriptionTranslationBR.validatePreConditions(description, targetConcept);

        /* Se realiza la actualización a nivel del modelo lógico */

        List<Description> sourceConceptDescriptions = sourceConcept.getDescriptions();

        /* Se agrega al concepto destino */
        if (!targetConcept.getDescriptions().contains(description)) {
            targetConcept.addDescription(description);
        }

        /* Se actualiza el concepto dueño de la descripción en la descripción */
        description.setConceptSMTK(targetConcept);

        /* Se aplican las reglas de negocio asociadas al movimiento de un concepto */
        descriptionTranslationBR.apply(targetConcept, description);

        /*Se cambia el estado de la descripción segun el concepto*/

        description.setModeled(targetConcept.isModeled());

        /* Luego se persiste el cambio */
        descriptionDAO.update(description);

        /* Se registra en el Audit el traslado */
        auditManager.recordDescriptionMovement(sourceConcept, targetConcept, description, user);
    }

    @Override
    public String getIdDescription(String tipoDescription) {

/*      TODO: Reparar esto.
        String idDescription=null;
        try {
            Class.forName(driver);
            Connection conne = (Connection) DriverManager.getConnection(ruta, user, password);
            CallableStatement call = conne.prepareCall("{call get_description_type_by_name(?)}");
            call.setString(1, tipoDescription);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                idDescription = rs.getString("iddescriptiontype");
            }
            conne.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        return idDescription;
*/
        return null;

    }

    @Override
    public List<DescriptionType> getAllTypes() {

        return DescriptionTypeFactory.getInstance().getDescriptionTypes();

    }


    @Override
    public List<Description> findDescriptionsByConcept(int idConcept) {

        /*
        DAODescriptionImpl DAOdescription= new DAODescriptionImpl();

        return DAOdescription.getDescriptionBy(idConcept);

        */


        return null;
    }

    @Override
    public DescriptionType getTypeFSN() {
        return DescriptionTypeFactory.getInstance().getFSNDescriptionType();
    }

    @Override
    public DescriptionType getTypeFavorite() {
        return DescriptionTypeFactory.getInstance().getFavoriteDescriptionType();
    }

    @Override
    public List<Description> getDescriptionsOf(ConceptSMTK concept) {
        return descriptionDAO.getDescriptionsByConcept(concept);
    }

    @Override
    public String generateDescriptionId() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    @Override
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories) {
        return descriptionDAO.searchDescriptionsByTerm(term, categories);
    }

    @Override
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories, List<RefSet> refSets) {
        return descriptionDAO.searchDescriptionsByTerm(term, categories, refSets);
    }

    @Override
    public void invalidateDescription(ConceptSMTK conceptSMTK, NoValidDescription noValidDescription, User user) {

        /* Se aplican las reglas de negocio para el traslado */
        DescriptionInvalidationBR descriptionInvalidationBR = new DescriptionInvalidationBR();
        descriptionInvalidationBR.validatePreConditions(noValidDescription);

        /* Se realiza el movimiento con la función genérica */
        Description theInvalidDescription = noValidDescription.getNoValidDescription();

        this.moveDescriptionToConcept(conceptSMTK, theInvalidDescription, user);

        /* Luego se persiste el cambio */
        descriptionDAO.setInvalidDescription(noValidDescription);

        /* No hay registro en el log, porque se registra ya en la función de negocio de mover */
    }

    @Override
    public List<ObservationNoValid> getObservationsNoValid() {
        return descriptionDAO.getObservationsNoValid();
    }

    @Override
    public Description getDescriptionByID(long id) {
        return descriptionDAO.getDescriptionBy(id);
    }
}
