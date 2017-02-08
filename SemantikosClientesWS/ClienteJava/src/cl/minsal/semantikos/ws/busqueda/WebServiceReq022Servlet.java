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

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-022")
public class WebServiceReq022Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq022Servlet.class);

    /**
     * La pagina JSP de destino para este servlet
     */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-022.jsp";

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
    public WebServiceReq022Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq022Servlet:doPost()");

        /* Se recuperan los parámetros */
        String refSetName = req.getParameter("refSetName");
        String idStablishment = req.getParameter("idStablishment");

        /* Se realizan las validaciones */
        this.parameterValidator.required(refSetName);
        this.parameterValidator.required(idStablishment);

        /* Se prepara la petición del WS */
        PeticionConceptosPorRefSet peticion = new PeticionConceptosPorRefSet();
        peticion.setIdEstablecimiento(idStablishment);
        peticion.setNombreRefSet(refSetName);

        RespuestaRefSets finalResponse = new RespuestaRefSets();
        logger.debug("WebServiceReq022Servlet.doPost(): Params: " + Stringer.toString(peticion));

        /* Se invoca el servicio */
        RespuestaConceptosPorRefSet response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().conceptosPorRefSet(peticion);
            logger.debug("WebServiceReq022Servlet.doPost(): Response: " + Stringer.toString(response));
        } catch (SOAPFaultException | NotFoundFault_Exception e) {
            req.setAttribute("exception", e);
            req.getRequestDispatcher(targetPage).forward(req, resp);
        }

        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", finalResponse);
        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq022Servlet:doGet()");
        this.doPost(req, resp);
    }
}
