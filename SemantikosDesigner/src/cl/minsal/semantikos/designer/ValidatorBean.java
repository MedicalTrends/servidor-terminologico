package cl.minsal.semantikos.designer;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.kernel.businessrules.ConceptDefinitionalGradeBR;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.DescriptionWeb;
import org.primefaces.context.RequestContext;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.util.List;

/**
 * Created by root on 02-09-16.
 */
@ManagedBean(name = "validatorBean")
@SessionScoped
public class ValidatorBean {

    //@EJB
    private ConceptDefinitionalGradeBR conceptDefinitionalGradeBR = (ConceptDefinitionalGradeBR) ServiceLocator.getInstance().getService(ConceptDefinitionalGradeBR.class);

    //@EJB
    private RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    //@EJB
    private SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public void validateRequiredInput(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg = "Debe especificar una descripción FSN y Preferida";

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
        HelperTableRow record = (HelperTableRow) UIComponent.getCurrentComponent(context).getAttributes().get("helperTableRecord");
        HelperTableRow record2 = (HelperTableRow) value;

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
        if(aDescription.getConceptSMTK()==null)return;
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
        DescriptionWeb description=((DescriptionWeb)component.getAttributes().get("description"));
        if(description.getCreatorUser()!= User.getDummyUser()){
            List<Description> aDescription = ((DescriptionWeb) component.getAttributes().get("description")).getConceptSMTK().getDescriptions();

            if(countAbbreviatedDescription(aDescription)>1){
                msg = "Un concepto no puede tener más de una descripción abreviada";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
            }
        }


    }

    private int countAbbreviatedDescription(List<Description> descriptionList){
        int count=0;
        for (Description aDescriptionList : descriptionList) {
            if (aDescriptionList.getDescriptionType().getName().equalsIgnoreCase("abreviada")) count++;
        }

        return count;
    }

    /**
     * Este metodo es responsable de validar el bioequivalente:
     * 1.- Que un término no esté vacía
     * 2.- Que un término no esté siendo utilizado por otra descripción del concepto que la posee
     * @return
     */
    public void validateHelperTableRecord(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String msg;

        RelationshipDefinition concept = (RelationshipDefinition) component.getAttributes().get("concept");

        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) component.getAttributes().get("relationshipDefinition");

        HelperTableRow helperTableRecord = (HelperTableRow) component.getAttributes().get("helperTableRecord");


        if(relationshipDefinition.isBioequivalente()) {

            if(helperTableRecord != null) {
                /**
                 * Verificar que no existan ISP apuntando a este bioequivalente
                 */

                /**
                 * Primero verificar en el contexto no persistido
                 */


                /**
                 * Luego verificar en el contexto persistido
                 */
                for (Relationship relationship : relationshipManager.getRelationshipsLike(relationshipDefinition, helperTableRecord)) {
                    if (relationship.getRelationshipDefinition().isISP()) {
                        msg = "Este bioequivalente está actualmente siendo utilizado como ISP por el concepto " + relationship.getSourceConcept().getDescriptionFavorite();
                        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
                    }
                }
            }

        }

    }


    public void validateQueryResultSize(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        RequestContext rContext = RequestContext.getCurrentInstance();
        String searchOption = (String) component.getAttributes().get("searchOption");
        Integer relationshipGroup = (Integer) component.getAttributes().get("relationshipGroup");
        String pattern = (String) value;

        if ( searchOption.equals("term") &&  pattern.trim().length() >= 3  ) {

            if (snomedCTManager.countConceptByPattern(pattern, relationshipGroup) > 1000) {
                rContext.execute("PF('dialogSCT').show();");
                String msg = "Los resultados de esta búsqueda son extensos.";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", msg));
            }

        }

    }

}
