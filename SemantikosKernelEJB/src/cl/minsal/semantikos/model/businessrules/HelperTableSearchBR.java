package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.helpertables.HelperTableFactory.HT_ATC_NAME;

/**
 * @author Andrés Farías on 11/11/16.
 */
public class HelperTableSearchBR {

    /** Mínima cantidad de caracteres en el patrón de búsqueda en tablas auxiliares */
    public static final short MINIMUM_PATTERN_LENGTH = 1;

    /**
     * Método para realizar las validaciones.
     *
     * @param helperTable La tabla Auxiliar que se desea validar.
     * @param columnName  El nombre de la columna en la que se desea realizar la búsqueda.
     * @param pattern     El patrón de búsqueda.
     *
     * @throws cl.minsal.semantikos.model.exceptions.BusinessRuleException Si alguna regla de negocio no se cumple.
     */
    public void validatePreConditions(HelperTable helperTable, String columnName, String pattern) {

        /* El patrón de búsqueda sobre la columna debe ser mayor a dos caracteres */
        precondition03(pattern);
    }

    /**
     * El patrón de búsqueda debe ser de al menos dos caracteres.
     *
     * @param pattern El patrón de búsqueda.
     */
    protected void precondition03(String pattern) {
        if ((pattern == null) || pattern.length() < MINIMUM_PATTERN_LENGTH) {
            throw new BusinessRuleException("BR-HT-PC03", "El patrón de búsqueda sobre tablas auxiliares debe tener un largo mínimo de " + MINIMUM_PATTERN_LENGTH + " caracteres.");
        }
    }

    /**
     * <p>Este método es responsable de implementar la regla de negocio:</p>
     * <b>BR-HT-PA01</b>: Los elementos de las tabla auxiliar deben ser ordenados alfabéticamente, excepto por la tabla
     * HT_ATC_NAME que se ordena por el largo de los resultados.
     *
     * @param records Los registros que se desea ordenar.
     */
    public void applyPostActions(@NotNull List<HelperTableRecord> records) {

        /* Se ordenan los resultados */
        postActionsortCollections(records);
    }

    private void postActionsortCollections(List<HelperTableRecord> records) {

        /* Las listas vacías no requieren ser ordenadas */
        if (records == null || records.isEmpty()){
            return;
        }

        /* Si la lista de registros es de la tabla HT_ATC_NAME, el ordenamiento es especial */
        HelperTableRecord helperTableRecord = records.get(0);
        if (helperTableRecord.getHelperTable().getName().equals(HT_ATC_NAME)){
            Collections.sort(records, new ATCRecordComparator());
        }

        /* Sino, se ordena alfabéticamente */
        Collections.sort(records);
    }

    class ATCRecordComparator implements Comparator<HelperTableRecord> {

        @Override
        public int compare(HelperTableRecord atc1, HelperTableRecord atc2) {

            Map<String, String> fieldsATC1 = atc1.getFields();
            Map<String, String> fieldsATC2 = atc2.getFields();

            String recordATC1 = fieldsATC1.get("DESCRIPTION").concat(fieldsATC1.get("DCC_COMPLETA_ATC"));
            String recordATC2 = fieldsATC2.get("DESCRIPTION").concat(fieldsATC2.get("DCC_COMPLETA_ATC"));

            return recordATC1.length() - recordATC2.length();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}


