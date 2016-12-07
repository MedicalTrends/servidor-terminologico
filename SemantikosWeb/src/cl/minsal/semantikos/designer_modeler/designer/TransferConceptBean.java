package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrés Farías on 11/24/16.
 */

@ManagedBean(name = "transferConceptBean")
@ViewScoped
public class TransferConceptBean {

    private static final Logger logger = LoggerFactory.getLogger(TransferConceptBean.class);

    /** El ID de la categoría destino */
    private long categoryId;

    @EJB
    private ConceptManager conceptManager;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    @EJB
    private CategoryManager categoryManager;

    private ConceptSMTK conceptSMTKSelected;

    public ConceptSMTK getConceptSMTKSelected() {
        return conceptSMTKSelected;
    }

    public void setConceptSMTKSelected(ConceptSMTK conceptSMTKSelected) {
        this.conceptSMTKSelected = conceptSMTKSelected;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void transferConcept(ConceptSMTK conceptSMTK) {
        Category categoryById = categoryManager.getCategoryById(categoryId);
        conceptManager.transferConcept(conceptSMTK, categoryById);

        /* Se redirige a la página de edición */
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + categoryId + "&idConcept=" + conceptSMTK.getId());
        } catch (IOException e) {
            logger.error("Error al redirigir");
        }
    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }
}
