package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.Institution;
import cl.minsal.semantikos.model.RefSet;

import javax.ejb.Local;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Andrés Farías on 9/20/16.
 */
@Local
public interface RefSetDAO {

    /**
     * Este método es responsable de persistir un RefSet. Si el método se ejecuta correctamente la entidad es
     * actualizada con un nuevo identificador.
     *
     * @param refSet El RefSet que se desea persistir.
     */
    public void persist(RefSet refSet);

    /**
     * Este método es responsable de actualizar un RefSet ya existente.
     *
     * @param refSet El RefSet qeu se desea actualizar.
     */
    public void update(RefSet refSet);

    /**
     * Este método es responsable de persistir la asociación de una descripción a un RefSet.
     *
     * @param conceptSMTK El concepto que se desea asociar al refset.
     * @param refSet      El Refset al cual se asocia la descripción.
     */
    public void bind(ConceptSMTK conceptSMTK, RefSet refSet);

    /**
     * Este método es responsable de persistir la des-asociación de una descripción a un RefSet.
     *
     * @param conceptSMTK El concepto que se desea des-asociar al refset.
     * @param refSet      El Refset al cual se asocia la descripción.
     */
    public void unbind(ConceptSMTK conceptSMTK, RefSet refSet);

    /**
     * Este metodo es el responsable de obtener todos los refsets
     * @return Lista de refsets
     */
    public List<RefSet> getReftsets();

    /**
     * Este método es el encargado de obtener los refset donde se encuentra un concepto
     * @param conceptSMTK
     * @return lista de refset relacionados con el concepto
     */

    public List<RefSet> getRefsetsBy(ConceptSMTK conceptSMTK);

    public List<RefSet> getRefsetBy(Institution institution);

    /**
     * Este metodo obtiene un refset por ID
     * @param id
     * @return
     */
    public RefSet getRefsetBy(long id);
}
