package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.externtables.*;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;

import java.util.List;

/**
 * Created by BluePrints Developer on 09-01-2017.
 */
public interface ExternTableDAO {

    List<ExternTable> getTables();

    List<ExternTableColumn> getColumns();

    List<ExternTableColumn> getColumns(ExternTable externTable);

    List<ExternTableReference> getReferences(ExternTable externTable);

    List<ExternTableRow> getRows(ExternTable table);

    List<ExternTableData> getCellsByRow(ExternTableRow externTableRow);

    List<ExternTableRelationship> getRelationships(ExternTableRow row);
}
