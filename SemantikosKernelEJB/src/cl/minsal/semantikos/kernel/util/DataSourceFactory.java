package cl.minsal.semantikos.kernel.util;

import oracle.jdbc.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by root on 12-04-17.
 */
public class DataSourceFactory {

    static private final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    private static final DataSourceFactory instance = new DataSourceFactory();

    private DataSource dataSource;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private DataSourceFactory() {

    }

    public static DataSourceFactory getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Error al conectarse a BD", e);
        }
        return null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
