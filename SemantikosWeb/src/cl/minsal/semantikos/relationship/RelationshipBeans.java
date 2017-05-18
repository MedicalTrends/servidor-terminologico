package cl.minsal.semantikos.relationship;

import cl.minsal.semantikos.concept.ConceptBean;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.modelweb.RelationshipWeb;
import org.primefaces.event.RowEditEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by des01c7 on 02-12-16.
 */
@ManagedBean(name = "relationshipBean")
@ViewScoped
public class RelationshipBeans {
    @ManagedProperty( value="#{conceptBean}")
    ConceptBean conceptBean;

    private Relationship relationshipSelected;

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }


    /**
     * Este método es el encargado de agregar relaciones al concepto recibiendo como parámetro un Relationship
     * Definition. Este método es utilizado por el componente BasicType, el cual agrega relaciones con target sin valor
     */
    public void addRelationship(RelationshipDefinition relationshipDefinition) {
        Target target = new BasicTypeValue(null);
        Relationship relationship = new Relationship(conceptBean.getConcept(), target, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);
        // Se utiliza el constructor mínimo (sin id)
        conceptBean.getConcept().addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
    }


    /**
     * Este método es el responsable de retornar verdadero en caso que se cumpla el UpperBoundary de la multiplicidad,
     * para asi desactivar
     * la opción de agregar más relaciones en la vista. En el caso que se retorne falso este seguirá activo el boton en
     * la presentación.
     *
     * @return
     */
    public boolean limitRelationship(RelationshipDefinition relationshipD) {
        if (relationshipD.getMultiplicity().getUpperBoundary() != 0) {
            if (conceptBean.getConcept().getValidRelationshipsByRelationDefinition(relationshipD).size() == relationshipD.getMultiplicity().getUpperBoundary()) {
                return true;
            }
        }
        return false;
    }


}
