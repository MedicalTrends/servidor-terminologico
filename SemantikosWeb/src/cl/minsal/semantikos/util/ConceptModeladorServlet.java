package cl.minsal.semantikos.util;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andrés Farías on 10/18/16.
 */
@WebServlet(urlPatterns = "/forceConceptModel")
public class ConceptModeladorServlet extends HttpServlet{

    private static final Logger logger = LoggerFactory.getLogger(ConceptModeladorServlet.class);

    @EJB
    ConceptDAO conceptDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String concept_id = req.getParameter("id");
        Long id = Long.parseLong(concept_id);
        logger.info("Modelando el concepto de ID=" + id);

        conceptDAO.forcedModeledConcept(id);
    }
}
