package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.*;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.snomedct.SnomedCT;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class TargetMapper {

    private static final int BASIC_TYPE_ID = 1;
    private static final int SMTK_TYPE_ID = 2;
    private static final int SCT_TYPE_ID = 3;
    private static final int HELPER_TABLE_TYPE_ID = 4;
    private static final int CROSSMAP_TYPE_ID = 5;

    @EJB
    HelperTableDAO helperTableDAO;

    @EJB
    CrossmapsDAO crossmapsDAO;

    @EJB
    SnomedCTDAO snomedCTDAO;

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    BasicTypeDefinitionDAO basicTypeDefinitionDAO;

    @EJB
    CategoryDAO categoryDAO;

    /**
     * Este método es responsable de reconstruir un Target a partir de una expresión JSON, yendo a buscar los otros
     * objetos necesarios.
     *
     * @param rs Una expresión JSON de la forma {"id":1,"float_value":null,"date_value":null,"string_value":"strig","boolean_value":null,"int_value":null,"id_auxiliary":null,"id_extern":null,"id_concept_sct":null,"id_concept_stk":null,"id_target_type":null}
     * @return Una instancia fresca y completa
     */
    public Target createTargetFromResultSet(ResultSet rs) {

        Target target = null;

        try {
            long idHelperTableRecord = rs.getLong("id_auxiliary");
            long idExtern = rs.getLong("id_extern");
            long idConceptSct = rs.getLong("id_concept_sct");
            long idConceptStk = rs.getLong("id_concept_stk");

            /* Se evalúa caso a caso. Helper Tables: */
            if (idHelperTableRecord > 0) {
                target = helperTableDAO.getRowById(idHelperTableRecord);
            } else if (idExtern > 0) {
                target = crossmapsDAO.getCrossmapSetMemberById(idExtern);
            } else if (idConceptSct > 0) {
                target = snomedCTDAO.getConceptByID(idConceptSct);
            } else if (idConceptStk > 0) {
                target = conceptDAO.getConceptByID(idConceptStk);
            }
            /* Ahora los tipos básicos */
            else {
                target = BasicTypeMapper.createBasicTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return target;
    }

    public TargetDefinition createTargetDefinitionFromResultSet(ResultSet rs) {

        TargetDefinition targetDefinition = null;

        try {
            switch ((int) rs.getLong("id_target_type")) {

                case BASIC_TYPE_ID:
                    return basicTypeDefinitionDAO.getBasicTypeDefinitionById(rs.getLong("id_basic_type"));

                case SMTK_TYPE_ID:
                    return categoryDAO.getCategoryById(rs.getLong("id_category"));

                case SCT_TYPE_ID:
                    return new SnomedCT("1.0");

                case HELPER_TABLE_TYPE_ID:
                    return helperTableDAO.getHelperTableByID(rs.getLong("id_helper_table_name"));

                case CROSSMAP_TYPE_ID:
                    return crossmapsDAO.getCrossmapSetByID(rs.getLong("id_extern_table_name"));

                default:
                    throw new EJBException("TIPO DE DEFINICION INCORRECTO. ID Target Type=" + rs.getLong("id_target_type"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return targetDefinition;
    }

}
