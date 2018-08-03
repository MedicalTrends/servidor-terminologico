package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.businessrules.*;
import cl.minsal.semantikos.kernel.daos.RefSetDAO;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.*;
import cl.minsal.semantikos.util.StringUtils;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.refsets.RefSet;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías on 9/20/16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class RefSetManagerImpl implements RefSetManager {

    private static final Logger logger = LoggerFactory.getLogger(RefSetManagerImpl.class);

    @EJB
    private RefSetDAO refsetDAO;

    @EJB
    private AuditManager auditManager;

    @EJB
    private RefSetCreationBR refSetCreationBR;

    @Override
    @PermitAll
    public RefSet createRefSet(RefSet refSet, User user) {

        refSetCreationBR.validatePreConditions(refSet, user);

        RefSet refsetPersist = createRefSet(refSet.getName(), refSet.getInstitution(), user);

        /* Se guardan los conceptos asignados al refset*/
        for (ConceptSMTK concept : refSet.getConcepts()) {
            bindConceptToRefSet(concept, refsetPersist, user);
            refsetPersist.bindConceptTo(concept);
        }

        return refsetPersist;
    }

    @Override
    @PermitAll
    public RefSet createRefSet(String name, Institution institution, User user) {

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
    @PermitAll
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
    @PermitAll
    public void bindConceptToRefSet(ConceptSMTK conceptSMTK, RefSet refSet, User user) {

        /* Se validan las pre-condiciones */
        new RefSetBindingBR().validatePreConditions();

        /* Se asocia la descripción al RefSet */
        refsetDAO.bind(conceptSMTK, refSet);

        /* Se registra la creación */
        auditManager.recordRefSetBinding(refSet, conceptSMTK, user);
    }

    @Override
    @PermitAll
    public void unbindConceptToRefSet(ConceptSMTK conceptSMTK, RefSet refSet, User user) {

        /* Se validan las pre-condiciones */
        new RefSetUnbindingBR().validatePreConditions();

        /* Se asocia la descripción al RefSet */
        refsetDAO.unbind(conceptSMTK, refSet);

        /* Se registra la creación */
        auditManager.recordRefSetUnbinding(refSet, conceptSMTK, user);
    }

    @Override
    @PermitAll
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
    @PermitAll
    public List<RefSet> getAllRefSets() {

        return refsetDAO.getReftsets();
    }

    @Override
    @PermitAll
    public List<RefSet> getValidRefSets() {

        return refsetDAO.getValidRefsets();
    }

    @Override
    @PermitAll
    public List<RefSet> getRefsetByInstitution(Institution institution) {
        if (institution.getId() == -1) {
            return Collections.emptyList();
        } else {
            return refsetDAO.getRefsetBy(institution);
        }
    }

    @Override
    @PermitAll
    public List<RefSet> getRefsetByUser(User user) {

        if(!user.getProfiles().contains(ProfileFactory.REFSET_ADMIN_PROFILE) &&
           !user.getProfiles().contains(ProfileFactory.ADMINISTRATOR_PROFILE)) {
            throw new BusinessRuleException("BR-UNK", "El usuario debe poseer los perfiles de administración necesarios para realizar esta acción");
        }

        List<RefSet> refsets = new ArrayList<>();

        List<Institution> institutions = user.getInstitutions();

        if(institutions.contains(InstitutionFactory.MINSAL)) {
            return getAllRefSets();
        }

        institutions.add(InstitutionFactory.MINSAL);

        for (Institution institution : user.getInstitutions()) {
            refsets.addAll(getRefsetByInstitution(institution));
        }

        return refsets;
    }

    @Override
    @PermitAll
    public boolean canWrite(User user, RefSet refSet) {
        if(user.getInstitutions().contains(InstitutionFactory.MINSAL)) {
            return true;
        }
        else {
            return user.getInstitutions().contains(refSet.getInstitution());
        }
    }

    @Override
    @PermitAll
    public List<RefSet> getRefsetsBy(ConceptSMTK conceptSMTK) {
        return refsetDAO.getRefsetsBy(conceptSMTK);
    }

    @Override
    public List<RefSet> getRefsetsBy(List<Long> categories, String pattern) {
        // TODO: Terminar lo que sea que haga esto
        return null;
    }

    @Override
    @PermitAll
    public List<RefSet> findRefsetsByName(String pattern) {
        return this.refsetDAO.findRefsetsByName(StringUtils.toSQLLikePattern(pattern));
    }

    @Override
    @PermitAll
    public RefSet getRefsetByName(String pattern) {
        List<RefSet> found = this.findRefsetsByName(pattern);
        if (!found.isEmpty()) {
            return found.get(0);
        }
        throw new NoSuchElementException("RefSet no encontrado: " + pattern);
    }

    @Override
    @PermitAll
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
    @PermitAll
    public void loadConceptRefSets(ConceptSMTK conceptSMTK) {
        if (conceptSMTK != null) {
            conceptSMTK.setRefsets(this.findByConcept(conceptSMTK));
        }
    }

    @Override
    @PermitAll
    public List<RefSet> findByConcept(ConceptSMTK conceptSMTK) {
        return this.refsetDAO.findByConcept(conceptSMTK);
    }

}
