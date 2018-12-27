package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.validation.constraints.NotNull;


@Local
public interface HelperTableRowCreationBR {

    public void apply(@NotNull HelperTableRow helperTableRow, User IUser) throws Exception;

}
