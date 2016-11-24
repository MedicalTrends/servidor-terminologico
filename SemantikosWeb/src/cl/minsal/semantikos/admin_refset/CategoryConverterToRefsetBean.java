package cl.minsal.semantikos.admin_refset;

import cl.minsal.semantikos.designer_modeler.designer.ConceptBean;
import cl.minsal.semantikos.model.Category;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.text.ParseException;

/**
 * Created by des01c7 on 21-09-16.
 */
@FacesConverter("CategoryConverterRefsetBean")
public class CategoryConverterToRefsetBean implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
            if(s != null && s.trim().length() > 0) {
                try {

                    ELContext elContext = facesContext.getELContext();
                    RefSetsBean bean = (RefSetsBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "refsetsBean");
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
