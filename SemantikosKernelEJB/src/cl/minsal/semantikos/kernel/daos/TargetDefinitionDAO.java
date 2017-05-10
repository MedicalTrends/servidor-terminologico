package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.relationships.TargetDefinition;

import javax.ejb.Local;

/**
 * @author Andrés Farías / Gustavo Punucura
 */
@Local
public interface TargetDefinitionDAO {


    public TargetDefinition getTargetDefinitionById(long idTargetDefinition);
}
