package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.modelws.response.BioequivalentResponse;
import cl.minsal.semantikos.modelws.response.ConceptLightResponse;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by Development on 2016-12-30.
 *
 */
public class BioequivalentMapper {

    /*
    public static BioequivalentResponse map(@NotNull Relationship relationship, List<Relationship> relationshipsLike) {
        if ( !relationship.getRelationshipDefinition().isBioequivalente() ) {
            throw new IllegalArgumentException("Solo se permiten mapear relacioens Bioequivalente");
        }

        BioequivalentResponse res = new BioequivalentResponse();

        HelperTableRow helperTableRecord = (HelperTableRow)relationship.getTarget();
        Map<String, String> values = new HashMap<>();

        for (HelperTableData helperTableData : helperTableRecord.getCells()) {
            HelperTableColumn column = helperTableData.getColumn();
            values.put(column.getName(), helperTableRecord.getColumnValue(column));
        }

        for (Relationship r : relationshipsLike) {
            res.setProductoComercial(new ConceptLightResponse(r.getSourceConcept()));
        }

        return res;
    }
    */

    public static BioequivalentResponse map(@NotNull Relationship relationship, List<ConceptSMTK> bioequivalents) {
        if ( !relationship.getRelationshipDefinition().isBioequivalente() ) {
            throw new IllegalArgumentException("Solo se permiten mapear relacioens Bioequivalente");
        }

        BioequivalentResponse res = new BioequivalentResponse();

        HelperTableRow helperTableRecord = (HelperTableRow)relationship.getTarget();
        Map<String, String> values = new HashMap<>();

        for (HelperTableData helperTableData : helperTableRecord.getCells()) {
            HelperTableColumn column = helperTableData.getColumn();
            values.put(column.getName(), helperTableRecord.getColumnValue(column));
        }

        for (ConceptSMTK bioequivalent : bioequivalents) {
            res.setProductoComercial(new ConceptLightResponse(bioequivalent));
        }

        return res;
    }

}
