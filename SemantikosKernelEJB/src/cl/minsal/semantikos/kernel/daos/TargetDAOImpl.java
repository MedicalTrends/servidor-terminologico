package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.*;
import oracle.jdbc.OracleTypes;
import oracle.sql.NUMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static cl.minsal.semantikos.model.relationships.TargetType.*;
import static java.sql.Types.*;

/**
 * @author Diego Soto
 */
@Stateless
public class TargetDAOImpl implements TargetDAO {

    /**
     * El logger para esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(TargetDAOImpl.class);

    @EJB
    private HelperTableDAO helperTableDAO;

    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private RelationshipAttributeDAO relationshipAttributeDAO;

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

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public Target getTargetByID(long idTarget) {

        Target target;
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_target.get_target_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idTarget);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                //String jsonResult = rs.getString(1);
                //target = targetFactory.createTargetFromJSON(jsonResult);
                target = createTargetFromResultSet(rs);
            } else {
                String errorMsg = "Un error imposible acaba de ocurrir";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
            call.close();
            connection.close();
        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_definitions_by_category(" + idTarget + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return target;
    }

    @Override
    public Target getDefaultTargetByID(long idTarget) {

        Target target;
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_target.get_default_target_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idTarget);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                target = createTargetFromResultSet(rs);
            } else {
                String errorMsg = "Un error imposible acaba de ocurrir";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_definitions_by_category(" + idTarget + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return target;
    }

    @Override
    public long persist(Target target, TargetDefinition targetDefinition) {

        //ConnectionBD connect = new ConnectionBD();
        /*
         * Parámetros de la función:
         *   1: Valor flotante tipo básico.
         *   2: Valor Timestamp tipo básico.
         *   3: Valor String tipo básico.
         *   4: Valor Booleano tipo básico.
         *   5: Valor entero tipo básico.
         *   6: ID helper table record.
         *   7: ID terminologia externa (crossmap set)
         *   8: ID SCT
         *   9: Concept SMTK
         *   10: ID del tipo de Target.
         */

        String sql = "begin ? := stk.stk_pck_target.create_target(?,?,?,?,?,?,?,?,?,?); end;";

        long idTarget;

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);

            /* Se fijan de los argumentos por defecto */
            setDefaultValuesForCreateTargetFunction(call);

            /* Almacenar el tipo básico */
            if (targetDefinition.isBasicType()) {
                setTargetCall((BasicTypeValue) target, (BasicTypeDefinition) targetDefinition, call);
                call.setLong(11, BasicType.getIdTargetType());
            }

            /* Almacenar concepto SMTK */
            else if (targetDefinition.isSMTKType()) {
                call.setLong(10, target.getId());
                call.setLong(11, SMTK.getIdTargetType());
            }

            /* Almacenar registro Tabla auxiliar */
            else if (targetDefinition.isHelperTable()) {
                call.setLong(7, target.getId());// Id de HelperTableRow
                call.setLong(11, HelperTable.getIdTargetType());
            }

            /* Almacenar concepto SCT */
            else if (targetDefinition.isSnomedCTType()) {
                call.setLong(9, target.getId());
                call.setLong(11, SnomedCT.getIdTargetType());
            }

            /* Almacenar registro crossmap (directo) */
            else if (targetDefinition.isCrossMapType()){
                call.setLong(8, target.getId());
                call.setLong(11, CrossMap.getIdTargetType());
            }

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                idTarget = call.getLong(1);
            } else {
                throw new EJBException("No se obtuvo respuesta de la base de datos, ni una excepción.");
            }
            //rs.close();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        return idTarget;
    }

    @Override
    public long persist(TargetDefinition targetDefinition) {
        //ConnectionBD connect = new ConnectionBD();
        /**
         * param 1: ID Categoría.
         * param 2: Helper Table
         * param 3: CrossMapType
         * param 4: BasicType
         * param 5: SnomedCTType
         */

        String sql = "begin ? := stk.stk_pck_target.create_target_definition(?,?,?,?,?); end;";

        long idTarget;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se setean todas las posibilidades en NULL */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setNull(2, NULL);
            call.setNull(3, NULL);
            call.setNull(4, NULL);
            call.setNull(5, NULL);
            call.setBoolean(6, false);

            /* Luego se setea solo el valor que corresponde */
            if (targetDefinition.isSMTKType()) {
                call.setLong(2, targetDefinition.getId());
            } else if (targetDefinition.isHelperTable()) {
                call.setLong(3, targetDefinition.getId());
            } else if (targetDefinition.isCrossMapType()) {
                call.setLong(4, targetDefinition.getId());
            } else if (targetDefinition.isBasicType()) {
                call.setLong(5, targetDefinition.getId());
            } else if (targetDefinition.isSnomedCTType()){
                call.setBoolean(6, true);
            }
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                idTarget = rs.getLong(1);
            } else {
                throw new EJBException("La función persist_target_definition(?,?,?,?,?) no retornó.");
            }
            rs.close();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        return idTarget;
    }

    @Override
    public long update(Relationship relationship) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_target.update_target(?,?,?,?,?,?,?,?,?,?,?); end;";

        long idTarget = relationshipDAO.getTargetByRelationship(relationship);

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);

            setDefaultValuesForUpdateTargetFunction(call);
            /* Almacenar el tipo básico */
            if (relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()) {

                BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) relationship.getRelationshipDefinition().getTargetDefinition();
                BasicTypeValue value = (BasicTypeValue) relationship.getTarget();


                //TODO: FIX
                if (value.isBoolean()) {
                    call.setBoolean(5, (Boolean) value.getValue());
                }

                if (value.isDate()) {
                    call.setTimestamp(3, (Timestamp) value.getValue());
                }

                if (value.isFloat()) {
                    call.setFloat(2, (Float) value.getValue());
                }
                if (value.isInteger()) {
                    call.setInt(6, (Integer) value.getValue());
                }
                if (value.isString()) {
                    call.setString(4, (String) value.getValue());
                }

            }

            /* Almacenar concepto SMTK */
            if (relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                call.setLong(10, relationship.getTarget().getId());
                call.setLong(11, SMTK.getIdTargetType());
            }

            /* Almacenar registro Tabla auxiliar */
            else if (relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()) {
                call.setLong(7, relationship.getTarget().getId()); //Id de HelperTableRow
                call.setLong(11, HelperTable.getIdTargetType());
            }

            /* Almacenar concepto SCT */
            else if (relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType()) {
                call.setLong(10, relationship.getTarget().getId());
                call.setLong(11, SnomedCT.getIdTargetType());
            }

            call.setLong(12, idTarget);

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);


            if (call.getLong(1) > 0) {
                idTarget = call.getLong(1);
            }

            //rs.close();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        return idTarget;
    }

    @Override
    public long update(RelationshipAttribute relationshipAttribute) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_target.update_target(?,?,?,?,?,?,?,?,?,?,?); end;";

        long idTarget = relationshipAttributeDAO.getTargetByRelationshipAttribute(relationshipAttribute);

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);

            setDefaultValuesForUpdateTargetFunction(call);
            /* Almacenar el tipo básico */
            if (relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition().isBasicType()) {

                BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition();
                BasicTypeValue value = (BasicTypeValue) relationshipAttribute.getTarget();

                if (value.isBoolean()) {
                    call.setBoolean(5, (Boolean) value.getValue());
                }

                if (value.isDate()) {
                    call.setTimestamp(3, (Timestamp) value.getValue());
                }

                if (value.isFloat()) {
                    call.setFloat(2, (Float) value.getValue());
                }
                if (value.isInteger()) {
                    call.setInt(6, (Integer) value.getValue());
                }
                if (value.isString()) {
                    call.setString(4, (String) value.getValue());
                }

            }

            /* Almacenar concepto SMTK */
            if (relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition().isSMTKType()) {
                call.setLong(10, relationshipAttribute.getTarget().getId());
                call.setLong(11, SMTK.getIdTargetType());
            }

            /* Almacenar registro Tabla auxiliar */
            else if (relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition().isHelperTable()) {
                //helperTableDAO.updateAuxiliary(relationship.getId(), relationship.getTarget().getId());
                call.setLong(7, relationshipAttribute.getTarget().getId()); //Id de HelperTableRow
                call.setLong(11, HelperTable.getIdTargetType());
            }

            /* Almacenar concepto SCT */
            else if (relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition().isSnomedCTType()) {
                call.setLong(10, relationshipAttribute.getTarget().getId());
                call.setLong(11, SnomedCT.getIdTargetType());
            }

            call.setLong(12, idTarget);

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                idTarget = call.getLong(1);
            }

            //rs.close();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        return idTarget;
    }

    private void setTargetCall(BasicTypeValue target, BasicTypeDefinition targetDefinition, CallableStatement call) throws SQLException {


        if (targetDefinition.getType().getTypeName().equals("date")) {
            java.util.Date d = (java.util.Date) target.getValue();
            call.setTimestamp(3, new Timestamp(d.getTime()) );
        } else if (targetDefinition.getType().getTypeName().equals("float")) {
            call.setFloat(2, Float.parseFloat (target.getValue().toString()) );
        } else if (targetDefinition.getType().getTypeName().equals("int")) {
            call.setInt(6, (Integer.parseInt(target.getValue().toString())));
        } else if (targetDefinition.getType().getTypeName().equals("string")) {
            call.setString(4, (String) target.getValue());
        } else if (targetDefinition.getType().getTypeName().equals("boolean")) {
            call.setBoolean(5, (Boolean) target.getValue());
        } else {
            throw new EJBException("Tipo Básico no conocido.");
        }
    }

    /**
     * Este método es responsable de fijar los valores por defectos NULOS de la función crear concepto.
     *
     * @throws SQLException
     */
    private void setDefaultValuesForCreateTargetFunction(CallableStatement call) throws SQLException {
        call.setNull(2, REAL);
        call.setNull(3, TIMESTAMP);
        call.setNull(4, VARCHAR);
        //call.setNull(5, BOOLEAN);
        call.setNull(5, NUMERIC);
        call.setNull(6, BIGINT);
        call.setNull(7, BIGINT);
        call.setNull(8, BIGINT);
        call.setNull(9, BIGINT);
        call.setNull(10, BIGINT);
        call.setNull(11, BIGINT);
    }

    private void setDefaultValuesForUpdateTargetFunction(CallableStatement call) throws SQLException {
        setDefaultValuesForCreateTargetFunction(call);
        call.setNull(12, BIGINT);
    }

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
                target = createBasicTypeFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return target;
    }

    public BasicTypeValue createBasicTypeFromResultSet(ResultSet rs) {

        BasicTypeValue bt = null;

        /* Se evaluan los tipos básicos */
        try {
            long id = rs.getLong("id");

            if (DaoTools.getFloat(rs, "float_value") != null) {
                bt = new BasicTypeValue<>(DaoTools.getFloat(rs, "float_value"));
                bt.setId(id);
            } else if (DaoTools.getInteger(rs, "int_value") != null) {
                bt = new BasicTypeValue<>(DaoTools.getInteger(rs, "int_value"));
                bt.setId(id);
            } else if (DaoTools.getBoolean(rs, "boolean_value") != null) {
                bt = new BasicTypeValue<>(DaoTools.getBoolean(rs, "boolean_value"));
                bt.setId(id);
            } else if (DaoTools.getString(rs, "string_value") != null) {
                bt = new BasicTypeValue<>(DaoTools.getString(rs, "string_value"));
                bt.setId(id);
            } else if (DaoTools.getDate(rs, "date_value") != null) {
                bt = new BasicTypeValue<>(DaoTools.getTimestamp(rs, "date_value"));
                bt.setId(id);
            } else {
                String message = "Existe un caso no contemplado";
                logger.error(message);
                throw new EJBException(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bt;
    }

}
