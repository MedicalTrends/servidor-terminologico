package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.HelperTableDAO;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.dtos.ConceptDTO;
import cl.minsal.semantikos.model.dtos.HelperTableRowDTO;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blueprints on 9/16/2015.
 */
@Stateless
public class HelperTableWSDAOImpl implements Serializable, HelperTableWSDAO {

    /** Logger de la clase */
    private static final Logger logger = LoggerFactory.getLogger(HelperTableWSDAOImpl.class);

    @EJB
    ConceptDAO conceptDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    public HelperTableRow createHelperTableRowFromDTO(HelperTableRowDTO helperTableRowDTO) {

        HelperTableRow helperTableRow = new HelperTableRow();

        helperTableRow.setId(helperTableRowDTO.getId());
        helperTableRow.setCreationDate(helperTableRowDTO.getCreationDate());
        helperTableRow.setLastEditDate(helperTableRowDTO.getLastEditDate());
        helperTableRow.setValid(helperTableRowDTO.isValid());
        helperTableRow.setValidityUntil(helperTableRowDTO.getValidityUntil());
        helperTableRow.setDescription(helperTableRowDTO.getDescription());
        helperTableRow.setCreationUsername(helperTableRowDTO.getCreationUsername());
        helperTableRow.setLastEditUsername(helperTableRowDTO.getLastEditUsername());
        helperTableRow.setHelperTableId(helperTableRowDTO.getHelperTableId());

        helperTableRow.setCells(helperTableRowDTO.getCells());

        for (HelperTableData helperTableData : helperTableRow.getCells()) {
            helperTableData.setColumn(HelperTableColumnFactory.getInstance().findColumnById(helperTableData.getColumnId()));
        }

        return helperTableRow;
    }

}