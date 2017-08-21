package cl.minsal.semantikos.kernel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Francisco Mendez on 29-06-16.
 */
@Deprecated
public class ConnectionBD {

    static private final Logger logger = LoggerFactory.getLogger(ConnectionBD.class);

    private Connection connection;

    public ConnectionBD() {

        javax.naming.InitialContext ctx;
        javax.sql.DataSource ds;

        try {
            ctx = new javax.naming.InitialContext();
            ds = (javax.sql.DataSource) ctx.lookup("java:jboss/OracleDS");
            connection = ds.getConnection();
        } catch (NamingException e) {
            logger.error("Error al buscar Datasource en Jboss", e);
        } catch (SQLException e) {
            logger.error("Error al conectarse a BD", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
