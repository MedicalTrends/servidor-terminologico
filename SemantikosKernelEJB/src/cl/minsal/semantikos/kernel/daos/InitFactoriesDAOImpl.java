package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.DescriptionMapper;
import cl.minsal.semantikos.kernel.daos.mappers.HelperTableMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DataSourceFactory;
import cl.minsal.semantikos.model.browser.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableColumnFactory;
import cl.minsal.semantikos.model.helpertables.HelperTableRecordFactory;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.EmailFactory;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.mail.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Diego Soto on 9/5/16.
 */
@Singleton
@Startup
public class InitFactoriesDAOImpl implements InitFactoriesDAO {

    private static final Logger logger = LoggerFactory.getLogger(InitFactoriesDAOImpl.class);

    @EJB
    HelperTableRecordFactory helperTableRecordFactory;

    @EJB
    HelperTableMapper helperTableMapper;

    @EJB
    RelationshipDefinitionDAO relationshipDefinitionDAO;

    @EJB
    private TagSMTKDAO tagSMTKDAO;

    @EJB
    private QueryDAO queryDAO;

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private InstitutionDAO institutionDAO;

    @EJB
    private QuestionDAO questionDAO;

    @EJB
    private AuthDAO authDAO;

    @EJB
    private DescriptionMapper descriptionMapper;

    @PostConstruct
    private void init() {
        try {
            this.refreshDataSource();
            //this.refreshEmail();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        this.testArrayOracle();
        this.refreshColumns();
        this.refreshCategories();
        this.refreshQueries();
        this.refreshDescriptionTypes();
        this.refreshTagsSMTK();
        this.refreshUsers();
    }

    private void testArrayOracle() {

        int intArray[] = { 1,2,3,4,5,6 };

        String sql = "begin stk.give_me_an_array(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection(); CallableStatement call =
                connection.prepareCall(sql)) {

            call.setArray(1, connection.unwrap(oracle.jdbc.OracleConnection.class).createARRAY("STK.STK_TYPE_NUM_ARRAY", intArray));

            call.execute();

            call.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

    }

    /**
     * Este método es responsable de crear un Tag STMK a partir de un raw de un resultset.
     *
     * @param rs El resultset posicionado en una Raw.
     *
     * @return El objeto fresco construido desde el Resultset.
     *
     * @throws SQLException Arrojada si hay un problema.
     */
    private TagSMTK createTagSMTKFromResultSet(@NotNull ResultSet rs) throws SQLException {

        long id = rs.getLong("id");
        String name = rs.getString("name");

        return new TagSMTK(id, name);
    }

    private Category createCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        long idCategory = resultSet.getLong("id");
        String nameCategory = resultSet.getString("name");
        String nameAbbreviated = resultSet.getString("name_abreviated");
        boolean restriction = resultSet.getBoolean("restriction");
        String color = resultSet.getString("name_abreviated");
        long idTagSMTK = resultSet.getLong("tag_semantikos");
        TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);

        return new Category(idCategory, nameCategory, nameAbbreviated, restriction, color, tagSMTKByID);
    }

    private User makeUserFromResult(ResultSet rs) throws SQLException {

        User u = new User();

        u.setId(rs.getBigDecimal(1).longValue());
        u.setUsername(rs.getString(2));
        u.setPasswordHash(rs.getString(3));
        u.setPasswordSalt(rs.getString(4));
        u.setName(rs.getString(5));
        u.setLastName(rs.getString(6));
        u.setSecondLastName(rs.getString(7));
        u.setEmail(rs.getString(8));

        u.setLocked(rs.getBoolean(9));
        u.setFailedLoginAttempts(rs.getInt(10));
        u.setFailedAnswerAttempts(rs.getInt(11));

        u.setLastLogin(rs.getTimestamp(12));
        u.setLastPasswordChange(rs.getTimestamp(13));

        u.setLastPasswordHash1(rs.getString(14));
        u.setLastPasswordHash2(rs.getString(15));
        u.setLastPasswordHash3(rs.getString(16));
        u.setLastPasswordHash4(rs.getString(17));

        u.setLastPasswordSalt1(rs.getString(18));
        u.setLastPasswordSalt2(rs.getString(19));
        u.setLastPasswordSalt3(rs.getString(20));
        u.setLastPasswordSalt4(rs.getString(21));

        u.setDocumentNumber(rs.getString(22));
        u.setVerificationCode(rs.getString(23));
        u.setValid(rs.getBoolean(24));
        u.setDocumentRut(rs.getBoolean(25));

        u.setProfiles(authDAO.getUserProfiles(u.getId()));

        u.setInstitutions(institutionDAO.getInstitutionBy(u));

        u.setAnswers(questionDAO.getAnswersByUser(u));

        return u;
    }


    public List<RelationshipDefinition> getCategoryMetaData(long idCategory) {
        return relationshipDefinitionDAO.getRelationshipDefinitionsByCategory(idCategory);
    }

    @Override
    public CategoryFactory refreshCategories() {

        List<Category> categories = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_category.get_all_categories; end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.execute();

            //ResultSet resultSet = call.getResultSet();
            ResultSet resultSet = (ResultSet) call.getObject(1);

            while (resultSet.next()) {
                Category categoryFromResultSet = createCategoryFromResultSet(resultSet);
                categories.add(categoryFromResultSet);
            }

            /* Ahora se recuperan sus definiciones */
            for (Category category : categories) {
                long id = category.getId();
                List<RelationshipDefinition> categoryMetaData = getCategoryMetaData(id);
                category.setRelationshipDefinitions(categoryMetaData);
            }

            /* Se setea la lista de tagsSMTK */
            CategoryFactory.getInstance().setCategories(categories);

        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return CategoryFactory.getInstance();
    }

    @Override
    public QueryFactory refreshQueries() {

        List<GeneralQuery> queries = new ArrayList<>();

        for (Category category : CategoryFactory.getInstance().getCategories()) {

            GeneralQuery query = new GeneralQuery();

            List<Category> categories = new ArrayList<Category>();
            categories.add(category);
            query.setCategories(categories);

            List<QueryFilter> filters = new ArrayList<QueryFilter>();
            query.setFilters(filters);

            // Stablishing custom filtering value
            query.setCustomFilterable(queryDAO.getCustomFilteringValue(category));

            // Adding dynamic columns
            for (RelationshipDefinition relationshipDefinition : queryDAO.getShowableAttributesByCategory(category)) {
                query.getColumns().add(new QueryColumn(relationshipDefinition.getName(), new Sort(null, false), relationshipDefinition));
            }

            // Adding second order columns, if this apply
            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {

                if(relationshipDefinition.getTargetDefinition().isSMTKType()) {
                    Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();

                    for (RelationshipDefinition relationshipDefinitionDestination : queryDAO.getSecondOrderShowableAttributesByCategory(categoryDestination)) {

                        QueryColumn secondOrderColumn = new QueryColumn(relationshipDefinitionDestination.getName(), new Sort(null, false), relationshipDefinitionDestination);
                        if(relationshipDefinitionDestination.isU_asist() && category.getNameAbbreviated().equals("MCCE")) {
                            continue;
                        }
                        if(relationshipDefinitionDestination.isCondicionDeVenta() && category.getNameAbbreviated().equals("PC")) {
                            continue;
                        }
                        else {
                            if(relationshipDefinition.getTargetDefinition().isSMTKType()) {
                                query.getSourceSecondOrderShowableAttributes().add(relationshipDefinition);
                            }

                            query.getColumns().add(secondOrderColumn);
                            secondOrderColumn.setSecondOrder(true);
                        }
                    }
                }
            }

            // Adding related concepts category to columns, if this apply
            if(queryDAO.getShowableRelatedConceptsValue(category)) {
                query.setShowRelatedConcepts(true);
                for (Category relatedCategory : categoryDAO.getRelatedCategories(category)) {
                    if(queryDAO.getShowableValue(relatedCategory)) {
                        RelationshipDefinition rd = new RelationshipDefinition(relatedCategory.getId(), relatedCategory.getName(), relatedCategory.getName(), relatedCategory, MultiplicityFactory.ONE_TO_ONE);
                        query.getColumns().add(new QueryColumn(rd.getName(), new Sort(null, false), rd));
                    }
                }
            }

            // Adding dynamic filters
            for (RelationshipDefinition relationshipDefinition : queryDAO.getSearchableAttributesByCategory(category)) {
                QueryFilter queryFilter = new QueryFilter(relationshipDefinition);
                queryFilter.setMultiple(queryDAO.getMultipleFilteringValue(category, relationshipDefinition));
                query.getFilters().add(queryFilter);
            }

            // Adding second order filters, if this apply
            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {
                if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                    Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();
                    for (RelationshipDefinition relationshipDefinitionDestination : queryDAO.getSecondOrderSearchableAttributesByCategory(categoryDestination)) {
                        QueryFilter secondOrderQueryFilter = new QueryFilter(relationshipDefinitionDestination);
                        secondOrderQueryFilter.setMultiple(queryDAO.getMultipleFilteringValue(categoryDestination, relationshipDefinition));
                        secondOrderQueryFilter.setSecondOrder(true);
                        query.getFilters().add(secondOrderQueryFilter);
                    }
                }
            }

            // Adding second derivate filters, if this apply
            for (RelationshipAttributeDefinition relationshipAttributeDefinition : queryDAO.getSecondDerivateSearchableAttributesByCategory(category)) {
                QueryFilterAttribute queryFilter = new QueryFilterAttribute(relationshipAttributeDefinition);
                query.getAttributeFilters().add(queryFilter);
            }

            queries.add(query);

        }

        QueryFactory.getInstance().setQueries(queries);

        return QueryFactory.getInstance();

    }


    @Override
    public TagSMTKFactory refreshTagsSMTK() {

        List<TagSMTK> tagsSMTK = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_tag_smtk.get_all_tag_smtks; end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);
            //ResultSet rs = call.getResultSet();

            /* Se recuperan los tagsSMTK */
            while (rs.next()) {
                tagsSMTK.add(createTagSMTKFromResultSet(rs));
            }

            /* Se setea la lista de tagsSMTK */
            TagSMTKFactory.getInstance().setTagsSMTK(tagsSMTK);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar tagsSMTK de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return TagSMTKFactory.getInstance();
    }

    @Override
    public DescriptionTypeFactory refreshDescriptionTypes() {

        ConnectionBD connect = new ConnectionBD();

        List<DescriptionType> descriptionTypes = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.get_description_types; end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            /* Se recuperan los description types */
            while (rs.next()) {
                descriptionTypes.add(descriptionMapper.createDescriptionTypeFromResultSet(rs));
            }

            /* Se setea la lista de Tipos de descripción */
            DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypes);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar Description Types de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return DescriptionTypeFactory.getInstance();
    }

    //@Override
    public UserFactory refreshUsers() {

        ArrayList<User> users = new ArrayList<>();

        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_all_users; end;";

        //String sql = "begin ? := stk.stk_pck_helper_table.get_all_helper_table_columns; end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
                users.add(user);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        UserFactory.getInstance().setUsers(users);

        return UserFactory.getInstance();
    }

    @Override
    public HelperTableColumnFactory refreshColumns() {
        ConnectionBD connect = new ConnectionBD();

        List<HelperTableColumn> helperTableColumns = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_helper_table.get_all_helper_table_columns; end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);
            //ResultSet rs = call.getResultSet();

            /* Se recuperan las columnas */
            while(rs.next()) {
                helperTableColumns.add(helperTableMapper.createHelperTableColumnFromResultSet(rs));
            }

            /* Se setea la lista de tagsSMTK */
            HelperTableColumnFactory.getInstance().setHelperTableColumns(helperTableColumns);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar la lista de columnas de tablas auxiliares de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return HelperTableColumnFactory.getInstance();
    }

    @Override
    public EmailFactory refreshEmail() throws NamingException {
        InitialContext c = new InitialContext();
        Session session = (Session)c.lookup("java:jboss/mail/Default");
        //Session session = (Session)c.lookup("java:/Mail");
        EmailFactory.getInstance().setMySession(session);

        return EmailFactory.getInstance();
    }

    @Override
    public DataSourceFactory refreshDataSource() throws NamingException {
        InitialContext c = new InitialContext();
        DataSource dataSource = (DataSource) c.lookup("java:jboss/OracleDS");
        DataSourceFactory.getInstance().setDataSource(dataSource);

        return DataSourceFactory.getInstance();
    }
}
