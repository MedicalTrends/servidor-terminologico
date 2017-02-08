package cl.minsal.semantikos.ws.busqueda.clients;

import cl.minsal.semantikos.ws.shared.*;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;

/**
 * Esta clase tiene como objetivo el de invocar el cliente de WS de Ingreso
 * .
 *
 * @author Andrés Farías on 03-01-17.
 */
public class ServicioDeBusquedaClientWS001 {

    public static void main(String[] args) throws IllegalInputFault_Exception, NotFoundFault_Exception {
        System.out.println("Cliente para consumo de WS-REQ-001: buscando 'Pierna'");

        /* Se prepara la peticion */
        PeticionBuscarTerminoSimple peticionBuscarTermino = new PeticionBuscarTerminoSimple();
        peticionBuscarTermino.setTermino("Pierna");
        //peticionBuscarTermino.setNombreCategoria(singletonList("Estructura Corporal"));

        ServicioDeBusqueda servicioDeBusqueda = new ServicioDeBusqueda();
        RespuestaBuscarTerminoGenerica respuestaBuscarTerminoGenerica = servicioDeBusqueda.getSearchServicePort().buscarTermino(peticionBuscarTermino);

        System.out.println("Cliente para consumo de WS-REQ-001, respuesta: "  + Stringer.toString(respuestaBuscarTerminoGenerica));
    }
}
