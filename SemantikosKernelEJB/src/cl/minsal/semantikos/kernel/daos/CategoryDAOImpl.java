package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Diego Soto
 * @author Andrés Farías
 * @version 2.0
 */

@Singleton
public class CategoryDAOImpl implements CategoryDAO {

    static final Logger logger = LoggerFactory.getLogger(CategoryDAOImpl.class);

    /** El DAO para recuperar atributos de la categoría */
    @EJB
    private RelationshipDefinitionDAO relationshipDefinitionDAO;

    @EJB
    private TagSMTKDAO tagSMTKDAO;

    /** Un caché de categorías */
    private Map<Long, Category> categoryMapByID;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    public CategoryDAOImpl() {
        this.categoryMapByID = new HashMap<>();
    }

    @Override
    public Category getCategoryById(long idCategory) {

        /* Si está en el caché, se retorna */
        if(categoryMapByID.containsKey(idCategory)){
            return categoryMapByID.get(idCategory);
        }

        /* Se almacena en el caché */
        Category categoryByIdFromDB = getCategoryByIdFromDB(idCategory);
        categoryMapByID.put(idCategory, categoryByIdFromDB);

        /* Y se carga su metadata */
        List<RelationshipDefinition> categoryMetaData = getCategoryMetaData(idCategory);
        categoryByIdFromDB.setRelationshipDefinitions(categoryMetaData);

        return categoryByIdFromDB;
    }

    /**
     * Este método es responsable de recuperar de la BDD.
     *
     * @param idCategory ID de la categoría.
     *
     * @return La categoría desde la bdd.
     */
    private Category getCategoryByIdFromDB(long idCategory) {
        Category category;
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_category.get_category_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idCategory);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                category = createCategoryFromResultSet(rs);
            } else {
                throw new EJBException("Error en la llamada");
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "error en getCategoryById = " + idCategory;
            logger.error(errorMsg, idCategory, e);
            throw new EJBException(errorMsg, e);
        }

        return category;
    }

    @Override
    public List<RelationshipDefinition> getCategoryMetaData(long idCategory) {
        return relationshipDefinitionDAO.getRelationshipDefinitionsByCategory(idCategory);
    }

    @Override
    public void persist(Category category) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_category.create_category(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, category.getName());
            call.setString(3, category.getNameAbbreviated());
            call.setBoolean(4, category.isRestriction());
            call.setLong(5, category.getTagSemantikos().getId());
            call.setString(6, category.getColor());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                long catID = rs.getLong(1);
                category.setId(catID);
                if (!categoryMapByID.containsKey(catID)) {
                    categoryMapByID.put(catID, category);
                }
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Error al crear la categoría:" + category, e);

        }
    }

    @Override
    public List<Category> getRelatedCategories(Category category) {
        List<Category> categories = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_category.get_related_category(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,category.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                categories.add(createCategoryFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Category createCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        long idCategory = resultSet.getLong("id");
        String nameCategory = resultSet.getString("name");
        String nameAbbreviated = resultSet.getString("name_abreviated");
        boolean restriction = resultSet.getBoolean("restriction");
        String color = resultSet.getString("name_abreviated");
        long idTagSMTK = resultSet.getLong("tag_semantikos");
        TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);

        return new Category(idCategory, nameCategory, nameAbbreviated, restriction, color, tagSMTKByID);
    }
}

