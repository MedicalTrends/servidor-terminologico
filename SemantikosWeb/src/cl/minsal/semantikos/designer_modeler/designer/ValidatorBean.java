package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.businessrules.ConceptDefinitionalGradeBRInterface;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.util.List;

/**
 * Created by root on 02-09-16.
 */
@ManagedBean(name = "validatorBean")
@ViewScoped
public class ValidatorBean {

    @EJB
    private ConceptDefinitionalGradeBRInterface conceptDefinitionalGradeBR;

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public void validateRequiredInput(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg = "Debe ingresar un valor";

        //component.getParent().getAttributes().
        if(value == null)
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));

        if (value.toString().trim().equals(""))
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
    }

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public void validateRequiredConceptSelect(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg = "Debe ingresar un valor";

        ConceptSMTK concept = (ConceptSMTK) value;

        //component.getParent().getAttributes().
        if(concept == null)
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));

    }

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public void validateRequiredRecordSelect(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg = "Debe ingresar un valor";

        HelperTable helperTable = (HelperTable) UIComponent.getCurrentComponent(context).getAttributes().get("helperTable");
        HelperTableRecord record = (HelperTableRecord) UIComponent.getCurrentComponent(context).getAttributes().get("helperTableRecord");;;
        HelperTableRecord record2 = (HelperTableRecord) value;;

        //component.getParent().getAttributes().
        if( record == null && record2 == null )
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));

    }

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public void validateRelationships(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg = "Faltan relaciones para los elementos marcados";

        ConceptSMTK concept = (ConceptSMTK) component.getAttributes().get("concept");

        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) component.getAttributes().get("relationshipDefinition");

        if(concept == null || relationshipDefinition == null)
            return;

        if(concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).size()<relationshipDefinition.getMultiplicity().getLowerBoundary()){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
        }

    }

    /**
     * Este metodo es responsable de realizar las validaciones vista de un termino perteneciente a una descripcion, a saber:
     * 1.- Que un término no esté vacío
     * 2.- Que un término no esté siendo utilizado por otra descripción del concepto que la posee
     * @return
     */
    public void validateTerm(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg;

        DescriptionWeb aDescription = (DescriptionWeb) component.getAttributes().get("description");
        String term = (String) value;

        if(term.trim().equals("")) {
            msg = "Debe ingresar un término";
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
        }

        for (Description description : aDescription.getConceptSMTK().getDescriptions()) {
            if(!description.getDescriptionType().equals(aDescription.getDescriptionType()) && description.getTerm().trim().equals(term.trim())){
                msg = "El concepto ya contiene una descripción que está usando este término";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
            }
        }

    }

    /**
     * Este metodo es responsable de realizar las validaciones vista de un tipo de descripción, a saber:
     * 1.- Que un término no esté vacía
     * 2.- Que un término no esté siendo utilizado por otra descripción del concepto que la posee
     * @return
     */
    public void validateDescriptionType(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg;

        List<Description> aDescription = ((DescriptionWeb) component.getAttributes().get("description")).getConceptSMTK().getDescriptions();

            if(countAbbreviatedDescription(aDescription)>1){
                msg = "Un concepto no puede tener más de una descripción abreviada";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
            }

    }

    private int countAbbreviatedDescription(List<Description> descriptionList){
        int count=0;
        for (Description aDescriptionList : descriptionList) {
            if (aDescriptionList.getDescriptionType().getName().equalsIgnoreCase("abreviada")) count++;
        }

        return count;
    }



    public void validateGradeOfDefinition(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg;

        ConceptSMTK concept = (ConceptSMTK) component.getAttributes().get("concept");

        try {
            conceptDefinitionalGradeBR.apply(concept);
        } catch (EJBException e) {
            if (concept.isModeled()) {
                concept.setFullyDefined(false);
            } else {
                concept.setFullyDefined(false);
            }
            msg = "Un concepto no puede tener más de una descripción abreviada";
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
        }

    }



}
