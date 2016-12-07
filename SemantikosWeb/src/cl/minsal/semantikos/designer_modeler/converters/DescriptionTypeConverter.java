package cl.minsal.semantikos.designer_modeler.converters;

import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.model.DescriptionType;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 11-08-16.
 */
@FacesConverter("descriptionTypeConverter")
public class DescriptionTypeConverter implements Converter {


        public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
            if(value != null && value.trim().length() > 0) {
                try {
                    ELContext elContext = fc.getELContext();
                    ConceptBean bean = (ConceptBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "conceptBean");

                    for (int i = 0; i < bean.getDescriptionTypes().size() ; i++) {
                        if (value.equalsIgnoreCase(String.valueOf(((DescriptionType)bean.getDescriptionTypes().get(i)).getId()))) {
                            return bean.getDescriptionTypes().get(i);
                        }

                    }
                } catch(NumberFormatException e) {
                    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
                }
            }
            return null;
        }

        public String getAsString(FacesContext fc, UIComponent uic, Object object) {
            if(object != null ) {
                return String.valueOf(((DescriptionType)object).getId());

            }
                return null;

        }


    }

