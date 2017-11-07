package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.dtos.ConceptDTO;
import cl.minsal.semantikos.model.dtos.ConceptSCTDTO;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.Local;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by des01c7 on 03-11-17.
 */
@Local
public interface ConceptSCTWSDAO {

    public ConceptSCT createConceptSCTFromDTO(ConceptSCTDTO conceptDTO) throws SQLException;
}
