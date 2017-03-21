package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 21-03-17.
 */
public class ConceptDTO extends ConceptSMTK {

    private List<RelationshipDTO> relationshipsDTO;

    public ConceptDTO(ConceptSMTK concept) {
        super(concept.getId(), concept.getConceptID(), concept.getCategory(), concept.isToBeReviewed(),
              concept.isToBeConsulted(), concept.isModeled(), concept.isFullyDefined(), concept.isInherited(),
                concept.isPublished(), concept.getObservation(), concept.getTagSMTK());
        super.setDescriptions(concept.getDescriptions());
    }

    public List<RelationshipDTO> getRelationshipsDTO() {
        return relationshipsDTO;
    }

    public void setRelationshipsDTO(List<RelationshipDTO> relationshipsDTO) {
        this.relationshipsDTO = relationshipsDTO;
    }

    public List<RelationshipDTO> getRelationshipsDTOByName(String name) {
        List<RelationshipDTO> someRelationships = new ArrayList<>();
        for (RelationshipDTO relationship : relationshipsDTO) {
            if (relationship.getName().equals(name)) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }
}
