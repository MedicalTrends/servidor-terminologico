package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;

import javax.ejb.Local;

/**
 * @author Andrés Farías on 10/5/16.
 */
@Local
public interface SemantikosWebDAO {

    /**
     * Este método es responsable de recuperar la información del objeto composite asociado a un
     * RelationshipDefinition.
     *
     * @param relationshipDefinition El RelationshipDefinition que requiere ser completado.
     * @param category               La categoría para la cual se ordenan las relaciones.
     *
     * @return El Identificador del Composite asociado a dicho RelationshipDefinition.
     */
    public ExtendedRelationshipDefinitionInfo getCompositeOf(Category category, RelationshipDefinition relationshipDefinition);


    /**
     * Este método es responsable de recuperar la información del objeto composite asociado a un
     * RelationshipDefinition.
     *
     * @param relationshipAttributeDefinition El RelationshipDefinition que requiere ser completado.
     * @param category               La categoría para la cual se ordenan las relaciones.
     *
     * @return El Identificador del Composite asociado a dicho RelationshipDefinition.
     */
    public ExtendedRelationshipAttributeDefinitionInfo getCompositeOf(Category category, RelationshipAttributeDefinition relationshipAttributeDefinition);


    public ConceptSMTKWeb augmentConcept(ConceptSMTKWeb concept);
}
