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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-043")
public class WebServiceReq043Servlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceReq043Servlet.class);

    /**
     * La pagina JSP de destino para este servlet
     */
    private String targetPage = "/search_service/ServicioBusquedaWS-REQ-043.jsp";

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
    public WebServiceReq043Servlet() {
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


        /* Ahora por cada categoría se recuperan los conceptos existentes */
        RespuestaConceptosPorCategoria respuestaConceptos = new RespuestaConceptosPorCategoria();
        RespuestaConceptosPorCategoria.Conceptos value = new RespuestaConceptosPorCategoria.Conceptos();
        //value.setConcepto(new ArrayList<Concepto>());
        respuestaConceptos.setConceptos(value);
        for (Categoria categoria : response.getCategorias().getCategoria()) {
            PeticionPorCategoria categoryRequest = new PeticionPorCategoria();
            categoryRequest.setNombreCategoria(categoria.getNombre());
            categoryRequest.setIdEstablecimiento(stablishment_id);

            try {
                RespuestaConceptosPorCategoria conceptosPorCategoria = this.servicioDeBusqueda
                        .getSearchServicePort().conceptosPorCategoria(categoryRequest);

                RespuestaConceptosPorCategoria.Conceptos conceptosXML = conceptosPorCategoria.getConceptos();
                if (conceptosXML == null){
                    logger.debug("El interior de conceptosPorCategoria era nulo.");
                    continue;
                }

                List<Concepto> conceptoXML = conceptosXML.getConcepto();
                if (conceptoXML == null){
                    logger.debug("La lista de conceptos para la categoría " + categoria.getNombre() + "esta vacia.");
                    continue;
                }

                respuestaConceptos.getConceptos().getConcepto().addAll(conceptoXML);

                logger.debug("Se recuperaron " + respuestaConceptos.getConceptos().getConcepto().size() + " conceptos" +
                        " de la categoría " + categoria.getNombre() + ".");
            } catch (NotFoundFault_Exception e) {
                req.setAttribute("exception", e);
                req.getRequestDispatcher(targetPage).forward(req, resp);
                return;
            }
        }

        /* Se almacena el resultado */
        req.setAttribute("serviceResponse", respuestaConceptos);
        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + ":doGet()");
        this.doPost(req, resp);
    }
}
