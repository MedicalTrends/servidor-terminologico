package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;

import javax.ejb.Local;

/**
 * Created by des01c7 on 17-11-16.
 */
@Local
public interface ConceptDefinitionalGradeBRInterface {
    public void apply(ConceptSMTK conceptSMTK);

}
