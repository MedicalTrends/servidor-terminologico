package cl.minsal.semantikos.ws.busqueda;

import cl.minsal.semantikos.ws.ParameterValidator;
import cl.minsal.semantikos.ws.shared.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-007")
public class WebServiceReq007Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq007Servlet.class);

    /** La pagina JSP de destino para este servlet */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-007.jsp";


    /**
     * El proxy al servicio web
     */
    private ServicioDeBusqueda servicioDeBusqueda;
    /**
     * Validador de parámetros
     */
    private ParameterValidator parameterValidator;

    /**
     * El constructror inicializa el proxy hacia el servicio.
     */
    public WebServiceReq007Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq005Servlet:doPost()");

        /* Se recuperan los parámetros */
        String description_id = req.getParameter("description_ID");
        String includeStablishments = req.getParameter("includeStablishments");
        String idStablishment = req.getParameter("idStablishment");

        /* Se realizan las validaciones */
        this.parameterValidator.required(description_id);
        this.parameterValidator.required(includeStablishments);
        this.parameterValidator.required(idStablishment);

        /* Se prepara la petición del WS */
        PeticionRefSetsPorIdDescripcion peticion = new PeticionRefSetsPorIdDescripcion();
        peticion.setIdDescripcion(description_id);
        peticion.setIncluyeEstablecimientos(Boolean.valueOf(includeStablishments));
        peticion.setIdStablishment(idStablishment);

        logger.debug("WebServiceReq007Servlet.doPost(): Params: " + Stringer.toString(peticion));

        /* Se invoca el servicio */
        RespuestaRefSets response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().refSetsPorIdDescripcion(peticion);
            logger.debug("WebServiceReq007Servlet.doPost(): Response: " + Stringer.toString(response));
        } catch (IllegalInputFault_Exception | SOAPFaultException e) {
            req.setAttribute("exception", e);
            req.getRequestDispatcher(targetPage).forward(req, resp);
            return;
        } catch (NotFoundFault_Exception e) {
            e.printStackTrace();
            return;
        }
        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", response);
        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq007Servlet:doGet()");
        this.doPost(req ,resp);
    }
}
