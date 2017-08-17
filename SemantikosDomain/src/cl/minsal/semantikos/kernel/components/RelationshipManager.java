package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.User;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Andres Farias
 * @version 1.0
 * @created 17-ago-2016 12:23:53
 */
public interface RelationshipManager {

    /**
     * Este método es responsable de crear persistentemente una Definición de Relación para Categorías.
     *
     * @return La definición de relación bien persistida (con us ID).
     */
    public RelationshipDefinition createRelationshipDefinition(RelationshipDefinition relationshipDefinition);

    /**
     * Este metodo es responsable de persistir
     *
     * @return
     */
    public Relationship createRelationship(Relationship relationship);

    /**
     * Este método es responsable de asociar (agregar) una relación a un concepto.
     *
     * @param concept      El concepto al cual se agrega la descripción.
     * @param relationship El tipo de la descripción.
     * @param user         El usuario que agrega el término
     *
     * @return La descripción creada a partir del término dado.
     */
    public Relationship bindRelationshipToConcept(ConceptSMTK concept, Relationship relationship, User user);

    /**
     * Este método es responsable de eliminar lógicamente una relación, dejándola no vigente, no desasociándola del
     * concepto.
     *
     * @param relationship La relación que se desea eliminar.
     * @param user         El usuario que realiza la eliminación de la relación.
     *
     * @return La relación eliminada, con sus campos de vigencia actualizados.
     */
    public Relationship removeRelationship(ConceptSMTK conceptSMTK, Relationship relationship, User user);

    /**
     * Este método es responsable de recuperar todas las relaciones que tienen como
     * origen un concepto dado.
     *
     * @param id El identificador único (BDD) del concepto SMTK.
     */
    public Relationship[] findRelationsByOriginConcept(long id);

    /**
     * Este método es responsable de recuperar todas las relaciones que tienen como
     * destino un concepto Snomed CT dado.
     *
     * @param conceptSCT El identificador único (BDD) del concepto CST.
     */
    public List<Relationship> findRelationsByTargetSCTConcept(ConceptSCT conceptSCT);

    /**
     * Este método es responsable de retornar la lista de todos los conceptos relacionados con el concepto
     * <code>conceptSMTK</code> desde este objeto, y que pertenecen a la categoría <code>category</code> dada como
     * parámetro.
     *
     * @param conceptSMTK El concepto origen.
     * @param category    La categoría a la que pertenecen los conceptos destino.
     *
     * @return Una lista de todos los conceptos destino que pertenecen a la categoría <code>category</code>.
     */
    public List<ConceptSMTK> getTargetConceptsByCategory(ConceptSMTK conceptSMTK, Category category);

    /**
     * Este método es responsable de actualizar el valor de una relación, actualizando
     * su destino. El destino debe ser del mismo tipo que el valor original.
     *
     * @param idRelationship          Identificador de la relación cuyo valor de destino va
     *                                a cambiar.
     * @param idRelationshipAttribute El atributo (relación) de la relación que se
     *                                desea modificar.
     */
    public int updateRelationAttribute(int idRelationship, int idRelationshipAttribute);

    /**
     * Este método es responsable de actualizar una relación de un concepto.
     *
     * @param conceptSMTK          El concepto cuya relación se desea actualizar.
     * @param originalRelationship La relación original, antes de la actualización.
     * @param editedRelationship   La relación actualizada.
     * @param user                 El usuario que realiza la operación.
     */
    void updateRelationship(@NotNull ConceptSMTK conceptSMTK, @NotNull Relationship originalRelationship, @NotNull Relationship editedRelationship, @NotNull User user);

    /**
     * Este método es responsable de dejar no vigente la relación a partir de este momento.
     *
     * @param relationship Relación que se invalida.
     */
    public Relationship invalidate(Relationship relationship);

    /**
     * Este método es responsable de recuperar todas las relaciones de un cierto tipo (es un/es una, por ejemplo) y que
     * referencian un cierto destino <code>target</code>.
     *
     * @param relationshipDefinition El tipo de relación que se desea recuperar.
     * @param target                 El destino al cual deben apuntar las relaciones deseadas.
     *
     * @return Una lista de relaciones del tipo indicado que apuntan al <code>target</code> indicado.
     */
    public List<Relationship> getRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target);

    /**
     * Este método es responsable de recuperar todas las relaciones de un cierto tipo y que
     * referencian un cierto destino <code>target</code>.
     *
     * @param relationshipDefinition El tipo de relación que se desea recuperar.
     * @param target                 El destino al cual deben apuntar las relaciones deseadas.
     *
     * @return Una lista de relaciones del tipo indicado que apuntan al <code>target</code> indicado.
     */
    public List<Relationship> findRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target);

    /**
     * Este método es responsable de recuperar todas las relaciones de un cierto tipo y que
     * referencian un cierto destino <code>target</code>.
     *
     * @param relationship            La relacion objeto de la comparación
     *
     * @return Una lista de relaciones del tipo indicado que apuntan al <code>target</code> indicado.
     */
    public List<Relationship> findRelationshipsLike(Relationship relationship);

    /**
     * Este método es responsable de recuperar todas las relaciones del <code>concepto</code>.
     *
     * @param concept El concepto cuyas relaciones son recuperadas.
     *
     * @return Una lista de relaciones asociadas al concepto.
     */
    List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK concept);

    /**
     * Este método es responsable de recuperar las relaciones donde el concepto de origen coincide con el
     * <code>idConcept</code> dado como argumento.
     *
     * @param conceptSMTK El id del concepto cuyas relaciones se quiere recuperar.
     *
     * @return Una lista con las relaciones del concepto.
     */
    List<Relationship> getRelationshipsBySourceConceptAndTargetType(ConceptSMTK conceptSMTK, TargetType targetType);

    /**
     * Este método es responsable de recuperar todas las relaciones del <code>concepto</code>.
     *
     * @param concept El concepto cuyas relaciones son recuperadas.
     *
     * @return Una lista de relaciones asociadas al concepto.
     */
    Map<Long, ArrayList<Relationship>> getRelationshipsBySourceConcepts(List<ConceptSMTK> concept);

    public RelationshipDefinitionFactory getRelationshipDefinitionFactory();

    public List<ConceptSMTK> loadRelationships(List<ConceptSMTK> conceptSMTKs) throws Exception;
}