package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.ConceptSMTK;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Local
public interface DrugsManager {

    public List<Category> getDrugsCategories();

    public List<ConceptSMTK> getDrugsConceptHierarchies(ConceptSMTK concept);

}
