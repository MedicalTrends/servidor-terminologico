package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Created by des01c7 on 17-11-16.
 */
@Local
public interface ConceptEditionBusinessRuleContainer {

    public void preconditionsConceptInvalidation(ConceptSMTK conceptSMTK, User user);

    public void preconditionsConceptEditionTag(ConceptSMTK conceptSMTK);

}
