package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.dtos.ConceptDTO;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by des01c7 on 03-11-17.
 */
@Local
public interface ConceptWSDAO {

    public ConceptSMTK getConceptByID(long id);

    public List<ConceptSMTK> getConceptsPaginated(Long categoryId, int pageSize, int pageNumber, boolean modeled);

    public ConceptSMTK createConceptFromDTO(ConceptDTO conceptDTO);

    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK);
}
