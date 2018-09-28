package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.daos.ExtendedRelationshipAttributeDefinitionInfo;
import cl.minsal.semantikos.kernel.daos.ExtendedRelationshipDefinitionInfo;
import cl.minsal.semantikos.kernel.daos.SemantikosWebDAO;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.users.Roles;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipAttributeDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * @author Andrés Farías on 10/5/16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class ViewAugmenterImpl implements ViewAugmenter {

    @EJB
    private SemantikosWebDAO semantikosWebDAO;

    @EJB
    private HelperTablesManager helperTablesManager;


    private static final Logger logger = LoggerFactory.getLogger(ViewAugmenterImpl.class);

    private Map<Long, RelationshipDefinitionWeb> relationshipDefinitiosnWeb = new HashMap<>();

    @Override
    @PermitAll
    public RelationshipDefinitionWeb augmentRelationshipDefinition(Category category, RelationshipDefinition relDef) {

        ExtendedRelationshipDefinitionInfo extendedRelationshipDefinitionInfo = semantikosWebDAO.getCompositeOf(category, relDef);
        RelationshipDefinitionWeb relationshipDefinitionWeb =
                new RelationshipDefinitionWeb(relDef.getId(), relDef.getName(), relDef.getDescription(), relDef.getTargetDefinition(), relDef.getMultiplicity(), extendedRelationshipDefinitionInfo.getIdComposite(), extendedRelationshipDefinitionInfo.getOrder());
        relationshipDefinitionWeb.setRelationshipAttributeDefinitions(relDef.getRelationshipAttributeDefinitions());
        relationshipDefinitionWeb.setDefaultValue(extendedRelationshipDefinitionInfo.getDefaultValue());
        relationshipDefinitionWeb.setAutogenerate(extendedRelationshipDefinitionInfo.isAutogenerate());
        List<RelationshipAttributeDefinitionWeb> attributeDefinitionWebs = new ArrayList<>();

        for (RelationshipAttributeDefinition relationshipAttributeDefinition : relDef.getRelationshipAttributeDefinitions()) {
            ExtendedRelationshipAttributeDefinitionInfo extendedAttributeDefinitionInfo = semantikosWebDAO.getCompositeOf(category,relationshipAttributeDefinition);
            RelationshipAttributeDefinitionWeb relationshipAttributeDefinitionWeb = new RelationshipAttributeDefinitionWeb(relationshipAttributeDefinition.getId(),relationshipAttributeDefinition.getTargetDefinition(),relationshipAttributeDefinition.getName(),relationshipAttributeDefinition.getMultiplicity(),extendedAttributeDefinitionInfo.getIdComposite(),extendedAttributeDefinitionInfo.getOrder(),relationshipAttributeDefinition);
            relationshipAttributeDefinitionWeb.setDefaultValue(extendedAttributeDefinitionInfo.getDefaultValue());
            attributeDefinitionWebs.add(relationshipAttributeDefinitionWeb);
        }

        relationshipDefinitionWeb.setRelationshipAttributeDefinitionWebs(attributeDefinitionWebs);

        return relationshipDefinitionWeb;
    }

    @Override
    @PermitAll
    public ConceptSMTKWeb augmentConcept(ConceptSMTKWeb concept, List<RelationshipDefinitionWeb> relationshipDefinitionsWeb) {
        ConceptSMTKWeb conceptSMTKWeb = semantikosWebDAO.augmentConcept(concept);

        for (RelationshipDefinitionWeb relationshipDefinitionWeb : relationshipDefinitionsWeb) {

            if (!concept.isPersistent() && relationshipDefinitionWeb.hasDefaultValue()) {
                concept.initRelationship(relationshipDefinitionWeb);
            }
        }

        return conceptSMTKWeb;

    }

    @Override
    @PermitAll
    public Map<Long, Relationship> augmentRelationships(Category category, Map<Long, Relationship> relationshipPlaceholders) {

        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

            if (!relationshipDefinition.getRelationshipAttributeDefinitions().isEmpty() && relationshipDefinition.getMultiplicity().isCollection()) {

                if(!relationshipDefinitiosnWeb.containsKey(relationshipDefinition.getId())) {
                    relationshipDefinitiosnWeb.put(relationshipDefinition.getId(), augmentRelationshipDefinition(category, relationshipDefinition));
                }

                RelationshipDefinitionWeb relationshipDefinitionWeb = relationshipDefinitiosnWeb.get(relationshipDefinition.getId());

                Relationship r = relationshipPlaceholders.get(relationshipDefinition.getId());


                for (RelationshipAttributeDefinitionWeb relAttrDefWeb : relationshipDefinitionWeb.getRelationshipAttributeDefinitionWebs()) {
                    if(relAttrDefWeb.getDefaultValue()!=null) {
                        RelationshipAttribute ra = new RelationshipAttribute(relAttrDefWeb.getRelationshipAttributeDefinition(), r, relAttrDefWeb.getDefaultValue());
                        r.getRelationshipAttributes().add(ra);
                    }
                }

                //relationshipPlaceholders.put(relationshipDefinition.getId(), r);

            }
        }

        return relationshipPlaceholders;
    }
}
