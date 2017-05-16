package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Andrés Farías on 11/22/16.
 */
@Remote
public interface PendingTermsManager {

    /**
     * Este método es responsable de agregar un término pendiente al sistema.
     *  @param pendingTerm El término que se desea agregar.
     * @param loggedUser  El usuario conectado que realiza la operación.
     */
    public Description addPendingTerm(PendingTerm pendingTerm, User loggedUser);

    /**
     * Este método es responsable de recuperar todos los términos pendientes, que tienen asociada una descripción en el
     * concepto especial "Pendientes".
     *
     * @return Una lista de Términos Pendientes.
     */
    public List<PendingTerm> getAllPendingTerms();


    /**
     * Este método es responsable de recuperar un término pendiente dado su id en la BD
     *
     * @return un <code>PendingTerm</code>
     */
    public PendingTerm getPendingTermById(long id);
}
