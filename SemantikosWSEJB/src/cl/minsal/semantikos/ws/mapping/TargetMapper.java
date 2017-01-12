package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.ws.response.ConceptResponse;
import cl.minsal.semantikos.ws.response.TargetResponse;

import java.util.Date;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class TargetMapper {

    public static TargetResponse map(Target target) {
        if ( target != null ) {
            TargetResponse res = new TargetResponse();

            if ( target.getTargetType() != null ) {
                res.setType(String.valueOf(target.getTargetType()));
            }

            if (target instanceof BasicTypeValue) {
                BasicTypeValue basicTypeValue = (BasicTypeValue) target;
                if (basicTypeValue.getValue() != null) {
                    res.setValue(String.valueOf(basicTypeValue.getValue()));
                }
            }

            if (target instanceof ConceptSCT) {
                ConceptSCT conceptSCT = (ConceptSCT) target;
                res.setEffectiveTime(new Date(conceptSCT.getEffectiveTime().getTime()));
                res.setActive(conceptSCT.isActive());
                res.setModuleId(conceptSCT.getModuleId());
//                res.setDefinitionStatusId(conceptSCT.getDefinitionStatusId());
            }

            if (target instanceof ConceptSMTK) {
                ConceptSMTK conceptSMTK = (ConceptSMTK) target;
                ConceptResponse conceptResponse = new ConceptResponse(conceptSMTK);
                ConceptMapper.appendPreferredDescriptions(conceptResponse, conceptSMTK);
                res.setConcept(conceptResponse);
            }

            if (target instanceof Crossmap) {
                Crossmap crossMap = (Crossmap) target;
                res.setRelationship(RelationshipMapper.map(crossMap));
            }

            if (target instanceof HelperTableRecord) {
                HelperTableRecord helperTableRecord = (HelperTableRecord) target;
                res.setHelperTableResponse(HelperTableMapper.map(helperTableRecord.getHelperTable()));
                res.setFields(helperTableRecord.getFields());
            }

            return res;
        }
        return null;
    }

}
