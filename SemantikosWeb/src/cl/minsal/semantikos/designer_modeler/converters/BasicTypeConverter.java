package cl.minsal.semantikos.designer_modeler.converters;

import cl.minsal.semantikos.designer_modeler.designer.HelperTableBean;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 04-08-16.
 */

//@FacesConverter("helperTableRecordConverter")
@FacesConverter(value="basicTypeConverter",forClass = BasicTypeValue.class)
public class BasicTypeConverter implements Converter{


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) UIComponent.getCurrentComponent(fc).getAttributes().get("basicTypeDefinition");
        BasicTypeValue basicTypeValue = (BasicTypeValue) UIComponent.getCurrentComponent(fc).getAttributes().get("basicTypeValue");

        if(value != null /*&& value.trim().length() > 0*/ ) {
            try {
                ELContext elContext = fc.getELContext();
                HelperTableBean bean = (HelperTableBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "helperTableBean");
                if(basicTypeDefinition.contains(value))
                    return new BasicTypeValue(value);
                else
                    return null;

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar un valor."));
            }
        }
        else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((BasicTypeValue) object).getId());
        }
        else {
            return "";
        }
    }


}
