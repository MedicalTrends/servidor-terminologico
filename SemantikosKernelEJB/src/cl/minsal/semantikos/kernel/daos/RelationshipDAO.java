package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.Local;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
@Local
public interface RelationshipDAO {

    /**
     * Este método es responsable de persistir una relación asociada a un concepto.
     *
     * @param relationship La relación que se desea persistir.
     */
    public Relationship persist(Relationship relationship);

    /**
     * Este método es responsable de persistir una definición de una relación asociada a una categoría.
     *
     * @param relationshipDefinition La Definición de relación que se desea persistir.
     */
    public RelationshipDefinition persist(RelationshipDefinition relationshipDefinition);

    /**
     * Este método es responsable de eliminar una relación.
     *
     * @param relationship La relación que se desea eliminar.
     */
    public void delete(Relationship relationship);

    /**
     * Este método es responsable de actualizar los atributos de una relación.
     * @param relationship La relación que se desea actualizar.
     */
    public void update(Relationship relationship);

    /**
     * Este método es responsable de dejar como no vigente una relación asociada a un concepto.
     *
     * @param relationship La relación que se desea actualizar.
     */
    public void invalidate(Relationship relationship);

    /**
     * Este método es responsable de obtener todas las relaciones que tienen como target un concepto SNOMED CT en
     * particular.
     *
     * @param conceptSCT El concepto SNOMED al cual apuntan las relaciones que se desea buscar.
     *
     * @return Todas las relaciones que tienen como destino el concepto SCT de identificador <code>id</code>
     */
    List<Relationship> getRelationshipsToCSTConcept(ConceptSCT conceptSCT);

    /**
     * Este método es responsable de obtener la relación con id <code>idRelationship</code> desde la BD.
     *
     * @param idRelationship El Identificador único en la base de datos.
     *
     * @return Un relación fresca creada desde la base de datos.
     */
    Relationship getRelationshipByID(long idRelationship);

    public List<Relationship> getRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target);

    public List<Relationship> findRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target);

    /**
     * Este método es responsable de obtener todas las relaciones que tienen como definicion un relationshipDefinition
     * en
     * particular.
     *
     * @param relationshipDefinition La definición de relacion a la cual apuntan las relaciones que se desea buscar.
     *
     * @return Todas las relaciones que tienen como  definicion un relationshipDefinition
     * <code>relationshipDefinition</code>
     */
    public List<Relationship> getRelationshipsByRelationshipDefinition(RelationshipDefinition relationshipDefinition);

    /**
     * Este método es responsable de recuperar las relaciones donde el concepto de origen coincide con el
     * <code>idConcept</code> dado como argumento.
     *
     * @param idConcept El id del concepto cuyas relaciones se quiere recuperar.
     *
     * @return Una lista con las relaciones del concepto.
     */
    List<Relationship> getRelationshipsBySourceConcept(long idConcept);

    /**
     * Este método es responsable de recuperar las relaciones donde el concepto de origen coincide con el
     * <code>idConcept</code> dado como argumento.
     *
     * @param idsConcept El id del concepto cuyas relaciones se quiere recuperar.
     *
     * @return Una lista con las relaciones del concepto.
     */
    Map<Long, ArrayList<Relationship>> getRelationshipsBySourceConcepts(List<Long> idsConcept);


    public Long getTargetByRelationship(Relationship relationship);

}
