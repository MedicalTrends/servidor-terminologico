package cl.minsal.semantikos.designer_modeler.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 26-08-16.
 */

@FacesConverter("booleanConverter")
public class BooleanConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if(s.equals("0"))
            return null;
        if(s.equals("1"))
            return true;
        if(s.equals("2"))
            return false;
        else
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid string."));

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        return o.toString();
    }
}
