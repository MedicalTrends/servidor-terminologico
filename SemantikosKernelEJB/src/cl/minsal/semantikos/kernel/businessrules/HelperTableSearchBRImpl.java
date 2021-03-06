package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumnFactory;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;

import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author Andrés Farías on 11/11/16.
 */
@Singleton
public class HelperTableSearchBRImpl implements HelperTableSearchBR {

    /** Mínima cantidad de caracteres en el patrón de búsqueda en tablas auxiliares */
    public static final short ATC_MINIMUM_PATTERN_LENGTH = 3;
    public static final short MINIMUM_PATTERN_LENGTH = 1;
    public static final long HT_ATC_ID = 14;

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

        /* El nombre de la columna debe ser un nombre válido */
        precondition02(columnName);
        /* El patrón de búsqueda sobre la columna debe ser mayor a dos caracteres */
        //precondition03(pattern);
    }

    /**
     * El patrón de búsqueda debe ser de al menos dos caracteres.
     *
     * @param columnName El nombre de la columna.
     */
    protected void precondition02(String columnName) {

        if(HelperTableColumnFactory.getInstance().findColumnByName(columnName)==null) {
            throw new BusinessRuleException("BR-HT-PC02", "El nombre de columna proporcionado '"+columnName+"' no corresponde a ninguno de los nombres de columnas existentes en el sistema");
        }
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
    public void applyPostActions(@NotNull List<HelperTableRow> records) {

        /* Se ordenan los resultados */
        postActionsortCollections(records);
    }

    private void postActionsortCollections(List<HelperTableRow> rows) {

        /* Las listas vacías no requieren ser ordenadas */
        if (rows == null || rows.isEmpty()){
            return;
        }

        /* Si la lista de registros es de la tabla HT_ATC_NAME, el ordenamiento es especial */
        HelperTableRow helperTableRow = rows.get(0);

        //Collections.sort(rows, new ATCRecordComparator());

        if (helperTableRow.getHelperTableId()==HT_ATC_ID) {
            Collections.sort(rows, new ATCRecordComparator());
        }
        else{
            // Sino, se ordena por el largo de la descripcion
            // Sino, se ordena alfabéticamente
            Collections.sort(rows, new DefaultRecordComparator());
        }
    }

    public int getMinQueryLength(HelperTable helperTable) {

        if(helperTable.getId() == HT_ATC_ID) {
            return ATC_MINIMUM_PATTERN_LENGTH;
        }
        else {
            return MINIMUM_PATTERN_LENGTH;
        }
    }

    class ATCRecordComparator implements Comparator<HelperTableRow> {

        @Override
        public int compare(HelperTableRow atc1, HelperTableRow atc2) {

            return atc1.getCellByColumnName("descripcion completa").toString().length() -
                   atc2.getCellByColumnName("descripcion completa").toString().length();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }


    class DefaultRecordComparator implements Comparator<HelperTableRow> {

        @Override
        public int compare(HelperTableRow row1, HelperTableRow row2) {
            return row1.getDescription().length() - row2.getDescription().length();
            //return row1.getDescription().compareTo(row2.getDescription());
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}


