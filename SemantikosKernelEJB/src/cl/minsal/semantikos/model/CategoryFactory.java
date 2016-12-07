package cl.minsal.semantikos.model;

import cl.minsal.semantikos.kernel.daos.DAO;

/**
 * @author Andrés Farías on 9/2/16.
 */
public class CategoryFactory {

    private static final Category nullCategory = new Category(DAO.NON_PERSISTED_ID, "Categoría Raíz", "Raiz", false, "black", new TagSMTK(-1, null));

    public static Category getNullCategory() {
        return nullCategory;
    }
}
