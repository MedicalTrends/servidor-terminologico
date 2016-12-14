package cl.minsal.semantikos.beans.relationship;

import cl.minsal.semantikos.kernel.components.HelperTableManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTKWeb;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.view.components.ViewAugmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * Created by des01c7 on 14-12-16.
 */

@javax.faces.bean.ManagedBean(name = "relationshipPlaceholderBean")
@ViewScoped
public class RelationshipPlaceholderBean {

    @EJB
    private HelperTableManager helperTableManager;

     private static final Logger logger = LoggerFactory.getLogger(RelationshipPlaceholderBean.class);

    public void initPlaceholder(ConceptSMTKWeb concept, Category category, Map<Long,Relationship> relationshipPlaceholders, ViewAugmenter viewAugmenter){
        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {
            RelationshipDefinitionWeb relationshipDefinitionWeb = viewAugmenter.augmentRelationshipDefinition(category, relationshipDefinition);

            if (!concept.isPersistent() && relationshipDefinitionWeb.hasDefaultValue())
                concept.initRelationship(relationshipDefinitionWeb);

            if (!relationshipDefinition.getRelationshipAttributeDefinitions().isEmpty()) {
                relationshipPlaceholders.put(relationshipDefinition.getId(), new Relationship(concept, null, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null));

                // Si esta definición de relación es de tipo CROSSMAP, Se agrega el atributo tipo de relacion = "ES_UN_MAPEO_DE" (por defecto)
                if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
                    for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {
                        if (attDef.isRelationshipTypeAttribute()) {
                            Relationship r = relationshipPlaceholders.get(relationshipDefinition.getId());
                            HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();
                            String[] columnNames = {HelperTable.SYSTEM_COLUMN_DESCRIPTION.getColumnName()};

                            List<HelperTableRecord> relationshipTypes = helperTableManager.searchRecords(helperTable, Arrays.asList(columnNames), ES_UN_MAPEO_DE, true);
                            RelationshipAttribute ra;
                            if (relationshipTypes.size() == 0) {
                                logger.error("No hay datos en la tabla de TIPOS DE RELACIONES.");
                            }

                            ra = new RelationshipAttribute(attDef, r, relationshipTypes.get(0));
                            r.getRelationshipAttributes().add(ra);
                        }
                    }
                }
            }
        }
    }
}
