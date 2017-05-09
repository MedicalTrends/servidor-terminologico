package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.categories.Category;

/**
 * @author Andrés Farías on 9/13/16.
 */
public class CategoryInvanriantsBR {

    /**
     * Este método es responsable de aplicar todas las invariantes sobre la entidad de Categorías.
     * @param category La categoría sobre la cual se validan las invariantes.
     */
    public void invariants(Category category) {
        brCategoryInvariant001(category);
    }

    private void brCategoryInvariant001(Category category) {

    }

}
