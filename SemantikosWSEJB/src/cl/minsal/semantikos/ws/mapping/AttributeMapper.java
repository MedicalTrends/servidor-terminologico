package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.ws.modelws.response.AttributeResponse;

/**
 * Created by Development on 2016-10-20.
 *
 */
public class AttributeMapper {

    public static AttributeResponse map(Relationship relationship) {
        if ( relationship != null ) {
            AttributeResponse res = new AttributeResponse();

            if(relationship.getTarget() instanceof ConceptSCT ||
                relationship.getTarget() instanceof CrossmapSetMember) {
                return null;
            }

            if ( relationship.getTarget() != null && relationship.getTarget() instanceof BasicTypeValue ) {
                BasicTypeValue btv = (BasicTypeValue) relationship.getTarget();
                res.setName(relationship.getRelationshipDefinition().getName());
                res.setValue(String.valueOf(btv.getValue()));
                res.setType("Tipo Básico");
            }
            if ( relationship.getTarget() != null && relationship.getTarget() instanceof ConceptSMTK) {
                ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                res.setName(relationship.getRelationshipDefinition().getName());
                res.setValue(conceptSMTK.toString());
                res.setType("Concepto SMTK");
            }
            if ( relationship.getTarget() != null && relationship.getTarget() instanceof HelperTableRow) {
                HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();
                res.setName(helperTableRow.toString());
                res.setValue(String.valueOf(helperTableRow.getId()));
                res.setType("Tabla Auxiliar - "+relationship.getRelationshipDefinition().getName());
            }

            for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

                AttributeResponse res2 = new AttributeResponse();

                if ( relationshipAttribute.getTarget() != null && relationshipAttribute.getTarget() instanceof BasicTypeValue ) {
                    BasicTypeValue btv = (BasicTypeValue) relationshipAttribute.getTarget();
                    res2.setName(relationshipAttribute.getRelationAttributeDefinition().getName());
                    res2.setValue(String.valueOf(btv.getValue()));
                    res2.setType("Tipo Básico");
                }
                if ( relationshipAttribute.getTarget() != null && relationshipAttribute.getTarget() instanceof ConceptSMTK) {
                    ConceptSMTK conceptSMTK = (ConceptSMTK) relationshipAttribute.getTarget();
                    res2.setName(relationshipAttribute.getRelationAttributeDefinition().getName());
                    res2.setValue(conceptSMTK.toString());
                    res2.setType("Concepto SMTK");
                }
                if ( relationshipAttribute.getTarget() != null && relationshipAttribute.getTarget() instanceof HelperTableRow) {
                    HelperTableRow helperTableRow = (HelperTableRow) relationshipAttribute.getTarget();
                    res2.setName(helperTableRow.toString());
                    res2.setValue(String.valueOf(helperTableRow.getId()));
                    res2.setType("Tabla Auxiliar - "+relationshipAttribute.getRelationAttributeDefinition().getName());
                }
                res.getAttributeResponses().add(res2);
            }

            return res;
        }
        return null;
    }

}
