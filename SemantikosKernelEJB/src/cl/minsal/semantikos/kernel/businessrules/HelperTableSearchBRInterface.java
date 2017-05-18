package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.helpertables.HelperTable;

import javax.ejb.Remote;


@Remote
public interface HelperTableSearchBRInterface {

    public int getMinQueryLength(HelperTable helperTable);

}
