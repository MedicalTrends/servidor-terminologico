package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.Constants;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.messages.MessageBean;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Andrés Farías on 11/24/16.
 */

@ManagedBean(name = "transferConceptBean")
@SessionScoped
public class TransferConceptBean {

    private static final Logger logger = LoggerFactory.getLogger(TransferConceptBean.class);

    /** El ID de la categoría destino */
    private Category targetCategory;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    @ManagedProperty(value = "#{messageBean}")
    private MessageBean messageBean;

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    private ConceptSMTK conceptSMTKSelected;

    public ConceptSMTK getConceptSMTKSelected() {
        return conceptSMTKSelected;
    }

    public void setConceptSMTKSelected(ConceptSMTK conceptSMTKSelected) {
        this.conceptSMTKSelected = conceptSMTKSelected;
    }

    public MessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public Category getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(Category targetCategory) {
        this.targetCategory = targetCategory;
    }

    public void transferConcept(ConceptSMTK conceptSMTK) {
        //Category categoryById = categoryManager.getCategoryById(categoryId);
        try{
            for (Description description : conceptSMTK.getDescriptions()) {

                ConceptSMTK aConcept = categoryManager.categoryContains(targetCategory, description.getTerm());

                if(aConcept != null){
                    messageBean.messageError("La descripción: "+description+" ya existe en la categoría. Descripción perteneciente a concepto: "+aConcept);
                    return;
                }
            }
            conceptManager.transferConcept(conceptSMTK, targetCategory, conceptBean.user);
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            //eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + categoryId + "&idConcept=" + conceptSMTK.getId());
            eContext.redirect(eContext.getRequestContextPath() + Constants.VIEWS_FOLDER + "/concepts/edit/" + conceptSMTK.getId());

        }catch (EJBException e){
            messageBean.messageError(e.getMessage());
        }
        /* Se redirige a la página de edición */
        catch (IOException e) {
            logger.error("Error al redirigir");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }
}
