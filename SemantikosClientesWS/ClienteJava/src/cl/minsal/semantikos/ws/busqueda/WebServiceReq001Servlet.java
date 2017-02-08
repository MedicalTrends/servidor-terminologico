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
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-001")
public class WebServiceReq001Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq001Servlet.class);

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
    public WebServiceReq001Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq001Servlet:doPost()");

        /* Se recuperan los parámetros */
        String term = req.getParameter("term");
        String categories = req.getParameter("categories");
        String refsets = req.getParameter("refsets");

        /* Se realizan las validaciones */
        this.parameterValidator.required(term);

        /* Se prepara la petición del WS */
        PeticionBuscarTerminoSimple peticion = new PeticionBuscarTerminoSimple();
        peticion.setTermino(term);
        peticion.setNombreCategoria(Arrays.asList(categories.split(",")));
        peticion.setNombreRefSet(Arrays.asList(refsets.split(",")));

        /* Se invoca el servicio */
        RespuestaBuscarTerminoGenerica response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().buscarTermino(peticion);
        } catch (IllegalInputFault_Exception | NotFoundFault_Exception e) {
            logger.error("WebServiceReq001Servlet invocation error", e);
            req.setAttribute("exception", e);
            req.getRequestDispatcher("/search_service/ServicioBusquedaWS-REQ-001.jsp").forward(req, resp);
            return;
        }

        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", response);
        req.getRequestDispatcher("/search_service/ServicioBusquedaWS-REQ-001.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq030Servlet:doGet()");
        this.doPost(req ,resp);
    }
}
