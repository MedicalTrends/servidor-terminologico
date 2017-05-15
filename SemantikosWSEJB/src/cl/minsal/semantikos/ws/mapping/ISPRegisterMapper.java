package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.modelws.response.ISPRegisterResponse;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by Development on 2016-12-30.
 *
 */
public class ISPRegisterMapper {

    public static ISPRegisterResponse map(@NotNull Relationship relationship) {
        if ( !relationship.getRelationshipDefinition().isBioequivalente() && !relationship.getRelationshipDefinition().isISP() ) {
            throw new IllegalArgumentException("Solo se permiten mapear relacioens Bioequivalente e ISP");
        }

        ISPRegisterResponse res = new ISPRegisterResponse();

        HelperTableRow helperTableRecord = (HelperTableRow)relationship.getTarget();
        Map<String, String> values = new HashMap<>();

        for (HelperTableData helperTableData : helperTableRecord.getCells()) {
            HelperTableColumn column = helperTableData.getColumn();
            values.put(column.getName(), helperTableRecord.getColumnValue(column));
        }

        res.setRegistro(values.get("REGISTRO"));
        res.setName(values.get("NOMBRE"));
        res.setDescription(values.get(helperTableRecord.getDescription()));
        res.setValid(values.get(helperTableRecord.isValid()));
        res.setValidityUntil(helperTableRecord.getValidityUntil()!=null?helperTableRecord.getValidityUntil().toString():EMPTY_STRING);
        res.setEstadoDelRegistro(values.get("ESTADO_REGISTRO"));
        res.setTitular(values.get("TITULAR"));
        res.setEquivalenciaTerapeutica(values.get("EQ_TERAPEUTICA"));
        res.setResolucionInscribase(values.get("RESOLUCION"));
        res.setFechaIngreso(helperTableRecord.getCreationDate().toString());
        res.setFechaInscribase(values.get("FEC_INS_BASE"));
        res.setUltimaRenovacion(values.get("FEC_ULT_RENOV"));
        res.setRegimen(values.get("REGIMEN"));
        res.setViaAdministracion(values.get("VIA_ADMINISTRACION"));
        res.setCondicionDeVenta(values.get("CONDICION_VENTA"));
        res.setExpendeTipoEstablecimiento(values.get("EXP_TIPO_ESTAB"));
        res.setIndicacion(values.get("INDICACION"));

        return res;
    }

}
