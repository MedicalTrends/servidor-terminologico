package cl.minsal.semantikos.ws.busqueda;

import cl.minsal.semantikos.modelws.request.DescriptionIDorConceptIDRequest;
import cl.minsal.semantikos.ws.ParameterValidator;
import cl.minsal.semantikos.modelws.request.DescriptionIDorConceptIDRequest;
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
@WebServlet(urlPatterns = "/ws-req-042")
public class WebServiceReq042Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq042Servlet.class);

    /**
     * La pagina JSP de destino para este servlet
     */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-042.jsp";

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
    public WebServiceReq042Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + ":doPost()");

        /* Se recuperan los parámetros */
        String stablishment_id = req.getParameter("stablishment_id");

        /* Se realizan las validaciones */
        this.parameterValidator.required(stablishment_id);

        /* Se prepara la petición del WS */
        DescriptionIDorConceptIDRequest request = new DescriptionIDorConceptIDRequest();
        request.setIdStablishment(stablishment_id);

        logger.debug(this.getClass().getName() + ".doPost(): Params: " + Stringer.toString(request));

        /* Se invoca el servicio */
        RespuestaCategorias response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().listaCategorias();
            logger.debug(this.getClass().getName() + ".doPost(): Response: " + Stringer.toString(response));
        } catch (SOAPFaultException | NotFoundFault_Exception e) {
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
        System.out.println(this.getClass().getName() + ":doGet()");
        this.doPost(req, resp);
    }
}
