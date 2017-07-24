package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Gustavo Punucura
 */
@Local
public interface RelationshipAttributeDAO {


    /**
     * Metodo encargado de persistir los relationship attribute
     * @param relationshipAttribute relationship attribute para persistir
     * @return retorna un relationship attribute
     */
    public RelationshipAttribute createRelationshipAttribute(RelationshipAttribute relationshipAttribute);

    /**
     * Este método es responsable de actualizar los atributos de un atributo de relación.
     * @param relationshipAttribute El atributo que se desea actualizar.
     */
    public void update(RelationshipAttribute relationshipAttribute);

    /**
     * Metodo encargado de obtener un relationship attribute por id
     * @param relationship id del relationship
     * @return retorna un relationship attribute
     */

    public List<RelationshipAttribute> getRelationshipAttribute(Relationship relationship);

    public Long getTargetByRelationshipAttribute(RelationshipAttribute relationshipAttribute);
}
