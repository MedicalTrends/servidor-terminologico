package cl.minsal.semantikos.converters;


import cl.minsal.semantikos.browser.BrowserBean;
import cl.minsal.semantikos.browser.BrowserSCTBean;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;

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

@FacesConverter("DescriptionSCTConverter")
public class DescriptionSCTConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if(s.equals("-1")) {
            return DescriptionSCT.DUMMY_DESCRIPTION_SCT;
        }

        if(s != null && s.trim().length() > 0) {
            try {

                ELContext elContext = facesContext.getELContext();
                BrowserSCTBean bean = (BrowserSCTBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "browserSCTBean");
                return bean.getSnomedCTManager().getDescriptionByID(Long.parseLong(s));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Descripción no valida"));
            }
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((DescriptionSCT) o).getId());
        }
        else {
            return null;
        }
    }
}
