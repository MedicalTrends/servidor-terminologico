package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.model.dtos.HelperTableRowDTO;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;

/**
 * Created by des01c7 on 03-11-17.
 */
public interface HelperTableWSDAO {

    public HelperTableRow createHelperTableRowFromDTO(HelperTableRowDTO helperTableRowDTO);
}
