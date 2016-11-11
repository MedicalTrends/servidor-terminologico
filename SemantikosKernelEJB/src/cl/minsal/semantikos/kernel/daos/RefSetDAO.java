package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
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
     * @param pattern Patron de nombre del REFSET buscado
     * @return Lista de REFSETs con nombre LIKE el patron ingresado (se espera lista con un solo elemento)
     */
    public List<RefSet> findRefsetsByName(String pattern);

    /**
     * RefSets a los que pertenece un concepto
     * @param
     * @return
     */
    public List<RefSet> findByConcept(ConceptSMTK conceptSMTK);
}
