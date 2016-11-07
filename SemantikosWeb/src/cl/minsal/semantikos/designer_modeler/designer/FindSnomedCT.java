package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * @author Andrés Farías on 10/26/16.
 */
@ManagedBean(name = "findSnomedCTBean")
@ViewScoped
public class FindSnomedCT {

    private static final Logger logger = LoggerFactory.getLogger(FindSnomedCT.class);

    @EJB
    private SnomedCTManager snomedCTManager;

    private List<ConceptSCT> conceptSCTs;

    public String getConcept(String pattern) {
        this.conceptSCTs = snomedCTManager.findConceptsByPattern(pattern);

        logger.info("Se encontraron " + this.conceptSCTs.size() + " conceptos SnomedCT");
        return "Todos";
    }
}
