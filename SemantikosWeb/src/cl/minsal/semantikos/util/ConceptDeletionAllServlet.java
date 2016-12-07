package cl.minsal.semantikos.util;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 11/25/16.
 */
@WebServlet(urlPatterns = "/deleteAllConcepts")
public class ConceptDeletionAllServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ConceptDeletionAllServlet.class);

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    private ConceptManager conceptManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Long> allConceptsID = conceptDAO.getAllConceptsId();

        ConceptSMTK noValidConcept = conceptDAO.getNoValidConcept();
        ConceptSMTK pendingConcept = conceptDAO.getPendingConcept();

        List<String> deletedConcepts = new ArrayList<>();
        for (Long aConceptID : allConceptsID) {
            ConceptSMTK aConcept = conceptManager.getConceptByID(aConceptID);

            /* Si el concepto no es el invalido o el de pendientes, se puede borrar */
            if (!aConcept.equals(noValidConcept) && !aConcept.equals(pendingConcept)) {
                deletedConcepts.add(aConcept.toString());
                conceptDAO.delete(aConcept);
            }
        }

        /* Se imprime la respuesta con datos sobre la eliminación */
        PrintWriter writer = resp.getWriter();
        writer.println("<HTML>");
        writer.println("<BODY>");
        writer.println("<H3>Conceptos Eliminados (" + deletedConcepts.size() + ")</H3>");
        writer.println("<UL>");
        for (String deletedConcept : deletedConcepts) {
            writer.println("<LI>" + deletedConcept);
        }
        writer.println("</UL>");
        writer.println("</BODY>");
        writer.println("</HTML>");
    }

}
