package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.ArrayList;
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
     * @return Una lista de conceptos que coinciden con los parámetros de búsqueda (modelado o no).
     */
    public List<ConceptSMTK> getConceptsBy(boolean modeled, int pageSize, int pageNumber);


    /**
     * Este método es responsable de recuperar los conceptos que coincidan con un cierto patrón (<code>pattern</code>)
     * y que pertenezcan a una o más categorías y uno o mas refsets.
     *
     * @return
     */
    public List<ConceptSMTK> findConceptsBy(String[] pattern, Long[] categories, Long[] refsets, boolean modeled, int pageSize, int pageNumber);

    /**
     * Este método es responsable de recuperar los conceptos que pertenecen a un conjunto de categorías.
     *
     * @param categories Las categorías desde donde se extraen los conceptos.
     * @param modeled    El estado de los conceptos que se desea obtener.
     * @param pageSize   El tamaño de la página.
     * @param pageNumber La página de resultados que se desea obtener.
     * @return Una lista de <code>ConceptSMTK</code> que cumplen los criterios de búsqueda.
     */
    public List<ConceptSMTK> findConceptsBy(Long[] categories, boolean modeled, int pageSize, int pageNumber);

    public List<ConceptSMTK> getModeledConceptBy(Long categoryId, int pageSize, int pageNumber);

    public List<ConceptSMTK> getModeledConceptPaginated(Long categoryId, int pageSize, int pageNumber);

    /*Método temporal para trabajar con el navegador de conceptos*/
    @Deprecated
    public List<ConceptSMTK> findConceptsBy(Category category, int pageSize, int pageNumber);

    /**
     * Este método es responsable de recuperar todos los conceptos asociados a una categoría.
     *
     * @param category La categoría de la cual se requieren sus conceptos.
     * @return Una lista con los conceptos asociados a la categoría.
     */
    List<ConceptSMTK> findConceptsBy(Category category);

    public List<ConceptSMTK> findConceptsBy(String PatternOrConceptId, Long[] Category, int pageNumber, int pageSize, boolean isModeled);

    public int countConceptBy(String[] Pattern, Long[] category, boolean isModeled);

    public Integer countConceptBy(String[] Pattern, Long[] category, Long[] refset, boolean isModeled);

    public int countConceptBy(String Pattern, Long[] category, boolean isModeled);

    public int countModeledConceptBy(Long categoryId);

    /**
     * Este método es responsable de recuperar todos los objetos que están asociados a un Tag.
     *
     * @param tag El tag del cual los conceptos que están asociados se desean recuperar.
     * @return Una lista de conceptos, donde cada uno se encuentra asociado al Tag <code>tag</code>.
     */
    List<ConceptSMTK> findConceptsByTag(Tag tag);

    /**
     * Este método es responsable de recuperar el concepto con DESCRIPTION_ID.
     *
     * @param conceptID El DESCRIPTION_ID (valor de negocio) del concepto que se desea recuperar.
     * @return Un objeto fresco de tipo <code>ConceptSMTK</code> con el Concepto solicitado.
     */
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptID);

    public ConceptSMTK getConceptByID(long id);

    public ConceptSMTK findConceptsBy(Category category, long id);

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

    public List<ConceptSMTK> findConceptsBy(RefSet refSet);

    public List<ConceptSMTK> findModeledConceptsBy(RefSet refSet, int page, int pageSize);

    /**
     * Este método es responsable de encontrar los conceptos de una cierta categoría que poseen un atributo buleano con
     * un cierto valor <code>aBoolean</code>.
     *
     * @param aCategory                 La categoría a la cual pertenecen los objetos buscados.
     * @param booleanBasicTypeAttribute El atributo buleano.
     * @param aString                   El valor buleano.
     * @return Una lista de conceptos con el atributo en el valor deseado.
     */
    public List<ConceptSMTK> findConceptsWithStringBasicType(Category aCategory, RelationshipDefinition booleanBasicTypeAttribute, String aString);

    /**
     * Este método es responsable de encontrar los conceptos de una cierta categoría que poseen un atributo buleano con
     * un cierto valor <code>aBoolean</code>. Cada concepto debe encontrarse al menos en uno de los RefSets
     * especificados en el parámetro <code>refsets</code>.
     *
     * @param aCategory                La categoría a la cual pertenecen los objetos buscados.
     * @param refsets                  Los refsets en los cuales debe encontrarse cada concepto (en uno al menos).
     * @param stringBasicTypeAttribute El atributo buleano.
     * @param aString                  El valor buleano.
     * @return Una lista de conceptos con el atributo en el valor deseado.
     */
    public List<ConceptSMTK> findConceptsWithStringBasicType(Category aCategory, ArrayList<RefSet> refsets, RelationshipDefinition stringBasicTypeAttribute, String aString);

    public Integer countModeledConceptsBy(RefSet refSet);

    /**
     * Metodo  que retorna los conceptos en borrador
     *
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
     * Este método es responsable de recuperar el Concepto pendiente.
     *
     * @return El concepto pendiente.
     */
    public ConceptSMTK getPendingConcept();

    /**
     * Este método es responsable de obtener los conceptos que se relacionan con el concepto indicado como parametro
     *
     * @param conceptSMTK concepto que se relaciona con otros
     * @return Lista de conceptos relacionados
     */
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK);

    /**
     * Este método recupera todos los ID's de los conceptos exsitentes.
     *
     * @return Una lista con los ID's de los conceptos.
     */
    public List<Long> getAllConceptsId();

    /**
     * Método encargado de realizar perfect match con patrón de búsqueda y categorías
     * @return
     */
    public List<ConceptSMTK> findPerfectMatchConcept(String pattern, Long[] categories, Long[] refsets, boolean modeled, int pageSize, int pageNumber);


    /**
     * Método encargado de realizar truncate match con patrón de búsqueda
     * @return
     */
    public List<ConceptSMTK> findTruncateMatchConcept(String pattern,  Long[] categories, Long[] refsets, boolean modeled, int pageSize, int pageNumber);

    /**
     * Método encargado de contar los conceptos con perfect match
     * @return
     */
    public int countPerfectMatchConceptBy(String pattern, Long[] categories, boolean modeled);

    /**
     * Método encargado de contar los conceptos con truncate match
     * @return
     */
    public int countTruncateMatchConceptBy(String pattern, Long[] categories, boolean modeled);


}
