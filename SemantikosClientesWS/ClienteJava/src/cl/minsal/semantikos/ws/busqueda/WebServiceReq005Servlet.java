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

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-005")
public class WebServiceReq005Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq005Servlet.class);

    /** La pagina JSP de destino para este servlet */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-005.jsp";


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
    public WebServiceReq005Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq005Servlet:doPost()");

        /* Se recuperan los parámetros */
        String pedible = req.getParameter("requestable");
        String categories = req.getParameter("categories");
        String refsets = req.getParameter("refsets");
        String idStablishment = req.getParameter("idStablishment");

        /* Se realizan las validaciones */
        this.parameterValidator.required(pedible);
        this.parameterValidator.required(idStablishment);

        /* Se prepara la petición del WS */
        PeticionConceptosPedibles peticion = new PeticionConceptosPedibles();
        peticion.setPedible(pedible);
        peticion.setNombreCategoria(Arrays.asList(categories.split(",")));
        peticion.setNombreRefSet(Arrays.asList(refsets.split(",")));
        peticion.setIdEstablecimiento(idStablishment);

        logger.debug("WebServiceReq004Servlet.doPost(): Params: " + Stringer.toString(peticion));

        /* Se invoca el servicio */
        RespuestaBuscarTermino response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().obtenerTerminosPedibles(peticion);
            logger.debug("WebServiceReq005Servlet.doPost(): Response: " + Stringer.toString(response));
        } catch (IllegalInputFault_Exception | SOAPFaultException e) {
            req.setAttribute("exception", e);
            req.getRequestDispatcher(targetPage).forward(req, resp);
            return;
        }
        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", response);
        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq004Servlet:doGet()");
        this.doPost(req ,resp);
    }
}
