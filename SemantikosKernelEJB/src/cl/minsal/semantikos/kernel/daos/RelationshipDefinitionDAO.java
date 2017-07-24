package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías / Gustavo Punucura.
 */
@Local
public interface RelationshipDefinitionDAO {

    /**
     * Este método es responsable de recuperar los atributos (RelationshipDefinitions) de una Categoría.
     *
     * @param idCategory Identificador de la categoría.
     *
     * @return Una lista con los atributos de la categoría.
     */
    public List<RelationshipDefinition> getRelationshipDefinitionsByCategory(long idCategory);

    List<RelationshipAttributeDefinition> getRelationshipAttributeDefinitionsByRelationshipDefinition(RelationshipDefinition relationshipDefinition);
}
