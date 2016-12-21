package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.BasicTypeType;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
public class ConceptQueryFilter {

    RelationshipDefinition definition;

    List<Target> targets = new ArrayList<Target>();

    boolean multiple;

    boolean checkable;

    boolean checked;

    boolean secondOrder;

    public ConceptQueryFilter(RelationshipDefinition definition) {
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

    public List<Long> getCategoryValues(){

        List<Long> categoryValues = new ArrayList<>();

        if(definition.getTargetDefinition().isSMTKType()){
            for (Target target : targets)
                categoryValues.add(definition.getTargetDefinition().getId());
        }

        return categoryValues;
    }

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
                HelperTableRecord helperTableRecord = (HelperTableRecord) target;
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
                basicTypeValues.add(basicTypeValue.getValue().toString());
            }
        }

        return basicTypeValues;

    }

}
