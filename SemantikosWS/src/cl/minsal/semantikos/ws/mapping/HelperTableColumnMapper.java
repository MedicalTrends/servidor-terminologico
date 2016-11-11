package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.ws.response.HelperTableColumnResponse;

/**
 * Created by Development on 2016-10-19.
 *
 */
public class HelperTableColumnMapper {

    public static HelperTableColumnResponse map(HelperTableColumn helperTableColumn) {
        if ( helperTableColumn != null ) {
            HelperTableColumnResponse res = new HelperTableColumnResponse();
            res.setColumnName(helperTableColumn.getColumnName());
            res.setPK(helperTableColumn.isPK());
            res.setSearchable(helperTableColumn.isSearchable());
            res.setShowable(helperTableColumn.isShowable());
            return res;
        }
        return null;
    }

}
