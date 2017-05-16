package cl.minsal.semantikos.modelweb;


import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.Target;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RelationshipWeb extends Relationship implements Comparable<RelationshipWeb>, Serializable {

    public boolean hasBeenModified;


    public RelationshipWeb(Relationship r) {
        super(r.getSourceConcept(),  r.getRelationshipDefinition(), new ArrayList<RelationshipAttribute>());
        if(r.getTarget() != null)
            this.setTarget(r.getTarget().copy());
        this.hasBeenModified = false;

    }

    public RelationshipWeb(Relationship r, List<RelationshipAttribute> ra){
        this(r);
        for (RelationshipAttribute attr : ra) {
            this.getRelationshipAttributes().add(new RelationshipAttribute(attr.getIdRelationshipAttribute(), attr.getRelationAttributeDefinition(), attr.getRelationship(), attr.getTarget()));
        }
    }

    public RelationshipWeb(long id, Relationship r) {
        this(r);
        this.setId(id);
    }

    public RelationshipWeb(long id, Relationship r, List<RelationshipAttribute> ra) {
        this(r, ra);
        this.setId(id);
    }

    public RelationshipWeb(ConceptSMTKWeb concept, long id, Relationship r) {
        this(id, r);
        super.setSourceConcept(concept);
    }

    public RelationshipWeb(ConceptSMTKWeb concept, long id, Relationship r, List<RelationshipAttribute> ra) {
        this(id, r, ra);
        super.setSourceConcept(concept);
    }

    public RelationshipWeb(ConceptSMTKWeb concept, long id, Relationship r, List<RelationshipAttribute> ra, Timestamp creationDate) {
        this(concept, id, r, ra);
        super.setCreationDate(creationDate);
    }

    @Override
    public void setTarget(Target target) {
        if (target != null) {
            super.setTarget(target);
        }
    }

    public boolean hasBeenModified() {
        return hasBeenModified;
    }

    public void setModified(boolean hasBeenModified) {
        this.hasBeenModified = hasBeenModified;
    }

    public RelationshipAttribute getAttributeById(long idRelationshipAttribute){
        for (RelationshipAttribute attribute : getRelationshipAttributes()) {
            if(attribute.getIdRelationshipAttribute() == idRelationshipAttribute)
                return attribute;
        }
        return null;
    }

    public Relationship toRelationship(){
        return new Relationship(this.getSourceConcept(), this.getTarget(), this.getRelationshipDefinition(), this.getRelationshipAttributes(), null);
    }

    public String getDateCreationFormat() {
        if(this.getCreationDate()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(this.getCreationDate());
        }return "";

    }

    @Override
    public int compareTo(RelationshipWeb o) {
        return this.getOrder() - o.getOrder();
    }

    public RelationshipAttribute getAttribute(RelationshipAttributeDefinition definition) {
        for (RelationshipAttribute attribute : getRelationshipAttributes()) {
            if (definition.equals(attribute.getRelationAttributeDefinition()))
                return attribute;
        }

        return null;
    }
}
