package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import static cl.minsal.semantikos.model.users.ProfileFactory.ADMINISTRATOR_PROFILE;

/**
 * Este componente es responsable de almacenar las reglas de negocio relacionadas a la persistencia de conceptos.
 *
 * @author Andrés Farías
 */
@Stateless
public class HelperTableRowCreationBRImpl implements BusinessRulesContainer, HelperTableRowCreationBR {

    private static final Logger logger = LoggerFactory.getLogger(HelperTableRowCreationBRImpl.class);

    @EJB
    private HelperTablesManager helperTablesManager;


    @Override
    public void apply(@NotNull HelperTableRow helperTableRow, User IUser) throws Exception {

        /* Reglas que aplican para todas las categorías */
        br101ReferencialIntegrity(helperTableRow, IUser);
        /* Creación de acuerdo al rol */
        br001creationRights(helperTableRow, IUser);
    }

    /**
     * <b>BR-SMTK-001</b>: Conceptos de ciertas categorías pueden sólo ser creados por usuarios con el perfil
     * Modelador.
     *
     * @param helperTableRow El concepto a crear ser creado.
     * @param user        El usuario que realiza la acción.
     */
    protected void br101ReferencialIntegrity(HelperTableRow helperTableRow, User user) {

        for (HelperTableData cell: helperTableRow.getCells()) {
            // Si es llave foranea, chequear integridad
            if (cell.getColumn().isForeignKey() && !HelperTableColumnFactory.isSTK(cell.getColumn())) {
                HelperTable foreignKeyHelperTable = HelperTableFactory.getInstance().findTableById(cell.getColumn().getForeignKeyHelperTableId());
                long foreignKeyValue = cell.getForeignKeyValue();
                HelperTableRow parentRow = helperTablesManager.getRowBy(foreignKeyHelperTable, foreignKeyValue);

                if (parentRow == null) {
                    throw new BusinessRuleException("BR-SMTK-101", "LLave foránea " + foreignKeyValue + " no existe en tabla " + foreignKeyHelperTable.getName());
                }

            }
        }

    }

    /**
     * <b>BR-SMTK-001</b>: Conceptos de ciertas categorías pueden sólo ser creados por usuarios con el perfil
     * Modelador.
     *
     * @param helperTableRow El concepto a crear ser creado.
     * @param user        El usuario que realiza la acción.
     */
    protected void br001creationRights(HelperTableRow helperTableRow, User user) {

        /* Categorías restringidas para usuarios con rol diseñador */
        if (!user.getProfiles().contains(ADMINISTRATOR_PROFILE)) {
            logger.info("Se intenta violar la regla de negocio BR-SMTK-001 por el usuario " + user);
            throw new BusinessRuleException("BR-SMTK-001", "El usuario " + user + " no tiene privilegios para crear registros en tablas auxiliares");
        }
    }

}
