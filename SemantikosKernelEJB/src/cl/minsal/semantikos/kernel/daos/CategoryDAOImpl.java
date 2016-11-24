package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.TagSMTK;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
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
    private Map<Long, Category> categoryMap;

    public CategoryDAOImpl() {
        this.categoryMap = new HashMap<>();
    }

    @Override
    public Category getCategoryById(long idCategory) {

        /* Si está en el caché, se retorna */
        if(categoryMap.containsKey(idCategory)){
            return categoryMap.get(idCategory);
        }

        /* Se almacena en el caché */
        Category categoryByIdFromDB = getCategoryByIdFromDB(idCategory);
        categoryMap.put(idCategory, categoryByIdFromDB);

        /* Y se carga su metadata */
        List<RelationshipDefinition> categoryMetaData = getCategoryMetaData(idCategory);
        categoryByIdFromDB.setRelationshipDefinitions(categoryMetaData);

        return categoryByIdFromDB;
    }


    @Override
    public Category getCategoryByName(String name) {
        for ( Category category : categoryMap.values() ) {
            if ( category.getName().equalsIgnoreCase(name) ) {
                return category;
            }
        }

        Category category = getCategoryByNameFromDB(name);
        if ( category != null ) {
            categoryMap.put(category.getId(), category);
            List<RelationshipDefinition> categoryMetaData = getCategoryMetaData(category.getId());
            category.setRelationshipDefinitions(categoryMetaData);
        }

        return category;
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
        ConnectionBD connect = new ConnectionBD();
        String GET_CATEGORY_BY_ID = "{call semantikos.get_category_by_id(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_CATEGORY_BY_ID)) {

            call.setLong(1, idCategory);
            call.execute();

            ResultSet rs = call.getResultSet();
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

    private Category getCategoryByNameFromDB(String name) {
        Category category;
        ConnectionBD connect = new ConnectionBD();
        String GET_CATEGORY_BY_NAME = "{call semantikos.get_category_by_name(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_CATEGORY_BY_NAME)) {

            call.setString(1, name);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                category = createCategoryFromResultSet(rs);
            } else {
                return null;
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "error en getCategoryByName = " + name;
            logger.error(errorMsg, name, e);
            throw new EJBException(errorMsg, e);
        }

        return category;
    }

    @Override
    public List<Category> getAllCategories() {
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

                if (!categoryMap.containsKey(id)){
                    categoryMap.put(id, category);
                }
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return categories;
    }

    private Category createCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        long idCategory = resultSet.getLong("idcategory");
        String nameCategory = resultSet.getString("namecategory");
        String nameAbbreviated = resultSet.getString("nameabbreviated");
        boolean restriction = resultSet.getBoolean("restriction");
        boolean active = resultSet.getBoolean("active");
        String color = resultSet.getString("nameabbreviated");

        long idTagSMTK = resultSet.getLong("tag");
        TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);

        return new Category(idCategory, nameCategory, nameAbbreviated, restriction, active, color, tagSMTKByID);
    }

    @Override
    public List<RelationshipDefinition> getCategoryMetaData(long idCategory) {
        return relationshipDefinitionDAO.getRelationshipDefinitionsByCategory(idCategory);
    }

    @Override
    public void persist(Category category) {
        ConnectionBD connect = new ConnectionBD();
        String CREATE_CATEGORY = "{call semantikos.create_category(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(CREATE_CATEGORY)) {

            call.setString(1, category.getName());
            call.setString(2, category.getNameAbbreviated());
            call.setBoolean(3, category.isRestriction());
            call.setLong(4, category.getTagSemantikos().getId());
            call.setBoolean(5, category.isValid());
            call.setString(6, category.getColor());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                long catID = rs.getLong(1);
                category.setId(catID);
                if (!categoryMap.containsKey(catID)) {
                    categoryMap.put(catID, category);
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

        ConnectionBD connect = new ConnectionBD();

        CallableStatement call;

        try (Connection connection = connect.getConnection();) {

            call = connection.prepareCall("{call semantikos.get_related_category(?)}");
            call.setLong(1,category.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                categories.add(createCategoryFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}

