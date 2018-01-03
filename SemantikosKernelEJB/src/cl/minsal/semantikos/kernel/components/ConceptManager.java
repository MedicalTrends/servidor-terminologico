package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
@Remote
public interface ConceptManager {

    /**
     * Este método es responsable de persistir un concepto que no se encuentra persistido. Esta acción, de
     * persistencia, queda registrado como una actividad de auditoría.
     *
     * @param conceptSMTK El concepto a persistir.
     * @param user        El usuario que persiste el concepto.
     */
    public long persist(@NotNull ConceptSMTK conceptSMTK, User user) throws Exception;

    /**
     * Este método es responsable de actualizar los campos (para no decir atributos que es un caso particular de las
     * relaciones).
     *
     * @param originalConcept El concepto original.
     * @param updatedConcept  El concepto actualizado con los cambios.
     * @param user            El usuario que realiza los cambios.
     */
    public void updateFields(@NotNull ConceptSMTK originalConcept, @NotNull ConceptSMTK updatedConcept, User user);

    /**
     * Este método es responsable de actualizar los campos (para no decir atributos que es un caso particular de las
     * relaciones).
     *
     * @param originalConcept El concepto original.
     * @param updatedConcept  El concepto actualizado con los cambios.
     * @param user            El usuario que realiza los cambios.
     */
    public void update(@NotNull ConceptSMTK originalConcept, @NotNull ConceptSMTK updatedConcept, User user) throws Exception;

    /**
     * Este método es responsable de cambiar el estado de publicación del concepto.
     *
     * @param conceptSMTK El concepto cuyo estado de publicación ha cambiado.
     * @param user        El usuario que realizó el cambio.
     */
    public void publish(@NotNull ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de eliminar un concepto, de acuerdo a las reglas de negocio.
     *
     * @param conceptSMTK El concepto que se desea dejar no vigente.
     * @param user        El usuario que realiza la operación.
     */
    public void delete(@NotNull ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de dejar no vigente (eliminar en la jerga del análisis!).
     *
     * @param conceptSMTK El concepto que se desea dejar no vigente.
     * @param user        El usuario que realiza la operación.
     */
    public void invalidate(@NotNull ConceptSMTK conceptSMTK, @NotNull User user);

    /**
     * Este método es responsable de cambiar el concepto de una categoría a otra.
     *
     * @param conceptSMTK    El concepto cuya categoría se desea cambiar.
     * @param targetCategory La categoría destino del concepto.
     * @param user           El usuario que realiza la operación.
     */
    public void changeCategory(@NotNull ConceptSMTK conceptSMTK, @NotNull Category targetCategory, User user);

    /**
     * Este método es responsable de agregar una relación a un concepto.
     *
     * @param conceptSMTK  El concepto al cual se agrega la relación.
     * @param relationship La relación que se agrega.
     * @param user         El usuario que realiza la operación.
     */
    public void bindRelationshipToConcept(@NotNull ConceptSMTK conceptSMTK, @NotNull Relationship relationship, @NotNull User user) throws Exception;

    /**
     * Este método es responsable de cambiar el concepto de una categoría a otra.
     *
     * @param conceptSMTK El concepto cuyo tag semántikos es actualizado.
     * @param tagSMTK     El Tag Semántikos que tenía el concepto antes de ser modificado.
     * @param user        El usuario que realiza la operación.
     */
    public void changeTagSMTK(@NotNull ConceptSMTK conceptSMTK, @NotNull TagSMTK tagSMTK, User user);

    /**
     * Este método es responsable de recuperar el concepto con DESCRIPTION_ID.
     *
     * @param conceptId El DESCRIPTION_ID (valor de negocio) del concepto que se desea recuperar.
     * @return Un objeto fresco de tipo <code>ConceptSMTK</code> con el Concepto solicitado.
     */
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptId);

    /**
     * Este método es responsable de recuperar el concepto con id (de BD)
     *
     * @param id El identificador de BD del concepto.
     * @return Un concepto fresco de tipo <code>ConceptSMTK</code>.
     */
    public ConceptSMTK getConceptByID(long id);

    public List<ConceptSMTK> findConcepts(String pattern, List<Category> categories, List<RefSet> refsets, Boolean modeled);

    public int countConcepts(String pattern, List<Category> categories, List<RefSet> refsets, Boolean modeled);

    public List<ConceptSMTK> findConceptsPaginated(Category category, int pageSize, int pageNumber, Boolean modeled);

    public List<ConceptSMTK> findConceptsWithTarget(Relationship relationship);

    /**
     * Método encargado de generar el concept ID
     *
     * @return retorna un String con el Concept ID generado
     */
    public String generateConceptId(long id);

    /**
     * Este método es responsable de recuperar todas las descripciones (vigentes) del concepto.
     *
     * @param concept El concepto cuyas descripciones se quieren recuperar.
     * @return Una lista de Description vigentes asociadas al <code>concept</code>
     */
    public List<Description> getDescriptionsBy(ConceptSMTK concept);

    /**
     * Este método es responsable de recuperar el concepto que posee una descripción con el <em>DESCRIPTION_ID</em>
     * dado
     * como parámetro (<code>descriptionId</code>).
     *
     * @param descriptionId El <em>DESCRIPTION_ID</em> de la descripción cuyo concepto contenedor se desea recuperar.
     * @return El concepto que contiene la descripción cuyo <em>DESCRIPTION_ID</em> corresponde con el parámetro.
     */
    public ConceptSMTK getConceptByDescriptionID(String descriptionId);

    /**
     * Este método es responsable de cargar las relaciones del concepto.
     *
     * @param concept El concepto cuyas relaciones son actualizadas.
     * @return La lista de relaciones actualizadas (que ya están asociadas al objeto <code>concepto</code>.
     */
    public List<Relationship> loadRelationships(ConceptSMTK concept) throws Exception;

    /**
     * Este método es responsable de obtener las relaciones del concepto.
     *
     * @param concept El concepto cuyas relaciones son obtenidas.
     * @return La lista de relaciones obtenidas.
     */
    public List<Relationship> getRelationships(ConceptSMTK concept);

    /**
     * Este método es responsable de obtener los conceptos que se relacionan con el concepto <code>conceptSMTK</code> a
     * través de relaciones, donde <code>conceptSMTK</code> es el concepto de origen y los conceptos relacionados con
     * esto son el destino en la relación.
     *
     * @param conceptSMTK El concepto origen cuyos objetos relacionados se piden.
     * @return Lista de conceptos relacionados (concepto --> relacionados)
     */
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK);

    /**
     * Este método es una extensión del método <code>getRelatedConcepts</code>, filtrando los conceptos relacionados a
     * las categorías especificadas.
     *
     * @param conceptSMTK El concepto cuyos conceptos relacionados se desea recuperar.
     * @param categories  Las categorías a las cuales los conceptos relacionados deben pertenecer.
     * @return La lista de conceptos relacionados que pertenecen a alguna de las categorías en <code>categories</code>.
     */
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK, Category... categories);

    /**
     * Este método es responsable de retornar la instancia del concepto no valido.
     *
     * @return La instancia (única) del concepto No Válido.
     */
    public ConceptSMTK getNoValidConcept();

    /**
     * Este método es responsable de trasladar un objeto de su categoría actual a otra categoría.
     *
     * @param conceptSMTK El concepto que se desea trasladar.
     * @param category    La categoría a la cual se desea trasladar el concepto.
     * @return El concepto con su categoría actualizada. Esto es necesario para cuando las llamadas al EJB sean remotas.
     */
    public ConceptSMTK transferConcept(ConceptSMTK conceptSMTK, Category category, User user) throws Exception;


    /**
     * Este método es responsable de retornar la instancia del concepto pendiente.
     *
     * @return La instancia (única) del concepto pendiente.
     */
    public ConceptSMTK getPendingConcept();

    /**
     * Método encargado de hacer Perfect Match según los parámetros ingresados
     * @return  Lista de conceptos que concuerdan con los parámetros de búsqueda
     */
    public List<ConceptSMTK> perfectMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean isModeled);

    /**
     * Método encargado de hacer Truncate Match según los parámetros ingresados
     * @return  Lista de conceptos que concuerdan con los parámetros de búsqueda
     */
    public List<ConceptSMTK> truncateMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean isModeled);

    /**
     * Este método se encarga de entregar la cantidad de conceptos por Perfect Match según patron, categoría y si esta modelado o no.
     * @return cantidad de conceptos según los parámetros ingresados
     */
    public int countPerfectMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean isModeled);
    /**
     * Este método se encarga de entregar la cantidad de conceptos por Truncate Match según patron, categoría y si esta modelado o no.
     * @return cantidad de conceptos según los parámetros ingresados
     */
    public int countTruncateMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean isModeled);

}