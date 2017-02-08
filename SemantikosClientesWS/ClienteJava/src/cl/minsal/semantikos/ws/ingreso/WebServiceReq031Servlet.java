package cl.minsal.semantikos.ws.ingreso;

import cl.minsal.semantikos.ws.shared.IllegalInputFault_Exception;
import cl.minsal.semantikos.ws.shared.PeticionCodificacionDeNuevoTermino;
import cl.minsal.semantikos.ws.shared.RespuestaCodificacionDeNuevoTermino;
import cl.minsal.semantikos.ws.shared.ServicioDeIngreso_Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-031")
public class WebServiceReq031Servlet extends HttpServlet {

    /** El proxy al servicio web */
    private ServicioDeIngreso_Service servicioDeIngreso_service;

    /**
     * El constructror inicializa el proxy hacia el servicio.
     */
    public WebServiceReq031Servlet() {
        this.servicioDeIngreso_service = new ServicioDeIngreso_Service();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq031Servlet:doPost()");

        PeticionCodificacionDeNuevoTermino peticionNuevoTermino = new PeticionCodificacionDeNuevoTermino();
        peticionNuevoTermino.setTermino(req.getParameter("term"));
        peticionNuevoTermino.setCategoria(req.getParameter("category"));
        peticionNuevoTermino.setProfesional(req.getParameter("profesional"));
        peticionNuevoTermino.setProfesion(req.getParameter("profession"));
        peticionNuevoTermino.setEmail(req.getParameter("email"));
        peticionNuevoTermino.setEspecialidad(req.getParameter("specialty"));
        peticionNuevoTermino.setSubEspecialidad(req.getParameter("subSpecialty"));
        peticionNuevoTermino.setEstablecimiento(req.getParameter("establishment"));
        peticionNuevoTermino.setEsSensibleAMayusculas(req.getParameter("sensitiveCase") == "true");
        peticionNuevoTermino.setObservacion(req.getParameter("observation"));
        peticionNuevoTermino.setTipoDescripcion(req.getParameter("descriptionType"));

        /* TODO: Eliminar el idConcept */
        peticionNuevoTermino.setIdConcepto("1");
        System.out.println("Peticion: " + peticionNuevoTermino);

        RespuestaCodificacionDeNuevoTermino respuestaCodificacionDeNuevoTermino;
        try {
            respuestaCodificacionDeNuevoTermino = servicioDeIngreso_service.getServicioDeIngresoPort().codificacionDeNuevoTermino(peticionNuevoTermino);
            System.out.println("WebServiceReq031Servlet:doPost(): " + respuestaCodificacionDeNuevoTermino.toString());
            req.setAttribute("serviceResponse", respuestaCodificacionDeNuevoTermino);
        } catch (IllegalInputFault_Exception e) {
            req.setAttribute("exception", e);
        } catch (SOAPFaultException soapEx){
            req.setAttribute("exception", soapEx);
        }


        req.getRequestDispatcher("/update_service/ServicioIngresoWS-REQ-003.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq030Servlet:doGet()");
    }
}
