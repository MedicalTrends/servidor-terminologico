package cl.minsal.semantikos.ws.ingreso;

import cl.minsal.semantikos.ws.shared.Descripcion;
import cl.minsal.semantikos.ws.shared.ServicioDeIngreso_Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andrés Farías
 */
@WebServlet(urlPatterns = "/ws-req-030")
public class WebServiceReq030Servlet extends HttpServlet {

    /** El proxy al servicio web */
    private ServicioDeIngreso_Service servicioDeIngreso_service;

    /**
     * El constructror inicializa el proxy hacia el servicio.
     */
    public WebServiceReq030Servlet() {
        this.servicioDeIngreso_service = new ServicioDeIngreso_Service();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq030Servlet:doPost()");

        Descripcion description_id = servicioDeIngreso_service.getServicioDeIngresoPort().incrementarContadorDescripcionConsumida(req.getParameter("DESCRIPTION_ID"));

        System.out.println("WebServiceReq030Servlet:doPost(): " + description_id.toString());
        req.setAttribute("serviceResponse", description_id);

        req.getRequestDispatcher("/update_service/ServicioIngresoWS-REQ-030.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WebServiceReq030Servlet:doGet()");
    }
}
