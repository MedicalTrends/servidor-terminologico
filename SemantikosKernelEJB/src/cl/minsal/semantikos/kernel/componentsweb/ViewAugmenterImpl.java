package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.daos.ExtendedRelationshipAttributeDefinitionInfo;
import cl.minsal.semantikos.kernel.daos.ExtendedRelationshipDefinitionInfo;
import cl.minsal.semantikos.kernel.daos.SemantikosWebDAO;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipAttributeDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ViewAugmenterImpl implements ViewAugmenter {

    @EJB
    private SemantikosWebDAO semantikosWebDAO;

    @EJB
    private HelperTablesManager helperTablesManager;


    private static final Logger logger = LoggerFactory.getLogger(ViewAugmenterImpl.class);

    private Map<Long, RelationshipDefinitionWeb> relationshipDefinitiosnWeb = new HashMap<>();

    @Override
    public RelationshipDefinitionWeb augmentRelationshipDefinition(Category category, RelationshipDefinition relDef) {

        ExtendedRelationshipDefinitionInfo extendedRelationshipDefinitionInfo = semantikosWebDAO.getCompositeOf(category, relDef);
        RelationshipDefinitionWeb relationshipDefinitionWeb =
                new RelationshipDefinitionWeb(relDef.getId(), relDef.getName(), relDef.getDescription(), relDef.getTargetDefinition(), relDef.getMultiplicity(), extendedRelationshipDefinitionInfo.getIdComposite(), extendedRelationshipDefinitionInfo.getOrder());
        relationshipDefinitionWeb.setRelationshipAttributeDefinitions(relDef.getRelationshipAttributeDefinitions());
        relationshipDefinitionWeb.setDefaultValue(extendedRelationshipDefinitionInfo.getDefaultValue());
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
    public ConceptSMTKWeb augmentConcept(Category category, ConceptSMTKWeb concept) {
        ConceptSMTKWeb conceptSMTKWeb = semantikosWebDAO.augmentConcept(category, concept);

        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

            if(!relationshipDefinitiosnWeb.containsKey(relationshipDefinition.getId())) {
                relationshipDefinitiosnWeb.put(relationshipDefinition.getId(), augmentRelationshipDefinition(category, relationshipDefinition));
            }

            RelationshipDefinitionWeb relationshipDefinitionWeb = relationshipDefinitiosnWeb.get(relationshipDefinition.getId());

            if (!concept.isPersistent() && relationshipDefinitionWeb.hasDefaultValue()) {
                concept.initRelationship(relationshipDefinitionWeb);
            }
        }

        return conceptSMTKWeb;

    }

    @Override
    public Map<Long, Relationship> augmentRelationships(Category category, ConceptSMTKWeb concept, Map<Long, Relationship> relationshipPlaceholders) {

        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

            if (!relationshipDefinition.getRelationshipAttributeDefinitions().isEmpty() && relationshipDefinition.getMultiplicity().isCollection()) {

                if(!relationshipDefinitiosnWeb.containsKey(relationshipDefinition.getId())) {
                    relationshipDefinitiosnWeb.put(relationshipDefinition.getId(), augmentRelationshipDefinition(category, relationshipDefinition));
                }

                RelationshipDefinitionWeb relationshipDefinitionWeb = relationshipDefinitiosnWeb.get(relationshipDefinition.getId());

                Relationship r = new Relationship(concept, null, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                for (RelationshipAttributeDefinitionWeb relAttrDefWeb : relationshipDefinitionWeb.getRelationshipAttributeDefinitionWebs()) {
                    if(relAttrDefWeb.getDefaultValue()!=null) {
                        RelationshipAttribute ra = new RelationshipAttribute(relAttrDefWeb.getRelationshipAttributeDefinition(), r, relAttrDefWeb.getDefaultValue());
                        r.getRelationshipAttributes().add(ra);
                    }
                }

                relationshipPlaceholders.put(relationshipDefinition.getId(), r);

                // Si esta definición de relación es de tipo CROSSMAP, Se agrega el atributo tipo de relacion = "ES_UN_MAPEO_DE" (por defecto)
                if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
                    for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {
                        if (attDef.isRelationshipTypeAttribute()) {
                            Relationship rel = relationshipPlaceholders.get(relationshipDefinition.getId());
                            HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                            List<HelperTableRow> relationshipTypes = helperTablesManager.searchRows(helperTable, ES_UN_MAPEO_DE);

                            RelationshipAttribute ra;

                            if (relationshipTypes.size() == 0) {
                                logger.error("No hay datos en la tabla de TIPOS DE RELACIONES.");
                            }

                            ra = new RelationshipAttribute(attDef, rel, relationshipTypes.get(0));
                            rel.getRelationshipAttributes().add(ra);
                        }
                    }
                }
            }
        }

        return relationshipPlaceholders;
    }
}
