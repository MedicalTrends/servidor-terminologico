package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.PendingTermDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.kernel.businessrules.PendingTermAddingBR;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrés Farías on 11/23/16.
 */
@Stateless
public class PendingTermsManagerImpl implements PendingTermsManager {

    @EJB
    PendingTermDAO pendingTermDAO;

    @EJB
    PendingTermAddingBR pendingTermAddingBR;

    @EJB
    ConceptManager conceptManager;

    @EJB
    DescriptionManager descriptionManager;

    private final static Logger logger = LoggerFactory.getLogger(PendingTermsManagerImpl.class);

    @AroundInvoke
    public Object postActions(InvocationContext ic) throws Exception {
        try {
            return ic.proceed();
        } finally {
            if(Arrays.asList(new String[]{"addPendingTerm"}).contains(ic.getMethod().getName())) {
                for (Object o : ic.getParameters()) {
                    if(o instanceof PendingTerm) {
                        pendingTermDAO.updateSearchIndexes((PendingTerm)o);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Description addPendingTerm(PendingTerm pendingTerm, User loggedUser) {

        /* Validación de pre-condiciones */
        pendingTermAddingBR.validatePreConditions(pendingTerm);

        /* Acciones de negocio a continuación */

        /* 1. Persistir el término pendiente */
        pendingTermDAO.persist(pendingTerm);
        logger.info("Pending term persited: " + pendingTerm);

        /* 2. Agregarlo al concepto especial 'Pendientes' */
        ConceptSMTK pendingTermsConcept = conceptManager.getPendingConcept();
        Description description = descriptionManager.bindDescriptionToConcept(pendingTermsConcept, pendingTerm.getTerm(), pendingTerm.isSensibility(), DescriptionType.SYNONYMOUS, loggedUser);
        logger.info("Description from pending term created: " + description.fullToString());

        pendingTerm.setRelatedDescription(description);
        pendingTermDAO.bindTerm2Description(pendingTerm, description);

        /* Validación de post-condiciones */
        pendingTermAddingBR.validatePostConditions(pendingTerm);

        return description;
    }

    @Override
    public List<PendingTerm> getAllPendingTerms() {
        return pendingTermDAO.getAllPendingTerms();
    }

    @Override
    public PendingTerm getPendingTermById(long id) {
        return pendingTermDAO.getPendingTermById(id);
    }


}
