package cl.minsal.semantikos.ws.busqueda.clients;

import cl.minsal.semantikos.ws.shared.*;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author Andrés Farías on 16-01-17.
 */
public class IndirectCrossmapsSearcher {

    public static void main(String[] args) throws NotFoundFault_Exception {

        /* Se prepara la peticion */
        ServicioDeBusqueda servicioDeBusqueda = new ServicioDeBusqueda();
        RespuestaCategorias response = servicioDeBusqueda.getSearchServicePort().listaCategorias();

        if (response== null || response.getCategorias() == null) {
            System.out.println("El objeto categorias es  nulo.");
        } else {
            System.out.println(response.getCategorias().getCategoria().size() + " categorías recuperadas");
        }

        System.out.println(Stringer.toString(response));

    }
}
