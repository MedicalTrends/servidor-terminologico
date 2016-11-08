package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.HelperTableDAO;
import cl.minsal.semantikos.model.helpertables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



/**
 * Este manager es responsable de proveer acceso a las distintas tablas auxiliares.
 * <p>
 * Las tablas auxiliares se han implementado de manera estática, es decir, se maneja una lista estática de las tablas
 * axiliares.
 * </p>
 *
 * @author Andrés Farías
 * @see HelperTableManager
 */
@Stateless
public class HelperTableManagerImpl implements HelperTableManager {

    private static final Logger logger = LoggerFactory.getLogger(HelperTableManagerImpl.class);

    @EJB
    private HelperTableDAO helperTableDAO;

    @Override
    public Collection<HelperTable> getHelperTables() {

        /* Esto se resuelve con delegación sobre el Factory, mientras las tablas estén en duro */
        return helperTableDAO.getHelperTables();
    }

    @Override
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable) {
        return helperTableDAO.getAllRecords(helperTable);
    }

    @Override
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable, List<String> columnNames) {

        logger.debug("Se solicitan los registros de la tabla " + helperTable);


        List<HelperTableRecord> allRecords = helperTableDAO.getAllRecords(helperTable);
        logger.debug("Elemenos en orden original: " + allRecords);
        Collections.sort(allRecords);
        logger.debug("Elemenos en orden final: " + allRecords);
        logger.debug("Se recuperan " + allRecords.size() + " registros de la tabla " + helperTable);

        return allRecords;
    }

    @Override
    public List<HelperTableRecord> getValidRecords(@NotNull HelperTable helperTable, List<String> columnNames) {
        logger.debug("Se solicitan los registros vigentes de la tabla " + helperTable);

        throw new NotImplementedException();
    }

    @Override
    public List<HelperTableRecord> searchValidRecords(@NotNull HelperTable helperTable, List<String> columnNames, String query) {
        logger.debug("Se solicitan los registros vigentes de la tabla " + helperTable);

        throw new NotImplementedException();


    }



    @Override
    public HelperTableRecord getRecord( long recordId) {
        return helperTableDAO.getHelperTableRecordFromId(recordId);
    }

    @Override
    public HelperTable findHelperTableByID(long id) {
        return helperTableDAO.getHelperTableByID(id);
    }
}
