package cl.minsal.semantikos.view.daos;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTKWeb;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

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

    public ConceptSMTKWeb augmentConcept(Category category, ConceptSMTKWeb concept);
}
