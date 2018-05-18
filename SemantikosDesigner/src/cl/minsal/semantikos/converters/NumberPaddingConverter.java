package cl.minsal.semantikos.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 26-08-16.
 */

@FacesConverter("numberPaddingConverter")
public class NumberPaddingConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if(s.isEmpty()) {
            return s;
        }

        try {
            long l = Long.parseLong(s);
            return String.format("%014d", l);
        }
        catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "No es un número válido"));
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        return o.toString();
    }
}
