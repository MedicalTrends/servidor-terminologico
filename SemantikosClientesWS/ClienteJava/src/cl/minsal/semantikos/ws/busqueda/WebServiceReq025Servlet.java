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
@WebServlet(urlPatterns = "/ws-req-025")
public class WebServiceReq025Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq025Servlet.class);

    /**
     * La pagina JSP de destino para este servlet
     */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-025.jsp";

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
    public WebServiceReq025Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + ":doPost()");

        /* Se recuperan los parámetros */
        String idStablishment = req.getParameter("idStablishment");
        String crossmapSetAbbName = req.getParameter("crossmapSetAbbreviatedName");

        /* Se realizan las validaciones */
        this.parameterValidator.required(idStablishment);
        this.parameterValidator.required(crossmapSetAbbName);

        /* Se prepara la petición del WS */
        PeticionConceptosPorRefSet peticion = new PeticionConceptosPorRefSet();
        peticion.setIdEstablecimiento(idStablishment);

        logger.debug(this.getClass().getName() + ".doPost(): Params: " + Stringer.toString(crossmapSetAbbName + ", " + idStablishment));

        /* Se invoca el servicio */
        CrossmapSetMembersResponse response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().crossmapSetMembersDeCrossmapSet(crossmapSetAbbName);
            logger.debug(this.getClass().getName() + ".doPost(): Response: " + Stringer.toString(response));
        } catch (SOAPFaultException e) {
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
