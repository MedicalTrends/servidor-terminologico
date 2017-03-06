package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.ws.response.AttributeResponse;

/**
 * Created by Development on 2016-10-20.
 *
 */
public class AttributeMapper {

    public static AttributeResponse map(Relationship relationship) {
        if ( relationship != null ) {
            AttributeResponse res = new AttributeResponse();

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
                res.setName(relationship.getRelationshipDefinition().getName());
                res.setValue(helperTableRow.toString());
                res.setType("Registro en Tabla Auxiliar");
            }

            return res;
        }
        return null;
    }

}
