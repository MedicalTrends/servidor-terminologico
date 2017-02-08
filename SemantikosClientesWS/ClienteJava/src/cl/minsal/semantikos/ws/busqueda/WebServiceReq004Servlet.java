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
@WebServlet(urlPatterns = "/ws-req-004")
public class WebServiceReq004Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq004Servlet.class);

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
    public WebServiceReq004Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq004Servlet:doPost()");

        /* Se recuperan los parámetros */
        String term = req.getParameter("term");
        String categories = req.getParameter("categories");
        String refsets = req.getParameter("refsets");
        String idStablishment = req.getParameter("idStablishment");

        /* Se realizan las validaciones */
        this.parameterValidator.required(term);
        this.parameterValidator.required(idStablishment);

        /* Se prepara la petición del WS */
        PeticionBuscarTermino peticion = new PeticionBuscarTermino();
        peticion.setTermino(term);
        peticion.setNombreCategoria(Arrays.asList(categories.split(",")));
        peticion.setNombreRefSet(Arrays.asList(refsets.split(",")));
        peticion.setIdEstablecimiento(idStablishment);

        logger.debug("WebServiceReq004Servlet.doPost(): Params: " + Stringer.toString(peticion));

        /* Se invoca el servicio */
        RespuestaConceptosPorCategoria response;
        try {
            response = this.servicioDeBusqueda.getSearchServicePort().buscarTruncatePerfect(peticion);
            logger.debug("WebServiceReq004Servlet.doPost(): Response: " + Stringer.toString(response));
        } catch (NotFoundFault_Exception e) {
            logger.error("WebServiceReq002Servlet invocation error", e);
            req.setAttribute("exception", e);
            req.getRequestDispatcher("/search_service/ServicioBusquedaWS-REQ-004.jsp").forward(req, resp);
            return;
        } catch (IllegalInputFault_Exception e) {
            req.setAttribute("exception", e);
            req.getRequestDispatcher("/search_service/ServicioBusquedaWS-REQ-004.jsp").forward(req, resp);
            return;
        }

        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", response);
        req.getRequestDispatcher("/search_service/ServicioBusquedaWS-REQ-004.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq004Servlet:doGet()");
        this.doPost(req ,resp);
    }
}
