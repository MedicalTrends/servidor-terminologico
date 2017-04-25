package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Remote
public interface DrugsManager {

    public List<Category> getDrugsCategories();

    public List<ConceptSMTK> getDrugsConceptHierarchies(ConceptSMTK concept);

}
