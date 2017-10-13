package cl.minsal.semantikos.kernel.singletons;

import cl.minsal.semantikos.model.categories.Category;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

@Singleton
public class CategorySingleton implements Serializable {

    /** La lista de categorías */
    private List<Category> categories= new ArrayList<>();

    /** Mapa de categorías por su id. */
    private static ConcurrentHashMap<Long, Category> categoriesById = new ConcurrentHashMap<>();

    /** Mapa de categorías por su nombre. */
    private static ConcurrentHashMap<String, Category> categoriesByName = new ConcurrentHashMap<>();

    @Lock(READ)
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    @Lock(READ)
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
    @Lock(READ)
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
    @Lock(WRITE)
    public void setCategories( List<Category> categories) {

        /* Se actualiza la lista */
        this.categories = categories;

        /* Se actualiza el mapa por nombres */
        this.categoriesById.clear();
        this.categoriesByName.clear();

        for (Category category : categories) {
            this.categoriesById.put(category.getId(), category);
            this.categoriesByName.put(category.getName().toLowerCase(), category);
        }
    }

    @Lock(READ)
    public ConcurrentHashMap<String, Category> getCategoriesByName() {
        return categoriesByName;
    }

    @Lock(WRITE)
    public void setCategoriesByName(ConcurrentHashMap<String, Category> categoriesByName) {
        this.categoriesByName = categoriesByName;
    }
}
