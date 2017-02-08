package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.RefSetDAO;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.businessrules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.in;

/**
 * @author Andrés Farías on 9/20/16.
 */
@Stateless
public class RefSetManagerImpl implements RefSetManager {

    private static final Logger logger = LoggerFactory.getLogger(RefSetManagerImpl.class);

    @EJB
    private RefSetDAO refsetDAO;

    @EJB
    private AuditManager auditManager;

    @Override
    public RefSet createRefSet(RefSet refSet, User user) {
        RefSet refsetPersist = createRefSet(refSet.getName(), refSet.getInstitution(), user);

        /* Se guardan los conceptos asignados al refset*/
        for (ConceptSMTK concept : refSet.getConcepts()) {
            bindConceptToRefSet(concept, refsetPersist, user);
            refsetPersist.bindConceptTo(concept);
        }

        return refsetPersist;
    }

    @Override
    public RefSet createRefSet(String name, Institution institution, User user) {

        /* Se validan las pre-condiciones */
        new RefSetCreationBR().validatePreConditions(institution, user);

        /* Se crea el RefSet y se persiste */
        RefSet refSet = new RefSet(name, institution, new Timestamp(currentTimeMillis()));
        refsetDAO.persist(refSet);

        //TODO: Verificar si se debe guardar un registro
        /* Se registra la creación */
        //auditManager.recordRefSetCreation(refSet, user);


        /* Se registra la creación del RefSet */
        return refSet;
    }

    @Override
    public RefSet updateRefSet(RefSet refSet, User user) {

        /* Se validan las pre-condiciones */
        new RefSetUpdateBR().validatePreConditions(refSet, user);

        /* Se crea el RefSet y se persiste */
        refsetDAO.update(refSet);


        //TODO: Verificar si se debe guardar un registro
        /* Se registra la creación */
        //auditManager.recordRefSetUpdate(refSet, user);


        /* Se registra la creación del RefSet */
        return refSet;
    }

    @Override
    public void bindConceptToRefSet(ConceptSMTK conceptSMTK, RefSet refSet, User user) {

        /* Se validan las pre-condiciones */
        new RefSetBindingBR().validatePreConditions();

        /* Se asocia la descripción al RefSet */
        refsetDAO.bind(conceptSMTK, refSet);

        /* Se registra la creación */
        auditManager.recordRefSetBinding(refSet, conceptSMTK, user);
    }

    @Override
    public void unbindConceptToRefSet(ConceptSMTK conceptSMTK, RefSet refSet, User user) {

        /* Se validan las pre-condiciones */
        new RefSetUnbindingBR().validatePreConditions();

        /* Se asocia la descripción al RefSet */
        refsetDAO.unbind(conceptSMTK, refSet);

        /* Se registra la creación */
        auditManager.recordRefSetUnbinding(refSet, conceptSMTK, user);
    }

    @Override
    public void invalidate(RefSet refSet, User user) {

        /* Se validan las pre-condiciones */
        new RefSetInvalidationBR().validatePreConditions();

        /* Se asocia la descripción al RefSet */
        refSet.setValidityUntil(new Timestamp(currentTimeMillis()));
        refsetDAO.update(refSet);

        /* Se registra la creación */
        auditManager.recordRefSetInvalidate(refSet, user);
    }

    @Override
    public List<RefSet> getAllRefSets() {

        return refsetDAO.getReftsets();
    }

    @Override
    public List<RefSet> getValidRefSets() {

        return refsetDAO.getValidRefsets();
    }

    @Override
    public List<RefSet> getRefsetByInstitution(Institution institution) {
        if (institution.getId() == -1) {
            return Collections.emptyList();
        } else {
            return refsetDAO.getRefsetBy(institution);
        }

    }

    @Override
    public List<RefSet> getRefsetsBy(ConceptSMTK conceptSMTK) {
        return refsetDAO.getRefsetsBy(conceptSMTK);
    }

    @Override
    public List<RefSet> getRefsetsBy(List<Long> categories, String pattern) {
        // TODO: Terminar lo que sea que haga esto
        return null;
    }

    @Override
    public List<RefSet> findRefsetsByName(String pattern) {
        return this.refsetDAO.findRefsetsByName(StringUtils.toSQLLikePattern(pattern));
    }

    @Override
    public RefSet getRefsetByName(String pattern) {
        List<RefSet> found = this.findRefsetsByName(pattern);
        if (found != null && !found.isEmpty()) {
            return found.get(0);
        }
        return null;
    }

    @Override
    public List<RefSet> findRefSetsByName(List<String> refSetNames) {

        /* Logging */
        logger.debug("RefSetManager.findRefSetsByName(" + refSetNames + ")");

        /* Si se utilizó el método sin refsets se retorna de inmediato */
        if (refSetNames == null || refSetNames.isEmpty()){
            logger.debug("RefSetManager.findRefSetsByName(" + refSetNames + ") --> emptyList()");
            return Collections.emptyList();
        }

        List<RefSet> res = new ArrayList<>();
        for (String refSetName : refSetNames) {

            /* Si por alguna razon el refset viene vacio se ignora */
            if (refSetName == null || refSetName.trim().equals("")){
                logger.debug("RefSetManager.findRefSetsByName(" + refSetNames + ")["+refSetName + "] --> ignored");
                continue;
            }

            RefSet found = this.getRefsetByName(refSetName);
            if (found != null) {
                res.add(found);
            } else {
                throw new NoSuchElementException("RefSet no encontrado: " + refSetName);
            }
        }
        return res;
    }

    @Override
    public void loadConceptRefSets(ConceptSMTK conceptSMTK) {
        if (conceptSMTK != null) {
            conceptSMTK.setRefsets(this.findByConcept(conceptSMTK));
        }
    }

    @Override
    public List<RefSet> findByConcept(ConceptSMTK conceptSMTK) {
        return this.refsetDAO.findByConcept(conceptSMTK);
    }

}
