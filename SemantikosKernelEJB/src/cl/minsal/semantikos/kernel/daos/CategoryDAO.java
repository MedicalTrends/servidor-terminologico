package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;


/**
 * Este DAO es responsable de recuperar información relativa a las categorías desde la BD.
 *
 * @author Andrés Farías
 */

@Local
public interface CategoryDAO {
    /**
     * Este método es responsable de recuperar toda la información de una categoría desde la BD y retornarla bien
     * organizada en un objeto de negocio.
     *
     * TODO: Hacer esto con un cache
     *
     * @return La categoría requerida por su ID.
     */
    public Category getCategoryById(long id);

    /**
     * Este método es responsable de recuperar todas las categorías del sistema.
     *
     * @return Una lista de categorías.
     */
    public List<Category> getAllCategories();

    /**
     * Este método responsable de recuperar toda la meta-data que consituye la definición de una categoría, en
     * particular todos los atributos que define.
     *
     * @param idCategory Identificador único de la categoría.
     *
     * @return La lista de definiciones de atributos de la categoría.
     */
    public List<RelationshipDefinition> getCategoryMetaData(long idCategory);

    /**
     * Este método permite persistir una categoría, con todas sus definiciones.
     *
     * @param category La categoría que se desea crear.
     */
    public void persist(Category category);


    /**
     * Este método es responsable de obtener las categorías que se relacionan con la categoría indicada como parametro
     *
     * @param category concepto que se relaciona con otros
     *
     * @return Lista de categorías relacionados
     */
    public List<Category> getRelatedCategories(Category category);

    /**
     * Este método es responsable de recuperar una categoría con un nombre en particular. De no encontrarse la categoría
     * se arroja una excepción de argumento.
     *
     * @param categoryName El nombre de la categoría.
     *
     * @return Un objeto fresco que representa la categoría.
     */
    public Category getCategoryByName(String categoryName);
}