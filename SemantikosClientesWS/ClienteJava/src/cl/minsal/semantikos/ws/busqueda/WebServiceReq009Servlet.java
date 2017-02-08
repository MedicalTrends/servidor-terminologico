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
import java.util.List;

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-009")
public class WebServiceReq009Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq009Servlet.class);

    /** La pagina JSP de destino para este servlet */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-009.jsp";


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
    public WebServiceReq009Servlet() {
        this.servicioDeBusqueda = new ServicioDeBusqueda();
        this.parameterValidator = new ParameterValidator();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq005Servlet:doPost()");

        /* Se recuperan los parámetros */
        String description_ids = req.getParameter("description_ID");
        String includeStablishments = req.getParameter("includeStablishments");
        String idStablishment = req.getParameter("idStablishment");

        /* Se realizan las validaciones */
        this.parameterValidator.required(description_ids);
        this.parameterValidator.required(includeStablishments);
        this.parameterValidator.required(idStablishment);

        /* Se prepara la petición del WS */
        PeticionRefSetsPorIdDescripcion peticion = new PeticionRefSetsPorIdDescripcion();
        peticion.setIncluyeEstablecimientos(Boolean.valueOf(includeStablishments));
        peticion.setIdStablishment(idStablishment);

        String[] ids = description_ids.split(",");
        RespuestaRefSets finalResponse = new RespuestaRefSets();
        for (String id : ids) {
            peticion.setIdDescripcion(id.trim());
            logger.debug("WebServiceReq009Servlet.doPost(): Params: " + Stringer.toString(peticion));

            /* Se invoca el servicio */
            RespuestaRefSets response;
            try {
                response = this.servicioDeBusqueda.getSearchServicePort().refSetsPorIdDescripcion(peticion);
                logger.debug("WebServiceReq009Servlet.doPost(): Response: " + Stringer.toString(response));

                /* Se agregan los refsets */
                new RequestResponseUtil().addAllRefsets(finalResponse, response);
            } catch (IllegalInputFault_Exception | SOAPFaultException e) {
                req.setAttribute("exception", e);
                req.getRequestDispatcher(targetPage).forward(req, resp);
                return;
            } catch (NotFoundFault_Exception e) {
                e.printStackTrace();
                return;
            }
        }

        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", finalResponse);
        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq007Servlet:doGet()");
        this.doPost(req ,resp);
    }
}
