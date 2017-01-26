package cl.minsal.semantikos.beans.description;

import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.beans.messages.MessageBean;
import cl.minsal.semantikos.model.*;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;

/**
 * @author Gustavo Punucura
 */

@ManagedBean(name = "descriptionBeans")
@ViewScoped
public class DescriptionBeans {

    @ManagedProperty(value = "#{conceptBean}")
    ConceptBean conceptBean;

    @ManagedProperty(value = "#{messageBean}")
    MessageBean messageBean;

    DescriptionTypeFactory descriptionTypeFactory = DescriptionTypeFactory.getInstance();

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    public MessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    private static long SYNONYMOUS_ID = 3;

    @PostConstruct
    public void init() {

    }

    /**
     * Este método es el encargado de agregar descripciones al concepto
     */
    public void addDescription() {
        if (!conceptBean.getOtherTermino().trim().equals("")) {
            if (conceptBean.getOtherDescriptionType() != null) {
                if (conceptBean.getOtherDescriptionType().getName().equalsIgnoreCase("abreviada") && conceptBean.getConcept().getValidDescriptionAbbreviated() != null) {
                    messageBean.messageError("Solo puede existir una descripción abreviada");
                    return;
                }
                DescriptionWeb description = new DescriptionWeb(conceptBean.getConcept(), conceptBean.getOtherTermino(), conceptBean.getOtherDescriptionType());
                if (conceptBean.getCategoryManager().categoryContains(conceptBean.getCategory(), description.getTerm())) {
                    messageBean.messageError("Esta descripcion ya existe en esta categoria");
                    return;
                }
                if (conceptBean.containDescription(description)) {
                    messageBean.messageError("Esta descripcion ya existe en este concepto");
                    return;
                }

                description.setCaseSensitive(conceptBean.getOtherSensibilidad());
                if (conceptBean.getOtherDescriptionType().getName().equalsIgnoreCase("abreviada") || conceptBean.getOtherDescriptionType().getName().equalsIgnoreCase("sinónimo")) {
                    description.setCaseSensitive(conceptBean.getConcept().getDescriptionFavorite().isCaseSensitive());
                }

                description.setModeled(conceptBean.getConcept().isModeled());
                description.setCreatorUser(conceptBean.user);
                description.setDescriptionId(conceptBean.getDescriptionManager().generateDescriptionId());
                conceptBean.getConcept().addDescriptionWeb(description);
                conceptBean.setOtherTermino("");
                conceptBean.setOtherDescriptionType(null);
            } else {
                messageBean.messageError("No se ha seleccionado el tipo de descripción");
            }
        } else {
            messageBean.messageError("No se ha ingresado el término a la descripción");
        }
    }

    /**
     * Este método es el encargado de remover una descripción específica de la lista de descripciones del concepto.
     */
    public void removeDescription(Description description) {
        conceptBean.getConcept().removeDescriptionWeb(description);
    }

    /**
     * Este método es el encargado de trasladar las descripciones al concepto especial no válido
     */
    public void traslateDescriptionNotValid() {
        conceptBean.getDescriptionToTranslate().setConceptSMTK(conceptBean.getConceptSMTKNotValid());
        conceptBean.getConcept().getDescriptionsWeb().remove(conceptBean.getDescriptionToTranslate());
        conceptBean.getNoValidDescriptions().add(new NoValidDescription(conceptBean.getDescriptionToTranslate(), conceptBean.getObservationNoValid().getId(), conceptBean.getConceptSuggestedList()));
        messageBean.messageSuccess("Traslado de descripción", "La descripción se trasladará al momento de guardar el concepto");
        conceptBean.setConceptSuggestedList(new ArrayList<ConceptSMTK>());

    }

    /**
     * Este método es el encargado de trasladar descripciones a otros conceptos
     */
    public void traslateDescription() {
        if (conceptBean.getConceptSMTKTranslateDes() == null) {
            messageBean.messageError("No se seleccionó el concepto de destino");
        } else {
            conceptBean.getConcept().getDescriptionsWeb().remove(conceptBean.getDescriptionToTranslate());
            conceptBean.getDescriptionToTranslate().setConceptSMTK(conceptBean.getConceptSMTKTranslateDes());
            conceptBean.getDescriptionsToTraslate().add(new DescriptionWeb(conceptBean.getDescriptionToTranslate()));
            conceptBean.setConceptSMTKTranslateDes(null);
            conceptBean.setDescriptionToTranslate(null);
            messageBean.messageSuccess("Acción exitosa", "La descripción se trasladará al momento de guardar el concepto");
        }
    }

    /**
     * Metodo encargado de hacer el "enroque" con la preferida.
     */
    public void descriptionEditRow(RowEditEvent event) {
        DescriptionWeb descriptionWeb = (DescriptionWeb) event.getObject();
        if (descriptionWeb.getDescriptionType().getName().equalsIgnoreCase("abreviada")) {
            descriptionWeb.setCaseSensitive(conceptBean.getConcept().getDescriptionFavorite().isCaseSensitive());
        }
        for (DescriptionWeb descriptionRowEdit : conceptBean.getConcept().getDescriptionsWeb()) {
            if (descriptionRowEdit.equals(descriptionWeb)) {
                if (descriptionRowEdit.getDescriptionType().equals(descriptionTypeFactory.getFavoriteDescriptionType())) {
                    descriptionRowEdit.setDescriptionType(descriptionTypeFactory.getDescriptionTypeByID(SYNONYMOUS_ID));
                    DescriptionWeb descriptionFavorite = conceptBean.getConcept().getValidDescriptionFavorite();
                    descriptionFavorite.setDescriptionType(descriptionTypeFactory.getDescriptionTypeByID(SYNONYMOUS_ID));
                    descriptionRowEdit.setDescriptionType(descriptionTypeFactory.getFavoriteDescriptionType());
                }
            }
        }
    }


    public void updateFSNFromFavourite(DescriptionWeb description) {

        description.getConceptSMTK().getDescriptionFSN().setTerm(description.getTerm().trim().replaceAll("\\(" + description.getConceptSMTK().getTagSMTK() + "\\)", "").trim());

    }

    public void updateFSNFromTagSMTK(ConceptSMTK conceptSMTK) {

        conceptSMTK.getDescriptionFSN().setTerm(conceptSMTK.getDescriptionFSN().getTerm());

    }

    public boolean disableSensibilityFSN(Description description){

        if( description.getConceptSMTK().isModeled() && description.getConceptSMTK().getCategory().getId()== 33L){
            return false;
        }
        if(description.getConceptSMTK().isModeled()){
            return true;
        }


        return false;
    }



}
