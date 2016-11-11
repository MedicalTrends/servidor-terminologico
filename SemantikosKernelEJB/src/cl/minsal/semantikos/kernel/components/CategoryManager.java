package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.text.ParseException;
import java.util.List;

/**
 * Esta interfaz tiene como propósito definir el comportamiento del componente de gestión de categorías.
 *
 * @author Andrés Farías
 */
@Local
public interface CategoryManager {

    /**
     * Este método es responsable de buscar un término en el contexto de una categoría.
     *
     * @param category La categoría en la cual se realiza la búsqueda.
     * @param term     El término que se busca en la categoría.
     *
     * @return <code>true</code> si la categoría contiene el término <code>term</code> y <code>false</code> sino.
     */
    public boolean categoryContains(Category category, String term);

    /**
     * Este método es responsable de recuperar una Categoría completa, con sus propiedades básicas y todos sus
     * meta-atributos
     *
     * @param idCategory Identificador único de la categoría.
     *
     * @return La categoría buscada.
     */
    public Category getCategoryById(int idCategory) throws ParseException;

    /**
     * Entrega la Categoría que tiene un nombre LIKE @{name} o NULL si no existe
     * @param name
     * @return
     */
    public Category getCategoryByName(String name);

    /**
     * Este método responsable de recuperar toda la meta-data que consituye la definición de una categoría, en
     * particular todos los atributos que define.
     *
     * @param id Identificador único de la categoría.
     *
     * @return La lista de definiciones de atributos de la categoría.
     */
    public List<RelationshipDefinition> getCategoryMetaData(int id);

    /**
     * Método encagado de recuperar todas las categorías existentes.
     *
     * @return Lista de categorías
     */
    public List<Category> getCategories();

    public void addAttribute(RelationshipDefinition attributeCategory, int idCategory);

    /**
     * Este método permite crear de manera persistente una categoría, con todas sus definiciones.
     *
     * @param category La categoría que se desea crear.
     * @param user     El usuario que crea la categoría.
     *
     * @return La Categoría con su ID actualizado.
     */
    public Category createCategory(Category category, User user);
}
