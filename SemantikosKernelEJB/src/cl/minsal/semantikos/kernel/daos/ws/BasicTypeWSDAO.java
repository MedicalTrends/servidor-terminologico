package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.dtos.BasicTypeValueDTO;

import javax.ejb.Local;

/**
 * @author Andrés Farías.
 */
@Local
public interface BasicTypeWSDAO {

    public BasicTypeValue createBasicTypeFromDTO(BasicTypeValueDTO basicTypeValueDTO, BasicTypeDefinition basicTypeDefinition);

}
