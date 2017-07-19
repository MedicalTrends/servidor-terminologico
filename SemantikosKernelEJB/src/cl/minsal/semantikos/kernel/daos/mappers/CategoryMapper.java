package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.kernel.daos.TagDAO;
import cl.minsal.semantikos.kernel.daos.TagSMTKDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class CategoryMapper {

    @EJB
    TagSMTKDAO tagSMTKDAO;

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
