package cl.minsal.semantikos.kernel.factories;

import oracle.jdbc.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by root on 12-04-17.
 */
public class DataSourceFactory {

    static private final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    private static final DataSourceFactory instance = new DataSourceFactory();

    private static DataSource dataSource = null;

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
        } catch (Exception e) {
            logger.error("Error al obtener una conexión", e);
        }
        finally {
        }
        // Si llegamos a este punto, quiere decir que no hay conexiones disponibles en el pool (se deberia lanzar una excepcion )
        return null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
