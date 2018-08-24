package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.kernel.businessrules.*;
import cl.minsal.semantikos.kernel.daos.ws.DescriptionWSDAO;
import cl.minsal.semantikos.kernel.util.IDGenerator;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.*;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.EMPTY_LIST;

/**
 * @author Andrés Farías
 */
@Stateless
public class DescriptionManagerImpl implements DescriptionManager {

    private static final Logger logger = LoggerFactory.getLogger(DescriptionManagerImpl.class);

    @EJB
    cl.minsal.semantikos.kernel.daos.DescriptionDAO descriptionDAO;

    @EJB
    DescriptionWSDAO descriptionWSDAO;

    @EJB
    CategoryManager categoryManager;

    @EJB
    private AuditManager auditManager;

    @EJB
    private ConceptManager conceptManager;

    /* El conjunto de reglas de negocio para validar creación de descripciones */
    private DescriptionCreationBR descriptionCreationBR = new DescriptionCreationBR();

    @EJB
    private DescriptionTranslationBR descriptionTranslationBR;

    @EJB
    private DescriptionSearchBR descriptionSearchBR;

    private static final int SUGGESTTION_SIZE = 5;

    private static final int DESCRIPTION_SIZE = 100;

    @Override
    public void createDescription(Description description, boolean editionMode, User user) {

        /* Reglas de negocio previas */
        ConceptSMTK conceptSMTK = description.getConceptSMTK();
        DescriptionCreationBR descriptionCreationBR1 = new DescriptionCreationBR();
        descriptionCreationBR1.validatePreConditions(conceptSMTK, description, categoryManager, editionMode);

        descriptionCreationBR1.applyRules(conceptSMTK, description.getTerm(), description.getDescriptionType(), user, categoryManager);

        if (!description.isPersistent()) {
            descriptionDAO.persist(description, user);
            description.setDescriptionId(generateDescriptionId(description.getId()));
            descriptionDAO.update(description);
        }

        /* Si el concepto al cual se agrega la descripción está modelado, se registra en el historial */
        if (conceptSMTK.isModeled()) {
            auditManager.recordDescriptionCreation(description, user);
        }
    }

    @Override
    public Description bindDescriptionToConcept(ConceptSMTK concept, String term, boolean caseSensitive, DescriptionType descriptionType, User user) {

        /* Se aplican las reglas de negocio para crear la Descripción*/
        descriptionCreationBR.applyRules(concept, term, descriptionType, user, categoryManager);

        /* Se crea la descripción */
        Description description = new Description(concept, term, descriptionType);
        description.setDescriptionId(" ");
        description.setCaseSensitive(caseSensitive);

        /* Se aplican las reglas de negocio para crear la Descripción y se persiste y asocia al concepto */
        new DescriptionBindingBR().applyRules(concept, description, user);
        descriptionDAO.persist(description, user);
        description.setDescriptionId(generateDescriptionId(description.getId()));
        descriptionDAO.update(description);
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
            description.setDescriptionId(generateDescriptionId(description.getId()));
            descriptionDAO.update(description);
        }

        descriptionDAO.update(description);

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
        if(initDescription.getConceptSMTK().getId()!=finalDescription.getConceptSMTK().getId()){
            finalDescription.setId(PersistentEntity.NON_PERSISTED_ID);
            descriptionDAO.invalidate(initDescription);
            finalDescription.setConceptSMTK(conceptSMTK);
            this.bindDescriptionToConcept(conceptSMTK, finalDescription, true, user);
        }else{
            descriptionDAO.update(finalDescription);
        }

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
        //DescriptionTranslationBR descriptionTranslationBR = new DescriptionTranslationBR();
        descriptionTranslationBR.validatePreConditions(sourceConcept,description, targetConcept, conceptManager, categoryManager);

        /* Se realiza la actualización a nivel del modelo lógico */

        List<Description> sourceConceptDescriptions = sourceConcept.getDescriptions();

        /* Se agrega al concepto destino */
        if (!targetConcept.getDescriptions().contains(description)) {
            targetConcept.addDescription(description);
        }

        /* Se actualiza el concepto dueño de la descripción en la descripción */
        description.setConceptSMTK(targetConcept);

        /* Se aplican las reglas de negocio asociadas al movimiento de un concepto */
        descriptionTranslationBR.apply(sourceConcept, targetConcept, description, conceptManager, categoryManager);

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
    public String generateDescriptionId(long id) {
        return IDGenerator.generator(String.valueOf(id),IDGenerator.TYPE_DESCRIPTION);
    }

    @Override
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories, List<RefSet> refSets) {
        long init = currentTimeMillis();
        List<Description> descriptions = descriptionDAO.searchDescriptionsByTerm(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets));
        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): " + descriptions);
        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    @Override
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Description> searchDescriptionsPerfectMatch(String term, List<Category> categories, List<RefSet> refSets) {
        //long init = currentTimeMillis();
        term = descriptionSearchBR.escapeSpecialCharacters(term);

        if(term.isEmpty()) {
            return EMPTY_LIST;
        }

        return descriptionDAO.searchDescriptionsPerfectMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets),0,DESCRIPTION_SIZE);
        //return descriptionWSDAO.searchDescriptionsPerfectMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets),0,DESCRIPTION_SIZE);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Description> searchDescriptionsPerfectMatchInParallel(String term, List<Category> categories, List<RefSet> refSets) throws InterruptedException, ExecutionException {
        long init = currentTimeMillis();
        //int cont = descriptionDAO.countDescriptionsPerfectMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets));
        int cont = 100;

        List<Description> descriptions = new ArrayList<>();

        int pageSize = 20;

        List<Future<List<Description>>> results = new ArrayList<>();

        for (int i = 0; i <= cont/pageSize ; ++i) {
            results.add(descriptionDAO.searchDescriptionsPerfectMatchAsync(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets), i, pageSize));
        }

        if(cont%pageSize != 0) {
            results.add(descriptionDAO.searchDescriptionsPerfectMatchAsync(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets), cont / pageSize, cont % pageSize));
        }

        for(Future<List<Description>> result : results){
            descriptions.addAll(result.get());
        }
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): " + descriptions);
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    @Override
    public List<Description> searchDescriptionsTruncateMatch(String term, List<Category> categories, List<RefSet> refSets) {
        long init = currentTimeMillis();
        //List<Description> descriptions = descriptionWSDAO.searchDescriptionsTruncateMatch(descriptionSearchBR.truncatePattern(term), PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets),0,100);
        term = descriptionSearchBR.escapeSpecialCharacters(term);

        if(term.isEmpty()) {
            return EMPTY_LIST;
        }

        List<Description> descriptions = descriptionDAO.searchDescriptionsTruncateMatch(descriptionSearchBR.truncatePattern(term), PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets),0,DESCRIPTION_SIZE);

        if (descriptions.isEmpty()) {
            //descriptions = descriptionWSDAO.searchDescriptionsTruncateMatch(descriptionSearchBR.truncatePattern(descriptionSearchBR.standardizationPattern(term)), PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets),0,100);
            descriptions = descriptionDAO.searchDescriptionsTruncateMatch(descriptionSearchBR.truncatePattern(descriptionSearchBR.removeStopWords(term)), PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets),0,100);
        }
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): " + descriptions);
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    @Override
    public List<Description> searchDescriptionsSuggested(String term, List<Category> categories, List<RefSet> refSets) {

        List<Description> descriptions; //= descriptionDAO.searchDescriptionsSuggested(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets));
        //logger.info("searchDescriptionsSuggested(" + term + ", " + categories + ", " + refSets + "): " + descriptions);
        //logger.info("searchDescriptionsSuggested(" + term + ", " + categories + ", " + refSets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));

        term = descriptionSearchBR.escapeSpecialCharacters(term);

        descriptions = descriptionDAO.searchDescriptionsPerfectMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets), 0, SUGGESTTION_SIZE);

        if(descriptions.size() < SUGGESTTION_SIZE) {

            int offSet = SUGGESTTION_SIZE - descriptions.size();

            descriptions.addAll(descriptionDAO.searchDescriptionsTruncateMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets), 0, offSet));

            if (descriptions.size() < SUGGESTTION_SIZE) {

                offSet = SUGGESTTION_SIZE - descriptions.size();

                List<Description> otherDescriptions = descriptionDAO.searchDescriptionsTruncateMatch(descriptionSearchBR.removeStopWords(term), PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets), 0, offSet);

                for (Description otherDescription : otherDescriptions) {
                    if(!descriptions.contains(otherDescription)) {
                        descriptions.add(otherDescription);
                    }
                }

            }
        }

        return descriptions;
    }

    @Override
    public int countDescriptionsSuggested(String term, List<Category> categories, List<RefSet> refSets) {

        term = descriptionSearchBR.escapeSpecialCharacters(term);
        //int count = descriptionDAO.countDescriptionsSuggested(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets));
        long count = descriptionDAO.countDescriptionsPerfectMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets));

        //logger.info("countDescriptionsSuggested(" + term + ", " + categories + ", " + refSets + "): " + count);
        //logger.info("countDescriptionsSuggested(" + term + ", " + categories + ", " + refSets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));

        if (count != 0) {
            return (int)count;
        } else {
            return descriptionDAO.countDescriptionsTruncateMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets));
        }
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
    public Description getDescriptionByDescriptionID(String descriptionId) {

        /* Validación de integridad */
        if (descriptionId == null || descriptionId.trim().equals("")) {
            throw new IllegalArgumentException("Se busca una descripción sin indicar su DESCRIPTION_ID.");
        }

        return descriptionDAO.getDescriptionByDescriptionID(descriptionId);
    }

    @Override
    public List<ObservationNoValid> getObservationsNoValid() {
        return descriptionDAO.getObservationsNoValid();
    }

    @Override
    public Description getDescriptionByID(long id) {
        return descriptionDAO.getDescriptionBy(id);
    }

    @Override
    public NoValidDescription getNoValidDescriptionByID(long id) {
        return descriptionDAO.getNoValidDescriptionByID(id);
    }

    @Override
    public Description incrementDescriptionHits(String descriptionId) {

        /* Primero se recupera la descripción */
        Description descriptionByDescriptionID = descriptionDAO.getDescriptionByDescriptionID(descriptionId);
        logger.info("DESCRIPTION ID=" + descriptionId + " tiene " + descriptionByDescriptionID.getUses() + " usos.");

        /* Se incrementa y se actualiza en la BDD */
        /* Se pregunta por el concepto, para soportar el incremento del uso de términos pendientes y no válidos */
        if(descriptionByDescriptionID.getConceptSMTK().isModeled()) {
            descriptionByDescriptionID.setUses(descriptionByDescriptionID.getUses() + 1);
            descriptionDAO.update(descriptionByDescriptionID);
        }
        else {
            throw new BusinessRuleException("BR-DESC-003", "Para incrementar el contador de usos la descripción debe estar modelada.", descriptionByDescriptionID.getConceptSMTK());
        }

        logger.info("DESCRIPTION ID=" + descriptionId + " tiene ahora " + descriptionByDescriptionID.getUses() + " usos.");

        /* Finalmente se retorna */
        return descriptionByDescriptionID;
    }

    @Override
    public DescriptionTypeFactory getDescriptionTypeFactory() {
        return DescriptionTypeFactory.getInstance();
    }
}
