package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class HelperTableSearchBRTest {

    private HelperTableSearchBR helperTableSearchBR = new HelperTableSearchBR();

    public static final String A_SEARCHABLE_COLUMN_NAME = "A_SEARCHABLE_COLUMN_NAME";
    public static final String A_NON_SEARCHABLE_COLUMN_NAME = "A_NON_SEARCHABLE_COLUMN_NAME";

    /**
     * Este método es responsable de crear una tabla auxiliar con columnas básicas.
     *
     * @return Un objeto Tabla Auxiliar no persistido.
     */
    private HelperTable dummyHelperTable() {


        Collection<HelperTableColumn> columns = new ArrayList<>();
        columns.add(new HelperTableColumn(A_SEARCHABLE_COLUMN_NAME, false, true, false));
        columns.add(new HelperTableColumn(A_NON_SEARCHABLE_COLUMN_NAME, false, false, false));
        return new HelperTable("HT Dummy", "Dummy", "dummy", columns);
    }

    /**
     * Este método valida que no es posible utilizar patrón de búsqueda de menos del mínimo caracteres.
     *
     * @throws Exception
     */
    @Test
    public void testPrecondition03_01() throws Exception {
        helperTableSearchBR.precondition03("abc");
        helperTableSearchBR.precondition03("abcd");
    }

    /**
     * Este método valida que no es posible utilizar patrón de búsqueda de menos del mínimo caracteres.
     *
     * @throws Exception
     */
    @Test(expected = BusinessRuleException.class)
    public void testPrecondition03_02() throws Exception {
        helperTableSearchBR.precondition03(null);
    }

    /**
     * Este método valida que no es posible utilizar patrón de búsqueda de menos del mínimo caracteres.
     *
     * @throws Exception
     */
    @Test(expected = BusinessRuleException.class)
    public void testPrecondition03_03() throws Exception {
        helperTableSearchBR.precondition03("");
    }

    /**
     * Este método valida que no es posible utilizar patrón de búsqueda de menos del mínimo caracteres.
     *
     * @throws Exception
     */
    @Test(expected = BusinessRuleException.class)
    public void testPrecondition03_04() throws Exception {
        helperTableSearchBR.precondition03("a");
    }

    @Test
    public void testValidatePreConditions_01() throws Exception {
        helperTableSearchBR.validatePreConditions(dummyHelperTable(), A_SEARCHABLE_COLUMN_NAME, "buscar");
    }

    @Test(expected = BusinessRuleException.class)
    public void testValidatePreConditions_02() throws Exception {
        helperTableSearchBR.validatePreConditions(dummyHelperTable(), A_SEARCHABLE_COLUMN_NAME, "b");
    }

    @Test(expected = BusinessRuleException.class)
    public void testValidatePreConditions_03() throws Exception {
        helperTableSearchBR.validatePreConditions(dummyHelperTable(), A_SEARCHABLE_COLUMN_NAME, "");
    }

    @Test(expected = BusinessRuleException.class)
    public void testValidatePreConditions_04() throws Exception {
        helperTableSearchBR.validatePreConditions(dummyHelperTable(), A_NON_SEARCHABLE_COLUMN_NAME, "buscar");
    }
}