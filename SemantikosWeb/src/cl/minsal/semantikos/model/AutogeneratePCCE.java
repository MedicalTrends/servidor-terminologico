package cl.minsal.semantikos.model;

import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import javax.ejb.EJB;

/**
 * Created by des01c7 on 18-10-16.
 */
public class AutogeneratePCCE {
    private String autogeneratePCCE;
    private String cantidad;
    private String pack;
    private String vol;
    private String pc;

    public AutogeneratePCCE() {
        this.autogeneratePCCE = "";
        this.cantidad="";
        this.vol="";
        this.pack="";
    }

    public void setCVP(){
        this.cantidad="";
        this.vol="";
        this.pack="";
    }

    public void autogeratePCCE(ConceptSMTK conceptSMTK){
        setCVP();

        for (Relationship relationship: conceptSMTK.getRelationships()) {
            if(relationship.getRelationshipDefinition().getId()==92){
                for (RelationshipAttribute attribute: relationship.getRelationshipAttributes()) {
                    if(attribute.getRelationAttributeDefinition().getId()==15){
                        cantidad = relationship.getTarget().toString()+" "+(((HelperTableRecord)attribute.getTarget()).getValueColumn("description"));
                    }
                }
            }
            if(relationship.getRelationshipDefinition().getId()==77){
                for (RelationshipAttribute attribute: relationship.getRelationshipAttributes()) {
                    if(attribute.getRelationAttributeDefinition().getId()==16){
                        pack = relationship.getTarget().toString()+" "+(((HelperTableRecord)attribute.getTarget()).getValueColumn("description"));
                    }
                }
            }
            if(relationship.getRelationshipDefinition().getId()==93){
                for (RelationshipAttribute attribute: relationship.getRelationshipAttributes()) {
                    if(attribute.getRelationAttributeDefinition().getId()==17){
                        vol = relationship.getTarget().toString()+" "+(((HelperTableRecord)attribute.getTarget()).getValueColumn("description"));
                    }
                }
            }
        }

        autogeneratePCCE= cantidad+" "+pack+ " "+vol;
    }

    public String getAutogeneratePCCE() {
        return autogeneratePCCE;
    }

    public void setAutogeneratePCCE(String autogeneratePCCE) {
        this.autogeneratePCCE = autogeneratePCCE;
    }

    public String getPc() {
        return (pc!=null)?pc:"";
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    @Override
    public String toString() {
        return getPc()+" "+autogeneratePCCE;
    }
}
