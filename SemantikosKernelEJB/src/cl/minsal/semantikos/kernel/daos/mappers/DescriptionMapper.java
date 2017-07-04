package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumnFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by root on 28-06-17.
 */
public class DescriptionMapper {

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
    public static DescriptionType createDescriptionTypeFromResultSet(ResultSet rs) {

        DescriptionType descriptionType = new DescriptionType();

        try {
            descriptionType.setId(rs.getLong("id"));
            descriptionType.setName(rs.getString("name"));
            descriptionType.setDescription(rs.getString("description"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descriptionType;

    }
}
