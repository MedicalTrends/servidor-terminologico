package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.beans.concept.ConceptBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 * Created by des01c7 on 14-10-16.
 */

@ManagedBean(name = "autogenerateBean")
@ViewScoped
public class Autogenerate {

    @ManagedProperty(value="#{conceptBean}")
    private ConceptBean conceptBean;

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    @PostConstruct
    public void init(){
    }


    public void autogenerateMB(){
        conceptBean.getConcept().getValidDescriptionFavorite().setTerm("");
    }

    public void autogenerateMC(){
        conceptBean.getConcept().getValidDescriptionFavorite().setTerm("");
    }

    public void autogenerateMCCE(){
        conceptBean.getConcept().getValidDescriptionFavorite().setTerm("");
    }

    public void autogeneratePCCE(){
        conceptBean.getConcept().getValidDescriptionFavorite().setTerm("");
    }
}
