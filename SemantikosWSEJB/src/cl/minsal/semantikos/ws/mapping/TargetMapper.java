package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.modelws.response.ConceptResponse;
import cl.minsal.semantikos.modelws.response.TargetResponse;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alfonsito on 2016-10-14.
 */
public class TargetMapper {

    public static TargetResponse map(@NotNull Target target, TargetDefinition targetDefinition) {
        TargetResponse res = new TargetResponse();

        if (target.getTargetType() != null) {
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
            res.setRelationship(RelationshipMapper.map(crossMap, targetDefinition));
        }

        if (target instanceof HelperTableRow) {
            HelperTableRow helperTableRecord = (HelperTableRow) target;

            res.setHelperTableResponse(HelperTableMapper.map((HelperTable)targetDefinition));

            Map<String, String> fields = new HashMap<>();
            for (HelperTableData helperTableData : helperTableRecord.getCells()) {
                HelperTableColumn column = helperTableData.getColumn();
                fields.put(column.getName(), helperTableRecord.getColumnValue(column));
            }
            res.setFields(fields);
        }

        return res;
    }

}
