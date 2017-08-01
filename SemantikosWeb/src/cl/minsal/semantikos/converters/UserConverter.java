package cl.minsal.semantikos.converters;

import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import cl.minsal.semantikos.tags.TagBean;
import cl.minsal.semantikos.users.UsersBean;
import cl.minsal.semantikos.users.UsersBroswerBean;

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

@FacesConverter("userConverter")
public class UserConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            try {
                return UserFactory.getInstance().findUserById(Long.parseLong(s));

            } catch(NumberFormatException e) {
                return null;
                //throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((User) o).getId());
        }
        else {
            return null;
        }
    }
}
