package cl.minsal.semantikos.ws.ingreso.clients;

import cl.minsal.semantikos.ws.shared.IllegalInputFault_Exception;
import cl.minsal.semantikos.ws.shared.PeticionCodificacionDeNuevoTermino;
import cl.minsal.semantikos.ws.shared.ServicioDeIngreso;
import cl.minsal.semantikos.ws.shared.ServicioDeIngreso_Service;

/**
 * Esta clase tiene como objetivo el de invocar el cliente de WS de Ingreso
 * .
 *
 * @author Andrés Farías on 03-01-17.
 */
public class ServicioDeIngresoNewTermClient {

    public static void main(String[] args) {
        System.out.println("Este es un test para consumir el WS.");

        PeticionCodificacionDeNuevoTermino peticionNuevoTermino = new PeticionCodificacionDeNuevoTermino();
        peticionNuevoTermino.setTermino("brazo");
        peticionNuevoTermino.setCategoria("estructura corporal");
        peticionNuevoTermino.setProfesional("andres farias");
        peticionNuevoTermino.setProfesion("informatico");
        peticionNuevoTermino.setEmail("andres.farias@gmail.com");
        peticionNuevoTermino.setEspecialidad("OOP");
        peticionNuevoTermino.setSubEspecialidad("AOP");
        peticionNuevoTermino.setEstablecimiento("1");
        peticionNuevoTermino.setEsSensibleAMayusculas(true);
        peticionNuevoTermino.setObservacion("Nada");
        peticionNuevoTermino.setTipoDescripcion("Sinonimo");

        ServicioDeIngreso_Service servicioDeIngreso_service = new ServicioDeIngreso_Service();
        ServicioDeIngreso servicioDeIngresoPort = servicioDeIngreso_service.getServicioDeIngresoPort();
        try {
            servicioDeIngresoPort.codificacionDeNuevoTermino(peticionNuevoTermino);
        } catch (IllegalInputFault_Exception e) {
            e.printStackTrace();
        }
    }
}
