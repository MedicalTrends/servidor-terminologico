package cl.minsal.semantikos;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import java.util.List;

/**
 * Created by des01c7 on 05-01-17.
 */
public class CompoundSpecialty {

    private static long RELATIONSHIP_DEFINITION_SPECIALTY=105;
    private static long ATTRIBUTE_DEFINITION_SUBSPECIALTY=30;

    public static boolean existCompounSpeciality(List<Relationship> relationships, Relationship relationshipAdd){

        if(relationshipAdd.getRelationshipDefinition().getId()==RELATIONSHIP_DEFINITION_SPECIALTY){
            for (Relationship relationship : relationships) {
                if(relationship.getRelationshipDefinition().getId()==relationshipAdd.getRelationshipDefinition().getId()){
                    if(relationshipAdd.getTarget().equals(relationship.getTarget()) && relationshipAdd.getRelationshipAttributes().size()== relationship.getRelationshipAttributes().size()){
                        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
                            if(relationshipAttribute.getRelationAttributeDefinition().getId()==ATTRIBUTE_DEFINITION_SUBSPECIALTY){
                                if(relationshipAdd.getAttribute(relationshipAttribute.getRelationAttributeDefinition()).getTarget().equals(relationshipAttribute.getTarget())){
                                    return true;
                                }return false;
                            }
                        }return true;
                    }
                }
            }
        }
        return false;
    }
}
