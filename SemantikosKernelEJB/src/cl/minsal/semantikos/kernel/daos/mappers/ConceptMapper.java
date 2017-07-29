package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class ConceptMapper {


    @EJB
    DescriptionDAO descriptionDAO;

    @EJB
    TagDAO tagDAO;

    @EJB
    RefSetDAO refSetDAO;

    /**
     * Este método es responsable de crear un concepto SMTK a partir de un resultset.
     *
     * @param resultSet El resultset a partir del cual se obtienen los conceptos.
     * @return La lista de conceptos contenidos en el ResultSet.
     * @throws SQLException Se arroja si hay un problema SQL.
     */
    public ConceptSMTK createConceptSMTKFromResultSet(ResultSet resultSet) throws SQLException {

        long id;
        long idCategory;
        Category objectCategory;
        boolean check;
        boolean consult;
        boolean modeled;
        boolean completelyDefined;
        boolean published;
        String conceptId;
        boolean heritable = false;

        id = Long.valueOf(resultSet.getString("id"));
        conceptId = resultSet.getString("conceptid");

        /* Se recupera la categoría como objeto de negocio */
        idCategory = Long.valueOf(resultSet.getString("id_category"));
        //objectCategory = categoryDAO.getCategoryById(idCategory);
        objectCategory = CategoryFactory.getInstance().findCategoryById(idCategory);

        check = resultSet.getBoolean("is_to_be_reviewed");
        consult = resultSet.getBoolean("is_to_be_consultated");
        modeled = resultSet.getBoolean("is_modeled");
        completelyDefined = resultSet.getBoolean("is_fully_defined");
        published = resultSet.getBoolean("is_published");
        conceptId = resultSet.getString("conceptid");
        String observation = resultSet.getString("observation");
        long idTagSMTK = resultSet.getLong("id_tag_smtk");

        /**
         * Try y catch ignored porque no todas las funciones de la BD que recuperan Concepts de la BD traen esta
         * columna.
         * Ej: Usar la funcion semantikos.find_concepts_by_refset_paginated para recueprar conceptos se cae con la
         * excepcion:
         * org.postgresql.util.PSQLException: The column name is_inherited was not found in this ResultSet.
         */
        try {
            heritable = resultSet.getBoolean("is_inherited");
        } catch (Exception ignored) {
        }

        /* Se recupera su Tag Semántikos */
        //TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);
        TagSMTK tagSMTKByID = TagSMTKFactory.getInstance().findTagSMTKById(idTagSMTK);

        ConceptSMTK conceptSMTK = new ConceptSMTK(id, conceptId, objectCategory, check, consult, modeled,
                completelyDefined, heritable, published, observation, tagSMTKByID);

        /* Se recuperan las descripciones del concepto */
        List<Description> descriptions = descriptionDAO.getDescriptionsByConcept(conceptSMTK);

        conceptSMTK.setDescriptions(descriptions);

        /* Se recuperan sus Etiquetas */
        conceptSMTK.setTags(tagDAO.getTagsByConcept(id));

        return conceptSMTK;
    }
}
