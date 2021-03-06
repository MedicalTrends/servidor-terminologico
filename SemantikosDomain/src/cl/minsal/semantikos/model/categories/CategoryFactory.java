package cl.minsal.semantikos.model.categories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class CategoryFactory implements Serializable {

    private static final CategoryFactory instance = new CategoryFactory();

    public List<Category> getCategories() {
        return categories;
    }

    /** La lista de categorías */
    private List<Category> categories;

    /** Mapa de categorías por su id. */
    private static ConcurrentHashMap<Long, Category> categoriesById;

    /** Mapa de categorías por su nombre. */
    private static ConcurrentHashMap<String, Category> categoriesByName;

    public static Category SPECIAL_CONCEPT;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private CategoryFactory() {
        this.categories = new ArrayList<>();
        this.categoriesById = new ConcurrentHashMap<>();
        this.categoriesByName = new ConcurrentHashMap<>();
    }

    public static CategoryFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public Category findCategoryById(long id) {

        if (categoriesById.containsKey(id)) {
            return this.categoriesById.get(id);
        }
        return null;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public Category findCategoryByName(String name) {

        if (categoriesByName.containsKey(name.toLowerCase())) {
            return this.categoriesByName.get(name.toLowerCase());
        }
        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setCategories( List<Category> categories) {

        /* Se actualiza la lista */
        this.categories = categories;

        /* Se actualiza el mapa por nombres */
        this.categoriesById.clear();
        this.categoriesByName.clear();

        for (Category category : categories) {
            if(category.getName().equalsIgnoreCase("Concepto Especial")) {
                SPECIAL_CONCEPT = category;
            }
            this.categoriesById.put(category.getId(), category);
            this.categoriesByName.put(category.getName().toLowerCase(), category);
        }
    }

    public ConcurrentHashMap<String, Category> getCategoriesByName() {
        return categoriesByName;
    }

    public void setCategoriesByName(ConcurrentHashMap<String, Category> categoriesByName) {
        this.categoriesByName = categoriesByName;
    }
}
