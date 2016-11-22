package cl.minsal.semantikos.view.components;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.ConceptSMTKWeb;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinitionWeb;
import cl.minsal.semantikos.view.daos.ExtendedRelationshipDefinitionInfo;
import cl.minsal.semantikos.view.daos.SemantikosWebDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Andrés Farías on 10/5/16.
 */
@Stateless
public class ViewAugmenterImpl implements ViewAugmenter {

    @EJB
    private SemantikosWebDAO semantikosWebDAO;

    @Override
    public RelationshipDefinitionWeb augmentRelationshipDefinition(Category category, RelationshipDefinition relDef) {

        ExtendedRelationshipDefinitionInfo extendedRelationshipDefinitionInfo = semantikosWebDAO.getCompositeOf(category, relDef);
        RelationshipDefinitionWeb relationshipDefinitionWeb = new RelationshipDefinitionWeb(relDef.getId(), relDef.getName(), relDef.getDescription(), relDef.getTargetDefinition(), relDef.getMultiplicity(), extendedRelationshipDefinitionInfo.getIdComposite(), extendedRelationshipDefinitionInfo.getOrder());
        relationshipDefinitionWeb.setRelationshipAttributeDefinitions(relDef.getRelationshipAttributeDefinitions());
        relationshipDefinitionWeb.setDefaultValue(extendedRelationshipDefinitionInfo.getDefaultValue());

        return relationshipDefinitionWeb;
    }

    @Override
    public ConceptSMTKWeb augmentConcept(Category category, ConceptSMTKWeb concept) {
        return semantikosWebDAO.augmentConcept(category, concept);
    }
}
