package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.PendingTermDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.kernel.businessrules.PendingTermAddingBR;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.Roles;
import cl.minsal.semantikos.model.users.User;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrés Farías on 11/23/16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
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

    private boolean exceptions = false;

    /*
    @AroundInvoke
    public Object postActions(InvocationContext ic) throws Exception {
        try {
            return ic.proceed();
        }
        finally {

            if(Arrays.asList(new String[]{"addPendingTerm"}).contains(ic.getMethod().getName())) {
                for (Object o : ic.getParameters()) {
                    if(o instanceof PendingTerm) {
                        pendingTermAddingBR.validatePostConditions((PendingTerm)o);
                        break;
                    }
                }
            }
        }
    }
    */

    @Override
    @PermitAll
    public Description addPendingTerm(PendingTerm pendingTerm, User loggedUser) throws EJBTransactionRolledbackException {

        try {
            /* Validación de pre-condiciones */
            pendingTermAddingBR.validatePreConditions(pendingTerm);

            /* Acciones de negocio a continuación */

            /* 1. Persistir el término pendiente */
            pendingTerm.setId(pendingTermDAO.persist(pendingTerm));
            logger.info("Pending term persited: " + pendingTerm);

            /* 2. Agregarlo al concepto especial 'Pendientes' */
            ConceptSMTK pendingTermsConcept = conceptManager.getPendingConcept();
            Description description = descriptionManager.bindDescriptionToConcept(pendingTermsConcept, pendingTerm.getTerm(), pendingTerm.isSensibility(), DescriptionType.SYNONYMOUS, loggedUser);
            logger.info("Description from pending term created: " + description.fullToString());

            pendingTerm.setRelatedDescription(description);
            pendingTermDAO.bindTerm2Description(pendingTerm, description);

            /* Validación de post-condiciones */
            //pendingTermAddingBR.validatePostConditions(pendingTerm);

            return description;
        }
        catch (EJBException e) {
            exceptions = true;
            throw e;
        }
    }

    @Override
    @PermitAll
    public List<PendingTerm> getAllPendingTerms() {
        return pendingTermDAO.getAllPendingTerms();
    }

    @Override
    @PermitAll
    public PendingTerm getPendingTermById(long id) {
        return pendingTermDAO.getPendingTermById(id);
    }


}
