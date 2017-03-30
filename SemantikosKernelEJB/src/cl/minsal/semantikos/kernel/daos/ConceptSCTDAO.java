package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.Local;

/**
 * @author Andrés Farías
 */
@Local
public interface ConceptSCTDAO {

    /**
     * Este método es responsable de recuperar un concepto CST a partir de un id <code>idConceptCST</code>.
     *
     * @param idConceptCST El identificador único de la base de datos.
     *
     * @return Una instancia fresca creada a partir de la entidad en la base de datos.
     */
    ConceptSCT getConceptCSTByID(long idConceptCST);

    /**
     * Este método es responsable persistir la entidad Concepto SMTK en la base de datos.
     *
     * @param conceptSCT El concepto que será persistido.
     * @param user       El usuario que está realizando la operación
     */
    public void persist(ConceptSCT conceptSCT, User user);
}
