package converters;


import browser.BrowserBean;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;

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

@FacesConverter("DescriptionConverter")
public class DescriptionConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if(s.equals("-1")) {
            return DescriptionTypeFactory.DUMMY_DESCRIPTION;
        }

        if(s != null && s.trim().length() > 0) {
            try {

                ELContext elContext = facesContext.getELContext();
                BrowserBean bean = (BrowserBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "browserBean");
                return bean.getDescriptionManager().getDescriptionByID(Integer.parseInt(s));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Descripción no valida"));
            }
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((Description) o).getId());
        }
        else {
            return null;
        }
    }
}
