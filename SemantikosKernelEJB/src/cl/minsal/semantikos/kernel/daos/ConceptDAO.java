package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.*;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Gustavo Punucura
 */
@Local
public interface ConceptDAO {

    /**
     * Este método es responsable de eliminar un concepto persistente.
     *
     * @param conceptSMTK El concepto que se desea eliminar.
     */
    public void delete(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de recuperar todos los conceptos, sin considerar su categoría, que posean un cierto
     * estado interno.
     *
     * @param modeled    Si el concepto está modelado.
     * @param pageSize   El tamaño de la página.
     * @param pageNumber La página de resultados que se desea obtener.
     *
     * @return Una lista de conceptos que coinciden con los parámetros de búsqueda (modelado o no).
     */
    public List<ConceptSMTK> getConceptsBy(boolean modeled, int pageSize, int pageNumber);

    /**
     * Este método es responsable de recuperar los conceptos que coincidan con un cierto patrón (<code>pattern</code>)
     * y que pertenezcan a una o más categorías.
     *
     * @param pattern    El patrón de búsqueda en los términos.
     * @param categories Las categorías en las que se realiza la búsqueda.
     * @param modeled    Los estados que deben satisfacer los conceptos a retornar.
     * @param pageSize   El tamaño de la página.
     * @param pageNumber La página de resultados que se desea obtener.
     *
     * @return Una lista de <code>ConceptSMTK</code> que cumplen los criterios de búsqueda.
     */
    public List<ConceptSMTK> getConceptBy(String[] pattern, Long[] categories, boolean modeled, int pageSize, int pageNumber);

    /**
     * Este método es responsable de recuperar los conceptos que pertenecen a un conjunto de categorías.
     *
     * @param categories Las categorías desde donde se extraen los conceptos.
     * @param modeled    El estado de los conceptos que se desea obtener.
     * @param pageSize   El tamaño de la página.
     * @param pageNumber La página de resultados que se desea obtener.
     *
     * @return Una lista de <code>ConceptSMTK</code> que cumplen los criterios de búsqueda.
     */
    public List<ConceptSMTK> getConceptBy(Long[] categories, boolean modeled, int pageSize, int pageNumber);

    public List<ConceptSMTK> getModeledConceptBy(Long categoryId, int pageSize, int pageNumber);

    /*Método temporal para trabajar con el navegador de conceptos*/
    @Deprecated
    public List<ConceptSMTK> getConceptBy(Category category, int pageSize, int pageNumber);

    @Deprecated
    public List<ConceptSMTK> getConceptBy(Category category);

    public List<ConceptSMTK> getConceptBy(String[] pattern, boolean isModeled, int pageSize, int pageNumber);

    public List<ConceptSMTK> getConceptBy(String PatternOrConceptId, Long[] Category, int pageNumber, int pageSize, boolean isModeled);

    public int countConceptBy(String[] Pattern, Long[] category, boolean isModeled);

    public int countConceptBy(String Pattern, Long[] category, boolean isModeled);

    public int countModeledConceptBy(Long categoryId);

    /**
     * Este método es responsable de recuperar todos los objetos que están asociados a un Tag.
     *
     * @param tag El tag del cual los conceptos que están asociados se desean recuperar.
     *
     * @return Una lista de conceptos, donde cada uno se encuentra asociado al Tag <code>tag</code>.
     */
    List<ConceptSMTK> findConceptsByTag(Tag tag);

    /**
     * Este método es responsable de recuperar el concepto con DESCRIPTION_ID.
     *
     * @param conceptID El DESCRIPTION_ID (valor de negocio) del concepto que se desea recuperar.
     *
     * @return Un objeto fresco de tipo <code>ConceptSMTK</code> con el Concepto solicitado.
     */
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptID);

    public ConceptSMTK getConceptByID(long id);

    public ConceptSMTK getConceptBy(Category category, long id);

    /**
     * Este método es responsable persistir la entidad Concepto SMTK en la base de datos.
     *
     * @param conceptSMTK El concepto que será persistido.
     * @param user        El usuario que se está persistiendo.
     */
    public void persistConceptAttributes(ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de actualizar la información base de un concepto (no sus relaciones o descripciones).
     *
     * @param conceptSMTK El concepto cuya información básica se actualizará.
     */
    public void update(ConceptSMTK conceptSMTK);

    public List<ConceptSMTK> getConceptBy(RefSet refSet);

    public List<ConceptSMTK> findModeledConceptsBy(RefSet refSet, int page, int pageSize);

    public Integer countModeledConceptsBy(RefSet refSet);

    /**
     * Metodo  que retorna los conceptos en borrador
     * @return lista de conceptos en borrador
     */

    public List<ConceptSMTK> getConceptDraft();

    /**
     * Este método es responsable de cambiar el estado de un concepto y sus descripciones al estado Modelado.
     *
     * @param idConcept Identificador del concepto.
     */
    public void forcedModeledConcept(Long idConcept);

    /**
     * Este método es responsable de recuperar el Concepto no Válido.
     *
     * @return El concepto no válido.
     */
    public ConceptSMTK getNoValidConcept();

    /**
     * Este método es responsable de obtener los conceptos que se relacionan con el concepto indicado como parametro
     * @param conceptSMTK concepto que se relaciona con otros
     * @return Lista de conceptos relacionados
     */
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK);

}
