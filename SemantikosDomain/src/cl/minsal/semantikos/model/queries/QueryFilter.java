package cl.minsal.semantikos.model.queries;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */

/**
 * Esta clase representa un filtro de Consulta del Navegador
 *
 * @author Diego Soto.
 */
public class QueryFilter implements Serializable {

    /**
     * La definición de este filtro
     */
    RelationshipDefinition definition;

    /**
     * La lista de valores seleccionados para este filtro
     */
    List<Target> targets = new ArrayList<Target>();

    /**
     * Indica si este filtro acepta selección múltiple o no
     */
    boolean multiple;

    /**
     * Indica si este filtro es de tipo checkeable, es decir, solo acepta los valores VERDADERO y FALSO:
     * VERDADERO para indicar que debe existir la relación establecida por esta definición
     * FALSO para indicar que no debe existir la relación establecida por esta definición
     */
    boolean checkable;

    /**
     * Si este filtro es de tipo chequeable, indica si el valor está seteado en VERDADERO o FALSO
     */
    boolean checked;

    /**
     * Indica si este filtro es de 2o orden. Es decir, si su definición corresponde a una relación que
     * proviene de una relación a concepto SMTK
     */
    boolean secondOrder;

    /**
     * Indica el componente de vista para este filtro
     */
    int composite;

    public QueryFilter(RelationshipDefinition definition) {
        this.definition = definition;
    }

    public RelationshipDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(RelationshipDefinition definition) {
        this.definition = definition;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public void setTargets(List<Target> targets) {
        if(targets == null)
            this.targets.clear();
        else
            this.targets = targets;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSecondOrder() {
        return secondOrder;
    }

    public void setSecondOrder(boolean secondOrder) {
        this.secondOrder = secondOrder;
    }

    /**
     * Este método es responsable de recuperar los ids de las categorías de los conceptos filtrados en este filtro
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * categorías de los conceptos filtrados
     */
    public List<Long> getCategoryValues(){

        List<Long> categoryValues = new ArrayList<>();

        if(definition.getTargetDefinition().isSMTKType()){
            for (Target target : targets)
                categoryValues.add(definition.getTargetDefinition().getId());
        }

        return categoryValues;
    }

    /**
     * Este método es responsable de recuperar los ids de los conceptos filtrados en este filtro
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * conceptos filtrados
     */
    public List<Long> getConceptValues(){

        List<Long> conceptValues = new ArrayList<>();

        if(definition.getTargetDefinition().isSMTKType()){
            for (Target target : targets) {
                ConceptSMTK conceptSMTK = (ConceptSMTK) target;
                conceptValues.add(conceptSMTK.getId());
            }
        }

        return conceptValues;
    }

    /**
     * Este método es responsable de recuperar los ids de las tablas auxiliares filtradas en este filtro
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * categorías de los conceptos filtrados
     */
    public List<Long> getHelperTableValues(){

        List<Long> helperTableValues = new ArrayList<>();

        if(definition.getTargetDefinition().isHelperTable()){
            for (Target target : targets) {
                //HelperTableRecord helperTableRecord = (HelperTableRecord) target;
                //helperTableValues.add(helperTableRecord.getHelperTable().getId());
                helperTableValues.add(definition.getTargetDefinition().getId());
            }
        }

        return helperTableValues;
    }

    public List<Long> getHelperTableRecordValues(){

        List<Long> helperTableRecordValues = new ArrayList<>();

        if(definition.getTargetDefinition().isHelperTable()){
            for (Target target : targets) {
                HelperTableRow helperTableRecord = (HelperTableRow) target;
                helperTableRecordValues.add(helperTableRecord.getId());
            }
        }

        return helperTableRecordValues;
    }

    public List<Long> getBasicTypeDefinitionValues(){

        List<Long> basicTypeDefinitionValues = new ArrayList<>();

        if(definition.getTargetDefinition().isBasicType()){
            for (Target target : targets)
                basicTypeDefinitionValues.add(definition.getId());
        }

        return basicTypeDefinitionValues;
    }

    public List<String> getBasicTypeValues(){

        List<String> basicTypeValues = new ArrayList<>();

        if(definition.getTargetDefinition().isBasicType()){
            for (Target target : targets) {
                BasicTypeValue basicTypeValue = (BasicTypeValue) target;
                if(!basicTypeValue.isBoolean()) {
                    basicTypeValues.add(basicTypeValue.toString());
                }
                else {
                    basicTypeValues.add((Boolean)basicTypeValue.getValue()?"1":"0");
                }
            }
        }

        return basicTypeValues;

    }

    public int getComposite() {
        return composite;
    }

    public void setComposite(int composite) {
        this.composite = composite;
    }

}
