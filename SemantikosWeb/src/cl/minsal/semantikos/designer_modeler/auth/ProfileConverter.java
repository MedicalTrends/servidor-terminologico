package cl.minsal.semantikos.designer_modeler.auth;

import cl.minsal.semantikos.model.users.Profile;

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

@FacesConverter("profileConverter")
public class ProfileConverter implements Converter{


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {

                ELContext elContext = fc.getELContext();
                UsersBean bean = (UsersBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "users");
                return bean.getProfileById(Long.parseLong(value));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid profile."));
            }catch(Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid profile."));
            }
        }
        else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Profile) object).getId());
        }
        else {
            return null;
        }
    }


}
