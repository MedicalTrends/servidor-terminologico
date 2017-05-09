package cl.minsal.semantikos.designer_modeler.converters;

import cl.minsal.semantikos.designer_modeler.designer.TagBean;
import cl.minsal.semantikos.model.tags.Tag;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 26-08-16.
 */

@FacesConverter("tagConverter")
public class tagConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            try {

                ELContext elContext = facesContext.getELContext();
                TagBean bean = (TagBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "tagBean");
                return bean.getTagManager().findTagByID((Long.parseLong(s)));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((Tag) o).getId());
        }
        else {
            return null;
        }
    }
}
