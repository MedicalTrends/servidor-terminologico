package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.kernel.daos.HelperTableDAO;
import cl.minsal.semantikos.kernel.daos.SnomedCTDAO;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Target;
import com.fasterxml.jackson.databind.*;

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

    @EJB
    HelperTableDAO helperTableDAO;

    @EJB
    CrossmapsDAO crossmapsDAO;

    @EJB
    SnomedCTDAO snomedCTDAO;

    @EJB
    ConceptDAO conceptDAO;

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
}
