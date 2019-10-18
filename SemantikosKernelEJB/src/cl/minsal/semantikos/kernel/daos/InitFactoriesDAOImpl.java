package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.EmailFactory;
import cl.minsal.semantikos.kernel.factories.QueryFactory;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetFactory;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableColumnFactory;

import cl.minsal.semantikos.model.helpertables.HelperTableFactory;
import cl.minsal.semantikos.model.queries.*;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;

import cl.minsal.semantikos.model.users.*;

import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Diego Soto on 9/5/16.
 */
@Singleton
@Startup
public class InitFactoriesDAOImpl implements InitFactoriesDAO {

    private static final Logger logger = LoggerFactory.getLogger(InitFactoriesDAOImpl.class);

    @EJB
    RelationshipDefinitionDAO relationshipDefinitionDAO;

    @EJB
    private TagSMTKDAO tagSMTKDAO;

    @EJB
    private QueryDAO queryDAO;

    @EJB
    private DescriptionDAO descriptionDAO;

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private ProfileDAO profileDAO;

    @EJB
    private InstitutionDAO institutionDAO;

    @EJB
    private QuestionDAO questionDAO;

    @EJB
    private AuthDAO authDAO;

    @EJB
    private CrossmapsDAO crossmapDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @PostConstruct
    private void init() {
        try {
            this.refreshEmail();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        //this.testArrayOracle();
        this.refreshColumns();
        this.refreshTables();
        this.refreshInstitutions();
        this.refreshCategories();
        this.refreshCrossmapSets();
        this.refreshQueries();
        this.refreshDescriptionTypes();
        this.refreshTagsSMTK();
        this.refreshUsers();
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


    public List<RelationshipDefinition> getCategoryMetaData(long idCategory) {
        return relationshipDefinitionDAO.getRelationshipDefinitionsByCategory(idCategory);
    }

    @Override
    public CategoryFactory refreshCategories() {

        List<Category> categories = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_category.get_all_categories; end;";

        try (Connection connection = dataSource.getConnection();
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
            for (QueryColumn queryColumn : queryDAO.getShowableAttributesByCategory(category)) {
                query.getColumns().add(queryColumn);
            }

            // Adding second order columns, if this apply
            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {

                if(relationshipDefinition.getTargetDefinition().isSMTKType()) {
                    Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();

                    for (RelationshipDefinition relationshipDefinitionDestination : queryDAO.getSecondOrderShowableAttributesByCategory(categoryDestination)) {

                        QueryColumn secondOrderColumn = new QueryColumn(relationshipDefinitionDestination.getName(), new Sort(null, false), relationshipDefinitionDestination, true);
                        if(relationshipDefinitionDestination.isU_asist() && category.getNameAbbreviated().equals("MCCE")) {
                            continue;
                        }
                        if(relationshipDefinitionDestination.isCondicionDeVenta() && category.getNameAbbreviated().equals("PC")) {
                            continue;
                        }
                        else {
                            if(relationshipDefinition.getTargetDefinition().isSMTKType()) {
                                if(!query.getSourceSecondOrderShowableAttributes().contains(relationshipDefinition)) {
                                    query.getSourceSecondOrderShowableAttributes().add(relationshipDefinition);
                                }
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
                        query.getColumns().add(new QueryColumn(rd.getName(), new Sort(null, false), rd, true));
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

        try (Connection connection = dataSource.getConnection();
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

        List<DescriptionType> descriptionTypes = descriptionDAO.getDescriptionTypes();
        DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypes);
        return DescriptionTypeFactory.getInstance();
    }

    @Override
    public CrossmapSetFactory refreshCrossmapSets() {

        List<CrossmapSet> crossmapSets = crossmapDAO.getCrossmapSets();
        CrossmapSetFactory.getInstance().setCrossmapSets(crossmapSets);
        return CrossmapSetFactory.getInstance();
    }

    //@Override
    public UserFactory refreshUsers() {

        List<User> users = authDAO.getAllUsers();
        UserFactory.getInstance().setUsers(users);
        return UserFactory.getInstance();
    }

    public InstitutionFactory refreshInstitutions() {

        List<Institution> institutions = institutionDAO.getAllInstitution();
        InstitutionFactory.getInstance().setInstitutions(institutions);
        return InstitutionFactory.getInstance();
    }

    @Override
    public HelperTableColumnFactory refreshColumns() {

        List<HelperTableColumn> helperTableColumns = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_helper_table.get_all_helper_table_columns; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            /* Se recuperan las columnas */
            while(rs.next()) {
                helperTableColumns.add(createHelperTableColumnFromResultSet(rs));
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
    public HelperTableFactory refreshTables() {

        List<HelperTable> helperTables = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_tables; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);
            //ResultSet rs = call.getResultSet();

            /* Se recuperan las columnas */
            while(rs.next()) {
                helperTables.add(createHelperTableFromResultSet(rs));
            }

            /* Se setea la lista de tagsSMTK */
            HelperTableFactory.getInstance().setHelperTables(helperTables);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar la lista de tablas auxiliares de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return HelperTableFactory.getInstance();
    }

    @Override
    public EmailFactory refreshEmail() throws NamingException {
        InitialContext c = new InitialContext();
        Session session = (Session)c.lookup("java:jboss/mail/Default");
        //Session session = (Session)c.lookup("java:/Mail");
        EmailFactory.getInstance().setMySession(session);

        return EmailFactory.getInstance();
    }

    /**
     * Este método es responsable de crear un HelperTable Record a partir de un objeto JSON.
     *
     * @param rs El objeto JSON a partir del cual se crea el objeto. El formato JSON será:
     *                       <code>{"TableName":"helper_table_atc","records":[{"id":1,"codigo_atc":"atc1"}</code>
     *
     * @return Un objeto fresco de tipo <code>HelperTableRecord</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public HelperTable createHelperTableFromResultSet(ResultSet rs) {

        HelperTable helperTable = new HelperTable();

        try {
            helperTable.setId(rs.getLong("id"));
            helperTable.setName(rs.getString("name"));
            helperTable.setDescription(rs.getString("description"));

            helperTable.setColumns(HelperTableColumnFactory.getInstance().findColumnsByHelperTable(rs.getLong("id")));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helperTable;

    }

    /**
     * Este método es responsable de crear un HelperTable Record a partir de un objeto JSON.
     *
     * @param rs El objeto JSON a partir del cual se crea el objeto. El formato JSON será:
     *                       <code>{"TableName":"helper_table_atc","records":[{"id":1,"codigo_atc":"atc1"}</code>
     *
     * @return Un objeto fresco de tipo <code>HelperTableRecord</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public HelperTableColumn createHelperTableColumnFromResultSet(ResultSet rs) {

        HelperTableColumn helperTableColumn = new HelperTableColumn();

        try {
            helperTableColumn.setId(rs.getLong("id"));
            helperTableColumn.setName(rs.getString("name"));
            helperTableColumn.setHelperTableId(rs.getLong("helper_table_id"));
            helperTableColumn.setHelperTableDataTypeId(rs.getInt("helper_table_data_type_id"));
            helperTableColumn.setForeignKeyHelperTableId(rs.getInt("foreign_key_table_id"));
            helperTableColumn.setForeignKey(rs.getBoolean("foreign_key"));
            helperTableColumn.setDescription(rs.getString("description"));
            helperTableColumn.setSearchable(rs.getBoolean("searchable"));
            helperTableColumn.setShowable(rs.getBoolean("showable"));
            helperTableColumn.setEditable(rs.getBoolean("editable"));
            helperTableColumn.setSortable(rs.getBoolean("sortable"));
            helperTableColumn.setRequired(rs.getBoolean("required"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helperTableColumn;

    }

}
