package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.PendingTerm;
import cl.minsal.semantikos.model.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías on 11/22/16.
 */
@Local
public interface PendingTermsManager {

    /**
     * Este método es responsable de agregar un término pendiente al sistema.
     *
     * @param pendingTerm El término que se desea agregar.
     * @param loggedUser  El usuario conectado que realiza la operación.
     *
     * @return La descripción creada a partir del término pendiente.
     */
    public Description addPendingTerm(PendingTerm pendingTerm, User loggedUser);

    /**
     * Este método es responsable de recuperar todos los términos pendientes, que tienen asociada una descripción en el
     * concepto especial "Pendientes".
     *
     * @return Una lista de Términos Pendientes.
     */
    public List<PendingTerm> getAllPendingTerms();
}
