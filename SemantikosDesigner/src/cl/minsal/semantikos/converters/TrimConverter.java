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

@FacesConverter(forClass=String.class)
public class TrimConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        String trimmed = (submittedValue != null) ? submittedValue.trim() : null;
        return (trimmed == null || trimmed.isEmpty()) ? null : trimmed;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        return (modelValue != null) ? modelValue.toString() : "";
    }

}
