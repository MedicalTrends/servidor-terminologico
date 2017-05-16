package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.PendingTerm;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

/**
 * @author Andrés Farías on 11/22/16.
 */
@Remote
public interface PendingTermDAO {

    /**
     * Este método es responsable de persistir el termino pendiente en la base de datos.
     *
     * @param pendingTerm El término pendiente que se desea persistir.
     */
    public void persist(PendingTerm pendingTerm);

    /**
     * Este método es responsable de asociar el término pendiente a una descripción.
     *
     * @param pendingTerm El término pendiente.
     * @param description La descripción que se va a asociar.
     */
    public void bindTerm2Description(PendingTerm pendingTerm, Description description);

    public List<PendingTerm> getAllPendingTerms();

    public PendingTerm getPendingTermById(long id);

    void updateSearchIndexes(PendingTerm pendingTerm);
}
