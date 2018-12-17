package cl.minsal.semantikos.converters;


import cl.minsal.semantikos.concept.ConceptBean;
import cl.minsal.semantikos.model.audit.EliminationCausal;
import cl.minsal.semantikos.model.categories.Category;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 09-09-16.
 */

@FacesConverter("EliminationCausalConverter")
public class EliminationCausalConverter implements Converter{


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            try {

                ELContext elContext = facesContext.getELContext();
                ConceptBean bean = (ConceptBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "conceptBean");
                for (EliminationCausal eliminationCausal : bean.getEliminationCausals()) {
                    if(eliminationCausal.getId() == Long.parseLong(s)) {
                        return eliminationCausal;
                    }
                }
                return null;

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Categoria no valida"));
            }
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((EliminationCausal) o).getId());
        }
        else {
            return null;
        }
    }
}
