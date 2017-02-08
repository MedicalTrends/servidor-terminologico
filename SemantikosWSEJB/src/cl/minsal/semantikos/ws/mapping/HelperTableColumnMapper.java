package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.ws.response.HelperTableColumnResponse;

/**
 * Created by Development on 2016-10-19.
 */
public class HelperTableColumnMapper {

    public static HelperTableColumnResponse map(HelperTableColumn helperTableColumn) {
        HelperTableColumnResponse res = new HelperTableColumnResponse();
        res.setColumnName(helperTableColumn.getName());
        res.setSearchable(helperTableColumn.isSearchable());
        res.setShowable(helperTableColumn.isShowable());
        return res;
    }

}
