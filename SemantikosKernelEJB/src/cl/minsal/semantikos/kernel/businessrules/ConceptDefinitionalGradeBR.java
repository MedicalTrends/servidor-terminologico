package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Created by des01c7 on 17-11-16.
 */
@Remote
public interface ConceptDefinitionalGradeBR {

    public void apply(ConceptSMTK conceptSMTK);

}
