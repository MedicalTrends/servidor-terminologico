package cl.minsal.semantikos.kernel.daos.mappers;

import java.sql.ResultSet;

/**
 * Created by root on 28-06-17.
 */
public interface ObjectMapper {

    Object createObjectFromResultSet(ResultSet resultSet);
}
