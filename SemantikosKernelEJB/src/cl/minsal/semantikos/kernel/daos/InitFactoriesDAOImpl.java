package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.*;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableRecordFactory;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    RelationshipDefinitionDAO relationshipDefinitionDAO;

    @EJB
    private TagSMTKDAO tagSMTKDAO;

    @EJB
    private QueryDAO queryDAO;

    @EJB
    private CategoryDAO categoryDAO;

    @PostConstruct
    private void init() {
        this.refreshCategories();
        this.refreshQueries();
        this.refreshDescriptionTypes();
        this.refreshTagsSMTK();
        this.refreshColumns();
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
        long idCategory = resultSet.getLong("idcategory");
        String nameCategory = resultSet.getString("namecategory");
        String nameAbbreviated = resultSet.getString("nameabbreviated");
        boolean restriction = resultSet.getBoolean("restriction");
        String color = resultSet.getString("nameabbreviated");
        long idTagSMTK = resultSet.getLong("tag");
        TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);

        return new Category(idCategory, nameCategory, nameAbbreviated, restriction, color, tagSMTKByID);
    }


    public List<RelationshipDefinition> getCategoryMetaData(long idCategory) {
        return relationshipDefinitionDAO.getRelationshipDefinitionsByCategory(idCategory);
    }

    @Override
    public CategoryFactory refreshCategories() {

        ConnectionBD connect = new ConnectionBD();
        List<Category> categories = new ArrayList<>();
        ;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("SELECT * FROM semantikos.get_all_categories()")) {
            call.execute();

            ResultSet resultSet = call.getResultSet();

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
                if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                    Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();

                    for (RelationshipDefinition relationshipDefinitionDestination : queryDAO.getSecondOrderShowableAttributesByCategory(categoryDestination)) {

                        if(relationshipDefinition.getTargetDefinition().isSMTKType()) {
                            query.getSourceSecondOrderShowableAttributes().add(relationshipDefinition);
                        }

                        QueryColumn secondOrderColumn = new QueryColumn(relationshipDefinitionDestination.getName(), new Sort(null, false), relationshipDefinitionDestination);
                        if(relationshipDefinitionDestination.isU_asist() && category.getNameAbbreviated().equals("MCCE")) {
                            continue;
                        }
                        if(relationshipDefinitionDestination.isCondicionDeVenta() && category.getNameAbbreviated().equals("PC")) {
                            continue;
                        }
                        else {
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

        ConnectionBD connect = new ConnectionBD();

        List<TagSMTK> tagsSMTK = new ArrayList<>();

        String sql = "{call semantikos.get_all_tag_smtks()}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

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
        ObjectMapper mapper = new ObjectMapper();

        List<DescriptionType> descriptionTypes = new ArrayList<>();

        String sql = "{call semantikos.get_description_types()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

            /* Se recuperan los description types */
            DescriptionTypeDTO[] theDescriptionTypes = new DescriptionTypeDTO[0];
            if (rs.next()) {
                String resultJSON = rs.getString(1);
                theDescriptionTypes = mapper.readValue(underScoreToCamelCaseJSON(resultJSON), DescriptionTypeDTO[].class);
            }

            if (theDescriptionTypes.length > 0) {
                for (DescriptionTypeDTO aDescriptionType : theDescriptionTypes) {
                    DescriptionType descriptionType = aDescriptionType.getDescriptionType();
                    descriptionTypes.add(descriptionType);
                }
            }

            /* Se setea la lista de Tipos de descripción */
            DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypes);
        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar Description Types de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        } catch (IOException e) {
            String errorMsg = "Error al intentar parsear Description Types en JSON.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return DescriptionTypeFactory.getInstance();
    }

    @Override
    public HelperTableColumnFactory refreshColumns() {
        ConnectionBD connect = new ConnectionBD();

        List<HelperTableColumn> helperTableColumns = new ArrayList<>();

        String sql = "{call semantikos.get_all_helper_table_columns()}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

            /* Se recuperan las columnas */
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return null;

                try {
                    helperTableColumns = helperTableRecordFactory.createHelperTableColumnsFromJSON(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
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
}
