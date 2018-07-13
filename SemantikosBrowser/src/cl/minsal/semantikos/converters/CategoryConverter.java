package cl.minsal.semantikos.converters;


import cl.minsal.semantikos.browser.ConceptBean;
import cl.minsal.semantikos.category.CategoryBean;
import cl.minsal.semantikos.model.categories.Category;

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

@FacesConverter("CategoryConverter")
public class CategoryConverter implements Converter{


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s != null && s.trim().length() > 0) {
            try {

                ELContext elContext = facesContext.getELContext();
                CategoryBean bean = (CategoryBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "conceptBean");
                return bean.getCategoryManager().getCategoryById(Integer.parseInt(s));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Categoria no valida"));
            }
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o != null) {
            return String.valueOf(((Category) o).getId());
        }
        else {
            return null;
        }
    }
}
