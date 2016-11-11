package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.CrossMap;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.ws.response.RelationshipResponse;
import cl.minsal.semantikos.ws.response.TargetResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class TargetMapper {

    public static TargetResponse map(Target target) {
        if ( target != null ) {
            TargetResponse res = new TargetResponse();
            res.setTargetTypeResponse(TargetTypeMapper.map(target.getTargetType()));

            if (target instanceof BasicTypeValue) {
                BasicTypeValue basicTypeValue = (BasicTypeValue) target;
                if (basicTypeValue.getValue() != null) {
                    res.setValue(String.valueOf(basicTypeValue.getValue()));
                }
            }
            if (target instanceof ConceptSCT) {
                ConceptSCT conceptSCT = (ConceptSCT) target;
                res.setEffectiveTime(MappingUtil.toDate(conceptSCT.getEffectiveTime()));
                res.setActive(conceptSCT.isActive());
                res.setModuleId(conceptSCT.getModuleId());
                res.setDefinitionStatusId(conceptSCT.getDefinitionStatusId());
            }
            if (target instanceof ConceptSMTK) {
                ConceptSMTK conceptSMTK = (ConceptSMTK) target;
                res.setConcept(ConceptMapper.map(conceptSMTK));
            }
            if (target instanceof CrossMap) {
                CrossMap crossMap = (CrossMap) target;
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
